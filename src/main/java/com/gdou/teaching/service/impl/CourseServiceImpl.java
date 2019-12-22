package com.gdou.teaching.service.impl;

import com.gdou.teaching.Enum.CourseStatusEnum;
import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.dao.CourseMasterDao;
import com.gdou.teaching.dto.CourseDTO;
import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.mbg.mapper.*;
import com.gdou.teaching.mbg.model.*;
import com.gdou.teaching.service.CourseService;
import com.gdou.teaching.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
<<<<<<< HEAD
 * @author carrymaniac
 * @date Created in 14:26 2019-08-14
=======
 * @author bo
 * @date Created in 14:26 2019/8/14
>>>>>>> CourseDev1
 * @description
 **/
@Service
@Slf4j
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseMasterMapper courseMasterMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CourseDetailMapper courseDetailMapper;
    @Autowired
    AchievementMapper achievementMapper;
    @Autowired
    UserService userService;
    @Autowired
    CourseMasterDao courseMasterDao;
    @Autowired
    ExperimentMasterMapper experimentMasterMapper;


    /**
     * 获取课程的基本信息 --隶属学生
     * @param courseId
     * @return
     */
    @Override
    public CourseDTO info(Integer courseId) {
        CourseMaster courseMaster = courseMasterMapper.selectByPrimaryKey(courseId);
        if(courseMaster==null){
            log.info("[CourseServiceImpl]-获取课程基本信息,课程主表不存在,courseId={}",courseId);
            throw new TeachingException(ResultEnum.COURSE_NOT_EXIST);
        }
        CourseDTO courseDTO = new CourseDTO();
        BeanUtils.copyProperties(courseMaster,courseDTO);
        return courseDTO;
    }

    /**
     * 新增或修改课程 --隶属老师端
     * @param courseDTO
     * @return
     */
    @Override
    @Transactional
    public CourseDTO save(CourseDTO courseDTO) {
        CourseMaster courseMaster = new CourseMaster();
        CourseDetail courseDetail = new CourseDetail();
        // 先对courseDetail进行新增/更新
        BeanUtils.copyProperties(courseDTO,courseDetail);
        if (courseDetail.getCourseDetailId()==null){
            int insert = courseDetailMapper.insert(courseDetail);
            if (insert<=0){
                log.error("[CourseServiceImpl]-保存课表,保存课程详情失败,courseDetail={}",courseDetail);
                throw new TeachingException(ResultEnum.COURSE_SAVE_ERROR);
            }
        }else{
            int i = courseDetailMapper.updateByPrimaryKeySelective(courseDetail);
            if (i<=0){
                log.error("[CourseServiceImpl]-保存课表,保存课程详情失败,courseDetail={}",courseDetail);
                throw new TeachingException(ResultEnum.COURSE_SAVE_ERROR);
            }
        }
        //回填CourseDetailId
        courseDTO.setCourseDetailId(courseDetail.getCourseDetailId());
        BeanUtils.copyProperties(courseDTO,courseMaster);
        if (courseMaster.getCourseId()==null){
            Integer i = courseMasterMapper.insert(courseMaster);
            if (i<=0){
                log.error("[CourseServiceImpl]-保存课表,保存课程主表失败,courseMaster={}",courseMaster);
                throw new TeachingException(ResultEnum.COURSE_SAVE_ERROR);
            }
        }else{
            int i = courseMasterMapper.updateByPrimaryKeySelective(courseMaster);
            if (i<=0){
                log.error("[CourseServiceImpl]-保存课表,保存课程主表失败,courseMaster={}",courseMaster);
                throw new TeachingException(ResultEnum.COURSE_SAVE_ERROR);
            }
        }
        //回填setCourseId
        courseDTO.setCourseId(courseMaster.getCourseId());
        return courseDTO;
    }

    /**
     * 通过老师UserID获取课程信息 不包含info信息 ---隶属教师端
     * @param userId
     * @return
     */
    @Override
    public List<CourseDTO> getCourseByUserId(Integer userId) {
        CourseMasterExample courseMasterExample = new CourseMasterExample();
        courseMasterExample.createCriteria().andTeacherIdEqualTo(userId);
        List<CourseMaster> courseMasters = courseMasterMapper.selectByExample(courseMasterExample);
        List<CourseDTO> courseDTOList = courseMasters.stream().map(courseMaster -> {
            CourseDTO courseDTO = new CourseDTO();
            BeanUtils.copyProperties(courseMaster, courseDTO);
            return courseDTO;
        }).collect(Collectors.toList());
        return courseDTOList;
    }

    /**
     *  获取课程详细信息 通用
     * @param courseId
     * @return
     */
    @Override
    public CourseDTO detail(Integer courseId) {
        CourseMaster courseMaster = courseMasterMapper.selectByPrimaryKey(courseId);
        if (courseMaster==null){
            log.info("[CourseServiceImpl]-查询课程详情,课程主表不存在,courseId={}",courseId);
            throw new TeachingException(ResultEnum.COURSE_NOT_EXIST);
        }
        CourseDTO courseDTO = new CourseDTO();
        BeanUtils.copyProperties(courseMaster,courseDTO);
        User user = userMapper.selectByPrimaryKey(courseMaster.getTeacherId());
        if (user==null){
            log.info("[CourseServiceImpl]-查询课程课程详情 用户ID错误,UserId={}",courseMaster.getTeacherId());
            throw new TeachingException(ResultEnum.USER_NOT_EXIST);
        }
        courseDTO.setTeacherNickname(user.getNickname());
        courseDTO.setTeacherId(user.getUserId());
        //查询课程详情表
        CourseDetail courseDetail = courseDetailMapper.selectByPrimaryKey(courseMaster.getCourseDetailId());
        if (courseDetail==null){
            log.error("[CourseServiceImpl]-查询课程 该课程详情不存在,courseDetailId={}",courseMaster.getCourseDetailId());
            throw new TeachingException(ResultEnum.COURSE_DETAIL_NOT_EXIST);
        }
        BeanUtils.copyProperties(courseDetail,courseDTO);
        return courseDTO;
    }


    /**
     * 注销课程操作 ----隶属老师端
     * @param courseId
     * @return
     */
    @Override
    public boolean invalid(Integer courseId) {
        CourseMaster courseMaster =
                courseMasterMapper.selectByPrimaryKey(courseId);
        if (courseMaster == null) {
            log.info("注销课程,该课程不存在或已被删除");
            throw new TeachingException(ResultEnum.COURSE_NOT_EXIST);
        }
        courseMaster.setCourseStatus(CourseStatusEnum.INVALID.getCode().byteValue());
        if (courseMasterMapper.updateByPrimaryKeySelective(courseMaster)!=1){
            log.info("注销课程,课程注销失败");
            throw new TeachingException(ResultEnum.COURSE_INVALID_ERROR);
        }
        return true;
    }

    /**
     * 解锁课程操作 ----隶属老师端
     * @param courseId
     * @return
     */
    @Override
    public boolean unlock(Integer courseId) {
        CourseMaster courseMaster =
                courseMasterMapper.selectByPrimaryKey(courseId);
        if (courseMaster == null) {
            log.info("解锁课程,该课程不存在");
            throw new TeachingException(ResultEnum.COURSE_NOT_EXIST);
        }
        if (courseMaster.getCourseStatus().intValue()!=(CourseStatusEnum.LOCK.getCode())){
            log.info("解锁课程,该课程状态异常");
            throw new TeachingException(ResultEnum.COURSE_STATUS_ERROR);
        }
        //更新
        courseMaster.setCourseStatus(CourseStatusEnum.NORMAL.getCode().byteValue());

        if (courseMasterMapper.updateByPrimaryKeySelective(courseMaster)!=1){
            log.info("解锁课程,该课程解锁失败");
            throw new TeachingException(ResultEnum.COURSE_RESTORE_ERROR);
        }
        return true;
    }


    @Override
    public boolean lock(Integer courseId) {
        CourseMaster courseMaster =
                courseMasterMapper.selectByPrimaryKey(courseId);
        if (courseMaster == null) {
            log.info("锁定课程,该课程不存在");
            throw new TeachingException(ResultEnum.COURSE_NOT_EXIST);
        }
        if (courseMaster.getCourseStatus().intValue()!=(CourseStatusEnum.NORMAL.getCode())){
            log.info("锁定课程,该课程状态异常");
            throw new TeachingException(ResultEnum.COURSE_STATUS_ERROR);
        }
        //更新
        courseMaster.setCourseStatus(CourseStatusEnum.LOCK.getCode().byteValue());

        if (courseMasterMapper.updateByPrimaryKeySelective(courseMaster)!=1){
            log.info("锁定课程,课程锁定失败");
            throw new TeachingException(ResultEnum.COURSE_RESTORE_ERROR);
        }
        return true;
    }

    @Override
    public boolean end(Integer courseId) {
        CourseMaster courseMaster =
                courseMasterMapper.selectByPrimaryKey(courseId);
        if (courseMaster == null) {
            log.info("完结课程,该课程不存在");
            throw new TeachingException(ResultEnum.COURSE_NOT_EXIST);
        }
        if (courseMaster.getCourseStatus().intValue()!=(CourseStatusEnum.NORMAL.getCode())
                &&courseMaster.getCourseStatus().intValue()!=(CourseStatusEnum.LOCK.getCode())){
            log.info("完结课程,该课程状态异常");
            throw new TeachingException(ResultEnum.COURSE_STATUS_ERROR);
        }
        //更新
        courseMaster.setCourseStatus(CourseStatusEnum.END.getCode().byteValue());
        if ( courseMasterMapper.updateByPrimaryKeySelective(courseMaster)!=1){
            log.info("完结课程,课程锁定失败");
            throw new TeachingException(ResultEnum.COURSE_RESTORE_ERROR);
        }
        return true;
    }

    /**
     * 给学生用的课程list
     * @param userId
     * @return
     */
    @Override
    public List<CourseDTO> list(Integer userId) {
        //查询课程主表记录
        AchievementExample achievementExample = new AchievementExample();
        achievementExample.createCriteria().andUserIdEqualTo(userId);
        List<Achievement> achievements = achievementMapper.selectByExample(achievementExample);
        if(achievements.size()==0){
            //说明该学生无报任何班级
            return null;
        }
        //获取选修的班级ID
        List<Integer> courseIds = achievements.stream().map(achievement -> {
            Integer courseId = achievement.getCourseId();
            return courseId;
        }).collect(Collectors.toList());
        //获取选修的班级主表ID
        CourseMasterExample courseMasterExample = new CourseMasterExample();
        courseMasterExample.createCriteria().andCourseIdIn(courseIds);
        List<CourseMaster> courseMasterList = courseMasterMapper.selectByExample(courseMasterExample);
        if (courseMasterList==null){
            throw new TeachingException(ResultEnum.COURSE_NOT_EXIST);
        }
        List<Integer> ids = courseMasterList.stream().map(courseMaster -> {
            Integer teacherId = courseMaster.getTeacherId();
            return teacherId;
        }).collect(Collectors.toList());
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserIdIn(ids);
        List<User> teacherList = userMapper.selectByExample(userExample);
        //获取到老师的信息
        List<CourseDTO> result = courseMasterList.stream().map(courseMaster -> {
            CourseDTO courseDTO = new CourseDTO();
            List<User> teacher = teacherList.stream().filter(userDTO -> userDTO.getUserId().equals(courseMaster.getTeacherId())).collect(Collectors.toList());
            if (teacher.size()>0) {
                courseDTO.setTeacherNickname(teacher.get(0).getNickname());
            }
            courseDTO.setCourseName(courseMaster.getCourseName());
            courseDTO.setCourseId(courseMaster.getCourseId());
            courseDTO.setCourseStatus(courseMaster.getCourseStatus());
            return courseDTO;
        }).collect(Collectors.toList());
        return result;
    }


    @Override
    public List<CourseDTO> listCourse(Integer userId) {
        //查询课程主表记录
        CourseMasterExample courseMasterExample = new CourseMasterExample();
        courseMasterExample.createCriteria().andTeacherIdEqualTo(userId);
        List<CourseMaster> courseMasters = courseMasterMapper.selectByExample(courseMasterExample);
        if(courseMasters.size()==0){
            //说明该教师暂无授课
            return null;
        }
        List<Integer> ids = courseMasters.stream().map(courseMaster -> {
            Integer teacherId = courseMaster.getTeacherId();
            return teacherId;
        }).collect(Collectors.toList());
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserIdIn(ids);
        List<User> teacherList = userMapper.selectByExample(userExample);
        //获取到老师的信息
        List<CourseDTO> result = courseMasters.stream().map(courseMaster -> {
            CourseDTO courseDTO = new CourseDTO();
            List<User> teacher = teacherList.stream().filter(userDTO -> userDTO.getUserId().equals(courseMaster.getTeacherId())).collect(Collectors.toList());
            if (teacher.size()>0) {
                courseDTO.setTeacherNickname(teacher.get(0).getNickname());
                courseDTO.setCourseName(courseMaster.getCourseName());
                courseDTO.setCourseId(courseMaster.getCourseId());
                courseDTO.setCourseStatus(courseMaster.getCourseStatus());
            }
            return courseDTO;
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    @Transactional
    @Async
    public void updateNumber(Integer courseId) {
        //获取人数
        AchievementExample example = new AchievementExample();
        example.createCriteria().andCourseIdEqualTo(courseId);
        int number = achievementMapper.countByExample(example);

        //更新人数
        CourseMaster courseMaster = new CourseMaster();
        courseMaster.setCourseId(courseId);
        courseMaster.setCourseNumber(number);
        courseMasterMapper.updateByPrimaryKeySelective(courseMaster);

        //更新到各个实验
        ExperimentMasterExample experimentMasterExample = new ExperimentMasterExample();
        experimentMasterExample.createCriteria().andCourseIdEqualTo(courseId);
        ExperimentMaster experimentMaster = new ExperimentMaster();
        experimentMaster.setExperimentParticipationNum(number);
        experimentMasterMapper.updateByExampleSelective(experimentMaster,experimentMasterExample);
    }
}
