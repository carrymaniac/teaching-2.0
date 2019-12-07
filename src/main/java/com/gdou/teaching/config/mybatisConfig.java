package com.gdou.teaching.config;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@MapperScan({"com.gdou.teaching.mbg.mapper","com.gdou.teaching.dao"})
public class mybatisConfig {
    @Bean
    public ConfigurationCustomizer configurationCustomizer(){
        return configuration -> {
            configuration.setCacheEnabled(true);
            configuration.setDefaultStatementTimeout(3000);
            configuration.setMapUnderscoreToCamelCase(true);
            configuration.setUseGeneratedKeys(true);
        };
    }
}
