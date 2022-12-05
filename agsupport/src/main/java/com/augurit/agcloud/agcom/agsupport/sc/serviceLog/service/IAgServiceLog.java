package com.augurit.agcloud.agcom.agsupport.sc.serviceLog.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgServiceLog;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-12-08.
 */
public interface IAgServiceLog {

    List<AgServiceLog> getLogOrderByUser() throws Exception;

    List<AgServiceLog> getLogOrderByService() throws Exception;


    List<AgServiceLog> getLogByServiceId(String serviceId) throws Exception;

    Map getHotServiceRecentAllByte() throws Exception; //获取热门服务最近访问的流量总量

}
