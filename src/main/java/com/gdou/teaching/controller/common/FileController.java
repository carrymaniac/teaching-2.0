package com.gdou.teaching.controller.common;

import com.gdou.teaching.Enum.ResultEnum;
import com.gdou.teaching.service.FileService;
import com.gdou.teaching.util.FileUtil;
import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

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

    @Autowired
    public FileController(FileUtil fileUtil, FileService fileService) {
        this.fileUtil = fileUtil;
        this.fileService = fileService;
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
        //文件后缀名字
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        //文件存储目录
        File fileDirectory = new File(fileUtil.genUploadPath());
        //文件
        File destFile = new File(fileUtil.genUploadPath() + newFileName);
        HashMap<String, String> map = new HashMap<>();
        try {
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdir()) {
                    throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
                }
            }
            file.transferTo(destFile);
            String url = FileUtil.getHost(new URI(httpServletRequest.getRequestURL() + "")) + contextPath + "/download/" + newFileName;
            file.getSize();
            map.put("fileName", fileName);
            map.put("fileType", fileUtil.getType(suffixName));
            map.put("filePath", url);
            map.put("fileSize", fileUtil.getFileSize(file.getSize()));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


}
