package com.augurit.agcloud.agcom.agsupport.sc.project.controller.form;

/**
 * Created by chendingxing on 2018-01-03.
 */
public class MapParamResult {
    private String id;
    private String extent;
    private String origin;
    private String tileOrigin;
    private String zoom;
    private String center;
    private String scales;
    private String resolutions;
    private String reference;
    private String name;
    private String minZoom;
    private String maxZoom;

    public String getTileOrigin() {
        return tileOrigin;
    }

    public void setTileOrigin(String tileOrigin) {
        this.tileOrigin = tileOrigin;
    }

    private Long roadPathId;
    private Long busChangeId;
    private Long isDefault;

    public MapParamResult() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExtent() {
        return extent;
    }

    public void setExtent(String extent) {
        this.extent = extent;
    }

    public String getZoom() {
        return zoom;
    }

    public void setZoom(String zoom) {
        this.zoom = zoom;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getScales() {
        return scales;
    }

    public void setScales(String scales) {
        this.scales = scales;
    }

    public String getResolutions() {
        return resolutions;
    }

    public void setResolutions(String resolutions) {
        this.resolutions = resolutions;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRoadPathId() {
        return roadPathId;
    }

    public void setRoadPathId(Long roadPathId) {
        this.roadPathId = roadPathId;
    }

    public Long getBusChangeId() {
        return busChangeId;
    }

    public void setBusChangeId(Long busChangeId) {
        this.busChangeId = busChangeId;
    }

    public Long getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Long isDefault) {
        this.isDefault = isDefault;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(String minZoom) {
        this.minZoom = minZoom;
    }

    public String getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(String maxZoom) {
        this.maxZoom = maxZoom;
    }
}
