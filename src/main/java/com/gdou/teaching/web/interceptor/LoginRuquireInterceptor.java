package com.gdou.teaching.web.interceptor;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.dataobject.HostHolder;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author carrymaniac
 * @date Created in 13:32 2019-08-10
 * @description
 **/
@Slf4j
public class LoginRuquireInterceptor implements HandlerInterceptor {
    @Autowired
    private HostHolder hostHolder;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(hostHolder.getUser()==null){
            WebUtil.sendJsonMessage(response, ResultVOUtil.fail(ResultEnum.USER_NO_LOGIN));
            return false;
        }
        log.info("【LoginRuquireInterceptor】已有用户信息:{}",hostHolder.getUser());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
