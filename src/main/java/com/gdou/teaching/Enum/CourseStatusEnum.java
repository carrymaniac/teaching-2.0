package com.gdou.teaching.Enum;

import lombok.Getter;

/**
 * @author bo
 * @date Created in 23:14 2019/8/13
 * @description  课程状态枚举
 **/
@Getter
public enum CourseStatusEnum implements CodeEnum {
    NORMAL(0,"正常"),
    BAN(1,"被禁用"),
    END(3,"已结束"),
    INVALID(2,"删除"),
    ;

    private final Integer code;
    private final String msg;

    CourseStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
