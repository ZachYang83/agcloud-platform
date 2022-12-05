package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class AgSupDatasource implements Serializable{
    @ApiModelProperty(value = "主键ID",notes = "修改时需填")
    private String id;
    @ApiModelProperty(value = "数据源名称",required = true)
    private String name;
    @ApiModelProperty(value = "IP地址",required = true)
    private String ip;
    @ApiModelProperty(value = "用户名",required = true)
    private String userName;
    @ApiModelProperty(value = "用户密码",required = true)
    private String password;
    @ApiModelProperty(value = "服务名称",required = true)
    private String sid;
    @ApiModelProperty(value = "端口",required = true)
    private String port;
    @ApiModelProperty(value = "数据库类型",required = true)
    private String dbType;
    @ApiModelProperty(value = "最小连接数",required = true)
    private String minconnection;
    @ApiModelProperty(value = "最大连接数",required = true)
    private String maxconnection;
    @ApiModelProperty(value = "数据源类型",required = true)
    private String type;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty("数据库driverClass")
    private String driverClass;
    @ApiModelProperty("数据库url")
    private String dbUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getMinconnection() {
        return minconnection;
    }

    public void setMinconnection(String minconnection) {
        this.minconnection = minconnection;
    }

    public String getMaxconnection() {
        return maxconnection;
    }

    public void setMaxconnection(String maxconnection) {
        this.maxconnection = maxconnection;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    @Override
    public String toString() {
        return "AgSupDatasource{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", sid='" + sid + '\'' +
                ", port='" + port + '\'' +
                ", dbType='" + dbType + '\'' +
                ", minconnection='" + minconnection + '\'' +
                ", maxconnection='" + maxconnection + '\'' +
                ", type='" + type + '\'' +
                ", remark='" + remark + '\'' +
                ", driverClass='" + driverClass + '\'' +
                ", dbUrl='" + dbUrl + '\'' +
                '}';
    }
}