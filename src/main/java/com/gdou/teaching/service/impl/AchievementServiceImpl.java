package com.gdou.teaching.service.impl;

import com.gdou.teaching.Enum.RecordStatusEnum;
import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.Enum.UserIdentEnum;
import com.gdou.teaching.Enum.UserStatusEnum;
import com.gdou.teaching.dao.AchievementDao;
import com.gdou.teaching.dto.AchievementDTO;
import com.gdou.teaching.dto.CourseDTO;
import com.gdou.teaching.dto.ExperimentDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.mbg.mapper.*;
import com.gdou.teaching.mbg.model.*;
import com.gdou.teaching.mbg.model.Class;
import com.gdou.teaching.service.AchievementService;
import com.gdou.teaching.service.CourseService;
import com.gdou.teaching.service.ExperimentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.service.impl
 * @ClassName: AchievementServiceImpl
 * @Author: carrymaniac
 * @Description: Achievement实现类
 * @Date: 2019/12/3 5:25 下午
 * @Version:
 */
@Service
@Slf4j
public class AchievementServiceImpl implements AchievementService {
    private final UserMapper userMapper;
    private final CourseMasterMapper courseMasterMapper;
    private final AchievementDao achievementDao;
    private final AchievementMapper achievementMapper;
    private final ClassMapper classMapper;
    private final ExperimentMasterMapper experimentMasterMapper;
    private final UserReExperimentMapper userReExperimentMapper;
    private final ExperimentService experimentService;

    public AchievementServiceImpl(UserMapper userMapper, CourseMasterMapper courseMasterMapper, AchievementDao achievementDao, AchievementMapper achievementMapper, ClassMapper classMapper, ExperimentMasterMapper experimentMasterMapper, UserReExperimentMapper userReExperimentMapper, ExperimentService experimentService) {
        this.userMapper = userMapper;
        this.courseMasterMapper = courseMasterMapper;
        this.achievementDao = achievementDao;
        this.achievementMapper = achievementMapper;
        this.classMapper = classMapper;
        this.experimentMasterMapper = experimentMasterMapper;
        this.userReExperimentMapper = userReExperimentMapper;
        this.experimentService = experimentService;
    }

    @Override
    public boolean addAchievementByStudentList(Integer courseId, List<Integer> studentIdList) {
        AchievementExample achievementExample = new AchievementExample();
        achievementExample.createCriteria().andCourseIdEqualTo(courseId).andUserIdIn(studentIdList);
        List<Achievement> exist = achievementMapper.selectByExample(achievementExample);
        if(exist!=null&&!exist.isEmpty()){
            StringBuilder sb = new StringBuilder();
            sb.append("学生 ");
            for(Achievement a :exist){
                sb.append(a.getUserName());
                sb.append("、");
            }
            sb.deleteCharAt(sb.length()-1);
            sb.append("已存在,请检查");
            throw new TeachingException(ResultEnum.PARAM_ERROR.getCode(),sb.toString());
        }
        UserExample userExample = new UserExample();
        userExample.createCriteria().andUserIdIn(studentIdList).andUserIdentEqualTo(UserIdentEnum.SUTUDENT.getCode().byteValue());
        List<User> users = userMapper.selectByExample(userExample);
        if(users==null||users.isEmpty()){
            log.info("[AchievementServiceImpl]-新增Achievement,用户信息不存在,studentIdList:{}",studentIdList);
            throw new TeachingException(ResultEnum.USER_NOT_EXIST);
        }
        CourseMaster courseMaster = courseMasterMapper.selectByPrimaryKey(courseId);
        if(courseMaster==null){
            log.info("[AchievementServiceImpl]-新增Achievement,课程主表不存在,courseID为:{}",courseId);
            throw new TeachingException(ResultEnum.COURSE_NOT_EXIST);
        }
        List<Achievement> achievements = users.stream().map(user -> {
            Achievement achievement = new Achievement();
            achievement.setCourseId(courseId);
            achievement.setUserId(user.getUserId());
            achievement.setClassId(user.getClassId());
            achievement.setUserName(user.getNickname());
            achievement.setCourseName(courseMaster.getCourseName());
            achievement.setTeacherId(courseMaster.getTeacherId());
            achievement.setCourseAchievement((double) 0);
            return achievement;
        }).collect(Collectors.toList());
        return achievementDao.insertList(achievements)==achievements.size();
    }

    @Override
    public boolean deleteAchievementByStudentList(Integer courseId, List<Integer> studentIdList) {
        AchievementExample achievementExample = new AchievementExample();
        achievementExample.createCriteria().andCourseIdEqualTo(courseId).andUserIdIn(studentIdList);
        return achievementMapper.deleteByExample(achievementExample)==studentIdList.size();
    }

    @Override
    @Async
    public Integer deleteAchievementByCourseId(Integer courseId) {
        AchievementExample achievementExample = new AchievementExample();
        achievementExample.createCriteria().andCourseIdEqualTo(courseId);
        return achievementMapper.deleteByExample(achievementExample);
    }

    @Override
    public Integer getCourseNumberByUserId(Integer userId) {
        AchievementExample achievementExample = new AchievementExample();
        achievementExample.createCriteria().andUserIdEqualTo(userId);
        return achievementMapper.countByExample(achievementExample);
    }

    @Override
    public Achievement getAchievementByUserIdAndCourseId(Integer userId, Integer courseId) {
        AchievementExample achievementExample = new AchievementExample();
        achievementExample.createCriteria().andUserIdEqualTo(userId).andCourseIdEqualTo(courseId);
        List<Achievement> achievements = achievementMapper.selectByExample(achievementExample);
        if(achievements==null||achievements.isEmpty()){
            log.info("[AchievementServiceImpl]-getAchievementByUserIdAndCourseId,成绩表信息不存在,userId:{},courseId:{}",userId,courseId);
            throw new TeachingException(ResultEnum.ACHIEVEMENT_NOT_EXIST);
        }
        return achievements.get(0);
    }

    @Override
    public List<AchievementDTO> getAchievementByCourseId(Integer courseId) {
        AchievementExample achievementExample = new AchievementExample();
        achievementExample.createCriteria().andCourseIdEqualTo(courseId);
        List<Achievement> achievements = achievementMapper.selectByExample(achievementExample);
        if (achievements==null||achievements.isEmpty()){
            log.info("[AchievementServiceImpl]-getAchievementByCourseId,成绩表信息不存在,courseId:{}",courseId);
            return null;
        }
        //获取老师名字
        Integer teacherId = achievements.get(0).getTeacherId();
        User teacher = userMapper.selectByPrimaryKey(teacherId);
        if (teacher==null){
            log.info("[AchievementServiceImpl]-getAchievementByCourseId,教师信息不存在,teacherId:{}",teacherId);
            throw new TeachingException(ResultEnum.USER_NOT_EXIST);
        }
        String teacherName = teacher.getNickname();
        //获取班级信息
        HashSet<Integer> classIds = new HashSet<>();
        achievements.stream().forEach(achievement -> {
            classIds.add(achievement.getClassId());
        });
        List<Integer> classIdList = new ArrayList<>(classIds);
        ClassExample classExample = new ClassExample();
        classExample.createCriteria().andClassIdIn(classIdList);
        List<Class> classes = classMapper.selectByExample(classExample);
        if (classes==null||classes.isEmpty()){
            log.info("[AchievementServiceImpl]-getAchievementByCourseId,班级信息不存在,classIdList:{}",classIdList);
            throw new TeachingException(ResultEnum.CLASS_NOT_EXIST);
        }
        HashMap<Integer,String> classMap = new HashMap<Integer, String>(classes.size()){
            {
                classes.forEach(aClass -> {
                    put(aClass.getClassId(),aClass.getClassName());
                });
            }
        };
        //汇总信息成AchievementDTO
        List<AchievementDTO> result = achievements.stream().map(achievement -> {
            AchievementDTO achievementDTO = new AchievementDTO();
            BeanUtils.copyProperties(achievement, achievementDTO);
            achievementDTO.setClassName(classMap.get(achievement.getClassId()));
            achievementDTO.setTeacherName(teacherName);
            return achievementDTO;
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public void updateAchievement(Integer courseId, Integer userId) {
        //获取实验列表
        ExperimentMasterExample example = new ExperimentMasterExample();
        example.createCriteria().andCourseIdEqualTo(courseId);
        List<ExperimentMaster> experimentMasterList = experimentMasterMapper.selectByExample(example);
        if (experimentMasterList==null||experimentMasterList.isEmpty()){
            log.info("[AchievementServiceImpl]-updateAchievement,实验主表信息不存在,courseId:{}",courseId);
            throw new TeachingException(ResultEnum.EXPERIMENT_NOT_EXIST);
        }
        UserReExperimentExample userReExperimentExample = new UserReExperimentExample();
        List<UserReExperiment> recordList = experimentMasterList.stream().map(experimentMaster -> {
            userReExperimentExample.clear();
            userReExperimentExample.createCriteria().andUserIdEqualTo(userId).andExperimentIdEqualTo(experimentMaster.getExperimentId());
            List<UserReExperiment> userReExperiments = userReExperimentMapper.selectByExample(userReExperimentExample);
            if (userReExperiments != null && !userReExperiments.isEmpty()) {
                UserReExperiment userReExperiment = userReExperiments.get(0);
                if (RecordStatusEnum.PASS.getCode().byteValue() == userReExperiment.getStatus()) {
                    return userReExperiment;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }).collect(Collectors.toList());

        //通过审核的实验个数
        int num=0;
        Double result=0.0;
        for(int i=0,size= recordList.size();i<size;i++){
            if(recordList.get(i)!=null){
                result+=recordList.get(i).getExperimentAchievement();
                num++;
            }
        }
        //通过审核的实验个数不为零,更新课程成绩
        AchievementExample achievementExample = new AchievementExample();
        achievementExample.createCriteria().andCourseIdEqualTo(courseId).andUserIdEqualTo(userId);
        Achievement achievement = new Achievement();
        if (num!=0){
            result=result/num;
            achievement.setCourseAchievement(result);
        }else { //通过审核的实验个数为零,课程成绩置为0
            achievement.setCourseAchievement(0.0);
        }
        achievementMapper.updateByExampleSelective(achievement,achievementExample);
    }


    @Override
    public List<List<String>> exportAchievement(CourseDTO courseDTO) {
        List<AchievementDTO> achievementList = getAchievementByCourseId(courseDTO.getCourseId());
        if (achievementList==null||achievementList.isEmpty()){
            log.info("[AchievementServiceImpl]-exportAchievement,成绩表信息不存在,courseId:{}",courseDTO.getCourseId());
            throw new TeachingException(ResultEnum.ACHIEVEMENT_NOT_EXIST);
        }
        List<ExperimentDTO> experimentDTOList = experimentService.list(courseDTO.getCourseId());
        //获取实验列表
        List<Integer> experimentIdList;
        if (experimentDTOList==null||experimentDTOList.isEmpty()){
            log.info("[AchievementServiceImpl]-exportAchievement,实验信息不存在,courseId:{}",courseDTO.getCourseId());
            experimentIdList=new ArrayList<>();
        }else{
            experimentIdList=experimentDTOList.stream().map(experimentDTO -> {
                return experimentDTO.getExperimentId();
            }).collect(Collectors.toList());
        }

        HashMap<Integer,String> classMap=new HashMap<>();
        List<List<String>> collect=achievementList.stream().map(achievement -> {
            List<String> list = new ArrayList<>();
            Integer classId = achievement.getClassId();
            if (classMap.containsKey(classId)){
                list.add(classMap.get(classId));
            }else{
                Class aClass = classMapper.selectByPrimaryKey(classId);
                classMap.put(classId,aClass.getClassName());
                list.add(aClass.getClassName());
            }
            User user = userMapper.selectByPrimaryKey(achievement.getUserId());
            list.add(user.getUserNumber());
            list.add(user.getNickname());
            list.add(achievement.getCourseAchievement().toString());
            UserReExperimentExample userReExperimentExample = new UserReExperimentExample();
            for (int experimentId:experimentIdList){
                userReExperimentExample.clear();
                userReExperimentExample.createCriteria().andExperimentIdEqualTo(experimentId).andUserIdEqualTo(user.getUserId());
                List<UserReExperiment> userReExperiments = userReExperimentMapper.selectByExample(userReExperimentExample);
                if (userReExperiments==null||userReExperiments.isEmpty()){
                    list.add("0");
                }else{
                    list.add(userReExperiments.get(0).getExperimentAchievement().toString());
                }
            }
            return list;
        }).collect(Collectors.toList());
        return collect;
    }
}
