package com.augurit.agcloud.agcom.agsupport.domain;

import java.util.Date;

/**
 * Created by caokp on 2017-08-28.
 * 附件实体类
 */
public class AgAttachment {
    private String id;
    private String name;            //附件名称
    private String filePath;        //文件路径
    private String funcName;        //功能名称
    private Date uploadDate;        //上传时间
    private String uploadMan;      //上传人
    private String remark;          //备注

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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getUploadMan() {
        return uploadMan;
    }

    public void setUploadMan(String uploadMan) {
        this.uploadMan = uploadMan;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
