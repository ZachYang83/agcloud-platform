package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dentityA;
import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dentityAExample;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Agcim3dCustomMapper {


    /**
     * 注意使用 orderByClause 当做前缀sql
     * @param example
     * @return
     */
    List<Agcim3dentityA> selectByExample(Agcim3dentityAExample example);

    List<Object> findAllDefineSql(String sql);

}