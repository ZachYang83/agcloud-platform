package com.augurit.agcloud.agcom.agsupport.mapper;


import com.augurit.agcloud.agcom.agsupport.domain.AgDataOverview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgDataOverviewMapper {
    int deleteById(String id);
    AgDataOverview selectById(String id);
    int insert(AgDataOverview record);
    int insertBatch(List<AgDataOverview> list);

    int updateAgDataOverview(AgDataOverview record);
    List<AgDataOverview> findAll(AgDataOverview agDataOverview);
    List<AgDataOverview> find();
    void deleteBySubjectIds(@Param("ids") String[] ids);

}