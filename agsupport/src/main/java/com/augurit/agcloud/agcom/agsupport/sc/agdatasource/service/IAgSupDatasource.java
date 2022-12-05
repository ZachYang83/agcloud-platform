package com.augurit.agcloud.agcom.agsupport.sc.agdatasource.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgSupDatasource;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author zhangmingyang
 * @Description: 操作数据源接口
 * @date 2019-01-11 11:15
 */
public interface IAgSupDatasource {
    int deleteDataSourceById(String id) throws Exception;
    int insert(AgSupDatasource record) throws Exception;
    AgSupDatasource selectDataSourceById(String id) throws Exception;
    AgSupDatasource selectDataSourceByName(String name) throws Exception;
    int updateDataSource(AgSupDatasource record) throws Exception;
    PageInfo<AgSupDatasource> findList(String name, Page page) throws Exception;

    List<AgSupDatasource> findAllList() throws Exception;

    /**
     * 查询所有矢量图层
     * @return
     * @throws Exception
     */
    List<AgLayer> findVectorList() throws Exception;
}
