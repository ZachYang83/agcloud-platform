package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dbuilding;
import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dbuildingExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @Author: libc
 * @Date: 2020/12/10
 * @tips: mapper接口
 */
@Mapper
public interface Agcim3dbuildingMapper {
    int countByExample(Agcim3dbuildingExample example);

    int deleteByExample(Agcim3dbuildingExample example);

    int deleteByPrimaryKey(String id);

    int insert(Agcim3dbuilding record);

    int insertSelective(Agcim3dbuilding record);

    List<Agcim3dbuilding> selectByExample(Agcim3dbuildingExample example);

    Agcim3dbuilding selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Agcim3dbuilding record, @Param("example") Agcim3dbuildingExample example);

    int updateByExample(@Param("record") Agcim3dbuilding record, @Param("example") Agcim3dbuildingExample example);

    int updateByPrimaryKeySelective(Agcim3dbuilding record);

    int updateByPrimaryKey(Agcim3dbuilding record);
}