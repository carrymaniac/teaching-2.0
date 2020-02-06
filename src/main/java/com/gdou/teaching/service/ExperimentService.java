package com.gdou.teaching.service;


import com.gdou.teaching.dto.ExperimentDTO;
import com.gdou.teaching.mbg.model.ExperimentMaster;

import java.util.List;

/**
 * @author bo
 * @date Created in 20:52 2019/8/16
 * @description
 **/

public interface ExperimentService {

    /**
     * 通过课程ID获取实验列表，以DTO方式返回
     * 此处只查询实验主表即可，用于首页列表显示
     * COMMON方法
     * @param courseId
     * @return
     */
    List<ExperimentDTO> list(Integer courseId);
    /**
     * 根据experimentId查找实验主表、实验详情纪录
     *
     * @param experimentId
     * @return
     */
    ExperimentDTO detail(Integer experimentId);
    /**
     * 根据experimentId更新实验提交人数
     * @param experimentId
     * @return 返回更新后的实验主表
     */
    ExperimentMaster updateCommitNumber(Integer experimentId);


    //下面是教师端操作

    /**
     * 新增实验主表和info表记录
     * 需要主动调用AnswerService的Save方法保存答案
     * @param experimentDTO
     * @return
     */
    ExperimentDTO add(ExperimentDTO experimentDTO);

    /**
     * 当前实验提交人数大于解锁阈值,自动解锁下一个实验
     * @param experimentId
     * @return
     */
    boolean autoUnLock(Integer experimentId);

    /**
     * 更新实验基本信息(实验主表)
     * @param experimentDTO
     * @return
     */
    boolean updateExperimentInfo(ExperimentDTO experimentDTO);
    /**
     * 更新实验详情表信息
     * @param experimentDTO
     * @return
     */
    boolean updateExperimentDetail(ExperimentDTO experimentDTO);


    /**
     * 根据experimentId逻辑删除实验主表记录
     * @param experimentId
     * @return
     */
    boolean invalid(Integer experimentId);

    /**
     * 截止上交作业
     * 根据experimentId设置实验为end状态
     * @param experimentId
     * @return
     */
    boolean end(Integer experimentId);

    /**
     *根据experimentId设置实验为锁定状态
     * @param experimentId
     * @return
     */
    boolean lock(Integer experimentId);

    /**
     * 根据experimentId设置实验为正常状态
     * @param experimentId
     * @return
     */
    boolean unlock(Integer experimentId);

}
