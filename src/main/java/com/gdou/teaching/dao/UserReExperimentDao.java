package com.gdou.teaching.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserReExperimentDao {
    /**
     * 获取已提交的用户记录数量
     * @param experimentId
     * @return
     */
    Integer getCountByExperimentId(Integer experimentId);
}
