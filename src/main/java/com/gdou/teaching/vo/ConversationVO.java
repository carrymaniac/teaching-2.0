package com.gdou.teaching.vo;

import com.gdou.teaching.dto.UserDTO;
import lombok.Data;

import java.util.List;

@Data
public class ConversationVO {
    String conversationID;
    /**
     * 会话的第一条消息的内容
     */
    String content;
    Integer unreadCount;
    List<Integer> unreadIdList;
    Integer targetId;
    String targetHeadUrl;
    String targetName;
}
