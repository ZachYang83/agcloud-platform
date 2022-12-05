package com.augurit.agcloud.agcom.agsupport.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DirTreeForOpenMap implements Serializable {
    private String id;
    private String name;
    private int order;
    private int childrenCount;//子节点数量
    private List<DirTreeForOpenMap> children = new ArrayList<>();

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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }

    public List<DirTreeForOpenMap> getChildren() {
        return children;
    }

    public void setChildren(List<DirTreeForOpenMap> children) {
        this.children = children;
    }
}
