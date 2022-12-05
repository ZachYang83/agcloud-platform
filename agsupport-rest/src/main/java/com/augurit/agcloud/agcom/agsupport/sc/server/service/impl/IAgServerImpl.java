package com.augurit.agcloud.agcom.agsupport.sc.server.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.util.DESedeUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgServer;
import com.augurit.agcloud.agcom.agsupport.mapper.AgServerMapper;
import com.augurit.agcloud.agcom.agsupport.sc.server.service.IAgServer;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.StringUtils;
import com.common.util.Common;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author zhangmingyang
 * @Description: 服务器管理实现接口类
 * @date 2018-11-12 15:57
 */
@Service
public class IAgServerImpl implements IAgServer {
    @Autowired
    private AgServerMapper agServerMapper;
    @Override
    public int deleteServerById(String id) throws Exception {
        return agServerMapper.deleteServerById(id);
    }

    @Override
    public int insert(AgServer record) throws Exception{
        return agServerMapper.insert(record);
    }

    @Override
    public AgServer selectServerById(String id) throws Exception{
        return agServerMapper.selectServerById(id);
    }

    @Override
    public int updateServer(AgServer record) throws Exception{
        if(record == null){
            return 0;
        }
        //if(!Common.isCheckNull(record.get))
        return agServerMapper.updateServer(record);
    }
    @Override
    public PageInfo<AgServer> findList(AgServer agServer , Page page) throws Exception{
        PageHelper.startPage(page);
        List<AgServer> list = agServerMapper.findList(agServer);
        for(AgServer eachAgServer : list){
            if(!Common.isCheckNull(eachAgServer.getToken())){
                eachAgServer.setAuthenticationMode(1);
            }else if(!Common.isCheckNull(eachAgServer.getUserName())||!Common.isCheckNull(eachAgServer.getPassword())){
                eachAgServer.setAuthenticationMode(0);
            }else{
                eachAgServer.setAuthenticationMode(2);
            }
        }
        return new PageInfo<AgServer>(list);
    }

    @Override
    public List<AgServer> findListByState(AgServer agServer) throws Exception {
        return agServerMapper.findList(agServer);
    }

    @Override
    public void save(AgServer agServer) throws Exception{
        if(agServer !=null) {
            agServer.setCreateTime(new Date());
            if (StringUtils.isNotBlank(agServer.getId())) {
                agServer.setCreateTime(new Date());
                setAuthenticationInfo(agServer);
                this.updateServer(agServer);
                //jsonObject = this.getLogInfo(request, "服务器管理--修改");
            } else {
                setAuthenticationInfo(agServer);
                agServer.setId(UUID.randomUUID().toString());
                this.insert(agServer);
                //jsonObject = this.getLogInfo(request, "服务器管理--新增");
            }
        }
    }


    private void setAuthenticationInfo(AgServer agServer) throws Exception{
        if(agServer != null){
            if(agServer.getAuthenticationMode()==0){
                //验证方式为账号密码
                if(StringUtils.isNotBlank(agServer.getId())) {
                    AgServer oldServer = agServerMapper.selectServerById(agServer.getId());//原Server实体信息
                    if(oldServer != null) {
                        if ((oldServer.getPassword() == null && agServer.getPassword() != null) ||
                                (oldServer.getPassword() != null && !oldServer.getPassword().equals(agServer.getPassword()))) {
                            //如果密码被修改过则重新加密
                            agServer.setPassword(DESedeUtil.desEncrypt(agServer.getPassword()));
                        }
                    }
                }
                else {
                    String passwordAfterEncrypt = DESedeUtil.desEncrypt(agServer.getPassword());
                    agServer.setPassword(passwordAfterEncrypt);
                }
                agServer.setToken(null);
            }else if(agServer.getAuthenticationMode()==1){
                //验证方式为token
                agServer.setUserName(null);
                agServer.setPassword(null);
            }else{
                agServer.setToken(null);
                agServer.setUserName(null);
                agServer.setPassword(null);
            }
        }
    }
}
