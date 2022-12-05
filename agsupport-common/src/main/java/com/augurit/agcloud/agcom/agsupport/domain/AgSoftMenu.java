package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * @author zhangmingyang
 * @Description: agcloud的应用菜单bean转为agsupport的应用菜单类
 * @date 2019-03-19 10:15
 */
public class AgSoftMenu {
    private String id;
    private String softCode;
    private String softName;
    private String softInnerUrl;
    private String softGovUrl;
    private String softOuterUrl;
    private String isImgIcon;
    private String smallImgPath;
    private String middleImgPath;
    private String bigImgPath;
    private String hugeImgPath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSoftCode() {
        return softCode;
    }

    public void setSoftCode(String softCode) {
        this.softCode = softCode;
    }

    public String getSoftName() {
        return softName;
    }

    public void setSoftName(String softName) {
        this.softName = softName;
    }

    public String getSoftInnerUrl() {
        return softInnerUrl;
    }

    public void setSoftInnerUrl(String softInnerUrl) {
        this.softInnerUrl = softInnerUrl;
    }

    public String getSoftGovUrl() {
        return softGovUrl;
    }

    public void setSoftGovUrl(String softGovUrl) {
        this.softGovUrl = softGovUrl;
    }

    public String getSoftOuterUrl() {
        return softOuterUrl;
    }

    public void setSoftOuterUrl(String softOuterUrl) {
        this.softOuterUrl = softOuterUrl;
    }

    public String getIsImgIcon() {
        return isImgIcon;
    }

    public void setIsImgIcon(String isImgIcon) {
        this.isImgIcon = isImgIcon;
    }

    public String getSmallImgPath() {
        return smallImgPath;
    }

    public void setSmallImgPath(String smallImgPath) {
        this.smallImgPath = smallImgPath;
    }

    public String getMiddleImgPath() {
        return middleImgPath;
    }

    public void setMiddleImgPath(String middleImgPath) {
        this.middleImgPath = middleImgPath;
    }

    public String getBigImgPath() {
        return bigImgPath;
    }

    public void setBigImgPath(String bigImgPath) {
        this.bigImgPath = bigImgPath;
    }

    public String getHugeImgPath() {
        return hugeImgPath;
    }

    public void setHugeImgPath(String hugeImgPath) {
        this.hugeImgPath = hugeImgPath;
    }
}
