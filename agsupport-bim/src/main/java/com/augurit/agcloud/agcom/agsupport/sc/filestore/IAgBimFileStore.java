package com.augurit.agcloud.agcom.agsupport.sc.filestore;

import com.augurit.agcloud.agcom.agsupport.domain.AgFileStore;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @ClassName IAgFileStore
 * @Description TODO
 * @Author Administrator
 * @Date 2019/9/23 14:49
 * @Version 1.0
 **/
public interface IAgBimFileStore {

    /**
     * 根据id获得数据
     *
     * @param id
     * @return
     * @throws RuntimeException
     */
    AgFileStore getById(String id) throws RuntimeException;

    /**
     * 获得所有数据
     *
     * @return
     * @throws RuntimeException
     */
    List<AgFileStore> getAll() throws RuntimeException;

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
     * @param agFileStore
     * @return
     * @throws RuntimeException
     */
    boolean save(AgFileStore agFileStore) throws Exception;

    /**
     * 编辑一条信息
     *
     * @param agFileStore
     * @return
     * @throws RuntimeException
     */
    boolean update(AgFileStore agFileStore) throws RuntimeException;

    /**
     * 特殊条件查询列表
     *
     * @param agFileStore
     * @param page
     * @return
     * @throws RuntimeException
     */
    PageInfo<AgFileStore> getByDomainOrUsage(AgFileStore agFileStore, Page page) throws RuntimeException;

    /**
     * 特殊条件查询列表
     *
     * @param agFileStore
     * @return
     * @throws RuntimeException
     */
    List<AgFileStore> downloadByDomainAndUsage(AgFileStore agFileStore) throws RuntimeException;

}
