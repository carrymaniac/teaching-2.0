package com.gdou.teaching.controller.common;

import com.gdou.teaching.dto.AchievementDTO;
import com.gdou.teaching.service.AchievementService;
import com.gdou.teaching.service.impl.RedisServiceImpl;
import com.gdou.teaching.util.PoiUtil;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.controller.common
 * @ClassName: devController
 * @Author: carrymaniac
 * @Description:
 * @Date: 2019/11/17 2:46 下午
 * @Version:
 */
@Controller
@RequestMapping("/dev")
@Slf4j
public class DevController {

    @Value("${spring.profiles.active}")
    private String profiles;
    @Value("${fileServer.uploadPath}")
    public String uploadPath;

    @Autowired
    RedisServiceImpl redisService;

    final
    PoiUtil poiUtil;
    final
    AchievementService achievementService;

    public DevController(PoiUtil poiUtil, AchievementService achievementService) {
        this.poiUtil = poiUtil;
        this.achievementService = achievementService;
    }

    @GetMapping("/info")
    @ResponseBody
    public ResultVO info(){
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("profiles",profiles);
        map.put("uploadPath",uploadPath);
        return ResultVOUtil.success(map);
    }

    @GetMapping("/download")
    public String download(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode("学生成绩.xls","UTF-8"));
        OutputStream outputStream = response.getOutputStream();
        try{
            List<AchievementDTO> achievementByCourseId = achievementService.getAchievementByCourseId(1);
            List<List<String>> collect = achievementByCourseId.stream().map(achievementDTO -> {
                List<String> list = new ArrayList<>();
                list.add(achievementDTO.getUserName());
                list.add(achievementDTO.getCourseAchievement().toString());
                return list;
            }).collect(Collectors.toList());
            Workbook sheet = poiUtil.createSheet("学生成绩", Arrays.asList("学生姓名", "学生成绩"), collect);
            sheet.write(outputStream);

            System.out.println("success");
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            outputStream.close();
        }
        return null;
    }

    @GetMapping("/getExcelTemple")
    public void genTemple(HttpServletResponse response) throws IOException {
        Workbook sheet = poiUtil.createSheet("新增用户模版", Arrays.asList("工号/学号", "姓名", "初始密码"), null);
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode("新增用户模版.xls","UTF-8"));
        OutputStream outputStream = response.getOutputStream();
        sheet.write(outputStream);
    }

}
