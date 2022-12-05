package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckProject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: zihui li
 * @Date: 2020/11
 * @Description:
 */
public interface IAgBimCheckProjectService {


	List<AgBimCheckProject> findAll(String paramType);

    String addProject(AgBimCheckProject agBimCheckProject);

    void updateProject(AgBimCheckProject agBimCheckProject);

    void deleteProject(String id);
}
