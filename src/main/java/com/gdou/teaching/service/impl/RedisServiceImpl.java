package com.gdou.teaching.service.impl;

import com.gdou.teaching.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.service
 * @ClassName: RedisServiceImpl
 * @Author: carrymaniac
 * @Description: Redis的相关服务
 * @Date: 2019/12/21 3:22 下午
 * @Version:
 */
@Service
public class RedisServiceImpl {

    private final StringRedisTemplate redisTemplate;

    public RedisServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取一条评语
     * @return
     */
    public String getOneComment(Double score){
        //1.判断分数属于哪个水平段
        if(score>=90){
            //A 优
            return redisTemplate.opsForSet().randomMember(RedisConstant.BIZ_SCORE_COMMENT_FOR_A);
        }else if(score<90&&score>=75){
            //B 良
            return redisTemplate.opsForSet().randomMember(RedisConstant.BIZ_SCORE_COMMENT_FOR_B);
        }else if(score<75&&score>=60){
            //C 及格
            return redisTemplate.opsForSet().randomMember(RedisConstant.BIZ_SCORE_COMMENT_FOR_C);
        }else {
            //D 不及格
            return redisTemplate.opsForSet().randomMember(RedisConstant.BIZ_SCORE_COMMENT_FOR_D);
        }
    }

    /**
     * 初始化评语库
     */
    void initComment(){
        redisTemplate.opsForSet().add(RedisConstant.BIZ_SCORE_COMMENT_FOR_A,
                "该生按时完成实验内容，实验步骤准确完整，实验总结全面，实验结果正确",
                "该生在实验过程中按时完成实验，实验结果正确，实验报告版面整洁",
                "该生按时完成实验内容，实验内容和实验过程记录完整；实验报告结果正确。"
                );
        redisTemplate.opsForSet().add(RedisConstant.BIZ_SCORE_COMMENT_FOR_B,
                "该生按要求完成实验内容，实验报告的撰写认真、报告格式符合要求，实验步骤不够完整",
                "该生在规定时间内完成实验报告，实验内容和实验过程记录完整；但缺少部分实验数据。",
                "该生实验报告版面整洁，实验步骤记录不够详细，实验结果正确。"
        );
        redisTemplate.opsForSet().add(RedisConstant.BIZ_SCORE_COMMENT_FOR_C,
                "该生按要求完成实验内容，达到实验要求，实验内容和实验过程记录基本完整；实验报告的撰写比较认真。",
                "该生可以独立完成实验内容，达到实验要求。按时完成实验内容，实验步骤基本准确，实验版面比较整洁。",
                "该生按要求完成实验内容，达到要求。实验内容和实验过程记录比较完整；实验结果正确。实验总结不够深入。"
        );
        redisTemplate.opsForSet().add(RedisConstant.BIZ_SCORE_COMMENT_FOR_D,
                "该生基本能按要求完成实验内容，实验报告版面不美观，实验步骤不完成。",
                "该生完成实验内容，实验步骤不够详细，实验总结不正确",
                "该生按要求完成实验内容，达到要求。实验内容和实验过程记录比较完整；实验结果正确。实验总结不够深入。",
                "该生按要求完成实验内容，达到要求。但缺少实验关键步骤，无实验图表、结果数据等",
                "该生基本能按要求完成实验内容，但缺少实验关键步骤，无自己的体会或体会不深刻"
        );
    }

}
