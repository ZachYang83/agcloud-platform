package com.augurit.agcloud.agcom.agsupport.sc.project.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by chendingxing on 2017-12-22.
 */
public interface IAgProject {

    List<Map> projectList( String loginName) throws Exception;
    /**
     * 根据专题名称获取专题
     * @param projectName
     * @return
     * @throws Exception
     */
    String getProject(String projectPath, String projectName) throws Exception;

    ResultForm addProject(String projectName, String mapParamId) throws Exception;

    ResultForm updateProject(String projectId, String projectName, String mapParamId) throws Exception;

    String deleteProject(String projectPath, String projectName) throws Exception;

    String deleteProjectDir(String projectPath, String projectName, String id) throws Exception;

    String addDir(String projectPath, String projectName, String id, String dirName) throws Exception;

    String updateDir(String projectPath, String projectName, String id, String dirName) throws Exception;

    String deleteDir(String projectPath, String projectName, String id) throws Exception;

    String addLayer(String projectPath, String projectName, String id, String ids) throws Exception;

    boolean removeLayer(String projectPath, String projectName, String id, String ids) throws Exception;

    PageInfo<AgLayer> getProjectDirLayer(String projectPath, String projectName, String id, String layerName, Page page) throws Exception;

    PageInfo<AgUser> getProjectUser(String projectPath, String projectName, String roleName, Page page) throws Exception;

    String projectAuthor(String projectPath, String projectName, String ids, Boolean isAdd) throws Exception;

    List getProjectLayerTree(String projectPath, String projectName, AgUser agUser) throws Exception;
    String updateDirBatch(String dirIds) throws Exception;
    List getchild(String id, String UserId)throws Exception;
}
