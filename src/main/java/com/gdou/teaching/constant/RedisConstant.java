package com.gdou.teaching.constant;

/**
 * @Author Ha
 * @DATE 2019/7/7 17:46
 **/
public interface RedisConstant {
    String TOKEN_PREFIX = "token_%s";
    /**
     * Token有效时间
     * 2小时
     */
    Integer EXPIRE = 7200;
    String SPLIT = ":";
    String BIZ_LIKE = "LIKE";
    String BIZ_DISLIKE = "DISLIKE";
    //事件队列的前缀头
    String BIZ_EVENTQUEUE = "EVENT_QUEUE";
    //实验的提交人数前缀头 需使用format方法
    String BIZ_COMMIT_NUM = "EXPERIMENT_%d_NUM";

    String BIZ_CHECK_ANSWER = "EXPERIMENT_CHECK_ID_%d";

}
