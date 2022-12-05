package com.augurit.agcloud.agcom.agsupport.domain;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017.
 */
public class Menu {

    private static final long serialVersionUID = 1L;

    private String id;//主键

    private String name;//菜单名称

    private String url;//菜单访问地址

    private String show;//是否显示

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }
}
