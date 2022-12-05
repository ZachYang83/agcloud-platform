package com.augurit.agcloud.agcom.agsupport.sc.service.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgServiceLog;
import com.augurit.agcloud.agcom.agsupport.domain.AgServiceUserinfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by Augurit on 2017-05-02.
 */
public interface IAgService {
    void insertAgServiceUserinfo(AgServiceUserinfo agServiceUserinfo);

    AgServiceUserinfo getAgServiceUserinfoById(String id);

    void updateAgServiceUserinfo(AgServiceUserinfo agServiceUserinfo);

    PageInfo<AgServiceUserinfo> searchAgServiceUserinfo(AgServiceUserinfo agServiceUserinfo, Page page);

    List<AgServiceUserinfo> findServiceUserinfoByUserId(String userId);

    List<AgServiceUserinfo> findServiceUserinfoByServiceId(String serviceId);

    void deleteAgServiceUserinfo(AgServiceUserinfo agServiceUserinfo);

    PageInfo<AgServiceLog> searchAgServiceLog(AgServiceLog agServiceLog, Page page);

    PageInfo<AgServiceLog> findAgServiceLog(AgServiceLog agServiceLog, Page page);

    //测试方法
    Object testMapper(String id);

    /**
     * 读取json，获取服务标签
     */
    String getMarks() throws Exception;

    String countByLabel() throws Exception;
    String countByLabelAnduserId(String userId) throws Exception;

    /**
     * 根据label查询图层
     *
     * @param label
     * @return
     */
    List<AgLayer> findLayerByLabel(String label, boolean asc) throws Exception;

    List<AgLayer> findByLabelAndUserId(String label, String userId, boolean asc) throws Exception;

    String applyServer(AgServiceUserinfo agServiceUserinfo) throws Exception;

    AgServiceUserinfo getApplyInfo(String userId, String serviceId) throws Exception;

    List<AgServiceUserinfo> getServiceUserInfoByServiceIdAndUserName(String serviceId, String userId) throws Exception;
}