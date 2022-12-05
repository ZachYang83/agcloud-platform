package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayerField;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayerFieldConf;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Lidw on 2017-04-27.
 */
@Mapper
public interface AgLayerFieldMapper {

    /**
     * 按图层id查找
     *
     * @param layerId
     * @return
     * @throws Exception
     */
    List<AgLayerField> findByLayerId(@Param("layerId") String layerId) throws Exception;

    /**
     * 按图层id查找
     *
     * @param id
     * @return
     * @throws Exception
     */
    AgLayerField findById(@Param("id") String id) throws Exception;

    /**
     * 删除
     *
     * @param id
     */
    void delete(@Param("id") String id) throws Exception;

    void deleteBacthByLayerIds(@Param("ids") List ids) throws Exception;

    AgLayerField getLayerFieldByLayerIdAndFieldName(@Param("layerId") String layerId, @Param("fieldName") String fieldName) throws Exception;

    List<AgLayerField> getFieldNameAlias(String layerId)throws Exception;

    Boolean insertOrUpdateLayerFieldList(@Param("agLayerFields") List<AgLayerFieldConf> agLayerFields);
}
