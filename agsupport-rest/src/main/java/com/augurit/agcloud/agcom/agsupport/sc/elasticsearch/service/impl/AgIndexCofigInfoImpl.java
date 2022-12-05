package com.augurit.agcloud.agcom.agsupport.sc.elasticsearch.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgIndexConfigInfo;
import com.augurit.agcloud.agcom.agsupport.mapper.AgIndexConfigInfoMapper;
import com.augurit.agcloud.agcom.agsupport.sc.elasticsearch.service.IAgIndexConfigInfo;
import com.github.pagehelper.Page;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :10:05 2018/12/27
 * @Modified By:
 */

@Service
public class AgIndexCofigInfoImpl implements IAgIndexConfigInfo {
    @Autowired
    private AgIndexConfigInfoMapper agIndexConfigInfoMapper;

    @Override
    public PageInfo<AgIndexConfigInfo> searchAgIndexConfigInfo(AgIndexConfigInfo agIndexConfigInfo, Page page) throws Exception {
        PageInfo<AgIndexConfigInfo> servicesAgIndexConfigInfo = null;
        List<AgIndexConfigInfo> list = null;
        PageHelper.startPage(page);
        list = agIndexConfigInfoMapper.findList(agIndexConfigInfo);
        servicesAgIndexConfigInfo = new PageInfo<AgIndexConfigInfo>(list);
        return servicesAgIndexConfigInfo;
    }

    @Override
    public AgIndexConfigInfo findById(String id) throws Exception {
        return agIndexConfigInfoMapper.findById(id);
    }

    @Override
    public AgIndexConfigInfo findByIndexName(String indexName) throws Exception {
        return agIndexConfigInfoMapper.findByIndexName(indexName);
    }

    @Override
    public void updateAgIndexConfigInfo(AgIndexConfigInfo agIndexConfigInfo) throws Exception {
        agIndexConfigInfoMapper.update(agIndexConfigInfo);
    }

    @Override
    public void updateShowField(AgIndexConfigInfo showField) throws Exception {
        agIndexConfigInfoMapper.updateShowField(showField);
    }

    @Override
    public void saveAgIndexConfigInfo(AgIndexConfigInfo agIndexConfigInfo) throws Exception {
        agIndexConfigInfoMapper.save(agIndexConfigInfo);
    }

    @Override
    public void delById(String id) throws Exception {
        agIndexConfigInfoMapper.delById(id);
    }

    @Override
    public List<AgIndexConfigInfo> getAllShowField() throws Exception {
        return agIndexConfigInfoMapper.getAllShowField();
    }
}
