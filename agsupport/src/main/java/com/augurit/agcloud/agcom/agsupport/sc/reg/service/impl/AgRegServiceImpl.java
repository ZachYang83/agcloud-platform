package com.augurit.agcloud.agcom.agsupport.sc.reg.service.impl;

import com.augurit.agcloud.agcom.agsupport.mapper.AgMetadataMapper;
import com.augurit.agcloud.agcom.agsupport.sc.reg.service.IAgRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by caokp on 2017-08-08.
 */
@Service
public class AgRegServiceImpl implements IAgRegService {

    @Autowired
    private AgMetadataMapper agMetadataMapper;

    @Override
    public List<String> getYears() throws Exception {

        return agMetadataMapper.getYears();
    }
}
