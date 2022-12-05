package com.augurit.agcloud.agcom.agsupport.common.config.RedisCache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @author zhangmingyang
 * @Description: 定时检测Redis
 * @date 2019-06-27 17:20
 */
@Component
@Configuration
@EnableScheduling
public class RedisScheduleTask {
    private static Logger logger = LoggerFactory.getLogger(RedisScheduleTask.class);
    @Autowired
    private AgServiceRedisCache agServiceRedisCache;

    @Scheduled(cron = "0/5 * * * * ?")
    //时间间隔 60s
    @Scheduled(fixedRate=60000)
    private void configureTasks() {
        boolean flag = agServiceRedisCache.checkRedisIsAvaliable();
        MapCache.put(flag);
    }
}
