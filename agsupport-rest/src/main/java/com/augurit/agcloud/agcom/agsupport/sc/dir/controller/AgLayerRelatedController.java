package com.augurit.agcloud.agcom.agsupport.sc.dir.controller;

import com.augurit.agcloud.agcom.agsupport.common.aspect.RestLog;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgLayerRelated;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Augurit on 2017-04-17.
 */
@Api(value = "地图资源管理",description = "地图资源管理相关接口")
@RestController
@RequestMapping("/agsupport/layerRelated")
public class AgLayerRelatedController {

    @Autowired
    private IAgLayerRelated iAgLayerRelated;

    private static Logger logger = LoggerFactory.getLogger(AgLayerRelatedController.class);

    @ApiOperation(value = "根据id获取服务图层与属性表的关联记录接口",notes = "根据id(主键)获取服务图层与属性表的关联记")
    @ApiImplicitParams({
    })
    @RestLog(value = "根据id获取服务图层与属性表的关联记录接口")
    @RequestMapping(value = "/getById",method = RequestMethod.GET)
    public ResultForm getById(String id){
        try{
            ResultForm form = iAgLayerRelated.getById(id);
            return form;
        }catch (Exception e){
            return new ContentResultForm(false, "", "获取失败");
        }
    }

    @ApiOperation(value = "分页获取服务图层与属性表的关联记录接口",notes = "分页获取服务图层与属性表的关联记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceDirLayerId",required = true, value = "服务的目录图层id", dataType = "String"),
    })
    @RestLog(value = "分页获取服务图层与属性表的关联记录接口")
    @RequestMapping(value = "/getByServiceDirLayerId",method = RequestMethod.GET)
    public ResultForm getByServiceDirLayerId( String serviceDirLayerId){
        return iAgLayerRelated.getByServiceDirLayerId(serviceDirLayerId);
    }
}
