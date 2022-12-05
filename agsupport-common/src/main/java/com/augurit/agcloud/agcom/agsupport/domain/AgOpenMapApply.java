package com.augurit.agcloud.agcom.agsupport.domain;

import com.augurit.agcloud.agcom.agsupport.domain.AgOpenMapApplyProcess;
import com.augurit.agcloud.agcom.agsupport.domain.AgOpenMapAttachFile;
import com.augurit.agcloud.agcom.agsupport.domain.AgOpenMapLayer;

import java.util.Date;
import java.util.List;

public class AgOpenMapApply {

    private String id;
    private String applicant; //申请人的姓名
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
    private Date applyTime; //申请时间
    private String applyFor; //申请用途
    private String ip;   //申请用户提交申请的机器ip
    private String auditorLoginName;
    private String auditorUserName;
    private String auditOpinion;
    private Date auditTime;
    private String dirLayerIds;
    //其它非表属性参数
    private List<AgOpenMapLayer> listAgOpenMapLayer;
    private List<AgOpenMapAttachFile> applyFiles; //申请材料
    private List<AgOpenMapApplyProcess>   applyProcess; //申请处理流程

    public void setValueForTest(){
        setApplicant("applicant");
        setApplyDocId("applyDocId");
        setApplicantLoginName("applicantLoginName");
        setSecrecy("0");
        setObtainWay("2");
        setObtainWayDesc("其他");
        setValidityDate("30");
        setWorkUnit("workUnit");
        setApplicantIdCard("applicantIdCard");
        setWorkAddress("workAddress");
        setApplicantMobile("applicantMobile");
        setApplyTime(new Date());
        setApplyFor("applyFor");
        setIp("ip");
        setDirLayerIds("5cbb763c-040b-42f1-bb81-369ec614be30");
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


    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
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

    public List<AgOpenMapLayer> getListAgOpenMapLayer() {
        return listAgOpenMapLayer;
    }

    public void setListAgOpenMapLayer(List<AgOpenMapLayer> listAgOpenMapLayer) {
        this.listAgOpenMapLayer = listAgOpenMapLayer;
    }

    public String getDirLayerIds() {
        return dirLayerIds;
    }

    public void setDirLayerIds(String dirLayerIds) {
        this.dirLayerIds = dirLayerIds;
    }

    public List<AgOpenMapAttachFile> getApplyFiles() {
        return applyFiles;
    }

    public void setApplyFiles(List<AgOpenMapAttachFile> applyFiles) {
        this.applyFiles = applyFiles;
    }



    public List getApplyProcess() {
        return applyProcess;
    }

    public void setApplyProcess(List applyProcess) {
        this.applyProcess = applyProcess;
    }


}
