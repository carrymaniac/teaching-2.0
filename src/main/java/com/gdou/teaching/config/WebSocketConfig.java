package com.gdou.teaching.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.config
 * @ClassName: WebSocketConfig
 * @Author: carrymaniac
 * @Description:
 * @Date: 2020/5/6 11:47 上午
 * @Version:
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
