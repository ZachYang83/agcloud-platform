package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgHouseMaterials;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgHouseMaterialsExample;
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
public interface AgHouseMaterialsMapper {
    int countByExample(AgHouseMaterialsExample example);

    int deleteByExample(AgHouseMaterialsExample example);

    int deleteByPrimaryKey(String id);

    int insert(AgHouseMaterials record);

    int insertSelective(AgHouseMaterials record);

    List<AgHouseMaterials> selectByExample(AgHouseMaterialsExample example);

    AgHouseMaterials selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AgHouseMaterials record, @Param("example") AgHouseMaterialsExample example);

    int updateByExample(@Param("record") AgHouseMaterials record, @Param("example") AgHouseMaterialsExample example);

    int updateByPrimaryKeySelective(AgHouseMaterials record);

    int updateByPrimaryKey(AgHouseMaterials record);
}