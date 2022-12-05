package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerEffectGroup;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerEffectGroupExample;
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
public interface AgServerEffectGroupMapper {
    int countByExample(AgServerEffectGroupExample example);

    int deleteByExample(AgServerEffectGroupExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgServerEffectGroup record);

    int insertSelective(AgServerEffectGroup record);

    List<AgServerEffectGroup> selectByExample(AgServerEffectGroupExample example);

    AgServerEffectGroup selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgServerEffectGroup record, @Param("example") AgServerEffectGroupExample example);

    int updateByExample(@Param("record") AgServerEffectGroup record, @Param("example") AgServerEffectGroupExample example);

    int updateByPrimaryKeySelective(AgServerEffectGroup record);

    int updateByPrimaryKey(AgServerEffectGroup record);
}