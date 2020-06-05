package com.gdou.teaching.service.impl;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.Enum.UserIdentEnum;
import com.gdou.teaching.Enum.UserStatusEnum;
import com.gdou.teaching.dao.UserDao;
import com.gdou.teaching.dao.UserInfoDao;
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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gdou.teaching.Enum.ResultEnum.PARAM_ERROR;
import static com.gdou.teaching.Enum.ResultEnum.USER_NOT_EXIST;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final
    UserMapper userMapper;
    private final
    UserDao userDao;
    private final
    UserInfoDao userInfoDao;
    private final
    UserInfoMapper userInfoMapper;
    private final
    ClassMapper classMapper;

    public UserServiceImpl(UserMapper userMapper, UserDao userDao, UserInfoDao userInfoDao, UserInfoMapper userInfoMapper, ClassMapper classMapper) {
        this.userMapper = userMapper;
        this.userDao = userDao;
        this.userInfoDao = userInfoDao;
        this.userInfoMapper = userInfoMapper;
        this.classMapper = classMapper;
    }

    /**
     * 对user对象进行盐值加密、生成头像、设置用户状态
     * @param user
     */
    private void genUser(User user) {
        String salt = UUID.randomUUID().toString().substring(0,5);
        user.setSalt(salt);
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setUserStatus(UserStatusEnum.NORMAL.getCode().byteValue());
        user.setPassword(DigestUtils.md5DigestAsHex((user.getPassword()+salt).getBytes()));
    }

    @Override
    public UserDTO selectOne(int id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user==null){
            log.info("[UserServiceImpl]-,getUserById,学生信息不存在,userId:{}",id);
            throw new TeachingException(USER_NOT_EXIST);
        }
        UserDTO userDTO=new UserDTO();
        BeanUtils.copyProperties(user,userDTO);
        return userDTO;
    }

    @Override
    public Boolean addUser(User user) {
        //判断学号信息是否重复
        String userNumber = user.getUserNumber();
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNumberEqualTo(userNumber);
        List<User> users = userMapper.selectByExample(userExample);
        if(users!=null&&!users.isEmpty()){
            return false;
        }
        genUser(user);
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
    public UserDTO selectOne(String userNumber) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNumberEqualTo(userNumber);
        List<User> users = userMapper.selectByExample(userExample);
        if(users==null||users.isEmpty()){
            return null;
        }
        UserDTO userDTO=new UserDTO();
        BeanUtils.copyProperties(users.get(0),userDTO);
        return userDTO;
    }

    @Override
    public Boolean addUserByBatch(List<User> userList) {
        List<String> userNumbers = userList.stream().map(User::getUserNumber).collect(Collectors.toList());
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserNumberIn(userNumbers);
        List<User> users = userMapper.selectByExample(userExample);
        if(users!=null&&!users.isEmpty()){
            StringBuilder sb = new StringBuilder();
            sb.append("用户");
            users.forEach(user -> {
                sb.append(" 学号-").append(user.getUserNumber());
                sb.append(" 姓名-").append(user.getNickname());
                sb.append(",");
            });
            sb.deleteCharAt(sb.length()-1);
            sb.append(" 已存在,请检查数据是否有误");
            throw new TeachingException(PARAM_ERROR.getCode(),sb.toString());
        }

        //todo 需要在info表插入信息，需要看看怎么调整一下这些信息
        userList.forEach(user -> {
            genUser(user);
        });
        return userDao.insertList(userList)==userList.size();
    }

    @Override
    public Boolean addUserInfoByBatch(List<Integer> userIdList, String college, String series, String major) {
        List<UserInfo> userInfoList=userIdList.stream().map(userId -> {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userId);
            userInfo.setCollege(college);
            userInfo.setSeries(series);
            userInfo.setMajor(major);
            return userInfo;
        }).collect(Collectors.toList());
        return userInfoDao.insertList(userInfoList)==userIdList.size();
    }

    @Override
    public UserDTO getUserDetailByUserId(Integer userId) {
        UserDTO userDTO = new UserDTO();
        User user = userMapper.selectByPrimaryKey(userId);
        if(user==null){
            throw new TeachingException(USER_NOT_EXIST);
        }
        BeanUtils.copyProperties(user,userDTO);
        if(user.getClassId()!=0){
            Class aClass = classMapper.selectByPrimaryKey(user.getClassId());
            userDTO.setClassName(aClass.getClassName());
        }
        UserInfoExample userInfoExample = new UserInfoExample();
        userInfoExample.createCriteria().andUserIdEqualTo(user.getUserId());
        List<UserInfo> userInfos = userInfoMapper.selectByExample(userInfoExample);
        if(userInfos==null||userInfos.isEmpty()){
            return userDTO;
        }
        UserInfo userInfo = userInfos.get(0);
        BeanUtils.copyProperties(userInfo,userDTO);
        return userDTO;
    }

    @Override
    public List<UserDTO> getStudentListByClassId(Integer classId) {
        UserExample userExample = new UserExample();
        if(classId!=0){
            userExample.createCriteria().andClassIdEqualTo(classId).andUserStatusEqualTo(UserStatusEnum.NORMAL.getCode().byteValue()).andUserIdentEqualTo(UserIdentEnum.SUTUDENT.getCode().byteValue());
        }else {
            userExample.createCriteria().andUserStatusEqualTo(UserStatusEnum.NORMAL.getCode().byteValue()).andUserIdentEqualTo(UserIdentEnum.SUTUDENT.getCode().byteValue());
        }
        List<User> users = userMapper.selectByExample(userExample);
        if (users==null||users.isEmpty()){
            return null;
        }
        List<UserDTO> userDTOS = users.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        }).collect(Collectors.toList());
        return userDTOS;
    }

    @Override
    public PageInfo getUserListByClassIdAndKeywordAndIdentInPage(Integer classId,Integer page,Integer size,String keyword,Integer ident) {
        PageHelper.startPage(page,size);
        List<User> users =  userDao.selectByClassIdAndKeyword(classId,keyword,ident);
        PageInfo pageInfo = new PageInfo(users);
        return pageInfo;
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
        if(user==null){
            throw new TeachingException(USER_NOT_EXIST);
        }
        if(!DigestUtils.md5DigestAsHex((oldPassword+user.getSalt()).getBytes()).equals(user.getPassword())){
            throw new TeachingException(PARAM_ERROR.getCode(),"原密码错误");
        }
        if(DigestUtils.md5DigestAsHex((oldPassword+user.getSalt()).getBytes()).equals(DigestUtils.md5DigestAsHex((newPassword+user.getSalt()).getBytes()))){
            throw new TeachingException(PARAM_ERROR.getCode(),"新密码不能与原密码相等");
        }
        //重新更新盐值
        String salt = UUID.randomUUID().toString().substring(0,5);
        user.setSalt(salt);
        user.setPassword(DigestUtils.md5DigestAsHex((newPassword+salt).getBytes()));
        return userMapper.updateByPrimaryKey(user)>0;
    }

    @Override
    public Boolean resetPassword(Integer userId, String newPassword) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user==null){
            throw new TeachingException(USER_NOT_EXIST);
        }
        //重新更新盐值
        String salt = UUID.randomUUID().toString().substring(0,5);
        user.setSalt(salt);
        user.setPassword(DigestUtils.md5DigestAsHex((newPassword+salt).getBytes()));
        return userMapper.updateByPrimaryKey(user)>0;
    }

    @Override
    public Boolean banUserByBatch(List<Integer> userIds){
        return updateStatusByUserIds(userIds, UserStatusEnum.BAN);
    }

    @Override
    public Boolean recoverUserByBatch(List<Integer> userIds){
        return updateStatusByUserIds(userIds, UserStatusEnum.NORMAL);
    }

    @Override
    public Boolean deleteUserByBatch(List<Integer> userIds) {
        return updateStatusByUserIds(userIds, UserStatusEnum.INVALID);
    }

    private Boolean updateStatusByUserIds(List<Integer> userIds, UserStatusEnum stauts) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserIdIn(userIds);
        User user = new User();
        user.setUserStatus(stauts.getCode().byteValue());
        int i = userMapper.updateByExampleSelective(user, userExample);
        return i==userIds.size();
    }

    @Override
    public void updateUserInfo(UserDTO user) {
        if(!StringUtils.isEmpty(user.getHeadUrl())){
            User u1 = new User();
            u1.setUserId(user.getUserId());
            u1.setHeadUrl(user.getHeadUrl());
            userMapper.updateByPrimaryKeySelective(u1);
        }
        UserInfoExample example = new UserInfoExample();
        example.createCriteria().andUserIdEqualTo(user.getUserId());
        List<UserInfo> userInfos = userInfoMapper.selectByExample(example);
        UserInfo userInfo  =  userInfos.get(0);
        userInfo.setUserId(user.getUserId());
        if(!StringUtils.isEmpty(user.getPhone())){
            userInfo.setPhone(user.getPhone());
        }
        if(!StringUtils.isEmpty(user.getPhone())){
            userInfo.setMail(user.getMail());
        }
        userInfoMapper.updateByPrimaryKeySelective(userInfo);
    }

}
