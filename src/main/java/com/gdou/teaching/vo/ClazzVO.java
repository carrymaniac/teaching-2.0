package com.gdou.teaching.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gdou.teaching.dto.UserDTO;
import lombok.Data;

import java.util.List;

/**
 * @author carrymaniac
 * @date Created in 22:21 2019-08-14
 * @description 班级类的VO，里面带了所有的学生信息
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClazzVO {
        /**
         * 班级Id
         */
        private Integer classId;
        /**
         *班级名称
         */
        private String className;
        /**
         *班级状态
         */
        private Integer classStatus;
        /**
         *学生列表
         */
        private List<UserDTO> userList;
}
