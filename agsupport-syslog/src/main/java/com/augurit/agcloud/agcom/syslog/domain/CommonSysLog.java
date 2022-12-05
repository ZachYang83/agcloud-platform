package com.augurit.agcloud.agcom.syslog.domain;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author zhangmy
 * @Description: 日志bean
 * @date 2019-10-29 11:06
 */
@Document(collection = "ag-sys-log")
@CompoundIndexes({
        @CompoundIndex(name = "ag-sys_log_index", def = "{'userName':1,'sysName':1,'operDate':-1}")
})
public class CommonSysLog {
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("登录名")
    private String loginName;
    @ApiModelProperty("ip地址")
    private String ipAddress;
    @ApiModelProperty("浏览器")
    private String browser;
    @ApiModelProperty("系统名称")
    private String sysName;
    @ApiModelProperty("功能名称")
    private String funcName;
    @ApiModelProperty("操作结果")
    private String operResult;
    @ApiModelProperty("操作时间")
    private Date operDate;
    @ApiModelProperty("异常信息")
    private String exceptionMessage;
    @ApiModelProperty("描述信息")
    private String remark;
    @ApiModelProperty("扩展字段，json格式")
    private String extendData;
    @ApiModelProperty("日志类型")
    private String type;
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

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getExtendData() {
        return extendData;
    }

    public void setExtendData(String extendData) {
        this.extendData = extendData;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CommonSysLog{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", loginName='" + loginName + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", browser='" + browser + '\'' +
                ", sysName='" + sysName + '\'' +
                ", funcName='" + funcName + '\'' +
                ", operResult='" + operResult + '\'' +
                ", operDate=" + operDate +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                ", remark='" + remark + '\'' +
                ", extendData='" + extendData + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
