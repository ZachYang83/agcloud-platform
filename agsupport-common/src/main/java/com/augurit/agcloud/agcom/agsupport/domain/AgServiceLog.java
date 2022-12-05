package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Augurit on 2017-05-02.
 */
/**
 * 建立索引，提高查询速度
 */
@Document(collection = "agcom-service-log")
@CompoundIndexes({
        @CompoundIndex(name = "agcom_service_log_index", def = "{'serviceOwner':1,'name':1,'ip':1,'accessTime':-1}")
})
public class AgServiceLog implements Serializable{
    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("用户ID")
    private String userId;
    @ApiModelProperty("服务ID")
    private String serviceId;
    @ApiModelProperty("客户端IP")
    private String ip;
    @ApiModelProperty("URL")
    private String url;
    @ApiModelProperty("访问时间")
    private Date accessTime;
    @ApiModelProperty("流量")
    private long byteSize;
    @ApiModelProperty("次数")
    private long totalTimes;
    @ApiModelProperty("服务拥有者")
    private String serviceOwner;
    @ApiModelProperty("服务器IP")
    private String serverIp;
    @ApiModelProperty("真实url")
    public String realUrl;
    @ApiModelProperty("失败请求次数")
    public int badRequest;//失败请求：1
    //查询条件
    @ApiModelProperty("服务名称")
    private String name;
    @ApiModelProperty("访问日期")
    private String visitDay;

    public String getVisitDay() {
        return visitDay;
    }

    public void setVisitDay(String visitDay) {
        this.visitDay = visitDay;
    }

    @ApiModelProperty(value = "查询开始时间", notes = "格式:yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date accessTimeStart;
    @ApiModelProperty(value = "查询结束时间", notes = "格式:yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date accessTimeEnd;
    @ApiModelProperty("用户名称")
    private String userName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getAccessTimeStart() {
        return accessTimeStart;
    }

    public void setAccessTimeStart(Date accessTimeStart) {
        this.accessTimeStart = accessTimeStart;
    }

    public Date getAccessTimeEnd() {
        return accessTimeEnd;
    }

    public void setAccessTimeEnd(Date accessTimeEnd) {
        this.accessTimeEnd = accessTimeEnd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(Date accessTime) {
        this.accessTime = accessTime;
    }

    public long getByteSize() {
        return byteSize;
    }

    public void setByteSize(long byteSize) {
        this.byteSize = byteSize;
    }

    public long getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(long totalTimes) {
        this.totalTimes = totalTimes;
    }

    public String getServiceOwner() {
        return serviceOwner;
    }

    public void setServiceOwner(String serviceOwner) {
        this.serviceOwner = serviceOwner;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getRealUrl() {
        return realUrl;
    }

    public void setRealUrl(String realUrl) {
        this.realUrl = realUrl;
    }

    public int getBadRequest() {
        return badRequest;
    }

    public void setBadRequest(int badRequest) {
        this.badRequest = badRequest;
    }

    @Override
    public String toString() {
        return "AgServiceLog{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", ip='" + ip + '\'' +
                ", url='" + url + '\'' +
                ", accessTime=" + accessTime +
                ", byteSize=" + byteSize +
                ", totalTimes=" + totalTimes +
                ", serviceOwner='" + serviceOwner + '\'' +
                ", serverIp='" + serverIp + '\'' +
                ", realUrl='" + realUrl + '\'' +
                ", badRequest=" + badRequest +
                ", name='" + name + '\'' +
                ", visitDay='" + visitDay + '\'' +
                ", accessTimeStart=" + accessTimeStart +
                ", accessTimeEnd=" + accessTimeEnd +
                ", userName='" + userName + '\'' +
                '}';
    }
}
