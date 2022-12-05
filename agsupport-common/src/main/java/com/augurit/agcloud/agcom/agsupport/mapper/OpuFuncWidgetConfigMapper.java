package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.OpuFuncWidgetConfig;
import com.augurit.agcloud.opus.common.domain.OpuRsFunc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OpuFuncWidgetConfigMapper {
    int deleteByPrimaryKey(String id);

    int insert(OpuFuncWidgetConfig record);

    int insertSelective(OpuFuncWidgetConfig record);

    OpuFuncWidgetConfig selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OpuFuncWidgetConfig record);

    int updateByPrimaryKeyWithBLOBs(OpuFuncWidgetConfig record);

    int updateByPrimaryKey(OpuFuncWidgetConfig record);

    List<OpuRsFunc> findList(@Param("name")String name);

    List<OpuFuncWidgetConfig> getConfigData(@Param("funcCode")String funcCode);

    List<OpuFuncWidgetConfig> searchByParams(OpuFuncWidgetConfig record);


    List<OpuFuncWidgetConfig> getAllConfigDataForRest(@Param("funcCode")String funcCode,@Param("configKey")String configKey);

    OpuFuncWidgetConfig getConfigDataByKey(@Param("funcInvokeUrl")String funcInvokeUrl,@Param("funcCode")String funcCode,@Param("configKey")String configKey);

    /**
     * 根据微件访问地址或微件编码获取微件配置信息列表
     * @param funcInvokeUrl
     * @param funcCode
     * @return
     */
    List<OpuFuncWidgetConfig> getConfigListByUrlOrCode(@Param("funcInvokeUrl")String funcInvokeUrl,@Param("funcCode")String funcCode);
}