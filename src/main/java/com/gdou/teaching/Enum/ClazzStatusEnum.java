package com.gdou.teaching.Enum;

import lombok.Getter;

/**
 * @author carrymaniac
 * @date Created in 14:54 2019-08-14
 * @description
 **/
@Getter
public enum  ClazzStatusEnum implements CodeEnum{

    NORMAL(1,"正常")

    ;
    private final Integer code;
    private final String msg;

    ClazzStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
