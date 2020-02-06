package com.gdou.teaching.web.interceptor;

import com.gdou.teaching.constant.CookieConstant;
import com.gdou.teaching.constant.RedisConstant;
import com.gdou.teaching.dataobject.HostHolder;
import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.service.UserService;
import com.gdou.teaching.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author carrymaniac
 * @date Created in 13:24 2019-08-10
 * @description
 **/
@Slf4j
public class UserIntercepter implements HandlerInterceptor {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if(cookie==null||cookie.getValue()==null){
            return true;
        }

        String tokenValue = stringRedisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));
        if(StringUtils.isEmpty(tokenValue)){
            return true;
        }
        UserDTO user = userService.selectOne(Integer.parseInt(tokenValue));
        log.info("UserIntercepter】此刻注入用户：user:{}",user);
        hostHolder.setUser(user);
        return true;
    }

    /**
     * 往每一个视图层中添加user数据，以便使用
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && hostHolder.getUser() != null) {
            modelAndView.addObject("user", hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("【UserIntercepter】清除用户信息:{}",hostHolder.getUser());
        hostHolder.clear();
    }
}
