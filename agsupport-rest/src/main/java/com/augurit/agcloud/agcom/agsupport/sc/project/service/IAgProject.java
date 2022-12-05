package com.augurit.agcloud.agcom.agsupport.sc.project.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgUser;

import java.util.List;
import java.util.Map;

/**
 * Created by chendingxing on 2017-12-22.
 */
public interface IAgProject {

    List<Map> projectList( String loginName) throws Exception;

    List getProjectLayerTree(String projectPath, String projectName, AgUser agUser) throws Exception;

    List getchild(String id, String UserId)throws Exception;
}
