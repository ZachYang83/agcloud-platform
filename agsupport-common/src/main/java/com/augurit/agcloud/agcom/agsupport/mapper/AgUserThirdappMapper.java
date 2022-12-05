package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgUserThirdapp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgUserThirdappMapper {
    int deleteById(String id);
    void deleteByIds(@Param("userIds") String[] userIds,@Param("appId") String appId);
    int insert(AgUserThirdapp record);

    AgUserThirdapp findById(String id);

    int update(AgUserThirdapp record);

    List<AgUserThirdapp> findByAppId(String appId);

    AgUserThirdapp findByAppIdAndUserId(@Param("appId") String appId,@Param("userId") String userId);

    Long findByMaxOrder();

    List<AgUserThirdapp> findByUserId(String userId);

    List<AgUserThirdapp> findByUserIdAndAppIds(@Param("userId") String userId,@Param("appIds") String[] appIds);

    void updateBatch(List<AgUserThirdapp> list);
}