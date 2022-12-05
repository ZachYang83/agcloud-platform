package com.augurit.agcloud.agcom.agsupport.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Author: libc
 * @Description: 问题识别模块实体类
 * @Date: 2020/8/28 11:24
 * @Version: 1.0
 */
public class AgProblemDiscern implements Serializable {
    //主键
    private String id;
    //问题图片 (二进制)
    private byte[] problemImg;
    //描述
    private String description;
    //图片唯一标识
    private String imgId;
    //创建时间
    private Timestamp createTime;
    //修改时间
    private Timestamp modifyTime;
    //备注
    private String remark;
    //问题类型："1"：BIM审查 "2"标签管理
    private String pType;

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getProblemImg() {
        return problemImg;
    }

    public void setProblemImg(byte[] problemImg) {
        this.problemImg = problemImg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
