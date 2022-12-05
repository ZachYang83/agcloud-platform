package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgAppSystem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgAppSystemMapper {
    int deleteById(String id);
    int deleteByIds(@Param("ids") List<String> ids);
    int insert(AgAppSystem record);

    AgAppSystem findById(String id);

    int updateAppSystem(AgAppSystem record);

    List<AgAppSystem> findAllRest(@Param("appName") String appName,@Param("authorizeStatus")String authorizeStatus);

    List<AgAppSystem> findByIds(List<String> list);

    List<AgAppSystem> findAll(String appSystemName);


}