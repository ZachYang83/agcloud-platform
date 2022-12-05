package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgUserMessage;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
public interface IAgUserMessageService {
    /**
     * 添加用户信息
     * @param message
     */
    void add(AgUserMessage message);

    /**
     * 1、无权限访问后台管理sys的controller列表
     * 2、需要分配权限才能访问前台
     * @return
     */
    Map<String, String> notPermissioList();

}
