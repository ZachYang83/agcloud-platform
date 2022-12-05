package com.augurit.agcloud.service;

import com.augurit.agcloud.domain.OpuRsToolBar;
import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.agcloud.opus.common.domain.OpuRsAppSoft;

import java.util.List;

public interface OpuRsToolBarService {
    OpuRsToolBar saveToolBar(OpuRsToolBar opuRsToolBar);

    List<ElementUiRsTreeNode> lltreeFunc(String appSoftId,String funcCode, String keyword, boolean b);

    OpuRsToolBar getModuleOrFuncById(String funcId);

    List<OpuRsToolBar> getToolBarByAppSoftId(String appSoftId);

    OpuRsAppSoft getAppSoftBySoftCode(String softCode);

    void deleteToolBarByName(String name);

    boolean editPagePosition(String funcId, String pagePosition);
}
