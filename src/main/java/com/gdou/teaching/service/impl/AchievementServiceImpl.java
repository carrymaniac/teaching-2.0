package com.gdou.teaching.service.impl;

import com.gdou.teaching.Enum.RecordStatusEnum;
import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.Enum.UserIdentEnum;
import com.gdou.teaching.Enum.UserStatusEnum;
import com.gdou.teaching.dao.AchievementDao;
import com.gdou.teaching.dto.AchievementDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.mbg.mapper.*;
import com.gdou.teaching.mbg.model.*;
import com.gdou.teaching.mbg.model.Class;
import com.gdou.teaching.service.AchievementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserMapper userMapper;
    @Autowired
    CourseMasterMapper courseMasterMapper;
    @Autowired
    AchievementDao achievementDao;
    @Autowired
    AchievementMapper achievementMapper;
    @Autowired
    ClassMapper classMapper;
    @Autowired
    ExperimentMasterMapper experimentMasterMapper;
    @Autowired
    UserReExperimentMapper userReExperimentMapper;
    @Override
    public boolean addAchievementByClazzId(Integer courseId, Integer clazzId) {
        return false;
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
        List<User> users = userMapper.selectByExample(userExample);
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
            return null;
        }
        return achievements.get(0);
    }

    @Override
    public List<AchievementDTO> getAchievementByCourseId(Integer courseId) {

        AchievementExample achievementExample = new AchievementExample();
        achievementExample.createCriteria().andCourseIdEqualTo(courseId);
        List<Achievement> achievements = achievementMapper.selectByExample(achievementExample);
        //获取老师名字
        Integer teacherId = achievements.get(0).getTeacherId();
        String teacherName = userMapper.selectByPrimaryKey(teacherId).getNickname();
        //获取班级信息
        HashSet<Integer> classIds = new HashSet<>();
        achievements.stream().forEach(achievement -> {
            classIds.add(achievement.getClassId());
        });
        List<Integer> classIdList = new ArrayList<>(classIds);
        ClassExample classExample = new ClassExample();
        classExample.createCriteria().andClassIdIn(classIdList);
        List<Class> classes = classMapper.selectByExample(classExample);
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
        for(int i=0;i<recordList.size();i++){
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
}
