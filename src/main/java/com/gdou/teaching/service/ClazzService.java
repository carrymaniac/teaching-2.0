package com.gdou.teaching.service;

import com.gdou.teaching.mbg.model.Class;

import java.util.List;
import java.util.Map;

/**
 * @author carrymaniac
 * @date Created in 14:34 2019-08-14
 * @description
 **/
public interface ClazzService {

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


    List<Integer> getStudentByClazzId(Integer clazzId);

    Integer getStudentCountByClazzId(Integer clazzId);

    /**
     * 获取班级列表
     */
    List<Map.Entry<Integer, String>> getAllClazzList();
}
