package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckProject;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckProjectModel;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgServerContent;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgBimCheckProjectMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service.IAgBimCheckProjectService;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service.IBimCheckProjectModelService;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service.IBimCheckService;
import com.augurit.agcloud.agcom.agsupport.sc.serverContent.service.IAgServerContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: zihui li
 * @Date: 2020/11
 * @Description:
 */
@Service
public class AgBimCheckProjectServiceImpl implements IAgBimCheckProjectService {
    private static final Logger logger = LoggerFactory.getLogger(AgBimCheckProjectServiceImpl.class);
    @Autowired
    private AgBimCheckProjectMapper agBimCheckProjectmapper;

    @Autowired
    private IBimCheckProjectModelService bimCheckProjectModelService;

    @Autowired
    private IAgServerContentService serverContentService;

    @Autowired
    private IBimCheckService bimCheckService;

    @Override
    public List<AgBimCheckProject> findAll(String paramType) {
        List<AgBimCheckProject> list = agBimCheckProjectmapper.selectByExample(null);
        if ("2".equals(paramType) && !CollectionUtils.isEmpty(list)) {
            // 项目列表加模型列表（对应BIM审查项目模型表）
            // 封装模型列表
            list.forEach(project -> {
                List<AgBimCheckProjectModel> projectModels = bimCheckProjectModelService.findListByBimCheckProjectId(project.getId());
                project.setProjectModels(projectModels);
            });
        } else if ("3".equals(paramType) && !CollectionUtils.isEmpty(list)) {
            // 项目列表加模型列表（对应服务内容表）
            // 封装模型列表
            list.forEach(project -> {
                List<AgServerContent> serverContents = serverContentService.findListBySourceRelId(project.getId());
                project.setServerContents(serverContents);
            });
        }
        return list;
    }

    @Override
    @Transactional
    public String addProject(AgBimCheckProject agBimCheckProject) {
        agBimCheckProject.setCreateTime(new Date());
        agBimCheckProject.setId(UUID.randomUUID().toString());
        agBimCheckProjectmapper.insert(agBimCheckProject);
        return agBimCheckProject.getId();
    }

    @Override
    public void updateProject(AgBimCheckProject agBimCheckProject) {
        agBimCheckProject.setModifyTime(new Date());
        agBimCheckProjectmapper.updateByPrimaryKeySelective(agBimCheckProject);
    }

    @Override
    @Transactional
    public void deleteProject(String id) {
        if (!StringUtils.isEmpty(id)) {
            // 删除项目对应的模型集合 （BIM审查项目模型表）
            bimCheckProjectModelService.deleteListByBimCheckProjectId(id);
            // 删除项目
            agBimCheckProjectmapper.deleteByPrimaryKey(id);
        }
    }
}