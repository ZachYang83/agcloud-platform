package com.augurit.agcloud.agcom.agsupport.sc.applyservice.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgProxyService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * @author zhangmy
 * @Description: TODO
 * @date 2019-12-24 14:30
 */
public interface AgApplyProxyService {
    void saveProxyService(AgProxyService agProxyService);

    void updateProxyServiceState(String id,String state,String approveOpinion);

    AgProxyService getAgProxyServiceById(String id);

    PageInfo<AgProxyService> findList(AgProxyService agProxyService, Page page) throws Exception;

    void deleteBatch(String ids);
}
