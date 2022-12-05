package com.augurit.agcloud.agcom.agsupport.sc.spatial.base;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017.
 */
public class Point {
    public double x;
    public double y;

    public Point(String[] point) {
        if (point.length >= 2) {
            this.x = Double.parseDouble(point[0]);
            this.y = Double.parseDouble(point[1]);
        }
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point() {

    }

    @Override
    public String toString() {
        return x + "," + y;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {

        return x;
    }

    public void setX(double x) {
        this.x = x;
    }
}
