package com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.controller;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetAssetsColumns;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetAssetsTable;
import com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.domain.AgWidgetAssetsColumnsParam;
import com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.service.IAgWidgetAssetsTableService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.opus.common.domain.OpuRsAppSoft;
import com.augurit.agcloud.opus.common.service.rs.OpuRsAppService;
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
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @Author: qinyg
 * @Date: 2020/11/17
 * @tips:
 */
@Api(value = "微件资产表相关接口", description = "微件资产表相关接口")
@RestController
@RequestMapping("/agsupport/configManager/widgetAssetsTable")
public class AgWidgetAssetsTableController {
    private static final Logger logger = LoggerFactory.getLogger(AgWidgetAssetsTableController.class);

    @Autowired
    private IAgWidgetAssetsTableService thematicTableService;

    /**
     * 样式管理页面
     *
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/index.html")
    public ModelAndView index(Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/widgetAssets/index");
    }

    @GetMapping("/find")
    @ApiOperation(value = "微件资产表列表", notes = "微件资产表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "thematicProjectId", value = "微件资产项目id", dataType = "String"),
            @ApiImplicitParam(name = "tableName", value = "表名（搜索条件）", dataType = "String"),
            @ApiImplicitParam(name = "rows", value = "分页参数:page=1&rows=10", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "String"),
    })
    public ContentResultForm find(String thematicProjectId, String tableName, Page page) {
        try {
            PageInfo<AgWidgetAssetsTable> list = thematicTableService.find(thematicProjectId, tableName, page);
            return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(list));
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加微件资产表", notes = "添加微件资产表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "columns[0].columnName", value = "字段名称", dataType = "String"),
            @ApiImplicitParam(name = "columns[0].columnType", value = "字段类型", dataType = "String"),
    })
    public ContentResultForm add(AgWidgetAssetsTable table, AgWidgetAssetsColumnsParam param) {
        if (table == null) {
            return new ContentResultForm(false, null, "微件资产表参数为空");
        }
        if (StringUtils.isEmpty(table.getAppSoftId())) {
            return new ContentResultForm(false, null, "appSoftId参数为空");
        }
        if (StringUtils.isEmpty(table.getTableName())) {
            return new ContentResultForm(false, null, "tableName参数为空");
        }
        try {
            thematicTableService.add(table, param != null ? param.getColumns() : null);
            return new ContentResultForm(true, null, "添加成功");
        } catch (SourceException e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, e.getMessage());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "添加失败");
        }
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除微件资产表", notes = "删除微件资产表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "微件资产表id，多个用逗号分隔", dataType = "String"),
    })
    public ContentResultForm delete(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return new ContentResultForm(false, null, "ids参数为空");
        }
        try {
            thematicTableService.deletes(ids);
            return new ContentResultForm(true, null, "删除成功");
        } catch (SourceException e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, e.getMessage());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "删除失败");
        }
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改微件资产表", notes = "修改微件资产表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "columns[0].columnName", value = "字段名称", dataType = "String"),
            @ApiImplicitParam(name = "columns[0].columnType", value = "字段类型", dataType = "String"),
            @ApiImplicitParam(name = "deleteColumnIds", value = "deleteColumnIds 需要删除的字段id，多个用逗号分隔", dataType = "String"),
    })
    public ContentResultForm update(AgWidgetAssetsTable table, AgWidgetAssetsColumnsParam param, String deleteColumnIds) {
        if (table == null) {
            return new ContentResultForm(false, null, "微件资产表参数为空");
        }
        if (StringUtils.isEmpty(table.getId())) {
            return new ContentResultForm(false, null, "id参数为空");
        }
        if (StringUtils.isEmpty(table.getTableName())) {
            return new ContentResultForm(false, null, "tableName参数为空");
        }
        try {
            thematicTableService.update(table, param != null ? param.getColumns() : null, deleteColumnIds);
            return new ContentResultForm(true, null, "修改成功");
        } catch (SourceException e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, e.getMessage());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "修改失败");
        }
    }


    @GetMapping("/get")
    @ApiOperation(value = "获取信息", notes = "获取信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramType", value = "paramType=1查询微件资产表所有属性，参数thematicTableId；", dataType = "String"),
            @ApiImplicitParam(name = "thematicTableId", value = "微件资产表id", dataType = "String"),

    })
    public ContentResultForm get(String paramType, String thematicTableId) {
        try {
            if ("1".equals(paramType)) {
                if (StringUtils.isEmpty(thematicTableId)) {
                    return new ContentResultForm(false, null, "thematicTableId参数为空");
                }
                List<AgWidgetAssetsColumns> thematicColumns = thematicTableService.getThematicColumns(thematicTableId);
                return new ContentResultForm(true, thematicColumns);
            }
            return new ContentResultForm(false, null, "paramType参数错误");
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "查询失败");
        }
    }

    @Autowired
    private OpuRsAppService opuRsAppService;

    @GetMapping({"/appSoft/list"})
    @ApiIgnore
    public List<OpuRsAppSoft> listAppSoftByCond(OpuRsAppSoft appSoft) {


        appSoft.setIsNeedFullPath(true);
        appSoft.setSoftDeleted("0");
        logger.debug("获取当前顶级机构应用程序列表,不带分页", appSoft);
        return this.opuRsAppService.listOpuRsAppSoftNoPage(appSoft);
    }

}
