package com.gdou.teaching.Enum;

import lombok.Getter;

/**
 * @author bo
 * @date Created in 23:29 2019/8/13
 * @description 实验状态枚举
 **/
@Getter
public enum ExperimentStatusEnum implements CodeEnum {
    LOCK(0,"锁定"),
    NORMAL(1,"正常"),
    BAN(2,"不可提交"),
    INVALID(3,"注销"),
    ;

    private final Integer code;
    private final String msg;

    ExperimentStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
