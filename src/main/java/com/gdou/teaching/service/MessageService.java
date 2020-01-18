package com.gdou.teaching.service;

import com.gdou.teaching.mbg.mapper.MessageMapper;
import com.gdou.teaching.mbg.model.Message;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;

import java.util.HashMap;
import java.util.List;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.service
 * @ClassName: MessageService
 * @Author: carrymaniac
 * @Description: 消息Service
 * @Date: 2020/1/14 8:38 下午
 * @Version:
 */
public interface MessageService {
    /**
     * 添加消息
     * @param message
     * @return
     */
    int addMessage(Message message);

    /**
     *
     * @param fromId
     * @param toId
     * @param content
     * @return
     */
    int addMessage(Integer fromId,Integer toId,String content);

    /**
     * 获取系统通知
     */
    List<HashMap> getMessageFromSystem(Integer userId);

    /**
     * 分页获取消息对话
     * @param userId
     * @param offset
     * @param limit
     */
    List<Message> getConversationList(Integer userId,int offset, int limit);

    /**
     * 通过会话ID获取消息记录
     * @param conversationId
     * @param page
     * @param size
     * @return
     */
    PageInfo getConversation(String conversationId, int page, int size);
}
