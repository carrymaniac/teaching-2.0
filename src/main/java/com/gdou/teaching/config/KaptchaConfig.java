package com.gdou.teaching.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.config
 * @ClassName: KaptchaConfig
 * @Author: carrymaniac
 * @Description: 验证码Kaptcha的配置类
 * @Date: 2019/11/16 3:06 下午
 * @Version:
 */
@Component
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha getDefaultKaptcha(){
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        //是否使用边框
        properties.put("kaptcha.border", "no");
        //验证码字体颜色
        properties.put("kaptcha.textproducer.font.color", "black");
        //验证码图片的宽
        properties.put("kaptcha.image.width", "150");
        //验证码图片的高
        properties.put("kaptcha.image.height", "40");
        //验证码字体大小
        properties.put("kaptcha.textproducer.font.size", "30");
        //验证码存储在session中的key
        properties.put("kaptcha.session.key", "verifyCode");
        //验证码中的文字间隔
        properties.put("kaptcha.textproducer.char.space", "5");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
