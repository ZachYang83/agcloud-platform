package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgTimerService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by caokp on 2017-08-29.
 */
@Mapper
public interface AgTimerServiceMapper {

    AgTimerService findById(@Param("id") String id) throws Exception;

    List<AgTimerService> findList(AgTimerService agTimerService) throws Exception;

    void save(AgTimerService agTimerService) throws Exception;

    void update(AgTimerService agTimerService) throws Exception;

    void delete(@Param("id") String id) throws Exception;
}
