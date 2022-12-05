package com.augurit.agcloud.agcom.agsupport.sc.app.service;

import com.augurit.agcloud.agcom.agsupport.sc.dir.controller.form.DirTree;

import java.util.List;
import java.util.Map;

/**
 * @Auther: zhangmingyang
 * @Date: 2018/9/30 09:33
 * @Description: 移动端专题接口
 */
public interface AppProjectService {
    List<Map> getProjectList(String loginName) throws Exception;

    List<DirTree> getTreeByProjectId(String projectId, String userId) throws Exception;

    List<DirTree> getLayersDirTree(String userId) throws Exception;
}
