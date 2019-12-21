package com.gdou.teaching.Enum;

import lombok.Getter;

/**
 * @author bo
 * @date Created in 23:29 2019/8/13
 * @description 实验状态枚举
 **/
@Getter
public enum ExperimentStatusEnum implements CodeEnum {
    NORMAL(0,"正常"),
    LOCK(1,"锁定"),
    INVALID(2,"删除"),
    END(3,"已结束"),
    ;

    private final Integer code;
    private final String msg;

    ExperimentStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
