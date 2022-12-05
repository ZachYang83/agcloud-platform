package com.augurit.agcloud.agcom.agsupport.domain;

import com.augurit.agcloud.opus.common.vo.OpuRsFuncVo;

public class OpuRsToolBarVo extends OpuRsFuncVo {

    private String toolBarId;

    private String src;

    private String onload;

    private String toolBarInvokeUrl;

    private String widgetId;

    private String icon;

    public String getToolBarId() {
        return toolBarId;
    }

    public void setToolBarId(String toolBarId) {
        this.toolBarId = toolBarId;
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

    public String getToolBarInvokeUrl() {
        return toolBarInvokeUrl;
    }

    public void setToolBarInvokeUrl(String toolBarInvokeUrl) {
        this.toolBarInvokeUrl = toolBarInvokeUrl;
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
}
