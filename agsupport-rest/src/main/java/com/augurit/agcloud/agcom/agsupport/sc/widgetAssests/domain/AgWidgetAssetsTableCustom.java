package com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.domain;

/**
 * @Author: qinyg
 * @Date: 2020/11/23
 * @tips:
 */
public class AgWidgetAssetsTableCustom {
    private String columnName;
    private Object columnValue;
    private String columnType;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Object getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(Object columnValue) {
        this.columnValue = columnValue;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }
}
