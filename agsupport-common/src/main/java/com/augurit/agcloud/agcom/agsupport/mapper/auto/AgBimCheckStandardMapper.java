package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckStandard;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckStandardExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @Author: Zihui Li
 * @Date: 2020/12/24
 * @tips: mapper接口
 */
@Mapper
public interface AgBimCheckStandardMapper {
    int countByExample(AgBimCheckStandardExample example);

    int deleteByExample(AgBimCheckStandardExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgBimCheckStandard record);

    int insertSelective(AgBimCheckStandard record);

    List<AgBimCheckStandard> selectByExample(AgBimCheckStandardExample example);

    AgBimCheckStandard selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgBimCheckStandard record, @Param("example") AgBimCheckStandardExample example);

    int updateByExample(@Param("record") AgBimCheckStandard record, @Param("example") AgBimCheckStandardExample example);

    int updateByPrimaryKeySelective(AgBimCheckStandard record);

    int updateByPrimaryKey(AgBimCheckStandard record);
}