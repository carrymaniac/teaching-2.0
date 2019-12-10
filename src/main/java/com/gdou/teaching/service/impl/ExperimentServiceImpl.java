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
    @Override
    public ExperimentDTO detail(Integer experimentId) {
        //需要查询的数据有：
        // 主表数据 副表detail数据 实验文件数据
        ExperimentMaster experimentMaster = experimentMasterMapper.selectByPrimaryKey(experimentId);
        if(experimentMaster==null){
            throw new TeachingException(ResultEnum.EXPERIMENT_NOT_EXIST);
        }
        ExperimentDetail experimentDetail = experimentDetailMapper.selectByPrimaryKey(experimentMaster.getExperimentDetailId());
        if(experimentDetail==null){
            throw new TeachingException(ResultEnum.EXPERIMENT_DETAIL_NOT_EXIST);
        }
        ExperimentDTO experimentDTO = new ExperimentDTO();
        List<FileDTO> fileDTOList = fileService.selectFileByCategoryAndFileCategoryId(FileCategoryEnum.EXPERIMENT_FILE.getCode(), experimentId);
        if(fileDTOList!=null&&!fileDTOList.isEmpty()){
            experimentDTO.setExperimentDetailFile(fileDTOList);
        }
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
                log.error("保存实验,新增实验详情表失败,experimentDetail={}",experimentDetail);
                throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
            }
        }else{
            int i = experimentDetailMapper.updateByPrimaryKey(experimentDetail);
            if(i<=0){
                log.error("保存实验,更新实验详情表失败,experimentDetail={}",experimentDetail);
                throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
            }
        }
        //回填ExperimentDetailId
        experimentDTO.setExperimentDetailId(experimentDetail.getExperimentDetailId());


        //如果有答案 插入到答案表
        if(experimentDTO.getExperimentAnswerContent()!=null){
            ExperimentAnswer answer = new ExperimentAnswer();
            answer.setExperimentAnswerContent(experimentDTO.getExperimentAnswerContent());
            answerMapper.insert(answer);
            //如果有答案文件 需要插入到文件表
            List<FileDTO> experimentAnswerFile = experimentDTO.getExperimentAnswerFile();
            if(experimentAnswerFile!=null&&!experimentAnswerFile.isEmpty()){
                fileService.saveFile(FileCategoryEnum.EXPERIMENT_ANSWER_FILE.getCode(),answer.getExperimentAnswerId(),experimentAnswerFile);
            }
            experimentDTO.setExperimentAnswerId(answer.getExperimentAnswerId());
        }


        BeanUtils.copyProperties(experimentDTO,experimentMaster);
        if (experimentMaster.getExperimentId()==null){
            Integer i = experimentMasterMapper.insert(experimentMaster);
            if (i<=0){
                log.error("保存实验,新增实验主表失败,experimentMaster={}",experimentMaster);
                throw new TeachingException(ResultEnum.EXPERIMENT_SAVE_ERROR);
            }
        }else{
            int i = experimentMasterMapper.updateByPrimaryKey(experimentMaster);
            if (i<=0){
                log.error("保存实验,更新实验主表失败,experimentMaster={}",experimentMaster);
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
            return null;
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
        return false;
    }

    @Override
    public ExperimentMaster ban(Integer experimentId) {
        return null;
    }

    @Override
    public ExperimentMaster restore(Integer experimentId) {
        return null;
    }

    @Override
    public ExperimentMaster lock(Integer experimentId) {
        return null;
    }

    @Override
    public ExperimentMaster unlock(Integer experimentId) {
        return null;
    }
}
