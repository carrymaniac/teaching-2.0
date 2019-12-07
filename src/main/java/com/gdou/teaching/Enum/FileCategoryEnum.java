package com.gdou.teaching.Enum;

import lombok.Getter;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.Enum
 * @ClassName: FileCategory
 * @Author: carrymaniac
 * @Description: File's Category Enum
 * @Date: 2019/11/7 10:21 上午
 * @Version:
 */
@Getter
public enum FileCategoryEnum implements CodeEnum{
    COURSE_FILE(0,"课程文件"),
    EXPERIMENT_FILE(1,"实验文件"),
    EXPERIMENT_ANSWER_FILE(2,"实验答案文件"),
    RECORD_FILE(3,"学生提交记录文件")
    ;
    private final Integer code;
    private final String msg;
    FileCategoryEnum(Integer code,String msg) {
        this.code =code;
        this.msg = msg;
    }
}
