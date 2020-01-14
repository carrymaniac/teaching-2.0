package com.gdou.teaching.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.gdou.teaching.mbg.mapper.CourseMasterMapper;
import com.gdou.teaching.mbg.mapper.MessageMapper;
import com.gdou.teaching.mbg.model.CourseMaster;
import com.gdou.teaching.mbg.model.Message;
import com.gdou.teaching.mbg.model.MessageExample;
import com.gdou.teaching.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.gdou.teaching.constant.CommonConstant.SYSTEM_USER_ID;
import static com.gdou.teaching.constant.CommonConstant.TOPIC_CourseUpdate;

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
    @Override
    public int addMessage(Message message) {
        return messageMapper.insert(message);
    }

    @Override
    public List<HashMap> getMessageFromSystem(Integer userId) {
        MessageExample example = new MessageExample();
        //之后需要吧0抽取出来作为常量
        example.createCriteria().andFromIdEqualTo(SYSTEM_USER_ID).andToIdEqualTo(userId).andStatusEqualTo(0);
        List<Message> messages = messageMapper.selectByExample(example);
        //开始处理message转为给前端显示的消息
        List<HashMap> messageList = new ArrayList<>();
        for (Message message:messages){
            String conversationId = message.getConversationId();
            if(conversationId.equals(TOPIC_CourseUpdate)){
                //这是一个课程更新的提醒
                //查一下课程名字
                HashMap<String, String> map = JSONObject.parseObject(message.getContent(), new TypeReference<HashMap<String, String>>() {
                });
                String entityId = map.get("entityId");
                CourseMaster courseMaster = courseMasterMapper.selectByPrimaryKey(Integer.valueOf(entityId));
                String msg = "你选修的课程"+courseMaster.getCourseName()+"已更新了新的实验啦，快来看一看吧。";
                HashMap<String,String> result = new HashMap<>();
                result.put("msg",msg);
                result.put("entityId",entityId);
                messageList.add(result);
            }
        }
        return messageList;
    }
}
