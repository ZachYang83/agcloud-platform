package com.augurit.agcloud.agcom.agsupport.sc.externalSystemRegister.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgAppSystem;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author zhangmy
 * @Description: 外部系统注册接口
 * @date 2019-10-14 15:43
 */
public interface IAppSystemService {
    int deleteById(String id);
    int deleteByIds(List<String> ids);
    int insert(AgAppSystem record);

    AgAppSystem findById(String id);

    int updateAppSystem(AgAppSystem record);

    PageInfo<AgAppSystem> findAll(String appSystemName, Page page);

    List<AgAppSystem> findAll(String appName);
}
