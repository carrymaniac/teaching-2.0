package com.gdou.teaching.service.impl;

import com.gdou.teaching.dataobject.Event;
import com.gdou.teaching.event.EventProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.security.RunAs;

import static com.gdou.teaching.constant.CommonConstant.TOPIC_CourseUpdate;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.service.impl
 * @ClassName: kafkaTest
 * @Author: carrymaniac
 * @Description:
 * @Date: 2020/1/14 6:05 下午
 * @Version:
 */
@SpringBootTest
@Slf4j
public class kafkaTest {
    @Autowired
    KafkaProduce kafkaProduce;
    @Autowired
    EventProducer eventProducer;

    @Test
    public void test(){
        kafkaProduce.sendMessage("test","你好");
        kafkaProduce.sendMessage("test","世界");
        try{
            Thread.sleep(1000*20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void kafkaTest(){
        Event event = new Event();
        event.setTopic(TOPIC_CourseUpdate);
        event.setUserId(2);
        event.setEntityId(1);
        eventProducer.fireEvent(event);
        try{
            Thread.sleep(1000*20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

@Component
@Slf4j
class KafkaProduce{
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic,String content){
        kafkaTemplate.send(topic,content);
    }
}

@Component
@Slf4j
class KafkaConsumer{
    @KafkaListener(topics = {"test"})
    public void handler(ConsumerRecord record){
        log.info("record topic:{}",record.topic());
        log.info("record content:{}",record.value());
    }
}