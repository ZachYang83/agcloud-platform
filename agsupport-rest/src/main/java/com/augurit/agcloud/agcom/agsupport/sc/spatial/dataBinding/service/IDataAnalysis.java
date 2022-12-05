package com.augurit.agcloud.agcom.agsupport.sc.spatial.dataBinding.service;

import java.util.List;
import java.util.Map;

/**
 * Created by chendingxing on 2018-03-15.
 */
public interface IDataAnalysis {
    List<Map> bufferAnalysis(String userId, String dirLayerId1, String dirLayerId2, double distance) throws Exception;
}
