package com.augurit.agcloud.agcom.agsupport.sc.menuDir.controller;

import com.augurit.agcloud.agcom.agsupport.sc.log.service.IAgLog;
import com.augurit.agcloud.agcom.agsupport.sc.menuDir.service.IAgMenuDir;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.JsonMapper;
import com.augurit.agcom.common.LoginHelpClient;
import com.common.util.Common;
import com.fasterxml.jackson.annotation.JsonInclude;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-11-21.
 */
@Api(value = "菜单目录接口",description = "菜单目录接口")
@RestController
@RequestMapping("/agsupport/menuDir")
public class AgMenuDirController {

    @Autowired
    private IAgMenuDir iAgMenuDir;

    @Autowired
    private IAgLog log;
    /**
     * 根据用户名获取应用
     * @param request
     * @param userName
     * @return
     */
    @ApiOperation(value = "根据用户名获取应用",notes = "根据用户名获取应用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName" ,required = true,value = "用户名",dataType = "String")
    })
    @RequestMapping(value = "/findSoftMenu",method = RequestMethod.GET)
    public ContentResultForm findSoftMenu(HttpServletRequest request, String userName) throws Exception{
        List softMenu = iAgMenuDir.findSoftMenu(userName);
        JsonMapper mapper = new JsonMapper(JsonInclude.Include.ALWAYS);//不去掉为空字段
        return new ContentResultForm(true,mapper.toJson(softMenu));
    }


    /**
     * 获取日志基本信息 2018-3-12
     *
     * @param request
     * @param funcName
     * @return
     */
    private JSONObject getLogInfo(HttpServletRequest request, String funcName) {
        JSONObject jsonObject = new JSONObject();
        String loginName = LoginHelpClient.getLoginName(request);
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        jsonObject.put("loginName", loginName);
        jsonObject.put("sysName", "agsupport");
        jsonObject.put("ipAddress", Common.getIpAddr(request));
        jsonObject.put("browser", userAgent.getBrowser().getName());
        jsonObject.put("funcName", funcName);
        return jsonObject;
    }
}
