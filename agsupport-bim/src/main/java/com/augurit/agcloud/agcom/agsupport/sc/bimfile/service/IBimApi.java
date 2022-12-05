package com.augurit.agcloud.agcom.agsupport.sc.bimfile.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimApi;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @ClassName: IBimApi
 * @Description: 第三方接口管理接口
 * @Author: zhangsj
 * @Date: Create in 2020/03/20 10:25
 **/

public interface IBimApi {

    /**
     * 新增一条Api
     * @param agBimApi
     * @return
     * @throws RuntimeException
     */
    boolean addAgBimApi(AgBimApi agBimApi) throws RuntimeException;

    /**
     * 删除一条Api
     * @param id
     * @return
     * @throws RuntimeException
     */
    boolean deleteById(String id) throws RuntimeException;

    /**
     * 删除更多数据
     * @param stringList
     * @return
     * @throws Exception
     */
    boolean deleteMany(List<String> stringList) throws Exception;

    /**
     * 根据ID获取Api
     * @param id
     * @return
     * @throws RuntimeException
     */
    AgBimApi getById(String id) throws RuntimeException;

    /**
     * 获取所有Api
     * @return
     * @throws RuntimeException
     */
    List<AgBimApi> getAll() throws RuntimeException;

    /**
     * 修改Api
     * @param agBimApi
     * @return
     * @throws RuntimeException
     */
    boolean update(AgBimApi agBimApi) throws RuntimeException;


    /**
     * 特殊条件Or分页查询
     * @param name
     * @param url
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo<AgBimApi> getByNameOrUrl(String name, String url, Page page) throws Exception;

}
