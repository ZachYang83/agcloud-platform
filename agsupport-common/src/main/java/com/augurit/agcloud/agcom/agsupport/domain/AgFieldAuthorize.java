package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * Author：李德文
 * CreateDate：2017-06-06
 * Description: 字段权限配置bean
 */
public class AgFieldAuthorize {
    // 属性
    private String id;
    private String roleLayerId;
    private String fieldId;
    private String viewInResult;
    private String editable;
    private String viewInBlurquery;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleLayerId() {
        return roleLayerId;
    }

    public void setRoleLayerId(String roleLayerId) {
        this.roleLayerId = roleLayerId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getViewInResult() {
        return viewInResult;
    }

    public void setViewInResult(String viewInResult) {
        this.viewInResult = viewInResult;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getViewInBlurquery() {
        return viewInBlurquery;
    }

    public void setViewInBlurquery(String viewInBlurquery) {
        this.viewInBlurquery = viewInBlurquery;
    }
}
