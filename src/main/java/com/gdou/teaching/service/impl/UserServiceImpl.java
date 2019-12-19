package com.gdou.teaching.service.impl;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.Enum.UserIdentEnum;
import com.gdou.teaching.Enum.UserStatusEnum;
import com.gdou.teaching.dao.UserDao;
import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.mbg.mapper.ClassMapper;
import com.gdou.teaching.mbg.mapper.UserInfoMapper;
import com.gdou.teaching.mbg.mapper.UserMapper;
import com.gdou.teaching.mbg.model.Class;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.mbg.model.UserExample;
import com.gdou.teaching.mbg.model.UserInfo;
import com.gdou.teaching.mbg.model.UserInfoExample;
import com.gdou.teaching.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gdou.teaching.Enum.ResultEnum.PARAM_ERROR;
import static com.gdou.teaching.Enum.ResultEnum.USER_NOT_EXIST;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.service
 * @ClassName: UserServiceImpl
 * @Author: carrymaniac
 * @Description:
 * @Date: 2019/12/2 4:50 下午
 * @Version:
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserDao userDao;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    ClassMapper classMapper;

    @Override
    public User getUserById(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean register(User user) {
        //判断学号信息是否重复
        String userNumber = user.getUserNumber();
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNumberEqualTo(userNumber);
        List<User> users = userMapper.selectByExample(userExample);
        if(users!=null&&!users.isEmpty()){
            return false;
        }
        String salt = UUID.randomUUID().toString().substring(0,5);
        user.setSalt(salt);
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setUserStatus(UserStatusEnum.NORMAL.getCode().byteValue());
        user.setPassword(DigestUtils.md5DigestAsHex((user.getPassword()+salt).getBytes()));
        return userMapper.insert(user)>0;
    }


    @Override
    public User login(String username, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNumberEqualTo(username);
        List<User> users = userMapper.selectByExample(userExample);
        if(users==null||users.isEmpty()){
            throw new TeachingException(ResultEnum.USER_NOT_EXIST);
        }
        User user = users.get(0);
        if(!DigestUtils.md5DigestAsHex((password+user.getSalt()).getBytes()).equals(user.getPassword())){
            throw new TeachingException(ResultEnum.USER_PASSWORD_ERROR);
        }
        return user;
    }

    @Override
    public User getUserByUserNumber(String userNumber) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNumberEqualTo(userNumber);
        List<User> users = userMapper.selectByExample(userExample);
        if(users==null||users.isEmpty()){
            return null;
        }
        return users.get(0);
    }

    @Override
    public Boolean addUserByBatch(List<User> users) {
        List<String> userNumbers = users.stream().map(User::getUserNumber).collect(Collectors.toList());
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNumberIn(userNumbers);
        List<User> userList = userMapper.selectByExample(userExample);
        if(userList!=null&&!userList.isEmpty()){
            StringBuilder sb = new StringBuilder();
            sb.append("用户");
            userList.forEach(user -> {
                sb.append(" 学号-").append(user.getUserNumber());
                sb.append(" 姓名-").append(user.getNickname());
                sb.append(",");
            });
            sb.deleteCharAt(sb.length()-1);
            sb.append(" 已存在,请检查数据是否有误");
            throw new TeachingException(PARAM_ERROR.getCode(),sb.toString());
        }
        //todo 需要在info表插入信息，需要看看怎么调整一下这些信息
        return userDao.insertList(userList)==userList.size();
    }



    @Override
    public Boolean deleteUserByBatch(List<String> userNumbers) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNumberIn(userNumbers);
        User user = new User();
        user.setUserStatus(UserStatusEnum.INVALID.getCode().byteValue());
        int i = userMapper.updateByExampleSelective(user, userExample);
        return i==userNumbers.size();
    }

    @Override
    public UserDTO getUserDetailByUserId(Integer userId) {
        UserDTO userDTO = new UserDTO();
        User user = userMapper.selectByPrimaryKey(userId);
        if(user==null){
            throw new TeachingException(USER_NOT_EXIST);
        }
        UserInfoExample userInfoExample = new UserInfoExample();
        userInfoExample.createCriteria().andUserIdEqualTo(user.getUserId());
        List<UserInfo> userInfos = userInfoMapper.selectByExample(userInfoExample);
        if(userInfos==null||userInfos.isEmpty()){
            throw new TeachingException(USER_NOT_EXIST);
        }
        UserInfo userInfo = userInfos.get(0);
        Class aClass = classMapper.selectByPrimaryKey(user.getClassId());
        BeanUtils.copyProperties(userInfo,userDTO);
        BeanUtils.copyProperties(user,userDTO);
        userDTO.setClassName(aClass.getClassName());
        return userDTO;
    }

    @Override
    public List<UserDTO> getStudentListByClassId(Integer classId) {
        UserExample userExample = new UserExample();
        if(classId!=0){
            userExample.createCriteria().andClassIdEqualTo(classId).andUserStatusEqualTo(UserStatusEnum.NORMAL.getCode().byteValue());
        }else {
            userExample.createCriteria().andUserStatusEqualTo(UserStatusEnum.NORMAL.getCode().byteValue());
        }
        List<User> users = userMapper.selectByExample(userExample);
        List<UserDTO> userDTOS = users.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        }).collect(Collectors.toList());
        return userDTOS;
    }

    @Override
    public List<User> selectTeacherList() {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserIdentEqualTo(UserIdentEnum.TEACHER.getCode().byteValue());
        List<User> teachers = userMapper.selectByExample(userExample);
        return teachers;
    }

    @Override
    public List<UserDTO> getUsersByUserId(List<Integer> userIds) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        List<UserDTO> userDTOS = users.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        }).collect(Collectors.toList());
        return userDTOS;
    }

    @Override
    public Boolean updatePassword(Integer userId,String oldPassword ,String newPassword) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(!DigestUtils.md5DigestAsHex((oldPassword+user.getSalt()).getBytes()).equals(user.getPassword())){
            return false;
        }
        //重新更新盐值
        String salt = UUID.randomUUID().toString().substring(0,5);
        user.setSalt(salt);
        user.setPassword(DigestUtils.md5DigestAsHex((newPassword+salt).getBytes()));
        return userMapper.updateByPrimaryKey(user)>0;
    }
}
