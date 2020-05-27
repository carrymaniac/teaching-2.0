package com.gdou.teaching.server;

import com.gdou.teaching.service.impl.RedisServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.server
 * @ClassName: RedisInit
 * @Author: carrymaniac
 * @Description: Redis初始化评语
 * @Date: 2020/5/27 4:40 下午
 * @Version:
 */
@Component
@Slf4j
public class RedisInit implements ApplicationRunner {
    @Autowired
    RedisServiceImpl redisService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //初始化所有的评语
        log.info("[RedisInit] init Redis data");
        redisService.initComment();
    }
}
