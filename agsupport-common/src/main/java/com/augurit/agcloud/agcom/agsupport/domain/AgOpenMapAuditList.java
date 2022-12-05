package com.augurit.agcloud.agcom.agsupport.domain;

import java.util.Date;
import java.util.List;

public class AgOpenMapAuditList { //审核列表

    private String id;
    private String serialNo;
    private String applicant; //申请人
    private String applyTime; //申请时间
    private String auditStatus; //审核状态
    private String layerName; //申请的地图名称（申请服务名）
    private String auditor; //审核人名称
    private String opinion; //处理意见

//    private AgOpenMapLayer applyResourceInfo; //申请Item

    private List<AgOpenMapLayer> applyResourceInfos; //申请Items

    private String currentProcessStatus; //当前处理状态，0-申请中，1-审核通过，2-审核不通过(驳回)
    private String applyDocId;
    private String applicantLoginName; //申请人的登录名
    private String secrecy;//是否涉密 0-否，1-是
    private String obtainWay;//获取方式
    private String obtainWayDesc;//获取方式说明
    private String validityDate;//申请使用天数
    private String workUnit;//工作单位
    private String applicantIdCard; //身份证号码
    private String workAddress;//工作地址
    private String applicantMobile; //申请人手机号码
    private String applyFor; //申请用途
    private String ip;   //申请用户提交申请的机器ip
    private String auditorLoginName;//审核人登录名
    private String auditorUserName;//审核人姓名
    private String auditOpinion;//审核意见
    private Date auditTime;//审核时间
    private String layerNames;//申请的图层名称
    private List<AgOpenMapAttachFile> applyFiles;

    public List<AgOpenMapLayer> getApplyResourceInfos() {
        return applyResourceInfos;
    }

    public void setApplyResourceInfos(List<AgOpenMapLayer> applyResourceInfos) {
        this.applyResourceInfos = applyResourceInfos;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

//    public AgOpenMapLayer getApplyResourceInfo() {
//        return applyResourceInfo;
//    }
//
//    public void setApplyResourceInfo(AgOpenMapLayer applyResourceInfo) {
//        this.applyResourceInfo = applyResourceInfo;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getCurrentProcessStatus() {
        return currentProcessStatus;
    }

    public void setCurrentProcessStatus(String currentProcessStatus) {
        this.currentProcessStatus = currentProcessStatus;
    }

    public String getApplyDocId() {
        return applyDocId;
    }

    public void setApplyDocId(String applyDocId) {
        this.applyDocId = applyDocId;
    }

    public String getApplicantLoginName() {
        return applicantLoginName;
    }

    public void setApplicantLoginName(String applicantLoginName) {
        this.applicantLoginName = applicantLoginName;
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

    public String getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(String validityDate) {
        this.validityDate = validityDate;
    }

    public String getWorkUnit() {
        return workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getApplicantIdCard() {
        return applicantIdCard;
    }

    public void setApplicantIdCard(String applicantIdCard) {
        this.applicantIdCard = applicantIdCard;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public String getApplicantMobile() {
        return applicantMobile;
    }

    public void setApplicantMobile(String applicantMobile) {
        this.applicantMobile = applicantMobile;
    }

    public String getApplyFor() {
        return applyFor;
    }

    public void setApplyFor(String applyFor) {
        this.applyFor = applyFor;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAuditorLoginName() {
        return auditorLoginName;
    }

    public void setAuditorLoginName(String auditorLoginName) {
        this.auditorLoginName = auditorLoginName;
    }

    public String getAuditorUserName() {
        return auditorUserName;
    }

    public void setAuditorUserName(String auditorUserName) {
        this.auditorUserName = auditorUserName;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getLayerNames() {
        return layerNames;
    }

    public void setLayerNames(String layerNames) {
        this.layerNames = layerNames;
    }

    public List<AgOpenMapAttachFile> getApplyFiles() {
        return applyFiles;
    }

    public void setApplyFiles(List<AgOpenMapAttachFile> applyFiles) {
        this.applyFiles = applyFiles;
    }
}
