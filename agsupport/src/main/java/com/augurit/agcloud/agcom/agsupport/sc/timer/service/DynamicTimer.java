package com.augurit.agcloud.agcom.agsupport.sc.timer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :14:12 2018/10/31
 * @Modified By:
 */
public class DynamicTimer {
/*    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private ScheduledFuture<?> future;
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @RequestMapping("/startCron")
    public String startCron() {
        future = threadPoolTaskScheduler.schedule(new MyRunnable(), new CronTrigger("0/5 * * * * *"));
        System.out.println("DynamicTask.startCron()");
        return "开启定时器";
    }
    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("定时任务DynamicTask.MyRunnable.run()，" + new Date());
        }
    }*/
}
