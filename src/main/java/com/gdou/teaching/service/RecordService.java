package com.gdou.teaching.service;

import com.gdou.teaching.dto.RecordDTO;

import java.util.List;

/**
 * @author bo
 * @date Created in 16:35 2019/9 /12
 * @description
 **/
public interface RecordService {

    /**
     * 保存或修改提交记录
     * @param recordDTO
     * @return
     */
    RecordDTO save(RecordDTO recordDTO);

    /**
     * 通过实验ID和用户ID获取用户的实验提交记录
     * @param experimentId
     * @param userId
     * @return
     */
    RecordDTO selectOne(Integer experimentId, Integer userId);

    /**
     * 更新experiment的提交人数
     * 对redis进行更新，同时进行阈值判断，如果到达了解锁条件，调用解锁条件进行解锁。
     * @param experimentId
     */
    void updateExperimentCommitNumber(Integer experimentId);


    /**
     * 根据用户Id和课程Id,获取用户在这门课的所有实验的成绩
     * ⚠️：该方法不带用户提交记录中的大文本
     * @param userId 用户ID
     * @param CourseId 课程ID
     * @return
     */
    List<RecordDTO> getRecordByUserIdAndCourseId(Integer userId, Integer CourseId);


    /**
     * 教师评改
     * @param recordDTO
     */
    void judge(RecordDTO recordDTO);


    /**
     * 教师评改 批量批改
     * @param recordDTO
     */
    void batchJudge(List<RecordDTO> recordDTO);


}
