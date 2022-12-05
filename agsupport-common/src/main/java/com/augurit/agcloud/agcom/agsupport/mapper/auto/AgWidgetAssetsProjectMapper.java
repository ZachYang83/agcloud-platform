package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetAssetsProject;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetAssetsProjectExample;
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
public interface AgWidgetAssetsProjectMapper {
    int countByExample(AgWidgetAssetsProjectExample example);

    int deleteByExample(AgWidgetAssetsProjectExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgWidgetAssetsProject record);

    int insertSelective(AgWidgetAssetsProject record);

    List<AgWidgetAssetsProject> selectByExample(AgWidgetAssetsProjectExample example);

    AgWidgetAssetsProject selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgWidgetAssetsProject record, @Param("example") AgWidgetAssetsProjectExample example);

    int updateByExample(@Param("record") AgWidgetAssetsProject record, @Param("example") AgWidgetAssetsProjectExample example);

    int updateByPrimaryKeySelective(AgWidgetAssetsProject record);

    int updateByPrimaryKey(AgWidgetAssetsProject record);
}