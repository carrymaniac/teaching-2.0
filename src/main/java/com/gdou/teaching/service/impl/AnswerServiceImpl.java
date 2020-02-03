package com.gdou.teaching.service.impl;

import com.gdou.teaching.Enum.FileCategoryEnum;
import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.dto.AnswerDTO;
import com.gdou.teaching.dto.FileDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.mbg.mapper.ExperimentAnswerMapper;
import com.gdou.teaching.mbg.mapper.ExperimentMasterMapper;
import com.gdou.teaching.mbg.mapper.FileMapper;
import com.gdou.teaching.mbg.model.ExperimentAnswer;
import com.gdou.teaching.mbg.model.ExperimentMaster;
import com.gdou.teaching.service.AnswerService;
import com.gdou.teaching.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author bo
 * @date Created in 20:59 2019/11/5
 * @description
 **/
@Service
@Slf4j
public class AnswerServiceImpl implements AnswerService {
    private final ExperimentAnswerMapper answerMapper;
    private final FileService fileService;
    private final FileMapper fileMapper;
    private final ExperimentMasterMapper experimentMasterMapper;

    public AnswerServiceImpl(ExperimentAnswerMapper answerMapper, FileService fileService, FileMapper fileMapper, ExperimentMasterMapper experimentMasterMapper) {
        this.answerMapper = answerMapper;
        this.fileService = fileService;
        this.fileMapper = fileMapper;
        this.experimentMasterMapper = experimentMasterMapper;
    }

    @Override
    public AnswerDTO detail(Integer experimentAnswerId) {
        ExperimentAnswer experimentAnswer = answerMapper.selectByPrimaryKey(experimentAnswerId);
        List<FileDTO> fileDTOList = fileService.selectFileByCategoryAndFileCategoryId(FileCategoryEnum.EXPERIMENT_ANSWER_FILE.getCode(), experimentAnswerId);
        if(experimentAnswer!=null){
            AnswerDTO answerDTO=new AnswerDTO();
            BeanUtils.copyProperties(experimentAnswer,answerDTO);
            answerDTO.setExperimentAnswerFileList(fileDTOList);
            return answerDTO;
        }
        return null;
    }

    @Override
    @Transactional
    public AnswerDTO save(AnswerDTO answerDTO) {
        ExperimentAnswer experimentAnswer=new ExperimentAnswer();
        BeanUtils.copyProperties(answerDTO,experimentAnswer);
        //如果ExperimentAnswerId为空,则为添加操作
        if (experimentAnswer.getExperimentAnswerId()==null){
            answerMapper.insert(experimentAnswer);
            if (experimentAnswer.getExperimentAnswerId()==null){
                log.error("保存实验答案,新增实验答案失败,experimentAnswer={}",experimentAnswer);
                throw new TeachingException(ResultEnum.PARAM_ERROR);
            }

            //需要对experiment主表进行更新AnswerID操作
            ExperimentMaster experimentMaster = new ExperimentMaster();
            experimentMaster.setExperimentId(answerDTO.getExperimentId());
            experimentMaster.setExperimentAnswerId(experimentAnswer.getExperimentAnswerId());
            experimentMasterMapper.updateByPrimaryKeySelective(experimentMaster);

            //新增答案文件
            if(answerDTO.getExperimentAnswerFileList()!=null&&!answerDTO.getExperimentAnswerFileList().isEmpty()){
                fileService.saveFile(FileCategoryEnum.EXPERIMENT_ANSWER_FILE.getCode(),experimentAnswer.getExperimentAnswerId(),answerDTO.getExperimentAnswerFileList());
            }
            //回填实验答案Id
            answerDTO.setExperimentAnswerId(experimentAnswer.getExperimentAnswerId());
        }else{ //更新操作
            int i = answerMapper.updateByPrimaryKeySelective(experimentAnswer);
            if (i<=0){
                log.error("保存实验答案,修改实验答案失败,experimentAnswer={}",experimentAnswer);
                throw new TeachingException(ResultEnum.PARAM_ERROR);
            }
            //先删除之前的
            fileService.deleteFiles(FileCategoryEnum.EXPERIMENT_ANSWER_FILE.getCode(),experimentAnswer.getExperimentAnswerId());
            //重新插入答案
            if(answerDTO.getExperimentAnswerFileList()!=null&&!answerDTO.getExperimentAnswerFileList().isEmpty()){
                fileService.saveFile(FileCategoryEnum.EXPERIMENT_ANSWER_FILE.getCode(),experimentAnswer.getExperimentAnswerId(),answerDTO.getExperimentAnswerFileList());
            }
        }
        return answerDTO;
    }
}
