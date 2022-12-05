package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by caokp on 2017-08-17.
 */
public class AgSysLog {
    @ApiModelProperty("主键")
    private String id;                  //主键
    @ApiModelProperty("用户名")
    private String userName;            //用户名
    @ApiModelProperty("登录名")
    private String loginName;           //登录名
    @ApiModelProperty("ip地址")
    private String ipAddress;           //ip地址
    @ApiModelProperty("浏览器")
    private String browser;             //浏览器
    @ApiModelProperty("系统名称")
    private String sysName;             //系统名称
    @ApiModelProperty("功能名称")
    private String funcName;            //功能名称
    @ApiModelProperty("操作结果")
    private String operResult;          //操作结果
    @ApiModelProperty("操作时间")
    private Date operDate;               //操作时间
    @ApiModelProperty("异常信息")
    private String exceptionMessage;    //异常信息
    @ApiModelProperty("日志类型")
    private String type;                //日志类型
    @ApiModelProperty("计数")
    private int count;                  //计数
    private Date fromDate;
    private Date toDate;

    private String accessTimeStart;
    private String accessTimeEnd;

    private String restUrl;   //接口url

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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getOperResult() {
        return operResult;
    }

    public void setOperResult(String operResult) {
        this.operResult = operResult;
    }

    public Date getOperDate() {
        return operDate;
    }

    public void setOperDate(Date operDate) {
        this.operDate = operDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAccessTimeStart() {
        return accessTimeStart;
    }

    public void setAccessTimeStart(String accessTimeStart) {
        this.accessTimeStart = accessTimeStart;
    }

    public String getAccessTimeEnd() {
        return accessTimeEnd;
    }

    public void setAccessTimeEnd(String accessTimeEnd) {
        this.accessTimeEnd = accessTimeEnd;
    }

    public String getRestUrl() {
        return restUrl;
    }

    public void setRestUrl(String restUrl) {
        this.restUrl = restUrl;
    }
}
