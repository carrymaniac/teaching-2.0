package com.gdou.teaching.service;

import com.gdou.teaching.mbg.mapper.MessageMapper;
import com.gdou.teaching.mbg.model.Message;
import org.springframework.beans.factory.annotation.Autowired;

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
     * 获取系统通知
     */
    List<HashMap> getMessageFromSystem(Integer userId);
}
