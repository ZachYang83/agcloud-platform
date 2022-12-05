package com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgUserThirdapp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangmy
 * @Description: 接口
 * @date 2019-10-22 10:07
 */
public interface IAppSystemAuthorizeService {
    AgUserThirdapp findById(String id);

    int update(AgUserThirdapp record);

    AgUserThirdapp findByAppIdAndUserId(@Param("appId") String appId, @Param("userId") String userId);

    void moveSys(AgUserThirdapp appSys,AgUserThirdapp targetSys);

    List<AgUserThirdapp> findByUserId(String userId);

    List<AgUserThirdapp> findByUserIdAndAppIds(String userId,String[] appIds);
    void updateBatch(List<AgUserThirdapp> list);
}
