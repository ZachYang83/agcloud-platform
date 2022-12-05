package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgMaterialsComponent;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgMaterialsComponentExample;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgMaterialsComponentWithBLOBs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgMaterialsComponentMapper {
    int countByExample(AgMaterialsComponentExample example);

    int deleteByExample(AgMaterialsComponentExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgMaterialsComponentWithBLOBs record);

    int insertSelective(AgMaterialsComponentWithBLOBs record);

    List<AgMaterialsComponentWithBLOBs> selectByExampleWithBLOBs(AgMaterialsComponentExample example);

    List<AgMaterialsComponent> selectByExample(AgMaterialsComponentExample example);

    AgMaterialsComponentWithBLOBs selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgMaterialsComponentWithBLOBs record, @Param("example") AgMaterialsComponentExample example);

    int updateByExampleWithBLOBs(@Param("record") AgMaterialsComponentWithBLOBs record, @Param("example") AgMaterialsComponentExample example);

    int updateByExample(@Param("record") AgMaterialsComponent record, @Param("example") AgMaterialsComponentExample example);

    int updateByPrimaryKeySelective(AgMaterialsComponentWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(AgMaterialsComponentWithBLOBs record);

    int updateByPrimaryKey(AgMaterialsComponent record);
}