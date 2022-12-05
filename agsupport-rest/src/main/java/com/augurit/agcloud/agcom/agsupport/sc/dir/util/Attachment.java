package com.augurit.agcloud.agcom.agsupport.sc.dir.util;

import java.io.File;

/**
 * Created by Augurit on 2017-04-21.
 */
public class Attachment {
    private File file;
    private String fileContentType;
    private String fileFileName;

    public Attachment() {
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getFileFileName() {
        return fileFileName;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }
}
