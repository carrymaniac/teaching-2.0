package com.gdou.teaching.util;

import com.gdou.teaching.form.UserRegisterForm;
import com.gdou.teaching.mbg.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

/**
 * @author carrymaniac
 * @date Created in 17:11 2019-08-09
 * @description
 **/
@Slf4j
public class UserRegisterForm2User {
    public static User convert(UserRegisterForm form){
        User user = new User();
        BeanUtils.copyProperties(form,user);
        log.debug("转换完后：{}",user);
        return user;
    }
}
