package com.augurit.agcloud.agcom.agsupport.sc.buildingComponent.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: libc
 * @Description: 树节点实体
 * @Date: 2020/11/5 10:01
 * @Version: 1.0
 */
public class TreeNode<T> {

    public TreeNode() {
    }

    public TreeNode(T node, String key, String title, String parentKey) {
        this.node = node;
        this.key = key;
        this.title = title;
        this.parentKey = parentKey;
    }

    /**
     * 节点对象
     */
    private T node;
    /**
     * a-tree树key
     */
    private String key;
    /**
     * a-tree树标题
     */
    private String title;
    /**
     * a-tree树父节点key
     */
    private String parentKey;
    /**
     * a-tree树子节点
     */
    private List<TreeNode<T>> children = new ArrayList<>();

    public T getNode() {
        return node;
    }

    public void setNode(T node) {
        this.node = node;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode<T>> children) {
        this.children = children;
    }
}
