package com.augurit.agcloud.agcom.agsupport.sc.agdatasource.service.impl;

import com.augurit.agcloud.agcom.syslog.service.annotation.SysLog;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.agcom.agsupport.common.util.DESedeUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgSupDatasource;
import com.augurit.agcloud.agcom.agsupport.mapper.AgLayerMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgSupDatasourceMapper;
import com.augurit.agcloud.agcom.agsupport.sc.agdatasource.service.IAgSupDatasource;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhangmingyang
 * @Description: TODO
 * @date 2019-01-11 11:20
 */
@Service
public class IAgSupDatasourceImpl implements IAgSupDatasource {

    @Autowired
    private AgSupDatasourceMapper agSupDatasourceMapper;

    @Autowired
    private AgLayerMapper agLayerMapper;
    @Override
    public int deleteDataSourceById(String id) throws Exception {
        return agSupDatasourceMapper.deleteByPrimaryKey(id);
    }

    @Override
    @SysLog(sysName = "地图运维",funcName = "新增数据源")
    public int insert(AgSupDatasource record) throws Exception {
        return agSupDatasourceMapper.insert(record);
    }

    @Override
    public AgSupDatasource selectDataSourceById(String id) throws Exception {
        return agSupDatasourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public AgSupDatasource selectDataSourceByName(String name) throws Exception {
        return agSupDatasourceMapper.selectByDataSourceName(name);
    }


    @Override
    @SysLog(sysName = "地图运维",funcName = "修改数据源")
    public int updateDataSource(AgSupDatasource record) throws Exception {
        return agSupDatasourceMapper.updateDataSource(record);
    }

    @Override
    public PageInfo<AgSupDatasource> findList(String name, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AgSupDatasource> list = agSupDatasourceMapper.findList(name);
        return new PageInfo<AgSupDatasource>(list);
    }

    @Override
    public List<AgLayer> findVectorList() throws Exception {
        return agLayerMapper.findVectorList();
    }

    @Override
    public List<AgSupDatasource> findAllList() throws Exception {
        return agSupDatasourceMapper.findAllList();
    }
}
