package com.augurit.agcloud.agcom.agsupport.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName: AgBimApi
 * @Description: 第三方接口实体类
 * @Author: zhangsj
 * @Date: Create in 2020/03/20 10:14
 **/

public class AgBimApi {
    @ApiModelProperty(value = "接口ID")
    String id;
    @ApiModelProperty(value = "接口名称")
    String name;
    @ApiModelProperty(value = "请求类型")
    String type;
    @ApiModelProperty(value = "接口地址")
    String url;
    @ApiModelProperty(value = "请求方法")
    String method;
    @ApiModelProperty(value = "参数")
    String param;
    @ApiModelProperty(value = "请求Ticket")
    String ticket;
    @ApiModelProperty(value = "备注说明")
    String note;
    @ApiModelProperty(value = "创建人")
    String creater;
    @ApiModelProperty(value = "创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date createTime;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
