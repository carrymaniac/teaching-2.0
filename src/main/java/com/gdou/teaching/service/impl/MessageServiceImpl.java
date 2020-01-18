package com.gdou.teaching.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gdou.teaching.Enum.MessageStatusEnum;
import com.gdou.teaching.dao.MessageDao;
import com.gdou.teaching.mbg.mapper.AchievementMapper;
import com.gdou.teaching.mbg.mapper.CourseMasterMapper;
import com.gdou.teaching.mbg.mapper.ExperimentMasterMapper;
import com.gdou.teaching.mbg.mapper.MessageMapper;
import com.gdou.teaching.mbg.model.*;
import com.gdou.teaching.service.MessageService;
import com.gdou.teaching.util.CommonUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    @Override
    public int addMessage(Message message) {
        return messageMapper.insert(message);
    }

    @Override
    public int addMessage(Integer fromId, Integer toId, String content) {
        String s = CommonUtil.genConversationId(fromId, toId);
        Message message = new Message();
        message.setToId(toId);
        message.setFromId(fromId);
        message.setContent(content);
        message.setStatus(MessageStatusEnum.UNREAD.getCode());
        message.setConversationId(s);
        return messageMapper.insert(message);
    }

    @Override
    public List<HashMap> getMessageFromSystem(Integer userId) {
        MessageExample example = new MessageExample();
        example.createCriteria().andFromIdEqualTo(SYSTEM_USER_ID).andToIdEqualTo(userId).andStatusEqualTo(MessageStatusEnum.UNREAD.getCode());
        List<Message> messages = messageMapper.selectByExample(example);
        //开始处理message转为给前端显示的消息
        List<HashMap> messageList = new ArrayList<>();
        for (Message message:messages){
            String conversationId = message.getConversationId();
            HashMap<String, String> map = JSONObject.parseObject(message.getContent(), new TypeReference<HashMap<String, String>>() {
            });
            if(conversationId.equals(TOPIC_CourseUpdate)){
                //这是一个课程更新的提醒
                //查一下课程名字
                String entityId = map.get("entityId");
                CourseMaster courseMaster = courseMasterMapper.selectByPrimaryKey(Integer.valueOf(entityId));
                String msg = "你选修的课程"+courseMaster.getCourseName()+"已更新了新的实验啦，快来看一看吧。";
                HashMap<String,String> result = new HashMap<>(3);
                result.put("entityType",TOPIC_CourseUpdate);
                result.put("msg",msg);
                result.put("entityId",entityId);
                messageList.add(result);
            }else if (conversationId.equals(TOPIC_AchievenmtUpdate)){
                //成绩更新提醒
                String entityId = map.get("entityId");
                Achievement achievement = achievementMapper.selectByPrimaryKey(Integer.valueOf(entityId));
                String msg = "你选修的课程"+achievement.getCourseName()+"已更新了了成绩，您目前的成绩为："+achievement.getCourseAchievement()+" ,快来看一看吧。";
                HashMap<String,String> result = new HashMap<>(3);
                result.put("entityType",TOPIC_AchievenmtUpdate);
                result.put("msg",msg);
                result.put("entityId",achievement.getCourseId().toString());
                messageList.add(result);
            }else if(conversationId.equals(TOPIC_NotifyJob)){
                //提醒事件
                //拿到实验ID
                String entityId = map.get("entityId");
                ExperimentMaster experimentMaster = experimentMasterMapper.selectByPrimaryKey(Integer.valueOf(entityId));
                String msg = "您的老师提醒您,您的实验"+experimentMaster.getExperimentName()+"尚未完成，快来看一看吧。";
                HashMap<String,String> result = new HashMap<>(3);
                result.put("entityType",TOPIC_NotifyJob);
                result.put("msg",msg);
                result.put("entityId",entityId);
                messageList.add(result);
            }
        }
        return messageList;
    }

    @Override
    public List<Message> getConversationList(Integer userId, int offset, int limit) {
        List<Message> messages = messageDao.selectConversations(userId, offset, limit);
        return messages;
    }

    @Override
    public PageInfo getConversation(String conversationId, int page, int size) {
        MessageExample example = new MessageExample();
        example.createCriteria().andConversationIdEqualTo(conversationId);
        example.setOrderByClause("message_id DESC");
        PageHelper.startPage(page,size);
        List<Message> messages = messageMapper.selectByExample(example);
        PageInfo pageInfo = new PageInfo(messages);
        return pageInfo;
    }
}
