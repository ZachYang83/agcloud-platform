package com.augurit.agcloud.agcom.agsupport.sc.bimfile.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimProject;

import java.util.List;

/**
 * BIM项目service
 * Created by fanghh on 2020/3/18.
 */
public interface IAgBimProject {

    List<AgBimProject> findTree();
}
