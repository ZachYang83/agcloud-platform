package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetStore;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetStoreExample;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetStoreWithBLOBs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgWidgetStoreMapper {
    int countByExample(AgWidgetStoreExample example);

    int deleteByExample(AgWidgetStoreExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgWidgetStoreWithBLOBs record);

    int insertSelective(AgWidgetStoreWithBLOBs record);

    List<AgWidgetStoreWithBLOBs> selectByExampleWithBLOBs(AgWidgetStoreExample example);

    List<AgWidgetStore> selectByExample(AgWidgetStoreExample example);

    AgWidgetStoreWithBLOBs selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgWidgetStoreWithBLOBs record, @Param("example") AgWidgetStoreExample example);

    int updateByExampleWithBLOBs(@Param("record") AgWidgetStoreWithBLOBs record, @Param("example") AgWidgetStoreExample example);

    int updateByExample(@Param("record") AgWidgetStore record, @Param("example") AgWidgetStoreExample example);

    int updateByPrimaryKeySelective(AgWidgetStoreWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(AgWidgetStoreWithBLOBs record);

    int updateByPrimaryKey(AgWidgetStore record);
}