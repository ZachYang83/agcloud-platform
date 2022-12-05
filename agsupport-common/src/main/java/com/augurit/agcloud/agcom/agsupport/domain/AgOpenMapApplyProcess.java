package com.augurit.agcloud.agcom.agsupport.domain;

import java.util.Date;

public class AgOpenMapApplyProcess {
    private String id;
    private String applyItemId;
    private String applyId;
    private String name;
    private Date processTime;
    private String code;
    private String handle;
    private int success; //当前流程是否成功，0-失败，1-成功

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplyItemId() {
        return applyItemId;
    }

    public void setApplyItemId(String applyItemId) {
        this.applyItemId = applyItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }
}
