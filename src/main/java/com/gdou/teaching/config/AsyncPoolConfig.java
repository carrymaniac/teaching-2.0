package com.gdou.teaching.config;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.config
 * @ClassName: AsyncPoolConfig
 * @Author: carrymaniac
 * @Description: 异步线程池配置类
 * @Date: 2019/9/23 10:20 上午
 * @Version:
 */
@Configuration
// 启动异步。
// @EnableAsync
public class AsyncPoolConfig implements AsyncConfigurer {
    private static final int CORE_POOL_SIZE  = 6;
    private static final int MAX_POOL_SIZE  = 10;
    private static final int QUEUE_CAPACITY  = 100;

    @Bean
    public Executor asyncThreadPoolTaskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();

        //核心线程数
        threadPoolTaskExecutor.setCorePoolSize(CORE_POOL_SIZE);
        //最大线程数
        threadPoolTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        //队列大小
        threadPoolTaskExecutor.setQueueCapacity(QUEUE_CAPACITY);
//        threadPoolTaskExecutor.setKeepAliveSeconds(200);
        //前缀名字
        threadPoolTaskExecutor.setThreadNamePrefix("teaching_TaskExecutor-");
        //当调度器shutdown被调用时等待当前被调度的任务完成
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        //等待时长
        threadPoolTaskExecutor.setAwaitTerminationSeconds(60);
        //当最大池满时，此策略保证不会丢失任务请求。但可能影响性能。
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
