package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgServicesMonitor;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;


/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :14:01 2018/10/30
 * @Modified By:
 */

@Mapper
public interface AgServicesMonitorMapper {

    /**
     * 获取服务监控列表
     * @param agServicesMonitor
     * @return
     * @throws Exception
     */
    List<AgServicesMonitor> findList(AgServicesMonitor agServicesMonitor) throws Exception;

    List<AgServicesMonitor> findAllList() throws Exception;

    void save(AgServicesMonitor agServicesMonitor) throws Exception;

    void update(AgServicesMonitor agServicesMonitor) throws Exception;

    void deleteByMonitorUrl(String monitorUrl) throws Exception;

    void delByIds(List<String> ids) throws Exception;

    void changeMonitorStatus(AgServicesMonitor agServicesMonitor) throws Exception;

    void changeAllMonitorStatus(AgServicesMonitor agServicesMonitor) throws Exception;

    AgServicesMonitor findByMonitorUrl(String monitorUrl)throws Exception;

    AgServicesMonitor findById(String id) throws Exception;

    void updateSetWarnInfo(AgServicesMonitor agServicesMonitor)throws Exception;
}
