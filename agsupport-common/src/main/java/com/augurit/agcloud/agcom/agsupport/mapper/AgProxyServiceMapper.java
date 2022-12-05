package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgProxyService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangmy
 * @Description: TODO
 * @date 2019-12-18 11:30
 */
@Mapper
public interface AgProxyServiceMapper {
    void saveProxyService(AgProxyService agProxyService);

    void updateProxyServiceState(AgProxyService agProxyService);

    void delete(String id);

    AgProxyService getAgProxyServiceById(String id);

    List<AgProxyService> findList(AgProxyService agProxyService) throws Exception;

}
