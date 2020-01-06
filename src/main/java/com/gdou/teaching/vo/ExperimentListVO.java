package com.gdou.teaching.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author bo
 * @date Created in 0:40 2019/8/16
 * @description 实验列表VO
 **/

@Data
public class ExperimentListVO {
    /**
     * 课程编号
     */
    private Integer courseId;
    /**
     * 课程下属实验
     */
    List<ExperimentVO> ExperimentDTOList;
}
