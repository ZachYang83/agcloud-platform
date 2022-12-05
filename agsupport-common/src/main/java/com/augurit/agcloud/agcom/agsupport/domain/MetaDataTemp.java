package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * 目录模板
 * @author Administrator
 *
 */
public class MetaDataTemp {
	private String id;
	private String temp_name;
	private String table_name;
	private String temp_dir;
	private String last_update;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTemp_name() {
		return temp_name;
	}
	public void setTemp_name(String temp_name) {
		this.temp_name = temp_name;
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public String getTemp_dir() {
		return temp_dir;
	}
	public void setTemp_dir(String temp_dir) {
		this.temp_dir = temp_dir;
	}
	public String getLast_update() {
		return last_update;
	}
	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}
	
}
