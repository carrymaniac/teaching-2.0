package com.gdou.teaching.service.impl;

import com.gdou.teaching.Enum.ExperimentStatusEnum;
import com.gdou.teaching.Enum.FileCategoryEnum;
import com.gdou.teaching.Enum.RecordStatusEnum;
import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.dao.UserReExperimentDao;
import com.gdou.teaching.dto.FileDTO;
import com.gdou.teaching.dto.RecordDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.mbg.mapper.ExperimentMasterMapper;
import com.gdou.teaching.mbg.mapper.UserReExperimentMapper;
import com.gdou.teaching.mbg.model.ExperimentMaster;
import com.gdou.teaching.mbg.model.ExperimentMasterExample;
import com.gdou.teaching.mbg.model.UserReExperiment;
import com.gdou.teaching.mbg.model.UserReExperimentExample;
import com.gdou.teaching.service.AchievementService;
import com.gdou.teaching.service.FileService;
import com.gdou.teaching.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.service.impl
 * @ClassName: RecordServiceImpl
 * @Author: carrymaniac
 * @Description: 实验提交记录实现类
 * @Date: 2019/12/7 10:54 上午
 * @Version:
 */
@Slf4j
@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    private ExperimentMasterMapper experimentMasterMapper;
    @Autowired
    private UserReExperimentMapper userReExperimentMapper;
    @Autowired
    UserReExperimentDao userReExperimentDao;
    @Autowired
    FileService fileService;
    @Autowired
    AchievementService achievementService;
    @Override
    public RecordDTO save(RecordDTO recordDTO) {
        //查询该实验的状态。
        ExperimentMaster experiment = experimentMasterMapper.selectByPrimaryKey(recordDTO.getExperimentId());
        if (ExperimentStatusEnum.NORMAL.getCode().byteValue()!=experiment.getExperimentStatus()){
            log.error("保存纪录,保存实验记录失败,当前实验不可提交,ExperimentStatus={}",experiment.getExperimentStatus());
            throw new TeachingException(ResultEnum.EXPERIMENT_STATUS_ERROR);
        }
        recordDTO.setExperimentName(experiment.getExperimentName());
        //准备变量
        List<FileDTO> fileDTOList = recordDTO.getUserExperimentFile();
        UserReExperiment userReExperiment = new UserReExperiment();
        BeanUtils.copyProperties(recordDTO,userReExperiment);
        userReExperiment.setExperimentAchievement((double)0);
        userReExperiment.setStatus(RecordStatusEnum.REVIEWING.getCode().byteValue());

        //查询用户的上一次提交
        UserReExperimentExample userReExperimentExample = new UserReExperimentExample();
        userReExperimentExample.createCriteria().andUserIdEqualTo(recordDTO.getUserId()).andExperimentIdEqualTo(recordDTO.getExperimentId());
        List<UserReExperiment> userReExperiments = userReExperimentMapper.selectByExample(userReExperimentExample);
        //上次提交过
        if(userReExperiments!=null&&!userReExperiments.isEmpty()){

            Byte preStatus = userReExperiments.get(0).getStatus();
            //检查状态
            if(preStatus.equals(RecordStatusEnum.PASS.getCode().byteValue())||preStatus.equals(RecordStatusEnum.LOCK.getCode().byteValue())){
                //如果已经通过审核或者是实验被锁定，无法再次提交
                log.error("保存实验记录失败,已有实验提交记录被审核通过或实验已被锁定,preStatus={}",preStatus);
                throw new TeachingException(ResultEnum.SUBMIT_RECORD_ERROR);
            }else{
                Integer userExperimentId = userReExperiments.get(0).getUserExperimentId();
                //更新，先删除之前所有的文件记录，进行重新插入
                fileService.deleteFiles(FileCategoryEnum.RECORD_FILE.getCode(),userExperimentId);
                if(fileDTOList!=null&&!fileDTOList.isEmpty()){
                    fileService.saveFile(FileCategoryEnum.RECORD_FILE.getCode(),userExperimentId,recordDTO.getUserExperimentFile());
                }
                userReExperiment.setUserExperimentId(userReExperiments.get(0).getUserExperimentId());
                userReExperimentMapper.updateByPrimaryKeySelective(userReExperiment);
            }
        }

        //进行新增存储记录
        int insert = userReExperimentMapper.insert(userReExperiment);
        if (insert<=0){
            log.error("保存记录,保存实验记录失败,record={}",userReExperiment);
            throw new TeachingException(ResultEnum.SUBMIT_RECORD_ERROR);
        }
        //存储文件
        if(fileDTOList!=null&&!fileDTOList.isEmpty()) {
            fileService.saveFile(FileCategoryEnum.RECORD_FILE.getCode(), userReExperiment.getUserExperimentId(), fileDTOList);
        }
        recordDTO.setUserExperimentId(userReExperiment.getUserExperimentId());
        return recordDTO;
    }

    @Override
    public RecordDTO selectOne(Integer experimentId, Integer userId) {
        UserReExperimentExample userReExperimentExample = new UserReExperimentExample();
        userReExperimentExample.setOrderByClause("create_time desc");
        userReExperimentExample.createCriteria().andUserIdEqualTo(userId).andExperimentIdEqualTo(experimentId);
        List<UserReExperiment> userReExperiments = userReExperimentMapper.selectByExampleWithBLOBs(userReExperimentExample);
        if(userReExperiments==null||userReExperiments.isEmpty()){
            log.error("[RecordServiceImpl]-selectOne,实验提交表信息不存在,experimentId={},userId={}",experimentId,userId);
            throw new TeachingException(ResultEnum.RECORD_NOT_EXIST);
        }
        UserReExperiment userReExperiment = userReExperiments.get(0);
        RecordDTO recordDTO = new RecordDTO();
        //查询用户提交时提交的文件记录
        List<FileDTO> fileDTOList = fileService.selectFileByCategoryAndFileCategoryId(FileCategoryEnum.RECORD_FILE.getCode(), userReExperiment.getUserExperimentId());
        recordDTO.setUserExperimentFile(fileDTOList);
        BeanUtils.copyProperties(userReExperiment,recordDTO);
        return recordDTO;
    }

    @Override
    public RecordDTO selectOne(Integer userExperimentId) {
        UserReExperiment userReExperiment = userReExperimentMapper.selectByPrimaryKey(userExperimentId);
        if(userReExperiment==null){
            log.error("[RecordServiceImpl]-selectOne,实验提交表信息不存在,userExperimentId={}",userExperimentId);
            throw new TeachingException(ResultEnum.RECORD_NOT_EXIST);
        }
        RecordDTO recordDTO = new RecordDTO();
        //查询用户提交时提交的文件记录
        List<FileDTO> fileDTOList = fileService.selectFileByCategoryAndFileCategoryId(FileCategoryEnum.RECORD_FILE.getCode(), userReExperiment.getUserExperimentId());
        recordDTO.setUserExperimentFile(fileDTOList);

        BeanUtils.copyProperties(userReExperiment,recordDTO);
        return recordDTO;
    }

    @Override
    public void updateExperimentCommitNumber(Integer experimentId) {
        Integer countByExperimentId = userReExperimentDao.getCountByExperimentId(experimentId);
        ExperimentMaster experimentMaster = new ExperimentMaster();
        experimentMaster.setExperimentCommitNum(countByExperimentId);
        experimentMaster.setExperimentId(experimentId);
        experimentMasterMapper.updateByPrimaryKeySelective(experimentMaster);
    }

    @Override
    public List<RecordDTO> getRecordByUserIdAndCourseId(Integer userId, Integer courseId) {
        //查询课程下属的实验ID
        ExperimentMasterExample experimentMasterExample = new ExperimentMasterExample();
        experimentMasterExample.createCriteria().andCourseIdEqualTo(courseId);
        List<ExperimentMaster> experimentMasters = experimentMasterMapper.selectByExample(experimentMasterExample);
        if (experimentMasters==null||experimentMasters.isEmpty()){
            log.error("[RecordServiceImpl]-getRecordByUserIdAndCourseId,实验主表信息不存在,courseId={}",courseId);
            throw new TeachingException(ResultEnum.EXPERIMENT_NOT_EXIST);
        }
        List<Integer> experimentIds = experimentMasters.stream().map(ExperimentMaster::getExperimentId).collect(Collectors.toList());

        //查询对应的实验提交记录（不带提交文本）
        UserReExperimentExample userReExperimentExample = new UserReExperimentExample();
        userReExperimentExample.createCriteria().andExperimentIdIn(experimentIds).andUserIdEqualTo(userId);
        List<UserReExperiment> userReExperiments = userReExperimentMapper.selectByExample(userReExperimentExample);
        if (userReExperiments==null||experimentMasters.isEmpty()){
            log.info("[RecordServiceImpl]-getRecordByUserIdAndCourseId,实验提交表信息不存在,experimentIds={},userId={}",experimentIds,userId);
            throw new TeachingException(ResultEnum.RECORD_NOT_EXIST);
        }
        List<RecordDTO> collect = userReExperiments.stream().map(userReExperiment -> {
            RecordDTO recordDTO = new RecordDTO();
            BeanUtils.copyProperties(userReExperiment, recordDTO);
            return recordDTO;
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public List<RecordDTO> getRecordListByExperimentId(Integer experimentId) {
        UserReExperimentExample userReExperimentExample = new UserReExperimentExample();
        userReExperimentExample.createCriteria().andExperimentIdEqualTo(experimentId);
        List<UserReExperiment> userReExperiments = userReExperimentMapper.selectByExample(userReExperimentExample);
        if (userReExperiments==null||userReExperiments.isEmpty()){
            log.info("[RecordServiceImpl]-getRecordListByExperimentId,实验提交表信息不存在,experimentId={}",experimentId);
            throw new TeachingException(ResultEnum.RECORD_NOT_EXIST);
        }
        List<RecordDTO> recordDTOList = userReExperiments.stream().map(record -> {
            RecordDTO recordDTO = new RecordDTO();
            BeanUtils.copyProperties(record, recordDTO);
            return recordDTO;
        }).collect(Collectors.toList());
        return recordDTOList;
    }

    @Override
    public void judge(RecordDTO recordDTO) {
        //判断是否提前看过答案
        ExperimentMaster experiment = experimentMasterMapper.selectByPrimaryKey(recordDTO.getExperimentId());
        if (experiment==null){
            log.info("[RecordServiceImpl]-judge,实验主表信息不存在,ExperimentId={}",recordDTO.getExperimentId());
            throw new TeachingException(ResultEnum.EXPERIMENT_NOT_EXIST);
        }
        if(recordDTO.getHaveCheckAnswer()){
            Float punishment = experiment.getPunishment();
            recordDTO.setExperimentAchievement(recordDTO.getExperimentAchievement()*punishment);
        }
        UserReExperiment userReExperiment = new UserReExperiment();
        BeanUtils.copyProperties(recordDTO,userReExperiment);
        int i = userReExperimentMapper.updateByPrimaryKeySelective(userReExperiment);
        if (i<=0){
            log.error("[RecordServiceImpl]-judge,保存实验提交表失败,userReExperiment={}",userReExperiment);
            throw new TeachingException(ResultEnum.SUBMIT_RECORD_ERROR);
        }
        //更新课程成绩
        achievementService.updateAchievement(experiment.getCourseId(),recordDTO.getUserId());
    }

    @Override
    public void batchJudge(List<RecordDTO> recordDTO) {
        Integer experimentId = recordDTO.get(0).getExperimentId();
        ExperimentMaster experiment = experimentMasterMapper.selectByPrimaryKey(experimentId);
        if(experiment==null){
            log.info("[RecordServiceImpl]-batchJudge,实验主表信息不存在,experimentId={}",experimentId);
            throw new TeachingException(ResultEnum.EXPERIMENT_NOT_EXIST);
        }
        Float punishment = experiment.getPunishment();
        List<UserReExperiment> recordList = recordDTO.stream().map(r -> {
            UserReExperiment record = new UserReExperiment();
            //判断是否提前查看过答案
            if (r.getHaveCheckAnswer()) {
                r.setExperimentAchievement(r.getExperimentAchievement() * punishment);
            }
            BeanUtils.copyProperties(r, record);
            return record;
        }).collect(Collectors.toList());
        //更新操作
        Integer i = userReExperimentDao.updateUserReExperimentByList(recordList);
        if (i<=0){
            log.error("[RecordServiceImpl]-batchJudge,批量保存实验提交表失败,recordList={}",recordList);
            throw new TeachingException(ResultEnum.SUBMIT_RECORD_ERROR);
        }
        //todo 之后需要批量去更新学生的成绩 也许是制作一个异步更新的方法
    }
}
