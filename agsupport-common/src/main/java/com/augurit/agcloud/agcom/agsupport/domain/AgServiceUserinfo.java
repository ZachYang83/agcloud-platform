package com.augurit.agcloud.agcom.agsupport.domain;

        import java.util.Date;

/**
 * Created by Augurit on 2017-05-02.
 */
public class AgServiceUserinfo {
    private String id;
    private String userId;
    private String serviceId;//其实就是layerid
    private String flag;
    private Date applyTime;
    private String applyOpinion;
    private String approveOpinion;
    private Date approveTime;
    private String uuid;
    private String ip;
    private String url;
    private String system;



    //查询条件
    private String name;
    private String applyTimeStart;
    private String applyTimeEnd;
    private String userName;

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getServiceId() {
        return serviceId;
    }
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
    public String getFlag() {
        return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }
    public Date getApplyTime() {
        return applyTime;
    }
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }
    public String getApplyOpinion() {
        return applyOpinion;
    }
    public void setApplyOpinion(String applyOpinion) {
        this.applyOpinion = applyOpinion;
    }
    public String getApproveOpinion() {
        return approveOpinion;
    }
    public void setApproveOpinion(String approveOpinion) {
        this.approveOpinion = approveOpinion;
    }
    public Date getApproveTime() {
        return approveTime;
    }
    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplyTimeStart() {
        return applyTimeStart;
    }

    public void setApplyTimeStart(String applyTimeStart) {
        this.applyTimeStart = applyTimeStart;
    }

    public String getApplyTimeEnd() {
        return applyTimeEnd;
    }

    public void setApplyTimeEnd(String applyTimeEnd) {
        this.applyTimeEnd = applyTimeEnd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getSystem() {return system;}
    public void setSystem(String system) {this.system = system;}
}
