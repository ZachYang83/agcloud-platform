package com.augurit.agcloud.agcom.agsupport.domain;

import java.util.List;

public class AgOpenMapApplyItem {

    private String id;
    private String applicant; //申请人的用户登录名
    private String layerId;
    private String applyDocId;
    private List   applyProcess; //申请处理流程
    private String loginName; //申请人的登录名
    private String applyFor; //申请用途
    private String secrecy;
    private String currentProcessStatus; //当前处理状态，0-申请中，1-审核通过，2-审核不通过(驳回)
    private String obtainWay;
    private String obtainWayDesc;
    private int    validityDate;
    private String workUnit;
    private String workAddress;
    private String applicantIDCard; //身份证号码
    private String applicantMobile; //申请人手机号码
    private String serialNo; //申请序列号
    private String auditor; //审核人
    private String applyTime; //申请时间
    private List<AgOpenMapAttachFile> applyFiles; //申请材料
    private String proxyURL; //地图代理URL
    private String ip;   //申请用户提交申请的机器ip

    private String applyId;
    private String dirLayerId;
    private String proxyUrl;

    private String completed; // 0:，  1:代表此item 不再与图层挂载，说明它是被驳回后，图层被重新申请了

    public String getComplete() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getProxyURL() {
        return proxyURL;
    }

    public void setProxyURL(String proxyURL) {
        this.proxyURL = proxyURL;
    }

    public List<AgOpenMapAttachFile> getApplyFiles() {
        return applyFiles;
    }

    public void setApplyFiles(List<AgOpenMapAttachFile> applyFiles) {
        this.applyFiles = applyFiles;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getApplyFor() {
        return applyFor;
    }

    public void setApplyFor(String applyFor) {
        this.applyFor = applyFor;
    }

    public String getSecrecy() {
        return secrecy;
    }

    public void setSecrecy(String secrecy) {
        this.secrecy = secrecy;
    }

    public String getObtainWay() {
        return obtainWay;
    }

    public void setObtainWay(String obtainWay) {
        this.obtainWay = obtainWay;
    }

    public String getObtainWayDesc() {
        return obtainWayDesc;
    }

    public void setObtainWayDesc(String obtainWayDesc) {
        this.obtainWayDesc = obtainWayDesc;
    }

    public int getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(int validityDate) {
        this.validityDate = validityDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public String getApplyDocId() {
        return applyDocId;
    }

    public void setApplyDocId(String applyDocId) {
        this.applyDocId = applyDocId;
    }

    public List getApplyProcess() {
        return applyProcess;
    }

    public void setApplyProcess(List applyProcess) {
        this.applyProcess = applyProcess;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getApplicantIDCard() {
        return applicantIDCard;
    }

    public void setApplicantIDCard(String applicantIDCard) {
        this.applicantIDCard = applicantIDCard;
    }

    public String getApplicantMobile() {
        return applicantMobile;
    }

    public void setApplicantMobile(String applicantMobile) {
        this.applicantMobile = applicantMobile;
    }

    public String getCurrentProcessStatus() {
        return currentProcessStatus;
    }

    public void setCurrentProcessStatus(String currentProcessStatus) {
        this.currentProcessStatus = currentProcessStatus;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getDirLayerId() {
        return dirLayerId;
    }

    public void setDirLayerId(String dirLayerId) {
        this.dirLayerId = dirLayerId;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public String getCompleted() {
        return completed;
    }
}
