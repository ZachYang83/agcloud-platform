package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * Created by Augurit on 2017-04-18.
 */
public class AgDirLayer {
    private String id;            //编号
    private String dirId;        //目录id
    private String layerId;        //图层id
    private String addFlag;        //标识符
    private int orderNm;        //排序字段

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDirId() {
        return dirId;
    }

    public void setDirId(String dirId) {
        this.dirId = dirId;
    }

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public String getAddFlag() {
        return addFlag;
    }

    public void setAddFlag(String addFlag) {
        this.addFlag = addFlag;
    }

    public int getOrderNm() {
        return orderNm;
    }

    public void setOrderNm(int orderNm) {
        this.orderNm = orderNm;
    }
}
