package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerEffect;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerEffectExample;
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
public interface AgServerEffectMapper {
    int countByExample(AgServerEffectExample example);

    int deleteByExample(AgServerEffectExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgServerEffect record);

    int insertSelective(AgServerEffect record);

    List<AgServerEffect> selectByExample(AgServerEffectExample example);

    AgServerEffect selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgServerEffect record, @Param("example") AgServerEffectExample example);

    int updateByExample(@Param("record") AgServerEffect record, @Param("example") AgServerEffectExample example);

    int updateByPrimaryKeySelective(AgServerEffect record);

    int updateByPrimaryKey(AgServerEffect record);
}