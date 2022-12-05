package com.augurit.agcloud.agcom.agsupport.sc.io.JsonStore.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgJsonStore;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IAgJsonStore
 * @Description TODO
 * @Author Administrator
 * @Date 2019/9/23 14:49
 * @Version 1.0
 **/
public interface IAgJsonStore {

    /**
     * 根据id获得数据
     *
     * @param id
     * @return
     * @throws RuntimeException
     */
    AgJsonStore getById(String id) throws RuntimeException;

    /**
     * 获得所有数据
     *
     * @return
     * @throws RuntimeException
     */
    List<AgJsonStore> getAll() throws RuntimeException;

    /**
     * 删除一条数据
     *
     * @param id
     * @return
     */
    boolean deleteById(String id) throws Exception;

    /**
     * 删除更多数据
     *
     * @param stringList
     * @return
     * @throws RuntimeException
     */
    boolean deleteMany(List<String> stringList) throws Exception;

    /**
     * 添加一条信息
     *
     * @param agJsonStore
     * @return
     * @throws RuntimeException
     */
    boolean save(AgJsonStore agJsonStore) throws RuntimeException;

    /**
     * 编辑一条信息
     *
     * @param agJsonStore
     * @return
     * @throws RuntimeException
     */
    boolean update(AgJsonStore agJsonStore) throws RuntimeException;

    /**
     * 特殊条件查询列表
     *
     * @param agJsonStore
     * @param page
     * @return
     * @throws RuntimeException
     */
    PageInfo<AgJsonStore> getByDomainAndUsage(AgJsonStore agJsonStore, Page page) throws RuntimeException;

}
