package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgWidgetStoreCustom;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetStoreExample;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetStoreWithBLOBs;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AgWidgetStoreCustomMapper {

    List<AgWidgetStoreCustom> selectByExampleWitThumbhBLOBs(AgWidgetStoreExample example);

}