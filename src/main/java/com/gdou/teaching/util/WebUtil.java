package com.gdou.teaching.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.util
 * @ClassName: WebUtil
 * @Author: carrymaniac
 * @Description: web工具类
 * @Date: 2019/10/10 7:09 下午
 * @Version:
 */
@Slf4j
public class WebUtil {
    public static void sendJsonMessage(HttpServletResponse response, Object obj) throws Exception {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream outputStream = null;
        response.setContentType("application/json; charset=utf-8");
        //必须使用outputstream,调用writer报错
        try {
            outputStream = response.getOutputStream();
            outputStream.write(JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat).getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            log.error("response error",e);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            response.flushBuffer();
        }
    }
}
