package com.augurit.agcloud.agcom.agsupport.domain;

import com.augurit.agcloud.agcom.agsupport.util.SysUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Augurit on 2017-04-18.
 */
public class AgLayer {
    //主表字段
    @ApiModelProperty(value = "主键ID",notes = "")
    private String id;                      //编号
    @ApiModelProperty(value = "名称",notes = "名称",required = true)
    private String name;                    //名称
    @ApiModelProperty(value = "别名",notes = "别名",required = true)
    private String nameCn;                  //别名
    @ApiModelProperty(value = "访问路径",notes = "访问路径",required = true)
    private String url;                     //访问路径
    @ApiModelProperty(value = "图层类型",notes = "图层类型",required = true)
    private String layerType;               //图层类型
    @ApiModelProperty(value = "要素类型",notes = "要素类型")
    private String featureType;             //要素类型
    @ApiModelProperty(value = "略缩图路径",notes = "略缩图路径")
    private String shortCut;                //略缩图路径
    @ApiModelProperty(value = "标识符",notes = "标识符")
    private String addFlag;                 //标识符
    @ApiModelProperty(value = "图层索引",notes = "图层索引")
    private String layerTable;              //图层表名
    @ApiModelProperty(value = "地图参数id",notes = "地图参数id")
    private String paramId;                 //地图参数id
    @ApiModelProperty(value = "矢量图层关联id",notes = "矢量图层关联id")
    private String vectorLayerId;           //矢量图层关联id
    @ApiModelProperty(value = "是否使用代理 0:否 1:是")
    private String isProxy;                 //是否使用代理 0否1是
    @ApiModelProperty(value = "代理地址",notes = "代理地址")
    private String proxyUrl;                //代理地址
    @ApiModelProperty(value = "图层表扩展字段集合的json:{\"renderLayerId\":\"\",\"dataSourceId\":\"5d120a30-7bfb-404f-a354-e5369bf23d50\",\"layerTable\":\"ag_address\",\"pkColumn\":\"objectid\",\"geometryColumn\":\"shape\",\"senceExtendData\":\"{}\",\"regExAndValue\":\"{\\\"regObj\\\":{},\\\"valueObj\\\":{\\\"value_0\\\":\\\"\\\"}}\",\"charset\":\"UTF-8\",\"isCacheText\":\"0\",\"isCacheImage\":\"0\",\"layerTypeCn\":\"矢量图层\",\"createTime\":{\"date\":7,\"day\":4,\"hours\":10,\"minutes\":58,\"month\":2,\"seconds\":53,\"time\":1551927533000,\"timezoneOffset\":-480,\"year\":119},\"illustration\":\"\",\"label\":\"\",\"layerId\":\"\",\"layerName\":\"\",\"owner\":\"zhangguo\",\"picture\":\"\"}")
    private String data;               //图层表扩展字段集合的json
    @ApiModelProperty(value = "图层范围",notes = "图层范围")
    private String extent;               //图层范围
    @ApiModelProperty(value = "图层元数据关联id",notes = "图层元数据关联id")
    private String metadataId;               //图层元数据关联id
    @ApiModelProperty(value = "是否对外",notes = "是否对外")
    private String isExternal;              //是否对外
    @ApiModelProperty(value = "父id",notes = "父id")
    private String _parentId;               //
    @ApiModelProperty(value = "图层类型中文名",notes = "图层类型中文名")
    //关联字段
    private String layerTypeCn;             //图层类型中文名
    //form表单字段
    @ApiModelProperty(value = "所在目录id",notes = "所在目录id")
    private String dirId;                   //所在目录id
    @ApiModelProperty(value = "所在目录名称",notes = "所在目录名称")
    private String dirPath;                 //所在目录名称
    @ApiModelProperty(value = "目录图层关联id",notes = "目录图层关联id")
    private String dirLayerId;              //目录关联id
    @ApiModelProperty(value = "图层索引集合",notes = "图层索引集合")
    private String layerTables;             //图层表名集合

    @ApiModelProperty(value = "已授权用户数",notes = "已授权用户数")
    private String authorCount;             //已授权用户数
    @ApiModelProperty(value = "",notes = "")
    private String roleLayerId;             //角色关联id
    @ApiModelProperty(value = "用户关联id",notes = "用户关联id")
    private String userLayerId;             //用户关联id
    @ApiModelProperty(value = "字段配置关联id",notes = "字段配置关联id")
    private String layerFieldId;            //字段配置关联id
    @ApiModelProperty(value = "要素类型集合",notes = "要素类型集合")
    private String featureTypes;            //要素类型集合
    @ApiModelProperty(value = "数据源ID",notes = "数据源ID")
    private String dataSourceId;            //数据源ID  矢量图册新增字段

    @ApiModelProperty(value = "矢量图层表主键",notes = "矢量图层表主键")
    private String pkColumn;                //矢量图层表主键
    @ApiModelProperty(value = "矢量图层空间字段",notes = "矢量图层空间字段")
    private String geometryColumn;         //矢量图层空间字段
    @ApiModelProperty(value = "是否是矢量",notes = "是否是矢量")
    private String isVector;                //是否是矢量
    @ApiModelProperty(value = "属性表关联字段",notes = "属性表关联字段")
    private String attrTableField;          //属性表关联字段
    @ApiModelProperty(value = "空间表关联字段",notes = "空间表关联字段")
    private String spatialTableField;       //空间表关联字段
    //扩展字段
    @ApiModelProperty(value = "图层可见最小缩放等级",notes = "图层可见最小缩放等级")
    private String visibleMinZoom;          //图层可见最小缩放等级
    @ApiModelProperty(value = "图层可见最大缩放等级",notes = "图层可见最大缩放等级")
    private String visibleMaxZoom;          //图层可见最大缩放等级
    @ApiModelProperty(value = "图层基础颜色",notes = "图层基础颜色")
    private String baseColor;          //图层亮度
    @ApiModelProperty(value = "图层亮度",notes = "图层亮度")
    private String specular;             //图层亮度
    @ApiModelProperty(value = "是否弹出",notes = "是否弹出")
    private String isSelfPoPup;          //图层亮度
    @ApiModelProperty(value = "弹出内容",notes = "弹出内容")
    private String selfPoPupContent;          //自弹出内容
    @ApiModelProperty(value = "创建人",notes = "创建人")
    private String creator;             //图层亮度
    @ApiModelProperty(value = "创建时间,后台自动生成",dataType = "string")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String CreateDate;
    @ApiModelProperty(value = "备注",notes = "备注")
    private String remarks;          //自弹出内容
    @ApiModelProperty(value = "服务启用状态",notes = "服务启用状态 0：关闭状态 1：开启状态")
    private String status;          //自弹出内容
    @ApiModelProperty(value = "是否外部服务",notes = "是否外部服务 0：不是  1：是")
    private String isFromExternal;
    @ApiModelProperty(value = "转换规则码",notes = "匹配到数据集成网关中的规则，规则在字典中定义，这里记录字典的Key")
    private String changeRuleCode;
    @ApiModelProperty(value = "数据清洗规则码",notes = "可选择哪些数据进行读取，过滤条件，规则在字典中定义，这里记录字典的Key")
    private String filterRuleCode;
    @ApiModelProperty(value = "转发规则码",notes = "设置数据需要转发给什么实体对象，规则在字典中定义，这里记录字典的Key")
    private String sendRuleCode;
    @ApiModelProperty(value = "行为方式码",notes = "设置转发实体后的行为方式，如改变颜色，规则在字典中定义，这里记录字典的Key")
    private String actionRuleCode;
    @ApiModelProperty(value = "转存规则",notes = "设置数据是否需要存储 0：不需要 1：需要")
    private String saveEnable;
    @ApiModelProperty(value = "异常处理规则",notes = "如发生错误时，如何处理，规则在字典中定义，这里记录字典的Key")
    private String errorRuleCode;
    @ApiModelProperty(value = "样式关联id",notes = "在样式管理表中的id")
    private String styleManagerId;
    @ApiModelProperty(value = "图层版本号",notes = "使用版本代理的时候需要用图层版本号")
    private String layerVersion;
    @ApiModelProperty(value = "聚合名称",notes = "使用版本代理的时候需要用聚合名称")
    private String layerAggregateName;

//    private String tileSize;                //瓦片大小
//    private String format;                  //瓦片后缀
//    private String zoomOffset;              //偏移级别
//    private String srs;                     //坐标投影
//    private String namespace;               //命名空间  Arcgis wfs  leaflet目前不支持
//    private String geometry;                //空间字段
//    private String regExAndValue;           //正则过滤表达式
//    private String tileMatrixSet;           //wmts服务矩阵集
//    private String style;                   //样式
//    private String dataSourceId;            //数据源ID  矢量图册新增字段
//    private String pkColumn;                //矢量图层表主键
//    private String geometryColumn;             //矢量图层空间字段
//    private String isVector;                //是否是矢量
//    private String typeNS;                  //名称空间 Geoserver wfs
//    private String typeName;                //类型名称
//    private String geometryField;           //空间字段

//    public String getTypeNS() {
//        return typeNS;
//    }
//
//    public void setTypeNS(String typeNS) {
//        this.typeNS = typeNS;
//    }
//
//    public String getGeometryField() {
//        return geometryField;
//    }
//
//    public void setGeometryField(String geometryField) {
//        this.geometryField = geometryField;
//    }
//
//    public String getTypeName() {
//        return typeName;
//    }
//
//    public void setTypeName(String typeName) {
//        this.typeName = typeName;
//    }
//
//    public String getStyle() {
//        return style;
//    }
//
//    public void setStyle(String style) {
//        this.style = style;
//    }


    public String getLayerVersion() {
        return layerVersion;
    }

    public void setLayerVersion(String layerVersion) {
        this.layerVersion = layerVersion;
    }

    public String getLayerAggregateName() {
        return layerAggregateName;
    }

    public void setLayerAggregateName(String layerAggregateName) {
        this.layerAggregateName = layerAggregateName;
    }

    public String getStyleManagerId() {
        return styleManagerId;
    }

    public void setStyleManagerId(String styleManagerId) {
        this.styleManagerId = styleManagerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProxyUrl() {
        return proxyUrl;
    }

    public void setProxyUrl(String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    public String getVisibleMinZoom() {
        return visibleMinZoom;
    }

    public void setVisibleMinZoom(String visibleMinZoom) {
        this.visibleMinZoom = visibleMinZoom;
    }

    public String getVisibleMaxZoom() {
        return visibleMaxZoom;
    }

    public void setVisibleMaxZoom(String visibleMaxZoom) {
        this.visibleMaxZoom = visibleMaxZoom;
    }

    public String getIsExternal() {
        return isExternal;
    }

    public void setIsExternal(String isExternal) {
        this.isExternal = isExternal;
    }
    //    public String getTileSize() {
//        return tileSize;
//    }
//
//    public void setTileSize(String tileSize) {
//        this.tileSize = tileSize;
//    }
//
//    public String getFormat() {
//        return format;
//    }
//
//    public void setFormat(String format) {
//        this.format = format;
//    }
//
//    public String getZoomOffset() {
//        return zoomOffset;
//    }
//
//    public void setZoomOffset(String zoomOffset) {
//        this.zoomOffset = zoomOffset;
//    }
//
//    public String getSrs() {
//        return srs;
//    }
//
//    public void setSrs(String srs) {
//        this.srs = srs;
//    }
//
//    public String getNamespace() {
//        return namespace;
//    }
//
//    public void setNamespace(String namespace) {
//        this.namespace = namespace;
//    }
//
//    public String getGeometry() {
//        return geometry;
//    }
//
//    public void setGeometry(String geometry) {
//        this.geometry = geometry;
//    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return SysUtil.replaceLayerOut(url);
    }

    public void setUrl(String url) {
        this.url = SysUtil.replaceLayerIn(url);
    }

    public String getLayerType() {
        return layerType;
    }

    public void setLayerType(String layerType) {
        this.layerType = layerType;
    }

    public String getShortCut() {
        return shortCut;
    }

    public void setShortCut(String shortCut) {
        this.shortCut = shortCut;
    }

    public String getAddFlag() {
        return addFlag;
    }

    public void setAddFlag(String addFlag) {
        this.addFlag = addFlag;
    }

    public String getLayerTypeCn() {
        return layerTypeCn;
    }

    public void setLayerTypeCn(String layerTypeCn) {
        this.layerTypeCn = layerTypeCn;
    }

    public String getLayerTable() {
        return layerTable;
    }

    public void setLayerTable(String layerTable) {
        this.layerTable = layerTable;
    }

    public String getDirId() {
        return dirId;
    }

    public void setDirId(String dirId) {
        this.dirId = dirId;
    }

    public String get_parentId() {
        return _parentId;
    }

    public void set_parentId(String _parentId) {
        this._parentId = _parentId;
    }


    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getDirLayerId() {
        return dirLayerId;
    }

    public void setDirLayerId(String dirLayerId) {
        this.dirLayerId = dirLayerId;
    }

    public String getAuthorCount() {
        return authorCount;
    }

    public void setAuthorCount(String authorCount) {
        this.authorCount = authorCount;
    }

    public String getRoleLayerId() {
        return roleLayerId;
    }

    public void setRoleLayerId(String roleLayerId) {
        this.roleLayerId = roleLayerId;
    }

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getIsProxy() {
        return isProxy;
    }

    public void setIsProxy(String isProxy) {
        this.isProxy = isProxy;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getExtent() {
        return extent;
    }

    public void setExtent(String extent) {
        this.extent = extent;
    }


    public String getFeatureType() {
        return featureType;
    }

    public void setFeatureType(String featureType) {
        this.featureType = featureType;
    }

    public String getLayerTables() {
        return layerTables;
    }

    public String getLayerFieldId() {
        return layerFieldId;
    }

    public void setLayerFieldId(String layerFieldId) {
        this.layerFieldId = layerFieldId;
    }

    public void setLayerTables(String layerTables) {
        this.layerTables = layerTables;
    }

    //    public String getRegExAndValue() {
//        return regExAndValue;
//    }
//

    public String getUserLayerId() {
        return userLayerId;
    }

    public void setUserLayerId(String userLayerId) {
        this.userLayerId = userLayerId;
    }

    //    public void setRegExAndValue(String regExAndValue) {
//        this.regExAndValue = regExAndValue;
//    }
//
//    public String getTileMatrixSet() {
//        return tileMatrixSet;
//    }
//
//    public void setTileMatrixSet(String tileMatrixSet) {
//        this.tileMatrixSet = tileMatrixSet;
//    }
//
    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(String pkColumn) {
        this.pkColumn = pkColumn;
    }

    public String getGeometryColumn() {
        return geometryColumn;
    }

    public void setGeometryColumn(String geometryColumn) {
        this.geometryColumn = geometryColumn;
    }

    public String getIsVector() {
        return isVector;
    }

    public void setIsVector(String isVector) {
        this.isVector = isVector;
    }

    public String getFeatureTypes() {
        return featureTypes;
    }

    public void setFeatureTypes(String featureTypes) {
        this.featureTypes = featureTypes;
    }

    public String getVectorLayerId() {
        return vectorLayerId;
    }

    public void setVectorLayerId(String vectorLayerId) {
        this.vectorLayerId = vectorLayerId;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(String metadataId) {
        this.metadataId = metadataId;
    }

    public String getAttrTableField() {
        return attrTableField;
    }

    public void setAttrTableField(String attrTableField) {
        this.attrTableField = attrTableField;
    }

    public String getSpatialTableField() {
        return spatialTableField;
    }

    public void setSpatialTableField(String spatialTableField) {
        this.spatialTableField = spatialTableField;
    }

    public String getBaseColor() {
        return baseColor;
    }

    public void setBaseColor(String baseColor) {
        this.baseColor = baseColor;
    }

    public String getSpecular() {
        return specular;
    }

    public void setSpecular(String specular) {
        this.specular = specular;
    }

    public String getIsSelfPoPup() {
        return isSelfPoPup;
    }

    public void setIsSelfPoPup(String isSelfPoPup) {
        this.isSelfPoPup = isSelfPoPup;
    }

    public String getSelfPoPupContent() {
        return selfPoPupContent;
    }

    public void setSelfPoPupContent(String selfPoPupContent) {
        this.selfPoPupContent = selfPoPupContent;
    }




    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getIsFromExternal() {
        return isFromExternal;
    }

    public void setIsFromExternal(String isFromExternal) {
        this.isFromExternal = isFromExternal;
    }

    public String getChangeRuleCode() {
        return changeRuleCode;
    }

    public void setChangeRuleCode(String changeRuleCode) {
        this.changeRuleCode = changeRuleCode;
    }

    public String getFilterRuleCode() {
        return filterRuleCode;
    }

    public void setFilterRuleCode(String filterRuleCode) {
        this.filterRuleCode = filterRuleCode;
    }

    public String getSendRuleCode() {
        return sendRuleCode;
    }

    public void setSendRuleCode(String sendRuleCode) {
        this.sendRuleCode = sendRuleCode;
    }

    public String getActionRuleCode() {
        return actionRuleCode;
    }

    public void setActionRuleCode(String actionRuleCode) {
        this.actionRuleCode = actionRuleCode;
    }

    public String getSaveEnable() {
        return saveEnable;
    }

    public void setSaveEnable(String saveEnable) {
        this.saveEnable = saveEnable;
    }

    public String getErrorRuleCode() {
        return errorRuleCode;
    }

    public void setErrorRuleCode(String errorRuleCode) {
        this.errorRuleCode = errorRuleCode;
    }
}
