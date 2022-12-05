package com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.agcom.agsupport.domain.AgUserThirdapp;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangmy
 * @Description: 接口
 * @date 2019-10-22 10:07
 */
public interface IAppSystemAuthorizeService {
    int deleteById(String id);
    void deleteByIds(String[] userIds,String appId);
    int insert(AgUserThirdapp record);

    AgUserThirdapp findById(String id);

    int update(AgUserThirdapp record);

    PageInfo<AgUser> findAppUser(String appId, String userName, Page page) throws Exception;

    AgUserThirdapp findByAppIdAndUserId(@Param("appId") String appId, @Param("userId") String userId);

    Long findByMaxOrder();

    List<AgUserThirdapp> findByAppId(String appId);
}
