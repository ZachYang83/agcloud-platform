package com.augurit.agcloud.agcom.agsupport.sc.func.controller;

import com.augurit.agcloud.agcom.agsupport.common.aspect.RestLog;
import com.augurit.agcloud.agcom.agsupport.domain.AgFunc;
import com.augurit.agcloud.agcom.agsupport.domain.OpuRsToolBar;
import com.augurit.agcloud.agcom.agsupport.sc.func.service.IAgFunc;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.opus.common.domain.OpuRsAppSoft;
import com.augurit.agcom.common.LoginHelpClient;
import com.common.util.Common;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Augurit on 2017-05-02.
 */
@Api(value = "功能", description = "功能接口")
@RestController
@RequestMapping("/agsupport/func")
public class AgFuncController {

    private static Logger logger = LoggerFactory.getLogger(AgFuncController.class);

    @Autowired
    private IAgFunc iAgFunc;

    /**
     * 根据用户id获取功能
     *
     * @param userId      用户id
     * @param isTree      是否返回树状结构
     * @param appSoftCode 应用名称
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据用户id获取功能", notes = "根据用户id获取功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", required = true, value = "用户id", dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "isMobile", required = true, value = "isMobile", dataType = "String"),
            @ApiImplicitParam(name = "isTree", required = false, value = "是否返回树状结构", dataType = "Boolean"),
            @ApiImplicitParam(name = "appSoftCode", required = true, value = "应用名称", dataType = "String"),
            @ApiImplicitParam(name = "isCloudSoft", required = true, value = "是否云应用（0：自建应用，1：云应用）", dataType = "String"),
            @ApiImplicitParam(name = "isAdmin", value = "是否管理端（0：用户端，1：管理端）", dataType = "String")
    })
    @RestLog(value = "根据用户id获取功能")
    @RequestMapping(value = "/findFuncByUser/{userId}", method = RequestMethod.GET)
    public ContentResultForm findFuncByUser(@PathVariable("userId") String userId, String isMobile, Boolean isTree, String appSoftCode, String isCloudSoft, String isAdmin) {
        if (Common.isCheckNull(userId)) {
            userId = null;
        }
        if (!"T".equals(isMobile)) {
            isMobile = "F";
        }
        if (isTree == null) {
            isTree = false;
        }
        List<AgFunc> widgets = null;
        try {
            widgets = iAgFunc.findFuncByUser(userId, isMobile, isTree, appSoftCode, isCloudSoft, isAdmin);
            return new ContentResultForm(true, widgets, "");
        } catch (Exception e) {
            return new ContentResultForm(false, "", "获取失败");
        }
    }

    /**
     * 获取日志基本信息 2017-12-07
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

    @GetMapping({"/getToolBar"})
    public List<ElementUiRsTreeNode> getToolBarByAppSoftId(String appSoftCode, String funcCode) {
        if (StringUtils.isBlank(appSoftCode) || StringUtils.isBlank(funcCode)) {
            throw new InvalidParameterException(new Object[]{"传递参数appSoftIdfuncCode和为空！"});
        } else {
            logger.info("通过id获取功能数据" + appSoftCode + "==" + funcCode);
            OpuRsAppSoft soft = this.iAgFunc.getAppSoftBySoftCode(appSoftCode);
            logger.info("通过应用工程id获取功能集合,树列表结构,id=" + soft.getAppSoftId());
            List<ElementUiRsTreeNode> result = this.iAgFunc.lltreeFunc(soft.getAppSoftId(), funcCode, "", true);
            List<ElementUiRsTreeNode> treeResult = new ArrayList<>();
            for (ElementUiRsTreeNode node : result) {
                OpuRsToolBar bar = (OpuRsToolBar) node.getData();
                if (bar.getFuncCode().equals(funcCode)) {
                    treeResult.add(node);
                }
            }
            return treeResult;
        }
    }
}
