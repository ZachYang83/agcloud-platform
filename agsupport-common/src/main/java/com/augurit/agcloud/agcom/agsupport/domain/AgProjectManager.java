package com.augurit.agcloud.agcom.agsupport.domain;

import java.sql.Timestamp;

/**
 * @Author: libc
 * @Description: 地图运维-配置管理-工程管理 实体类
 * @Date: 2020/7/14 13:55
 * @Version: 1.0
 */
public class AgProjectManager {
    // 主键
    private String id;
    // 工程名称
    private String name;
    // 工程类型
    private String type;
    // 扩展字段（json字符串格式，名称：值：说明）
    private String extendData;
    // 创建者
    private String creator;
    // 创建时间
    private Timestamp createTime;
    // 修改时间
    private Timestamp modifyTime;
    // 备注
    private String remark;
    // 是否删除
    private String isDelete;

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

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

    public String getExtendData() {
        return extendData;
    }

    public void setExtendData(String extendData) {
        this.extendData = extendData;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

}
