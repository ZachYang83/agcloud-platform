package com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetAssetsProject;
import com.augurit.agcloud.agcom.agsupport.mapper.AgWidgetAssetsProjectCustomMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgWidgetAssetsProjectMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.service.IAgWidgetAssetsProjectService;
import com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.service.IAgWidgetAssetsTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: Zihui Li
 * @Date: 2020/11/18
 * @tips:
 */

@Service
public class AgWidgetAssetsProjectServiceImpl implements IAgWidgetAssetsProjectService {

    @Autowired
    AgWidgetAssetsProjectMapper agThematicProjectMapper;

    @Autowired
    AgWidgetAssetsProjectCustomMapper agThematicProjectCustomMapper;

    @Autowired
    private IAgWidgetAssetsTableService agThematicTableService;

    @Override
    public List<AgWidgetAssetsProject> findAll() {
        List<AgWidgetAssetsProject> list = agThematicProjectMapper.selectByExample(null);
        return list;
    }

    @Override
    @Transactional
    public void updateProject(AgWidgetAssetsProject agThematicProject) {
        AgWidgetAssetsProject currentThematicProj = agThematicProjectCustomMapper.selectByAppSoftId(agThematicProject.getAppSoftId());

        if (currentThematicProj == null) {
            // ag_thematic_project表中没有这条appSoftId对应的记录，创建
            agThematicProject.setCreateTime(new Date());
            agThematicProject.setId(UUID.randomUUID().toString());
            agThematicProject.setUniqueIdf(agThematicProject.getUniqueIdf().trim());
            agThematicProjectMapper.insertSelective(agThematicProject);
        } else { //修改
            AgWidgetAssetsProject selectedProj = agThematicProjectMapper.selectByPrimaryKey(agThematicProject.getId());
            if (selectedProj == null) {
                throw new SourceException("Project id may not exists!");
            }
            String currentUniqueIdf = selectedProj.getUniqueIdf();
            String targetUniqueIdf = agThematicProject.getUniqueIdf().trim();
            if (!targetUniqueIdf.equals(currentUniqueIdf)) {
                //修改专题项目表，需要同步修改已创建的相关表名，此步骤要先执行
                agThematicTableService.updateThematicProjectNeedAlterThematicTable(agThematicProject.getId(), agThematicProject.getUniqueIdf());

                agThematicProject.setUniqueIdf(targetUniqueIdf);
                agThematicProject.setModifyTime(new Date());
                agThematicProjectMapper.updateByPrimaryKeySelective(agThematicProject);
            } else {
                throw new SourceException("该标识符已存在,请换一个标识符, UniqueIdf already exists. ");
            }
        }
    }

    @Override
    public AgWidgetAssetsProject getProject(String appSoftId) {
        return agThematicProjectCustomMapper.selectByAppSoftId(appSoftId);
    }

    @Override
    public String getUniqueIdfBySoftCode(String softCode) {
        if (StringUtils.isEmpty(softCode)) {
            throw new SourceException("SoftCode should not be empty. ");
        }
        String uniqueIdfRes = agThematicProjectCustomMapper.getUniqueIdBySoftCode(softCode);
        return uniqueIdfRes;
    }
}
