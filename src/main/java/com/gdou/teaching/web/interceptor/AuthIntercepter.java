package com.gdou.teaching.web.interceptor;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.Enum.UserIdentEnum;
import com.gdou.teaching.dataobject.HostHolder;
import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.util.WebUtil;
import com.gdou.teaching.web.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.web.interceptor
 * @ClassName: AuthIntercepter
 * @Author: carrymaniac
 * @Description: 校验权限拦截器
 * @Date: 2019/12/28 1:38 下午
 * @Version:
 */
@Slf4j
public class AuthIntercepter implements HandlerInterceptor {
    @Autowired
    HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("【AuthIntercepter】已拦截，开始进行判断");
        if (!handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            log.debug("can't cast handler to HandlerMethod.class");
            return true;
        }
        Auth annotation = ((HandlerMethod) handler).getMethod().getAnnotation(Auth.class);
        if(annotation==null){
            //如果方法上的注解为空的话，尝试获取类的注解
            annotation = ((HandlerMethod) handler).getMethod().getDeclaringClass().getAnnotation(Auth.class);
        }
        if(annotation==null){
            log.debug("can't find @Auth in this uri: {}",request.getRequestURI());
            return true;
        }
        UserIdentEnum admin = annotation.user();
        UserDTO user = hostHolder.getUser();
        if(admin.getCode().byteValue()==user.getUserIdent().byteValue()){
            return true;
        }else {
            log.info("【AuthIntercepter】权限校验失败,该方法要求权限为：{},而当前用户的权限为:{}",admin,user.getUserIdent());
            WebUtil.sendJsonMessage(response, ResultVOUtil.fail(ResultEnum.Forbidden));
            return false;
        }
    }
}
