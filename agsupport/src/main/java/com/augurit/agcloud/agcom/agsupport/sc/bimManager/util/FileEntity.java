package com.augurit.agcloud.agcom.agsupport.sc.bimManager.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

/**
 *
 */
public class FileEntity implements Serializable {
    //名称
    private String name;
    //上传的全路径
    private String uploadFullPath;
    //文件
    @JsonIgnore
    private File file;
    //json文件对象
    @JsonIgnore
    private JSONObject jsonObject;
    @JsonIgnore
    private byte[] fileBytes;
    @JsonIgnore
    private InputStream inputStream;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUploadFullPath() {
        return uploadFullPath;
    }

    public void setUploadFullPath(String uploadFullPath) {
        this.uploadFullPath = uploadFullPath;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }


    @Override
    public String toString() {
        return "FileEntity{" +
                "name='" + name + '\'' +
                ", uploadFullPath='" + uploadFullPath + '\'' +
                ", file=" + file +
                ", jsonObject=" + jsonObject +
                '}';
    }
}
