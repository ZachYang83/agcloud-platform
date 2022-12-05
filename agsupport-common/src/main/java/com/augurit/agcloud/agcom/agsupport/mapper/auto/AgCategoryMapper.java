package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgCategory;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgCategoryExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgCategoryMapper {
    int countByExample(AgCategoryExample example);

    int deleteByExample(AgCategoryExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgCategory record);

    int insertSelective(AgCategory record);

    List<AgCategory> selectByExample(AgCategoryExample example);

    AgCategory selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgCategory record, @Param("example") AgCategoryExample example);

    int updateByExample(@Param("record") AgCategory record, @Param("example") AgCategoryExample example);

    int updateByPrimaryKeySelective(AgCategory record);

    int updateByPrimaryKey(AgCategory record);
}