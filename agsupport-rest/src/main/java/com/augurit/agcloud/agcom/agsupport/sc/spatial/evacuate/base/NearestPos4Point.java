package com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.base;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;

public class NearestPos4Point {
    /**
     * 投影的点
     */
    public Point point;
    /**
     * 投影点到原点的距离
     */
    public double DistanceSq;

    public NearestPos4Point() {
    }

    public NearestPos4Point(Point point, double distanceSq) {
        this.point = point;
        DistanceSq = distanceSq;
    }
}
