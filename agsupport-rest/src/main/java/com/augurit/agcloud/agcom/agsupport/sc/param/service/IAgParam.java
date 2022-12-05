package com.augurit.agcloud.agcom.agsupport.sc.param.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgMapParam;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Augurit on 2017-04-18.
 */
public interface IAgParam {
    /**
     * 获取所有地图参数
     *
     * @return
     * @throws Exception
     */
    List<AgMapParam> searchAll() throws Exception;

    /**
     * 按条件分页查询
     *
     * @param name
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo<AgMapParam> searchParam(String name, Page page) throws Exception;

    /**
     * 根据id查找地图参数
     *
     * @param id
     * @return
     * @throws Exception
     */
    AgMapParam findMapParamById(String id) throws Exception;

    /**
     * 按条件查找地图参数
     *
     * @param agMapParam
     * @return
     * @throws Exception
     */
    AgMapParam findMapParam(AgMapParam agMapParam) throws Exception;

    /**
     * 保存地图参数
     *
     * @param agMapParam
     * @throws Exception
     */
    void saveMapParam(AgMapParam agMapParam) throws Exception;

    /**
     * 批量保存地图参数
     *
     * @param list
     * @throws Exception
     */
    void saveMapParamBatch(List<AgMapParam> list) throws Exception;

    /**
     * 修改地图参数
     *
     * @param agMapParam
     * @throws Exception
     */
    void updateMapParam(AgMapParam agMapParam) throws Exception;

    /**
     * 批量删除地图参数
     *
     * @param ids
     * @throws Exception
     */
    void deleteMapParamBatch(String[] ids) throws Exception;

    /**
     * 获取生产库中的地图参数
     *
     * @return
     * @throws Exception
     */
    List<Map> findMapParamFromProd() throws Exception;

    /**
     * 导入图层时获取图层的地图参数
     *
     * @param layerId
     * @return
     * @throws Exception
     */
    Map findParamFromProdByLayerId(String layerId) throws Exception;

    /**
     * 获取地图原点
     *
     * @param paramId
     * @return
     * @throws Exception
     */
    List<Map> findOriginFromProd(String paramId) throws Exception;

    /**
     * 解析xml地图范围
     *
     * @param in
     * @return
     */
    String loadMapextent(InputStream in);

    /**
     * 解析xml读取分辨率
     *
     * @param in
     * @return
     */
    String loadResolution(InputStream in);

    Map<String, String> readTileOrigin(InputStream in);

    List<AgMapParam> checkName(String name) throws Exception;
}
