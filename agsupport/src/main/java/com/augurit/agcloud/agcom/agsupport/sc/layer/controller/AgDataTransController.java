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
 * ??????????????????????????????!!!
 * Created by Caip on 2017-05-17.
 */
@Api(value = "??????????????????",description = "??????????????????")
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
    @ApiOperation(value = "??????????????????",notes = "??????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "layer",required = true, value = "????????????", dataType = "Layer")
    })
    @RequestMapping(value = "/startDataTrans",method = RequestMethod.POST)
    public ResultForm startDataTrans(Layer layer) throws Exception{
        //String msg = iAgDataTrans.startDataTrans(layer);
        /*boolean transComplete = iAgDataTrans.isTransComplete(layer.getDataSourceId(), layer.getLayerTable());
        if (transComplete){
            return new ContentResultForm<>(true,"1","?????????????????????!");
        }*/
        // ???????????????????????????
        if (iAgDataTrans.mongoDataTranIsRun(layer.getDataSourceId(), layer.getLayerTable())){
            return new ContentResultForm(true,"2","??????????????????????????????????????????????????????");
        }
        int totalNum = dbStats.statsCount(layer);
        if (totalNum == 0){
            return new ContentResultForm(true,"1","????????????????????????????????????????????????!");
        }
        String start = iAgDataTrans.start(layer);
        if ("1".equals(start)){
            return new ContentResultForm(true,"0","????????????");
        }
        return new ContentResultForm(true,"1","?????????????????????mongodb????????????!");

    }

    @ApiOperation(value = "?????????????????????",notes = "?????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId",required = true, value = "?????????id", dataType = "String"),
            @ApiImplicitParam(name = "layerTable",required = true, value = "????????????", dataType = "String"),
    })
    @RequestMapping(value = "/stopDataTrans",method = RequestMethod.POST)
    public ResultForm stopDataTrans(String dataSourceId, String layerTable) {
        boolean transComplete = iAgDataTrans.isTransComplete(dataSourceId, layerTable);
        if (transComplete){
            return new ResultForm(true,"????????????????????????!");
        }else {
            boolean result = iAgDataTrans.stopDataTrans(dataSourceId, layerTable);
            if (result){
                return new ResultForm(result,"????????????");
            }else {
                return new ResultForm(result,"????????????");
            }
        }
    }

    @ApiOperation(value = "?????????????????????",notes = "?????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId",required = true, value = "?????????id", dataType = "String"),
            @ApiImplicitParam(name = "layerTable",required = true, value = "????????????", dataType = "String"),
    })
    @RequestMapping(value = "/clearDataTrans",method = RequestMethod.POST)
    public ResultForm clearDataTrans(String dataSourceId, String layerTable) {
        return new ResultForm(true, iAgDataTrans.clearDataTrans(dataSourceId, layerTable));
    }

    @ApiOperation(value = "????????????????????????????????????",notes = "????????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId",required = true, value = "?????????id", dataType = "String"),
            @ApiImplicitParam(name = "layerTable",required = true, value = "????????????", dataType = "String"),
    })
    @RequestMapping(value = "/getProgressMsg",method = RequestMethod.POST)
    public ContentResultForm getProgress(String dataSourceId, String layerTable) {
        String result = "0";
        if (dataSourceId != null && layerTable != null) {
            result = iAgDataTrans.getProgressMsg(dataSourceId, layerTable);
            try {
                if ("1.0".equals(result)){
                    iAgDataTrans.clearHashMap(dataSourceId,layerTable);
                    System.out.println("?????????????????????????????????!"+SpatialUtil.getKey(dataSourceId,layerTable));
                }
            } catch (Exception e) {
                return new ContentResultForm(false,result,"??????????????????????????????????????????");
            }
        }
        return new ContentResultForm(true,result,"??????????????????????????????????????????");
    }

    @ApiOperation(value = "???????????????????????????",notes = "???????????????????????????")
    @RequestMapping(value = "/getDataTransKey",method = RequestMethod.GET)
    public ContentResultForm getDataTransKey() {
        String dataTransKey = iAgDataTrans.getDataTransKey();
        return new ContentResultForm(dataTransKey != null,dataTransKey);
    }

    @ApiOperation(value = "??????redis??????",notes = "??????redis??????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cacheTable",required = true, value = "????????????", dataType = "String")
    })
    @RequestMapping(value = "/clearRedisCache",method = RequestMethod.POST)
    public ResultForm clearRedisCache(String cacheTable) {
        boolean flag = iAgDataTrans.clearRedisCache(cacheTable);
        return new ResultForm(flag);
    }

    @ApiOperation(value = "?????????????????????????????????",notes = "?????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "layerId",required = true, value = "????????????id", dataType = "String"),
            @ApiImplicitParam(name = "pks",required = true, value = "??????", dataType = "String")
    })
    @RequestMapping(value = "/updateOrSaveDataTrans",method = RequestMethod.POST)
    public ResultForm updateOrSaveDataTrans(String layerId, String pks) throws Exception {
        boolean success = false;
        AgLayer agLayer = iAgDir.findLayerByLayerId(layerId);
        if (agLayer.getLayerType().startsWith("01") && iAgDataTrans.isTransComplete(agLayer.getDataSourceId(), agLayer.getLayerTable())) {//??????????????????
            List pkList = JsonUtils.listFromJson(pks);
            Layer layer = ReflectBeans.copy(agLayer, Layer.class);
            success = iAgDataTrans.updateOrSaveByPks(layer, pkList);
        }
        return new ResultForm(success);
    }

    @ApiOperation(value = "??????mongodb??????",notes = "??????mongodb??????")
    @ApiImplicitParam(value = "mongodb????????????",name = "mongoCollection")
    @RequestMapping(value = "/clearMongoDBCache",method = RequestMethod.GET)
    public ContentResultForm clearMongoDBCache(){
        ContentResultForm resultForm = new ContentResultForm(true, "????????????");
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
            resultForm = new ContentResultForm(false,"????????????");
            e.printStackTrace();
        }
        return resultForm;
    }
    @ApiOperation(value = "???????????????????????????????????????",notes = "???????????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "?????????id",name = "dataSourceId",dataType = "String"),
            @ApiImplicitParam(value = "?????????",name = "layerTable",dataType = "String")
    })
    @RequestMapping(value = "/clearMongoHashMap",method = RequestMethod.GET)
    public ContentResultForm clearMongoHashMap(String dataSourceId,String layerTable){
        iAgDataTrans.clearHashMap(dataSourceId,layerTable);
        return new ContentResultForm(true,"????????????");
    }

}
