package com.augurit.agcloud.agcom.agsupport.sc.project.controller.form;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018-01-04.
 */
public class DirLayerTree {
    private String id;
    private String text;
    private String checked;
    private String open;
    private Boolean disabled;
    private String child;
    private Map userdata;
    private List<DirLayerTree> item = new ArrayList<DirLayerTree>();
    private Integer order;
    private String dependId;// 父级ID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public Map getUserdata() {
        return userdata;
    }

    public void setUserdata(Map userdata) {
        this.userdata = userdata;
    }

    public List<DirLayerTree> getItem() {
        return item;
    }

    public void setItem(List<DirLayerTree> item) {
        this.item = item;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getDependId() {
        return dependId;
    }

    public void setDependId(String dependId) {
        this.dependId = dependId;
    }
}
