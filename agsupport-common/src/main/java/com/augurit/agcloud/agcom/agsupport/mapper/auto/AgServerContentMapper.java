package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerContent;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerContentExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AgServerContentMapper {
    int countByExample(AgServerContentExample example);

    int deleteByExample(AgServerContentExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgServerContent record);

    int insertSelective(AgServerContent record);

    List<AgServerContent> selectByExample(AgServerContentExample example);

    AgServerContent selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgServerContent record, @Param("example") AgServerContentExample example);

    int updateByExample(@Param("record") AgServerContent record, @Param("example") AgServerContentExample example);

    int updateByPrimaryKeySelective(AgServerContent record);

    int updateByPrimaryKey(AgServerContent record);
}