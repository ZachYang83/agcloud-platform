package com.augurit.agcloud.agcom.agsupport.sc.dir.controller.form;

import java.util.List;
import java.util.Map;

/**
 * Created by Augurit on 2017-05-22.
 */
public class DirTree {

    private String id;
    private String text;            //目录名称
    private String pid;
    private Map state;           //默认勾选状态
    private List nodes;     //子节点

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map getState() {
        return state;
    }

    public void setState(Map state) {
        this.state = state;
    }

    public List getNodes() {
        return nodes;
    }

    public void setNodes(List nodes) {
        this.nodes = nodes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
