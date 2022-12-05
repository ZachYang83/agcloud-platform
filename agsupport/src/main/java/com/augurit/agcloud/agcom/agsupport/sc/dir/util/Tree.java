package com.augurit.agcloud.agcom.agsupport.sc.dir.util;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;

import java.util.List;
import java.util.Map;

/**
 * Created by Augurit on 2017-04-19.
 */
public class Tree {
    private String id;
    private String text;
    private String pid;
    private String xpath;
    private String state;
    private Map<String, Object> attributes;
    private List children;
    private List<AgLayer> layers;
    private String iconCls;
    private String viewIcon;
    private String pId;
    private String open = "false";

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public List<AgLayer> getLayers() {
        return layers;
    }

    public void setLayers(List<AgLayer> layers) {
        this.layers = layers;
    }

    public List getChildren() {
        return children;
    }

    public void setChildren(List children) {
        this.children = children;
    }

    public String getViewIcon() {
        return viewIcon;
    }

    public void setViewIcon(String viewIcon) {
        this.viewIcon = viewIcon;
    }
}
