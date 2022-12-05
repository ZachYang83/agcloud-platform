package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Augurit on 2017-05-02.
 */
@Mapper
public interface AgServiceMapper {
    AgServiceUserinfo getAgServiceUserinfoById(@Param("id") String id);

    void updateAgServiceUserinfo(AgServiceUserinfo agServiceUserinfo);

    void insertAgServiceUserinfo(AgServiceUserinfo agServiceUserinfo);

    List<AgServiceUserinfo> listAgServiceUserinfo(AgServiceUserinfo agServiceUserinfo);

    List<AgServiceUserinfo> listAgServiceUserinfoOpus(AgServiceUserinfo agServiceUserinfo);

    List<AgServiceUserinfo> findListByUserId(@Param("userId") String userId);

    List<AgServiceUserinfo> findListByServiceId(@Param("serviceId") String serviceId);

    void deleteAgServiceUserinfo(@Param("id") String id);

    void deleteAgServiceUserinfoBatch(@Param("layerIds") List<String> layerIds);

    AgServiceLog getAgServiceLogById(@Param("id") String id);

    List<AgServiceLog> listAgServiceLog(AgServiceLog agServiceLog);

    List<AgServiceLog> listAgServiceLogOpus(@Param("userIds") List<String> userIds,@Param("agServiceLog") AgServiceLog agServiceLog);

    AgLayer getAgServiceById(@Param("id") String id);

    List<AgMetadata> findByLabel(@Param("label") String label, @Param("asc") boolean asc) throws Exception;

    AgLayer findByMidAndUid(@Param("metadataId") String metadataId, @Param("userId") String userId) throws Exception;

    AgLayer findByMidAndUsers(@Param("metadataId") String metadataId, @Param("userList") List<AgUser> userList) throws Exception;

    int countByLabel(@Param("label") String label) throws Exception;

    int countByLabelAndUid(@Param("label") String label, @Param("userId") String userId) throws Exception;

    int countByLabelAndUsers(@Param("label") String label, @Param("userList") List<AgUser> userList) throws Exception;

    AgLayer findByMetadataId(@Param("metadataId") String metadataId) throws Exception;

    AgServiceUserinfo getAgServiceUserinfoByUidAndSid(@Param("userId") String userId, @Param("serviceId") String serviceId);

    List<AgServiceLog> getLogOrderByUser() throws Exception;

    List<AgServiceLog> getLogOrderByUserOpus() throws Exception;

    List<AgServiceLog> getLogOrderByService() throws Exception;

    List<AgServiceLog> getLogOrderByServiceOpus() throws Exception;


    List<AgServiceLog> getLogByServiceId(String serviceId) throws Exception;


    List<AgServiceUserinfo> getServiceUserInfoByServiceIdAndUserName(@Param("serviceId") String serviceId, @Param("userId") String userId);

    List<Map<String,Object>> getHotService(@Param("startDay") String startDay, @Param("endDay") String endDay) throws Exception; //获取热门服务前5名

    List<AgServiceLog> getHotServiceRecentAllByte(
            @Param("serviceId") String serviceId,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    ) throws Exception;

}
