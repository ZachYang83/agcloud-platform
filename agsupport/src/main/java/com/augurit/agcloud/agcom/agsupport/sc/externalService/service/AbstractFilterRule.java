package com.augurit.agcloud.agcom.agsupport.sc.externalService.service;


import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.agcom.agsupport.sc.externalService.service.impl.DateFilterRuleImpl;
import com.augurit.agcloud.agcom.agsupport.sc.externalService.service.impl.NumberFilterRuleImpl;
import com.augurit.agcloud.agcom.agsupport.sc.externalService.service.impl.StringFilterRuleImpl;

public abstract class AbstractFilterRule {
    private String fieldName;//字段名称
    private int fieldIndex;//字段索引
    private String fieldType;//字段类型
    private String compareExpression;//比较表达式

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public int getFieldIndex() {
        return fieldIndex;
    }

    public void setFieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getCompareExpression() {
        return compareExpression;
    }

    public void setCompareExpression(String compareExpression) {
        this.compareExpression = compareExpression;
    }

    public static AbstractFilterRule createInstance(JSONObject jsonObject){
        AbstractFilterRule abstractFilterRule = null;
        String fieldName = jsonObject.getString("fieldName");
        String type = jsonObject.getString("type").toLowerCase();
        String compare = jsonObject.getString("compare");
        switch (type){
            case "date":
                abstractFilterRule = new DateFilterRuleImpl();
                break;
            case "string":
                abstractFilterRule = new StringFilterRuleImpl();
                break;
            case "number":
                abstractFilterRule = new NumberFilterRuleImpl();
                break;
        }
        abstractFilterRule.setFieldName(fieldName);
        abstractFilterRule.setFieldType(type);
        abstractFilterRule.setCompareExpression(compare);
        return abstractFilterRule;
    }

    /*
     *@content 内容
     *@fieldContent 规则
     * */
    public abstract boolean handle(String content);
}
