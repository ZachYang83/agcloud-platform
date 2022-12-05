package com.augurit.agcloud.agcom.agsupport.sc.datacatalog.util;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/2.
 */
public class TreeNode {
    private String id;
    private String text;
    private String tflag;
    private String state;
    private Map<String, String> attributes;
    private List<TreeNode> children;
    private String iconCls;


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


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getTflag() {
        return tflag;
    }

    public void setTflag(String tflag) {
        this.tflag = tflag;
    }
}
