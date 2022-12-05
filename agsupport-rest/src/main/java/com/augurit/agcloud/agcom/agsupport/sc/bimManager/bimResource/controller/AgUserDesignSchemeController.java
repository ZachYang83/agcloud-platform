package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.controller;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.AgUserDesignParam;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserDesignMaterials;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserDesignScheme;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.IAgUserDesignSchemeService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
@RestController
@RequestMapping("/agsupport/BIM/scheme/")
@Api(value = "用户方案设计接口", description = "用户方案设计接口")
public class AgUserDesignSchemeController {
    private static final Logger logger = LoggerFactory.getLogger(AgUserDesignSchemeController.class);
    @Autowired
    private IAgUserDesignSchemeService schemeService;

    @ApiOperation(value = "用户方案设计列表",notes = "用户方案设计列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "方案名称查询", dataType = "String"),
            @ApiImplicitParam(name = "isDefault", value = "默认方案查询（isDefault=1默认方案；isDefault=0非默认方案；参数不传两者都有）", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "分页参数:page=1&rows=10", dataType = "int"),
    })
    @GetMapping("/find")
    public ContentResultForm find(AgUserDesignScheme scheme, Page page){
        try{
            PageInfo<AgUserDesignScheme> list = schemeService.list(scheme, page);
            return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(list));
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
    }

    @ApiOperation(value = "添加方案",notes = "添加方案")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "方案名称", dataType = "String"),
            @ApiImplicitParam(name = "landInfo", value = "地块信息", dataType = "String"),
            @ApiImplicitParam(name = "location", value = "保存位置", dataType = "String"),
            @ApiImplicitParam(name = "description", value = "方案简介", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "file", value = "缩略图", dataType = "file"),

            @ApiImplicitParam(name = "materials[0].type", value = "模型类型（1房屋模型；2小品模型；3构件模型）", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].name", value = "模型名称", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].url", value = "模型地址", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].tableName", value = "房屋关联表", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].componentId", value = "componentId属性", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].boundingbox", value = "包围盒", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].topologyelements", value = "拓扑关系", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].obbCenter", value = "obb包围盒中心", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].subtract", value = "rvt模型包围盒中心与3dtiles模型包围盒中心差", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].relationIds", value = "多个用逗号分隔（自关联。房屋所关联的构件或者构件所在的房屋）", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].measure", value = "尺寸（长x宽x高）", dataType = "String"),

            @ApiImplicitParam(name = "materials[0].position", value = "坐标(type=2小品模型参数)", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].orientation", value = "方向(type=2小品模型参数)", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].angle", value = "角度(type=2小品模型参数)", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].components", value = "组件(type=2小品模型参数)", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].style", value = "样式(type=2小品模型参数)", dataType = "String"),

            @ApiImplicitParam(name = "materials[0].modelMatrix", value = "模型矩阵(type=1房屋模型参数)", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].propertyUrl", value = "属性地址(type=1房屋模型参数)", dataType = "String"),

            @ApiImplicitParam(name = "materials[0].tileUrl", value = "tile url(type=3构件模型参数)", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].componentType", value = "组件类型(type=3构件模型参数)", dataType = "String"),

            @ApiImplicitParam(name = "materials[0].size", value = "构件的尺寸,用来计算再次加载时剖切的大小", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].clipMatrix", value = "保存剖切的位置", dataType = "String"),
    })
    @PostMapping(value = "/add")
    public ResultForm add(AgUserDesignParam param, MultipartFile file){
        if(param == null){
            return new ResultForm(false, "请输入参数");
        }
        if(StringUtils.isEmpty(param.getUserId())){
            return new ResultForm(false, "用户id必传");
        }
        try{
            schemeService.add(param, file, param.getMaterials());
            return new ResultForm(true, "添加成功");
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "添加失败");
        }
    }

    @ApiOperation(value = "修改方案",notes = "修改方案")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramType", value = "paramType=1修改方案；paramType=2设置默认方案（只需要id参数）", dataType = "String"),

            @ApiImplicitParam(name = "id", value = "方案id", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "方案名称", dataType = "String"),
            @ApiImplicitParam(name = "landInfo", value = "地块信息", dataType = "String"),
            @ApiImplicitParam(name = "location", value = "保存位置", dataType = "String"),
            @ApiImplicitParam(name = "description", value = "方案简介", dataType = "String"),
            @ApiImplicitParam(name = "file", value = "缩略图", dataType = "file"),

            @ApiImplicitParam(name = "materials[0].id", value = "模型id（新增id为null，修改id必传）", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].type", value = "模型类型（1房屋模型；2小品模型）", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].name", value = "模型名称", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].url", value = "模型地址", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].tableName", value = "房屋关联表", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].componentId", value = "componentId属性", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].boundingbox", value = "包围盒", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].topologyelements", value = "拓扑关系", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].obbCenter", value = "obb包围盒中心", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].subtract", value = "rvt模型包围盒中心与3dtiles模型包围盒中心差", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].relationIds", value = "多个用逗号分隔（自关联。房屋所关联的构件或者构件所在的房屋）", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].measure", value = "尺寸（长x宽x高）", dataType = "String"),

            @ApiImplicitParam(name = "materials[0].position", value = "坐标(type=2小品模型参数)", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].orientation", value = "方向(type=2小品模型参数)", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].angle", value = "角度(type=2小品模型参数)", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].components", value = "组件(type=2小品模型参数)", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].style", value = "样式(type=2小品模型参数)", dataType = "String"),

            @ApiImplicitParam(name = "materials[0].modelMatrix", value = "模型矩阵(type=1房屋模型参数)", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].propertyUrl", value = "属性地址(type=1房屋模型参数)", dataType = "String"),

            @ApiImplicitParam(name = "materials[0].tileUrl", value = "tile url(type=3构件模型参数)", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].componentType", value = "组件类型(type=3构件模型参数)", dataType = "String"),

            @ApiImplicitParam(name = "materials[0].size", value = "构件的尺寸,用来计算再次加载时剖切的大小", dataType = "String"),
            @ApiImplicitParam(name = "materials[0].clipMatrix", value = "保存剖切的位置", dataType = "String"),
    })
    @PutMapping("/update")
    public ResultForm update( AgUserDesignParam param, String paramType, MultipartFile file){
        if(param == null){
            return new ResultForm(false, "请输入参数");
        }
        try{
            if("1".equals(paramType)){
                schemeService.update(param, file, param.getMaterials());
                return new ResultForm(true, "修改成功");
            }
            if("2".equals(paramType)){
                schemeService.setDefault(param.getId());
                return new ResultForm(true, "设置成功");
            }
            return new ResultForm(false, "修改失败");
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "修改失败");
        }
    }


    @ApiOperation(value = "查询详情",notes = "查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramType", value = "paramType=1查询方案详情（参数id）；paramType=2查询小品/房屋模型/构件(参数id，materialsId）", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "方案id", dataType = "String"),
            @ApiImplicitParam(name = "materialsId", value = "小品/房屋模型/构件的id", dataType = "String"),
    })
    @GetMapping("/get")
    public ContentResultForm get(String paramType, String id,  String materialsId){
        try{
            if("1".equals(paramType)){
                AgUserDesignScheme agUserDesignScheme = schemeService.get(id);
                return new ContentResultForm(true, agUserDesignScheme,"查询失败");
            }
            if("2".equals(paramType)){
                List<AgUserDesignMaterials> model = schemeService.getModel(id, materialsId);
                return new ContentResultForm(true, model, "查询成功");
            }
            return new ContentResultForm(true, null, "查询成功");
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
    }

    @ApiOperation(value = "预览方案封面图",notes = "预览方案封面图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thumb", value = "背景图存储位置", dataType = "String"),
    })
    @GetMapping("/preview")
    public void preview(String thumb, HttpServletResponse response){
        try{
            schemeService.view(thumb, response);
        }catch (Exception e){
            logger.info(e.getMessage());
        }
    }

    @ApiOperation(value = "删除设计方案",notes = "删除设计方案")
    @DeleteMapping("/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "方案id", dataType = "String"),
    })
    public ResultForm delete(String id){
        if(StringUtils.isEmpty(id)){
            return new ResultForm(false, "id参数不能为空");
        }
        try{
            schemeService.delete(id);
            return new ResultForm(true, "删除成功");
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "删除失败");
        }
    }

}
