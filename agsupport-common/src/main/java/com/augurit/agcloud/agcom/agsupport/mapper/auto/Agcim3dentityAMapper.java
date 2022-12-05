package com.augurit.agcloud.agcom.agsupport.mapper.auto;

import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dentityA;
import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dentityAExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @Author: libc
 * @Date: 2020/12/14
 * @tips: mapper接口
 */
@Mapper
public interface Agcim3dentityAMapper {
    int countByExample(Agcim3dentityAExample example);

    int deleteByExample(Agcim3dentityAExample example);

    int insert(Agcim3dentityA record);

    int insertSelective(Agcim3dentityA record);

    List<Agcim3dentityA> selectByExample(Agcim3dentityAExample example);

    int updateByExampleSelective(@Param("record") Agcim3dentityA record, @Param("example") Agcim3dentityAExample example);

    int updateByExample(@Param("record") Agcim3dentityA record, @Param("example") Agcim3dentityAExample example);
}