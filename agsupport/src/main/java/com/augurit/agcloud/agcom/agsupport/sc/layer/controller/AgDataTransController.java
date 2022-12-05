package com.augurit.agcloud.agcom.agsupport.sc.layer.controller;

import com.augurit.agcloud.agcom.agsupport.common.mongodb.MongoDbService;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.sc.coordTrans.util.CoordTransUtil;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.func.service.IAgFunc;
import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgDataTrans;
import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgLayer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.stats.service.IAgDBStats;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.SpatialUtil;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.bsc.upload.MongoDbAchieve;
import com.augurit.agcloud.bsc.upload.UploadFileStrategy;
import com.augurit.agcloud.bsc.upload.UploadType;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.common.util.ReflectBeans;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-05-17.
 */
@Api(value = "数据缓冲接口",description = "数据缓冲接口")
@RestController
@RequestMapping("/agsupport/dataTrans")
public class AgDataTransController {
    @Autowired
    private IAgDataTrans iAgDataTrans;
    @Autowired
    private IAgDir iAgDir;
    @Autowired
    private IAgLayer iAgLayer;
    @Autowired
    private MongoDbService mongoDbService;

    @RequestMapping(value = "/index.html")
    public ModelAndView index(HttpServletRequest request, Model model) {

        return new ModelAndView("agcloud/agcom/agsupport/dataTrans/index");
    }
    @Autowired
    private IAgDBStats dbStats;
    @ApiOperation(value = "开始数据缓冲",notes = "开始数据缓冲")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "layer",required = true, value = "矢量图层", dataType = "Layer")
    })
    @RequestMapping(value = "/startDataTrans",method = RequestMethod.POST)
    public ResultForm startDataTrans(Layer layer) throws Exception{
        //String msg = iAgDataTrans.startDataTrans(layer);
        /*boolean transComplete = iAgDataTrans.isTransComplete(layer.getDataSourceId(), layer.getLayerTable());
        if (transComplete){
            return new ContentResultForm<>(true,"1","已经缓冲完成了!");
        }*/
        // 服务器是否正在缓冲
        if (iAgDataTrans.mongoDataTranIsRun(layer.getDataSourceId(), layer.getLayerTable())){
            return new ContentResultForm(true,"2","服务器正在进行数据缓冲，请稍后再试！");
        }
        int totalNum = dbStats.statsCount(layer);
        if (totalNum == 0){
            return new ContentResultForm(true,"1","图层没有数据进行缓冲，请检查图层!");
        }
        String start = iAgDataTrans.start(layer);
        if ("1".equals(start)){
            return new ContentResultForm(true,"0","开始缓冲");
        }
        return new ContentResultForm(true,"1","请检查图层或者mongodb配置信息!");

    }

    @ApiOperation(value = "开停止数据缓冲",notes = "开停止数据缓冲")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId",required = true, value = "数据源id", dataType = "String"),
            @ApiImplicitParam(name = "layerTable",required = true, value = "图层表名", dataType = "String"),
    })
    @RequestMapping(value = "/stopDataTrans",method = RequestMethod.POST)
    public ResultForm stopDataTrans(String dataSourceId, String layerTable) {
        boolean transComplete = iAgDataTrans.isTransComplete(dataSourceId, layerTable);
        if (transComplete){
            return new ResultForm(true,"数据已缓冲完毕了!");
        }else {
            boolean result = iAgDataTrans.stopDataTrans(dataSourceId, layerTable);
            if (result){
                return new ResultForm(result,"停止成功");
            }else {
                return new ResultForm(result,"停止失败");
            }
        }
    }

    @ApiOperation(value = "清除缓冲的数据",notes = "清除缓冲的数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId",required = true, value = "数据源id", dataType = "String"),
            @ApiImplicitParam(name = "layerTable",required = true, value = "图层表名", dataType = "String"),
    })
    @RequestMapping(value = "/clearDataTrans",method = RequestMethod.POST)
    public ResultForm clearDataTrans(String dataSourceId, String layerTable) {
        return new ResultForm(true, iAgDataTrans.clearDataTrans(dataSourceId, layerTable));
    }

    @ApiOperation(value = "获取图层数据清洗进度信息",notes = "获取图层数据清洗进度信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId",required = true, value = "数据源id", dataType = "String"),
            @ApiImplicitParam(name = "layerTable",required = true, value = "图层表名", dataType = "String"),
    })
    @RequestMapping(value = "/getProgressMsg",method = RequestMethod.POST)
    public ContentResultForm getProgress(String dataSourceId, String layerTable) {
        String result = "0";
        if (dataSourceId != null && layerTable != null) {
            result = iAgDataTrans.getProgressMsg(dataSourceId, layerTable);
            try {
                if ("1.0".equals(result)){
                    iAgDataTrans.clearHashMap(dataSourceId,layerTable);
                    System.out.println("缓冲完成，清除缓冲信息!"+SpatialUtil.getKey(dataSourceId,layerTable));
                }
            } catch (Exception e) {
                return new ContentResultForm(false,result,"获取图层数据清洗进度信息失败");
            }
        }
        return new ContentResultForm(true,result,"获取图层数据清洗进度信息成功");
    }

    @ApiOperation(value = "获取正在清洗的图层",notes = "获取正在清洗的图层")
    @RequestMapping(value = "/getDataTransKey",method = RequestMethod.GET)
    public ContentResultForm getDataTransKey() {
        String dataTransKey = iAgDataTrans.getDataTransKey();
        return new ContentResultForm(dataTransKey != null,dataTransKey);
    }

    @ApiOperation(value = "清除redis缓存",notes = "清除redis缓存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cacheTable",required = true, value = "图层表名", dataType = "String")
    })
    @RequestMapping(value = "/clearRedisCache",method = RequestMethod.POST)
    public ResultForm clearRedisCache(String cacheTable) {
        boolean flag = iAgDataTrans.clearRedisCache(cacheTable);
        return new ResultForm(flag);
    }

    @ApiOperation(value = "批量更新或新增缓冲数据",notes = "批量更新或新增缓冲数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "layerId",required = true, value = "矢量图层id", dataType = "String"),
            @ApiImplicitParam(name = "pks",required = true, value = "主键", dataType = "String")
    })
    @RequestMapping(value = "/updateOrSaveDataTrans",method = RequestMethod.POST)
    public ResultForm updateOrSaveDataTrans(String layerId, String pks) throws Exception {
        boolean success = false;
        AgLayer agLayer = iAgDir.findLayerByLayerId(layerId);
        if (agLayer.getLayerType().startsWith("01") && iAgDataTrans.isTransComplete(agLayer.getDataSourceId(), agLayer.getLayerTable())) {//矢量图层判断
            List pkList = JsonUtils.listFromJson(pks);
            Layer layer = ReflectBeans.copy(agLayer, Layer.class);
            success = iAgDataTrans.updateOrSaveByPks(layer, pkList);
        }
        return new ResultForm(success);
    }

    @ApiOperation(value = "清除mongodb数据",notes = "清除mongodb数据")
    @ApiImplicitParam(value = "mongodb集合名称",name = "mongoCollection")
    @RequestMapping(value = "/clearMongoDBCache",method = RequestMethod.GET)
    public ContentResultForm clearMongoDBCache(){
        ContentResultForm resultForm = new ContentResultForm(true, "删除成功");
        try {
            MongoTemplate mongoTemplate = mongoDbService.getMongoTemplate();
            List<AgLayer> vectorLayer = iAgLayer.getVectorLayer();
            for (AgLayer agLayer : vectorLayer){
                JSONObject data = JSONObject.fromObject(agLayer.getData());
                String dataSourceId = data.get("dataSourceId").toString();
                if (StringUtils.isNotBlank(dataSourceId)){
                    String layerTable = agLayer.getLayerTable();
                    boolean transComplete = iAgDataTrans.isTransComplete(dataSourceId, layerTable);
                    if (transComplete){
                        String collection = SpatialUtil.getKey(dataSourceId, layerTable);
                        mongoTemplate.dropCollection(collection);
                    }
                }
            }
            mongoTemplate.dropCollection(SpatialConfig.DATA_TRANS_LOG_TABLE);
            return resultForm;
        }catch (Exception e){
            resultForm = new ContentResultForm(false,"清除出错");
            e.printStackTrace();
        }
        return resultForm;
    }
    @ApiOperation(value = "清除数据缓冲完成的缓存标志",notes = "清除数据缓冲完成的缓存标志")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "数据源id",name = "dataSourceId",dataType = "String"),
            @ApiImplicitParam(value = "数据表",name = "layerTable",dataType = "String")
    })
    @RequestMapping(value = "/clearMongoHashMap",method = RequestMethod.GET)
    public ContentResultForm clearMongoHashMap(String dataSourceId,String layerTable){
        iAgDataTrans.clearHashMap(dataSourceId,layerTable);
        return new ContentResultForm(true,"清除成功");
    }

}
