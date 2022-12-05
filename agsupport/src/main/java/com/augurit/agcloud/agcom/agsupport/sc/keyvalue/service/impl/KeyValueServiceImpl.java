package com.augurit.agcloud.agcom.agsupport.sc.keyvalue.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgKeyValue;
import com.augurit.agcloud.agcom.agsupport.mapper.AgKeyValueMapper;
import com.augurit.agcloud.agcom.agsupport.sc.keyvalue.service.KeyValueService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyValueServiceImpl implements KeyValueService{

    @Autowired
    private AgKeyValueMapper agKeyValueMapper;
    @Override
    public PageInfo<AgKeyValue> findKeyValueList(String domain, Page page) {
        PageHelper.startPage(page);
        List<AgKeyValue> list = agKeyValueMapper.findKeyValueList(domain);
        return new PageInfo<AgKeyValue>(list);
    }

    @Override
    public void update(AgKeyValue keyvalue) {
        agKeyValueMapper.updateKeyValue(keyvalue);
    }

    @Override
    public void save(AgKeyValue keyvalue) {
        agKeyValueMapper.save(keyvalue);
    }

    @Override
    public void delete(List<String> ids) {
        for(String id:ids){
            agKeyValueMapper.deleteByPrimaryKey(id);
        }
    }
}
