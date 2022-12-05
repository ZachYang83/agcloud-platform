package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgShare;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Augurit on 2017-04-18.
 */
@Mapper
public interface AgLayerMapper {

    List<AgLayer> findList(@Param("name") String name, @Param("isVector") String isVector) throws Exception;

    List<AgLayer> findAll() throws Exception;

    List<AgLayer> findListByXpath(@Param("agLayer") AgLayer agLayer, @Param("xpath") String xpath, @Param("userId") String userId, @Param("isContain") String isContain, @Param("isBaseMap") String isBaseMap) throws Exception;

    List<AgLayer> findListByXpathUsers(@Param("agLayer") AgLayer agLayer, @Param("xpath") String xpath, @Param("userList") List<AgUser> userList, @Param("isContain") String isContain, @Param("isBaseMap") String isBaseMap) throws Exception;

    List<AgLayer> findListByUserIdAndAgLayer(@Param("name") String name, @Param("userId") String userId) throws Exception;

    List<AgLayer> findFromExternalLayers(@Param("name") String name, @Param("userId") String userId,@Param("status") String status) throws Exception;

    List<AgLayer> findListByUserId(@Param("userId") String userId) throws Exception;

    List<AgLayer> findPageByUserId(@Param("userId") String userId) throws Exception;

    List<AgLayer> findListByUsers(@Param("userList") List<AgUser> userList) throws Exception;

    List<AgLayer> findBaseLayerByUserId(@Param("userId") String userId) throws Exception;

    List<AgLayer> findBaseLayerByUsers(@Param("userList") List<AgUser> userList) throws Exception;

    List<AgLayer> findListByUserIdAndXpath(@Param("userId") String userId, @Param("xpath") String xpath) throws Exception;

    List<AgLayer> findListByUsersAndXpath(@Param("userList") List<AgUser> userList, @Param("xpath") String xpath) throws Exception;

    List<AgLayer> findListByUserIdAndFeatureType(@Param("userId") String userId, @Param("featureType") String featureType) throws Exception;

    List<AgLayer> findListByUsersAndFeatureType(@Param("userList") List<AgUser> userList, @Param("featureType") String featureType) throws Exception;

    List<AgLayer> findVectorList() throws Exception;

    List<AgLayer> findListByDirLayer() throws Exception;

    List<AgLayer> findListByKeyWord(@Param("type") String type, @Param("keyWord") String keyWord, @Param("year") String year) throws Exception;

    List<AgLayer> findListByXpathAndLayerName(@Param("xpath") String xpath, @Param("name") String name, @Param("userId") String userId) throws Exception;

    List<AgLayer> findListByXpathAndLayerNameUsers(@Param("xpath") String xpath, @Param("name") String name, @Param("userId") String userId) throws Exception;

    List<AgLayer> findListByDirNameAndLayerName(@Param("dirName") String dirName, @Param("layerName") String layerName, @Param("userId") String userId) throws Exception;

    List<AgLayer> findListByDirNameAndLayerNameUsers(@Param("dirName") String dirName, @Param("layerName") String layerName, @Param("userId") String userId) throws Exception;

    AgLayer findByLayerId(@Param("layerId") String layerId) throws Exception;

    AgLayer findByLayerUrl(@Param("url") String url) throws Exception;

    AgLayer findByDirLayerId(@Param("dirLayerId") String dirLayerId) throws Exception;

    List<AgLayer> findByDirLayerIdsNotWithData(@Param("dirLayerIds") List<String> dirLayerIds) throws Exception;

    AgLayer findByDLidAndUid(@Param("dirLayerId") String dirLayerId, @Param("userId") String userId) throws Exception;

    AgLayer findByDLidAndUsers(@Param("dirLayerId") String dirLayerId, @Param("userList") List<AgUser> userList) throws Exception;

    AgLayer findByUrl(@Param("url") String url, @Param("layerTable") String layerTable) throws Exception;

    AgLayer findLayerByUrl(@Param("url") String url) throws Exception;
    AgLayer findByNameCn(@Param("nameCn") String nameCn) throws Exception;

    void save(AgLayer agLayer) throws Exception;

    void update(AgLayer agLayer) throws Exception;

    void delete(@Param("id") String id) throws Exception;

    void deleteLayerBatch(@Param("list") List<String> list) throws Exception;

    void putPreviewKey(AgShare agShare) throws Exception;

    AgShare getPreviewKey(@Param("shareKey") String shareKey) throws Exception;

    void updatePreviewKey(AgShare agShare) throws Exception;

    List<AgLayer> findListByLayerIds(@Param("ids") String[] layerIds) throws Exception;

    List<AgLayer> findListByUserIdAndType(@Param("userId") String userId, @Param("type") String type) throws Exception;

    List<AgLayer> findListByUsersAndType(@Param("userList") List<AgUser> userList, @Param("type") String type) throws Exception;

    boolean filterByFeatureType(@Param("vectorLayerId") String vectorLayerId, @Param("featureType") String featureType) throws Exception;

    String getVectorLayerFeatureType(@Param("vectorLayerId") String vectorLayerId) throws Exception;

    List<AgLayer> findEditableListByUserIdAndType(@Param("userId") String userId, @Param("type") String type) throws Exception;

    List<AgLayer> findEditableListByUsersAndType(@Param("userList") List<AgUser> userList, @Param("type") String type) throws Exception;

    AgLayer findLayerBylayerTableAndUserId(@Param("userId") String userId, @Param("layerTable") String layerTable) throws Exception;

    AgLayer findLayerBylayerTableAndUsers(@Param("userList") List<AgUser> userList, @Param("layerTable") String layerTable) throws Exception;

    List<AgLayer> getAllAgentUrl() throws Exception;

    List<AgLayer> findVectorLayer() throws Exception;

    List<AgLayer> findDataSourceName(@Param("dataSourceId") String dataSourceId) throws Exception;
    Integer findUserlayerCount(String userId) throws Exception;

    List<AgLayer> findByName(@Param("name") String name,@Param("nameCn") String nameCn);

    List<AgLayer> findLayerByNameAndStatus(@Param("name") String name,@Param("status") String status) throws Exception;

    void changeStatus(@Param("id") String id, @Param("status") String status) throws Exception;

    void disable(@Param("id") String id,@Param("status") String status)throws Exception;

    void setHandleRules(AgLayer agLayer) throws Exception;

    /** 查询出已启用的图层 */
    AgLayer findByLayerIdAndEnabled(@Param("layerId") String layerId) throws Exception;
    
    /**
     *
     * @Author: libc 
     * @Date: 2020/10/12 15:01
     * @tips:
     * @param styleManagerId 样式id
     * @return 
     */
    List<AgLayer> findByStyleManagerId(@Param("styleManagerId") String styleManagerId);
}
