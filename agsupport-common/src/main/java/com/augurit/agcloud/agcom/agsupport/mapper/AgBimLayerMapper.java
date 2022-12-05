package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgDirLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Augurit on 2017-04-25.
 */
@Mapper
public interface AgBimLayerMapper {

    AgDirLayer findByDirIdAndLayerId(@Param("dirId") String dirId, @Param("layerId") String layerId) throws Exception;

    List<AgDirLayer> findByDirIdAndLayerIds(@Param("dirId") String dirId, @Param("layerIds") List layerIds) throws Exception;

    AgDirLayer findByIdAndUserId(@Param("id") String id, @Param("userId") String userId) throws Exception;

    AgDirLayer findByIdAndUsers(@Param("id") String id, @Param("userList") List<AgUser> userList) throws Exception;

    List<AgDirLayer> findByIds(@Param("ids") String[] ids) throws Exception;

    AgDirLayer findById(@Param("id") String id)throws Exception;

    List<AgDirLayer> findListByDirXpath(@Param("xpath") String dirXpath) throws Exception;

    List<AgDirLayer> findListByLayerId(@Param("layerId") String layerId) throws Exception;

    List<AgDirLayer> findListByUserId(@Param("userId") String roleId) throws Exception;

    String getMaxOrder() throws Exception;

    void save(AgDirLayer agDirLayer) throws Exception;

    void updateBatch(List<AgDirLayer> list) throws Exception;

    void delete(@Param("id") String id) throws Exception;
    void deleteBatchByIds(@Param("agDirLayers") List<AgDirLayer> agDirLayers) throws Exception;

    List<AgDirLayer> findAll() throws Exception;
    List<AgDirLayer> findByDirId(@Param("dirId") String dirId) throws Exception;
}
