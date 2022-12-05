package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.controller;


import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.AgComponentCodeCustom;
import com.augurit.agcloud.agcom.agsupport.domain.AgMaterialsComponentCustom;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgMaterialsComponent;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgMaterialsComponentWithBLOBs;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.ISysAgMaterialsComponentService;
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

import javax.servlet.http.HttpServletResponse;


/**
 * Created with IntelliJ IDEA.
 * 材料构件相关接口
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
@RestController
@RequestMapping("/agsupport/BIM/Component")
@Api(value = "后台管理-材料构件相关接口", description = "后台管理-材料构件相关接口")
public class AgMaterialsComponentController {
    private static final Logger logger = LoggerFactory.getLogger(AgMaterialsComponentController.class);

    @Autowired
    private ISysAgMaterialsComponentService materialsComponentService;

    @RequestMapping("/index.html")
    @ApiIgnore
    public ModelAndView index(Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/bim/component/index");
    }

    @GetMapping("/find")
    @ApiOperation(value = "材料构件列表",notes = "材料构件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "材料构件名称（搜索条件）", dataType = "String"),
            @ApiImplicitParam(name = "texture", value = "材质（筛选条件，多个用逗号分隔）", dataType = "String"),
            @ApiImplicitParam(name = "vendor", value = "厂商（筛选条件，多个用逗号分隔）", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "int"),
            @ApiImplicitParam(name = "rows", value = "分页参数:page=1&rows=10", dataType = "int"),
    })
    public ResultForm find(AgMaterialsComponent materials, Page page){
        try{
            PageInfo<AgMaterialsComponent> allResource = materialsComponentService.list(materials, page);
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
     *
     此接口已修改，需要删除，改成zip上传

    */
    @Deprecated
    @ApiIgnore
    @PostMapping("/save")
    @ApiOperation(value = "保存材料构件信息",notes = "保存材料构件信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "构件名称", dataType = "String"),
            @ApiImplicitParam(name = "catagory", value = "构件类型", dataType = "String"),
            @ApiImplicitParam(name = "texture", value = "材质", dataType = "String"),
            @ApiImplicitParam(name = "measure", value = "尺寸", dataType = "String"),
            @ApiImplicitParam(name = "singlePrice", value = "单价（元）", dataType = "String"),
            @ApiImplicitParam(name = "vendor", value = "厂商", dataType = "String"),
            @ApiImplicitParam(name = "remark", value = "备注说明", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "tableName", value = "材料构件表名", dataType = "String"),

            @ApiImplicitParam(name = "thumbFile", value = "预览图", dataType = "File"),
            @ApiImplicitParam(name = "modelFile", value = "材料模型glb文件", dataType = "File", required = true),
    })
    public ResultForm save(AgMaterialsComponentWithBLOBs materials, MultipartFile thumbFile, MultipartFile modelFile){
        logger.info("---------start-----------");
        if(materials == null){
            return new ResultForm(false, "请填写参数");
        }
        if(modelFile == null || modelFile.isEmpty()){
            return new ResultForm(false, "请选择材料模型文件");
        }
        try {
            materialsComponentService.save(materials, thumbFile, modelFile);
            return new ContentResultForm(true, null,"保存成功");
        } catch (SourceException e) {
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e) {
            logger.info(e.getMessage());
            return new ResultForm(false, "上传失败");
        }
    }



    @PutMapping("/update")
    @ApiOperation(value = "修改材料构件",notes = "修改材料构件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "构件id", dataType = "String", required = true),
            @ApiImplicitParam(name = "name", value = "构件名称", dataType = "String"),
            @ApiImplicitParam(name = "catagory", value = "构件类型", dataType = "String"),
            @ApiImplicitParam(name = "texture", value = "材质", dataType = "String"),
            @ApiImplicitParam(name = "measure", value = "尺寸", dataType = "String"),
            @ApiImplicitParam(name = "singlePrice", value = "单价（元）", dataType = "String"),
            @ApiImplicitParam(name = "vendor", value = "厂商", dataType = "String"),
            @ApiImplicitParam(name = "remark", value = "备注说明", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String", required = true),
            @ApiImplicitParam(name = "tableName", value = "材料构件表名", dataType = "String"),
            @ApiImplicitParam(name = "specification", value = "规格（长宽高）", dataType = "String"),

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
            @ApiImplicitParam(name = "auths", value = "数据访问权限（保存字典的code值，多个用逗号分隔）", dataType = "String"),
            @ApiImplicitParam(name = "modelFile", value = "材料模型glb文件", dataType = "File", required = true),
    })
    public ResultForm update(AgMaterialsComponentCustom materials, AgComponentCodeCustom param, MultipartFile thumbFile, MultipartFile modelFile){
        if(materials == null){
            return new ResultForm(false, "请填写参数");
        }
        if(StringUtils.isEmpty(materials.getId())){
            return new ResultForm(false, "请选择数据id");
        }
        try {
            materialsComponentService.update(materials, param, thumbFile, modelFile);
            return new ResultForm(true, "修改成功");
        } catch (SourceException e) {
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ResultForm(false, "修改失败");
        }
    }


    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除材料构件信息",notes = "批量删除材料构件信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "材料构件id,多个用逗号分隔", dataType = "String", required = true),
    })
    public ResultForm delete(String ids){
        if(StringUtils.isEmpty(ids)){
            return new ResultForm(false,"请选择数据");
        }
        try{
            materialsComponentService.batchDelete(ids);
            return new ResultForm(true,"删除成功");
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false,e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "删除失败");
        }
    }

    //此接口不对外开放
    @ApiIgnore
    @PostMapping("/uploadGlb")
    @ApiOperation(value = "批量更新glb",notes = "批量更新glb")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "glb的zip压缩包文件", dataType = "File", required = true),
    })
    public ResultForm uploadGlb(MultipartFile file){
        if(StringUtils.isEmpty(file) || file.isEmpty()){
            return new ResultForm(false,"请选择文化");
        }
        try{
            materialsComponentService.uploadglb(file);
            return new ResultForm(true,"批量更新glb成功");
        }catch (SourceException e){
            logger.info(e.getMessage());
            return new ResultForm(false,e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "批量更新glb失败");
        }
    }

    @GetMapping("/statistics")
    @ApiOperation(value = "过滤筛选条件获取",notes = "过滤筛选条件获取")
    @ApiImplicitParams({

    })
    public ResultForm statistics(){
        try{
            Object countFilter = materialsComponentService.statistics();
            return new ContentResultForm(true,countFilter, "查询成功");
        }catch (SourceException e){
            return new ResultForm(false,e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "查询失败");
        }
    }


    //此接口不对外开放
    @ApiIgnore
    @PostMapping("/saveDoorOrWin")
    @ApiOperation(value = "保存门/窗构件信息",notes = "保存门/窗构件信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "glbFile", value = "file", dataType = "File"),
            @ApiImplicitParam(name = "thumbFile", value = "file", dataType = "File"),
    })
    public ResultForm saveDoorOrWin(AgMaterialsComponentWithBLOBs door, MultipartFile glbFile, MultipartFile thumbFile){
        try{
            materialsComponentService.saveDoorOrWin(door, glbFile, thumbFile);
            return new ResultForm(true,"添加成功");
        }catch (SourceException e){
            return new ResultForm(false,e.getMessage());
        }catch (Exception e){
            logger.info(e.getMessage());
            return new ResultForm(false, "添加失败");
        }
    }

    @PostMapping("/add")
    @ApiOperation(value = "保存材料信息(zip压缩文件)",notes = "保存材料信息(zip压缩文件)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramType", value = "paramType=1添加glb构件模型；paramType=2添加rfa构件模型", dataType = "String", required = true),
            @ApiImplicitParam(name = "modelZipFile", value = "材料模型（只支持ip压缩文件）", dataType = "File", required = true),
    })
    public ResultForm add(@RequestParam(defaultValue = "1") String paramType, MultipartFile modelZipFile){
        //默认paramType=1,如果是2添加rvt
        if(!StringUtils.isEmpty(paramType) && "2".equals(paramType)){
            return saveRfa(modelZipFile);
        }

        if(modelZipFile == null || modelZipFile.isEmpty()){
            return new ResultForm(false, "modelZipFile参数为空");
        }
        try {
            materialsComponentService.saveGlbZip(modelZipFile);
            return new ContentResultForm(true, null,"保存成功");
        } catch (SourceException e) {
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e) {
            logger.info(e.getMessage());
            return new ResultForm(false, "上传失败");
        }
    }



    public ResultForm saveRfa(MultipartFile modelFile){
        if(modelFile == null || modelFile.isEmpty()){
            return new ResultForm(false, "modelFile参数为空");
        }
        try {
            materialsComponentService.saveRfa(modelFile);
            return new ContentResultForm(true, null,"保存成功");
        } catch (SourceException e) {
            logger.info(e.getMessage());
            return new ResultForm(false, e.getMessage());
        }catch (Exception e) {
            logger.info(e.getMessage());
            return new ResultForm(false, "上传失败");
        }
    }


    @GetMapping("/preview")
    @ApiOperation(value = "预览文件",notes = "预览文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "type=1预览图片；type=2预览构件glb", dataType = "String", required = true),
            @ApiImplicitParam(name = "id", value = "材料id", dataType = "String", required = true),
    })
    public void preview( String type, String id, HttpServletResponse response){
        try{
            materialsComponentService.view(type, id, response);
        }catch (SourceException e){
            logger.info("-----预览文件error-------" + e.getMessage());
        }catch (Exception e){
            logger.info("-----预览文件error-------" + e.getMessage());
        }
    }
}
