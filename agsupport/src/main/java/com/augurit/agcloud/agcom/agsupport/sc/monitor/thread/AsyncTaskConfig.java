package com.augurit.agcloud.agcom.agsupport.sc.monitor.thread;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :15:19 2019/3/13
 * @Modified By:
 */

@Configuration
@ComponentScan("com.augurit.agcloud.agcom.agsupport.sc.monitor.thread")
@EnableAsync
public class AsyncTaskConfig implements AsyncConfigurer{

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(15);
        taskExecutor.setMaxPoolSize(40);
        taskExecutor.setQueueCapacity(25);// 等待队列
        taskExecutor.initialize();

        return taskExecutor;
    }
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}
