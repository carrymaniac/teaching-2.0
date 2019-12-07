package com.gdou.teaching.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Ha
 * @DATE 2019/7/7 17:39
 **/
@Component
public class RedisUtil {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate redisTemplate;


    public void sadd(String key,String value){
        stringRedisTemplate.opsForSet().add(key,value);
    }

    public long srem(String key,String value){
        return stringRedisTemplate.opsForSet().remove(key,value);
    }

    public long scard(String key){
        return stringRedisTemplate.opsForSet().size(key);
    }

    public boolean sismeber(String key,String value){
        return stringRedisTemplate.opsForSet().isMember(key,value);
    }

    public long lpush(String key ,String value){
        return stringRedisTemplate.opsForList().leftPush(key,value);
    }

    public List<String> brpop(int timeout,String key){
        RedisConnectionFactory connectionFactory = redisTemplate.getConnectionFactory();
        RedisConnection connection = connectionFactory.getConnection();
        List<byte[]> bytes = connection.bRPop(timeout, key.getBytes());
        List<String> collect = bytes.stream().map(e -> {
            String str = new String(e);
            return str;
        }).collect(Collectors.toList());
        return collect;
    }


}
