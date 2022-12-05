package com.augurit.agcloud.agcom.agsupport.domain;

import java.util.Date;

/**
 * @author zhangmy
 * @Description: 服务代理审核
 * @date 2019-12-18 10:52
 */
public class AgProxyService {
    private String id;
    private String userName;
    private String password;
    private String layerId;
    private String applyReason;
    private Date applyTime;
    private Date auditTime;
    private String auditor;
    private String state;
    private String uuid;
    private String approveOpinion;
    private String serviceName;
    private String proxyUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public String getApproveOpinion() {
        return approveOpinion;
    }

    public void setApproveOpinion(String approveOpinion) {
        this.approveOpinion = approveOpinion;
    }

    @Override
    public String toString() {
        return "AgProxyService{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", layerId='" + layerId + '\'' +
                ", applyReason='" + applyReason + '\'' +
                ", applyTime=" + applyTime +
                ", auditTime=" + auditTime +
                ", auditor='" + auditor + '\'' +
                ", state='" + state + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
