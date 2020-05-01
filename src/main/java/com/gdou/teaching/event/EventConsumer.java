package com.gdou.teaching.event;

import com.alibaba.fastjson.JSONObject;
import com.gdou.teaching.dataobject.Event;
import com.gdou.teaching.mbg.model.Message;
import com.gdou.teaching.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.gdou.teaching.constant.CommonConstant.*;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.event
 * @ClassName: EventProducer
 * @Author: carrymaniac
 * @Description: 事件生产者
 * @Date: 2020/1/14 4:33 下午
 * @Version:
 */
@Component
@Slf4j
public class EventConsumer {
    private final MessageService messageService;

    public EventConsumer(MessageService messageService) {
        this.messageService = messageService;
    }

    @KafkaListener(topics = {TOPIC_CourseUpdate,TOPIC_AchievementUpdate,TOPIC_NotifyJob})
    public void handleCommentMessage(ConsumerRecord record) {
        log.info("[EventConsumer]获取到事件：{}",record);
        if(record==null||record.value()==null){
            log.error("消息的内容为空");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(),Event.class);
        if(event==null){
            log.error("消息的格式有误");
            return;
        }
        //写入通知到服务器
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getUserId());
        message.setConversationId(event.getTopic());
        //放置其余参数
        Map<String, Object> content = new HashMap<>(2);
        content.put("entityType",event.getEntityType());
        content.put("entityId",event.getEntityId());
        if(!event.getData().isEmpty()){
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));
        message.setStatus(0);
        log.info("ready to insert into DB: {}",message);
        messageService.addMessage(message);

    }
}
