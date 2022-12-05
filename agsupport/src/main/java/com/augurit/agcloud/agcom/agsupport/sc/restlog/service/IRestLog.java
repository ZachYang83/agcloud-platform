package com.augurit.agcloud.agcom.agsupport.sc.restlog.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgSysLog;
import com.github.pagehelper.Page;

import java.util.Map;

public interface IRestLog {
    Map<String,Object> search(AgSysLog agSysLog, Page page) throws Exception;
}
