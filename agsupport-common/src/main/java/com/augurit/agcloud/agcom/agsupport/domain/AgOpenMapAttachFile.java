package com.augurit.agcloud.agcom.agsupport.domain;

import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgOpenMapAttachFile {
    // 格式化日期对象
    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private String id;

    private String nameId; // 文件id名， 作为服务器保存文件时的名称，但无后缀名
    private String applyItemId;

    // 原始文件的后缀名, 如.doc、.txt等
    private String suffix;
    private String originalFilename;// 文件原始名称
    private String fileSize = "0KB";// 文件大小，带上单位
    private String isActive = "0";// 0：无效 1：有效
    private String encoding = "UTF-8";// 文件内容编码
    private String createTime = format.format(new Date());// 附件信息创建时间
    private String dirId = "0";
    private String dirPath;
    private String creatorLoginName;
    private String creatorUserName;

    private String applyId; // 申请号
    private String name;//文件名称
    private String filePath;
    //以下是非数据实体属性
    private MultipartFile file;

    public String getApplyItemId() {
        return applyItemId;
    }

    public void setApplyItemId(String applyItemId) {
        this.applyItemId = applyItemId;
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDirId() {
        return dirId;
    }

    public void setDirId(String dirId) {
        this.dirId = dirId;
    }

    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getCreatorLoginName() {
        return creatorLoginName;
    }

    public void setCreatorLoginName(String creatorLoginName) {
        this.creatorLoginName = creatorLoginName;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    public static DateFormat getFormat() {
        return format;
    }

    public static void setFormat(DateFormat format) {
        AgOpenMapAttachFile.format = format;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
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

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
