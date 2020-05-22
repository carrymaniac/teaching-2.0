package com.gdou.teaching.config;

import com.gdou.teaching.web.interceptor.AuthIntercepter;
import com.gdou.teaching.web.interceptor.LoginRuquireInterceptor;
import com.gdou.teaching.web.interceptor.UserIntercepter;
import com.gdou.teaching.web.interceptor.UserInterceptorForJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @author carrymaniac
 * @date Created in 13:36 2019-08-10
 * @description
 **/
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${teaching.uploadPath}")
    public String uploadPath;

    @Bean
    LoginRuquireInterceptor loginRuquireInterceptor(){
        return new LoginRuquireInterceptor();
    }
    @Bean
    UserIntercepter userIntercepter(){
        return new UserIntercepter();
    }
    @Bean
    UserInterceptorForJWT userInterceptorForJWT(){
        return new UserInterceptorForJWT();
    }
    @Bean
    AuthIntercepter authIntercepter(){return new AuthIntercepter();}

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptorForJWT())
                .excludePathPatterns("/common/kaptcha","/static/**",
                        "/dev/**","/user/login","/user/register",
                        "/index.html","/download/**",
                        "/user/loginForTest","/test/**","/common/file/getExcelTemple");
        registry.addInterceptor(loginRuquireInterceptor())
                .excludePathPatterns("/common/kaptcha","/static/**",
                        "/dev/**","/user/login","/user/register",
                        "/index.html","/download/**",
                        "/user/loginForTest","/test/**","/common/file/getExcelTemple");
        registry.addInterceptor(authIntercepter())
                .excludePathPatterns("/common/kaptcha","/static/**",
                        "/dev/**","/user/login",
                        "/user/register","/index.html",
                        "/download/**","/user/loginForTest","/test/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/download/**").addResourceLocations("file:"+uploadPath);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .maxAge(3600);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/index").setViewName("/index");
//        registry.addViewController("/").setViewName("/index");
    }
}
