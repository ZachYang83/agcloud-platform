package com.augurit.agcloud.agcom.agsupport.sc.serverEffect.controller;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerEffect;
import com.augurit.agcloud.agcom.agsupport.sc.serverEffect.service.IAgServerEffectService;
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


/**
 * <p>
 * 服务管理-效果控制类
 * </p>
 *
 * @author libc
 * @since 2020-09-29
 */
@Api(value = "服务管理-效果", description = "服务管理-效果接口")
@RestController
@RequestMapping("/agsupport/serverManager/effect")
public class AgServerEffectController {

    @Autowired
    private IAgServerEffectService iAgServerEffectService;

    @ApiOperation(value = "分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "查询参数", dataType = "AgServerEffect"),
            @ApiImplicitParam(name = "page", value = "分页参数(当前页):page=1", dataType = "String",defaultValue = "1"),
            @ApiImplicitParam(name = "rows", value = "分页参数(每页大小):rows=10", dataType = "String",defaultValue = "10"),
    })
    @GetMapping(value = "/find")
    public ContentResultForm<EasyuiPageInfo> find(AgServerEffect param, @RequestParam(value = "page",defaultValue = "1")String page, @RequestParam(value = "rows",defaultValue = "10") String rows) {
        PageInfo<AgServerEffect> pageInfo=iAgServerEffectService.find(param, new Page(Integer.valueOf(page),Integer.valueOf(rows)));
        return new ContentResultForm<EasyuiPageInfo>(true, PageHelper.toEasyuiPageInfo(pageInfo));
    }

    @ApiOperation(value = "详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "String")
    })
    @GetMapping(value = "/get")
    public ContentResultForm<AgServerEffect> get(String id) {
        AgServerEffect data = iAgServerEffectService.get(id);
        return new ContentResultForm<AgServerEffect>(true, data);
    }

    @ApiOperation(value = "新增")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "保存对象", dataType = "AgServerEffect"),
            @ApiImplicitParam(name = "file", value = "上传文件", dataType = "MultipartFile")
    })
    @PostMapping(value = "/add")
    public ResultForm add(AgServerEffect param, @RequestParam("file") MultipartFile file, HttpServletRequest request) {
        iAgServerEffectService.add(param, file,request);
        return new ResultForm(true, "添加成功");
    }

    @ApiOperation(value = "修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "修改对象", dataType = "AgServerEffect")
    })
    @PutMapping(value = "/update")
    public ResultForm update(AgServerEffect param) {
        iAgServerEffectService.update(param);
        return new ResultForm(true, "修改成功");
    }

    @ApiOperation(value = "删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "ids", dataType = "String")
    })
    @DeleteMapping(value = "/delete")
    public ResultForm delete(String ids) {
        iAgServerEffectService.delete(ids);
        return new ResultForm(true, "删除成功");
    }


}
