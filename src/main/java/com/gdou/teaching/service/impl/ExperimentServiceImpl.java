package com.gdou.teaching.service.impl;

import com.gdou.teaching.Enum.ExperimentStatusEnum;
import com.gdou.teaching.Enum.FileCategoryEnum;
import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.dao.CourseMasterDao;
import com.gdou.teaching.dto.ExperimentDTO;
import com.gdou.teaching.dto.FileDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.mbg.mapper.*;
import com.gdou.teaching.mbg.model.*;
import com.gdou.teaching.service.ExperimentService;
import com.gdou.teaching.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.service.impl
 * @ClassName: ExperimentServiceImpl
 * @Author: carrymaniac
 * @Description: 实验Service实现类
 * @Date: 2019/12/6 9:43 上午
 * @Version:
 */
@Slf4j
@Service
public class ExperimentServiceImpl implements ExperimentService {
    @Autowired
    ExperimentMasterMapper experimentMasterMapper;
    @Autowired
    ExperimentDetailMapper experimentDetailMapper;
    @Autowired
    FileMapper fileMapper;
    @Autowired
    FileService fileService;
    @Autowired
    CourseMasterMapper courseMasterMapper;
    @Autowired
    ExperimentAnswerMapper answerMapper;
    @Autowired
    UserReExperimentMapper userReExperimentMapper;
    @Override
    public ExperimentDTO detail(Integer experimentId) {
        //需要查询的数据有：
        // 主表数据 副表detail数据 实验文件数据
        ExperimentMaster experimentMaster = experimentMasterMapper.selectByPrimaryKey(experimentId);
        if(experimentMaster==null){
            log.info("[ExperimentServiceImpl]-datail查询实验详情信息,实验主表不存在,experimentId={}",experimentId);
            throw new TeachingException(ResultEnum.EXPERIMENT_NOT_EXIST);
        }
        ExperimentDetail experimentDetail = experimentDetailMapper.selectByPrimaryKey(experimentMaster.getExperimentDetailId());
        if(experimentDetail==null){
            log.info("[ExperimentServiceImpl]-datail查询实验详情信息,实验详情表不存在,experimentDetailId={}",experimentMaster.getExperimentDetailId());
            throw new TeachingException(ResultEnum.EXPERIMENT_DETAIL_NOT_EXIST);
        }
        ExperimentDTO experimentDTO = new ExperimentDTO();
        List<FileDTO> fileDTOList = fileService.selectFileByCategoryAndFileCategoryId(FileCategoryEnum.EXPERIMENT_FILE.getCode(), experimentId);
        experimentDTO.setExperimentDetailFile(fileDTOList);
        //属性拷贝
        BeanUtils.copyProperties(experimentDetail,experimentDTO);
        BeanUtils.copyProperties(experimentMaster,experimentDTO);
        return experimentDTO;
    }

    @Override
    @Transactional
    public ExperimentDTO save(ExperimentDTO experimentDTO) {
        ExperimentMaster experimentMaster=new ExperimentMaster();
        ExperimentDetail experimentDetail=new ExperimentDetail();
        //查询应提交人数
        Integer courseId = experimentDTO.getCourseId();
        CourseMaster courseMaster = courseMasterMapper.selectByPrimaryKey(courseId);
        if(courseMaster==null){
            log.info("[ExperimentServiceImpl]-save 保存实验信息,课程主表不存在,courseId={}",courseId);
            throw new TeachingException(ResultEnum.COURSE_NOT_EXIST);
        }
        experimentDTO.setExperimentParticipationNum(courseMaster.getCourseNumber());
        //检查是否需要加上默认值
        if(experimentDTO.getValve()==null){
            experimentDTO.setValve((float) 0.9);
        }
        if(experimentDTO.getPunishment()==null){
            experimentDTO.setPunishment((float) 0.9);
        }
        experimentDTO.setExperimentCommitNum(0);
        experimentDTO.setExperimentStatus(ExperimentStatusEnum.NORMAL.getCode().byteValue());
        // 先对experimentDetail进行新增/更新
        BeanUtils.copyProperties(experimentDTO,experimentDetail);
        if (experimentDetail.getExperimentDetailId()==null){
            int insert = experimentDetailMapper.insert(experimentDetail);
            if(insert<=0){
                log.error("[ExperimentServiceImpl]-保存实验,新增实验详情表失败,experimentDetail={}",experimentDetail);
                throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
            }
        }else{
            int i = experimentDetailMapper.updateByPrimaryKeyWithBLOBs(experimentDetail);
            if(i<=0){
                log.error("[ExperimentServiceImpl]-保存实验,更新实验详情表失败,experimentDetail={}",experimentDetail);
                throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
            }
        }
        //回填ExperimentDetailId
        experimentDTO.setExperimentDetailId(experimentDetail.getExperimentDetailId());

        BeanUtils.copyProperties(experimentDTO,experimentMaster);
        if (experimentMaster.getExperimentId()==null){
            Integer i = experimentMasterMapper.insert(experimentMaster);
            if (i<=0){
                log.error("[ExperimentServiceImpl]-保存实验,新增实验主表失败,experimentMaster={}",experimentMaster);
                throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
            }
        }else{
            int i = experimentMasterMapper.updateByPrimaryKey(experimentMaster);
            if (i<=0){
                log.error("[ExperimentServiceImpl]-保存实验,更新实验主表失败,experimentMaster={}",experimentMaster);
                throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
            }
        }
        //回填ExperimentId
        experimentDTO.setExperimentId(experimentMaster.getExperimentId());
        //新增实验的文件：
        List<FileDTO> experimentDetailFile = experimentDTO.getExperimentDetailFile();
        if(experimentDetailFile!=null&&!experimentDetailFile.isEmpty()){
            fileService.saveFile(FileCategoryEnum.EXPERIMENT_FILE.getCode(),experimentMaster.getExperimentId(),experimentDetailFile);
        }
        return experimentDTO;
    }

    @Override
    public List<ExperimentDTO> list(Integer courseId) {
        ExperimentMasterExample experimentMasterExample = new ExperimentMasterExample();
        experimentMasterExample.createCriteria().andCourseIdEqualTo(courseId);
        List<ExperimentMaster> experimentMasters = experimentMasterMapper.selectByExample(experimentMasterExample);
        if(experimentMasters==null||experimentMasters.isEmpty()){
            log.info("[ExperimentServiceImpl]-获取实验列表信息,实验主表不存在,courseId={}",courseId);
            throw new TeachingException(ResultEnum.EXPERIMENT_NOT_EXIST);
        }
        List<ExperimentDTO> experimentDTOList = experimentMasters.stream().map(experimentMaster -> {
            ExperimentDTO experimentDTO = new ExperimentDTO();
            BeanUtils.copyProperties(experimentMaster,experimentDTO);
            return experimentDTO;
        }).collect(Collectors.toList());
        return experimentDTOList;

    }

    @Override
    public boolean invalid(Integer experimentId) {
        ExperimentMaster experimentMaster =
                experimentMasterMapper.selectByPrimaryKey(experimentId);
        if (experimentMaster == null) {
            log.info("[ExperimentServiceImpl]-删除实验,该实验不存在或已被删除");
            throw new TeachingException(ResultEnum.EXPERIMENT_NOT_EXIST);
        }
        experimentMaster.setExperimentStatus(ExperimentStatusEnum.INVALID.getCode().byteValue());
        if (experimentMasterMapper.updateByPrimaryKeySelective(experimentMaster)!=1){
            log.info("[ExperimentServiceImpl]-删除实验,实验删除失败");
            throw new TeachingException(ResultEnum.EXPERIMENT_INVALID_ERROR);
        }
        return true;
    }

    @Override
    public boolean end(Integer experimentId) {
        ExperimentMaster experimentMaster =
                experimentMasterMapper.selectByPrimaryKey(experimentId);
        if (experimentMaster == null) {
            log.info("[ExperimentServiceImpl]-完结实验,该实验不存在或已被删除");
            throw new TeachingException(ResultEnum.EXPERIMENT_NOT_EXIST);
        }
        //判断当前状态
        if (experimentMaster.getExperimentStatus().intValue()!=(ExperimentStatusEnum.NORMAL.getCode())
                &&experimentMaster.getExperimentStatus().intValue()!=(ExperimentStatusEnum.LOCK.getCode())){
            log.info("[ExperimentServiceImpl]-完结实验,实验主表状态异常,status={}",experimentMaster.getExperimentStatus());
            throw new TeachingException(ResultEnum.EXPERIMENT_STATUS_ERROR);
        }
        experimentMaster.setExperimentStatus(ExperimentStatusEnum.END.getCode().byteValue());
        if (experimentMasterMapper.updateByPrimaryKeySelective(experimentMaster)!=1){
            log.info("[ExperimentServiceImpl]-完结实验,实验完结失败");
            throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
        }
        return true;
    }



    @Override
    public boolean lock(Integer experimentId) {
        ExperimentMaster experimentMaster =
                experimentMasterMapper.selectByPrimaryKey(experimentId);
        if (experimentMaster == null) {
            log.info("[ExperimentServiceImpl]-锁定实验,该实验不存在或已被删除");
            throw new TeachingException(ResultEnum.EXPERIMENT_NOT_EXIST);
        }
        //判断当前状态
        if (experimentMaster.getExperimentStatus().intValue()!=ExperimentStatusEnum.NORMAL.getCode()){
            log.info("[ExperimentServiceImpl]-锁定实验,实验主表状态异常,status={}",experimentMaster.getExperimentStatus());
            throw new TeachingException(ResultEnum.EXPERIMENT_STATUS_ERROR);
        }
        experimentMaster.setExperimentStatus(ExperimentStatusEnum.END.getCode().byteValue());
        if (experimentMasterMapper.updateByPrimaryKeySelective(experimentMaster)!=1){
            log.info("[ExperimentServiceImpl]-锁定实验,实验锁定失败");
            throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
        }
        return true;
    }

    @Override
    public boolean unlock(Integer experimentId) {
        ExperimentMaster experimentMaster =
                experimentMasterMapper.selectByPrimaryKey(experimentId);
        if (experimentMaster == null) {
            log.info("[ExperimentServiceImpl]-解锁实验,该实验不存在或已被删除");
            throw new TeachingException(ResultEnum.EXPERIMENT_NOT_EXIST);
        }
        //判断当前状态
        if (experimentMaster.getExperimentStatus().intValue()!=ExperimentStatusEnum.LOCK.getCode()){
            log.info("[ExperimentServiceImpl]-解锁实验主表,实验主表状态异常,status={}",experimentMaster.getExperimentStatus());
            throw new TeachingException(ResultEnum.EXPERIMENT_STATUS_ERROR);
        }
        experimentMaster.setExperimentStatus(ExperimentStatusEnum.NORMAL.getCode().byteValue());
        if (experimentMasterMapper.updateByPrimaryKeySelective(experimentMaster)!=1){
            log.info("[ExperimentServiceImpl]-解锁实验,实验解锁失败");
            throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
        }
        return true;
    }

    @Override
    public void updateCommitNumber(Integer experimentId) {
        UserReExperimentExample example = new UserReExperimentExample();
        example.createCriteria().andExperimentIdEqualTo(experimentId);
        int i = userReExperimentMapper.countByExample(example);
        ExperimentMaster experimentMaster = new ExperimentMaster();
        experimentMaster.setExperimentId(experimentId);
        experimentMaster.setExperimentCommitNum(i);
        experimentMasterMapper.updateByPrimaryKeySelective(experimentMaster);
    }
}
