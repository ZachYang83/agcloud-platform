package com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.base;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;

/// <summary>
/// 单点数据
/// </summary>
public class Shapefile_Point {

    public double x;
    public double y;
    /// <summary>
    /// Tag
    /// </summary>
    public Object Tag;

    /// <summary>
    /// 原始的值X
    /// </summary>
    public double getX() {
        return x;
    }

    public void setX(double value) {
        x = value;
    }

    /// <summary>
    /// 原始的值Y
    /// </summary>
    public double getY() {
        return y;
    }

    public void setY(double value) {
        y = value;
    }

    //region 构造函数
    public Shapefile_Point() {
    }

    public Shapefile_Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
    //endregion

    /// <summary>
    /// 转换为X,y,Z
    /// </summary>
    /// <returns></returns>
    @Override
    public String toString() {
        return x + "," + y;
    }

    // region 比较
    public boolean equals(Shapefile_Point other) {
        if (Math.abs(getX() - other.getX()) > 1e-5 / 2)
            return false;
        return !(Math.abs(getY() - other.getY()) > 1e-5 / 2);
    }

    /**
     * 比较两点点是否一致
     *
     * @param other
     * @return
     */
    public boolean equals(Point other) {
        if (Math.abs(getX() - other.x) > 1e-5 / 2)
            return false;
        return !(Math.abs(getY() - other.y) > 1e-5 / 2);
    }
    // endregion
}