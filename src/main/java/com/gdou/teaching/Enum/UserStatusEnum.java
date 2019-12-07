package com.gdou.teaching.Enum;

import lombok.Getter;

/**
 * @author carrymaniac
 * @date Created in 18:25 2019-07-28
 * @description
 **/
@Getter
public enum UserStatusEnum implements CodeEnum{
    NORMAL(0,"正常"),
    BAN(1,"被禁用"),
    INVALID(2,"注销"),
    ;
    private final String msg;
    private final Integer code;

    UserStatusEnum(int code,String msg) {
        this.msg = msg;
        this.code = code;
    }
}
