package com.augurit.agcloud.agcom.agsupport.sc.layer.services.impl;

import com.augurit.agcloud.agcom.agsupport.common.datasource.JDBCUtils;
import com.augurit.agcloud.agcom.agsupport.common.mongodb.MongoDbService;
import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgDataTrans;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Range;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.query.dao.AgDBQueryDao;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.query.service.IAgDBQuery;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.stats.service.IAgDBStats;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.MongoDBHelp;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.MongoDBUtil;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.SpatialUtil;
import com.common.thread.Job;
import com.common.util.Common;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.*;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-05-02.
 */
@Service
public class AgDataTransImpl implements IAgDataTrans {

    private static Logger logger = LoggerFactory.getLogger(AgDataTransImpl.class);
    //正在缓冲的图层表名
    private final DataTransListener dataTransListener = new DataTransListener();
    @Autowired
    private IAgDBQuery dbQuery;
    @Autowired
    private AgDBQueryDao dbQueryDao;
    @Autowired
    private IAgDBStats dbStats;
    @Autowired
    private StringRedisTemplate redis;
    @Autowired
    private MongoDbService mongoDbService;

    private static final Map<Object,FutureTask<String>> concurrentHashMap = new ConcurrentHashMap<>();
    public String start(Layer layer) {
        String key = SpatialUtil.getKey(layer.getDataSourceId(), layer.getLayerTable());
        FutureTask<String> longFutureTask = concurrentHashMap.get(key);
        String msg = "";
        if (longFutureTask == null){
            Callable<String> callable = new Callable<String>() {
                @Override
                public String call() {
                    String msg = startDataTrans(layer);
                    return msg;
                }
            };
            FutureTask<String> newSourceFuture = new FutureTask<String>(callable);
            longFutureTask = concurrentHashMap.putIfAbsent(key, newSourceFuture);
            if (longFutureTask == null){
                longFutureTask = newSourceFuture;
                Thread thread = new Thread(longFutureTask);
                thread.start();
            }
            msg = "1";
        }
        return msg;
    }

    public void clearHashMap(String dataSourceId,String layerTable){
        if (concurrentHashMap.get(SpatialUtil.getKey(dataSourceId,layerTable)) != null){
            concurrentHashMap.remove(SpatialUtil.getKey(dataSourceId,layerTable));
        }
    }
    public boolean mongoDataTranIsRun(String dataSourceId,String layerTable){
        boolean f = false;
        FutureTask<String> stringFutureTask = concurrentHashMap.get(SpatialUtil.getKey(dataSourceId, layerTable));
        if (stringFutureTask != null){
            // stringFutureTask不为空，并且返回false,说明任务正在执行
            boolean flag = concurrentHashMap.get(SpatialUtil.getKey(dataSourceId,layerTable)).isDone();
            if (!flag){
                f = true;
            }
        }
        return f;
    }

    @Override
    public synchronized String startDataTrans(final Layer layer) {
        String msg = MSG_1;
        try {
            if (isUseMDB()) {
                if (dataTransListener.getDataTransKey() == null) {
                    long startMili = System.currentTimeMillis();
                    logger.info("------开始缓冲数据------");
                    String key = SpatialUtil.getKey(layer.getDataSourceId(), layer.getLayerTable());
                    dataTransListener.setDataTransKey(key);
                    //清除原有数据
                    deleteData(layer.getDataSourceId(), layer.getLayerTable());
                    int totalNum = dbStats.statsCount(layer);
                    dataTransListener.setTotalNum(totalNum);
                    String sql = dbQueryDao.getQuerySql(layer);
                    Map<String, Integer> result = new HashMap<String, Integer>();
                    final String pkColumn = layer.getPkColumn();
                    // 2019.01.25使用新的数据源连接池
                    JDBCUtils.each(layer.getDataSourceId(), sql, new ArrayList(), 1000, new Job(result) {
                        @Override
                        public Object execute() {
                            Map dataMap = (Map) getData()[0];
                            Map<String, Integer> result = (Map<String, Integer>) getArgs()[0];
                            try {
                                String primaryKey = Common.checkNull(dataMap.get(pkColumn));
                                String wkt = Common.checkNull(dataMap.get(WKT_COLUMN));
                                Range range = SpatialUtil.getRangeByWKT(wkt);
                                Document doc = new Document(PK_COLUMN, primaryKey);
                                for (Object key : dataMap.keySet()) {
                                    doc.append(Common.checkNull(key), Common.checkNull(dataMap.get(key)));
                                }
                                doc.append(MINX_COLUMN, range.getMinX()).append(MAXX_COLUMN, range.getMaxX())
                                        .append(MINY_COLUMN, range.getMinY()).append(MAXY_COLUMN, range.getMaxY())
                                        .append(CREATETIME_COLUMN, sdf.format(new Date()));
                                if (dataTransListener.isStop()) {
                                    result.put("result", 3);
                                    this.getExecuter().setStop(true);
                                } else if (!mongoDbService.insertOne(doc,key)) {
                                    result.put("result", 2);
                                    this.getExecuter().setStop(true);
                                } else {
                                    result.put("result", 1);
                                }
                            } catch (Exception e) {
                                result.put("result", 2);
                                this.getExecuter().setStop(true);
                            }
                            return null;
                        }
                    });
                    long endMili = System.currentTimeMillis();
                    Integer r = Common.checkInt(result.get("result"),0);
                    if (r == 1) {
                        Document doc = new Document(PK_COLUMN, SpatialUtil.getKey(layer.getDataSourceId(), layer.getLayerTable()))
                                .append(CREATETIME_COLUMN, sdf.format(new Date()));
                        //MongoDBHelp.insertOne(doc, DATA_TRANS_LOG_TABLE);
                        mongoDbService.insertOne(doc,DATA_TRANS_LOG_TABLE);
                        //msg = MSG_1;
                        msg = "缓冲成功！本次成功缓冲" + totalNum + "条数据，共耗时：" + (endMili - startMili) / 1000.0 + "秒";
                        logger.info("------结束缓冲数据------");
                        logger.info("------" + msg);
                    } else if (r == 0 || r == 2) {
                        //缓冲失败则清除垃圾数据
                        logger.info("------结束缓冲数据------");
                        deleteData(layer.getDataSourceId(), layer.getLayerTable());
                        msg = MSG_2;
                    } else if (r == 3) {
                        //停止缓冲数据 清除垃圾数据
                        logger.info("------停止缓冲数据------");
                        deleteData(layer.getDataSourceId(), layer.getLayerTable());
                        msg = MSG_3;
                    }
                } else {
                    msg = MSG_4;
                }
            } else {
                msg = MSG_10;
            }
        } catch (Exception e) {
            deleteData(layer.getDataSourceId(), layer.getLayerTable());
            msg = MSG_2;
        } finally {
            //初始化值
            dataTransListener.setDataTransKey(null);
            dataTransListener.setTotalNum(0);
            dataTransListener.setStop(false);
            logger.info(msg);
            clearHashMap(layer.getDataSourceId(),layer.getLayerTable());
        }
        return msg;
    }

    @Override
    public boolean stopDataTrans(String dataSourceId, String layerTable) {
        String key = SpatialUtil.getKey(dataSourceId, layerTable);
        if (key.equals(dataTransListener.getDataTransKey())) {
            dataTransListener.setStop(true);
            return true;
        }
        return false;
    }

    @Override
    public String clearDataTrans(String dataSourceId, String layerTable) {
        String msg;
        String key = SpatialUtil.getKey(dataSourceId, layerTable);
        if (isUseMDB()) {
            if (key.equals(dataTransListener.getDataTransKey())) {
                msg = MSG_5;
            } else {
                if (deleteData(dataSourceId, layerTable)) {
                    msg = MSG_6;
                } else {
                    msg = MSG_7;
                }
            }
        } else {
            msg = MSG_10;
        }
        return msg;
    }

    @Override
    public double getProgress(String dataSourceId, String layerTable) {
        double present = 0;
        try {
            if (isUseMDB()) {
                String key = SpatialUtil.getKey(dataSourceId, layerTable);
                if (key.equals(dataTransListener.getDataTransKey())) {
                    int totalNum = dataTransListener.getTotalNum();
                    long transNum = mongoDbService.count(key);
                    //int transNum = MongoDBHelp.getCount(new Document(), key);
                    if (totalNum == 0 || transNum == 0) {
                        present = 0;
                    } else {
                        present = transNum / totalNum;
                    }
                } else if (isTransComplete(dataSourceId, layerTable)) {
                    present = 1.0;
                } else {
                    present = 0.0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return present;
    }

    @Override
    public String getProgressMsg(String dataSourceId, String layerTable) {
        String present = "0.0";
        try {
            if (isUseMDB()) {
                String key = SpatialUtil.getKey(dataSourceId, layerTable);
                if (key.equals(dataTransListener.getDataTransKey())) {
                    int totalNum = dataTransListener.getTotalNum();
                    long transNum = mongoDbService.count(key);
                    if (totalNum == 0) {
                        present = MSG_8;
                    } else if (transNum == 0) {
                        present = MSG_9;
                    } else {
                        present = transNum + "/" + totalNum;
                    }
                } else if (isTransComplete(dataSourceId, layerTable)) {
                    present = "1.0";
                } else {
                    present = "0.0";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return present;
    }

    @Override
    public String getDataTransKey() {
        return dataTransListener.getDataTransKey();
    }

    @Override
    public boolean isTransComplete(String dataSourceId, String layerTable) {
        boolean result = false;
        if (isUseMDB()) {
            try {
                String key = SpatialUtil.getKey(dataSourceId, layerTable);
                //Document doc = new Document(PK_COLUMN, key);
                MongoTemplate mongoTemplate = mongoDbService.getMongoTemplate();

                //result = MongoDBHelp.getCount(doc, DATA_TRANS_LOG_TABLE) > 0;
                Query query = new Query();
                Criteria criteria = Criteria.where(PK_COLUMN).is(key);
                query.addCriteria(criteria);
                result = mongoTemplate.count(query,DATA_TRANS_LOG_TABLE) > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean updateOrSaveByPk(Layer layer, String primaryKey) {
        boolean result = false;
        if (isUseMDB()) {
            try {
                String pkColumn = layer.getPkColumn();
                String key = SpatialUtil.getKey(layer.getDataSourceId(), layer.getLayerTable());
                Map resultMap = dbQuery.queryByPk(layer, primaryKey);
                if (resultMap != null && resultMap.get(pkColumn) != null) {
                    MongoDBHelp.deleteMany(new Document(PK_COLUMN, primaryKey), key);
                    String wkt = Common.checkNull(resultMap.get(WKT_COLUMN));
                    Range range = SpatialUtil.getRangeByWKT(wkt);
                    Document doc = new Document(PK_COLUMN, primaryKey);
                    for (Object k : resultMap.keySet()) {
                        doc.append(Common.checkNull(k), Common.checkNull(resultMap.get(k)));
                    }
                    doc.append(MINX_COLUMN, range.getMinX()).append(MAXX_COLUMN, range.getMaxX())
                            .append(MINY_COLUMN, range.getMinY()).append(MAXY_COLUMN, range.getMaxY())
                            .append(CREATETIME_COLUMN, sdf.format(new Date()));
                    MongoDBHelp.insertOne(doc, key);
                    result = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean updateOrSaveByPks(Layer layer, List<String> pkList) {
        boolean result = false;
        if (isUseMDB() && pkList != null && pkList.size() > 0) {
            try {
                String pkColumn = layer.getPkColumn();
                String key = SpatialUtil.getKey(layer.getDataSourceId(), layer.getLayerTable());
                List<Document> docs = new ArrayList();
                MongoDBUtil db = new MongoDBUtil(key);
                List<Map> resultMaps = dbQuery.queryByPks(layer, pkList);
                for (int i = 0; i < resultMaps.size(); i++) {
                    Map data = resultMaps.get(i);
                    String primaryKey = Common.checkNull(data.get(pkColumn));
                    db.deleteMany(new Document(PK_COLUMN, primaryKey));
                    String wkt = Common.checkNull(data.get(WKT_COLUMN));
                    Range range = SpatialUtil.getRangeByWKT(wkt);
                    Document doc = new Document(PK_COLUMN, primaryKey);
                    for (Object k : data.keySet()) {
                        doc.append(Common.checkNull(k), Common.checkNull(data.get(k)));
                    }
                    doc.append(MINX_COLUMN, range.getMinX()).append(MAXX_COLUMN, range.getMaxX())
                            .append(MINY_COLUMN, range.getMinY()).append(MAXY_COLUMN, range.getMaxY())
                            .append(CREATETIME_COLUMN, sdf.format(new Date()));
                    docs.add(doc);
                    if (docs.size() >= INSERT_NUM || i == resultMaps.size() - 1) {
                        result = db.insertMany(docs);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    @Override
    public boolean deleteData(String dataSourceId, String layerTable) {
        boolean result = false;
        if (isUseMDB()) {
            try {
                String key = SpatialUtil.getKey(dataSourceId, layerTable);
                MongoTemplate mongoTemplate = mongoDbService.getMongoTemplate();
                mongoTemplate.dropCollection(key);
                Query query = new Query();
                query.addCriteria(Criteria.where(PK_COLUMN).is(key));
                mongoTemplate.remove(query, DATA_TRANS_LOG_TABLE);
                //MongoDBHelp.deleteMany(new Document(), key);
                //MongoDBHelp.deleteMany(new Document(PK_COLUMN, key), DATA_TRANS_LOG_TABLE);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean deleteDataByPk(String dataSourceId, String layerTable, String primaryKey) {
        boolean result = false;
        if (isUseMDB()) {
            try {
                String key = SpatialUtil.getKey(dataSourceId, layerTable);
                MongoDBHelp.deleteMany(new Document(PK_COLUMN, primaryKey), key);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private boolean isUseMDB() {
        boolean flag = true;
        try {
            MongoTemplate mongoTemplate = mongoDbService.getMongoTemplate();
            if (mongoTemplate == null){
                flag = false;
            }
        }catch (Exception e){
            flag = false;
            e.printStackTrace();
        }

        return flag;
    }

    class DataTransListener {
        private String dataTransKey;
        private int totalNum = 0;
        private boolean isStop = false;

        public String getDataTransKey() {
            return dataTransKey;
        }

        public void setDataTransKey(String dataTransKey) {
            this.dataTransKey = dataTransKey;
        }

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }

        public boolean isStop() {
            return isStop;
        }

        public void setStop(boolean stop) {
            isStop = stop;
        }
    }

    @Override
    public boolean clearRedisCache(String cacheTable) {
        boolean flag = false;
        String keyPatten;
        Set<String> keys;
        try {
            if (cacheTable == null || "".equals(cacheTable)) {
                keyPatten = "*";
            } else {
                keyPatten = cacheTable.endsWith(":") ? cacheTable + "*" : cacheTable + ":*";
            }
            keys = redis.keys(keyPatten);
            redis.delete(keys);
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
