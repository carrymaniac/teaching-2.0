package com.gdou.teaching.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gdou.teaching.Enum.EntityTypeEnum;
import com.gdou.teaching.constant.CommonConstant;
import com.gdou.teaching.dataobject.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

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
public class EventProducer {
    private final KafkaTemplate kafkaTemplate;

    public EventProducer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void fireEvent(Event event){
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }

    public void fireCourseUpdateEvent(Integer toUserId,Integer courseId){
        Event e = new Event();
        e.setEntityType(EntityTypeEnum.COURSE.getCode());
        e.setUserId(toUserId);
        e.setTopic(CommonConstant.TOPIC_CourseUpdate);
        e.setEntityId(courseId);
        fireEvent(e);
    }
    public void fireAchievementUpdateEvent(Integer toUserId,Integer courseId){
        Event e = new Event();
        e.setEntityType(EntityTypeEnum.Achievement.getCode());
        e.setUserId(toUserId);
        e.setTopic(CommonConstant.TOPIC_AchievementUpdate);
        e.setEntityId(courseId);
        fireEvent(e);
    }
}
