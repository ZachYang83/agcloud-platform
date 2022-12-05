package com.augurit.agcloud.agcom.agsupport.domain;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * @Author:Dreram
 * @Description: 服务监控
 * @Date:created in :13:50 2018/10/29
 * @Modified By:
 */
public class AgServicesMonitor {
    private String id;
    private int status;//状态 -1:服务停止，1：服务已经启动，2：服务器地址错误，无法访问，3：图层服务不存在
    private int monitorStatus;//监控状态：1：启动监控:，-1：停止监控
    private String serviceName;//服务名称
    private String serviceFullName;
    private String monitorUrl;//监控地址
    private double available;//可用率
    private int averageTime;//平均响应时间
    private int monitorFrequency;//监控频率
    private double successRate;//成功率
    private String monitorDetail;//监控的情况，只保留一天的数据
    private long requestTotal;//服务请求总数
    private Date startMonitorTime;//开始监控时间
    private Date lastMonitorTime;//最后一次监控时间
    private int ipBlackList;//IP黑名单  1：黑名单

    //发送邮件增加的字段
    private String emailAddress;//邮箱地址
    private String phone;//电话号码
    private float sendRate;//发送间隔 0.5,1,2,3...小时
    private String sendLevel;//all:所有情况，-1服务停止，1服务启动，2服务器地址错误，3图层不存在
    private String receiveWay;//接收方式：email，note（邮件或者短信或者两者都需要）


    //查询条件
    private String queryStartTime;//查询开始时间
    private String queryEndTime;//查询结束时间

    //setter and getter methods


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMonitorStatus() {
        return monitorStatus;
    }

    public void setMonitorStatus(int monitorStatus) {
        this.monitorStatus = monitorStatus;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceFullName() {
        return serviceFullName;
    }

    public void setServiceFullName(String serviceFullName) {
        this.serviceFullName = serviceFullName;
    }

    public String getMonitorUrl() {
        return monitorUrl;
    }

    public void setMonitorUrl(String monitorUrl) {
        this.monitorUrl = monitorUrl;
    }

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    public int getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(int averageTime) {
        this.averageTime = averageTime;
    }

    public int getMonitorFrequency() {
        return monitorFrequency;
    }

    public void setMonitorFrequency(int monitorFrequency) {
        this.monitorFrequency = monitorFrequency;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

    public String getMonitorDetail() {
        return monitorDetail;
    }

    public void setMonitorDetail(String monitorDetail) {
        this.monitorDetail = monitorDetail;
    }

    public long getRequestTotal() {
        return requestTotal;
    }

    public void setRequestTotal(long requestTotal) {
        this.requestTotal = requestTotal;
    }

    public Date getStartMonitorTime() {
        return startMonitorTime;
    }

    public void setStartMonitorTime(Date startMonitorTime) {
        this.startMonitorTime = startMonitorTime;
    }

    public Date getLastMonitorTime() {
        return lastMonitorTime;
    }

    public void setLastMonitorTime(Date lastMonitorTime) {
        this.lastMonitorTime = lastMonitorTime;
    }

    public int getIpBlackList() {
        return ipBlackList;
    }

    public void setIpBlackList(int ipBlackList) {
        this.ipBlackList = ipBlackList;
    }


    public String getQueryStartTime() {
        if ("".equals(queryStartTime)) return null;
        return queryStartTime;
    }

    public void setQueryStartTime(String queryStartTime) {
        this.queryStartTime = queryStartTime;
    }

    public String getQueryEndTime() {
        if ("".equals(queryEndTime)) return null;
        return queryEndTime;
    }

    public void setQueryEndTime(String queryEndTime) {
        this.queryEndTime = queryEndTime;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public float getSendRate() {
        return sendRate;
    }

    public void setSendRate(float sendRate) {
        this.sendRate = sendRate;
    }

    public String getSendLevel() {
        return sendLevel;
    }

    public void setSendLevel(String sendLevel) {
        this.sendLevel = sendLevel;
    }

    public String getReceiveWay() {
        return receiveWay;
    }

    public void setReceiveWay(String receiveWay) {
        this.receiveWay = receiveWay;
    }
}
