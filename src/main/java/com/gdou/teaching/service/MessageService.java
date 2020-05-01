package com.gdou.teaching.service;

import com.gdou.teaching.mbg.mapper.MessageMapper;
import com.gdou.teaching.mbg.model.Message;
import com.gdou.teaching.vo.ConversationVO;
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
     * 增加一条新的消息
     * @param fromId
     * @param toId
     * @param content
     * @return
     */
    Message addMessage(Integer fromId,Integer toId,String content);

    /**
     * 获取系统通知
     * @param userId
     * @return
     */
    List<HashMap> getMessageFromSystem(Integer userId);

    /**
     * 分页获取消息对话
     * @param userId
     * @param offset
     * @param limit
     */
    List<ConversationVO> getConversationList(Integer userId, int offset, int limit);

    /**
     * 通过会话ID获取消息记录
     * @param conversationId
     * @param page
     * @param size
     * @return
     */
    PageInfo<Message> getConversation(String conversationId, int page, int size);

    /**
     * 将消息设置为已读
     * @param messageId
     * @return
     */
    int setMessageRead(List<Integer> messageId);


    /**
     * 查询未读私信的数量
     * @param userId 用户ID
     * @param conversationId 会话ID，若为空则为查询用户所有的未读私信数量
     * @return
     */
    int selectLetterUnreadCount(int userId, String conversationId);

    /**
     * 查询未读的通知的数量
     * @param userId
     * @return
     */
    int selectSystemMessageUnreadCount(int userId);



}
