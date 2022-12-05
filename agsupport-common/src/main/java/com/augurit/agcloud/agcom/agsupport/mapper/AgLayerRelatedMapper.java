package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayerRelated;
import com.augurit.agcloud.agcom.agsupport.domain.DirLayer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Augurit on 2017-04-18.
 */
@Mapper
public interface AgLayerRelatedMapper {

    List<AgLayerRelated> getByServiceDirLayerId(@Param("serviceDirLayerId") String serviceDirLayerId) throws Exception;
    AgLayerRelated findById(@Param("id") String id) throws Exception;
    /*根据dataSourceId查询属性表*/
    List<DirLayer> findPropertyTablesByDataSourceId(@Param("dataSourceId") String dataSourceId);
    boolean save(AgLayerRelated agLayerRelated) throws Exception;
    boolean delete(@Param("id") String id) throws Exception;
    boolean deleteBatch(@Param("ids") List<String> ids);
    boolean update(AgLayerRelated agLayerRelated) throws Exception;

    List<AgLayerRelated> getByServiceDirLayerIdAndRelatedDirLayerId(@Param("serviceDirLayerId") String serviceDirLayerId,@Param("relatedDirLayerId") String relatedDirLayerId) throws Exception;


}
