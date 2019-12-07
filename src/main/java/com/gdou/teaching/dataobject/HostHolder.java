package com.gdou.teaching.dataobject;

import com.gdou.teaching.mbg.model.User;
import org.springframework.stereotype.Component;

/**
 * @Author Ha
 * @DATE 2019/7/7 20:33
 **/
@Component
public class HostHolder {
    public static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
