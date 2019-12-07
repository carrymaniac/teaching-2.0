package com.gdou.teaching.service;


import com.gdou.teaching.dto.AchievementDTO;
import com.gdou.teaching.mbg.model.Achievement;


import java.util.List;

/**
 * @author bo
 * @date Created in 13:14 2019/9/12
 * @description
 **/
public interface AchievementService {

    /**
     * 根据课程id、班级id批量增加成绩记录.
     * @param courseId
     * @param clazzId
     */
    boolean addAchievementByClazzId(Integer courseId, Integer clazzId);

    /**
     * 根据课程id,学生列表id增加成绩记录
     * @param courseId
     * @param studentIdList
     * @return
     */
    boolean addAchievementByStudentList(Integer courseId, List<Integer> studentIdList);

    /**
     * 根据课程id,学生列表id删除成绩记录
     * @param courseId
     * @param studentIdList
     * @return
     */
    boolean deleteAchievementByStudentList(Integer courseId, List<Integer> studentIdList);
//
//    /**
//     * 根据课程id 查询班级id列表
//     */
//    List<ClazzVO> getClazzIdListByCourseId(Integer courseId);

    /**
     * 根据用户ID查询学生选修的课程数量
     * @param userId
     * @return
     */
    Integer getCourseNumberByUserId(Integer userId);

    /**
     * 根据用户ID和课程ID查询学生的总成绩
     * @param userId
     * @param courseId
     * @return
     */
    Achievement getAchievementByUserIdAndCourseId(Integer userId, Integer courseId);

    /**
     * 根据课程Id查询学生的成绩记录
     * @param courseId
     * @return
     */
    List<AchievementDTO> getAchievementByCourseId(Integer courseId);


}
