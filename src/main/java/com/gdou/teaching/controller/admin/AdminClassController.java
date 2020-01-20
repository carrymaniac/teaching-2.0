package com.gdou.teaching.controller.admin;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.Enum.UserIdentEnum;
import com.gdou.teaching.dto.CourseDTO;
import com.gdou.teaching.dto.UserDTO;
import com.gdou.teaching.form.ClazzRegisterForm;
import com.gdou.teaching.mbg.model.Class;
import com.gdou.teaching.mbg.model.User;
import com.gdou.teaching.service.AchievementService;
import com.gdou.teaching.service.ClassService;
import com.gdou.teaching.service.CourseService;
import com.gdou.teaching.service.UserService;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ResultVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.controller.admin
 * @ClassName: ClassController
 * @Author: carrymaniac
 * @Description: 班级管理控制器
 * @Date: 2019/12/30 2:04 下午
 * @Version:
 */
@Controller
@RequestMapping("admin/class")
@Slf4j
public class AdminClassController {

    @Autowired
    UserService userService;

    @Autowired
    AchievementService achievementService;

    @Autowired
    CourseService courseService;

    @Autowired
    ClassService classService;

    @ResponseBody
    @GetMapping("/list")
    public ResultVO list(@RequestParam(value = "page", required = false,defaultValue = "1")Integer page,
                         @RequestParam(value = "size", required = false,defaultValue = "10")Integer size,
                         @RequestParam(value = "classId",required = false,defaultValue = "0")Integer classId,
                         @RequestParam(value = "keyword",required = false,defaultValue = "")String keyword
    ){
        HashMap<String,Object> map = new HashMap<>(2);
        PageInfo pageInfo = userService.getStudentListByClassIdAndKeywordInPage(classId, page, size,keyword);
        map.put("userList",pageInfo);
        if(classId==0){
            //查询一下班级列表供用户前端使用
            List<TreeMap> allClazzList = classService.getAllClazzList();
            map.put("classList",allClazzList);
        }
        return ResultVOUtil.success(map);
    }

    @ResponseBody
    @GetMapping("/info/{userId}")
    public ResultVO info(@PathVariable("userId")Integer userId) {
        if (userId == null) {
            return ResultVOUtil.fail(ResultEnum.PARAM_ERROR);
        }
        UserDTO userInfo = userService.getUserDetailByUserId(userId);
        //查询用户的各课程成绩
        List<CourseDTO> courseDTOS = courseService.listCourseForAdminByStudentId(userId);
        HashMap map = new HashMap(2);
        map.put("user",userInfo);
        map.put("courseList",courseDTOS);
        return ResultVOUtil.success(map);
    }

    @ResponseBody
//    @Auth(user=UserIdentEnum.ADMIN)
    @PostMapping("/addStudentByBatch")
    public ResultVO addStudentByBatch(@RequestBody @Valid ClazzRegisterForm form,
                                      BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("参数格式错误：{}" + form);
            return ResultVOUtil.fail(ResultEnum.BAD_REQUEST.getCode(), ResultEnum.BAD_REQUEST.getMsg());
        }
        List<User> userList = form.getUserList();
        //注册班级
        Class aClass = classService.registerClass(form.getClassName(),userList.size());

        userList.forEach(user->{
            user.setClassId(aClass.getClassId());
            user.setUserIdent(UserIdentEnum.SUTUDENT.getCode().byteValue());
        });
        userService.addUserByBatch(userList);
        List<Integer> userIdList = userList.stream().map(user -> user.getUserId()).collect(Collectors.toList());
        userService.addUserInfoByBatch(userIdList,form.getCollege(),form.getSeries(),form.getMajor());
        return ResultVOUtil.success();
    }

    @ResponseBody
    @PostMapping("/readFile")
    public ResultVO readFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf(".") + 1);
        if ("xls".equals(extension)) {
            return read2003Excel(file);
        } else if ("xlsx".equals(extension)) {
            return read2007Excel(file);
        } else {
            throw new IOException("不支持的文件类型");
        }
    }

    ResultVO read2003Excel(MultipartFile file) throws IOException {
            Workbook workbook = new HSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
        return parseSheetToResultVO(sheet);
    }
    ResultVO read2007Excel(MultipartFile file) throws IOException {
        Workbook xwb = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = xwb.getSheetAt(0);
        return parseSheetToResultVO(sheet);
    }
    private ResultVO parseSheetToResultVO(Sheet sheet) {
        List<HashMap<String,Object>> result = new ArrayList<>();
        //获取行数
        int nums = sheet.getPhysicalNumberOfRows();
        for(int i = 0;i<nums;i++){
            Row row = sheet.getRow(i);
            Cell userNumberCell = row.getCell(0);
            userNumberCell.setCellType(CellType.STRING);
            Cell userNameCell = row.getCell(1);
            userNameCell.setCellType(CellType.STRING);
            Cell passwordCell = row.getCell(2);
            passwordCell.setCellType(CellType.STRING);
            //按照学号、学生姓名、初始密码
            String userNumber = userNumberCell.getStringCellValue();
            String userName = userNameCell.getStringCellValue();
            String password = passwordCell.getStringCellValue();
            HashMap<String,Object> map = new HashMap<>(3);
            map.put("userNumber",userNumber);
            map.put("userName",userName);
            map.put("password",password);
            result.add(map);
        }
        return ResultVOUtil.success(result);
    }
}
