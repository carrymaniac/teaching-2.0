package com.gdou.teaching.util;


/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.util
 * @ClassName: CommonUtil
 * @Author: carrymaniac
 * @Description: 通用的工具类（之后再分类）
 * @Date: 2020/1/18 4:28 下午
 * @Version:
 */
public class CommonUtil {


    public static String genConversationId(Integer fromId,Integer toId){
        if (fromId.compareTo(toId)>=0) {
            return toId+"-"+fromId;
        }else {
            return fromId+"-"+toId;
        }
    }

    public static int getToId(String conversationId,int fromId){
        String[] split = conversationId.split("-");
        return split[0].equals(String.valueOf(fromId))?Integer.valueOf(split[1]):Integer.valueOf(split[0]);
    }
}
