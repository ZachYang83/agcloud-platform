package com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimApi;
import com.augurit.agcloud.agcom.agsupport.mapper.AgBimApiMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IBimApi;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName: BimApiImpl
 * @Description: 第三方接口实现类
 * @Author: zhangsj
 * @Date: Create in 2020/03/20 13:39
 **/

@Transactional
@Service
public class BimApiImpl implements IBimApi {

    @Autowired
    AgBimApiMapper agBimApiMapper;

    /**
     * 根据Id新增api
     * @param agBimApi
     * @return
     * @throws RuntimeException
     */
    @Override
    public boolean addAgBimApi(AgBimApi agBimApi) throws RuntimeException {
        return agBimApiMapper.add(agBimApi) > 0 ? true : false;
    }

    /**
     * 删除一条数据
     * @param id
     * @return
     * @throws RuntimeException
     */
    @Override
    public boolean deleteById(String id) throws RuntimeException {
        if (StringUtils.isBlank(id)){
            throw new RuntimeException("Id is null");
        }
        return agBimApiMapper.deleteById(id) > 0 ? true : false;
    }

    @Override
    public boolean deleteMany(List<String> stringList) throws Exception {
        return agBimApiMapper.deleteMany(stringList) > 0 ? true : false;
    }

    @Override
    public AgBimApi getById(String id) throws RuntimeException {
        if (StringUtils.isBlank(id)){
            throw new RuntimeException("Id is null");
        }
        return agBimApiMapper.getById(id);
    }

    @Override
    public List<AgBimApi> getAll() throws RuntimeException {
        return agBimApiMapper.getAll();
    }

    @Override
    public boolean update(AgBimApi agBimApi) throws RuntimeException {
        return agBimApiMapper.update(agBimApi) > 0 ? true : false;
    }

    @Override
    public PageInfo<AgBimApi> getByNameOrUrl(String name, String url, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AgBimApi> list = agBimApiMapper.getByNameOrUrl(name, url);
        return new PageInfo<AgBimApi>(list);
    }

}


