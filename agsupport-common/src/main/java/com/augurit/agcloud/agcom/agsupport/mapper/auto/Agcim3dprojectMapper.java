package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dproject;
import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dprojectExample;
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
public interface Agcim3dprojectMapper {
    int countByExample(Agcim3dprojectExample example);

    int deleteByExample(Agcim3dprojectExample example);

    int deleteByPrimaryKey(String id);

    int insert(Agcim3dproject record);

    int insertSelective(Agcim3dproject record);

    List<Agcim3dproject> selectByExample(Agcim3dprojectExample example);

    Agcim3dproject selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Agcim3dproject record, @Param("example") Agcim3dprojectExample example);

    int updateByExample(@Param("record") Agcim3dproject record, @Param("example") Agcim3dprojectExample example);

    int updateByPrimaryKeySelective(Agcim3dproject record);

    int updateByPrimaryKey(Agcim3dproject record);
}