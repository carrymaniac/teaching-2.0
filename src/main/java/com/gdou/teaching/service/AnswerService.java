package com.gdou.teaching.service;

import com.gdou.teaching.dto.AnswerDTO;
import com.gdou.teaching.dto.ExperimentDTO;

/**
 * @author bo
 * @date Created in 21:10 2019/10/14
 * @description
 **/
public interface AnswerService {
    /**
     * 查询实验答案的详情
     * @param experimentAnswerId
     * @return
     */
    AnswerDTO selectOne(Integer experimentAnswerId);

    /**
     * 新增或修改实验答案
     * @param answerDTO
     * @return
     */
    AnswerDTO save(AnswerDTO answerDTO);

    /**
     * 更新实验答案
     * @param experimentDTO
     * @return
     */
    boolean updateExperimentAnswer(ExperimentDTO experimentDTO);
}
