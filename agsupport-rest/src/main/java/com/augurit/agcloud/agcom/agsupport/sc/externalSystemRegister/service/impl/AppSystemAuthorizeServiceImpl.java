package com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgUserThirdapp;
import com.augurit.agcloud.agcom.agsupport.mapper.AgUserThirdappMapper;
import com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.service.IAppSystemAuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhangmy
 * @Description: TODO
 * @date 2019-10-22 10:09
 */
@Service
public class AppSystemAuthorizeServiceImpl implements IAppSystemAuthorizeService{
    @Autowired
    private AgUserThirdappMapper agUserThirdappMapper;
    @Override
    public AgUserThirdapp findById(String id) {
        return agUserThirdappMapper.findById(id);
    }

    @Override
    public int update(AgUserThirdapp record) {
        return agUserThirdappMapper.update(record);
    }

    @Override
    public AgUserThirdapp findByAppIdAndUserId(String appId, String userId) {
        return agUserThirdappMapper.findByAppIdAndUserId(appId,userId);
    }

    @Override
    @Transactional
    public void moveSys(AgUserThirdapp appSys, AgUserThirdapp targetSys) {
        agUserThirdappMapper.update(appSys);
        agUserThirdappMapper.update(targetSys);
    }

    @Override
    public List<AgUserThirdapp> findByUserId(String userId) {
        return agUserThirdappMapper.findByUserId(userId);
    }

    @Override
    public List<AgUserThirdapp> findByUserIdAndAppIds(String userId, String[] appIds) {
        return agUserThirdappMapper.findByUserIdAndAppIds(userId,appIds);
    }

    @Override
    public void updateBatch(List<AgUserThirdapp> list) {
        agUserThirdappMapper.updateBatch(list);
    }
}
