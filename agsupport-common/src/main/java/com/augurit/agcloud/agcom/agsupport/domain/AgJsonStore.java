package com.augurit.agcloud.agcom.agsupport.domain;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Author BYD
 * @ClassName AgJsonStore
 * @Date 2019/9/23 13:37
 * @Version 1.0
 */
public class AgJsonStore {

    @ApiModelProperty(value = "JsonID")
    private String id;
    @ApiModelProperty(value = "Json名称")
    private String name;
    @ApiModelProperty(value = "Json所处区域")
    private String domain;
    @ApiModelProperty(value = "Json使用者")
    private String usage;
    @ApiModelProperty(value = "Json链接")
    private String url;
    @ApiModelProperty(value = "Json信息")
    private String json;
    @ApiModelProperty(value = "Json备注")
    private String tag;
    @ApiModelProperty(value = "Json序号")
    private String sort;
    @ApiModelProperty(value = "Json创建时间")
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
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
