package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetAssetsProject;
import org.apache.ibatis.annotations.Mapper;

/**
 *
 * @Author: Zihui Li
 * @Date: 2020/11/19
 * @tips: mapper接口
 */
@Mapper

public interface AgWidgetAssetsProjectCustomMapper {

    AgWidgetAssetsProject selectByAppSoftId(String appSoftId);

    String getUniqueIdBySoftCode(String softCode);

    String getAppSoftIdBySoftCode(String softCode);

}
