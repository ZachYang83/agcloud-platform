package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.controller;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgMaterialsComponent;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.IAgMaterialsComponentService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
@RestController
@RequestMapping("/agsupport/BIM/Component")
@Api(value = "材料构件相关接口", description = "材料构件相关接口")
public class AgMaterialsComponentController {
    private static final Logger logger = LoggerFactory.getLogger(AgMaterialsComponentController.class);

    @Autowired
    private IAgMaterialsComponentService materialsComponentService;
//    @Autowired
//    private ISysAgMaterialsComponentService sysAgMaterialsComponentService;


    @GetMapping("/find")
    @ApiOperation(value = "材料构件列表",notes = "材料构件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "材料构件名称（搜索条件）", dataType = "String"),
            @ApiImplicitParam(name = "catagory", value = "构件分类（字典的code值）", dataType = "String"),
            @ApiImplicitParam(name = "componentCode", value = "类目编码code", dataType = "String"),
            @ApiImplicitParam(name = "componentCodeName", value = "类目编码名称", dataType = "String"),
            @ApiImplicitParam(name = "specification", value = "长宽高", dataType = "String"),
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

//    @PostMapping("/add")
//    @ApiOperation(value = "保存材料信息(zip压缩文件)",notes = "保存材料信息(zip压缩文件)")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "modelFile", value = "材料模型（只支持ip压缩文件）", dataType = "File", required = true),
//    })
//    public ResultForm add(MultipartFile modelFile){
//        if(modelFile == null || modelFile.isEmpty()){
//            return new ResultForm(false, "modelFile参数为空");
//        }
//        try {
////            sysAgMaterialsComponentService.saveRfa(modelFile);
//            return new ContentResultForm(true, null,"保存成功");
//        } catch (SourceException e) {
//            logger.info(e.getMessage());
//            return new ResultForm(false, e.getMessage());
//        }catch (Exception e) {
//            logger.info(e.getMessage());
//            return new ResultForm(false, "上传失败");
//        }
//    }
}
