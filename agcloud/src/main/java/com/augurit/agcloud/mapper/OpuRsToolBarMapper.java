package com.augurit.agcloud.mapper;

import com.augurit.agcloud.domain.OpuRsToolBar;
import com.augurit.agcloud.opus.common.domain.OpuRsAppSoft;
import com.augurit.agcloud.opus.common.domain.OpuRsFunc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OpuRsToolBarMapper {
    public void updateToolBar(OpuRsToolBar func);

    public void insertToolBar(OpuRsToolBar func);

    List<OpuRsToolBar> listFuncsBySoftIdAndSorting(@Param("appSoftId") String appSoftId, @Param("keyword") String keyword);

    OpuRsToolBar getModuleOrFuncById(@Param("funcId")String funcId);

    OpuRsAppSoft getAppSoftBySoftCode(@Param("softCode")String softCode);

    List<OpuRsToolBar> getToolBarByAppSoftCode(@Param("appSoftId")String appSoftId);

    void deleteToolBarByName(@Param("funcName")String name);

    int editPagePosition(String funcId, String pagePosition);
}
