package com.gdou.teaching.service;

import com.gdou.teaching.mbg.model.Class;
import com.gdou.teaching.mbg.model.User;

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
     * @param clazzName 班级名
     * @return
     */
    Class registerClass(String clazzName);

    Class getClassByClazzId(Integer clazzId);

    /**
     * 分页获取班级列表
     * @param page
     * @param size
     * @return
     */
    List<Class> getClassesByPage(Integer page, Integer size);

    /**
     * 班级信息修改
     * @param clazz
     * @return
     */
    Boolean updateClazz(Class clazz);

    /**
     * 获取班级列表
     */
    List<TreeMap> getAllClazzList();

    /**
     * 根据clazzId 获取学生列表
     * @param clazzId
     * @return
     */
    List<User> getStudentByClazzId(Integer clazzId);

    /**
     * 根据classId来更新学生数量
     * @param clazzId
     * @return
     */
    boolean updateStudentCountByClazzId(Integer clazzId);

}
