package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetAssetsTable;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetAssetsTableExample;
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
public interface AgWidgetAssetsTableMapper {
    int countByExample(AgWidgetAssetsTableExample example);

    int deleteByExample(AgWidgetAssetsTableExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgWidgetAssetsTable record);

    int insertSelective(AgWidgetAssetsTable record);

    List<AgWidgetAssetsTable> selectByExample(AgWidgetAssetsTableExample example);

    AgWidgetAssetsTable selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgWidgetAssetsTable record, @Param("example") AgWidgetAssetsTableExample example);

    int updateByExample(@Param("record") AgWidgetAssetsTable record, @Param("example") AgWidgetAssetsTableExample example);

    int updateByPrimaryKeySelective(AgWidgetAssetsTable record);

    int updateByPrimaryKey(AgWidgetAssetsTable record);
}