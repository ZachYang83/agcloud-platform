package com.augurit.agcloud.agcom.agsupport.domain;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 文件管理
 */
@ApiModel(value = "文件管理")
public class AgFileStore {

    @ApiModelProperty(value = "文件ID")
    private String id;
    @ApiModelProperty(value = "文件名称")
    private String name;
    @ApiModelProperty(value = "文件别名")
    private String alias;
    @ApiModelProperty(value = "文件所处区域")
    private String domain;
    @ApiModelProperty(value = "文件使用者")
    private String usage;
    @ApiModelProperty(value = "文件链接")
    private String url;
    @ApiModelProperty(value = "文件路径")
    private String path;
    @ApiModelProperty(value = "文件后缀")
    private String extension;
    @ApiModelProperty(value = "文件大小")
    private String length;
    @ApiModelProperty(value = "文件备注")
    private String tag;
    @ApiModelProperty(value = "文件失效时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date expireTime;
    @ApiModelProperty(value = "文件上传时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;
    @ApiModelProperty(value = "模块编号")
    private String moduleCode;

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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }
}
