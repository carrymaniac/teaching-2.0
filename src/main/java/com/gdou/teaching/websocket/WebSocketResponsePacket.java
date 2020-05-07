package com.gdou.teaching.websocket;

import lombok.Data;

import java.util.Date;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.websocket
 * @ClassName: WebSocketMessage
 * @Author: carrymaniac
 * @Description: 服务器发给客户端的包
 * @Date: 2020/5/6 9:21 下午
 * @Version:
 */
@Data
public class WebSocketResponsePacket {
    /**
     * 发送人ID
     */
    Integer fromId;
    /**
     * 接收人ID
     */
    Integer toId;
    /**
     * ResponsePacket类型
     */
    Integer type;
    /**
     * 内容
     */
    String content;
    /**
     * 时间戳
     */
    String timestamp;
    /**
     * 扩展字段
     */
    String Describe;
}
