package com.gdou.teaching.websocket;

import lombok.Data;

import java.util.Date;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.websocket
 * @ClassName: WebSocketMessage
 * @Author: carrymaniac
 * @Description: 前端发送来的消息包
 * @Date: 2020/5/6 9:21 下午
 * @Version:
 */
@Data
public class WebSocketResponsePacket {
    Integer fromId;
    Integer toId;
    Integer type;
    String content;
    Date time;
    /**
     * 扩展字段
     */
    String Describe;
}
