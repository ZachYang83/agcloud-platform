package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgKeyValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgKeyValueMapper {

    List<AgKeyValue> findKeyValueList(@Param("key") String key);

    void updateKeyValue(AgKeyValue keyvalue);

    void save(AgKeyValue keyvalue);

    void deleteByPrimaryKey(String id);

    List<AgKeyValue> findKeyValue(@Param("domain")String domain,@Param("key") String key);

}
