package com.gdou.teaching.util;


import com.gdou.teaching.Enum.CodeEnum;

/**
 * 枚举工具类
 */
public class EnumUtil {
    /**
     * 通过枚举code获取信息
     * @param code
     * @param enumClass
     * @param <T>
     * @return
     */
    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass){
        for(T each:enumClass.getEnumConstants()){
            if(code.equals(each.getCode())){
                return each;
            }
        }
        return null;
    }
}
