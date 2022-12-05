package com.augurit.agcloud.agcom.agsupport.sc.site.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author zhangmy
 * @Description: 图层服务管理
 * @date 2019-11-11 13:39
 */
@Api(value = "Arcgis服务管理",description = "Arcgis服务管理接口")
@RestController
@RequestMapping("/agsupport/serviceManager")
public class AgLayerServiceManagerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgLayerServiceManagerController.class);

    @Autowired
    private IAgDir agDir;

    @ApiIgnore
    @RequestMapping("/index.html")
    public ModelAndView index(){
        return new ModelAndView("agcloud/agcom/agsupport/serviceManager/index.html");
    }

    @ApiOperation(value = "查询所有服务",notes = "查询所有服务接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "服务名称",dataType = "String"),
            @ApiImplicitParam(name = "status",value = "服务操作 0：已启动服务 1：已停止服务 2：已被禁用的服务",dataType = "String"),
            @ApiImplicitParam(name = "page",value = "分页参数:page=1&rows=10",dataType = "String")
    })
    @GetMapping("/findList")
    public ContentResultForm findList(String name, String status, Page page) throws Exception{
        PageInfo<AgLayer> agLayers = agDir.findLayerByNameAndStatus(name, status, page);
        ContentResultForm resultForm = new ContentResultForm(true,agLayers);
        return resultForm;
    }

    @ApiOperation(value = "启动服务",notes = "启动服务接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "服务的ID,多个服务使用英文逗号分隔",required = true,dataType = "String"),
    })
    @PostMapping("/start")
    public ContentResultForm start(String ids, HttpServletRequest request){
        ContentResultForm resultForm = new ContentResultForm(true,"","启动服务成功");
        try {
            agDir.changeStatus(ids,"0",request.getRemoteHost());
        }catch (Exception e){
            resultForm.setSuccess(false);
            resultForm.setMessage("服务资源无效，请检查!");
        }
        return resultForm;
    }

    @ApiOperation(value = "停止服务",notes = "停止服务接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "服务的ID,多个服务使用英文逗号分隔",required = true,dataType = "String"),
    })
    @PostMapping("/stop")
    public ContentResultForm stop(String ids, HttpServletRequest request){
        ContentResultForm resultForm = new ContentResultForm(true,"","停止服务成功");
        try {
            agDir.changeStatus(ids,"1",request.getRemoteHost());
        }catch (Exception e){
            resultForm.setSuccess(false);
            resultForm.setMessage("服务资源无效，请检查!");
        }
        return resultForm;
    }

    @ApiOperation(value = "禁用服务",notes = "禁用服务接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids",value = "服务的ID,多个服务使用英文逗号分隔",required = true,dataType = "String"),
    })
    @PostMapping("/disable")
    public ContentResultForm disable(String ids, HttpServletRequest request){
        ContentResultForm resultForm = new ContentResultForm(true,"","禁用服务成功");
        try {
            agDir.disable(ids,"2");
        }catch (Exception e){
            resultForm.setSuccess(false);
            resultForm.setMessage("服务资源无效，请检查!!");
        }
        return resultForm;
    }

}
