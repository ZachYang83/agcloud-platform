package com.augurit.agcloud.agcom.agsupport.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zhangmingyang
 * @Description: 数据概览
 * @date 2019-08-20 14:20
 */
@Mapper
public interface AgStatisticsMapper {
    Integer layerCount() throws Exception;

    List<Map> countByDataType() throws Exception;

    Long getResourceSize();

    Map getResourceSituationByDay(@Param("endDay") String endDay);

    List<Map> countByDataSource();

    Map countBySubjectIds(@Param("ids") List<String> ids);

    List<Map> countByDay();
}
