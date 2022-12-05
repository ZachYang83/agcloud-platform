package com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimFile;
import com.augurit.agcloud.agcom.agsupport.domain.AgBimProject;
import com.augurit.agcloud.agcom.agsupport.domain.AgBimVersion;
import com.augurit.agcloud.agcom.agsupport.mapper.AgBimProjectMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IAgBimProject;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.controller.AgBimProjectController;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IBimFile;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IBimVersion;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * BIM项目管理Impl
 * Created by fanghh on 2020/2/26.
 */
@Service
public class AgBimProjectImpl implements IAgBimProject {

    private final static Logger LOGGER = LoggerFactory.getLogger(AgBimProjectController.class);

    @Autowired
    private AgBimProjectMapper agBimProjectMapper;

    @Autowired
    private IBimFile iBimFile;

    @Autowired
    private IBimVersion iBimVersion;

    @Override
    public void save(AgBimProject project) {
        agBimProjectMapper.save(project);
    }

    @Override
    public void update(AgBimProject project) {
        agBimProjectMapper.update(project);
    }

    @Override
    public int delete(String id) {
        List<AgBimProject> projectList = findChildrenList(id);
        projectList.forEach(project -> {
            List<AgBimFile> bimFileList = iBimFile.findByProjectId(project.getId());
            bimFileList.forEach(agBimFile ->{
                try {
                    iBimFile.deleteById(agBimFile.getId());
                } catch (Exception e) {
                    LOGGER.debug("删除失败：{}",e.getMessage());
                    e.printStackTrace();
                }
            });
            agBimProjectMapper.delete(project.getId());
        });
        return projectList.size();
    }

    @Override
    public AgBimProject find(String id) {
        return agBimProjectMapper.find(id);
    }

    @Override
    public List<AgBimProject> findAll() {
        return agBimProjectMapper.findAll();
    }

    @Override
    public List<AgBimProject> findProjectTree(String projectId) {
        List<AgBimProject> projectList = new ArrayList<>();
        findTopProjectList(projectId,projectList);
        return projectList;
    }

    public void findTopProjectList(String projectId,List<AgBimProject>projectList){
        if(StringUtils.isNotBlank(projectId)){
            AgBimProject project = agBimProjectMapper.find(projectId);
            if(StringUtils.isNotBlank(project.getParentId())){
                findTopProjectList(project.getParentId(),projectList);
            }else {
                projectList.add(project);
            }
        }
    }

    @Override
    public List<AgBimProject> findChildrenList(String projectId){
        List<AgBimProject> projectList = new ArrayList<>();
        AgBimProject project = agBimProjectMapper.find(projectId);
        projectList.add(project);
        findDownProjectList(projectId,projectList);
        return projectList;
    }

    public void findDownProjectList(String projectId,List<AgBimProject> projectList){
        List<AgBimProject> projects = agBimProjectMapper.findByParentId(projectId);
        if(CollectionUtils.isEmpty(projects)){
            return;
        }
        projects.forEach(x-> {
            findDownProjectList(x.getId(),projectList);
        });
        projectList.addAll(projects);
    }
}
