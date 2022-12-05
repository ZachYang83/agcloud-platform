package com.augurit.agcloud.agcom.agsupport.sc.websocket.controller;

import com.augurit.agcloud.agcom.agsupport.sc.marker.controller.AgMarkerController;
import com.augurit.agcloud.agcom.agsupport.sc.websocket.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangmy
 * @Description: 移动端发送消息端口
 * @date 2019-11-08 10:33
 */
@Api(value = "移动端发送消息",description = "移动端发送消息相关接口")
@RestController
@RequestMapping("/app")
public class AppController {
    /** WebSocketServer 是多对象，静态注入*/
    private static WebSocketServer webSocketServer;
    @Autowired
    public void setWebSocketServer(WebSocketServer webSocketServer) {
        AppController.webSocketServer = webSocketServer;
    }

    @ApiOperation(value = "发送上报问题消息给用户",notes = "发送上报问题消息给用户接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param" ,value = "问题相关参数",dataType = "String")
    })
    @RequestMapping(value = "/sendMessage",method = RequestMethod.POST)
    public void sendMessage(String param){
        Map userIdMap = new HashMap();
        // 暂时默认给zhangmy的userId
        userIdMap.put("4a9d6fad-856a-45bf-bc3c-964c24d550a7",true);
        webSocketServer.broadCastInfo(param,userIdMap,"1");
    }
}
