package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgSysSetting;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgSysSettingExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgSysSettingMapper {
    int countByExample(AgSysSettingExample example);

    int deleteByExample(AgSysSettingExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgSysSetting record);

    int insertSelective(AgSysSetting record);

    List<AgSysSetting> selectByExample(AgSysSettingExample example);

    AgSysSetting selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgSysSetting record, @Param("example") AgSysSettingExample example);

    int updateByExample(@Param("record") AgSysSetting record, @Param("example") AgSysSettingExample example);

    int updateByPrimaryKeySelective(AgSysSetting record);

    int updateByPrimaryKey(AgSysSetting record);
}