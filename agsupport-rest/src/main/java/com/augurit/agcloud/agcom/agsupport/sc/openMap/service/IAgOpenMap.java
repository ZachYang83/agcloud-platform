package com.augurit.agcloud.agcom.agsupport.sc.openMap.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgOpenMapApply;
import com.augurit.agcloud.agcom.agsupport.domain.AgOpenMapAttachFile;
import com.augurit.agcloud.agcom.agsupport.domain.AgOpenMapCartItem;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;

public interface IAgOpenMap {
    /*获取目录树及目录图层数量*/
    ContentResultForm getDirTreeOfOpenMap();

    /*获取目录树，并根据用户统计目录图层数量*/
    ContentResultForm getDirTreeOfOpenMapByUserId(String loginName);

    /*
     * 获取对外开放的图层
     * @param
     */
    ContentResultForm getLayersOfExternal(String loginName,String dirId, String keyWord, Page page);

    /*
     * 根据用户获取对外开放的图层
     * @param
     */
    ContentResultForm getLayersOfExternalForUser(String loginName, String dirId, String keyWord , String applyStatus, Page page);

    /*
     * 获取信息详情
     * @param
     */
    ContentResultForm getDetails(String loginName, String dirLayerId);

    /*提交申请表*/
    ResultForm submitApplyTable(AgOpenMapApply applyInfo);

    /*驳回后重新提交申请表*/
    ResultForm repeatSubmitApplyTable(AgOpenMapApply applyInfo);

    ContentResultForm getAuditList(String loginName, String keyWord, String auditStatus,Page page);

    ResultForm auditApply(String loginName,String applyId,String auditResult,String auditOpinion);



    ContentResultForm findCartItemByLoginName(String loginName);

    ResultForm saveCartItem(AgOpenMapCartItem cartItem);

    ResultForm delCartItem(String id);




    /*
     * 保存申请材料文件信息
     */
    void saveApplyFile(AgOpenMapAttachFile fileInfo) throws Exception;

    /*
     * 保存申请材料文件Id 获取文件信息
     */
    AgOpenMapAttachFile getFileInfo(String id) throws Exception;

}
