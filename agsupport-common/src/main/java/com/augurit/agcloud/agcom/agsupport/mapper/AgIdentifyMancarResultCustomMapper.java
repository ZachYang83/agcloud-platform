package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgIdentifyMancarResult;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgIdentifyMancarResultExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *
 * @Author: qinyg
 * @Date: 2020/12/30
 * @tips: mapper接口
 */
@Mapper
public interface AgIdentifyMancarResultCustomMapper {

    AgIdentifyMancarResult statisticsPeopleAndCar(@Param("sourceId") String sourceId,
                                                  @Param("identifyTimeStart") Date identifyTimeStart,
                                                  @Param("identifyTmeEnd") Date identifyTmeEnd);
}