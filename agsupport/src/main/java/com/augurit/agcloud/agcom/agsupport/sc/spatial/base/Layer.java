package com.augurit.agcloud.agcom.agsupport.sc.spatial.base;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.SpatialUtil;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by q on 2017-03-31.
 */
public class Layer {

    @ApiModelProperty("数据源id")
    private String dataSourceId;

    @ApiModelProperty("数据源类型")
    private String dataSourceType;

    @ApiModelProperty("图层名称")
    private String layerTable;

    @ApiModelProperty("图层类型")
    private String layerType;

    @ApiModelProperty("经度")
    private String xColumn;

    @ApiModelProperty("纬度")
    private String yColumn;

    @ApiModelProperty("wkt格式")
    private String wktColumn;

    @ApiModelProperty("地理参考系")
    private String geometryColumn;

    @ApiModelProperty("groupColumn")
    private String groupColumn;

    @ApiModelProperty("statsColumn")
    private String statsColumn;

    @ApiModelProperty("statsType")
    private String statsType;//stat type

    @ApiModelProperty("where")
    private String where;

    @ApiModelProperty("查询的字段")
    private List fields = new ArrayList(); //查询的字段

    @ApiModelProperty("values")
    private List values = new ArrayList();

    @ApiModelProperty("主键")
    private String pkColumn;

    public String getDataSourceId() {
        /*if (dataSourceType == null || dataSourceType.equals("")) {
            dataSourceType = SpatialUtil.getDbTypeById(dataSourceId);
        }*/
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public String getLayerTable() {
        return layerTable;
    }

    public void setLayerTable(String layerTable) {
        this.layerTable = layerTable;
    }

    public String getLayerType() {
        return layerType;
    }

    public void setLayerType(String layerType) {
        this.layerType = layerType;
    }

    public String getxColumn() {
        return xColumn;
    }

    public void setxColumn(String xColumn) {
        this.xColumn = xColumn;
    }

    public String getyColumn() {
        return yColumn;
    }

    public void setyColumn(String yColumn) {
        this.yColumn = yColumn;
    }

    public String getWktColumn() {
        return wktColumn;
    }

    public void setWktColumn(String wktColumn) {
        this.wktColumn = wktColumn;
    }

    public String getGeometryColumn() {
        return geometryColumn;
    }

    public void setGeometryColumn(String geometryColumn) {
        this.geometryColumn = geometryColumn;
    }

    public String getGroupColumn() {
        return groupColumn;
    }

    public void setGroupColumn(String groupColumn) {
        this.groupColumn = groupColumn;
    }

    public String getStatsColumn() {
        return statsColumn;
    }

    public void setStatsColumn(String statsColumn) {
        this.statsColumn = statsColumn;
    }

    public String getStatsType() {
        return statsType;
    }

    public void setStatsType(String statsType) {
        this.statsType = statsType;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public List getFields() {
        return fields;
    }

    public void setFields(List fields) {
        this.fields = fields;
    }

    public List getValues() {
        return values;
    }

    public void setValues(List values) {
        this.values = values;
    }

    public String getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(String pkColumn) {
        this.pkColumn = pkColumn;
    }

}
