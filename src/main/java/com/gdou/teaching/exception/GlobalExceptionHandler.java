package com.gdou.teaching.exception;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.exception
 * @ClassName: GlobalExceptionHandler
 * @Author: carrymaniac
 * @Description: 全局异常处理器
 * @Date: 2019/12/22 2:10 下午
 * @Version:
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultVO exceptionHandler(HttpServletRequest httpServletRequest, Exception e){
        log.error("服务错误:", e);
        return ResultVOUtil.fail(ResultEnum.SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(value = TeachingException.class)
    public ResultVO teachingExceptionHandler(HttpServletRequest httpServletRequest,TeachingException e){
        return ResultVOUtil.fail(500,"很抱歉，您访问的服务出错了");
    }

}
