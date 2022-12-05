package com.augurit.agcloud.agcom.agsupport.sc.widgetStore.controller;

import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgWidgetStoreCustom;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetStoreWithBLOBs;
import com.augurit.agcloud.agcom.agsupport.sc.widgetStore.service.IAgWidgetStoreService;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
@Api(value = "微件商店相关接口", description = "微件商店相关接口")
@RestController
@RequestMapping("/agsupport/applicationManager/widgetStore")
public class AgWidgetStoreController {
    private static final Logger logger = LoggerFactory.getLogger(AgWidgetStoreController.class);

    @Autowired
    private IAgWidgetStoreService widgetStoreService;

    /**
     * 样式管理页面
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/index.html")
    @ApiIgnore
    public ModelAndView index(Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/widgetStore/index");
    }

    @GetMapping("/find")
    @ApiOperation(value = "微件商店列表", notes = "微件商店列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "微件商店名称", dataType = "String"),
            @ApiImplicitParam(name = "rows", value = "分页参数:page=1&rows=10", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "String"),
    })
    public ContentResultForm find(String name, Page page) {
        try {
            PageInfo<AgWidgetStoreCustom> list = widgetStoreService.list(name, page);
            return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(list));
        } catch (Exception e) {
            logger.info("-----微件商店查询失败-----" + e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加微件商店", notes = "添加微件商店")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "微件商店名称", dataType = "String"),
            @ApiImplicitParam(name = "description", value = "功能描述", dataType = "String"),
            @ApiImplicitParam(name = "version", value = "版本", dataType = "String"),

            @ApiImplicitParam(name = "thumbFile", value = "缩略图", dataType = "File"),
            @ApiImplicitParam(name = "widgetZipFile", value = "微件压缩文件（zip）", dataType = "File"),
    })
    public ContentResultForm add(AgWidgetStoreWithBLOBs store, MultipartFile thumbFile, MultipartFile widgetZipFile) {
        if(widgetZipFile == null || widgetZipFile.isEmpty()){
            return new ContentResultForm(false, null, "请上传商店源码文件");
        }
        //必须是zip
        if(!widgetZipFile.getOriginalFilename().endsWith(".zip")){
            return new ContentResultForm(false, null, "请上传zip压缩文件");
        }
        try {
            widgetStoreService.save(store, thumbFile, widgetZipFile);
            return new ContentResultForm(true, null, "添加成功");
        } catch (Exception e) {
            logger.info("-----微件商店添加失败-----" + e.getMessage());
            return new ContentResultForm(false, null,  e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改微件商店", notes = "修改微件商店")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "String", required = true),
            @ApiImplicitParam(name = "name", value = "微件商店名称", dataType = "String"),
            @ApiImplicitParam(name = "description", value = "功能描述", dataType = "String"),
            @ApiImplicitParam(name = "version", value = "版本", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "审核状态：2审核通过；3审核拒绝", dataType = "Integer"),

            @ApiImplicitParam(name = "thumbFile", value = "缩略图", dataType = "File"),
            @ApiImplicitParam(name = "widgetZipFile", value = "微件压缩文件（zip）", dataType = "File"),
    })
    public ContentResultForm update(String id, String name, String description, String version, Integer status, MultipartFile thumbFile, MultipartFile widgetZipFile) {
        if(widgetZipFile != null && !widgetZipFile.isEmpty()){
            //必须是zip
            if(!widgetZipFile.getOriginalFilename().endsWith(".zip")){
                return new ContentResultForm(false, null, "请上传zip压缩文件");
            }
        }
        try {
            AgWidgetStoreWithBLOBs store = new AgWidgetStoreWithBLOBs();
            store.setId(id);
            store.setName(name);
            store.setDescription(description);
            store.setVersion(version);
            store.setStatus(status);
            widgetStoreService.update(store, thumbFile, widgetZipFile);
            return new ContentResultForm(true, null, "修改成功");
        } catch (Exception e) {
            logger.info("-----微件商店修改失败-----" + e.getMessage());
            return new ContentResultForm(false, null, "修改失败");
        }
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除微件商店", notes = "删除微件商店")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "选中要删除的ids", dataType = "String", required = true),
    })
    public ContentResultForm delete(String ids) {
        try {
            widgetStoreService.deleteBatch(ids);
            return new ContentResultForm(true, null, "删除成功");
        } catch (Exception e) {
            logger.info("-----微件商店删除失败-----" + e.getMessage());
            return new ContentResultForm(false, null, "删除失败");
        }
    }



    @GetMapping("/download")
    @ApiOperation(value = "微件源码获取", notes = "微件源码获取")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "选中要获取条目的id", dataType = "String", required = true),
    })
    public void download(String id, HttpServletResponse response, HttpServletRequest request) {
        try {
            AgWidgetStoreWithBLOBs store = widgetStoreService.getWidget(id);
            String fileName = FileUtil.encodeFileName(store.getWidgetName(),request);
            FileUtil.writerFile(store.getWidgetFile(), fileName,  true, response);
        } catch (Exception e) {
            logger.info("-----微件商店微件源码获取失败-----" + e.getMessage());
        }
    }
}
