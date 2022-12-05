package com.augurit.agcloud.agcom.agsupport.sc.bimfile.dto;

import com.augurit.agcloud.agcom.agsupport.common.util.io.UploadFile;

import java.util.List;

/**
 * Created by fanghh on 2020/3/10.
 */
public class BimFileListDTO {

    private List<UploadFile> fileList;
    private String projectId;


    public List<UploadFile> getFileList() {
        return fileList;
    }

    public void setFileList(List<UploadFile> fileList) {
        this.fileList = fileList;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
