package com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.util.UploadUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgAppSystem;
import com.augurit.agcloud.agcom.agsupport.mapper.AgAppSystemMapper;
import com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.controller.AgAppSystemController;
import com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.service.IAppSystemService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * @author zhangmy
 * @Description: 实现类
 * @date 2019-10-14 16:02
 */
@Service
public class AppSystemServiceImpl implements IAppSystemService {
    @Autowired
    private AgAppSystemMapper agAppSystemMapper;

    @Override
    @Transactional
    public int deleteById(String id) {
        AgAppSystem agAppSystem = agAppSystemMapper.findById(id);
        int n = agAppSystemMapper.deleteById(id);
        String iconAddr = agAppSystem.getIconAddr();
        if (StringUtils.isNotBlank(iconAddr)) {
            String filePath = UploadUtil.getUploadAbsolutePath() + AgAppSystemController.systemIconPath;
            File file = new File(filePath + iconAddr);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
        return n;
    }

    @Override
    public int deleteByIds(List<String> ids) {
        for (String id : ids) {
            AgAppSystem agAppSystem = agAppSystemMapper.findById(id);
            String iconAddr = agAppSystem.getIconAddr();
            if (StringUtils.isNotBlank(iconAddr)) {
                String filePath = UploadUtil.getUploadAbsolutePath() + AgAppSystemController.systemIconPath;
                File file = new File(filePath + iconAddr);
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
            }
        }
        return agAppSystemMapper.deleteByIds(ids);
    }

    @Override
    public int insert(AgAppSystem record) {
        return agAppSystemMapper.insert(record);
    }

    @Override
    public AgAppSystem findById(String id) {
        AgAppSystem agAppSystem = agAppSystemMapper.findById(id);
        String path = UploadUtil.getUploadRelativePath() + AgAppSystemController.systemIconPath;
        if (StringUtils.isNotBlank(agAppSystem.getIconAddr())) {
            String iconPath = path + agAppSystem.getIconAddr();
            agAppSystem.setIconAddr(iconPath);
        }
        return agAppSystem;
    }

    @Override
    public List<AgAppSystem> findByIds(List<String> list) {
        List<AgAppSystem> agAppSystems = agAppSystemMapper.findByIds(list);
        String path = UploadUtil.getUploadRelativePath() + AgAppSystemController.systemIconPath;
        for (AgAppSystem agAppSystem : agAppSystems) {
            if (StringUtils.isNotBlank(agAppSystem.getIconAddr())) {
                String iconPath = path + agAppSystem.getIconAddr();
                agAppSystem.setIconAddr(iconPath);
            }
        }
        return agAppSystems;
    }

    @Override
    public int updateAppSystem(AgAppSystem record) {
        return agAppSystemMapper.updateAppSystem(record);
    }

    @Override
    public PageInfo<AgAppSystem> findAll(String appSystemName, Page page) {
        PageHelper.startPage(page);
        List<AgAppSystem> list = agAppSystemMapper.findAllRest(appSystemName, "");
        String path = UploadUtil.getUploadRelativePath() + AgAppSystemController.systemIconPath;
        for (AgAppSystem agAppSystem : list) {
            if (StringUtils.isNotBlank(agAppSystem.getIconAddr())) {
                String iconPath = path + agAppSystem.getIconAddr();
                agAppSystem.setIconAddr(iconPath);
            }
        }
        return new PageInfo(list);
    }

    @Override
    public List<AgAppSystem> findAll(String appName, String authorizeStatus) {
        if (StringUtils.isBlank(appName)) {
            appName = "";
        }
        if (StringUtils.isBlank(authorizeStatus)) {
            authorizeStatus = "";
        }
        List<AgAppSystem> list = agAppSystemMapper.findAllRest(appName, authorizeStatus);
        String path = UploadUtil.getUploadRelativePath() + AgAppSystemController.systemIconPath;
        for (AgAppSystem agAppSystem : list) {
            if (StringUtils.isNotBlank(agAppSystem.getIconAddr())) {
                String iconPath = path + agAppSystem.getIconAddr();
                agAppSystem.setIconAddr(iconPath);
            }
        }
        return list;
    }
}
