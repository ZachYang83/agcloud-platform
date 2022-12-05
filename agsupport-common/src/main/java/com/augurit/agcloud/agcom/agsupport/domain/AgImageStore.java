package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Author BYD
 * @ClassName AgImageStore
 * @Date 2019/9/23 13:37
 * @Version 1.0
 */
public class AgImageStore {

    @ApiModelProperty(value = "图片ID")
    private String id;
    @ApiModelProperty(value = "图片名称")
    private String name;
    @ApiModelProperty(value = "图片所处区域")
    private String domain;
    @ApiModelProperty(value = "图片使用者")
    private String usage;
    @ApiModelProperty(value = "图片全路径")
    private String fullpath;
    @ApiModelProperty(value = "图片链接")
    private String url;
    @ApiModelProperty(value = "图片本地路径")
    private String path;
    @ApiModelProperty(value = "图片信息")
    private String information;
    @ApiModelProperty(value = "图片编码")
    private String code;
    @ApiModelProperty(value = "图片备注")
    private String tag;
    @ApiModelProperty(value = "图片序号")
    private String sort;
    @ApiModelProperty(value = "图片创建时间")
    private Date createTime;


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

    public String getFullpath() {
        return fullpath;
    }

    public void setFullpath(String fullpath) {
        this.fullpath = fullpath;
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

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
