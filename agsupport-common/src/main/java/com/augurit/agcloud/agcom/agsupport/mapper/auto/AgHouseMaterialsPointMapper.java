package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgHouseMaterialsPoint;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgHouseMaterialsPointExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @Author: qinyg
 * @Date: 2020/10/24
 * @tips: mapper接口
 */
@Mapper
public interface AgHouseMaterialsPointMapper {
    int countByExample(AgHouseMaterialsPointExample example);

    int deleteByExample(AgHouseMaterialsPointExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgHouseMaterialsPoint record);

    int insertSelective(AgHouseMaterialsPoint record);

    List<AgHouseMaterialsPoint> selectByExample(AgHouseMaterialsPointExample example);

    AgHouseMaterialsPoint selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgHouseMaterialsPoint record, @Param("example") AgHouseMaterialsPointExample example);

    int updateByExample(@Param("record") AgHouseMaterialsPoint record, @Param("example") AgHouseMaterialsPointExample example);

    int updateByPrimaryKeySelective(AgHouseMaterialsPoint record);

    int updateByPrimaryKey(AgHouseMaterialsPoint record);
}