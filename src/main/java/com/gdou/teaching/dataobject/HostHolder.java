package com.gdou.teaching.dataobject;

import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.mbg.model.User;
import org.springframework.stereotype.Component;

/**
 * @Author Ha
 * @DATE 2019/7/7 20:33
 **/
@Component
public class HostHolder {
    public static ThreadLocal<UserDTO> users = new ThreadLocal<UserDTO>();

    public UserDTO getUser() {
        return users.get();
    }

    public void setUser(UserDTO user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
