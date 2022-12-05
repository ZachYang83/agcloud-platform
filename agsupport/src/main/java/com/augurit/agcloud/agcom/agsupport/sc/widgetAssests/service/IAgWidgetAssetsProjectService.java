package com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.service;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetAssetsProject;

import java.util.List;

public interface IAgWidgetAssetsProjectService {
    List<AgWidgetAssetsProject> findAll();
    void updateProject(AgWidgetAssetsProject agThematicProject);
    AgWidgetAssetsProject getProject(String appSoftId);
    String getUniqueIdfBySoftCode(String softCode);
}
