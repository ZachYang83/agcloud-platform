package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetAssetsColumns;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetAssetsColumnsExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @Author: Zihui Li
 * @Date: 2020/12/4
 * @tips: mapper接口
 */
@Mapper
public interface AgWidgetAssetsColumnsMapper {
    int countByExample(AgWidgetAssetsColumnsExample example);

    int deleteByExample(AgWidgetAssetsColumnsExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgWidgetAssetsColumns record);

    int insertSelective(AgWidgetAssetsColumns record);

    List<AgWidgetAssetsColumns> selectByExample(AgWidgetAssetsColumnsExample example);

    AgWidgetAssetsColumns selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgWidgetAssetsColumns record, @Param("example") AgWidgetAssetsColumnsExample example);

    int updateByExample(@Param("record") AgWidgetAssetsColumns record, @Param("example") AgWidgetAssetsColumnsExample example);

    int updateByPrimaryKeySelective(AgWidgetAssetsColumns record);

    int updateByPrimaryKey(AgWidgetAssetsColumns record);
}