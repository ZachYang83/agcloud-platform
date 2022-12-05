package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgSysLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zhangmingyang
 * @Description: TODO
 * @date 2018-12-05 10:39
 */
@Mapper
public interface AgSysLogMapper {
    List<AgSysLog> findList(AgSysLog agSysLog) throws Exception;

    int save(AgSysLog agSysLog)throws Exception;

    List<Map> findSysLoginLog(@Param("startTime") String startTime,@Param("flag") String flag);
}
