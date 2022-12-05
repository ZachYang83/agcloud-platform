package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * 元数据-字段
 * @author Administrator
 *
 */
public class MetaDataField {
	private String id;  // 主键
	private String name; // 英文名称
	private String cname; // 中文名称
	private String description; // 描述
	private String type;  // 类型
	private String constraint; // 约束
 	private String unit; // 单位
	private String dd;   // 数据字典
	private String editable; // 是否编辑
	private String visible; // 是否显示
	private String tableid; // 表id
	private String dispsort; // 排序
	private String prikey; // 主键
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
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getConstraint() {
		return constraint;
	}
	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getDd() {
		return dd;
	}
	public void setDd(String dd) {
		this.dd = dd;
	}
	public String getEditable() {
		return editable;
	}
	public void setEditable(String editable) {
		this.editable = editable;
	}
	public String getVisible() {
		return visible;
	}
	public void setVisible(String visible) {
		this.visible = visible;
	}
	public String getTableid() {
		return tableid;
	}
	public void setTableid(String tableid) {
		this.tableid = tableid;
	}
	public String getDispsort() {
		return dispsort;
	}
	public void setDispsort(String dispsort) {
		this.dispsort = dispsort;
	}
	public String getPrikey() {
		return prikey;
	}
	public void setPrikey(String prikey) {
		this.prikey = prikey;
	}
	
	
}