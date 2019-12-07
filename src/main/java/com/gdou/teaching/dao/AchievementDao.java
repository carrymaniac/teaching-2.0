package com.gdou.teaching.dao;

import com.gdou.teaching.mbg.model.Achievement;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.dao
 * @ClassName: AchievementDao
 * @Author: carrymaniac
 * @Description: 成绩类的Dao
 * @Date: 2019/12/3 5:34 下午
 * @Version:
 */
@Repository
public interface AchievementDao {
    /**
     * 批量插入Achievement
     * @param achievements
     * @return
     */
    int insertList(List<Achievement> achievements);
}
