package com.augurit.agcloud.agcom.agsupport.sc.spatial.base;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017.
 */
public class Range {
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public Range(String[] range) {
        if (range.length == 4) {
            this.minX = Double.valueOf(range[0]);
            this.maxX = Double.valueOf(range[1]);
            this.minY = Double.valueOf(range[2]);
            this.maxY = Double.valueOf(range[3]);
        }
    }

    public Range(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public Range(Point point_1, Point point_2) {
        if (point_1.x < point_2.x) {
            this.minX = point_1.x;
            this.maxX = point_2.x;
        } else {
            this.minX = point_2.x;
            this.maxX = point_1.x;
        }
        if (point_1.y < point_2.y) {
            this.minY = point_1.y;
            this.maxY = point_2.y;
        } else {
            this.minY = point_2.y;
            this.maxY = point_1.y;
        }
    }

    public Range() {

    }
}
