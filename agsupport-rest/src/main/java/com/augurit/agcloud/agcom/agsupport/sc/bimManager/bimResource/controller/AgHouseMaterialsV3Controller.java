package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.AgHouseMaterialsCustom;
import com.augurit.agcloud.agcom.agsupport.domain.AgHouseMaterialsPointCustom;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgHouseMaterialsPoint;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.IAgHouseMaterialsService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.Md5Utils;
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
import springfox.documentation.annotations.ApiIgnore;

import java.net.URLDecoder;

/**
 *
 * @Author: qinyg
 * @Date: 2020/12/14 14:03
 * @tips:
 *
 */
@RestController
@RequestMapping("/agsupport/bimResource/instance")
@Api(value = "房屋材料接口", description = "房屋材料接口")
public class AgHouseMaterialsV3Controller {
    private static final Logger logger = LoggerFactory.getLogger(AgHouseMaterialsV3Controller.class);

    @Autowired
    private IAgHouseMaterialsService agHouseMaterialsService;

    @GetMapping("/find")
    @ApiOperation(value = "列表",notes = "列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sourceId", value = "来源id", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "类型", dataType = "String"),
            @ApiImplicitParam(name = "floor", value = "楼层", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "名称（搜索条件）", dataType = "String"),
            @ApiImplicitParam(name = "mapArray", value = "范围，需要对此参数进行encodeURIComponent编码。原始数据格式如：[[\"113.37202773\",\"23.10177294\"],[\"113.37323526\",\"23.10174397\"]]", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "分页参数:page=1&rows=10", dataType = "int"),
    })
    public ResultForm find(String sourceId, String type, String name, String floor, Page page, String mapArray){
        try{
            String paramList = "";
            if(!StringUtils.isEmpty(mapArray)){
                if(!StringUtils.isEmpty(mapArray)){
                    mapArray = URLDecoder.decode(mapArray, "utf8");
                    JSONArray jsonArray = JSONObject.parseArray(mapArray);
                    if(jsonArray != null){
                        for(int i = 0; i < jsonArray.size(); i++){
                            JSONArray objectArr = jsonArray.getJSONArray(i);
                            if(objectArr != null && objectArr.size() == 2){
                                paramList += objectArr.get(1) + "_" + objectArr.get(0) + ",";
                            }
                        }
                    }
                    if(!StringUtils.isEmpty(paramList)){
                        //去除最后一个“，"
                        paramList = paramList.substring(0, paramList.length() - 1);
                    }
                }
            }
            //作为缓存的key，无任何逻辑意义
            String cacheableKey = Md5Utils.hash(sourceId + type + name + floor + page.getPageSize() +page.getPageNum() + paramList);
            PageInfo<AgHouseMaterialsPointCustom> list = agHouseMaterialsService.findMaterialsPoint(sourceId, type, name, floor, page, paramList, cacheableKey);
            return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(list));
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "查询失败");
        }
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加点",notes = "添加点")
    @ApiImplicitParams({

    })
    public ResultForm add(AgHouseMaterialsPoint entity){
        try{
            agHouseMaterialsService.addMaterialsPoint(entity);
            return new ResultForm(true, "添加成功");
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "添加失败");
        }
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除点",notes = "删除点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id，删除多个用逗号分隔", dataType = "String"),
    })
    public ResultForm delete(String ids){
        if(StringUtils.isEmpty(ids)){
            return new ResultForm(false, "ids参数不能为空");
        }
        try{
            agHouseMaterialsService.deleteMaterialsPoint(ids);
            return new ResultForm(true, "删除成功");
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "删除失败");
        }
    }



    //此接口不对外开放，使用来导入数据
    @ApiIgnore
    @PostMapping("/importStr")
    @ApiOperation(value = "导入数据",notes = "导入数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型", dataType = "String"),
            @ApiImplicitParam(name = "jsonStr", value = "jsonStr构件数据", dataType = "String"),
    })
    public ResultForm importStr(String jsonStr,  AgHouseMaterialsPoint entity){
        try{
            agHouseMaterialsService.importMaterialsPointJsonStr(jsonStr, entity);
            return new ResultForm(true, "导入数据成功");
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "导入数据失败");
        }
    }


    @GetMapping("/statistics")
    @ApiOperation(value = "统计",notes = "统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramType", value = "paramType=1,查询该范围内的构件；", dataType = "String", required = true),
            @ApiImplicitParam(name = "mapArray", value = "paramType=1,范围，需要对此参数进行encodeURIComponent编码。原始数据格式如：[[\"113.37202773\",\"23.10177294\"],[\"113.37323526\",\"23.10174397\"]]", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "分页参数:page=1&rows=10", dataType = "int"),
    })
    public ResultForm statistics(Page page, String paramType, String mapArray){
        try{
            if("1".equals(paramType)){
                String paramList = "";
                if(!StringUtils.isEmpty(mapArray)){
                    mapArray = URLDecoder.decode(mapArray, "utf8");
                    JSONArray jsonArray = JSONObject.parseArray(mapArray);
                    if(jsonArray != null){
                        for(int i = 0; i < jsonArray.size(); i++){
                            JSONArray objectArr = jsonArray.getJSONArray(i);
                            if(objectArr != null && objectArr.size() == 2){
                                paramList += objectArr.get(1) + "_" + objectArr.get(0) + ",";
                            }
                        }
                    }
                    if(!StringUtils.isEmpty(paramList)){
                        //去除最后一个“，"
                        paramList = paramList.substring(0, paramList.length() - 1);
                    }
                }
                //作为缓存的key，无任何逻辑意义
                String cacheableKey = Md5Utils.hash(page.getPageNum() + page.getPageSize() + paramList);
                PageInfo<AgHouseMaterialsCustom> list = agHouseMaterialsService.statisticsMaterialsisInPolygon(page, paramList, cacheableKey);
                return new ContentResultForm(true,  PageHelper.toEasyuiPageInfo(list));
            }
            return new ResultForm(false, "paramType参数不正确");
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "查询失败");
        }
    }




}
