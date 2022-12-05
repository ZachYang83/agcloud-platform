package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgDataSituation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangmingyang
 * @date 2019-08-26 10:08
 */
@Mapper
public interface AgDataSituationMapper {
    int insert(AgDataSituation agDataSituation);

    AgDataSituation statisticsCountAndSize();

    List<AgDataSituation> findAll();

    List<AgDataSituation> getResourceSituationByTime(@Param("startDay") String startDay, @Param("endDay") String endDay);
}
