package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgDirLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgProjectdirLayer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Augurit on 2017-04-25.
 */
@Mapper
public interface AgProjectdirLayerMapper {
    AgProjectdirLayer findByDirIdAndLayerId(@Param("dirId") String dirId, @Param("layerId") String layerId) throws Exception;

    void save(AgProjectdirLayer agProjectdirLayer) throws Exception;

    String getMaxOrder() throws Exception;

    List<AgProjectdirLayer> findListByDirId(@Param("dirId") String dirId) throws Exception;

    List<AgProjectdirLayer> findByIds(@Param("dirId") String dirId, @Param("ids") String[] ids) throws Exception;

    void updateBatch(List<AgProjectdirLayer> list) throws Exception;
    void delete(@Param("id") String id) throws Exception;

    void deleteBacthByLayerIds(@Param("layerIds") List<AgDirLayer> layerIds) throws Exception;
    void deleteByDirIdAndLayerIds(@Param("dirId") String dirId, @Param("layerIds") String[] layerIds) throws Exception;

    void update(AgProjectdirLayer agProjectdirLayer) throws Exception;


}
