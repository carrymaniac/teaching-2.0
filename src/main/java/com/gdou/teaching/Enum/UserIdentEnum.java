package com.gdou.teaching.Enum;

import lombok.Getter;

/**
 * @author carrymaniac
 * @date Created in 18:25 2019-07-28
 * @description
 **/
@Getter
public enum UserIdentEnum implements CodeEnum {
    SUTUDENT(0,"学生"),
    TEACHER(1,"老师"),
    ADMIN(2,"管理员")
    ;
    private final String msg;
    private final Integer code;

    UserIdentEnum(int code,String msg) {
        this.msg = msg;
        this.code = code;
    }
}
