package com.augurit.agcloud.agcom.agsupport.sc.field.service;

import com.augurit.agcloud.agcom.agsupport.domain.*;

import java.util.List;

/**
 * Created by Lidw on 2017-04-27.
 */
public interface IAgField {
    AgUserLayer getLayerConfigByUserId(String dirLayerId, String userId) throws Exception;

    List<AgLayerFieldConf> getLayerFields(String dirLayerId, String layerId) throws Exception;

    List<AgLayerFieldConf> getLayerFieldsByUserId(String dirLayerId, String layerId) throws Exception;

    List<AgLayerFieldConf> getRefreshFields(String userLayerId, String layerId) throws Exception;

    void saveLayerFields(List<AgLayerFieldConf> list) throws Exception;

    void deleteLayerFields(List<AgLayerFieldConf> list) throws Exception;

    List<AgFieldAuthorize> findFieldAuthorizeByRoleLayerId(String roleLayerId) throws Exception;

    List<AgFieldAuthorize> findFieldAuthorizeByFieldId(String fieldId) throws Exception;

    void saveFieldAuthorize(List<AgFieldAuthorize> fieldAuthorizelist) throws Exception;

    void deleteFieldAuthorize(String id) throws Exception;

    List<AgLayerField> findLayerFieldByLayerId(String layerId) throws Exception;

    void deleteLayerField(String id) throws Exception;

    void saveLayerConfig(AgUserLayer agUserLayer);

    AgLayerField getLayerFieldById(String id) throws Exception;

    AgLayerField getLayerFieldByLayerIdAndFieldName(String layerId, String fieldName) throws Exception;

    public List<AgLayerField> getFieldNameAlias(String layerId) throws Exception;
}
