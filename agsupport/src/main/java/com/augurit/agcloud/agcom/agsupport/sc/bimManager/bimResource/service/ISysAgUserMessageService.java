package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service;

import com.augurit.agcloud.agcom.agsupport.sc.bimManager.util.AgUserCustom;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
public interface ISysAgUserMessageService {


    /**
     * 查询组织id是210下面的所有agcloud用户
     * @param page
     * @return
     */
    PageInfo<AgUserCustom> list(String userName, Page page) throws Exception;

    /**
     * 修改用户授权
     * @param userId
     * @param auths
     */
    void udpateUser(String userId, String auths, String remark);
}
