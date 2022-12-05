package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.controller;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckStandard;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service.IAgBimCheckStandardService;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName AgBimCheckStandardController
 * @Author lizih
 * @Date 2020/12/18 16:32
 * @Version 1.0
 */
@Api(value = "审查规范管理", description = "审查规范管理相关接口")
@RestController
@RequestMapping("/agsupport/bimCheck/standard")
public class AgBimCheckStandardController {
    private static final Logger logger = LoggerFactory.getLogger(AgBimCheckStandardController.class);

    @Autowired
    private IAgBimCheckStandardService agBimCheckStandardService;

    @RequestMapping("/index.html")
    public ModelAndView index() throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/bimCheckStandard/index");
    }

    @ApiOperation(value = "分页获取审查规范条文", notes = "分页获取审查规范条文接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category", value = "条文所属类型，不传参则全部查询", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页信息")
    })
    @GetMapping(value = "/find")
    public ContentResultForm find(String category, Page page) {
        try{
            PageInfo<AgBimCheckStandard> pageInfo = agBimCheckStandardService.find(category, page);
            return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(pageInfo));
        } catch (SourceException se){
            logger.info(se.getMessage());
            return new ContentResultForm(false, null, "查询失败"+se.getMessage());
        } catch (Exception e){
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
    }

    @ApiOperation(value = "新增审查条文规范条目", notes = "新增审查条文规范条目接口")
    @PostMapping(value = "/add")
    public ContentResultForm add(AgBimCheckStandard agBimCheckStandard) {
        try {
            agBimCheckStandardService.save(agBimCheckStandard);
            return new ContentResultForm(true, null,"新增成功");
        } catch (SourceException se){
            logger.info(se.getMessage());
            return new ContentResultForm(false, null, "添加失败，"+se.getMessage());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false,null,"添加失败");
        }
    }

    @ApiOperation(value = "修改审查条文规范条目", notes = "修改构审查条文规范条目接口")
    @PostMapping("/update")
    public ContentResultForm update(AgBimCheckStandard agBimCheckStandard) {
        try {
            agBimCheckStandardService.save(agBimCheckStandard);
            return new ContentResultForm(true, null,"修改成功");
        } catch (SourceException se){
            logger.info(se.getMessage());
            return new ContentResultForm(false, null, "修改失败"+se.getMessage());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false, null,"修改失败");
        }
    }

    @ApiOperation(value = "批量删除审查条文规范条目", notes = "批量删除审查条文规范条目接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "删除的ids字符串", dataType = "String")
    })
    @DeleteMapping("/delete")
    public ContentResultForm delete(String ids) {
        try {
            if (StringUtils.isEmpty(ids)) {
                return new ContentResultForm(false, null,"ids不能为空或参数类型传递错误");
            }
            // 封装pIds数组
            String[] pIds = ids.split(",");

            agBimCheckStandardService.deleteByIds(pIds);
            return new ContentResultForm(true, null,"删除成功");
        } catch (SourceException se){
            logger.info(se.getMessage());
            return new ContentResultForm(false, null, "删除失败"+se.getMessage());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false, null,"删除失败");
        }
    }

    @ApiOperation(value = "excel表格批量导入构件分类", notes = "excel表格批量导入构件分类接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "excel文件", dataType = "String")
    })
    @PostMapping("/import")
    public ContentResultForm excelImport(MultipartFile file) {
        try {
            agBimCheckStandardService.excelImport(file);
            return new ContentResultForm(true, null,"导入成功");
        } catch (SourceException se){
            logger.info(se.getMessage());
            return new ContentResultForm(false, null, "导入excel文件失败"+se.getMessage());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false,null, "导入excel文件失败");
        }
    }

}
