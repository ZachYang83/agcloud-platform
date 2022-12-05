package com.augurit.agcloud.agcom.agsupport.domain;

import java.util.Date;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :9:26 2018/12/27
 * @Modified By:
 */
public class AgIndexConfigInfo {

    private String id;
    private String dataSourceId;//数据源id
    private String tableNameCn;//表别名
    private String dbname;//数据库名称
    private String tableName;//表名
    private String indexName;//索引名称
    private int docNumber;//文档数量
    private String nodeIp;//节点ip
    private String indexField;//索引字段
    private int fieldTotal;//字段个数
    private Date createTime;//创建时间
    private Date updataTime;//更新时间

    private String tableType;//表类型
    private String isWgs84;//是否经纬度坐标
    private String coordinateType;//坐标类型 点线面

    private String layerId;//图层id
    private String notAnalysisWord;//不分词字段
    private String showField;//结果显示字段

    public String getShowField() {
        return showField;
    }

    public void setShowField(String showField) {
        this.showField = showField;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableNameCn() {
        return tableNameCn;
    }

    public void setTableNameCn(String tableNameCn) {
        this.tableNameCn = tableNameCn;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public int getDocNumber() {
        return docNumber;
    }

    public void setDocNumber(int docNumber) {
        this.docNumber = docNumber;
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }

    public String getIndexField() {
        return indexField;
    }

    public void setIndexField(String indexField) {
        this.indexField = indexField;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public int getFieldTotal() {
        return fieldTotal;
    }

    public void setFieldTotal(int fieldTotal) {
        this.fieldTotal = fieldTotal;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdataTime() {
        return updataTime;
    }

    public void setUpdataTime(Date updataTime) {
        this.updataTime = updataTime;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getIsWgs84() {
        return isWgs84;
    }

    public void setIsWgs84(String isWgs84) {
        this.isWgs84 = isWgs84;
    }

    public String getCoordinateType() {
        return coordinateType;
    }

    public void setCoordinateType(String coordinateType) {
        this.coordinateType = coordinateType;
    }

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public String getNotAnalysisWord() {
        return notAnalysisWord;
    }

    public void setNotAnalysisWord(String notAnalysisWord) {
        this.notAnalysisWord = notAnalysisWord;
    }
}
