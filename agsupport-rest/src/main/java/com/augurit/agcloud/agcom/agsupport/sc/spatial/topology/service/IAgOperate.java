package com.augurit.agcloud.agcom.agsupport.sc.spatial.topology.service;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;

import java.util.List;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017.
 */
public interface IAgOperate {
    /**
     * 缓冲分析
     *
     * @param wkt
     * @param dBuffer
     * @return
     */
    String buffer(String wkt, double dBuffer) throws ParseException;

    Geometry buffer(Geometry geometry, double dBuffer);

    /**
     * 相交
     *
     * @param wkt1
     * @param wkt2
     * @return
     */
    String intersect(String wkt1, String wkt2) throws ParseException;

    Geometry intersect(Geometry geometry1, Geometry geometry2);

    /**
     * 获取凸包
     *
     * @param wkt
     * @return
     */
    String convexHull(String wkt) throws ParseException;

    Geometry convexHull(Geometry geometry);

    /**
     * 合并
     *
     * @param wkt1
     * @param wkt2
     * @return
     */
    String union(String wkt1, String wkt2) throws ParseException;

    Geometry union(Geometry geometry1, Geometry geometry2);

    /**
     * 擦除
     *
     * @param wkt1
     * @param wkt2
     * @return
     */
    String difference(String wkt1, String wkt2) throws ParseException;

    Geometry difference(Geometry geometry1, Geometry geometry2);

    /**
     * 交集取反
     *
     * @param wkt1
     * @param wkt2
     * @return
     */
    String symDifference(String wkt1, String wkt2) throws ParseException;

    Geometry symDifference(Geometry geometry1, Geometry geometry2);

    /**
     * 弧段闭合
     *
     * @param wkt
     * @return
     */
    String closingLineString(String wkt) throws ParseException;

    Geometry closingLineString(LineString lineString);

    /**
     * 距离
     *
     * @param wkt1
     * @param wkt2
     * @return
     */
    String distance(String wkt1, String wkt2) throws ParseException;

    String distance(Geometry geometry1, Geometry geometry2);

    /**
     * 克隆
     *
     * @param wkt
     * @return
     */
    String clone(String wkt) throws ParseException;

    Geometry clone(Geometry geometry);

    /**
     * 平滑
     *
     * @param wkt
     * @param fit
     * @return
     */

    String smooth(String wkt, double fit) throws ParseException;

    Geometry smooth(Geometry geometry, double fit) throws ParseException;

    /**
     * 抽稀
     *
     * @param wkt
     * @return
     */
    String sparse(String wkt, double fit) throws ParseException;

    Geometry sparse(Geometry geometry, double fit) throws ParseException;

    /**
     * 把空间坐标点转换成WKT格式
     *
     * @param points
     * @param type
     * @return
     */
    String pasePointsToWKT(List<Point> points, String type) throws ParseException;
}
