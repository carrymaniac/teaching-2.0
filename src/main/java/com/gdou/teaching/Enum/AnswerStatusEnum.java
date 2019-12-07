package com.gdou.teaching.Enum;

import lombok.Getter;

/**
 * @author bo
 * @date Created in 18:17 2019/10/14
 * @description 实验答案状态枚举类
 **/
@Getter
public enum AnswerStatusEnum implements CodeEnum {
    NORMAL(0,"正常"),
    INVALID(1,"已删除"),
    ;

    private final Integer code;
    private final String msg;

    AnswerStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
