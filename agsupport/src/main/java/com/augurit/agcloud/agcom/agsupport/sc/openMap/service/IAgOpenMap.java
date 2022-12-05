package com.augurit.agcloud.agcom.agsupport.sc.openMap.service;

import com.augurit.agcloud.agcom.agsupport.domain.*;

import java.util.List;

public interface IAgOpenMap {

    /*
     * 获取标签图层
     * @param
     */
    List<AgTagLayer> findLayerAndTag(String loginName) throws Exception;


    /*
     * 获取对外开放的所有图层
     * @param
     */
    List<AgOpenMapLayer> findOpenLayers(String loginName, String tagId, String tabType, String keyWord, String applyStatus, Integer pageNum, Integer pageSize) throws Exception;

    List<AgOpenMapLayer> findOpenLayersByconditons(String loginName, String tagId, String tabType, String keyWord, String applyStatus, Integer pageNum, Integer pageSize) throws Exception;

    /*
     * 创建申请Item
     * @param
     */
    AgOpenMapApplyItem createApplyItemByInfo(String serialNo, String layerId, String applyFor, String secrecy, String obtainWay, String obtainWayDesc, int validityDate, String ip, AgCloudUser user) throws Exception;


    /*
    * 保存申请材料文件信息
    */
    void saveApplyFile(AgOpenMapAttachFile fileInfo) throws Exception;

    /*
     * 保存申请材料文件Id 获取文件信息
     */
    AgOpenMapAttachFile getFileInfo(String id) throws Exception;

    /*
     * 创建申请处理流程
     */
    void createApplyProcess(AgOpenMapApplyProcess applyProcess) throws Exception;

    /*
     * 创建审核表单
     */
    void createAuditList(AgOpenMapAuditList auditList) throws Exception;


    List<AgOpenMapAuditList> findAuditList(String loginName, String tabType, String serialNo, String applicant, String keyWord, String auditStatus) throws Exception;

    List<AgOpenMapApplyItem> findApplyItemBySerialNo(String serialNo) throws Exception;

    AgOpenMapAuditList findAuditListBySerialNo(String serialNo) throws Exception;

    void updateAuditList(String id, String auditStatus, String auditor, String opinion) throws Exception;

    void updateApplyItem(String id, String currentProcessStatus, String proxyURL, String auditor) throws Exception;

//  List<FileInfo> findApplyFilesByApplyItemId(String applyItemId) throws Exception;

    AgOpenMapApplyItem findApplyItemByLayerIdAndApplicant(String layerId, String loginName) throws Exception;

    void updateApplyItemByOldApplyItem(AgOpenMapApplyItem applyItem) throws Exception;

    void updateAuditListByOldAuditList(AgOpenMapAuditList oldAuditList) throws Exception;

    List<AgOpenMapCartItem> findCartItem(String layerId, String loginName) throws Exception;

    void saveCartItem(AgOpenMapCartItem cartItem) throws Exception;

    void delCartItem(String id) throws Exception;

    void createApplyItemAndLayerIdList(String applyItemId, String layerId, String loginName) throws Exception;

    void createBatchApplyItemAndLayerIdList(String applyItemId, List<String> layerIds, String loginName) throws Exception;

    AgOpenMapApplyItem findApplyItemByLayerIdAndLoginName(String layerId, String loginName) throws Exception;


    void delCartItemByLayerIdAndLoginName(String layerId, String loginName) throws Exception;

    String getBeforeProxyId(String layerId) throws Exception;



}
