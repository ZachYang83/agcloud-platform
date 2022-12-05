package com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgAppSystem;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.agcom.agsupport.domain.AgUserThirdapp;
import com.augurit.agcloud.agcom.agsupport.mapper.AgUserThirdappMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.service.IAppSystemAuthorizeService;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private IAgUser iagUser;

    @Override
    public int deleteById(String id) {
        return agUserThirdappMapper.deleteById(id);
    }

    @Override
    public void deleteByIds(String[] userIds, String appId) {
        agUserThirdappMapper.deleteByIds(userIds,appId);
    }

    @Override
    public int insert(AgUserThirdapp record) {
        return agUserThirdappMapper.insert(record);
    }

    @Override
    public AgUserThirdapp findById(String id) {
        return agUserThirdappMapper.findById(id);
    }

    @Override
    public int update(AgUserThirdapp record) {
        return agUserThirdappMapper.update(record);
    }

    @Override
    public PageInfo<AgUser> findAppUser(String appId, String userName, Page page) throws Exception{
        PageHelper.startPage(page);
        List<AgUserThirdapp> list = agUserThirdappMapper.findByAppId(appId);
        if (list.size() == 0){
            return new PageInfo<AgUser>();
        }
        String userIds = "";
        for (AgUserThirdapp agUserThirdapp : list){
            String userId = agUserThirdapp.getUserId();
            userIds = userIds + userId + ",";
        }
        if (StringUtils.isNotBlank(userIds)){
            userIds = userIds.substring(0, userIds.length() - 1);
        }
        List<AgUser> agUsers = iagUser.findUsersByUserIds(userIds);
        List<AgUser> newagusers = new ArrayList<>();
        if (StringUtils.isNotBlank(userName)){
            for (AgUser agUser : agUsers){
                String userName1 = agUser.getUserName();
                if (userName1.equals(userName)){
                    newagusers.add(agUser);
                }
            }
            return new PageInfo<>(newagusers);
        }

        return new PageInfo<>(agUsers);
    }

    @Override
    public AgUserThirdapp findByAppIdAndUserId(String appId, String userId) {
        return agUserThirdappMapper.findByAppIdAndUserId(appId,userId);
    }

    @Override
    public Long findByMaxOrder() {
        return agUserThirdappMapper.findByMaxOrder();
    }

    @Override
    public List<AgUserThirdapp> findByAppId(String appId) {
        return agUserThirdappMapper.findByAppId(appId);
    }
}
