package com.augurit.agcloud.agcom.agsupport.sc.regainWork.controller;

import com.augurit.agcloud.agcom.agsupport.sc.regainWork.service.IRegainWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Configuration
@EnableScheduling
public class ScheduleWork {

    @Autowired
    IRegainWork iRegainWork;

    @Scheduled(cron="0 55 14 * * ?")
    public void writeRegainProjectCache(){
        iRegainWork.writeRedisCache();
    }
}
