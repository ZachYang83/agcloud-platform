package com.augurit.agcloud.agcom.agsupport.mapper;



import com.augurit.agcloud.agcom.agsupport.domain.Agcim3dentityVbXCustom;
import com.augurit.agcloud.agcom.agsupport.domain.Agcim3dentityVbXCustomExample;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Agcim3dentityVbXCustomMapper {

    List<Agcim3dentityVbXCustom> selectAllByExample(Agcim3dentityVbXCustomExample example);

    List<Agcim3dentityVbXCustom> selectDefineByExample(Agcim3dentityVbXCustomExample example);

    List<Object>  selectBySql(String sql);

}