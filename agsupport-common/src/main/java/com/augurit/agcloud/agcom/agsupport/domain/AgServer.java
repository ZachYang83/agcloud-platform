package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhangmingyang
 * @Description: 站点管理类
 * @date 2018-11-12
 */
public class AgServer implements Serializable{
    private static final long serialVersionUID = 3878343507073446508L;
    @ApiModelProperty(value = "主键ID",notes = "新增数据不用传,修改数据需要传")
    private String id;
    @ApiModelProperty(value = "站点名称",required = true)
    private String name;
    @ApiModelProperty(value = "站点IP地址",required = true)
    private String ip;
    @ApiModelProperty(value = "用户名",required = true)
    private String userName;
    @ApiModelProperty(value = "用户密码",required = true)
    private String password;
    @ApiModelProperty(value = "Token令牌",required = true)
    private String token;
    @ApiModelProperty(value = "站点类型",required = true)
    private String type;
    @ApiModelProperty(value = "站点端口",required = true)
    private String port;
    @ApiModelProperty(value = "认证方式",required = true)
    private int authenticationMode;//0-账号密码认证；1-token认证；2-公开
    @ApiModelProperty(value = "创建时间",notes = "不需要填")
    private Date createTime;
    @ApiModelProperty(value = "arcgis服务发布方式",notes = "0：ArcGIS Portal 1：ArcGIS Server")
    private String arcgisServerType;
    @ApiModelProperty(value = "站点启用状态",notes = "0:为不启用，1:为启用。")
    private String state;
    @ApiModelProperty(value = "服务地址",required = true)
    private String serverUrl;
    @ApiModelProperty(value = "客户端请求的模式",required = true,notes = "0:ip地址;1:HTTP地址;2:请求的源头ip")
    private String client;
    @ApiModelProperty(value = "具体referer值",required = true)
    private String referer;
    @ApiModelProperty(value = "站点获取token地址",required = true)
    private String tokenUrl;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port == null ? null : port.trim();
    }

    public int getAuthenticationMode() {
        return authenticationMode;
    }

    public void setAuthenticationMode(int authenticationMode) {
        this.authenticationMode = authenticationMode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getArcgisServerType() {
        return arcgisServerType;
    }

    public void setArcgisServerType(String arcgisServerType) {
        this.arcgisServerType = arcgisServerType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    @Override
    public String toString() {
        return "AgServer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", type='" + type + '\'' +
                ", port='" + port + '\'' +
                ", authenticationMode=" + authenticationMode +
                ", createTime=" + createTime +
                ", arcgisServerType='" + arcgisServerType + '\'' +
                ", state='" + state + '\'' +
                ", serverUrl='" + serverUrl + '\'' +
                ", client='" + client + '\'' +
                ", referer='" + referer + '\'' +
                ", tokenUrl='" + tokenUrl + '\'' +
                '}';
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

}