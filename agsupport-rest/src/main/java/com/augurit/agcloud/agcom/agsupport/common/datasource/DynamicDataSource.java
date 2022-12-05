package com.augurit.agcloud.agcom.agsupport.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author zhangmingyang
 * @Description: 动态数据源缓存
 * @date 2018-12-29
 */
public class DynamicDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSource.class);
    private static final Map<Object,FutureTask<DataSource>> dataSourceMap = new ConcurrentHashMap<>();
    private DynamicDataSource(){}
    /**
     * 获取数据源集合
     * @return
     */
    public Set<Object> getDataSourceMap() {
        Set<Object> objects = dataSourceMap.keySet();
        return objects;
    }

    /**
     * 获取数据源方法，采用Future模式，避免高并发情况下创建垃圾数据源，造成资源浪费
     * @param dataSourceId
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public DataSource getDataSource(String dataSourceId) throws InterruptedException, ExecutionException {
            FutureTask<DataSource> dataSourceFuture = dataSourceMap.get(dataSourceId);
            if (dataSourceFuture == null){
                Callable<DataSource> callable = new Callable<DataSource>() {
                    @Override
                    public DataSource call() {
                        return DynamicDataSourceHelper.createDataSource(dataSourceId);
                    }
                };
                FutureTask<DataSource> newSourceFuture = new FutureTask<DataSource>(callable);
                dataSourceFuture = dataSourceMap.putIfAbsent(dataSourceId, newSourceFuture);
                if (dataSourceFuture == null){
                    dataSourceFuture = newSourceFuture;
                    dataSourceFuture.run();
                    LOGGER.info("已成功创建数据源-id:"+dataSourceId);
                }
                try{
                    return dataSourceFuture.get();
                }catch(Exception e){
                    dataSourceMap.remove(dataSourceId);
                    e.printStackTrace();
                }
            }
        DataSource dataSource = dataSourceFuture.get();
        //LOGGER.info("使用已创建的数据源-dataSourceId:"+dataSourceId);
        return dataSource;
    }

    public String getDbType(String dataSourceId) throws ExecutionException, InterruptedException {
        return ((DruidDataSource)getDataSource(dataSourceId)).getDbType();
    }

    /**
     * 关闭数据源
     * @param dataSourceId
     * @throws Exception
     */
    public void closeDatasource(String dataSourceId) throws Exception{
        if(org.apache.commons.lang.StringUtils.isNotBlank(dataSourceId)){
            FutureTask<DataSource> dataSourceFutureTask = dataSourceMap.get(dataSourceId);
            if (dataSourceFutureTask != null){
                ((DruidDataSource)getDataSource(dataSourceId)).close();
                dataSourceMap.remove(dataSourceId);
            }
        }
    }


    private static class DynamicDataSourceHolder{
        private static final DynamicDataSource instance = new DynamicDataSource();
    }

    public static final DynamicDataSource getInstance(){
        return DynamicDataSourceHolder.instance;
    }

}
