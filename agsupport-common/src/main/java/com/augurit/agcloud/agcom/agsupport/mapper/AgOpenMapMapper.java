package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AgOpenMapMapper {

    List<AgTagLayer> findLayerAndTag(@Param("loginName") String loginName) throws Exception;

    List<AgOpenMapLayer> findOpenLayers(@Param("keyWord") String keyWord) throws Exception;

    AgOpenMapApplyItem findApplyItemByLayerIdAndApplicant(@Param("layerId") String layerId, @Param("loginName") String loginName) throws Exception;

    List<AgOpenMapApplyProcess> findApplyProcessByApplyItemId(@Param("applyItemId") String applyItemId) throws Exception;

    void addApplyItem(AgOpenMapApplyItem applyItem) throws Exception;

    void saveApplyFile(AgOpenMapAttachFile fileInfo) throws Exception;

    AgOpenMapAttachFile getFileInfo(@Param("id") String id) throws Exception;

    void addApplyProcess(AgOpenMapApplyProcess applyProcess) throws Exception;

    void addAuditList(AgOpenMapAuditList auditList) throws Exception;

    List<AgOpenMapAuditList> findAuditList(@Param("loginName") String loginName, @Param("tabType") String tabType, @Param("serialNo") String serialNo, @Param("applicant") String applicant, @Param("keyWord") String keyWord, @Param("auditStatus") String auditStatus) throws Exception;

    List<AgOpenMapApplyItem> findApplyItemBySerialNo(@Param("serialNo") String serialNo) throws Exception;

    AgOpenMapLayer findOpenLayerById(@Param("id") String id) throws Exception;


    AgOpenMapAuditList findAuditListBySerialNo(@Param("serialNo") String serialNo) throws Exception;

    void updateAuditList(@Param("id") String id, @Param("auditStatus") String auditStatus, @Param("auditor") String auditor, @Param("opinion") String opinion) throws Exception;

    void updateApplyItem(@Param("id") String id, @Param("currentProcessStatus") String currentProcessStatus, @Param("proxyURL") String proxyURL, @Param("auditor") String auditor) throws Exception;

    List<AgOpenMapAttachFile> findApplyFilesByApplyItemId(@Param("applyItemId") String applyItemId) throws Exception;

    void updateApplyItemByOldApplyItem(AgOpenMapApplyItem applyItem) throws Exception;

    void updateAuditListByOldAuditList(AgOpenMapAuditList oldAuditList) throws Exception;

    List<AgOpenMapCartItem> findCartItem(@Param("layerId") String layerId, @Param("loginName") String loginName) throws Exception;

    void saveCartItem(AgOpenMapCartItem cartItem) throws Exception;

    void delCartItem(@Param("id") String id) throws Exception;

    void createApplyItemAndLayerIdList(@Param("applyItemId") String applyItemId, @Param("layerId") String layerId, @Param("loginName") String loginName) throws Exception;

    void insertApplyItemAndLayerIdListBatch(@Param("applyItemId") String applyItemId, @Param("layerIds") List<String> layerIds, @Param("loginName") String loginName) throws Exception;

    AgOpenMapApplyItem findApplyItemByLayerIdAndLoginName(@Param("layerId") String layerId, @Param("loginName") String loginName) throws Exception;

    void delCartItemByLayerIdAndLoginName(@Param("layerId") String layerId, @Param("loginName") String loginName) throws Exception;

    List<AgOpenMapLayer> findOpenLayersByCondition(@Param("loginName") String loginName, @Param("tagId") String tagId, @Param("keyWord") String keyWord, @Param("tabType") String tabType, @Param("applyStatus") String applyStatus) throws Exception;

    String getBeforeProxyId(@Param("layerId") String layerId) throws Exception;

    List<AgOpenMapApplyItem> findUserApplyItemByLoginName(@Param("loginName") String loginName) throws Exception;

    List<AgOpenMapApplyItem> findApplyItemByLayerIds(@Param("layerIds") List<String> layerIds) throws Exception;

    // 根据layerIds删除关联的申请信息
    void delApplyItemByLayerIds(@Param("layerIds") List<String> layerIds);
    // 根据申请id批量删除关联的信息
    void delApplyItemByApplyId(@Param("applyItemByLayerIds") List<AgOpenMapApplyItem> applyItemByLayerIds);
    void delAuditlistByNo(@Param("applyItemByLayerIds") List<AgOpenMapApplyItem> applyItemByLayerIds);
    void delAttachfile(@Param("applyItemByLayerIds") List<AgOpenMapApplyItem> applyItemByLayerIds);
    void delApplyCartitemByLayerIds(@Param("layerIds") List<String> layerIds);
    void delApplyItemById(@Param("itemID") String itemID);
    void delApplyProcessByItemId(@Param("itemID") String itemID);
//    void delAuditListByItemId(@Param("itemID") String itemID);
    void delApplyProxyService(@Param("userId") String userId, @Param("serviceId") String serviceId) throws Exception;

    /*根据目录和关键字获取对外图层*/
    List<AgOpenMapLayer> findListOfExternal(@Param("xpath") String xpath,@Param("keyWord") String keyWord) throws Exception;

    /*根据目录、关键字、申请状态获取用户申请过的图层*/
    List<AgOpenMapLayer> findListOfExternalByUser(@Param("applicantLoginName") String applicantLoginName,@Param("xpath") String xpath,@Param("keyWord") String keyWord,@Param("applyStatus") String applyStatus) throws Exception;

    /*根据目录、关键字、申请状态获取用户申请过的图层，并过滤掉重复的*/
    List<AgOpenMapLayer> findListOfExternalByUserWithDistinct(@Param("applicantLoginName") String applicantLoginName,@Param("xpath") String xpath,@Param("keyWord") String keyWord,@Param("applyStatus") String applyStatus) throws Exception;

    /*获取用户持有的图层(持有的图层是指申请中的 或 通过审核且未过期的)*/
    List<AgOpenMapLayer> findValidListOfExternalByUser(@Param("applicantLoginName") String applicantLoginName) throws Exception;

    AgOpenMapLayer  findOpenMapLayerByDirLayerId(@Param("dirLayerId") String dirLayerId) throws Exception;

    List<AgOpenMapApply> findApplyByApplicantLoginNameAndDirLayerId(@Param("applicantLoginName") String applicantLoginName,@Param("dirLayerId") String dirLayerId) throws Exception;

    List<AgOpenMapApplyProcess> findApplyProcessByApplyId(@Param("applyId") String applyId) throws Exception;

    //按目录统计图层数量
    List<LayerCountByDir> getLayerCountGroupByDir() throws Exception;

    //按目录统计用户申请的的图层数量
    List<LayerCountByDir> getUserApplyLayerCountGroupByDir(@Param("loginName") String loginName) throws Exception;

    void addApply(AgOpenMapApply apply) throws Exception;

    AgOpenMapApply findApplyById(@Param("id") String id) throws Exception;

    List<AgOpenMapApplyItem> findApplyItemByApplyId(@Param("applyId") String applyId) throws Exception;

    //查找指定申请的所有图层名称
    List<String> findApplyLayerNameByApplyId(@Param("applyId") String applyId) throws Exception;

    List<AgOpenMapAttachFile> findApplyAttachFileByApplyId(@Param("applyId") String applyId) throws Exception;
    List<AgOpenMapAttachFile> findApplyAttachFileByApplyIds(@Param("applyIds") List<String> applyIds) throws Exception;

    void updateApplyForRepeatApply(AgOpenMapApply apply) throws Exception;

    void updateApplyForAudit(AgOpenMapApply apply) throws Exception;

    List<AgOpenMapCartItem> findCartItemByLoginName(@Param("loginName") String loginName) throws Exception;

    void delCartItemByDirLayerIdAndLoginName(@Param("dirLayerId") String layerId, @Param("loginName") String loginName) throws Exception;

    List<AgOpenMapApplyItem> findApplyItemByApplicantLoginName(@Param("applicantLoginName") String applicantLoginName) throws Exception;

    List<AgOpenMapApply> findApplyByApplicantLoginName(@Param("applicantLoginName") String applicantLoginName) throws Exception;

    void deleteAttachfileByApplyId(@Param("applyId") String applyId);

    List<AgOpenMapAuditList> findAuditListRest(@Param("auditorLoginName") String auditorLoginName,  @Param("keyWord") String keyWord, @Param("auditStatus") String auditStatus) throws Exception;


    void updateApplyItemRest(AgOpenMapApplyItem applyItem) throws Exception;

    void delApplyItemByApplyIdRest(@Param("applyId") String applyId) throws Exception;




}
