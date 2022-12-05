package com.augurit.agcloud.agcom.agsupport.common.util.io;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 文件实体类
 *
 * @Project Augurit
 * @Author LiRuiFa
 * @ClassName FileEntity
 * @Date 2019/6/13 15:28
 * @Version 1.0
 **/
@ApiModel
public class FileEntity {

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "文件新名称")
    private String fileNewName;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "文件链接")
    private String fileLink;

    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @ApiModelProperty(value = "文件大小")
    private String fileSize;

    @ApiModelProperty(value = "文件创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date fileCreateTime;

    public FileEntity() {
    }

    public FileEntity(String fileName, String fileNewName, String fileType, String fileLink, String filePath, String fileSize, Date fileCreateTime) {
        this.fileName = fileName;
        this.fileNewName = fileNewName;
        this.fileType = fileType;
        this.fileLink = fileLink;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileCreateTime = fileCreateTime;
    }

    public FileEntity(Builder builder) {
        this.fileName = builder.fileName;
        this.fileNewName = builder.fileNewName;
        this.fileType = builder.fileType;
        this.fileLink = builder.fileLink;
        this.filePath = builder.filePath;
        this.fileSize = builder.fileSize;
        this.fileCreateTime = builder.fileCreateTime;
    }

    public static class Builder {

        private String fileName;
        private String fileNewName;
        private String fileType;
        private String fileLink;
        private String filePath;
        private String fileSize;
        private Date fileCreateTime;

        public Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder setFileNewName(String fileNewName) {
            this.fileNewName = fileNewName;
            return this;
        }

        public Builder setFileType(String fileType) {
            this.fileType = fileType;
            return this;
        }

        public Builder setFileLink(String fileLink) {
            this.fileLink = fileLink;
            return this;
        }

        public Builder setFilePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder setFileSize(String fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public Builder setFileCreateTime(Date fileCreateTime) {
            this.fileCreateTime = fileCreateTime;
            return this;
        }

        public FileEntity build() {
            return new FileEntity(this);
        }

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileNewName() {
        return fileNewName;
    }

    public void setFileNewName(String fileNewName) {
        this.fileNewName = fileNewName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Date getFileCreateTime() {
        return fileCreateTime;
    }

    public void setFileCreateTime(Date fileCreateTime) {
        this.fileCreateTime = fileCreateTime;
    }
}
