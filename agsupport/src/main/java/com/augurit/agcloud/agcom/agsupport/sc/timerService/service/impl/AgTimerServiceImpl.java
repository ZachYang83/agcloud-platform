package com.augurit.agcloud.agcom.agsupport.sc.timerService.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import com.augurit.agcloud.agcom.agsupport.domain.AgTimerService;
import com.augurit.agcloud.agcom.agsupport.mapper.AgTimerServiceMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dic.service.IAgDic;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.timerService.service.IAgTimerService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.common.dbcp.DBHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by caokp on 2017-08-29.
 */
@Service
public class AgTimerServiceImpl implements IAgTimerService {

    @Autowired
    private IAgDic iAgDic;

    @Autowired
    private AgTimerServiceMapper agTimerServiceMapper;

    @Override
    public AgTimerService findTimerServiceById(String id) throws Exception {

        return agTimerServiceMapper.findById(id);
    }

    @Override
    public PageInfo<AgTimerService> findTimerServiceList(AgTimerService agTimerService, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AgTimerService> list = agTimerServiceMapper.findList(agTimerService);
        return new PageInfo<AgTimerService>(list);
    }

    @Override
    public void save(MultipartFile file, AgTimerService agTimerService) throws Exception {
        try {
            if (file != null) {
                String uploadPath = "C:/upload";//默认上传至C:/upload文件夹
                AgDic agDic = iAgDic.findAgDicByCode("A015001");
                if (agDic != null && StringUtils.isNotEmpty(agDic.getValue())) uploadPath = agDic.getValue();
                File tempDirPath = new File(uploadPath);
                if (!tempDirPath.exists()) {
                    tempDirPath.mkdirs();
                }
                String fileName = file.getOriginalFilename();
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                String tempName = UUID.randomUUID().toString() + suffixName;
                File itemFile = new File(uploadPath + File.separator + tempName);
                file.transferTo(itemFile);
                agTimerService.setBatPath(uploadPath + File.separator + tempName);
            }
            if (StringUtils.isNotEmpty(agTimerService.getId())) {
                if (file != null) {
                    AgTimerService temp = this.findTimerServiceById(agTimerService.getId());
                    File bat = new File(temp.getBatPath());
                    if (bat.exists()) bat.delete();
                }
                agTimerServiceMapper.update(agTimerService);
            } else {
                agTimerService.setId(UUID.randomUUID().toString());
                agTimerService.setState("0");
                agTimerServiceMapper.save(agTimerService);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(AgTimerService agTimerService) throws Exception {
        if (agTimerService == null) return;
        agTimerServiceMapper.update(agTimerService);
    }

    @Override
    public void delete(String id) throws Exception {
        AgTimerService temp = this.findTimerServiceById(id);
        if (temp != null) {
            if (StringUtils.isNotEmpty(temp.getBatPath())) {
                File file = new File(temp.getBatPath());
                if (file.exists()) file.delete();
            }
            agTimerServiceMapper.delete(id);
        }
    }

    @Override
    public void deleteBatch(String[] ids) throws Exception {
        if (ids == null || ids.length < 0) return;
        for (String id : ids) {
            this.delete(id);
        }
    }

    @Override
    public void timerRun(AgTimerService agTimerService) throws Exception {
        if (agTimerService == null) return;
        agTimerService.setState("1");
        this.update(agTimerService);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    Map map = DBHelper.findFirst("select * from ag_timer_service where id=?", Arrays.asList(agTimerService.getId()));
                    if (map.containsKey("state") && "0".equals(map.get("state").toString())) {
                        timer.cancel();
                        return;
                    }
                    if (map.containsKey("circulated") && "0".equals(map.get("circulated").toString())) {
                        DBHelper.executeUpdate("update ag_timer_service set state=? where id=?", Arrays.asList("0", agTimerService.getId()));
                    }
                    String cmd = "cmd.exe /C start " + agTimerService.getBatPath();
                    Runtime.getRuntime().exec(cmd);//执行批处理文件
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        long delay = 0, timeInterval = 0;

        String type = agTimerService.getType();
        if ("delay".equals(type)) {
            delay = this.getSecond(agTimerService.getDelay()) * 1000;
            if ("1".equals(agTimerService.getCirculated())) {
                timeInterval = this.getSecond(agTimerService.getTimeInterval()) * 1000;
                timer.scheduleAtFixedRate(task, delay, timeInterval);
            } else {
                timer.schedule(task, delay);
            }
        } else if ("timing".equals(type)) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormatter.parse(agTimerService.getTiming());
            if ("1".equals(agTimerService.getCirculated())) {
                timeInterval = this.getSecond(agTimerService.getTimeInterval()) * 1000;
                timer.scheduleAtFixedRate(task, date, timeInterval);
            } else {
                timer.schedule(task, date);
            }
        }
    }

    public long getSecond(String time) {
        String[] times = time.split(":");
        int hour = Integer.parseInt(times[0]);
        int min = Integer.parseInt(times[1]);
        int sec = Integer.parseInt(times[2]);

        return hour * 3600 + min * 60 + sec;
    }
}
