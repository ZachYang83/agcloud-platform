package com.augurit.agcloud.agcom.agsupport.sc.projectManager.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgProjectManager;
import com.augurit.agcloud.agcom.agsupport.mapper.AgProjectManagerMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.StringUtils;
import com.augurit.agcloud.agcom.agsupport.sc.projectManager.service.AgProjectManagerService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcom.common.LoginHelpClient;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: libc
 * @Description: 工程管理 业务实现类
 * @Date: 2020/7/14 15:16
 * @Version: 1.0
 */
@Service
@Transactional
public class AgProjectManagerServiceImpl implements AgProjectManagerService {

    @Autowired
    private AgProjectManagerMapper projectManagerMapper;

    /**
     * @param name 查询条件 （工程名称）
     * @param page 分页对象
     * @Version 1.0
     * @Author libc
     * @Description
     * @Return 分页 工程列表
     * @Date 2020/7/14 16:13
     */
    public PageInfo<AgProjectManager> findList(String name, Page page) {
        PageHelper.startPage(page);
        List<AgProjectManager> list = projectManagerMapper.findList(name);
        return new PageInfo<AgProjectManager>(list);
    }

    /**
     * @param agProjectManager 工程信息对象
     * @param request
     * @Version 1.0
     * @Author libc
     * @Description 保存工程信息（新增或修改）
     * @Return
     * @Date 2020/7/14 16:15
     */
    public void save(AgProjectManager agProjectManager, HttpServletRequest request) {

        if (StringUtils.isNotEmpty(agProjectManager.getId())) {
            // 修改时间
            agProjectManager.setModifyTime(new Timestamp(new Date().getTime()));
            projectManagerMapper.updateByPrimaryKeySelective(agProjectManager);
        } else {
            // 获取当前登陆用户名
            String loginName = LoginHelpClient.getLoginName(request);
            agProjectManager.setCreator(loginName);
            // 创建时间
            agProjectManager.setCreateTime(new Timestamp(new Date().getTime()));
            agProjectManager.setIsDelete("0");
            agProjectManager.setId(UUID.randomUUID().toString());
            projectManagerMapper.insertSelective(agProjectManager);
        }
    }

    /**
     * @param ids 删除的id集合
     * @Version 1.0
     * @Author libc
     * @Description 批量删除工程信息  (逻辑删除)
     * @Return
     * @Date 2020/7/14 16:17
     */
    public void deleteProjectData(String[] ids) {
        /*if (ids != null && ids.length > 0) {
            for (String id : ids) {
                projectManagerMapper.deleteByPrimaryKey(id);
            }
        }*/
        projectManagerMapper.deleteByIds(ids);
    }

    /**
     * @param dataList 工程信息集合
     * @Version 1.0
     * @Author libc
     * @Description 批量保存工程信息
     * @Return
     * @Date 2020/7/14 16:18
     */
    public void saveDataList(List<AgProjectManager> dataList) {
        if (dataList != null && dataList.size() > 0) {
            for (AgProjectManager pm : dataList) {
                if (StringUtils.isNotEmpty(pm.getId())) {
                    projectManagerMapper.updateByPrimaryKeySelective(pm);
                } else {
                    pm.setId(UUID.randomUUID().toString());
                    projectManagerMapper.insertSelective(pm);
                }
            }
        }
    }

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据id删除工程信息 (逻辑删除)
     * @param id
     * @Return
     * @Date 2020/7/15 10:19
     */
    public void deleteProject(String id) {
        projectManagerMapper.deleteByPrimaryKey(id);
    }

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据名称查询工程（未删除记录）
     * @param name  工程名称
     * @Return
     * @Date 2020/7/16 14:59
     */
    public AgProjectManager findByName(String name) {
        return projectManagerMapper.findByName(name);
    }

    /**
     * @Version  1.0
     * @Author libc
     * @Description 查询所有工程信息
     * @Return 工程信息集合
     * @Date 2020/7/16 16:28
     */
    public List<AgProjectManager> findAll() {
        return projectManagerMapper.findAll();
    }

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据参数获取工程信息
     * @Return
     * @Date 2020/7/16 16:28
     */
    public AgProjectManager findByParam(Map<String, String> paramMap) {
        return projectManagerMapper.findByParam(paramMap);
    }
}
