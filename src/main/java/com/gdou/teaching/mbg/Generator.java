package com.gdou.teaching.mbg;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: jwt
 * @Package: com.gdou.jwt.mgb
 * @ClassName: Generator
 * @Author: carrymaniac
 * @Description: mbg生产代码
 * @Date: 2019/11/20 3:14 下午
 * @Version:
 */
public class Generator {
    public static void main(String[] args) throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException {
        //执行MBG中产生的警告信息
        List<String> warnings = new ArrayList<String>();
        //生成的代码重复时，覆盖源代码
        boolean overwrite = true;
        //读取MBG配置文件
        InputStream is = Generator.class.getResourceAsStream("/generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(is);
        is.close();

        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        //执行MBG
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,callback,warnings);
        //执行生成代码
        myBatisGenerator.generate(null);

        for (String warning:warnings){
            System.out.println(warning);
        }


    }
}
