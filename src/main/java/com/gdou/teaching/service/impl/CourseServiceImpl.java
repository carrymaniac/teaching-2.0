package com.gdou.teaching.service.impl;

import com.gdou.teaching.Enum.CourseStatusEnum;
import com.gdou.teaching.Enum.FileCategoryEnum;
import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.dao.CourseMasterDao;
import com.gdou.teaching.dto.CourseDTO;
import com.gdou.teaching.dto.FileDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.mbg.mapper.*;
import com.gdou.teaching.mbg.model.*;
import com.gdou.teaching.service.CourseService;
import com.gdou.teaching.service.FileService;
import com.gdou.teaching.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author carrymaniac
 */
@Service
@Slf4j
public class CourseServiceImpl implements CourseService {
    private final CourseMasterMapper courseMasterMapper;
    private final UserMapper userMapper;
    private final CourseDetailMapper courseDetailMapper;
    private final AchievementMapper achievementMapper;
    private final UserService userService;
    private final CourseMasterDao courseMasterDao;
    private final ExperimentMasterMapper experimentMasterMapper;
    private final FileService fileService;

    public CourseServiceImpl(CourseMasterMapper courseMasterMapper, UserMapper userMapper, CourseDetailMapper courseDetailMapper, AchievementMapper achievementMapper, UserService userService, CourseMasterDao courseMasterDao, ExperimentMasterMapper experimentMasterMapper, FileService fileService) {
        this.courseMasterMapper = courseMasterMapper;
        this.userMapper = userMapper;
        this.courseDetailMapper = courseDetailMapper;
        this.achievementMapper = achievementMapper;
        this.userService = userService;
        this.courseMasterDao = courseMasterDao;
        this.experimentMasterMapper = experimentMasterMapper;
        this.fileService = fileService;
    }


    /**
     * 获取课程的基本信息 --隶属学生
     * @param courseId
     * @return
     */
    @Override
    public CourseDTO info(Integer courseId) throws TeachingException{
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
                log.error("[CourseServiceImpl-save]新增课程,新增课程详情表详情失败,courseDetail={}",courseDetail);
                throw new TeachingException(ResultEnum.COURSE_SAVE_ERROR);
            }
        }else{
            int i = courseDetailMapper.updateByPrimaryKeySelective(courseDetail);
            if (i<=0){
                log.error("[CourseServiceImpl-save]更新课程,更新课程详情表详情失败,courseDetail={}",courseDetail);
                throw new TeachingException(ResultEnum.COURSE_SAVE_ERROR);
            }
        }
        //回填CourseDetailId
        courseDTO.setCourseDetailId(courseDetail.getCourseDetailId());
        BeanUtils.copyProperties(courseDTO,courseMaster);
        if (courseMaster.getCourseId()==null){
            Integer i = courseMasterMapper.insert(courseMaster);
            if (i<=0){
                log.error("[CourseServiceImpl-save]新增课程,新增课程主表失败,courseMaster={}",courseMaster);
                throw new TeachingException(ResultEnum.COURSE_SAVE_ERROR);
            }
        }else{
            int i = courseMasterMapper.updateByPrimaryKeySelective(courseMaster);
            if (i<=0){
                log.error("[CourseServiceImpl-save]更新课程,更新课程主表失败,courseMaster={}",courseMaster);
                throw new TeachingException(ResultEnum.COURSE_SAVE_ERROR);
            }
        }
        //回填setCourseId
        courseDTO.setCourseId(courseMaster.getCourseId());
        return courseDTO;
    }

    /**
     *  获取课程详细信息 通用
     * @param courseId
     * @return
     */
    @Override
    public CourseDTO detail(Integer courseId) throws TeachingException{
        CourseMaster courseMaster = courseMasterMapper.selectByPrimaryKey(courseId);
        if (courseMaster==null){
            log.info("[CourseServiceImpl]-查询课程详情,课程主表不存在,courseId={}",courseId);
            throw new TeachingException(ResultEnum.COURSE_NOT_EXIST);
        }
        CourseDTO courseDTO = new CourseDTO();
        BeanUtils.copyProperties(courseMaster,courseDTO);
        User teacher = userMapper.selectByPrimaryKey(courseMaster.getTeacherId());
        if (teacher==null){
            log.info("[CourseServiceImpl]-查询课程课程详情 用户ID错误,UserId={}",courseMaster.getTeacherId());
        }else{
            courseDTO.setTeacherNickname(teacher.getNickname());
            courseDTO.setTeacherId(teacher.getUserId());
        }
        //查询课程详情表
        CourseDetail courseDetail = courseDetailMapper.selectByPrimaryKey(courseMaster.getCourseDetailId());
        if (courseDetail==null){
           return courseDTO;
        }
        BeanUtils.copyProperties(courseDetail,courseDTO);
        return courseDTO;
    }

    @Override
    public boolean addCourseFile(Integer courseId,List<FileDTO> courseFile) {
        if(courseFile!=null&&!courseFile.isEmpty()){
            fileService.saveFile(FileCategoryEnum.COURSE_FILE.getCode(),courseId,courseFile);
        }
        return true;
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
        if (courseMaster.getCourseStatus().intValue()!=(CourseStatusEnum.END.getCode())){
            log.info("注销课程,该课程状态异常");
            throw new TeachingException(ResultEnum.COURSE_STATUS_ERROR);
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
    public List<CourseDTO> listCourseByUserIdAndKeywordForStudent(Integer userId,String keyword) {
        //查询课程主表记录
        AchievementExample achievementExample = new AchievementExample();
        AchievementExample.Criteria criteria = achievementExample.createCriteria().andUserIdEqualTo(userId);
        if(!StringUtils.isEmpty(keyword)){
            criteria.andCourseNameLike("%"+keyword+"%");
        }
        List<Achievement> achievements = achievementMapper.selectByExample(achievementExample);
        if(achievements==null||achievements.isEmpty()){
            //说明该学生无报任何课程
            return null;
        }
        //获取选修的课程ID
        List<Integer> courseIds = achievements.stream().map(achievement -> {
            Integer courseId = achievement.getCourseId();
            return courseId;
        }).collect(Collectors.toList());
        //获取选修的课程主表ID
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
        //
        HashMap<Integer,User>teacherMap=new HashMap<Integer,User>(teacherList.size()){
            {
                teacherList.forEach(aTeacher->{
                    put(aTeacher.getUserId(),aTeacher);
                });
            }
        };
        //获取到老师的信息
        List<CourseDTO> result = courseMasterList.stream().map(courseMaster -> {
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setTeacherNickname(teacherMap.get(courseMaster.getTeacherId()).getNickname());
            courseDTO.setHeadUrl(teacherMap.get(courseMaster.getTeacherId()).getHeadUrl());
            courseDTO.setCourseName(courseMaster.getCourseName());
            courseDTO.setCourseId(courseMaster.getCourseId());
            courseDTO.setCourseStatus(courseMaster.getCourseStatus());
            courseDTO.setCourseCover(courseMaster.getCourseCover());
            return courseDTO;
        }).collect(Collectors.toList());
        return result;
    }


    @Override
    public List<CourseDTO> listCourseByUserIdAndKeywordForTeacher(Integer userId,String keyword) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user==null) {
            throw new TeachingException(ResultEnum.USER_NOT_EXIST);
        }
        //查询课程主表记录
        CourseMasterExample courseMasterExample = new CourseMasterExample();
        CourseMasterExample.Criteria criteria = courseMasterExample.createCriteria().andTeacherIdEqualTo(userId).andCourseStatusNotEqualTo(CourseStatusEnum.INVALID.getCode().byteValue());
        if(!StringUtils.isEmpty(keyword)){
            criteria.andCourseNameLike("%"+keyword+"%");
        }
        List<CourseMaster> courseMasters = courseMasterMapper.selectByExample(courseMasterExample);
        if(courseMasters==null||courseMasters.isEmpty()){
            //说明该教师暂无授课
            return null;
        }
        //拼装
        List<CourseDTO> result = courseMasters.stream().map(courseMaster -> {
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setTeacherNickname(user.getNickname());
            courseDTO.setHeadUrl(user.getHeadUrl());
            courseDTO.setCourseName(courseMaster.getCourseName());
            courseDTO.setCourseId(courseMaster.getCourseId());
            courseDTO.setCourseStatus(courseMaster.getCourseStatus());
            courseDTO.setCourseCover(courseMaster.getCourseCover());
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


    @Override
    public HashMap<String,Object> listCourseForAdminByTeacherIdAndKeywordInPage(Integer userId, Integer page, Integer size, String keyWord, Integer status) {
        CourseMasterExample courseMasterExample = new CourseMasterExample();
        CourseMasterExample.Criteria criteria = courseMasterExample.createCriteria().andTeacherIdEqualTo(userId).andCourseStatusNotEqualTo(CourseStatusEnum.INVALID.getCode().byteValue());
        if(!StringUtils.isEmpty(keyWord)){
            keyWord = "%"+keyWord+"%";
            criteria.andCourseNameLike(keyWord);
        }
        if(status!=null){
            criteria.andCourseStatusEqualTo(status.byteValue());
        }
        List<CourseDTO> collect = new ArrayList<>(0);
        HashMap<String,Object> result  = new HashMap<>(3);
        PageHelper.startPage(page,size);
        List<CourseMaster> courseMasters = courseMasterMapper.selectByExample(courseMasterExample);
        PageInfo<CourseMaster> pageInfo = new PageInfo(courseMasters);
        List<CourseMaster> list = pageInfo.getList();
        long total = pageInfo.getTotal();
        if(list==null||list.isEmpty()){
            //说明该教师暂无授课或分页最末
            result.put("list",collect);
            result.put("total",total);
            return result;
        }
            collect = list.stream().map(courseMaster -> {
            CourseDetail courseDetail = courseDetailMapper.selectByPrimaryKey(courseMaster.getCourseDetailId());
            CourseDTO courseDTO = new CourseDTO();
            BeanUtils.copyProperties(courseMaster, courseDTO);
            BeanUtils.copyProperties(courseDetail, courseDTO);
            return courseDTO;
        }).collect(Collectors.toList());
        result.put("list",collect);
        result.put("total",total);
        return result;
    }

    @Override
    public HashMap listCourseForAdminByStudentIdAndKeywordAndStatusInPage(Integer page, Integer size,Integer userId,String keyword,Integer status) {
        HashMap result = new HashMap(3);
        List<CourseDTO> collect = new ArrayList<>(0);
        AchievementExample achievementExample = new AchievementExample();
        AchievementExample.Criteria criteria = achievementExample.createCriteria().andUserIdEqualTo(userId);
        if(!StringUtils.isEmpty(keyword)){
            criteria.andCourseNameLike("%"+keyword+"%");
        }
        List<Achievement> achievements = achievementMapper.selectByExample(achievementExample);
        if(achievements==null||achievements.isEmpty()){
            //说明该学生无报任何课程
            //返回total=0，list=null的map
            result.put("total",0);
            result.put("list",collect);
            return result;
        }
        Map<Integer, Double> map = achievements.stream().collect(Collectors.toMap(Achievement::getCourseId, Achievement::getCourseAchievement));
        List<Integer> courseIds = achievements.stream().map(achievement -> {
            Integer courseId = achievement.getCourseId();
            return courseId;
        }).collect(Collectors.toList());

        //拼装数据
        //获取选修的课程主表ID
        //这里做分页
        CourseMasterExample courseMasterExample = new CourseMasterExample();
        CourseMasterExample.Criteria criteria1 = courseMasterExample.createCriteria().andCourseIdIn(courseIds);
        if(status!=null){
            criteria1.andCourseStatusEqualTo(status.byteValue());
        }
        PageHelper.startPage(page,size);
        List<CourseMaster> courseMasterList = courseMasterMapper.selectByExample(courseMasterExample);
        PageInfo<CourseMaster> pageInfo = new PageInfo<>(courseMasterList);
        long total = pageInfo.getTotal();
        courseMasterList = pageInfo.getList();
        collect = courseMasterList.stream().map(courseMaster -> {
            CourseDetail courseDetail = courseDetailMapper.selectByPrimaryKey(courseMaster.getCourseDetailId());
            CourseDTO courseDTO = new CourseDTO();
            BeanUtils.copyProperties(courseMaster, courseDTO);
            if(courseDetail.getCourseIntroduction().length()<=15){
                courseDTO.setCourseIntroduction(courseDetail.getCourseIntroduction());
            }else {
                courseDTO.setCourseIntroduction(courseDetail.getCourseIntroduction().substring(0, 15));
            }
            courseDTO.setAchievement(map.get(courseMaster.getCourseId()));
            return courseDTO;
        }).collect(Collectors.toList());
        result.put("total",total);
        result.put("list",collect);
        return result;
    }

}


