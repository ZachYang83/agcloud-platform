package com.augurit.agcloud.agcom.agsupport.sc.defaultvalue.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgDefaultValue;
import com.augurit.agcloud.agcom.agsupport.mapper.AgDefaultValueMapper;
import com.augurit.agcloud.agcom.agsupport.sc.defaultvalue.service.IAgDefaultValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :15:07 2019/3/19
 * @Modified By:
 */
@Service
public class AgDefaultValueImpl implements IAgDefaultValue {
    @Autowired
    private AgDefaultValueMapper agDefaultValueMapper;

    @Override
    public void save(AgDefaultValue agDefaultValue) throws Exception {
        agDefaultValueMapper.save(agDefaultValue);
    }

    @Override
    public void update(AgDefaultValue agDefaultValue) throws Exception {
        agDefaultValueMapper.update(agDefaultValue);
    }

    @Override
    public AgDefaultValue findByKey(String key) throws Exception {
        return agDefaultValueMapper.findByKey(key);
    }
}
