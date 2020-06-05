package com.gdou.teaching.web.interceptor;

import com.gdou.teaching.constant.CookieConstant;
import com.gdou.teaching.constant.RedisConstant;
import com.gdou.teaching.dataobject.HostHolder;
import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.service.UserService;
import com.gdou.teaching.util.CookieUtil;
import com.gdou.teaching.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class UserInterceptorForJWT implements HandlerInterceptor {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    UserService userService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    JWTUtil jwtUtil;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /**
         * 流程：1.从request头中获取authHeader
         *      2.从中获取用户ID，去Redis中查询用户ID对于的Token
         *      3.若Token不相同，或者是不存在，则说明为假 不注入用户
         *      4.若相同则说明验证成功
         */
        String authHeader = request.getHeader(this.tokenHeader);
        if (authHeader != null && authHeader.startsWith(this.tokenHead)) {
            String token = authHeader.substring(this.tokenHead.length()+1);
            Integer userID = jwtUtil.getUserIdFromToken(token);
            if(userID==null){
                log.info("[UserInterceptorForJWT]无法从token中获取用户信息，token为:{}",token);
                return true;
            }
            String format = String.format(RedisConstant.TOKEN_PREFIX, userID);
            String tokenValue = stringRedisTemplate.opsForValue().get(format);
            if (!StringUtils.isEmpty(tokenValue) && tokenValue.equals(token)) {
                //当Redis中的token不为空且与Request中的Token相同时，说明验证成功 可以注入用户数据了
                UserDTO user = userService.selectOne(userID);
                log.info("【UserInterceptor】此刻注入用户：user:{}", user);
                hostHolder.setUser(user);
            }else{
                log.info("【UserInterceptor】校验失败，无法注入用户");
            }
        }
        return true;
    }

    /**
     * 往每一个视图层中添加user数据，以便使用
     *
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
        log.info("【UserIntercepter】清除用户信息:{}", hostHolder.getUser());
        hostHolder.clear();
    }
}
