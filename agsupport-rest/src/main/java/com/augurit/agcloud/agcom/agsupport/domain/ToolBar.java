package com.augurit.agcloud.agcom.agsupport.domain;

import java.io.Serializable;

public class ToolBar implements Serializable {

    private static final long serialVersionUID = 52042078716379489L;

    private String id;

    private String src;

    private String onload;

    private String url;

    private String widgetId;

    private String icon;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getOnload() {
        return onload;
    }

    public void setOnload(String onload) {
        this.onload = onload;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
