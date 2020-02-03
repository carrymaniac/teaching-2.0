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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.ClassMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.*;

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
@Slf4j
public class ClassServiceImpl implements ClassService {
    private final ClassMapper classMapper;
    private final UserMapper userMapper;

    public ClassServiceImpl(ClassMapper classMapper, UserMapper userMapper) {
        this.classMapper = classMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Class registerClass(String clazzName,Integer classSize) {
        Class clazz = new Class();
        ClassExample classExample = new ClassExample();
        classExample.createCriteria().andClassNameEqualTo(clazzName).andClassStatusEqualTo(ClazzStatusEnum.NORMAL.getCode().byteValue());
        List<Class> classes = classMapper.selectByExample(classExample);
        if(classes!=null&&!classes.isEmpty()){
            throw new TeachingException(ResultEnum.CLASSNAME_ERROR);
        }
        clazz.setClassName(clazzName);
        clazz.setClassSize(classSize);
        clazz.setClassStatus(ClazzStatusEnum.NORMAL.getCode().byteValue());
        classMapper.insert(clazz);
        return clazz;
    }

    @Override
    public Class getClassByClazzId(Integer classId) {
        Class Class = classMapper.selectByPrimaryKey(classId);
        if(Class==null){
            log.info("[ClassServiceImpl]-getClassByClazzId,班级信息不存在,classId:{}",classId);
            throw new TeachingException(ResultEnum.CLASS_NOT_EXIST);
        }
        return Class;
    }

    @Override
    public PageInfo getClassesByPage(Integer page, Integer size) {
        PageHelper.startPage(page,size);
        ClassExample classExample = new ClassExample();
        classExample.createCriteria();
        List<Class> classes = classMapper.selectByExample(classExample);
        PageInfo<Class> pageInfo = new PageInfo<>(classes);
        return pageInfo;
    }

    @Override
    public Boolean updateClazz(Class clazz) {
        return classMapper.updateByPrimaryKeySelective(clazz)>0;
    }


    @Override
    public List<TreeMap> getAllClazzList() {
        List<TreeMap> list = new ArrayList<>();
        ClassExample classExample = new ClassExample();
        classExample.createCriteria().andClassStatusEqualTo(ClazzStatusEnum.NORMAL.getCode().byteValue());
        List<Class> classes = classMapper.selectByExample(classExample);
        if(classes==null||classes.isEmpty()){
            log.info("[ClassServiceImpl]-获取班级列表,班级信息不存在");
            throw new TeachingException(ResultEnum.CLASS_NOT_EXIST);
        }
        classes.forEach(clazz -> {
            TreeMap<String,Object> map = new TreeMap<>();
            map.put("classId",clazz.getClassId());
            map.put("className",clazz.getClassName());
            list.add(map);
        });
        Collections.sort(list, (o1, o2) -> Collator.getInstance(Locale.CHINESE).compare(o1.get("className"),o2.get("className")));
        return list;
    }

    @Override
    public List<User> getStudentByClazzId(Integer clazzId) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andClassIdEqualTo(clazzId).andUserStatusEqualTo(UserStatusEnum.NORMAL.getCode().byteValue()).andUserIdentEqualTo(UserIdentEnum.SUTUDENT.getCode().byteValue());
        return userMapper.selectByExample(userExample);
    }

    @Override
    public boolean updateStudentCountByClazzId(Integer clazzId) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andClassIdEqualTo(clazzId).andUserStatusEqualTo(UserStatusEnum.NORMAL.getCode().byteValue()).andUserIdentEqualTo(UserIdentEnum.SUTUDENT.getCode().byteValue());
        int number = userMapper.countByExample(userExample);
        Class cl = new Class();
        cl.setClassId(clazzId);
        cl.setClassSize(number);
        return classMapper.updateByPrimaryKeySelective(cl)>0;
    }
}
