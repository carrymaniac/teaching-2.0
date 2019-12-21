package com.gdou.teaching.util;

import com.gdou.teaching.dto.AchievementDTO;
import com.gdou.teaching.mbg.model.Achievement;
import com.gdou.teaching.service.AchievementService;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ProjectName: teaching-2.0
 * @Package: com.gdou.teaching.util
 * @ClassName: PoiUtilTest
 * @Author: carrymaniac
 * @Description:
 * @Date: 2019/12/21 7:17 下午
 * @Version:
 */
@SpringBootTest
class PoiUtilTest {

    @Autowired
    PoiUtil poiUtil;
    @Autowired
    AchievementService achievementService;
    @Test
    void createSheet() throws IOException {
        List<AchievementDTO> achievementByCourseId = achievementService.getAchievementByCourseId(1);
        List<List<String>> collect = achievementByCourseId.stream().map(achievementDTO -> {
            List<String> list = new ArrayList<>();
            list.add(achievementDTO.getUserName());
            list.add(achievementDTO.getCourseAchievement().toString());
            return list;
        }).collect(Collectors.toList());
        Workbook sheet = poiUtil.createSheet("学生成绩", Arrays.asList("学生姓名", "学生成绩"), collect);
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/carrymaniac/upload/学生成绩.xls");
        sheet.write(fileOutputStream);
        fileOutputStream.close();
    }
}