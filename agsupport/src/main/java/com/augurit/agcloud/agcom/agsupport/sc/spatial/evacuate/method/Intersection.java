package com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.method;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.base.Shapefile_Point;

public class Intersection {

    /**
     * 在赤道或者经度方面，多少米一度
     * 大约是111319.490
     *
     * @return
     */
    public static double MeterPerDegree() {
        double meterPerDegree = 6378137.0 * Math.PI / 180;
        return meterPerDegree;
    }

    //region 获得距离
    /// <summary>
    /// 获得点对的距离
    /// </summary>
    /// <param name="p1"></param>
    /// <param name="p2"></param>
    /// <returns></returns>
    public static double GetDistance(Shapefile_Point p1, Shapefile_Point p2) {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }

    public static double GetDistance(Point p1, Point p2) {
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }

    public static Point getInterpolation(Point p1, Point p2, double length, double stepDis) {
        if (length == 0)
            return p1;
        return new Point(p1.x + stepDis * (p2.x - p2.x) / length, p1.y + stepDis * (p2.y - p2.y) / length);
    }

    /// <summary>
    /// 获得点对的距离的平方
    /// </summary>
    /// <param name="p1"></param>
    /// <param name="p2"></param>
    /// <returns></returns>
    public static double GetDistanceSq(Shapefile_Point p1, Shapefile_Point p2) {
        return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
    }
    //endregion
}
