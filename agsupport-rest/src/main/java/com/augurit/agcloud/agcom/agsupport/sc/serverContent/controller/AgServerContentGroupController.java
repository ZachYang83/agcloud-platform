package com.augurit.agcloud.agcom.agsupport.sc.serverContent.controller;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerContentGroup;
import com.augurit.agcloud.agcom.agsupport.sc.serverContent.service.IAgServerContentGroupService;
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

import java.util.List;


/**
* <p>
* 服务内容分组表 控制类
* </p>
*
* @author libc
* @since 2020-09-23
*/
@Api(value = "服务内容分组表", description = "服务内容分组表")
@RestController
@RequestMapping("/agsupport/serverManager/contentGroup")
public class AgServerContentGroupController {

    @Autowired
    private IAgServerContentGroupService iAgServerContentGroupService;

    @ApiOperation(value = "服务内容分组表分页列表")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "param", value = "查询参数", dataType = "AgServerContentGroup"),
            @ApiImplicitParam(name = "page", value = "分页参数（当前页码）:page=1", dataType = "String",defaultValue = "1"),
            @ApiImplicitParam(name = "rows", value = "分页参数（每页大小）:rows=10", dataType = "String",defaultValue = "10"),
    })
    @GetMapping(value = "/find")
    public ContentResultForm<EasyuiPageInfo> find(AgServerContentGroup param, @RequestParam(value = "page",defaultValue = "1") String page, @RequestParam(value = "rows",defaultValue = "10")String rows) {
        PageInfo<AgServerContentGroup> data = iAgServerContentGroupService.find(param, new Page(Integer.valueOf(page),Integer.valueOf(rows)));
        return new ContentResultForm<EasyuiPageInfo>(true,  PageHelper.toEasyuiPageInfo(data));
    }

    @ApiOperation(value = "服务内容分组表详情")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "paramType", value = "paramType=1 根据id查询（默认）；paramType=2查询所有", defaultValue = "1",dataType = "String"),
        @ApiImplicitParam(name = "id", value = "服务内容分组表id", dataType = "String")
    })
    @GetMapping(value = "/get")
    public ContentResultForm get(@RequestParam(value = "paramType",defaultValue = "1") String paramType, String id) {
        if ("1".equals(paramType)){
            // 根据id查询
            AgServerContentGroup scg = iAgServerContentGroupService.get(id);
            return new ContentResultForm<AgServerContentGroup>(true, scg);
        }else if ("2".equals(paramType)){
            // 查询所有
            List<AgServerContentGroup> data = iAgServerContentGroupService.findAll();
            return new ContentResultForm<List<AgServerContentGroup>>(true, data);
        }
        return new ContentResultForm<AgServerContentGroup>(true, null,"未查询到数据！");
    }

    @ApiOperation(value = "服务内容分组表新增")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "param", value = "保存对象", dataType = "AgServerContentGroup")
    })
    @PostMapping(value = "/add")
    public ResultForm add(AgServerContentGroup param) {
        iAgServerContentGroupService.add(param);
        return new ResultForm(true, "添加成功");
    }

    @ApiOperation(value = "服务内容分组表修改")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "param", value = "修改对象", dataType = "AgServerContentGroup")
    })
    @PutMapping(value = "/update")
    public ResultForm update(AgServerContentGroup param) {
        iAgServerContentGroupService.update(param);
        return new ResultForm(true, "修改成功");
    }

    @ApiOperation(value = "服务内容分组表删除(单个条目)")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "服务内容分组表id", dataType = "String")
    })
    @DeleteMapping(value = "/delete")
    public ResultForm delete(String id) {
        iAgServerContentGroupService.delete(id);
        return new ResultForm(true, "删除成功");
    }



}
