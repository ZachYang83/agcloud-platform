package com.augurit.agcloud.agcom.agsupport.domain;

import java.util.Date;

/**
 * 元数据-数据库
 * @author Administrator
 *
 */
public class MetaDataDB {
	private String id;  // 主键
	private String name; // 英文名称
	private String cname; // 中文名称
	private String url; // url
	private String dbtype; // 数据类型
	private Date createtime;//创建时间
	private Date edittime;//修改时间
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

	public String getDbtype() {
		return dbtype;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getEdittime() {
		return edittime;
	}

	public void setEdittime(Date edittime) {
		this.edittime = edittime;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}




}
