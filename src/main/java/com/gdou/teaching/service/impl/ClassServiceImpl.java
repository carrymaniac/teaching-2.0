package com.gdou.teaching.service.impl;

import com.gdou.teaching.Enum.ClazzStatusEnum;
import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.Enum.UserIdentEnum;
import com.gdou.teaching.Enum.UserStatusEnum;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.mbg.mapper.ClassMapper;
import com.gdou.teaching.mbg.mapper.UserMapper;
import com.gdou.teaching.mbg.model.Class;
import com.gdou.teaching.mbg.model.ClassExample;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.mbg.model.UserExample;
import com.gdou.teaching.service.ClassService;
import org.apache.ibatis.javassist.ClassMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.service.impl
 * @ClassName: ClassServiceImpl
 * @Author: carrymaniac
 * @Description:
 * @Date: 2019/12/9 2:16 下午
 * @Version:
 */
@Service
public class ClassServiceImpl implements ClassService {
    @Autowired
    ClassMapper classMapper;
    @Autowired
    UserMapper userMapper;
    @Override
    public Class registerClass(String clazzName) {
        Class clazz = new Class();
        ClassExample classExample = new ClassExample();
        classExample.createCriteria().andClassNameEqualTo(clazzName).andClassStatusEqualTo(ClazzStatusEnum.NORMAL.getCode().byteValue());
        List<Class> classes = classMapper.selectByExample(classExample);
        if(classes!=null&&!classes.isEmpty()){
            throw new TeachingException(ResultEnum.CLASSNAME_ERROR);
        }
        clazz.setClassName(clazzName);
        clazz.setClassSize(0);
        clazz.setClassStatus(ClazzStatusEnum.NORMAL.getCode().byteValue());
        classMapper.insert(clazz);
        return clazz;
    }

    @Override
    public Class getClassByClazzId(Integer clazzId) {
        return classMapper.selectByPrimaryKey(clazzId);
    }

    @Override
    public List<Class> getClassesByPage(Integer page, Integer size) {
        //todo 引入PageHelper再完成
        return null;
    }

    @Override
    public Boolean updateClazz(Class clazz) {
        return classMapper.updateByPrimaryKeySelective(clazz)>0;
    }


    @Override
    public List<Class> getAllClazzList() {
        ClassExample classExample = new ClassExample();
        classExample.createCriteria().andClassStatusEqualTo(ClazzStatusEnum.NORMAL.getCode().byteValue());
        List<Class> classes = classMapper.selectByExample(classExample);
        return classes;
    }

    @Override
    public List<User> getStudentByClazzId(Integer clazzId) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andClassIdEqualTo(clazzId).andUserStatusEqualTo(UserStatusEnum.NORMAL.getCode().byteValue()).andUserIdentEqualTo(UserIdentEnum.SUTUDENT.getCode().byteValue());
        return userMapper.selectByExample(userExample);
    }

    @Override
    public Integer getStudentCountByClazzId(Integer clazzId) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andClassIdEqualTo(clazzId).andUserStatusEqualTo(UserStatusEnum.NORMAL.getCode().byteValue()).andUserIdentEqualTo(UserIdentEnum.SUTUDENT.getCode().byteValue());
        return userMapper.countByExample(userExample);
    }
}
