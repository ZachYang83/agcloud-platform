package com.augurit.agcloud.agcom.syslog.service.service;

import com.augurit.agcloud.agcom.syslog.domain.CommonSysLog;

public interface CommonSysLogService {
    void save(CommonSysLog commonSysLog);

    void saveToMongodb(CommonSysLog commonSysLog,String collectionName);
}
