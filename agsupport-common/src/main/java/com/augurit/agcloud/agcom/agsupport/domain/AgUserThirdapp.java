package com.augurit.agcloud.agcom.agsupport.domain;

public class AgUserThirdapp {
    private String id;

    private String userId;

    private String appId;

    private Long orderNo;

    private String toDesktop;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public String getToDesktop() {
        return toDesktop;
    }

    public void setToDesktop(String toDesktop) {
        this.toDesktop = toDesktop;
    }

    @Override
    public String toString() {
        return "AgUserThirdapp{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", appId='" + appId + '\'' +
                ", orderNo=" + orderNo +
                ", toDesktop='" + toDesktop + '\'' +
                '}';
    }
}