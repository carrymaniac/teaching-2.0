package com.gdou.teaching.exception;

import com.gdou.teaching.Enum.ResultEnum;
import lombok.Getter;

/**
 * @author carrymaniac
 * @date Created in 18:36 2019-07-28
 * @description
 **/
@Getter
public class TeachingException extends RuntimeException{
    private Integer code ;

    public TeachingException(ResultEnum resultEnum){
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }
    public TeachingException(Integer code, String msg){
        super(msg);
        this.code = code ;
    }
}
