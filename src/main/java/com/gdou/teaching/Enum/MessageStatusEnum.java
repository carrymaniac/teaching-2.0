package com.gdou.teaching.Enum;

import lombok.Getter;

/**
 * @author bo
 * @date Created in 22:22 2020/1/14
 * @description
 **/
@Getter
public enum MessageStatusEnum implements CodeEnum {
    UNREAD(0,"未读"),
    READ(1,"已读");
    private final Integer code;
    private final String msg;
    MessageStatusEnum(Integer code,String msg) {
        this.code =code;
        this.msg = msg;
    }
}
