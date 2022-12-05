package com.augurit.agcloud.agcom.agsupport.sc.marker.controller;


import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.agcom.agsupport.common.aspect.RestLog;
import com.augurit.agcloud.agcom.agsupport.domain.AgMarkRemind;
import com.augurit.agcloud.agcom.agsupport.domain.AgMarker;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.agcom.agsupport.domain.AgUserMarker;
import com.augurit.agcloud.agcom.agsupport.sc.marker.services.IAgMarker;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.agcom.agsupport.sc.websocket.WebSocketServer;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.common.util.Common;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by yzq on 2019-10-20.
 */
@Api(value = "标注接口", description = "标注相关接口")
@RestController
@RequestMapping("/agsupport/marker")
public class AgMarkerController {
    @Autowired
    private IAgMarker IAgMarker;
    @Autowired
    private IAgUser iAgUser;
    /**
     * WebSocketServer 是多对象，静态注入
     */
    private static WebSocketServer webSocketServer;

    @Autowired
    public void setWebSocketServer(WebSocketServer webSocketServer) {
        AgMarkerController.webSocketServer = webSocketServer;
    }

    @ApiOperation(value = "获取标注接口", notes = "获取标注")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String")
    })
    @RestLog(value = "获取标注接口")
    @RequestMapping(value = "/getMarkers", method = RequestMethod.POST)
    public ContentResultForm getMarkers(String userId) throws Exception {
        AgUser user = iAgUser.findUserById(userId);
        if (Common.isCheckNull(user)) {
            JSONArray jsonArray = new JSONArray();
            return new ContentResultForm<JSONArray>(false, jsonArray, "找不到用户");
        }
        List result = IAgMarker.getMarkers(user.getId());
        return new ContentResultForm(true, result);

    }

    @ApiOperation(value = "分享标注接口", notes = "分享标注")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(name = "markId", value = "标注id", dataType = "String"),
            @ApiImplicitParam(name = "userName", value = "分享人", dataType = "String")
    })
    @RestLog(value = "分享标注接口")
    @RequestMapping(value = "/shareMarker", method = RequestMethod.POST)
    public ResultForm shareMarker(String userId, String markId, String userName) throws Exception {
//        AgUser user = iAgUser.findUserById(userId);
//        if(Common.isCheckNull(user)){
//            JSONArray jsonArray = new JSONArray();
//            return new ContentResultForm<JSONArray>(false,jsonArray,"找不到用户");
//        }
        // 未分享过的用户需要发送通知消息
        String[] userIds = userId.split(",");
        Map userIdMap = new HashMap();
        for (String id : userIds) {
            AgUserMarker userMarker = IAgMarker.getUserMarker(id, markId);
            if (userMarker == null) {
                userIdMap.put(id, true);
            }
        }
        if (userIdMap.size() > 0) {
            webSocketServer.broadCastInfo(userName, userIdMap);
        }
        IAgMarker.shareMarker(userId, markId, userName);
        return new ResultForm(true, "保存成功");
    }

    @ApiOperation(value = "删除标注接口", notes = "删除标注")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(name = "markId", value = "标注id", dataType = "String")
    })
    @RestLog(value = "删除标注接口")
    @RequestMapping(value = "/deleteMarker", method = RequestMethod.POST)
    public ResultForm deleteMarker(String userId, String markId) throws Exception {
        AgUser user = iAgUser.findUserById(userId);
        if (Common.isCheckNull(user)) {
            JSONArray jsonArray = new JSONArray();
            return new ContentResultForm<JSONArray>(false, jsonArray, "找不到用户");
        }
        IAgMarker.deleteMarker(userId, markId);
        return new ResultForm(true, "保存成功");
    }

    ;

    /**
     * 保存或修改标注
     *
     * @param agMarker
     * @returnayer
     */
    @ApiOperation(value = "保存或修改标注", notes = "保存或修改标注")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agMarker", value = "用户id", dataType = "AgMarker"),
            @ApiImplicitParam(name = "userId", value = "用户id", dataType = "String")

    })
    @RestLog(value = "保存或修改标注")
    @ResponseBody
    @RequestMapping(value = "/saveMarker", method = RequestMethod.POST)
    public ResultForm saveMarker(AgMarker agMarker, String userId) {
        String funcName = null;
        try {
            if (StringUtils.isNotEmpty(agMarker.getId())) {
                funcName = "修改图层";
                IAgMarker.updateMarker(agMarker, userId);
            } else {
                funcName = "新增图层";
                agMarker.setId(UUID.randomUUID().toString());
                IAgMarker.saveMarker(agMarker, userId);
            }
            return new ResultForm(true, funcName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false, "保存失败，请联系管理员！");

    }


    @ApiOperation(value = "分享的用户已阅读标注", notes = "分享的用户已阅读标注接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "markId", value = "标注id", required = true, dataType = "String")
    })
    @RestLog(value = "分享的用户已阅读标注")
    @RequestMapping(value = "/readMarker", method = RequestMethod.POST)
    public ResultForm readMarker(String userId, String markId) throws Exception {
        IAgMarker.readMarker(userId, markId);
        return new ResultForm(true, "保存成功");

    }

    @ApiOperation(value = "开启或关闭分享标注提醒功能", notes = "开启或关闭分享标注提醒功能接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "state", value = "开启状态 0：关闭提醒功能；1：开启提醒功能", required = true, dataType = "String")
    })
    @RestLog(value = "开启或关闭分享标注提醒功能")
    @PostMapping("/markRemindFunction")
    public ResultForm markRemindFunction(String userId, String state) throws Exception {
        if ("0".equals(state)) {
            AgMarkRemind agMarkRemind = IAgMarker.findMarkRemindByUserId(userId);
            if (agMarkRemind == null) {
                agMarkRemind = new AgMarkRemind();
                agMarkRemind.setId(UUID.randomUUID().toString());
                agMarkRemind.setState(state);
                agMarkRemind.setUserId(userId);
                IAgMarker.saveMarkRemind(agMarkRemind);
            } else {
                agMarkRemind.setState(state);
                IAgMarker.updateMarkRemind(agMarkRemind);
            }
        } else {
            AgMarkRemind agMarkRemind = IAgMarker.findMarkRemindByUserId(userId);
            agMarkRemind.setState(state);
            IAgMarker.updateMarkRemind(agMarkRemind);
        }
        return new ResultForm(true, "保存成功");
    }

    @ApiOperation(value = "获取用户分享标注提醒功能的状态", notes = "获取分享标注提醒功能的状态的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "String")
    })
    @RestLog(value = "获取用户分享标注提醒功能的状态")
    @GetMapping("/getRemindFunction")
    public ContentResultForm getRemindFunction(String userId) throws Exception {
        AgMarkRemind agMarkRemind = IAgMarker.findMarkRemindByUserId(userId);
        if (agMarkRemind == null) {
            agMarkRemind = new AgMarkRemind();
            agMarkRemind.setUserId(userId);
            agMarkRemind.setState("1");
        }
        return new ContentResultForm(true, agMarkRemind, "保存成功");
    }
}
