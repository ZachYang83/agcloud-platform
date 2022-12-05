package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgPermission;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgPermissionExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgPermissionMapper {
    int countByExample(AgPermissionExample example);

    int deleteByExample(AgPermissionExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgPermission record);

    int insertSelective(AgPermission record);

    List<AgPermission> selectByExample(AgPermissionExample example);

    AgPermission selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgPermission record, @Param("example") AgPermissionExample example);

    int updateByExample(@Param("record") AgPermission record, @Param("example") AgPermissionExample example);

    int updateByPrimaryKeySelective(AgPermission record);

    int updateByPrimaryKey(AgPermission record);
}