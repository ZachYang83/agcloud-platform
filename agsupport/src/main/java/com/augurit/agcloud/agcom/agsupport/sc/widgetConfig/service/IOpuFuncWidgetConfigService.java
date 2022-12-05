package com.augurit.agcloud.agcom.agsupport.sc.widgetConfig.service;

import com.augurit.agcloud.agcom.agsupport.domain.OpuFuncWidgetConfig;


import com.augurit.agcloud.opus.common.domain.OpuRsFunc;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IOpuFuncWidgetConfigService{
    int deleteByPrimaryKey(String id);

    int insert(OpuFuncWidgetConfig record);

    int insertSelective(OpuFuncWidgetConfig record);

    OpuFuncWidgetConfig selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OpuFuncWidgetConfig record);

    int updateByPrimaryKeyWithBLOBs(OpuFuncWidgetConfig record);

    int updateByPrimaryKey(OpuFuncWidgetConfig record);

    PageInfo<OpuRsFunc> searchParam(String name, Page page) throws Exception;

    List<OpuFuncWidgetConfig> getConfigData(String funcCode) throws Exception;

    void saveDataList(List<OpuFuncWidgetConfig> dataList) throws Exception;

    void save(OpuFuncWidgetConfig record) throws Exception;

    void saveWidget(String funcId,String funcName) throws Exception;

    void deleteConfigData(String[] ids) throws Exception;

    void deleteWidget(String[] ids) throws Exception;

    List<OpuFuncWidgetConfig> searchByParams(OpuFuncWidgetConfig record) throws Exception;

    List<OpuFuncWidgetConfig> getAllConfigDataForRest(String funcCode,String key) throws Exception;

    OpuFuncWidgetConfig getConfigDataByKey(String url,String funcCode,String configKey)throws Exception;
}