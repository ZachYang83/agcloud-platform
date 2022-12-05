package com.augurit.agcloud.agcom.agsupport.sc.dir.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayerRelated;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgLayerRelated;
import com.augurit.agcloud.agcom.agsupport.sc.log.service.IAgLog;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

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
    @Autowired
    private IAgLog log;
    @Value("${agcloud.opus.admin.orgId}")
    private String orgId;


    @ApiOperation(value = "获取属性表接口",notes = "根据数据源id获取属性表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId",required = true, value = "数据源id", dataType = "String")
    })
    @RequestMapping(value = "/getPropertyTablesByDataSourceId",method = RequestMethod.GET)
   public ContentResultForm getPropertyTablesByDataSourceId(String dataSourceId){
       return iAgLayerRelated.getPropertyTablesByDataSourceId(dataSourceId);
   }

    @ApiOperation(value = "获取服务字段接口",notes = "根据dirLayerId获取表字段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirLayerId",required = true, value = "属性表资源标识", dataType = "String")
    })
    @RequestMapping(value = "/getServiceFieldsByDirLayerId",method = RequestMethod.GET)
    public ContentResultForm getServiceFieldsByDirLayerId(String dirLayerId){
        return iAgLayerRelated.getServiceFieldsByDirLayerId(dirLayerId);
    }

    @ApiOperation(value = "获取表字段接口",notes = "根据dirLayerId获取表字段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirLayerId",required = true, value = "属性表资源标识", dataType = "String")
    })
    @RequestMapping(value = "/getTableFieldsByDirLayerId",method = RequestMethod.GET)
    public ContentResultForm getTableFieldsByDirLayerId(String dirLayerId){
        return iAgLayerRelated.getTableFieldsByDirLayerId(dirLayerId);
    }



    @ApiOperation(value = "新增图层属性关联记录接口",notes = "新增图层属性关联记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",required = true, value = "关联类型:0-属性表", dataType = "String"),
            @ApiImplicitParam(name = "name",required = true, value = "关联名称，用于描述该关联的作用", dataType = "String"),
            @ApiImplicitParam(name = "serviceDirLayerId",required = true, value = "服务图层资源标识", dataType = "String"),
            @ApiImplicitParam(name = "serviceField",required = true, value = "服务图层字段", dataType = "String"),
            @ApiImplicitParam(name = "relatedDirLayerId",required = true, value = "关联资源标识", dataType = "String"),
            @ApiImplicitParam(name = "relatedField",required = true, value = "关联资源字段", dataType = "String")
    })
    @RequestMapping(value = "/addLayerRelated",method = RequestMethod.POST)
    public ResultForm addLayerRelated(AgLayerRelated agLayerRelated){
        /*agLayerRelated.setServiceDirLayerId("cd94aa3a-7fb9-4aa5-bc00-a0399c3f03e8");
        agLayerRelated.setServiceField("aaa");
        agLayerRelated.setRelatedDirLayerId("9fe601a9-cb3e-48ae-b003-55dce5b7e032");
        agLayerRelated.setRelatedField("bbb");*/
        return iAgLayerRelated.addLayerRelated(agLayerRelated);
    }

    @ApiOperation(value = "删除图层属性关联记录接口",notes = "删除图层属性关联记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",required = true, value = "关联标识", dataType = "String")
    })
    @RequestMapping(value = "/deleteLayerRelated",method = RequestMethod.DELETE)
    public ResultForm deleteLayerRelated(String ids){
        if(ids!=null&& ids.contains(",")){
            List<String> list = Arrays.asList(ids.split(","));
            return iAgLayerRelated.deleteLayerRelated(list);
        }
        else {
            return iAgLayerRelated.deleteLayerRelated(ids);
        }
    }

    @ApiOperation(value = "修改图层属性关联记录接口",notes = "修改图层属性关联记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",required = true, value = "关联标识", dataType = "String"),
            @ApiImplicitParam(name = "type",required = true, value = "关联类型:0-属性表", dataType = "String"),
            @ApiImplicitParam(name = "name",required = true, value = "关联名称，用于描述该关联的作用", dataType = "String"),
            @ApiImplicitParam(name = "ServiceDirLayerId",required = true, value = "服务图层资源标识", dataType = "String"),
            @ApiImplicitParam(name = "ServiceField",required = true, value = "服务图层字段", dataType = "String"),
            @ApiImplicitParam(name = "RelatedDirLayerId",required = true, value = "关联资源标识", dataType = "String"),
            @ApiImplicitParam(name = "RelatedField",required = true, value = "关联资源字段", dataType = "String")
    })
    @RequestMapping(value = "/editLayerRelated",method = RequestMethod.POST)
    public ResultForm editLayerRelated(AgLayerRelated agLayerRelated){
        /*agLayerRelated.setId("8d300c3f-add3-4732-9c5a-8f195fa0ec2e");
        agLayerRelated.setServiceDirLayerId("cd94aa3a-7fb9-4aa5-bc00-a0399c3f03e1");
        agLayerRelated.setServiceField("bbb");
        agLayerRelated.setRelatedDirLayerId("9fe601a9-cb3e-48ae-b003-55dce5b7e032");
        agLayerRelated.setRelatedField("ccc");*/
        return iAgLayerRelated.editLayerRelated(agLayerRelated);
    }

    @ApiOperation(value = "分页获取服务图层与属性表的关联记录接口",notes = "分页获取服务图层与属性表的关联记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "serviceDirLayerId",required = true, value = "服务的目录图层id", dataType = "String"),
            @ApiImplicitParam(name = "pageNum",required = true, value = "页码", dataType = "int"),
            @ApiImplicitParam(name = "pageSize",required = true, value = "每页显示记录数量", dataType = "int")
    })
    @RequestMapping(value = "/getByServiceDirLayerId",method = RequestMethod.GET)
    public ResultForm getByServiceDirLayerId(Page page,String serviceDirLayerId){
        return iAgLayerRelated.getByServiceDirLayerId(page,serviceDirLayerId);
    }

    @ApiOperation(value = "根据id获取服务图层与属性表的关联记录接口",notes = "根据id(主键)获取服务图层与属性表的关联记")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/getById",method = RequestMethod.GET)
    public ResultForm getById(String id){
        return iAgLayerRelated.getById(id);
    }

}
