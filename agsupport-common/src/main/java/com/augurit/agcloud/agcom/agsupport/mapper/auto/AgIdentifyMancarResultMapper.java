package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgIdentifyMancarResult;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgIdentifyMancarResultExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @Author: qinyg
 * @Date: 2020/12/28
 * @tips: mapper接口
 */
@Mapper
public interface AgIdentifyMancarResultMapper {
    int countByExample(AgIdentifyMancarResultExample example);

    int deleteByExample(AgIdentifyMancarResultExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgIdentifyMancarResult record);

    int insertSelective(AgIdentifyMancarResult record);

    List<AgIdentifyMancarResult> selectByExample(AgIdentifyMancarResultExample example);

    AgIdentifyMancarResult selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgIdentifyMancarResult record, @Param("example") AgIdentifyMancarResultExample example);

    int updateByExample(@Param("record") AgIdentifyMancarResult record, @Param("example") AgIdentifyMancarResultExample example);

    int updateByPrimaryKeySelective(AgIdentifyMancarResult record);

    int updateByPrimaryKey(AgIdentifyMancarResult record);
}