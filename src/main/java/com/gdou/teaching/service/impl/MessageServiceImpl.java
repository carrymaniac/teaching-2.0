package com.gdou.teaching.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gdou.teaching.Enum.MessageStatusEnum;
import com.gdou.teaching.dao.MessageDao;
import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.mbg.mapper.AchievementMapper;
import com.gdou.teaching.mbg.mapper.CourseMasterMapper;
import com.gdou.teaching.mbg.mapper.ExperimentMasterMapper;
import com.gdou.teaching.mbg.mapper.MessageMapper;
import com.gdou.teaching.mbg.model.*;
import com.gdou.teaching.service.MessageService;
import com.gdou.teaching.service.UserService;
import com.gdou.teaching.util.CommonUtil;
import com.gdou.teaching.vo.ConversationVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gdou.teaching.constant.CommonConstant.*;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.service.impl
 * @ClassName: MessageServiceImpl
 * @Author: carrymaniac
 * @Description: MessageService实现类
 * @Date: 2020/1/14 8:40 下午
 * @Version:
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private CourseMasterMapper courseMasterMapper;
    @Autowired
    private AchievementMapper achievementMapper;
    @Autowired
    private ExperimentMasterMapper experimentMasterMapper;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    UserService userService;
    @Override
    public int addMessage(Message message) {
        return messageMapper.insert(message);
    }

    @Override
    public Message addMessage(Integer fromId, Integer toId, String content) {
        //生成会话ID 用于会话用
        String s = CommonUtil.genConversationId(fromId, toId);
        Message message = new Message();
        message.setToId(toId);
        message.setFromId(fromId);
        message.setContent(content);
        message.setStatus(MessageStatusEnum.UNREAD.getCode());
        message.setConversationId(s);
        messageMapper.insert(message);
        return message;
    }

    @Override
    public List<HashMap> getMessageFromSystem(Integer userId) {
        //TODO 大改
        /**
         * 逻辑：
         * 1. 拿到用户ID 去数据库里查询（未读的）系统消息
         * 2. 处理content 转化为所需的DTO格式返回给Controller使用
         */
        MessageExample example = new MessageExample();
        String s = CommonUtil.genConversationId(SYSTEM_USER_ID, userId);
        example.createCriteria().andFromIdEqualTo(SYSTEM_USER_ID).andToIdEqualTo(userId).andStatusEqualTo(MessageStatusEnum.UNREAD.getCode());
        List<Message> messages = messageMapper.selectByExample(example);
        //开始处理message转为给前端显示的消息
        List<HashMap> messageList = new ArrayList<>();
        for (Message message:messages){
            String conversationId = message.getConversationId();
            HashMap<String, String> map = JSONObject.parseObject(message.getContent(), new TypeReference<HashMap<String, String>>(){});

            //todo 把result的map抽取出来变成一个单独的DTO类
            if(conversationId.equals(TOPIC_CourseUpdate)){
                //这是一个课程更新的提醒
                //查一下课程名字
                String entityId = map.get("entityId");
                CourseMaster courseMaster = courseMasterMapper.selectByPrimaryKey(Integer.valueOf(entityId));
                //todo 这里看看是否catch错误 如果查询不到
                String msg = "你选修的课程"+courseMaster.getCourseName()+"已更新了新的实验啦，快来看一看吧。";
                HashMap<String,Object> result = new HashMap<>(5);
                result.put("msg",msg);
                result.put("tittle","课程内容更新通知");
                result.put("messageId",message.getMessageId());
                result.put("time",message.getCreateTime());
                result.put("routerUrl",String.format("/courseList/experiment/%s",entityId));
                messageList.add(result);
            }else if (conversationId.equals(TOPIC_AchievementUpdate)){
                //成绩更新提醒
                String entityId = map.get("entityId");
                Achievement achievement = achievementMapper.selectByPrimaryKey(Integer.valueOf(entityId));
                String msg = "你选修的课程"+achievement.getCourseName()+"已更新了了成绩，您目前的成绩为："+achievement.getCourseAchievement()+" ,快来看一看吧。";
                HashMap<String,Object> result = new HashMap<>(5);
                result.put("msg",msg);
                result.put("tittle","课程成绩更新通知");
                result.put("messageId",message.getMessageId());
                result.put("time",message.getCreateTime());
                result.put("routerUrl",String.format("/courseList/score/%d",achievement.getCourseId()));
                messageList.add(result);
            }else if(conversationId.equals(TOPIC_NotifyJob)){
                //提醒事件
                //拿到实验ID
                String entityId = map.get("entityId");
                ExperimentMaster experimentMaster = experimentMasterMapper.selectByPrimaryKey(Integer.valueOf(entityId));
                String msg = "您的老师提醒您,您的实验"+experimentMaster.getExperimentName()+"尚未完成，快来看一看吧。";
                HashMap<String,Object> result = new HashMap<>(5);
                result.put("msg",msg);
                result.put("tittle","实验进度提醒通知");
                result.put("messageId",message.getMessageId());
                result.put("time",message.getCreateTime());
                result.put("routerUrl",String.format("/courseList/%d/content/%s",experimentMaster.getCourseId(),entityId));
                messageList.add(result);
            }
            //这里可以继续写其他类型的事件
        }
        return messageList;
    }

    //获取用户会话列表
    @Override
    public List<ConversationVO> getConversationList(Integer userId, int offset, int limit) {
        List<Message> messages = messageDao.selectConversations(userId, offset, limit);
        List<ConversationVO> conversations = new ArrayList<>();
        if(messages!=null){
            for(Message message:messages){
                ConversationVO conversation = new ConversationVO();
                //会话ID
                conversation.setConversationID(message.getConversationId());
                //会话的第一条消息的内容
                conversation.setContent(message.getContent());
                //放置未读数
                conversation.setUnreadCount(this.selectLetterUnreadCount(userId, message.getConversationId()));
                //查询目标的头像以及个人信息
                int targetId = userId.equals(message.getFromId()) ? message.getToId() : message.getFromId();
                UserDTO userById = userService.selectOne(targetId);
                conversation.setTargetId(targetId);
                conversation.setTargetName(userById.getNickname());
                conversation.setTargetHeadUrl(userById.getHeadUrl());
                conversations.add(conversation);
            }
        }
        return conversations;
    }

    @Override
    public PageInfo<Message> getConversation(String conversationId, int page, int size) {
        MessageExample example = new MessageExample();
        example.createCriteria().andConversationIdEqualTo(conversationId);
        example.setOrderByClause("message_id DESC");
        PageHelper.startPage(page,size);
        List<Message> messages = messageMapper.selectByExample(example);
        PageInfo<Message> pageInfo = new PageInfo(messages);
        return pageInfo;
    }

    @Override
    @Async
    public int setMessageRead(List<Integer> messageId) {
        MessageExample example = new MessageExample();
        example.createCriteria().andMessageIdIn(messageId);
        Message message = new Message();
        message.setStatus(MessageStatusEnum.READ.getCode());
        return messageMapper.updateByExampleSelective(message,example);
    }

    @Override
    public int selectLetterUnreadCount(int userId, String conversationId) {
        MessageExample example = new MessageExample();
        if(conversationId!=null){
            example.createCriteria().
                    andToIdEqualTo(userId).
                    andFromIdNotEqualTo(0).
                    andStatusEqualTo(MessageStatusEnum.UNREAD.getCode()).
                    andConversationIdEqualTo(conversationId);
        }else {
            example.createCriteria().
                    andToIdEqualTo(userId).
                    andStatusEqualTo(MessageStatusEnum.UNREAD.getCode()).
                    andFromIdNotEqualTo(0);
        }
        return messageMapper.countByExample(example);
    }

    @Override
    public int selectSystemMessageUnreadCount(int userId) {
        //消息来自系统。且未读
        MessageExample example = new MessageExample();
            example.createCriteria().
            andToIdEqualTo(userId).
            andFromIdEqualTo(0).
            andStatusEqualTo(MessageStatusEnum.UNREAD.getCode());
        return messageMapper.countByExample(example);
    }
}
