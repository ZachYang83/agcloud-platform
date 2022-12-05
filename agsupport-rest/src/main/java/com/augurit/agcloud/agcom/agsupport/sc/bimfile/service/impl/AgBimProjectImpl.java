package com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimFile;
import com.augurit.agcloud.agcom.agsupport.domain.AgBimProject;
import com.augurit.agcloud.agcom.agsupport.mapper.AgBimFileMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgBimProjectMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.service.IAgBimProject;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * BIM项目ServiceImpl
 * Created by fanghh on 2020/3/18.
 */
@Service
public class AgBimProjectImpl implements IAgBimProject {

    @Autowired
    private AgBimProjectMapper agBimProjectMapper;

    @Autowired
    private AgBimFileMapper agBimFileMapper;

    @Override
    public List<AgBimProject> findTree() {
        List<AgBimProject> projectList = agBimProjectMapper.findParentIsNull();
        buildTree(projectList);
        return projectList;
    }

    private void buildTree(List<AgBimProject> projectList){
        projectList.forEach(x->{
            List<AgBimFile> bimFileList = agBimFileMapper.findByProjectId(x.getId());
            x.setBimFileList(bimFileList);
            List<AgBimProject> projects = agBimProjectMapper.findByParentId(x.getId());
            if(CollectionUtils.isNotEmpty(projectList)){
                buildTree(projects);
            }
            x.setProjectList(projects);
        });
    }
}
