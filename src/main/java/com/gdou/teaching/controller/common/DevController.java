package com.gdou.teaching.controller.common;

import com.gdou.teaching.util.ResultVOUtil;
import com.gdou.teaching.vo.ResultVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

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
public class DevController {

    @Value("${spring.profiles.active}")
    private String profiles;
    @Value("${teaching.uploadPath}")
    public String uploadPath;

    @GetMapping("/info")
    @ResponseBody
    public ResultVO info(){
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("profiles",profiles);
        map.put("uploadPath",uploadPath);
        return ResultVOUtil.success(map);
    }
}
