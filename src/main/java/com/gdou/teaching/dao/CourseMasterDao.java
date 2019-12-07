package com.gdou.teaching.dao;

import org.springframework.stereotype.Repository;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.dao
 * @ClassName: CourseMasterDao
 * @Author: carrymaniac
 * @Description: 课程主表自定义Dao
 * @Date: 2019/12/7 4:19 下午
 * @Version:
 */
@Repository
public interface CourseMasterDao {
    /**
     * 根据课程主表ID删除课程主表记录
     * @param courseId
     * @return
     */
    boolean deleteCourseMasterByCourseId(Integer  courseId);
}
