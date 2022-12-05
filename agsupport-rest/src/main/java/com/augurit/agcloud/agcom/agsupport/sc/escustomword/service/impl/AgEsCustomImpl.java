package com.augurit.agcloud.agcom.agsupport.sc.escustomword.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgEsCustomWord;
import com.augurit.agcloud.agcom.agsupport.mapper.AgEsCustomWordMapper;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.agcom.agsupport.sc.escustomword.service.IAgEsCustomWord;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :13:05 2018/12/23
 * @Modified By:
 */
@Service
public class AgEsCustomImpl implements IAgEsCustomWord{



    @Autowired
    private AgEsCustomWordMapper agEsCustomWordMapper;

    @Override
    public List<AgEsCustomWord> findAll() throws Exception {
        return agEsCustomWordMapper.findAll();
    }

    @Override
    public AgEsCustomWord findCustom(String customWord) throws Exception {
        return agEsCustomWordMapper.findCustom(customWord);
    }

    @Override
    public PageInfo<AgEsCustomWord> searchAgEsCustomWord(AgEsCustomWord agEsCustomWord, Page page) throws Exception {
        PageInfo<AgEsCustomWord> servicesAgEsCustomWord = null;
        List<AgEsCustomWord> list = null;
        PageHelper.startPage(page);
        list = agEsCustomWordMapper.findList(agEsCustomWord);
        servicesAgEsCustomWord = new PageInfo<AgEsCustomWord>(list);
        return servicesAgEsCustomWord;
    }

    @Override
    public AgEsCustomWord findById(String id) throws Exception {
        return agEsCustomWordMapper.findById(id);
    }

    @Override
    public AgEsCustomWord findByCustomWord(String customWord) throws Exception {
        return agEsCustomWordMapper.findByCustomWord(customWord);
    }

    @Override
    public void saveAgEsCustomWord(AgEsCustomWord agEsCustomWord) throws Exception {
        agEsCustomWordMapper.save(agEsCustomWord);
    }

    @Override
    public void updataAgEsCustomWord(AgEsCustomWord agEsCustomWord) throws Exception {
        agEsCustomWordMapper.update(agEsCustomWord);
    }

    @Override
    public void delById(String id) throws Exception {
        agEsCustomWordMapper.delById(id);
    }

    @Override
    public void batchDelById(String[] ids) throws Exception {
        agEsCustomWordMapper.batchDelById(ids);
    }

    @Override
    public AgEsCustomWord getLatestTime() throws Exception {
        return agEsCustomWordMapper.getLatestTime();
    }
}
