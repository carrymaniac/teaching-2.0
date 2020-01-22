package com.gdou.teaching.exception;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
        StackTraceElement[] stackTrace = e.getStackTrace();
        StackTraceElement stackTraceElement = stackTrace[0];
        log.error("服务报错:报错类为:{},报错文件为:{},报错的方法为:{},报错的行数为{}",
                stackTraceElement.getClassName(),
                stackTraceElement.getFileName(),
                stackTraceElement.getMethodName(),
                stackTraceElement.getLineNumber()
                );
        log.error("完整报错:", e);
        return ResultVOUtil.fail(ResultEnum.SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResultVO MissingServletRequestParameterException(HttpServletRequest httpServletRequest, MissingServletRequestParameterException e){
        return ResultVOUtil.fail(ResultEnum.PARAM_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResultVO HttpRequestMethodNotSupportedException(HttpServletRequest httpServletRequest, HttpRequestMethodNotSupportedException e){
        return ResultVOUtil.fail(404,"请求方法不存在");
    }


    @ResponseBody
    @ExceptionHandler(value = TeachingException.class)
    public ResultVO teachingExceptionHandler(HttpServletRequest httpServletRequest,TeachingException e){
        if(e.getCode()==500){
            return ResultVOUtil.fail(500,"很抱歉，您访问的服务出错了");
        }else{
            return ResultVOUtil.fail(e);
        }
    }

}
