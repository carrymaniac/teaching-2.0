package com.gdou.teaching.config;

import com.gdou.teaching.dataobject.condition.LocalFileServerCondition;
import com.gdou.teaching.dataobject.condition.OSSFileServerCondition;
import com.gdou.teaching.server.FileServer;
import com.gdou.teaching.server.impl.LocalFileServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.config
 * @ClassName: FileConfig
 * @Author: carrymaniac
 * @Description: 文件服务的配置类
 * @Date: 2020/5/19 9:20 下午
 * @Version:
 */
@Configuration
public class FileConfig {

    @Bean
    @Conditional(OSSFileServerCondition.class)
    public FileServer OSSFileServer(){
        return new LocalFileServer();
    }

    @Bean
    @Conditional(LocalFileServerCondition.class)
    public FileServer LocalFileServer(){
        return new LocalFileServer();
    }



}
