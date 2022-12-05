package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayerExtend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Augurit on 2017-04-21.
 */
@Mapper
public interface AgLayerExtendMapper {

    List<AgLayerExtend> findListByParentId(@Param("parentId") String parentId) throws Exception;

    void deleteByParentId(@Param("parentId") String parentId) throws Exception;

    void save(AgLayerExtend agLayerExtend) throws Exception;

    void saveBatch(List<AgLayerExtend> list) throws Exception;
}
