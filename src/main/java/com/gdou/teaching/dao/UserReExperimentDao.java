package com.gdou.teaching.dao;

import com.gdou.teaching.mbg.model.UserReExperiment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserReExperimentDao {
    /**
     * 获取已提交的用户记录数量
     * @param experimentId
     * @return
     */
    Integer getCountByExperimentId(Integer experimentId);

    /**
     * 批量更新用户提交数量
     * @param record
     * @return
     */
    Integer updateUserReExperimentByList(List<UserReExperiment> record);
}
