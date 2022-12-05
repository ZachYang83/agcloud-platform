package com.augurit.agcloud.agcom.agsupport.sc.role.service;

import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created by Augurit on 2017-04-21.
 */
public interface IAgRole {

    /**
     * 分页查询所有角色
     *
     * @param agRole
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo<AgRole> searchRole(AgRole agRole, Page page) throws Exception;

}
