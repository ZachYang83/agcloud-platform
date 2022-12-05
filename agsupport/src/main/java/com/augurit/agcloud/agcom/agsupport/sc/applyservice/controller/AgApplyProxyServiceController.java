package com.augurit.agcloud.agcom.agsupport.sc.applyservice.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgProxyService;
import com.augurit.agcloud.agcom.agsupport.sc.applyservice.service.AgApplyProxyService;
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
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author zhangmy
 * @Description: TODO
 * @date 2019-12-24 14:38
 */
@Api(value = "外部服务审核",description = "外部服务审核接口")
@RestController
@RequestMapping("agsupport/proxyService")
public class AgApplyProxyServiceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgApplyProxyServiceController.class);
    @Autowired
    private AgApplyProxyService agApplyProxyService;
    @ApiIgnore
    @RequestMapping("/index.html")
    public ModelAndView index(){
        return new ModelAndView("agcloud/agcom/agsupport/applyservice/index");
    }

    @ApiOperation(value = "审核外部服务",notes = "审核外部服务接口")
    @ApiImplicitParam(name = "state",value = "服务审核状态,0:审核中 1：通过 2：拒绝通过")
    @PostMapping("/applyProxyService/{id}/{state}")
    public ContentResultForm applyProxyService(@PathVariable("id") String id,@PathVariable("state") String state,String approveOpinion) throws Exception{
        agApplyProxyService.updateProxyServiceState(id,state,approveOpinion);
        return new ContentResultForm(true);
    }
    @ApiOperation(value = "查询审核服务",notes = "查询审核服务信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName",value = "账户名"),
            @ApiImplicitParam(name = "state",value = "服务审核状态,0:审核中 1：通过 2：拒绝通过"),
            @ApiImplicitParam(name = "page",value = "分页参数:page=1&rows=10",dataType = "Page")
    })
    @GetMapping("/findList")
    public ContentResultForm findList(AgProxyService agProxyService, Page page) throws Exception{
        PageInfo<AgProxyService> list = agApplyProxyService.findList(agProxyService,page);
        return new ContentResultForm(true,list);
    }

    @ApiOperation(value = "删除审核记录",notes = "删除审核记录接口")
    @ApiImplicitParam(name = "ids",value = "记录Id")
    @DeleteMapping("/delete")
    public ContentResultForm delete(String ids) throws Exception{
        agApplyProxyService.deleteBatch(ids);
        return new ContentResultForm(true,"删除成功");
    }
}
