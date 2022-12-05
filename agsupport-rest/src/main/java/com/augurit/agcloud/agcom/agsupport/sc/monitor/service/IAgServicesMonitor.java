package com.augurit.agcloud.agcom.agsupport.sc.monitor.service;
import com.augurit.agcloud.agcom.agsupport.domain.AgServicesMonitor;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :14:11 2018/10/30
 * @Modified By:
 */
public interface IAgServicesMonitor {

    PageInfo<AgServicesMonitor> searchAgServicesMonitor(AgServicesMonitor agServicesMonitor, Page page) throws Exception;

    List<AgServicesMonitor> findAll() throws Exception;

    void saveAgServicesMonitor(AgServicesMonitor agServicesMonitor) throws Exception;

    void updataAgServicesMonitor(AgServicesMonitor agServicesMonitor) throws Exception;

    void deleteByMonitorUrl(String monitorUrl)throws Exception;

    void delByIds(List<String> ids) throws Exception;

    void changeMonitorStatus(AgServicesMonitor agServicesMonitor) throws Exception;

    void changeAllMonitorStatus(AgServicesMonitor agServicesMonitor) throws Exception;

    AgServicesMonitor findByMonitorUrl(String monitorUrl)throws Exception;

    AgServicesMonitor findById(String id) throws Exception;

    void refreshServerStatus(List<AgServicesMonitor> agServicesMonitorList, long count) throws Exception;

    /**
     * 修改告警设置信息
     * @param agServicesMonitor
     * @throws Exception
     */
    void updateSetWarnInfo(AgServicesMonitor agServicesMonitor)throws Exception;


}
