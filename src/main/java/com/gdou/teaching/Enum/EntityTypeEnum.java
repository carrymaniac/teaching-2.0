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
public enum EntityTypeEnum implements CodeEnum{
    COURSE(0,"课程文件"),
    EXPERIMENT(1,"实验文件"),
    RECORD(3,"学生提交记录文件")
    ;
    private final Integer code;
    private final String msg;
    EntityTypeEnum(Integer code, String msg) {
        this.code =code;
        this.msg = msg;
    }
}
