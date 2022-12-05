package com.augurit.agcloud.agcom.agsupport.sc.spatial.dataBinding.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;

import java.util.List;
import java.util.Map;

/**
 * Created by chendingxing on 2018-03-15.
 */
public interface IDataBinding {

    List<AgLayer> findListByUserIdAndType(String userId, String type, String featureType) throws Exception;

    List<AgLayer> findAllByUserId(String userId, String type) throws Exception;

    List<AgLayer> findAllEditableByUserId(String userId, String type) throws Exception;

    List<Map> findBindingData(String userId, String dirLayerId, List<String> conditions) throws Exception;

    List<Map> queryData(String userId, String tableName, List<String> conditions) throws Exception;

    List findFieldByTableId(String dirLayerId, String userId) throws Exception;

    List findAllFieldByTableId(String dirLayerId, String userId) throws Exception;
}
