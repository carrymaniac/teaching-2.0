package com.gdou.teaching.constant;

/**
 * @Author Ha
 * @DATE 2019/7/7 17:46
 **/
public interface RedisConstant {
    String TOKEN_PREFIX = "token_%s";
    Integer EXPIRE = 7200; //2小时
    String SPLIT = ":";
    String BIZ_LIKE = "LIKE";
    String BIZ_DISLIKE = "DISLIKE";
    //事件队列的前缀头
    String BIZ_EVENTQUEUE = "EVENT_QUEUE";
    //实验的提交人数前缀头 需使用format方法
    String BIZ_COMMIT_NUM = "EXPERIMENT_%d_NUM";


    static String getLikeKey(int entityType, int entityId){
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    };
    static String getDisLikeKey(int entityType, int entityId) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
    static String getEventQueueKey() {
        return BIZ_EVENTQUEUE;
    }

    static String getBizCommitNumKey(int experimentId){
        return String.format(BIZ_COMMIT_NUM,experimentId);
    }
}
