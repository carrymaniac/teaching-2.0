package com.gdou.teaching.controller.common;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.controller.common
 * @ClassName: KaptchaController
 * @Author: carrymaniac
 * @Description: Kaptcha验证码的控制器
 * @Date: 2019/11/16 3:11 下午
 * @Version:
 */
@Controller
@Slf4j
public class KaptchaController {
    @Autowired
    DefaultKaptcha kaptcha;


    @GetMapping("common/kaptcha")
    public void verifyCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        try {
            //生产验证码字符串并保存到session中
            String verifyCode = kaptcha.createText();
            log.debug("生成的验证码为:{}",verifyCode);
            httpServletRequest.getSession().setAttribute("verifyCode", verifyCode);
            BufferedImage challenge = kaptcha.createImage(verifyCode);
            httpServletResponse.setHeader("Cache-Control", "no-store");
            httpServletResponse.setHeader("Pragma", "no-cache");
            httpServletResponse.setDateHeader("Expires", 0);
            httpServletResponse.setContentType("image/jpeg");
            ServletOutputStream os = httpServletResponse.getOutputStream();
            ImageIO.write(challenge, "jpg", os);
            os.flush();
            os.close();
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
    }


}
