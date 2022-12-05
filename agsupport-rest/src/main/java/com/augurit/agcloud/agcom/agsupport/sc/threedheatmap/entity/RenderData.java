package com.augurit.agcloud.agcom.agsupport.sc.threedheatmap.entity;

import java.util.List;

/**
 * @author Liangjh
 * @version 1.0
 * @date 2020/2/28
 * @description 三维热力图前端json格式
 */
public class RenderData
{
    public double meshSize;
    public int meshWidth;
    public int meshHeight;
    public Double[] meshPoint;

    public RenderData(double meshSize,int meshWidth, int meshHeight,Double[] meshPoint){
        this.meshSize = meshSize;
        this.meshWidth = meshWidth;
        this.meshHeight = meshHeight;
        this.meshPoint = meshPoint;
    }

    public double getMeshSize() {
        return meshSize;
    }

    public void setMeshSize(double meshSize) {
        this.meshSize = meshSize;
    }

    public int getMeshWidth() {
        return meshWidth;
    }

    public void setMeshWidth(int meshWidth) {
        this.meshWidth = meshWidth;
    }

    public int getMeshHeight() {
        return meshHeight;
    }

    public void setMeshHeight(int meshHeight) {
        this.meshHeight = meshHeight;
    }

    public Double[] getMeshPoint() {
        return meshPoint;
    }

    public void setMeshPoint(Double[] meshPoint) {
        this.meshPoint = meshPoint;
    }
}