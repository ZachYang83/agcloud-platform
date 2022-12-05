package com.augurit.agcloud.agcom.agsupport.sc.site.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgSite;

import java.util.List;
import java.util.Map;

/**
 * Created by Augurit on 2017-05-08.
 */
public interface IAgSite {

    List<Map> getregis() throws Exception;

    AgSite getOrgserbyid(String id)throws Exception;
    AgSite getSite(String serType, String serAlias)throws Exception;
    void deleteOrgsById(String id) throws Exception;
    void updateAgOrgs(AgSite AgOrgs) throws Exception;
    void insetAgOrgs(AgSite AgOrgs) throws Exception;
    String getMaxid()throws Exception;
    List<AgSite> getSiteName(String serType)throws Exception;
    List<AgSite> getAllSite()throws Exception;
}
