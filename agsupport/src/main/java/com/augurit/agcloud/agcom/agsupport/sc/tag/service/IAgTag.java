package com.augurit.agcloud.agcom.agsupport.sc.tag.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgTag;
import com.augurit.agcloud.agcom.agsupport.domain.AgTagCatalog;
import com.augurit.agcloud.agcom.agsupport.domain.AgTagLayer;

import java.util.List;

public interface IAgTag {
    /*
    * 添加标签目录
    * @param tagCatalog
    */
    void addTagCatalog(AgTagCatalog tagCatalog) throws Exception;

    /*
     * 保存标签
     * @param tag
     */
    void addTag(AgTag tag) throws Exception;

    /*
     * 查找所有标签目录
     */
    List<AgTagCatalog> getAllTagCatalogs() throws Exception;

    /*
     * 查找所有标签
     */
    List<AgTag> getAllTags() throws Exception;

    /*
     * 根据Id查找标签
     */
    AgTag getTagById(String id) throws Exception;

    /*
     * 根据id 删除标签目录
     */
    void deleteTagCatalog(String id) throws Exception;

    /*
     * 根据id 删除标签
     */
    void deleteTag(String id) throws Exception;

    /*
     * 修改标签
     * @param tag
     */
    void updateTag(AgTag tag) throws Exception;

    /*
     * 修改标签目录
     * @param tagCatalog
     */
    void updateTagCatalog(AgTagCatalog tagCatalog) throws Exception;


    AgTagCatalog getTagCatalogById(String id) throws Exception;


    List<AgTag> getAllTagByCatalogId(String id) throws Exception;

    /*
    * 获取对外开放且没有打上指定标签的图层
    */
    List<AgLayer> getOpenLayerwithoutTag(String serviceName, String tagId) throws Exception;

    /*
     * 获取对外开放且已经打上标签的图层
     */
    List<AgTagLayer> getOpenLayerwithTag(String tagId, String catalogId, String serviceName) throws Exception;

    /*
    * 批量插入标签图层
    * */
    void insertTagLayer(List<AgTagLayer> tagLayers) throws Exception;

    /*
    * 删除标签图层
    * */
    void removeTayLayerByLayerIds(String layerIds) throws Exception;


    List<AgTagLayer> countTagLayerApplyNum() throws Exception;


    int wetherCanDeleteTag(String id, String isTag) throws Exception ;
}

