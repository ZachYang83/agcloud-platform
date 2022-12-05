package com.augurit.agcloud.agcom.agsupport.sc.counties.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgCounties;
import com.augurit.agcloud.agcom.agsupport.mapper.AgCountiesMapper;
import com.augurit.agcloud.agcom.agsupport.sc.counties.service.IAgCounties;
import com.common.util.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by czh on 2018-05-29.
 */
@Service
public class AgCountiesImpl implements IAgCounties {

    @Autowired
    public AgCountiesMapper agCountiesMapper;

    public List<AgCounties> getCounties() {
        try {
            return agCountiesMapper.findCounties();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<AgCounties> getTown(String xzqhdmCounties) {
        try {
            if (!Common.isCheckNull(xzqhdmCounties)) {
                return agCountiesMapper.findTown(xzqhdmCounties);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
