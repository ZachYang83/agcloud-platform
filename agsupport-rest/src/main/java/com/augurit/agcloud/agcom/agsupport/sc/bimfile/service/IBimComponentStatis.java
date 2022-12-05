package com.augurit.agcloud.agcom.agsupport.sc.bimfile.service;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.common.util.page.Pager;

import java.util.List;
import java.util.Map;


public interface IBimComponentStatis {

    /**
     * 根据BIM模型名称统计模型构件
     *
     * @return
     * @throws Exception
     */
    public Map componentStatisticsBy(String tableName, String datasourceId, Pager page) throws Exception;


    /**
     * 根据BIM模型名称统计模型构件
     *
     * @param tableName
     * @return
     * @throws Exception
     */
    boolean bimAttributeDataIsExist(String tableName, String datasourceId) throws Exception;

}
