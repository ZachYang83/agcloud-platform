package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgSite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Augurit on 2017-04-27.
 */
@Mapper
public interface AgSiteMapper {

    /**
     * 查询所有机构
     *
     * @return
     * @throws Exception
     */
    AgSite getOrgserbyid(@Param("id") String id) throws Exception;
    void updateAgOrgs(AgSite AgOrgs) throws Exception;
    void deleteOrgsById(@Param("id") String id) throws Exception;
    void insetAgOrgs(AgSite AgOrgs) throws Exception;
    String getMaxid()throws Exception;
    List<AgSite> getSiteName(String serType)throws Exception;
    AgSite getSite(@Param("serType") String serType, @Param("serAlias") String serAlias)throws Exception;
    List<AgSite> getAllSite()throws Exception;
}
