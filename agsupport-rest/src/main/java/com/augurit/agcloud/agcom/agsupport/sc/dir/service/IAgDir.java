package com.augurit.agcloud.agcom.agsupport.sc.dir.service;

import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.sc.dir.controller.form.LayerForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Augurit on 2017-04-17.
 */
public interface IAgDir {

    /**
     * 获取所有目录
     *
     * @return
     * @throws Exception
     */
    List<AgDir> findAllDir() throws Exception;

    /**
     * 获取所有目录 有矢量数据的目录（数据缓冲）
     * @return
     * @throws Exception
     */
    List<AgDir> findAllDirSJHC() throws Exception;

    /**
     * 获取所有二级目录
     *
     * @return
     * @throws Exception
     */
    List<AgDir> findSecondDir() throws Exception;

    /**
     * 根据xpath获取子目录
     *
     * @return
     * @throws Exception
     */
    List<AgDir> findDirsByXpath(String xpath) throws Exception;

    /**
     * 根据id获取目录
     *
     * @return
     * @throws Exception
     */
    AgDir findDirById(String id) throws Exception;

    /**
     * 获取子节点最大顺序
     *
     * @param pid
     * @return
     * @throws Exception
     */
    String getDirOrder(String pid) throws Exception;

    /**
     * 保存目录
     *
     * @param agDir
     * @throws Exception
     */
    void saveDir(AgDir agDir) throws Exception;

    /**
     * 修改目录
     *
     * @param agDir
     * @throws Exception
     */
    void updateDir(AgDir agDir) throws Exception;

    /**
     * 批量修改目录
     *
     * @throws Exception
     */
    void updateDirBatch(List<AgDir> list) throws Exception;

    /**
     * 删除目录
     *
     * @param id
     * @throws Exception
     */
    void deleteDir(String id) throws Exception;

    /**
     * 分页查找图层
     *
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo<AgLayer> searchLayer(String name,String isVector, Page page) throws Exception;

    /**
     * 分页查找某个目录包含的图层
     *
     * @param agLayer
     * @param page
     * @param dirId  需要搜索的目录的id，dirId为空时代码搜索全部目录下的图层
     * @param isContain
     * @return
     */
    PageInfo<AgLayer> searchLayersByDirId(AgLayer agLayer, Page page, String dirId, String userId, String isContain, String isBaseMap) throws Exception;

    /**
     * 分页查找某个目录包含的图层
     *
     * @param agLayer
     * @param page
     * @param xpath
     * @param isContain
     * @return
     */
    PageInfo<AgLayer> searchLayer(AgLayer agLayer, Page page, String xpath, String userId, String isContain, String isBaseMap) throws Exception;

    List<AgLayer> getBaseLayerByUserId(String userId) throws Exception;

    /**
     * 分页查询用戶包含的图层
     *
     * @param name
     * @param page
     * @param userId
     * @return
     * @throws Exception
     */
    PageInfo<AgLayer> searchLayer(String name, Page page, String userId) throws Exception;

    List<AgLayer> findLayerByUserId(String name, Page page, String userId) throws Exception;

    /**
     * 通用服务--按条件查询服务
     *
     * @param type
     * @param keyWord
     * @param year
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo<AgLayer> searchLayer(String type, String keyWord, String year, Page page) throws Exception;

    /**
     * 根据layerId查找图层
     *
     * @param layerId
     * @return
     * @throws Exception
     */
    AgLayer findLayerByLayerId(String layerId) throws Exception;

    /**
     * 根据图层id数组查询图层
     * @param layerIds
     * @return
     * @throws Exception
     */
    List<AgLayer> findLayerListByLayerIds(String[] layerIds) throws Exception;

    List<AgLayer> setparamid(List<AgLayer> agLayer) throws Exception;

    /**
     * 根据dirLayerId查找图层
     *
     * @param dirLayerId
     * @return
     * @throws Exception
     */
    AgLayer findLayerByDirLayerId(String dirLayerId) throws Exception;

    /**
     * 根据dirLayerId和userId查找图层
     *
     * @param dirLayerId
     * @param userId
     * @return
     * @throws Exception
     */
    AgLayer findLayerByDLidAndUid(String dirLayerId, String userId) throws Exception;

    /**
     * 根据dirLayerId查询图层关联的矢量图层id
     *
     * @param dirLayerId
     * @param userId
     * @return
     * @throws Exception
     */
    String findBindAuthorizedVector(String dirLayerId, String userId) throws Exception;

    /**
     * 根据url和layerTable查询图层
     *
     * @param url
     * @param layerTable
     * @return
     */
    AgLayer findLayerByUrl(String url, String layerTable) throws Exception;

    /**
     * 根据url查询图层
     *
     * @param url
     * @return
     */
    AgLayer findLayerByUrl(String url) throws Exception;
    /**
     * 按别名查询图层
     *
     * @param nameCn
     * @return
     * @throws Exception
     */
    AgLayer findLayerByNameCn(String nameCn) throws Exception;

    List<AgLayer> findLayerByXpathAndUserId(String xpath, String userId) throws Exception;

    List<AgLayer> findLayerByDirNameAndLayerName(String dirName, String layerName, String userId) throws Exception;

    /**
     * 保存图层
     *
     * @param agLayer
     * @throws Exception
     */
    void saveLayer(AgLayer agLayer) throws Exception;

    /**
     * 修改图层
     *
     * @param agLayer
     * @throws Exception
     */
    void updateLayer(AgLayer agLayer) throws Exception;


    /**
     * 批量删除图层
     *
     * @param agDirLayers
     * @throws Exception
     */
    void deleteLayerBatch(List<AgDirLayer> agDirLayers, String filePath) throws Exception;

    /**
     * 批量删除图层
     * create by huangby
     * @param dirLayerIds 目录图层Id，多个Id用逗号分隔
     * @param uploadAbsolutePath 上传文件存放路径，
     * @throws Exception
     */
    void deleteLayerBatch(String dirLayerIds, String uploadAbsolutePath) throws Exception;

    /**
     * 获取目录图层关联
     *
     * @param dirId
     * @param layerId
     * @return
     * @throws Exception
     */
    AgDirLayer getDirLayer(String dirId, String layerId) throws Exception;
    List<AgDirLayer> findAll() throws Exception;

    List<AgDirLayer> findByDirId(String dirId) throws Exception;

    List<AgDirLayer> findDirLayerByDirXpath(String dirXpath) throws Exception;

    List<AgDirLayer> findDirLayerByLayerId(String layerId) throws Exception;

    /**
     * 根据目录图层id数组查找
     *
     * @param dirLayerIds
     * @return
     * @throws Exception
     */
    List<AgDirLayer> findDirLayerByIds(String[] dirLayerIds) throws Exception;

    List<AgDir> findDirByIds(String[] dirIds) throws Exception;

    /**
     * 获取图层最大顺序
     *
     * @return
     * @throws Exception
     */
    String getLayerOrder() throws Exception;

    /**
     * 保存目录图层关联
     */
    void saveDirLayer(AgDirLayer agDirLayer) throws Exception;

    /**
     * 批量修改目录图层关联
     *
     * @param list
     * @throws Exception
     */
    void updateDirLayerBatch(List<AgDirLayer> list) throws Exception;

//    void updateDirBatch(List<AgDir> list) throws Exception;

    /**
     * 根据id删除目录图层关联
     *
     * @param id
     * @throws Exception
     */
    void delDirLayer(String id) throws Exception;

    /**
     * 从生产库中获取图层
     *
     * @return
     * @throws Exception
     */
    List<Map> findLayersFromProd() throws Exception;

    /**
     * 转换旧版图层分类，返回新版分类代码
     *
     * @param layerType
     * @param featureType
     * @param tileType
     * @return
     */
    String getLayerType(String layerType, String featureType, String tileType) throws Exception;

    boolean checkLayerTable(String dataSourceId, String layerTable, String pkColumn, String geometryColumn);

    /**
     * 获取某个用户的目录图层树 bootstrap-tree
     *
     * @return
     * @throws Exception
     */
    String getTreeByUser(String userId) throws Exception;

    /**
     * 按用户id，要素类型查找图层
     *
     * @param userId
     * @param featureType
     * @return
     * @throws Exception
     */
    List<LayerForm> findLayerByUserIdAndFeatureType(String userId, String featureType) throws Exception;

    /**
     * 获取所有矢量图层
     *
     * @return
     * @throws Exception
     */
    String findVectorLayer() throws Exception;

    String findRenderLayer() throws Exception;

    void putPreviewKey(String keyName, String previewKey) throws Exception;

    AgMetadata getAgMetadata(String id) throws Exception;

    String getToken(String id) throws Exception;

    List<AgServer> getServerLink() throws Exception;

    List<AgLayer> findLayerByUserIdAndXpath(String userId, String xpath) throws Exception;

    void setLayerUseCache(String dirLayerIds) throws Exception;

    List<AgLayerFieldConf> getTableFields(String datasourceId, String tableName) throws Exception;

    List<AgLayerFieldConf> findTableFieldsByDirLayerId(String dirLayerId) throws Exception;

    /**
     * 按用户id和图层表名查询图层
     *
     * @return
     * @throws Exception
     */
    AgLayer findLayerBylayerTableAndUserId(String userId, String layerTable) throws Exception;


    /**
     * 根据用户id获取图层信息
     * @param userId
     * @return
     * @throws Exception
     */
    List<AgLayer> getPageByUserId(String userId)throws  Exception;

    LinkedList<Map<String,String>> findTableFields(String dataSourceId, String layerTable, String layerId)throws Exception;

}
