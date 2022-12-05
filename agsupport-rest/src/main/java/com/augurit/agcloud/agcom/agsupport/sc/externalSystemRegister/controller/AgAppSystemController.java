package com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgAppSystem;
import com.augurit.agcloud.agcom.agsupport.domain.AgUserThirdapp;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.service.IAppSystemAuthorizeService;
import com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.service.IAppSystemService;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangmy
 * @Description: 个性化桌面功能接口
 * @date 2019-10-14 16:31
 */
@Api(value = "个性化桌面功能", description = "个性化桌面功能接口")
@RestController()
@RequestMapping("/agsupport/appsystem")
public class AgAppSystemController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AgAppSystemController.class);
    public static final String systemIconPath = "app_system_icon/";
    @Autowired
    private IAppSystemService appSystemService;

    @Autowired
    private IAppSystemAuthorizeService appSystemAuthorizeService;
    @ApiOperation(value = "个性化桌面第三方应用系统查询所有数据", notes = "个性化桌面第三方应用系统查询所有数据接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id",required = true,dataType = "string"),
            @ApiImplicitParam(name = "appName", value = "系统名称", dataType = "string"),
    })
    @GetMapping("/find")
    public ContentResultForm find(String userId,String appName) {
        ContentResultForm resultForm = new ContentResultForm(true);
        List<AgAppSystem> hasAdd = new ArrayList<>();
        List<AgAppSystem> noAdd = new ArrayList<>();
        // 按顺序查出用户已授权的应用
        List<AgUserThirdapp> agUserThirdapps = appSystemAuthorizeService.findByUserId(userId);
        List<String> collect = agUserThirdapps.stream().map(AgUserThirdapp::getAppId).collect(Collectors.toList());
        List<AgAppSystem> agAppSystemList = appSystemService.findByIds(collect);
        Map<String,AgAppSystem> appmap = new HashMap();
        for (AgAppSystem agAppSystem : agAppSystemList){
            appmap.put(agAppSystem.getId(),agAppSystem);
        }

        for (AgUserThirdapp agUserThirdapp : agUserThirdapps){
            String appId = agUserThirdapp.getAppId();
            String toDesktop = agUserThirdapp.getToDesktop();
            //AgAppSystem appSystem = appSystemService.findById(appId);
            AgAppSystem appSystem = appmap.get(appId);
            if ("0".equals(toDesktop)){
                noAdd.add(appSystem);
            }else {
                hasAdd.add(appSystem);
            }
        }
        // 如果通过名称查询
        if (StringUtils.isNotBlank(appName)){
            List<AgAppSystem> hasAddByname = new ArrayList<>();
            List<AgAppSystem> noAddByname = new ArrayList<>();
            for (AgAppSystem agAppSystem : hasAdd){
                if (agAppSystem.getAppName().contains(appName)){
                    hasAddByname.add(agAppSystem);
                }
            }
            for (AgAppSystem agAppSystem : noAdd){
                if (agAppSystem.getAppName().contains(appName)){
                    noAddByname.add(agAppSystem);
                }
            }
            HashMap map = new HashMap();
            map.put("hasAdd",hasAddByname);
            map.put("noAdd",noAddByname);
            resultForm.setContent(map);
            return resultForm;
        }

        HashMap map = new HashMap();
        map.put("hasAdd",hasAdd);
        map.put("noAdd",noAdd);
        resultForm.setContent(map);
        return resultForm;
    }

    @ApiOperation(value = "个性化桌面添加或者移除应用系统", notes = "个性化桌面添加或者移除应用系统接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true, dataType = "string"),
            @ApiImplicitParam(name = "appId", value = "添加到桌面的应用系统id", required = true, dataType = "string"),
            @ApiImplicitParam(name = "toDesktop", value = "添加到桌面的状态，0:从桌面移除系统 1:系统添加到桌面", required = true, dataType = "string"),
    })
    @GetMapping("/systemAuthorize")
    public ContentResultForm systemAuthorize(String userId, String appId, String toDesktop) {
        ContentResultForm resultForm = new ContentResultForm(true);
        try {
            AgUserThirdapp ag = appSystemAuthorizeService.findByAppIdAndUserId(appId, userId);
            ag.setToDesktop(toDesktop);
            appSystemAuthorizeService.update(ag);
        }catch (Exception e){
            resultForm.setSuccess(false);
            resultForm.setMessage("添加出错了!");
            LOGGER.error("添加出错!",e);
        }
        return resultForm;
    }

    @ApiOperation(value = "个性化桌面应用系统拖拽", notes = "个性化桌面应用系统拖拽接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true, dataType = "string"),
            @ApiImplicitParam(name = "appIds", value = "应用系统移动后的应用Id拼接成的字符串,英文逗号分隔", required = true, dataType = "string"),
    })
    @GetMapping("/systemMove")
    public ContentResultForm systemMove(String userId,String appIds) {
        ContentResultForm resultForm = new ContentResultForm(true);
        String[] appSysIds = null;
        if(StringUtils.isNotBlank(appIds)){
            appSysIds = appIds.split(",");
        }
        try {
            List<AgUserThirdapp> newList = new ArrayList<>();
            if (appSysIds.length>0){
                List<AgUserThirdapp> agUserThirdappList = appSystemAuthorizeService.findByUserIdAndAppIds(userId, appSysIds);
                for (int i=0;i<appSysIds.length;i++){
                    // 获取拖拽后的顺序的应用Id
                    AgUserThirdapp app = appSystemAuthorizeService.findByAppIdAndUserId(appSysIds[i], userId);
                    AgUserThirdapp userThirdapp = new AgUserThirdapp();
                    userThirdapp.setId(app.getId());
                    userThirdapp.setOrderNo(agUserThirdappList.get(i).getOrderNo());
                    newList.add(userThirdapp);
                }
            }
            if (newList.size()>0){
                appSystemAuthorizeService.updateBatch(newList);
            }
        }catch (Exception e){
            resultForm.setSuccess(false);
            resultForm.setMessage("修改数据出错!");
            LOGGER.error(e.getMessage());
        }
        return resultForm;
    }

}
