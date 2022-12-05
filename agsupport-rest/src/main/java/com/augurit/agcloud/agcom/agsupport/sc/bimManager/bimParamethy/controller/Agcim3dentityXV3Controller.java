//package com.augurit.agcloud.agcom.agsupport.sc.bim.controller;
//
//
//import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dentityA;
//import com.augurit.agcloud.agcom.agsupport.sc.bim.service.IAgcim3dentityAService;
//import com.augurit.agcloud.framework.ui.result.ContentResultForm;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
///**
// * Created with IntelliJ IDEA.
// *
// * @Auther: qinyg
// * @Date: 2020/08
// * @Description:
// */
//@RestController
//@RequestMapping("/agsupport/BIM/dentitya")
//@Api(value = "agcim3dentitya查询数据", description = "agcim3dentitya查询数据")
//public class Agcim3dentityAController {
//    private static final Logger logger = LoggerFactory.getLogger(Agcim3dentityAController.class);
//
//    @Autowired
//    private IAgcim3dentityAService agcim3dentityAService;
//
//    @GetMapping("/find")
//    @ApiOperation(value = "查询agcim3dentitya（需要对查询参数进行url编码防止特殊字符）",notes = "查询agcim3dentitya（需要对查询参数进行url编码防止特殊字符）")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "objectid" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "name" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "version" ,value = "查询条件",dataType = "Long"),
//            @ApiImplicitParam(name = "infotype" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "profession" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "level" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "catagory" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "familyname" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "familytype" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "materialid" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "elementattributes" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "categorypath" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "geometry" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "topologyelements" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "boundingbox" ,value = "查询条件",dataType = "String"),
//    })
//    public ContentResultForm listAgcim3dentityA(Agcim3dentityA param){
//        try{
//            List<Agcim3dentityA> list = agcim3dentityAService.list(param);
//            return new ContentResultForm(true, list, "查询成功");
//        }catch (Exception e){
//            logger.info(e.getMessage());
//            return new ContentResultForm(false, null, "查询失败");
//        }
//    }
//
//    @GetMapping("/filter")
//    @ApiOperation(value = "过滤agcim3dentitya（需要对查询参数进行url编码防止特殊字符）",notes = "过滤agcim3dentitya（需要对查询参数进行url编码防止特殊字符）")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "filterKey" ,value = "需要过滤的key(默认全部key），多个用逗号分隔.例如：id,objectid,name ",dataType = "String"),
//
//            @ApiImplicitParam(name = "id" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "objectid" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "name" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "version" ,value = "查询条件",dataType = "Long"),
//            @ApiImplicitParam(name = "infotype" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "profession" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "level" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "catagory" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "familyname" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "familytype" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "materialid" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "elementattributes" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "categorypath" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "geometry" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "topologyelements" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "boundingbox" ,value = "查询条件",dataType = "String"),
//    })
//    public ContentResultForm filter(String filterKey, Agcim3dentityA param){
//        try{
//            List<Agcim3dentityA>  list = agcim3dentityAService.filter(filterKey, param);
//            return new ContentResultForm(true, list, "查询成功");
//        }catch (Exception e){
//            logger.info(e.getMessage());
//            return new ContentResultForm(false, null, "查询失败");
//        }
//    }
//
//    @GetMapping("/statistics")
//    @ApiOperation(value = "统计agcim3dentitya（需要对查询参数进行url编码防止特殊字符）",notes = "统计agcim3dentitya（需要对查询参数进行url编码防止特殊字符）")
//    @ApiImplicitParams({
//
//            @ApiImplicitParam(name = "countKey" ,value = "需要统计的key，例如：id",dataType = "String"),
//            @ApiImplicitParam(name = "groupKey" ,value = "需要分组的条件，例如：id",dataType = "String"),
//
//
//            @ApiImplicitParam(name = "id" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "objectid" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "name" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "version" ,value = "查询条件",dataType = "Long"),
//            @ApiImplicitParam(name = "infotype" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "profession" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "level" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "catagory" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "familyname" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "familytype" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "materialid" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "elementattributes" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "categorypath" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "geometry" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "topologyelements" ,value = "查询条件",dataType = "String"),
//            @ApiImplicitParam(name = "boundingbox" ,value = "查询条件",dataType = "String"),
//    })
//    public ContentResultForm statistics(String countKey, String groupKey, Agcim3dentityA param){
//        if(StringUtils.isEmpty(countKey)){
//            return new ContentResultForm(false, null, "countKey不能为空");
//        }
//        if(StringUtils.isEmpty(groupKey)){
//            return new ContentResultForm(false, null, "groupKey不能为空");
//        }
//        try{
//            Object  list = agcim3dentityAService.count(countKey, groupKey, param);
//            return new ContentResultForm(true, list, "查询成功");
//        }catch (Exception e){
//            logger.info(e.getMessage());
//            return new ContentResultForm(false, null, "查询失败");
//        }
//    }
//
//}
package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.controller;


import com.augurit.agcloud.agcom.agsupport.domain.Agcim3dentityXCustom;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.service.IAgcim3dentityXService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
@RestController
@RequestMapping("/agsupport/bimParamethy/dentity")
@Api(value = "agcim3dentityx查询数据", description = "agcim3dentityx查询数据")
public class Agcim3dentityXV3Controller {
    private static final Logger logger = LoggerFactory.getLogger(Agcim3dentityXV3Controller.class);

    @Autowired
    private IAgcim3dentityXService agcim3dentityAService;

    @GetMapping("/find")
    @ApiOperation(value = "查询agcim3dentitya（需要对查询参数进行url编码防止特殊字符）",notes = "查询agcim3dentitya（需要对查询参数进行url编码防止特殊字符）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "objectid" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "name" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "version" ,value = "查询条件",dataType = "Long"),
            @ApiImplicitParam(name = "infotype" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "profession" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "level" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "catagory" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "familyname" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "familytype" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "materialid" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "elementattributes" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "categorypath" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "geometry" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "topologyelements" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "boundingbox" ,value = "查询条件",dataType = "String"),

            @ApiImplicitParam(name = "tableName" ,value = "表名，默认是：agcim3dentity_a",dataType = "String"),
    })
    public ContentResultForm listAgcim3dentityA(Agcim3dentityXCustom param, @RequestParam(defaultValue = "agcim3dentity_a") String tableName){
        try{
            List<Agcim3dentityXCustom> list = agcim3dentityAService.list(param, tableName);
            return new ContentResultForm(true, list, "查询成功");
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
    }

    @GetMapping("/filter")
    @ApiOperation(value = "过滤agcim3dentitya（需要对查询参数进行url编码防止特殊字符）",notes = "过滤agcim3dentitya（需要对查询参数进行url编码防止特殊字符）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filterKey" ,value = "需要过滤的key(默认全部key），多个用逗号分隔.例如：id,objectid,name ",dataType = "String"),

            @ApiImplicitParam(name = "id" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "objectid" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "name" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "version" ,value = "查询条件",dataType = "Long"),
            @ApiImplicitParam(name = "infotype" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "profession" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "level" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "catagory" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "familyname" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "familytype" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "materialid" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "elementattributes" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "categorypath" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "geometry" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "topologyelements" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "boundingbox" ,value = "查询条件",dataType = "String"),

            @ApiImplicitParam(name = "tableName" ,value = "表名，默认是：agcim3dentity_a",dataType = "String"),
    })
    public ContentResultForm filter(String filterKey, Agcim3dentityXCustom param, @RequestParam(defaultValue = "agcim3dentity_a") String tableName){
        try{
            List<Agcim3dentityXCustom>  list = agcim3dentityAService.filter(filterKey, param, tableName);
            return new ContentResultForm(true, list, "查询成功");
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
    }

    @GetMapping("/statistics")
    @ApiOperation(value = "统计agcim3dentitya（需要对查询参数进行url编码防止特殊字符）",notes = "统计agcim3dentitya（需要对查询参数进行url编码防止特殊字符）")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "countKey" ,value = "需要统计的key，例如：id",dataType = "String"),
            @ApiImplicitParam(name = "groupKey" ,value = "需要分组的条件，例如：id",dataType = "String"),


            @ApiImplicitParam(name = "id" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "objectid" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "name" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "version" ,value = "查询条件",dataType = "Long"),
            @ApiImplicitParam(name = "infotype" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "profession" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "level" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "catagory" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "familyname" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "familytype" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "materialid" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "elementattributes" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "categorypath" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "geometry" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "topologyelements" ,value = "查询条件",dataType = "String"),
            @ApiImplicitParam(name = "boundingbox" ,value = "查询条件",dataType = "String"),

            @ApiImplicitParam(name = "tableName" ,value = "表名，默认是：agcim3dentity_a",dataType = "String"),

            @ApiImplicitParam(name = "paramType" ,value = "1：根据传入的countKey统计； 2：根据构件分类（catagory）字段分类型统计各构件总数",dataType = "String",defaultValue = "1"),
    })
    public ContentResultForm statistics(String countKey, String groupKey, Agcim3dentityXCustom param, @RequestParam(defaultValue = "agcim3dentity_a") String tableName, @RequestParam(defaultValue = "1") String paramType ){
        if(StringUtils.isEmpty(countKey)){
            return new ContentResultForm(false, null, "countKey不能为空");
        }
        if(StringUtils.isEmpty(groupKey)){
            return new ContentResultForm(false, null, "groupKey不能为空");
        }
        try{
            if ("1".equals(paramType)) {
                Object list = agcim3dentityAService.statistics(countKey, groupKey, param, tableName);
                return new ContentResultForm(true, list, "查询成功");
            } else if ("2".equals(paramType)) {
                Map map = agcim3dentityAService.countForCatagoryType(param, tableName);
                return new ContentResultForm(true, map, "查询成功");
            }
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
        return new ContentResultForm(false, null, "paramType参数非法");
    }

    @GetMapping("/get")
    @ApiOperation(value = "获取",notes = "获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramType" ,value = "paramType=1，比较两个表不同的列数据，返回某个表的数据，参数有：baseTableName、targetTableName，columns；" +
                    "paramType=2，比较两个表的添加和或者删除的数据，参数有：baseTableName、targetTableName;",dataType = "String"),
            @ApiImplicitParam(name = "columns" ,value = "需要比较的列，多个用逗号分隔",dataType = "String"),
            @ApiImplicitParam(name = "baseTableName" ,value = "基础表，需要返回的表数据",dataType = "String"),
            @ApiImplicitParam(name = "targetTableName" ,value = "目标表，需要比较的表",dataType = "String"),

            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "分页参数:page=1&rows=10", dataType = "int"),
    })
    public ContentResultForm get(String paramType, String baseTableName, String targetTableName, String columns, Page page){
        if(StringUtils.isEmpty(paramType)){
            return new ContentResultForm(false, null, "paramType不能为空");
        }
        if(StringUtils.isEmpty(baseTableName)){
            return new ContentResultForm(false, null, "baseTableName不能为空");
        }
        if(StringUtils.isEmpty(targetTableName)){
            return new ContentResultForm(false, null, "targetTableName不能为空");
        }
        try{
            if("1".equals(paramType)){
                if(StringUtils.isEmpty(columns)){
                    return new ContentResultForm(false, null, "columns不能为空");
                }
                PageInfo<Object> list = agcim3dentityAService.compareDoubleTableColumn(baseTableName, targetTableName, columns, page);
                return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(list));
            }
            if("2".equals(paramType)){
                PageInfo<Object> list = agcim3dentityAService.compareDoubleTableModify(baseTableName, targetTableName, page);
                return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(list));
            }
            return new ContentResultForm(false, null, "paramType参数错误");
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
    }
}
