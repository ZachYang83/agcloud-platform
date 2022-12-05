package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserMessage;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserMessageExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgUserMessageMapper {
    int countByExample(AgUserMessageExample example);

    int deleteByExample(AgUserMessageExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgUserMessage record);

    int insertSelective(AgUserMessage record);

    List<AgUserMessage> selectByExample(AgUserMessageExample example);

    AgUserMessage selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgUserMessage record, @Param("example") AgUserMessageExample example);

    int updateByExample(@Param("record") AgUserMessage record, @Param("example") AgUserMessageExample example);

    int updateByPrimaryKeySelective(AgUserMessage record);

    int updateByPrimaryKey(AgUserMessage record);
}