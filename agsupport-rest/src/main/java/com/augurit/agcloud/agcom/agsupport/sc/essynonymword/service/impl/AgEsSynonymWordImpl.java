package com.augurit.agcloud.agcom.agsupport.sc.essynonymword.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgEsSynonymWord;
import com.augurit.agcloud.agcom.agsupport.mapper.AgEsSynonymWordMapper;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.agcom.agsupport.sc.essynonymword.service.IAgEsSynonymWord;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :14:22 2018/12/23
 * @Modified By:
 */

@Service
public class AgEsSynonymWordImpl implements IAgEsSynonymWord {

    @Autowired
    private AgEsSynonymWordMapper agEsSynonymWordMapper;

    @Override
    public List<AgEsSynonymWord> findAll() throws Exception {
        return agEsSynonymWordMapper.findAll();
    }

    @Override
    public AgEsSynonymWord findWord(String word) throws Exception {
        return agEsSynonymWordMapper.findWord(word);
    }

    @Override
    public PageInfo<AgEsSynonymWord> searchAgEsSynonymWord(AgEsSynonymWord agEsSynonymWord, Page page) throws Exception {
        PageHelper.startPage(page);
        PageInfo<AgEsSynonymWord> servicesAgEsSynonymWord = null;
        List<AgEsSynonymWord> list = null;
        list = agEsSynonymWordMapper.findList(agEsSynonymWord);
        servicesAgEsSynonymWord = new PageInfo<AgEsSynonymWord>(list);
        return servicesAgEsSynonymWord;
    }
    @Override
    public AgEsSynonymWord findById(String id) throws Exception {
        return agEsSynonymWordMapper.findById(id);
    }

    @Override
    public AgEsSynonymWord findByWord(String word) throws Exception {
        return agEsSynonymWordMapper.findByWord(word);
    }

    @Override
    public void saveAgEsSynonymWord(AgEsSynonymWord agEsSynonymWord) throws Exception {
        agEsSynonymWordMapper.save(agEsSynonymWord);
    }
    @Override
    public void updataAgEsSynonymWord(AgEsSynonymWord agEsSynonymWord) throws Exception {
        agEsSynonymWordMapper.update(agEsSynonymWord);
    }

    @Override
    public void delById(String id) throws Exception {

        agEsSynonymWordMapper.delById(id);
    }

    @Override
    public void batchDelById(String[] ids) throws Exception {
        agEsSynonymWordMapper.batchDelById(ids);
    }

    @Override
    public AgEsSynonymWord getLatestTime() throws Exception {
        return agEsSynonymWordMapper.getLatestTime();
    }
}
