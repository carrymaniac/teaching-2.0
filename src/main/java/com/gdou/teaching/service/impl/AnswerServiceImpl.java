package com.gdou.teaching.service.impl;

import com.gdou.teaching.Enum.FileCategoryEnum;
import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.dataobject.ExperimentAnswer;
import com.gdou.teaching.dto.AnswerDTO;
import com.gdou.teaching.dto.FileDTO;
import com.gdou.teaching.exception.TeachingException;
import com.gdou.teaching.mapper.ExperimentAnswerMapper;
import com.gdou.teaching.mbg.mapper.ExperimentAnswerMapper;
import com.gdou.teaching.mbg.mapper.FileMapper;
import com.gdou.teaching.mbg.model.ExperimentAnswer;
import com.gdou.teaching.service.AnswerService;
import com.gdou.teaching.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author bo
 * @date Created in 20:59 2019/11/5
 * @description
 **/
@Service
@Slf4j
public class AnswerServiceImpl implements AnswerService {
    @Autowired
    ExperimentAnswerMapper answerMapper;
    @Autowired
    FileService fileService;
    @Autowired
    FileMapper fileMapper;
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
            //重新插入答案
            fileService.saveFile(FileCategoryEnum.EXPERIMENT_ANSWER_FILE.getCode(),experimentAnswer.getExperimentAnswerId(),answerDTO.getExperimentAnswerFileList());
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
            //重新插入
            fileService.saveFile(FileCategoryEnum.EXPERIMENT_ANSWER_FILE.getCode(),experimentAnswer.getExperimentAnswerId(),answerDTO.getExperimentAnswerFileList());
        }
        return answerDTO;
    }

//    //todo 现实现根据experimentId删除实验答案, 可修改为根据experimentAnswerId删除(待讨论)
//    @Override
//    public boolean invalid(Integer experimentId) {
//        ExperimentAnswer experimentAnswer
//                = answerMapper.selectExperimentAnswerByExperimentId(experimentId);
//        if (experimentAnswer == null) {
//            log.error("删除实验答案,该实验答案不存在或已被删除");
//            throw new TeachingException(ResultEnum.ANSWER_NOT_EXIST);
//        }
//        //更新
//        boolean flag = answerMapper.deleteExperimentAnswerByExperimentAnswerId(experimentAnswer.getExperimentAnswerId());
//        if (!flag){
//            log.error("删除实验答案,实验答案删除失败");
//            throw new TeachingException(ResultEnum.ANSWER_INVALID_ERROR);
//        }
//        return true;
//    }
}
