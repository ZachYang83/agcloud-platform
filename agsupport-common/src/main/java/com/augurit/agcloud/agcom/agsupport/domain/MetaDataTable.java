package com.augurit.agcloud.agcom.agsupport.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 元数据-表
 * @author Administrator
 *
 */
public  class MetaDataTable {
	// 格式化日期对象
	private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String id;  // 主键
	private String name; // 英文名称
	private String cname;  // 中文名称
	private String databaseid; // 数据库
	private String lastupdate=format.format(new Date());  // 更新时间
	private String dirlayerid;
	private String layer_config;
	private String searfield;

	public String getFieldunit() {
		return fieldunit;
	}

	public void setFieldunit(String fieldunit) {
		this.fieldunit = fieldunit;
	}

	private String distfield;  // 地区字段
	private String priid;
	private String sumfield;//累计字段SUMFIELD
	private String groupfield;//分组字段GROUPFIELD
	private String fieldunit;//统计单位
	public String getSumfield() {
		return sumfield;
	}

	public void setSumfield(String sumfield) {
		this.sumfield = sumfield;
	}

	public String getGroupfield() {
		return groupfield;
	}

	public void setGroupfield(String groupfield) {
		this.groupfield = groupfield;
	}


	public String getLayer_config() {
		return layer_config;
	}
	public void setLayer_config(String layer_config) {
		this.layer_config = layer_config;
	}
	
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
	public String getDatabaseid() {
		return databaseid;
	}
	public void setDatabaseid(String databaseid) {
		this.databaseid = databaseid;
	}
	public String getLastupdate() {
		return lastupdate;
	}
	public void setLastupdate(String lastupdate) {
		this.lastupdate = lastupdate;
	}
	public String getDirlayerid() {
		return dirlayerid;
	}
	public void setDirlayerid(String dirlayerid) {
		this.dirlayerid = dirlayerid;
	}
	public String getPriid() {
		return priid;
	}
	public void setPriid(String priid) {
		this.priid = priid;
	}
	public String getDistfield() {
		return distfield;
	}
	public void setDistfield(String distfield) {
		this.distfield = distfield;
	}
	public String getSearfield() {
		return searfield;
	}
	public void setSearfield(String searfield) {
		this.searfield = searfield;
	}	
}
