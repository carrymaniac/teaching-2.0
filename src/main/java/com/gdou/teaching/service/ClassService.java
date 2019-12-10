package com.gdou.teaching.service;

import com.gdou.teaching.mbg.model.Class;

import java.util.List;
import java.util.Map;

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
    List<Class> getAllClazzList();
}
