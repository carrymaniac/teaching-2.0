package com.gdou.teaching.service;

import com.gdou.teaching.dto.AnswerDTO;

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
    AnswerDTO detail(Integer experimentAnswerId);

    /**
     * 新增或修改实验答案
     * @param answerDTO
     * @return
     */
    AnswerDTO save(AnswerDTO answerDTO);

//    /**
//     * 根据实验ID删除实验答案
//     * @param experimentId
//     * @return
//     */
//    boolean invalid(Integer experimentId);

}
