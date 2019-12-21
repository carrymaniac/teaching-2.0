package com.gdou.teaching.service.impl;

import com.gdou.teaching.constant.RedisConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.relational.core.sql.In;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.service.impl
 * @ClassName: RedisTest
 * @Author: carrymaniac
 * @Description: Redis相关测试
 * @Date: 2019/12/21 3:12 下午
 * @Version:
 */
@SpringBootTest
public class RedisTest {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    RedisServiceImpl redisService;
    @Test
    public void RedisTest(){
        String format = String.format(RedisConstant.BIZ_CHECK_ANSWER, 1);
        System.out.println(format);
        Boolean member = redisTemplate.opsForSet().isMember(format, Integer.toString(1));
        Boolean member1 = redisTemplate.opsForSet().isMember(format, Integer.toString(2));
        Boolean member2 = redisTemplate.opsForSet().isMember(format, Integer.toString(3));
        System.out.println(member);
        System.out.println(member1);
        System.out.println(member2);
    }

    @Test
    public void RedisTestForCommnet(){
        System.out.println(redisService.getOneComment((double)59));
        System.out.println(redisService.getOneComment((double) 59));
        System.out.println(redisService.getOneComment((double) 59));
        System.out.println(redisService.getOneComment((double) 59));
        System.out.println(redisService.getOneComment((double) 59));

    }
}
