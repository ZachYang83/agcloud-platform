package com.augurit.agcloud.agcom.agsupport.sc.io.ImageStore.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgImageStore;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IAgImageStore
 * @Description TODO
 * @Author Administrator
 * @Date 2019/9/23 14:49
 * @Version 1.0
 **/
public interface IAgImageStore {

    /**
     * 根据id获得数据
     *
     * @param id
     * @return
     * @throws RuntimeException
     */
    AgImageStore getById(String id) throws RuntimeException;

    /**
     * 获得所有数据
     *
     * @return
     * @throws RuntimeException
     */
    List<AgImageStore> getAll() throws RuntimeException;

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
     * @param agImageStore
     * @return
     * @throws RuntimeException
     */
    boolean save(AgImageStore agImageStore) throws RuntimeException;

    /**
     * 编辑一条信息
     *
     * @param agImageStore
     * @return
     * @throws RuntimeException
     */
    boolean update(AgImageStore agImageStore) throws RuntimeException;

    /**
     * 特殊条件查询列表（不分页）
     * @param agImageStore
     * @return
     * @throws RuntimeException
     */
    List<AgImageStore> getByDomainAndUsage(AgImageStore agImageStore) throws RuntimeException;

    /**
     * 特殊条件查询列表
     *
     * @param agImageStore
     * @param page
     * @return
     * @throws RuntimeException
     */
    PageInfo<AgImageStore> getByDomainAndUsage(AgImageStore agImageStore, Page page) throws RuntimeException;

    /**
     * 特殊条件查询列表
     *
     * @param agImageStore
     * @param page
     * @return
     * @throws RuntimeException
     */
    PageInfo<AgImageStore> getByDomainOrUsage(AgImageStore agImageStore, Page page) throws RuntimeException;

    /**
     * 设置默认视点
     *
     * @param id
     * @return
     * @throws RuntimeException
     */
    Map<String, Object> setDefaultViewpoints(String id) throws RuntimeException;
}
