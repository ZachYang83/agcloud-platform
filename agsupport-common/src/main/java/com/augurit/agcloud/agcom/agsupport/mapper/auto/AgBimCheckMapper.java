package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheck;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @Author: qinyg
 * @Date: 2020/10/30
 * @tips: mapper接口
 */
@Mapper
public interface AgBimCheckMapper {
    int countByExample(AgBimCheckExample example);

    int deleteByExample(AgBimCheckExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgBimCheck record);

    int insertSelective(AgBimCheck record);

    List<AgBimCheck> selectByExample(AgBimCheckExample example);

    AgBimCheck selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgBimCheck record, @Param("example") AgBimCheckExample example);

    int updateByExample(@Param("record") AgBimCheck record, @Param("example") AgBimCheckExample example);

    int updateByPrimaryKeySelective(AgBimCheck record);

    int updateByPrimaryKey(AgBimCheck record);
}