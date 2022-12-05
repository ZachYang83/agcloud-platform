package com.augurit.agcloud.agcom.agsupport.sc.statistics.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgDataSituation;

import java.util.List;
import java.util.Map;

/**
 * @author zhangmingyang
 * @Description: 数据概览接口
 * @date 2019-08-20 14:08
 */
public interface IAgStatisticsService {
    Integer layerCount() throws Exception;

    List<Map> countByDataType() throws Exception;

    List<Map> countByDataSource() throws Exception;

    Long getResourceSize();

    List<Map> getResourceSizeBySubjectType();

    int insert(AgDataSituation agDataSituation);

    List<Map> getResourceSituationByTime() throws Exception;

    AgDataSituation statisticsCountAndSize();

    List<AgDataSituation> findAll();
}
