package com.gdou.teaching.dao;

import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.mbg.model.UserInfo;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author bo
 * @date Created in 0:01 2020/1/16
 * @description
 **/
@Repository
public interface UserInfoDao {
    /**
     * 批量新增User用户信息，用于注册
     * @param userInfos
     * @return
     */
    int insertList (List<UserInfo> userInfos);
}
