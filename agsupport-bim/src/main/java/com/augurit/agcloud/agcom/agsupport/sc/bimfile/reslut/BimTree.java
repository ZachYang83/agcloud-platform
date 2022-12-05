package com.augurit.agcloud.agcom.agsupport.sc.bimfile.reslut;

import java.util.List;

/**
 * @ClassName BimTree
 * @Description TODO
 * @Author Administrator
 * @Date 2019/10/23 17:24
 * @Version 1.0
 **/
public class BimTree {

    private String bimName;
    private String bimUrl;
    private Boolean bimIsChild;
    private List<BimTree> bimTreeList;

    public BimTree(){}

    public BimTree(String bimName, String bimUrl, Boolean bimIsChild, List<BimTree> bimTreeList) {
        this.bimName = bimName;
        this.bimUrl = bimUrl;
        this.bimIsChild = bimIsChild;
        this.bimTreeList = bimTreeList;
    }

    public String getBimName() {
        return bimName;
    }

    public void setBimName(String bimName) {
        this.bimName = bimName;
    }

    public String getBimUrl() {
        return bimUrl;
    }

    public void setBimUrl(String bimUrl) {
        this.bimUrl = bimUrl;
    }

    public Boolean getBimIsChild() {
        return bimIsChild;
    }

    public void setBimIsChild(Boolean bimIsChild) {
        this.bimIsChild = bimIsChild;
    }

    public List<BimTree> getBimTreeList() {
        return bimTreeList;
    }

    public void setBimTreeList(List<BimTree> bimTreeList) {
        this.bimTreeList = bimTreeList;
    }
}
