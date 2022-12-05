package com.augurit.agcloud.agcom.syslog.mapper;

import com.augurit.agcloud.agcom.syslog.domain.CommonSysLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonSysLogMapper {
    void save(CommonSysLog commonSysLog);
}
