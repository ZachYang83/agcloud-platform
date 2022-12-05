package com.augurit.agcloud.agcom.agsupport.sc.func.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgFunc;
import com.augurit.agcloud.agcom.agsupport.domain.OpuRsToolBar;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.opus.common.domain.OpuRsAppSoft;

import java.util.List;

/**
 * Created by Augurit on 2017-05-02.
 */
public interface IAgFunc {

    /**
     * 按用户查询功能
     *
     * @param userId
     * @return
     * @throws Exception
     */
    List<AgFunc> findFuncByUser(String userId, String isMobile, Boolean isTree, String appSoftCode, String isCloudSoft, String isAdmin) throws Exception;

    OpuRsToolBar saveToolBar(OpuRsToolBar opuRsToolBar);

    List<ElementUiRsTreeNode> lltreeFunc(String appSoftId, String funcCode, String keyword, boolean b);

    OpuRsToolBar getModuleOrFuncById(String funcId);

    List<OpuRsToolBar> getToolBarByAppSoftId(String appSoftId);

    OpuRsAppSoft getAppSoftBySoftCode(String softCode);

    void deleteToolBarByName(String name);
}
