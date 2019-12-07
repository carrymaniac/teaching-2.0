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
     * 根据experimentId查找实验主表、实验详情纪录
     * @param experimentId
     * @return
     */
    ExperimentDTO detail(Integer experimentId);

    /**
     * 新增或更新实验主表记录
     * @param experimentDTO
     * @return
     */
    ExperimentDTO save(ExperimentDTO experimentDTO);

    /**
     * 根据experimentId逻辑删除实验主表记录
     * @param experimentId
     * @return
     */
    boolean invalid(Integer experimentId);

    /**
     * 截止上交作业
     * 根据experimentId设置实验为不可提交状态
     */
    ExperimentMaster ban(Integer experimentId);

    /**
     * 根据experimentId设置实验为正常状态
     */
    ExperimentMaster restore(Integer experimentId);

    /**
     * 根据experimentId设置实验为锁定状态
     */
    ExperimentMaster lock(Integer experimentId);

    /**
     * 根据experimentId设置实验为正常状态
     */
    ExperimentMaster unlock(Integer experimentId);
    /**
     * 通过课程ID获取实验列表，以DTO方式返回
     * @param courseId
     * @return
     */
    List<ExperimentDTO> list(Integer courseId);
}
