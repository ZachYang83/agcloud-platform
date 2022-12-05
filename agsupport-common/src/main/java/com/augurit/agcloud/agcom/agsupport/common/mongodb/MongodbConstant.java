package com.augurit.agcloud.agcom.agsupport.common.mongodb;

/**
 * @author zhangmingyang
 * @Description: mongo 集合相关常量
 * @date 2019-09-26 9:28
 */
public interface MongodbConstant {
    /** 服务日志记录集合 */
    String AGCOM_SERVICE_LOG = "agcom-service-log";

    /**
     * rest 服务调用日志topic
     */
    String AGSUPPORT_REST_SERVICE_LOG = "agsupport-rest-service-log";

    /** 系统日志集合名称*/
    String AG_SYS_LOG = "ag-sys-log";
}
