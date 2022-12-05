package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgMetadata;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by chendindxing on 2017-07-26.
 */
@Mapper
public interface AgMetadataMapper {

    void insertAgMetadata(AgMetadata agMetadata) throws Exception;

    AgMetadata getAgMetadataById(@Param("id") String id) throws Exception;

    List<String> getYears() throws Exception;

    void updateAgMetadata(AgMetadata agMetadata) throws Exception;

    void deleteAgMetadata(@Param("id") String id) throws Exception;
}
