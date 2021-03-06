package com.gdou.teaching.controller.common;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.dto.CourseDTO;
import com.gdou.teaching.server.FileServer;
import com.gdou.teaching.service.FileService;
import com.gdou.teaching.util.FileUtil;
import com.gdou.teaching.util.PoiUtil;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.controller
 * @ClassName: FileController
 * @Author: carrymaniac
 * @Description: 文件控制器，用于上传下载
 * @Date: 2019/9/12 12:15 下午
 * @Version:
 */
@Controller
@Slf4j
@RequestMapping("/common/file")
public class FileController {
    @Value("${server.servlet.context-path}")
    private String contextPath;

    private final FileUtil fileUtil;
    private final FileService fileService;
    private final PoiUtil poiUtil;
    private final FileServer fileServer;

    @Autowired
    public FileController(FileUtil fileUtil, FileService fileService, PoiUtil poiUtil, FileServer fileServer) {
        this.fileUtil = fileUtil;
        this.fileService = fileService;
        this.poiUtil = poiUtil;
        this.fileServer = fileServer;
    }

    @PostMapping("/upload")
    @ResponseBody
    private ResultVO uploadForFile(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) throws URISyntaxException {
        try {
            HashMap<String, String> map = uploadFile(httpServletRequest, file);
            return ResultVOUtil.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.fail(ResultEnum.FILE_UPLOAD_FAIL);
        }
    }

    @PostMapping("/uploadBatch")
    @ResponseBody
    private ResultVO uploadForFileBatch(HttpServletRequest httpServletRequest, @RequestParam("files") MultipartFile[] files) throws URISyntaxException {
        try {
            List<HashMap<String,String>> result = new ArrayList<>();
            for(MultipartFile file:files){
                HashMap<String, String> map = uploadFile(httpServletRequest, file);
                result.add(map);
            }
            return ResultVOUtil.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVOUtil.fail(ResultEnum.FILE_UPLOAD_FAIL);
        }
    }

    @ResponseBody
    @PostMapping("/uploadPic")
    private ResultVO uploadForPic(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) throws URISyntaxException {
        try {
            HashMap<String, String> map = uploadFile(httpServletRequest, file);
            HashMap<String,String> result = new HashMap<>();
            result.put("url",map.get("filePath"));
            return ResultVOUtil.success(result);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultVOUtil.fail(ResultEnum.FILE_UPLOAD_FAIL);
        }
    }
    @ResponseBody
    @GetMapping("/delete/{fileId}")
    public ResultVO delete(@PathVariable(value = "fileId") Integer fileId){
        fileService.deleteFile(fileId);
        return ResultVOUtil.success();
    }


    public HashMap<String, String> uploadFile(HttpServletRequest httpServletRequest, MultipartFile file) throws IOException {
        //文件原始名字
        String fileName = file.getOriginalFilename();
        String url = fileServer.uploadFile(httpServletRequest, fileName, file);
        //文件后缀名字
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        HashMap<String, String> map = new HashMap<>();
        try {
            map.put("fileName", fileName);
            map.put("fileType", fileUtil.getType(suffixName));
            map.put("filePath", url);
            map.put("fileSize", fileUtil.getFileSize(file.getSize()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    //解析Excel文件 管理员-文件导入
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
        //跳过第一行列头
        for(int i = 1;i<nums;i++){
            Row row = sheet.getRow(i);
            Cell userNumberCell = row.getCell(0);
            userNumberCell.setCellType(CellType.STRING);
            Cell userNameCell = row.getCell(1);
            userNameCell.setCellType(CellType.STRING);
            Cell passwordCell = row.getCell(2);
            passwordCell.setCellType(CellType.STRING);
            //按照学号、学生姓名、初始密码
            String userNumber = userNumberCell.getStringCellValue();
            String nickName = userNameCell.getStringCellValue();
            String password = passwordCell.getStringCellValue();
            HashMap<String,Object> map = new HashMap<>(3);
            map.put("userNumber",userNumber);
            map.put("nickname",nickName);
            map.put("password",password);
            result.add(map);
        }
        return ResultVOUtil.success(result);
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
