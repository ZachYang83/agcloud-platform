package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * Author：李德文
 * CreateDate：2017-06-06
 * Description: 字段配置属性配置、权限配置bean
 */
public class AgLayerFieldConf {

    private String id;
    private String viewInResult;
    private String editable;
    private String viewInBlurquery;
    private String layerId;
    private String fieldName;
    private String fieldNameCn;
    private String fieldType;
    private String fieldSize;
    private String orderNm;
    private String isKey;
    private String refId;
    private String fnullable;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldNameCn() {
        return fieldNameCn;
    }

    public void setFieldNameCn(String fieldNameCn) {
        this.fieldNameCn = fieldNameCn;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldSize() {
        return fieldSize;
    }

    public void setFieldSize(String fieldSize) {
        this.fieldSize = fieldSize;
    }

    public String getOrderNm() {
        return orderNm;
    }

    public void setOrderNm(String orderNm) {
        this.orderNm = orderNm;
    }

    public String getIsKey() {
        return isKey;
    }

    public void setIsKey(String isKey) {
        this.isKey = isKey;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getFnullable() {
        return fnullable;
    }

    public void setFnullable(String fnullable) {
        this.fnullable = fnullable;
    }
}
