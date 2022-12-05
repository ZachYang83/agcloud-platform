package com.augurit.agcloud.agcom.agsupport.sc.serverContent.controller;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerContent;
import com.augurit.agcloud.agcom.agsupport.sc.serverContent.service.IAgServerContentService;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p>
 * 内容管理控制类
 * </p>
 *
 * @author libc
 * @since 2020-09-15
 */
@Api(value = "内容管理", description = "内容管理接口")
@RestController
@RequestMapping("/agsupport/serverManager/content")
public class AgServerContentController {


    @Autowired
    private IAgServerContentService iAgContentService;


    @ApiOperation(value = "分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "查询参数", dataType = "AgServerContent"),
            @ApiImplicitParam(name = "page", value = "分页参数（当前页码）:page=1", dataType = "String",defaultValue = "1"),
            @ApiImplicitParam(name = "rows", value = "分页参数（每页大小）:rows=10", dataType = "String",defaultValue = "10"),
    })
    @GetMapping(value = "/find")
    public ContentResultForm<EasyuiPageInfo> find(AgServerContent param, @RequestParam(value = "page",defaultValue = "1") String page,@RequestParam(value = "rows",defaultValue = "10")String rows) {
        PageInfo<AgServerContent> pageInfo=iAgContentService.find(param, new Page(Integer.valueOf(page),Integer.valueOf(rows)));
        return new ContentResultForm<EasyuiPageInfo>(true, PageHelper.toEasyuiPageInfo(pageInfo));
    }

    @ApiOperation(value = "详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "String")
    })
    @GetMapping(value = "/get")
    public ContentResultForm<AgServerContent> get(String id) {
        AgServerContent data = iAgContentService.get(id);
        return new ContentResultForm<AgServerContent>(true, data);
    }

    @ApiOperation(value = "新增")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "保存对象", dataType = "AgContent"),
            @ApiImplicitParam(name = "file", value = "上传zip文件", dataType = "MultipartFile"),
            @ApiImplicitParam(name = "paramType", value = "文件来源类型。1：agcim服务管理； 2：BIM审查'", dataType = "String",required = true),
            @ApiImplicitParam(name = "sourceRelId", value = "文件来源关联的业务ID （BIM审查关联BIM审查项目id）", dataType = "String")
    })
    @PostMapping(value = "/add")
    public ResultForm add(AgServerContent param, @RequestParam("file") MultipartFile file, HttpServletRequest request,String paramType,String sourceRelId) {
        iAgContentService.add(param,file,request,paramType,sourceRelId);
        return new ResultForm(true, "添加成功");
    }

    @ApiOperation(value = "修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "修改对象", dataType = "AgContent")
    })
    @PutMapping(value = "/update")
    public ResultForm update(AgServerContent param) {
        iAgContentService.update(param);
        return new ResultForm(true, "修改成功");
    }

    @ApiOperation(value = "删除(单个条目)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "String")
    })
    @DeleteMapping(value = "/delete")
    public ResultForm delete(String id) {
        iAgContentService.delete(id);
        return new ResultForm(true, "删除成功");
    }


    @ApiOperation(value = "3dtiles文件下载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "String")
    })
    @GetMapping(value = "/download")
    public void download(String id, HttpServletRequest request , HttpServletResponse response) {
        iAgContentService.download(id,request,response);
    }




}
