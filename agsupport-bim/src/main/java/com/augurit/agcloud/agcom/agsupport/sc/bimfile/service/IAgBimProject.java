package com.augurit.agcloud.agcom.agsupport.sc.bimfile.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimProject;

import java.util.List;

/**
 * bim项目管理service
 * Created by fanghh on 2019/12/5.
 */
public interface IAgBimProject {

    void save(AgBimProject project);

    void update(AgBimProject project);

    int delete(String id);

    AgBimProject find(String id);

    List<AgBimProject> findAll();

    /**
     * 根据叶子的id获取项目树
     * @param projectId
     * @return
     */
    List<AgBimProject>findProjectTree(String projectId);

    /**
     * 获取下级项目树
     * @param projectId
     * @return
     */
    List<AgBimProject> findChildrenList(String projectId);
}
