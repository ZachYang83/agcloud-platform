package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.Agcim3dentityXCustom;
import com.augurit.agcloud.agcom.agsupport.domain.Agcim3dentityXCustomExample;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface Agcim3dentityXCustomMapper {


    List<Agcim3dentityXCustom> selectByExample(Agcim3dentityXCustomExample example);


    List<Object> findAllDefineSql(String sql);


    /**
     * 注意使用 orderByClause 当做前缀sql
     * @param example
     * @return
     */
    List<Agcim3dentityXCustom> filterOrderByClause(Agcim3dentityXCustomExample example);
}