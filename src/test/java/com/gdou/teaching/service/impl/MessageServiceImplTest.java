package com.gdou.teaching.service.impl;

import com.gdou.teaching.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.service.impl
 * @ClassName: MessageServiceImplTest
 * @Author: carrymaniac
 * @Description:
 * @Date: 2020/1/14 9:25 下午
 * @Version:
 */
@SpringBootTest
@Slf4j
class MessageServiceImplTest {
    @Autowired
    MessageServiceImpl messageService;
    @Test
    void addMessage() {
    }

    @Test
    void getMessageFromSystem() {
        List<HashMap> messageFromSystem = messageService.getMessageFromSystem(2);
        log.info("result :{}",messageFromSystem);
    }
}