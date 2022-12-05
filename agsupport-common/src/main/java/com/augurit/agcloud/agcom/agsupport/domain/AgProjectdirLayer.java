package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * Created by Augurit on 2017-04-18.
 */
public class AgProjectdirLayer {
    private String id;            //编号
    private String projectdirId;        //目录id
    private String layerId;        //图层id private String addFlag;        //标识符

    private int orderNm;        //排序字段

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }


    public int getOrderNm() {
        return orderNm;
    }

    public void setOrderNm(int orderNm) {
        this.orderNm = orderNm;
    }

    public String getProjectdirId() {
        return projectdirId;
    }

    public void setProjectdirId(String projectdirId) {
        this.projectdirId = projectdirId;
    }
}
