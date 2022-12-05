package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.controller;


import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgHouse;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgSysSetting;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.ISysAgHouseService2;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.util.*;
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
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/09
 * @Description:
 */
@RestController
@RequestMapping("/agsupport/BIM/Project")
@Api(value = "后台管理-房屋相关接口", description = "后台管理-房屋相关接口")
public class AgHouseController {
    private static final Logger logger = LoggerFactory.getLogger(AgHouseController.class);

    @Autowired
    private ISysAgHouseService2 sysAgHouseService;

    @RequestMapping("/index.html")
    @ApiIgnore
    public ModelAndView index(Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/bim/project/index");
    }

    /**
     * 查询所有资源
     * @param resource
     * @param page
     * @return
     */
    @GetMapping("/find")
    @ApiOperation(value = "房屋列表",notes = "房屋列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "hourseName", value = "房屋名称（搜索条件）", dataType = "String"),
            @ApiImplicitParam(name = "structureType", value = "结构类型（筛选条件，多个用逗号分隔）", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "分页参数:page=1&rows=10", dataType = "int"),
    })
    public ResultForm find(AgHouse resource, Page page){

        try{
            PageInfo<AgHouse> allResource = sysAgHouseService.find(resource, page);
            return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(allResource));
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false, "查询失败");
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "查询失败");
        }
    }

    /**
     * 查询
     * @return.
     *
     */
    @GetMapping("/get")
    @ApiOperation(value = "查询数据",notes = "查询数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramType", value = "paramType=1查询户型图；paramType=2获取系统设置信息", dataType = "String"),
            @ApiImplicitParam(name = "id", value = "paramType=1查询户型图；id是房屋id", dataType = "String"),
    })
    public ContentResultForm findSysSetting(String paramType, String id){
        try{
            if("1".equals(paramType)){
                List<AgHouse> allResource = sysAgHouseService.findHouseDir(id);
                return new ContentResultForm(true, allResource);
            }
            if("2".equals(paramType)){
                List<AgSysSetting> list = sysAgHouseService.getAllSysSetting();
                return new ContentResultForm(true, list, "查询成功");
            }
            return new ContentResultForm(true, null, "查询成功");
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
    }



    /**
     * 修改资源，只修改属性，不修改文件
     * @param resource
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改房屋信息",notes = "修改房屋信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "房屋id", dataType = "String", required = true),
            @ApiImplicitParam(name = "hourseName", value = "房屋名称", dataType = "String"),
            @ApiImplicitParam(name = "structureType", value = "结构类型", dataType = "String"),
            @ApiImplicitParam(name = "homesteadArea", value = "宅基地面积", dataType = "String"),
            @ApiImplicitParam(name = "floorArea", value = "占地面积", dataType = "String"),
            @ApiImplicitParam(name = "coveredArea", value = "建筑面积", dataType = "String"),
            @ApiImplicitParam(name = "costEstimates", value = "造价估计", dataType = "String"),
            @ApiImplicitParam(name = "remark", value = "备注说明", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(name = "tableName", value = "房屋数据表名", dataType = "String"),

            @ApiImplicitParam(name = "tableCode", value = "分类名称（表代码，字典code值）", dataType = "String"),
            @ApiImplicitParam(name = "tableCodeName", value = "分类名称（表代码，字典name值）", dataType = "String"),
            @ApiImplicitParam(name = "largeCode", value = "大类（大类代码）", dataType = "String"),
            @ApiImplicitParam(name = "largeCodeName", value = "大类名称（大类代码名称）", dataType = "String"),
            @ApiImplicitParam(name = "mediumCode", value = "中类（中类代码）", dataType = "String"),
            @ApiImplicitParam(name = "mediumCodeName", value = "中类名称（中类代码名称）", dataType = "String"),
            @ApiImplicitParam(name = "smallCode", value = "小类（小类代码）", dataType = "String"),
            @ApiImplicitParam(name = "smallCodeName", value = "小类名称（小类代码名称）", dataType = "String"),
            @ApiImplicitParam(name = "detailCode", value = "细类（细类代码）", dataType = "String"),
            @ApiImplicitParam(name = "detailCodeName", value = "细类名称（细类代码名称）", dataType = "String"),

            @ApiImplicitParam(name = "thumbFile", value = "预览图", dataType = "File"),
            @ApiImplicitParam(name = "dirFiles", value = "户型文件夹", dataType = "File[]"),

            @ApiImplicitParam(name = "auths", value = "数据访问权限（保存字典的code值，多个用逗号分隔）", dataType = "String"),
    })
    public ResultForm update(AgHouseCustom resource, MultipartFile thumbFile, MultipartFile dirFiles[]){
        if(resource == null){
            return new ResultForm(false, "请填写参数");
        }
        if(StringUtils.isEmpty(resource.getId())){
            return new ResultForm(false, "请填写参数");
        }
        try {
            sysAgHouseService.update(resource, thumbFile, dirFiles);
            return new ResultForm(true, "修改成功");
        } catch (SourceException e) {
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ResultForm(false, "修改失败");
        }
    }


    /**
     * 删除资源
     * @param ids
     * @return
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除房屋信息",notes = "批量删除房屋信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "房屋id,多个用逗号分隔", dataType = "String", required = true),
    })
    public ResultForm delete(String ids){
        if(StringUtils.isEmpty(ids)){
            return new ResultForm(false,"请选择数据");
        }
        try{
            sysAgHouseService.batchDelete(ids);
            return new ResultForm(true,"删除成功");
        }catch (SourceException e){
            return new ResultForm(false,e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "删除失败");
        }
    }


    @GetMapping("/statistics")
    @ApiOperation(value = "过滤筛选条件获取",notes = "过滤筛选条件获取")
    @ApiImplicitParams({

    })
    public ResultForm statistics(){
        try{
            Object countFilter = sysAgHouseService.statistics();
            return new ContentResultForm(true,countFilter, "查询成功");
        }catch (SourceException e){
            return new ResultForm(false,e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "查询失败");
        }
    }

    @PostMapping("/add")
    @ApiOperation(value = "保存房屋信息(zip压缩文件)",notes = "保存房屋信息(zip压缩文件)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramType", value = "paramType=1添加房屋模型3dtiles；paramType=2添加房屋模型rvt", dataType = "String", required = true),
            @ApiImplicitParam(name = "modelZipFile", value = "房屋模型（只支持zip压缩文件）", dataType = "File", required = true),
            @ApiImplicitParam(name = "modelFile", value = "房屋模型（只支持zip压缩文件）", dataType = "File", required = true),
    })
    public ResultForm add(@RequestParam(defaultValue = "1") String paramType, MultipartFile modelZipFile){
        //默认paramType=1,如果是2添加rvt
        if(!StringUtils.isEmpty(paramType) && "2".equals(paramType)){
            return saveRvtZip(modelZipFile);
        }

        if(modelZipFile == null || modelZipFile.isEmpty()){
            return new ResultForm(false, "modelZipFile参数为空");
        }
        try {
            sysAgHouseService.add3dtilesZip(modelZipFile);
            return new ContentResultForm(true, null,"保存成功");
        } catch (SourceException e) {
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e) {
            logger.info(e.getMessage());
            return new ResultForm(false, "上传失败");
        }
    }



    public ResultForm saveRvtZip(MultipartFile modelFile){
        if(modelFile == null || modelFile.isEmpty()){
            return new ResultForm(false, "modelFile参数为空");
        }
        try {
            sysAgHouseService.saveRvtZip(modelFile);
            return new ContentResultForm(true, null,"保存成功");
        } catch (SourceException e) {
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e) {
            logger.info(e.getMessage());
            return new ResultForm(false, "上传失败");
        }
    }
}
