package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgHouse;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgHouseExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgHouseMapper {
    int countByExample(AgHouseExample example);

    int deleteByExample(AgHouseExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgHouse record);

    int insertSelective(AgHouse record);

    List<AgHouse> selectByExample(AgHouseExample example);

    AgHouse selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgHouse record, @Param("example") AgHouseExample example);

    int updateByExample(@Param("record") AgHouse record, @Param("example") AgHouseExample example);

    int updateByPrimaryKeySelective(AgHouse record);

    int updateByPrimaryKey(AgHouse record);
}