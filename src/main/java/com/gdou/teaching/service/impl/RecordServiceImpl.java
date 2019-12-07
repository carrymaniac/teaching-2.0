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
import com.gdou.teaching.mbg.model.UserReExperiment;
import com.gdou.teaching.mbg.model.UserReExperimentExample;
import com.gdou.teaching.service.FileService;
import com.gdou.teaching.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Override
    public RecordDTO save(RecordDTO recordDTO) {
        //查询该实验的状态。
        ExperimentMaster experiment = experimentMasterMapper.selectByPrimaryKey(recordDTO.getExperimentId());
        if (!experiment.getExperimentStatus().equals(ExperimentStatusEnum.NORMAL.getCode())){
            log.error("保存纪录,保存实验记录失败,当前实验不可提交,ExperimentStatus={}",experiment.getExperimentStatus());
            throw new TeachingException(ResultEnum.EXPERIMENT_STATUS_ERROR);
        }
        //查询用户的上一次提交
        UserReExperimentExample userReExperimentExample = new UserReExperimentExample();
        userReExperimentExample.createCriteria().andUserIdEqualTo(recordDTO.getUserId()).andExperimentIdEqualTo(recordDTO.getExperimentId());
        List<UserReExperiment> userReExperiments = userReExperimentMapper.selectByExample(userReExperimentExample);
        if(userReExperiments!=null&&!userReExperiments.isEmpty()){
            Byte preStatus = userReExperiments.get(0).getStatus();
            if(preStatus.equals(RecordStatusEnum.PASS.getCode().byteValue())||preStatus.equals(RecordStatusEnum.LOCK.getCode().byteValue())){
                //如果已经通过审核或者是实验被锁定，无法再次提交
                log.error("保存实验记录失败,已有实验提交记录被审核通过或实验已被锁定,preStatus={}",preStatus);
                throw new TeachingException(ResultEnum.SUBMIT_RECORD_ERROR);
            }
        }

        //存储记录
        UserReExperiment userReExperiment = new UserReExperiment();
        BeanUtils.copyProperties(recordDTO,userReExperiment);
        userReExperiment.setExperimentAchievement((double)0);
        userReExperiment.setStatus(RecordStatusEnum.REVIEWING.getCode().byteValue());
        int insert = userReExperimentMapper.insert(userReExperiment);
        if (insert<=0){
            log.error("保存记录,保存实验记录失败,record={}",userReExperiment);
            throw new TeachingException(ResultEnum.SUBMIT_RECORD_ERROR);
        }

        //存储文件
        List<FileDTO> fileDTOs = recordDTO.getUserExperimentFile();
        if(fileDTOs!=null&&!fileDTOs.isEmpty()) {
            fileService.saveFile(FileCategoryEnum.RECORD_FILE.getCode(), userReExperiment.getUserExperimentId(), fileDTOs);
        }
        recordDTO.setUserExperimentId(userReExperiment.getUserExperimentId());
        return recordDTO;
    }

    @Override
    public RecordDTO selectOne(Integer experimentId, Integer userId) {
        UserReExperimentExample userReExperimentExample = new UserReExperimentExample();
        userReExperimentExample.setOrderByClause("create_time desc");
        userReExperimentExample.createCriteria().andUserIdEqualTo(userId).andExperimentIdEqualTo(experimentId);
        List<UserReExperiment> userReExperiments = userReExperimentMapper.selectByExample(userReExperimentExample);
        if(userReExperiments==null||userReExperiments.isEmpty()){
            return null;
        }
        UserReExperiment userReExperiment = userReExperiments.get(0);
        RecordDTO recordDTO = new RecordDTO();
        //查询用户提交时提交的文件记录
        List<FileDTO> fileDTOList = fileService.selectFileByCategoryAndFileCategoryId(FileCategoryEnum.RECORD_FILE.getCode(), userReExperiment.getUserExperimentId());
        if(!fileDTOList.isEmpty()){
            recordDTO.setUserExperimentFile(fileDTOList);
        }
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
    public List<RecordDTO> getRecordByUserIdAndCourseId(Integer userId, Integer CourseId) {

        return null;
    }
}
