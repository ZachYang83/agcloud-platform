package com.augurit.agcloud.agcom.agsupport.sc.projectManager.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgProjectManager;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: libc
 * @Description: 工程管理 业务类接口
 * @Date: 2020/7/14 15:15
 * @Version: 1.0
 */
public interface AgProjectManagerService {
    /**
     * @Version  1.0
     * @Author libc
     * @Description
     * @param name 查询条件 （工程名称）
     * @param page 分页对象
     * @Return 分页 工程列表
     * @Date 2020/7/14 16:13
     */
    PageInfo<AgProjectManager> findList(String name, Page page);

    /**
     * @Version  1.0
     * @Author libc
     * @Description 保存工程信息（新增或修改）
     * @param agProjectManager 工程信息对象
     * @param request
     * @Return
     * @Date 2020/7/14 16:15
     */
    void save(AgProjectManager agProjectManager, HttpServletRequest request);

    /**
     * @Version  1.0
     * @Author libc
     * @Description 批量删除工程信息
     * @param ids 删除的id集合
     * @Return
     * @Date 2020/7/14 16:17
     */
    void deleteProjectData(String[] ids);

    /**
     * @Version  1.0
     * @Author libc
     * @Description 批量保存工程信息
     * @param dataList 工程信息集合
     * @Return
     * @Date 2020/7/14 16:18
     */
    void saveDataList(List<AgProjectManager> dataList);

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据id删除工程信息
     * @param id
     * @Return
     * @Date 2020/7/15 10:19
     */
    void deleteProject(String id);

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据名称查询工程（未删除记录）
     * @param name  工程名称
     * @Return
     * @Date 2020/7/16 14:59
     */
    AgProjectManager findByName(String name);

    /**
     * @Version  1.0
     * @Author libc
     * @Description 查询所有工程信息
     * @Return 工程信息集合
     * @Date 2020/7/16 16:28
     */
    List<AgProjectManager> findAll();

    /**
     * @Version  1.0
     * @Author libc
     * @Description 根据参数获取工程信息
     * @Return
     * @Date 2020/7/16 16:28
     */
    AgProjectManager findByParam(Map<String, String> paramMap);
}
