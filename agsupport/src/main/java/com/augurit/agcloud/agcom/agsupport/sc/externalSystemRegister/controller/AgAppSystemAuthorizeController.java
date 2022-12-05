package com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgAppSystem;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.agcom.agsupport.domain.AgUserThirdapp;
import com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.service.IAppSystemAuthorizeService;
import com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.service.IAppSystemService;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

import java.util.UUID;

/**
 * @author zhangmy
 * @Description: 第三方应用授权
 * @date 2019-10-22 10:06
 */
@Api(value = "第三方应用系统授权功能", description = "第三方应用系统授权功能接口")
@RestController
@RequestMapping("/agsupport/appSystemAuthorize")
public class AgAppSystemAuthorizeController {
    @Autowired
    private IAppSystemService appSystemService;
    @Autowired
    private IAppSystemAuthorizeService appSystemAuthorizeService;
    @Autowired
    private IAgUser iAgUser;
    @RequestMapping("/index.html")
    @ApiIgnore
    public ModelAndView index() {
        return new ModelAndView("agcloud/agcom/agsupport/appsystem/authorizeIndex");
    }

    @ApiOperation(value = "第三方应用系统数据", notes = "第三方应用系统数据接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appName", value = "系统名称", dataType = "string"),
            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "Page")
    })
    @GetMapping("/findAll")
    public ContentResultForm findAll(String appName, Page page) {
        ContentResultForm resultForm = new ContentResultForm(true);
        try {
            PageInfo<AgAppSystem> list = appSystemService.findAll(appName, page);
            resultForm.setContent(list);
        } catch (Exception e) {
            resultForm.setSuccess(false);
            resultForm.setMessage("查询数据出错");
        }
        return resultForm;
    }

    @ApiOperation(value = "第三方应用系统已授权的用户", notes = "获取第三方应用系统已授权用户接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "第三方应用系统Id", dataType = "string"),
            @ApiImplicitParam(name = "userName", value = "用户姓名", dataType = "string"),
            @ApiImplicitParam(name = "page", value = "分页参数:page=1&rows=10", dataType = "Page")
    })
    @GetMapping("/findAppUser")
    public ContentResultForm findAppUser(String appId,String userName, Page page) {
        ContentResultForm resultForm = new ContentResultForm(true);
        try {
            PageInfo<AgUser> list = appSystemAuthorizeService.findAppUser(appId,userName, page);
            resultForm.setContent(list);
        } catch (Exception e) {
            resultForm.setSuccess(false);
            resultForm.setMessage("查询数据出错");
        }
        return resultForm;
    }

    @ApiOperation(value = "第三方应用系统用户授权", notes = "第三方应用系统用户授权接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "第三方应用系统Id",required = true,dataType = "string"),
            @ApiImplicitParam(name = "userIds", value = "用户Id,多个用户使用英文逗号分隔",required = true,dataType = "string"),
    })
    @PostMapping("/authorizeAppUser")
    public ContentResultForm authorizeAppUser(String appId,String userIds) {
        ContentResultForm resultForm = new ContentResultForm(true);
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(userIds)){
            resultForm.setSuccess(false);
            resultForm.setMessage("appId 和 userIds 不能为空!");
            return resultForm;
        }
        Long orderNum = appSystemAuthorizeService.findByMaxOrder();
        if (orderNum == null){
            orderNum = 0l;
        }
        try {
            String[] userIdList = userIds.split(",");
            for (String userId : userIdList){
                AgUserThirdapp agUserThirdapp = appSystemAuthorizeService.findByAppIdAndUserId(appId, userId);
                if (agUserThirdapp == null){
                    orderNum++;
                    agUserThirdapp = new AgUserThirdapp();
                    agUserThirdapp.setAppId(appId);
                    agUserThirdapp.setUserId(userId);
                    agUserThirdapp.setId(UUID.randomUUID().toString());
                    agUserThirdapp.setOrderNo(orderNum);
                    // 默认未添加到桌面
                    agUserThirdapp.setToDesktop("0");
                    appSystemAuthorizeService.insert(agUserThirdapp);

                }
            }
        }catch (Exception e){
            resultForm.setSuccess(false);
            resultForm.setMessage("查询出错了!");
            e.printStackTrace();
        }
        return resultForm;
    }

    @ApiOperation(value = "第三方应用系统移除授权用户", notes = "第三方应用系统移除授权用户接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "第三方应用系统Id", required = true, dataType = "string"),
            @ApiImplicitParam(name = "userIds", value = "用户Id,多个用户使用英文逗号分隔",required = true, dataType = "string"),
    })
    @DeleteMapping("/delAuthorizeAppUser")
    public ContentResultForm delAuthorizeAppUser(String appId,String userIds) {
        ContentResultForm resultForm = new ContentResultForm(true);
        if (StringUtils.isBlank(appId) || StringUtils.isBlank(userIds)){
            resultForm.setSuccess(false);
            resultForm.setMessage("appId 和 userIds 不能为空!");
            return resultForm;
        }
        try {
            String[] userIdList = userIds.split(",");
            appSystemAuthorizeService.deleteByIds(userIdList,appId);
        }catch (Exception e){
            resultForm.setSuccess(false);
            resultForm.setMessage("删除出错了!");
        }
        return resultForm;
    }


}
