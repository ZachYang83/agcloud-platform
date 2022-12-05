package com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.controller;


import com.alibaba.fastjson.JSON;
import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.common.util.io.FileUtil;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.service.IAgWidgetAssetsTableService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * @Description TODO
 * @Author lizih
 * @Date 2020/11/19
 * @Version 1.0
 */

@Api(value = "微件资产相关接口", description = "微件资产相关接口")
@RestController
@RequestMapping("/agsupport/configManager/widgetAssetsTable")
public class AgWidgetAssetsTableController {
    private static final Logger logger = LoggerFactory.getLogger(AgWidgetAssetsTableController.class);

    @Autowired
    private IAgWidgetAssetsTableService agThematicTableService;

    @GetMapping("/find")
    @ApiOperation(value = "分页条件查询专题表", notes = "分页查询专题表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "softCode", value = "项目编码", dataType = "String", required = true),
            @ApiImplicitParam(name = "tableName", value = "专题表名", dataType = "String", required = true),
            @ApiImplicitParam(name = "selectColumns", value = "需要查询的属性，可以多个，用逗号隔开。如果不输入，默认查询全部属性", dataType = "String", defaultValue = "*"),
            @ApiImplicitParam(name = "searchCondition", value = "sql语句where之后的内容", dataType = "String", defaultValue = "1=1"),
            @ApiImplicitParam(name = "orderByColumn", value = "单个属性名：查询结果按照某一个属性排序。如果不输入，默认按照name属性排序", dataType = "String", defaultValue = "name"),
            @ApiImplicitParam(name = "orderDesc", value = "升降序排列 0：升序排列 1:降序排列。如果不输入，默认升序", dataType = "String", defaultValue = "0"),
            @ApiImplicitParam(name = "rows", value = "分页参数:page=1&rows=10", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "String"),
    })
    public ContentResultForm find(String softCode,
                                  String tableName,
                                  @RequestParam(value = "selectColumns", defaultValue = "*") String selectColumns,
                                  @RequestParam(value = "searchCondition", defaultValue = "1=1") String searchCondition,
                                  @RequestParam(value = "orderByColumn", defaultValue = "name") String orderByColumn,
                                  @RequestParam(value = "orderDesc", defaultValue = "0") String orderDesc,
                                  Page page) {
        try {
            if (StringUtils.isEmpty(softCode) || StringUtils.isEmpty(tableName.trim())) {
                //softCode，tableName不能为空
                return new ContentResultForm(false, null, "softCode and tableName should not be empty. ");
            }
            if (!"0".equals(orderDesc) && !"1".equals(orderDesc)) {
                // orderDesc 必须是0：升序 或者 1:降序
                return new ContentResultForm(false, null, "orderDesc should be 0 or 1 ");
            }
            PageInfo<Object> list = agThematicTableService.findConditionPage(softCode, tableName, selectColumns, searchCondition, orderByColumn, orderDesc, page);
            return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(list));

        } catch (SourceException se) {
            logger.info(se.getMessage());
            return new ContentResultForm(false, null, se.getMessage());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "查询失败，请检查输入是否符合规则");
        }
    }


    @GetMapping("/get")
    @ApiOperation(value = "条件查询专题表", notes = "查询专题表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "softCode", value = "项目编码", dataType = "String", required = true),
            @ApiImplicitParam(name = "tableName", value = "专题表名", dataType = "String", required = true),
            @ApiImplicitParam(name = "selectColumns", value = "查询的属性，可以多个，用逗号隔开。不传参则默认查询全部属性", dataType = "String", defaultValue = "*"),
            @ApiImplicitParam(name = "searchCondition", value = "sql语句where之后的内容", dataType = "String", defaultValue = "1=1"),
            @ApiImplicitParam(name = "orderByColumn", value = "单个属性名：查询结果按照某一个属性排序。默认按照name排序", dataType = "String", defaultValue = "name"),
            @ApiImplicitParam(name = "orderDesc", value = "升降序排列 0：升序排列 1:降序排列。默认升序", dataType = "String", defaultValue = "0"),
    })
    public ContentResultForm get(String softCode,
                                 String tableName,
                                 @RequestParam(value = "selectColumns", defaultValue = "*") String selectColumns,
                                 @RequestParam(value = "searchCondition", defaultValue = "1=1") String searchCondition,
                                 @RequestParam(value = "orderByColumn", defaultValue = "name") String orderByColumn,
                                 @RequestParam(value = "orderDesc", defaultValue = "0") String orderDesc) {
        try {
            if (StringUtils.isEmpty(softCode) || StringUtils.isEmpty(tableName.trim())) {
                //softCode，tableName不能为空
                return new ContentResultForm(false, null, "softCode and tableName should not be empty. ");
            }
            if (!"0".equals(orderDesc) && !"1".equals(orderDesc)) {
                // orderDesc 必须是0：升序 或者 1:降序
                return new ContentResultForm(false, null, "orderDesc should be 0 or 1 ");
            }
            List<Object> list = agThematicTableService.findCondition(softCode, tableName, selectColumns, searchCondition, orderByColumn, orderDesc);
            return new ContentResultForm(true, list);

        } catch (SourceException se) {
            logger.info(se.getMessage());
            return new ContentResultForm(false, null, se.getMessage());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "查询失败，请检查输入是否符合规则");
        }
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加专题表相关数据", notes = "添加专题表相关数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "softCode", value = "项目编码", dataType = "String", required = true),
            @ApiImplicitParam(name = "tableName", value = "专题表名", dataType = "String", required = true),
            @ApiImplicitParam(name = "param自定义参数", value = "其他自定义参数需要根据后台配置的表的属性进行添加；主键id可以用UUID生成，后端不维护", dataType = "String"),
    })
    public ContentResultForm add(String softCode, String tableName, HttpServletRequest request) {
        if (StringUtils.isEmpty(softCode)) {
            return new ContentResultForm(false, null, "softCode参数不能为空");
        }
        if (StringUtils.isEmpty(tableName)) {
            return new ContentResultForm(false, null, "tableName参数不能为空");
        }
        try {
            agThematicTableService.add(softCode, tableName, request);
            return new ContentResultForm(true, null, "添加成功");
        } catch (SourceException se) {
            logger.info(se.getMessage());
            return new ContentResultForm(false, null, se.getMessage());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "添加失败，请检查输入是否符合规则");
        }
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改专题表相关数据", notes = "修改专题表相关数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "softCode", value = "项目编码", dataType = "String", required = true),
            @ApiImplicitParam(name = "tableName", value = "专题表名", dataType = "String", required = true),
            @ApiImplicitParam(name = "param自定义参数", value = "其他自定义参数需要根据后台配置的表的属性进行修改；通过主键id进行修改", dataType = "String"),
    })
    public ContentResultForm update(String softCode, String tableName, HttpServletRequest request) {
        if (StringUtils.isEmpty(softCode)) {
            return new ContentResultForm(false, null, "softCode参数不能为空");
        }
        if (StringUtils.isEmpty(tableName)) {
            return new ContentResultForm(false, null, "tableName参数不能为空");
        }
        try {
            agThematicTableService.update(softCode, tableName, request);
            return new ContentResultForm(true, null, "修改成功");
        } catch (SourceException se) {
            logger.info(se.getMessage());
            return new ContentResultForm(false, null, se.getMessage());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "修改失败，请检查输入是否符合规则");
        }
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除专题表相关数据", notes = "删除专题表相关数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "softCode", value = "项目编码", dataType = "String", required = true),
            @ApiImplicitParam(name = "tableName", value = "专题表名", dataType = "String", required = true),
            @ApiImplicitParam(name = "ids", value = "主键id，多个用逗号分隔", dataType = "String", required = true),
    })
    public ContentResultForm delete(String softCode, String tableName, String ids) {
        if (StringUtils.isEmpty(ids)) {
            return new ContentResultForm(false, null, "ids参数不能为空");
        }
        if (StringUtils.isEmpty(softCode)) {
            return new ContentResultForm(false, null, "softCode参数不能为空");
        }
        if (StringUtils.isEmpty(tableName)) {
            return new ContentResultForm(false, null, "tableName参数不能为空");
        }
        try {
            agThematicTableService.delete(softCode, tableName, ids);
            return new ContentResultForm(true, null, "删除成功");
        } catch (SourceException se) {
            logger.info(se.getMessage());
            return new ContentResultForm(false, null, se.getMessage());
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ContentResultForm(false, null, "删除失败，请检查输入是否符合规则");
        }
    }

    @GetMapping("/download")
    @ApiOperation(value = "下载专题表特定字段内容", notes = "下载专题表特定字段内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "softCode", value = "项目编码", dataType = "String", required = true),
            @ApiImplicitParam(name = "tableName", value = "专题表名", dataType = "String", required = true),
            @ApiImplicitParam(name = "id", value = "要下载文件对应的id", dataType = "String", required = true),
            @ApiImplicitParam(name = "fieldName", value = "要下载的文件的字段名", dataType = "String", required = true),
            @ApiImplicitParam(name = "fileName", value = "要下载的文件的文件名", dataType = "String", required = true)
    })
    public void download(String softCode,
                         String tableName,
                         String id,
                         String fieldName,
                         String fileName,
                         HttpServletResponse response
    ) {
        try {
            if (StringUtils.isEmpty(softCode)) {
                responseWriteError("softCode", response);
                return;
            }
            if (StringUtils.isEmpty(tableName)) {
                responseWriteError("tableName", response);
                return;
            }
            if (StringUtils.isEmpty(id)) {
                responseWriteError("id", response);
                return;
            }
            if (StringUtils.isEmpty(fieldName)) {
                responseWriteError("fieldName", response);
                return;
            }
            if (StringUtils.isEmpty(fileName)) {
                responseWriteError("fileName", response);
                return;
            }
            byte[] targetFile = agThematicTableService.getTargetFile(softCode, tableName, id, fieldName);
            if (targetFile == null){
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(JSON.toJSONString(new ContentResultForm(false, null, "This file does not exist. 该文件不存在。")));
            }
            FileUtil.writerFile(targetFile, fileName, true, response);
        } catch (SourceException se){
            logger.info("-----文件下载失败，请检查输入是否符合规则-----" + se.getMessage());
            try{
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(JSON.toJSONString(new ContentResultForm(false, null, se.getMessage())));
            } catch (Exception e){
                logger.info("response.getWriter Error" + e.getMessage());
            }
        } catch (Exception e) {
            logger.info("-----文件获取失败，请检查输入是否符合规则-----" + e.getMessage());
        }
    }

    private void responseWriteError(String missingVar, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(new ContentResultForm(false, null, missingVar + "不能为空")));
    }


    @GetMapping("/preview")
    @ApiOperation(value = "下载专题表特定字段内容", notes = "下载专题表特定字段内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "softCode", value = "项目编码", dataType = "String", required = true),
            @ApiImplicitParam(name = "tableName", value = "专题表名", dataType = "String", required = true),
            @ApiImplicitParam(name = "id", value = "要预览文件对应的id", dataType = "String", required = true),
            @ApiImplicitParam(name = "fieldName", value = "要预览的文件的字段名", dataType = "String", required = true),
            @ApiImplicitParam(name = "fileName", value = "要预览的文件的文件名", dataType = "String", required = true)
    })
    public void preview(String softCode,
                        String tableName,
                        String id,
                        String fieldName,
                        String fileName,
                        HttpServletResponse response
    ) {
        try {
            if (StringUtils.isEmpty(softCode)) {
                responseWriteError("softCode", response);
                return;
            }
            if (StringUtils.isEmpty(tableName)) {
                responseWriteError("tableName", response);
                return;
            }
            if (StringUtils.isEmpty(id)) {
                responseWriteError("id", response);
                return;
            }
            if (StringUtils.isEmpty(fieldName)) {
                responseWriteError("fieldName", response);
                return;
            }
            if (StringUtils.isEmpty(fileName)) {
                responseWriteError("fileName", response);
                return;
            }
            byte[] targetFile = agThematicTableService.getTargetFile(softCode, tableName, id, fieldName);
            if (targetFile == null){
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(JSON.toJSONString(new ContentResultForm(false, null, "This file does not exist. 该文件不存在。")));
            }
            FileUtil.writerFile(targetFile, fileName, false, response);
        } catch (SourceException se){
            logger.info("-----文件预览失败，请检查输入是否符合规则-----" + se.getMessage());
            try{
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(JSON.toJSONString(new ContentResultForm(false, null, se.getMessage())));
            } catch (Exception e){
                logger.info("response.getWriter Error" + e.getMessage());
            }
        } catch (Exception e) {
            logger.info("-----文件预览失败，请检查输入是否符合规则-----" + e.getMessage());
        }
    }

}
