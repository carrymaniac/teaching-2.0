package com.gdou.teaching.Enum;

import lombok.Getter;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.Enum
 * @ClassName: RecordEnum
 * @Author: carrymaniac
 * @Description: Record状态枚举类
 * @Date: 2019/9/20 5:57 下午
 * @Version:
 */
@Getter
public enum RecordStatusEnum implements CodeEnum {
    REVIEWING(0,"审核中"),
    PASS(1,"通过审核"),
    NOT_PASS(2,"未通过审核"),
    NOT_FINISH(3,"未提交过"),
    LOCK(4,"实验锁定中");
    private final Integer code;
    private final String msg;
    RecordStatusEnum(Integer code,String msg) {
        this.code =code;
        this.msg = msg;
    }
}
