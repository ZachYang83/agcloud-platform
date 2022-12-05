package com.augurit.agcloud.agcom.agsupport.sc.timer.controller;


import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgServicesMonitor;

import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgLayer;
import com.augurit.agcloud.agcom.agsupport.sc.monitor.service.IAgServicesMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :14:18 2018/10/31
 * @Modified By:
 */
@RestController
@RequestMapping("/agsupport/timer")
public class DynamicTimerController implements CommandLineRunner {

    @Autowired
    private IAgServicesMonitor iAgServicesMonitor;
    @Autowired
    private IAgLayer iAgLayer;

    @RequestMapping("/startCron")
    public String startCron() {
        TimerTask timerTask10 = new TimerTask() {
            long counter = 1;
            @Override
            public void run() {
                monitorTask(counter);
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask10,10,600000);
        System.out.println("统计日志定时器启动成功");

        TimerTask timerTask8 = new TimerTask() {
            @Override
            public void run() {
                registerUrlTask();
            }
        };
        Timer timer8 = new Timer();
        timer8.schedule(timerTask8,10,480000);
        System.out.println("获取使用代理服务地址定时器启动成功");
        return "定时器启动成功";
    }

    @Override
    public void run(String... args) {
        startCron();
    }

    /**
     * 定时去获取注册代理服务的url
     * 以注册代理服务的url列表为准，监控列表多余的要删除
     */
    private void registerUrlTask() {
        try {
            //获取使用代理地址的服务图层
            List<AgLayer> list = iAgLayer.findAllAgentUrlList();
            List<AgServicesMonitor> monitorList = iAgServicesMonitor.findAll();
            AgServicesMonitor asm = null;
            for (AgLayer layer : list) {
                asm = null;
                for (AgServicesMonitor monitor : monitorList) {
                    if (monitor.getId().equals(layer.getId())) {
                        asm = monitor;
                    }
                }
                if (asm == null) {
                    asm = new AgServicesMonitor();
                    asm.setId(layer.getId());
                    asm.setServiceName(layer.getName());
                    asm.setServiceFullName(layer.getNameCn());
                    asm.setMonitorUrl(layer.getUrl());
                    asm.setStatus(1);//默认服务状态是正常的
                    asm.setMonitorStatus(1);//默认监控状态是启动的
                    asm.setMonitorFrequency(10);//默认监控频率是十分钟
                    asm.setStartMonitorTime(new Date());
                    asm.setIpBlackList(0);//默认不是黑名单
                    asm.setLastMonitorTime(new Date());
                    iAgServicesMonitor.saveAgServicesMonitor(asm);
                } else {
                    asm.setServiceName(layer.getName());
                    asm.setServiceFullName(layer.getNameCn());
                    asm.setMonitorUrl(layer.getUrl());
                    iAgServicesMonitor.updataAgServicesMonitor(asm);
                }
            }
            //删除监控列表多余的监控记录
            boolean delFlag;
            List<String> monitorIds = new ArrayList<>();
            for (AgServicesMonitor monitor : monitorList) {
                delFlag = true;
                for (AgLayer layer : list) {
                    if (monitor.getId().equals(layer.getId())) delFlag = false;
                }
                if (delFlag) monitorIds.add(monitor.getId());
            }
            if (monitorIds.size() > 0) iAgServicesMonitor.delByIds(monitorIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 定时执行 统计服务日志 并将结果写入监控表
     */
    private void monitorTask(long counter) {
        try {
            List<AgServicesMonitor> list = iAgServicesMonitor.findAll();
            List<AgServicesMonitor> monitorList = new ArrayList<>();
            for (AgServicesMonitor asm : list) {
                int mf = asm.getMonitorFrequency();
                if (asm.getMonitorStatus() == 1 && ((10 * counter) % mf == 0)) {//1:监控状态为监控中;-1监控状态为监控;mf:监控频率，每个服务的监控频率不一样
                    monitorList.add(asm);
                }
            }
            System.out.println("开始第" + counter + "次监控 " + "监控列表长度 " + monitorList.size());
            iAgServicesMonitor.refreshServerStatus(monitorList,counter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
