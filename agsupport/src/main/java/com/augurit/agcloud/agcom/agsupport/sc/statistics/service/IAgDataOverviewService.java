package com.augurit.agcloud.agcom.agsupport.sc.statistics.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgDataOverview;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author zhangmingyang
 * @Description: 数据登记
 * @date 2019-08-22 18:33
 */
public interface IAgDataOverviewService {
    int deleteById(String id);
    AgDataOverview selectById(String id);
    int insert(AgDataOverview record);
    int insertBatch(List<AgDataOverview> list) throws Exception;
    int updateAgDataOverview(AgDataOverview record);
    PageInfo<AgDataOverview> findAll(AgDataOverview agDataOverview, Page page);

    void deleteBySubjectIds(String[] ids);
}
