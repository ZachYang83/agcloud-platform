package com.augurit.agcloud.agcom.agsupport.sc.address.domain;

/**
 * Created by Administrator on 2017-09-09.
 */
public class AgDmdz {
    private String id;          //id
    private String province;   //所属省份
    private String city;        //所属城市

    private String district;    //所属区、县
    private String town;        //所属镇
    private String village;     //所属村
    private String street;     //所属街道
    private String doorpn;     //所属门牌号

    private String name;        //名称
    private String addname;    //地址名称
    private String tel;         //电话
    private String x;           //纬度x
    private String y;           //经度y
    private String wkt;         //wkt
    private String shape;      //空间数据

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddname() {
        return addname;
    }

    public void setAddname(String addname) {
        this.addname = addname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getWkt() {
        return wkt;
    }

    public void setWkt(String wkt) {
        this.wkt = wkt;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDoorpn() {
        return doorpn;
    }

    public void setDoorpn(String doorpn) {
        this.doorpn = doorpn;
    }
}
