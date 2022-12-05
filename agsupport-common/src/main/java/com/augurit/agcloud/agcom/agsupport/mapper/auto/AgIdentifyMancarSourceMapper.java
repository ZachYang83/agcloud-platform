package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgIdentifyMancarSource;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgIdentifyMancarSourceExample;
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
public interface AgIdentifyMancarSourceMapper {
    int countByExample(AgIdentifyMancarSourceExample example);

    int deleteByExample(AgIdentifyMancarSourceExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgIdentifyMancarSource record);

    int insertSelective(AgIdentifyMancarSource record);

    List<AgIdentifyMancarSource> selectByExample(AgIdentifyMancarSourceExample example);

    AgIdentifyMancarSource selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgIdentifyMancarSource record, @Param("example") AgIdentifyMancarSourceExample example);

    int updateByExample(@Param("record") AgIdentifyMancarSource record, @Param("example") AgIdentifyMancarSourceExample example);

    int updateByPrimaryKeySelective(AgIdentifyMancarSource record);

    int updateByPrimaryKey(AgIdentifyMancarSource record);
}