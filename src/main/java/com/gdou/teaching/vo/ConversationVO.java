package com.gdou.teaching.vo;

import com.gdou.teaching.dto.UserDTO;
import lombok.Data;

@Data
public class ConversationVO {
    String conversationID;
    /**
     * 会话的第一条消息的内容
     */
    String content;
    Integer unreadCount;
    Integer targetId;
    String targetHeadUrl;
    String targetName;
}
