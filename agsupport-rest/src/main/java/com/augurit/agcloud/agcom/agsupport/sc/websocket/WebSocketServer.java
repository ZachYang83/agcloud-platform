package com.augurit.agcloud.agcom.agsupport.sc.websocket;

import com.alibaba.druid.support.json.JSONUtils;
import com.augurit.agcloud.agcom.agsupport.domain.AgUserMarker;
import com.augurit.agcloud.agcom.agsupport.sc.marker.services.IAgMarker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhangmy
 * @Description: websocket 服务端
 * @date 2019-10-30 13:38
 */
@ServerEndpoint("/websocket/{userId}/{uuid}")
@Component
public class WebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    /**
     * 记录当前在线连接数
     */
    private static final AtomicInteger OnlineCount = new AtomicInteger(0);
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
     */
    private static ConcurrentHashMap<String,WebSocketServer> webSocketServerSet = new ConcurrentHashMap<>();
    /** 与某个客户端的连接会话，需要通过它来给客户端发送数据 */
    private Session webSocketsession;
    /** 用户连接的userId */
    private String userId;

    private static IAgMarker agMarker;
    @Autowired
    public void setAgMarker(IAgMarker agMarker) {
        WebSocketServer.agMarker = agMarker;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId,@PathParam("uuid") String uuid){
        this.userId = userId;
        this.webSocketsession = session;
        webSocketServerSet.put(userId+":"+uuid,this);
        int cnt = OnlineCount.incrementAndGet(); // 在线数加1
        logger.info("有新连接加入，当前连接数为：{}", cnt);
        try {
            // 第一次连接进来，别人分享的标注，未查看的标注要发送消息
            if (StringUtils.isNotBlank(userId)){
                List<AgUserMarker> agUserMarkerList = agMarker.getUserMarkerByUserAndView(userId, "0");
                Map map = new LinkedHashMap();
                for (AgUserMarker userMarker : agUserMarkerList){
                    if (userMarker.getShareUser() != null){
                        map.put(userMarker.getShareUser(),true);
                    }
                }
                Map result = new HashMap();
                result.put("total",agUserMarkerList.size());
                result.put("result",map.keySet());
                if (agUserMarkerList.size()>0){
                    sendMessage(JSONUtils.toJSONString(result));
                }
            }
        }catch (Exception e){
            logger.error("连接异常!",e);
        }

    }

    @OnClose
    public void onClose(){
        if (!userId.equals("")){
            webSocketServerSet.remove(userId);
            int cnt = OnlineCount.decrementAndGet();
            logger.info("有连接关闭，当前连接数为：{}", cnt);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        this.webSocketsession = session;
        message = "来自客户端的消息:" + message;
        logger.info(message);
        try {
            sendMessage( message);
        } catch (IOException e) {
            logger.error("onMessage方法异常"+e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 发生错误时调用
     * @OnError
     **/
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("onMessage方法异常"+error.toString());
        error.printStackTrace();
    }

    /**
     * 注意session.getBasicRemote()与session.getAsyncRemote()的区别
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        //this.session.getBasicRemote().sendText(message);
        this.webSocketsession.getAsyncRemote().sendText(message);
    }

    /**
     * 发送消息,每次浏览器刷新，session会发生变化。
     * @param message
     */
    public void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
            //session.getBasicRemote().sendText(String.format("%s",message));
        } catch (IOException e) {
            logger.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 群发消息
     * @param message
     * @param userMap 需要发送到的用户Map
     * @throws IOException
     */
    public void broadCastInfo(String message, Map userMap) {
        for (String key : webSocketServerSet.keySet()) {
            String[] split = key.split(":");
            Object o = userMap.get(split[0]);
            if (o != null){
                Session session = webSocketServerSet.get(key).webSocketsession;
                Map map = new HashMap();
                List<String> list = new ArrayList<>();
                list.add(message);
                map.put("result",list);
                map.put("total",1);
                // 0 表示标注消息
                map.put("messageType","0");
                if(session != null && session.isOpen()){
                    sendMessage(session, JSONUtils.toJSONString(map));
                }
            }

        }
    }

    /**
     * 群发消息,有消息类型区分
     * @param message
     * @param messageType 0:标注消息 1：上报问题消息
     * @param userMap 需要发送到的用户Map
     * @throws IOException
     */
    public void broadCastInfo(String message, Map userMap,String messageType) {
        for (String key : webSocketServerSet.keySet()) {
            String[] split = key.split(":");
            Object o = userMap.get(split[0]);
            if (o != null){
                Session session = webSocketServerSet.get(key).webSocketsession;
                Map map = new HashMap();
                List<String> list = new ArrayList<>();
                list.add(message);
                map.put("result",list);
                map.put("total",1);
                map.put("messageType",messageType);
                if(session != null && session.isOpen()){
                    sendMessage(session, JSONUtils.toJSONString(map));
                }
            }

        }
    }

    /**
     * 发送到指定用户
     * @param message
     * @throws IOException
     */
    public void sendToUser(String userId, String message) {
        WebSocketServer webSocketServer = webSocketServerSet.get(userId);
        if ( webSocketServer != null && webSocketServer.webSocketsession.isOpen()){
            Map map = new HashMap();
            List<String> list = new ArrayList<>();
            list.add(message);
            map.put("result",list);
            map.put("total",1);
            sendMessage(webSocketServer.webSocketsession, message);
        }
        else{
            logger.warn("当前用户不在线：{}",userId);
        }
    }
}
