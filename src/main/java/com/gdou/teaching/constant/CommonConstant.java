package com.gdou.teaching.constant;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.constant
 * @ClassName: CommonConstant
 * @Author: carrymaniac
 * @Description: 通用型常量，用于解决魔法值问题
 * @Date: 2019/11/27 5:37 下午
 * @Version:
 */
public interface CommonConstant {
    Integer ExperimentTextLength = 10;
    Byte STUDENT = 0;
    public final Byte TEACHER = 1;
    Byte ADMIN = 2;

    String TOPIC_CourseUpdate = "courseUpdate";
    String TOPIC_AchievenmtUpdate = "achievenmtUpdate";
    String TOPIC_NotifyJob = "notifyJob";

    int SYSTEM_USER_ID = 0;

    int ENTITY_TYPE_COURSE = 0;
    int ENTITY_TYPE_EXPERIMENT = 1;
    int ENTITY_TYPE_RECORD = 2;




}
