package com.gdou.teaching.service;

import com.gdou.teaching.mbg.model.Class;
import com.gdou.teaching.mbg.model.User;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author carrymaniac
 * @date Created in 14:34 2019-08-14
 * @description
 **/
public interface ClassService {

    /**
     * 注册一个班级
     * @param clazzName  班级名
     * @param classSize  班级人数
     * @return
     */
    Class registerClass(String clazzName,Integer classSize);

    /**
     * 通过classID获取class
     * @param classId
     * @return
     */
    Class selectOne(Integer classId);

    /**
     * 班级信息修改
     * @param clazz
     * @return
     */
    Boolean updateClazz(Class clazz);

    /**
     * 获取班级列表,用于前端页面下拉框
     * @return 一个包含id和className的List
     */
    List<TreeMap> getAllClazzList();

    /**
     * 根据clazzId 获取学生列表
     * @param clazzId
     * @return
     */
    List<User> getStudentByClazzId(Integer clazzId);

}
