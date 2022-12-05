package com.augurit.agcloud.agcom.agsupport.sc.func.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgFunc;

import java.util.List;

/**
 * Created by Augurit on 2017-05-02.
 */
public interface IAgFunc {

    /**
     * 按用户查询功能
     *
     * @param userId
     * @return
     * @throws Exception
     */
    List<AgFunc> findFuncByUser(String userId, String isMobile, Boolean isTree, String appSoftCode,String isCloudSoft,String isAdmin) throws Exception;
}
