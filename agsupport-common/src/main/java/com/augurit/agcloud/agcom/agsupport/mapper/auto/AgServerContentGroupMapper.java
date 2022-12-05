package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerContentGroup;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerContentGroupExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 *
 * @Author: libc
 * @Date: 2020/10/16
 * @tips: mapper接口
 */
@Mapper
public interface AgServerContentGroupMapper {
    int countByExample(AgServerContentGroupExample example);

    int deleteByExample(AgServerContentGroupExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgServerContentGroup record);

    int insertSelective(AgServerContentGroup record);

    List<AgServerContentGroup> selectByExample(AgServerContentGroupExample example);

    AgServerContentGroup selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgServerContentGroup record, @Param("example") AgServerContentGroupExample example);

    int updateByExample(@Param("record") AgServerContentGroup record, @Param("example") AgServerContentGroupExample example);

    int updateByPrimaryKeySelective(AgServerContentGroup record);

    int updateByPrimaryKey(AgServerContentGroup record);
}