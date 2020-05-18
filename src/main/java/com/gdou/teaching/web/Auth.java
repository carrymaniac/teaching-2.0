package com.gdou.teaching.web;
import com.gdou.teaching.Enum.UserIdentEnum;

import java.lang.annotation.*;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.web
 * @ClassName: Auth
 * @Author: carrymaniac
 * @Description: 权限校验注解
 * @Date: 2019/12/28 1:36 下午
 * @Version:
 */

@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth
{
    UserIdentEnum user() default UserIdentEnum.TEACHER;
}
