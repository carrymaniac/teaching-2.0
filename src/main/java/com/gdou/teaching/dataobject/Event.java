package com.gdou.teaching.dataobject;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.dataobject
 * @ClassName: Event
 * @Author: carrymaniac
 * @Description: 事件
 * @Date: 2020/1/14 4:28 下午
 * @Version:
 */
public class Event {
    private String topic;
    private Integer userId;
    private int entityType;
    private int entityId;
    private int entityUserId;
    /**
     * 用于扩展
     */
    private Map<String,Object> data = new HashMap<>();

    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public Event setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key,Object value) {
        this.data.put(key,value);
        return this;
    }
}
