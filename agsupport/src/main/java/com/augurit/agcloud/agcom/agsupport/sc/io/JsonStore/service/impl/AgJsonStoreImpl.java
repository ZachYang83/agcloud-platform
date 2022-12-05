package com.augurit.agcloud.agcom.agsupport.sc.io.JsonStore.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgJsonStore;
import com.augurit.agcloud.agcom.agsupport.mapper.AgJsonStoreMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.io.JsonStore.service.IAgJsonStore;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName AgJsonStoreImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2019/9/23 14:49
 * @Version 1.0
 **/
@Service
public class AgJsonStoreImpl implements IAgJsonStore {

    @Autowired
    AgJsonStoreMapper agJsonStoreMapper;

    /**
     * 根据id获得image
     *
     * @param id
     * @return
     */
    @Override
    public AgJsonStore getById(String id) throws RuntimeException {
        if (StringUtils.isBlank(id)) {
            throw new RuntimeException("getById is not null");
        }
        return agJsonStoreMapper.getById(id);
    }

    /**
     * 获得所有image
     *
     * @return
     * @throws RuntimeException
     */
    @Override
    public List<AgJsonStore> getAll() throws RuntimeException {
        return agJsonStoreMapper.getAll();
    }

    /**
     * 删除一条数据
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(String id) throws Exception {
        if (agJsonStoreMapper.deleteById(id) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 删除更多数据
     *
     * @param stringList
     * @return
     * @throws RuntimeException
     */
    @Override
    public boolean deleteMany(List<String> stringList) throws Exception {
        if(agJsonStoreMapper.deleteMany(stringList) > 0){
            return true;
        }
        return false;
    }

    /**
     * 添加一条信息
     *
     * @param agJsonStore
     * @return
     * @throws RuntimeException
     */
    @Override
    public boolean save(AgJsonStore agJsonStore) throws RuntimeException {
        return agJsonStoreMapper.save(agJsonStore) > 0 ? true : false;
    }

    /**
     * 编辑一条信息
     *
     * @param agJsonStore
     * @return
     * @throws RuntimeException
     */
    @Override
    public boolean update(AgJsonStore agJsonStore) throws RuntimeException {
        return agJsonStoreMapper.update(agJsonStore) > 0 ? true : false;
    }

    /**
     * 特殊条件查询列表
     *
     * @param agJsonStore
     * @param page
     * @return
     * @throws RuntimeException
     */
    @Override
    public PageInfo<AgJsonStore> getByDomainAndUsage(AgJsonStore agJsonStore, Page page) throws RuntimeException {
        PageHelper.startPage(page);
        List<AgJsonStore> list = agJsonStoreMapper.getByDomainOrUsage(agJsonStore);
        return new PageInfo<AgJsonStore>(list);
    }


}
