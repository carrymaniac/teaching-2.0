package com.gdou.teaching.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.gdou.teaching.constant.WebSocketConstant;
import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.mbg.model.Message;
import com.gdou.teaching.service.MessageService;
import com.gdou.teaching.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.websocket
 * @ClassName: WebSocketServer
 * @Author: carrymaniac
 * @Description:
 * @Date: 2020/5/6 11:48 上午
 * @Version:
 */
@Component
@ServerEndpoint("/imserver/{userId}")
@Slf4j
public class WebSocketServer {
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    private static ConcurrentHashMap<Integer, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    private Session session;
    private Integer userId;
    private static MessageService messageService;
    private static UserService userService;
    @Autowired
    public void setMessageService(MessageService messageService) {
        WebSocketServer.messageService = messageService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        WebSocketServer.userService = userService;
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId) {
        this.session = session;
        this.userId = userId;

        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            webSocketMap.put(userId, this);
            log.info("[WebSocketServer] open a webSocket, userId = {}", userId);
        } else {
            webSocketMap.put(userId, this);
            log.info("[WebSocketServer] open a webSocket, userId = {}", userId);
            onlineCount.incrementAndGet();
        }
    }


    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            onlineCount.decrementAndGet();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // message 是客户端发来的消息
        log.info("[WebSocketServer]获取到客户端发来的消息，用户ID：{},内容:{}", this.userId, message);
        /**
         * 流程：
         * 1。解析检查内容 (使用json来传递)
         *  获取toID 以及通话内容
         * 2。检查toID对象是否在线。如果在线发送消息给对象
         * 3。将消息记录写入数据库。完成操作
         */
        //todo 还需要校验用户ID的合法程度？
        if (!StringUtils.isEmpty(message)) {
            try {
                //解析为JSON对象
                WebSocketRequestPacket request = JSON.parseObject(message, WebSocketRequestPacket.class);
                String content = request.getContent();
                if(request.type.equals(WebSocketConstant.REQUEST_READ)){
                    List<Integer> messageIds = JSON.parseArray(content, Integer.class);
                    messageService.setMessageRead(messageIds);
                }else if(request.type.equals(WebSocketConstant.REQUEST_MESSAGE)){
                    if (request.getToId()!=null) {
                        Integer toID = request.getToId();
                        Message m = messageService.addMessage(userId, toID, content);
                        if (webSocketMap.containsKey(toID)) {
                            //用户在线，发给他
                            WebSocketServer webSocketServer = webSocketMap.get(toID);
                            //构建包
                            WebSocketResponsePacket response = new WebSocketResponsePacket();
                            //额外信息查询
                            UserDTO userDTO = userService.selectOne(this.userId);
                            HashMap map = new HashMap(2);
                            map.put("userName",userDTO.getNickname());
                            map.put("headURL",userDTO.getHeadUrl());
                            response.setContent(content);
                            response.setFromId(this.userId);
                            response.setTime(m.getCreateTime());
                            response.setToId(toID);
                            response.setType(WebSocketConstant.RESPONSE_MESSAGE);
                            response.setDescribe(JSON.toJSONString(map));
                            webSocketServer.sendMessage(JSON.toJSONString(response));
                        }
                    }
                }
            } catch (JSONException e) {
                //解析错误，用户发来的消息有误
                log.info("[WebSocketServer] 接受消息失败，告知用户发送失败,{}",e);
                WebSocketResponsePacket response = new WebSocketResponsePacket();
                //构建包
                response.setContent(null);
                response.setType(WebSocketConstant.RESPONSE_FAIL);
                response.setTime(new Date());
                try {
                    this.sendMessage(JSON.toJSONString(response));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } catch (IOException e) {
                log.info("[WebSocketServer] 发送消息失败，放弃本次发送");
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }


    /**
     * 使用json字符串来传递
     */
    public void sendMessage(String message) throws IOException {

        this.session.getBasicRemote().sendText(message);
    }

}
