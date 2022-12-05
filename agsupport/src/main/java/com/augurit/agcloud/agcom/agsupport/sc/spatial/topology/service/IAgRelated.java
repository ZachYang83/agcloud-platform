package com.augurit.agcloud.agcom.agsupport.sc.spatial.topology.service;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017.
 */
public interface IAgRelated {
    /**
     * 重叠、相等
     *
     * @param wkt1 "LINESTRING(0 0, 2 0, 5 0)"
     * @param wkt2 "LINESTRING(5 0, 0 0)"
     */
    boolean stEquals(String wkt1, String wkt2) throws ParseException;

    boolean stEquals(Geometry geometry1, Geometry geometry2);

    /**
     * 相离
     *
     * @param wkt1 ,"LINESTRING(0 0, 2 0, 5 0)"
     * @param wkt2 ,"LINESTRING(0 1, 0 2)"
     * @return
     */
    boolean stDisjoint(String wkt1, String wkt2) throws ParseException;

    boolean stDisjoint(Geometry geometry1, Geometry geometry2);

    /**
     * 相交
     *
     * @param wkt1 ,"LINESTRING(0 0, 2 0, 5 0)"
     * @param wkt2 ,"LINESTRING(0 0, 0 2)"
     * @return
     */
    boolean stIntersects(String wkt1, String wkt2) throws ParseException;

    boolean stIntersects(Geometry geometry1, Geometry geometry2);

    /**
     * 包含
     *
     * @param wkt1 ,"LINESTRING(0 0, 2 0, 5 0)"
     * @param wkt2 ,"LINESTRING(0 0, 0 2)"
     * @return
     */
    boolean stContains(String wkt1, String wkt2) throws ParseException;

    boolean stContains(Geometry geometry1, Geometry geometry2);

    /**
     * 穿过
     *
     * @param wkt1 ,"LINESTRING(0 0, 2 0, 5 0)"
     * @param wkt2 ,"LINESTRING(0 0, 0 2)"
     * @return
     */
    boolean stCrosses(String wkt1, String wkt2) throws ParseException;

    boolean stCrosses(Geometry geometry1, Geometry geometry2);

    /**
     * 在内部
     *
     * @param wkt1 ,"LINESTRING(0 0, 2 0, 5 0)"
     * @param wkt2 ,"LINESTRING(0 0, 0 2)"
     * @return
     */
    boolean stWithin(String wkt1, String wkt2) throws ParseException;

    boolean stWithin(Geometry geometry1, Geometry geometry2);

    /**
     * 相邻、相接
     *
     * @param wkt1 ,"LINESTRING(0 0, 2 0, 5 0)"
     * @param wkt2 ,"LINESTRING(0 0, 0 2)"
     * @return
     */
    boolean stTouches(String wkt1, String wkt2) throws ParseException;

    boolean stTouches(Geometry geometry1, Geometry geometry2);

    /**
     * 要素一被要素二覆盖
     *
     * @param wkt1
     * @param wkt2
     * @return
     */
    boolean stCoveredBy(String wkt1, String wkt2) throws ParseException;

    boolean stCoveredBy(Geometry geometry1, Geometry geometry2);

    /**
     * 要素一覆盖要素二
     *
     * @param wkt1
     * @param wkt2
     * @return
     */
    boolean stCovers(String wkt1, String wkt2) throws ParseException;

    boolean stCovers(Geometry geometry1, Geometry geometry2);

    /**
     * 完全相等
     *
     * @param wkt1
     * @param wkt2
     * @return
     */
    boolean stEqualsExact(String wkt1, String wkt2) throws ParseException;

    boolean stEqualsExact(Geometry geometry1, Geometry geometry2);

    /**
     * 完全相等,带容差
     *
     * @param wkt1
     * @param wkt2
     * @param tolerance
     * @return
     */
    boolean stEqualsExact(String wkt1, String wkt2, double tolerance) throws ParseException;

    boolean stEqualsExact(Geometry geometry1, Geometry geometry2, double tolerance);

    /**
     * 规范化比较(坐标点相等，序列顺序不同，返回为true)
     *
     * @param wkt1 ，"LINESTRING(0 0, 2 0, 5 0)"
     * @param wkt2 ，"LINESTRING(5 0,  2 0, 0 0)"
     * @return true
     */
    boolean stEqualsNorm(String wkt1, String wkt2) throws ParseException;

    boolean stEqualsNorm(Geometry geometry1, Geometry geometry2);

    /**
     * 拓扑比较
     *
     * @param wkt1 ,"LINESTRING(0 0, 2 0, 5 0)"
     * @param wkt2 ,"LINESTRING(2 0, 5 0, 0 0)"
     * @return true
     */
    boolean stEqualsTopo(String wkt1, String wkt2) throws ParseException;

    boolean stEqualsTopo(Geometry geometry1, Geometry geometry2);

    /**
     * 在距离范围内
     *
     * @param wkt1     ,"LINESTRING(0 0, 2 0, 5 0)"
     * @param wkt2     ,"LINESTRING(8 0, 7 0)"
     * @param distance 2(返回true)，1(返回false)
     * @return
     */
    boolean stIsWithinDistance(String wkt1, String wkt2, double distance) throws ParseException;

    boolean stIsWithinDistance(Geometry geometry1, Geometry geometry2, double distance);

    /**
     * 部分重叠
     *
     * @param wkt1
     * @param wkt2
     * @return LINESTRING(0 0, 2 0, 5 0)","LINESTRING(3 0, 7 0),返回false
     * LINESTRING(0 0, 2 0, 5 0)","LINESTRING(6 0, 7 0),返回true
     */
    boolean stOverlaps(String wkt1, String wkt2) throws ParseException;

    boolean stOverlaps(Geometry geometry1, Geometry geometry2);

    /**
     * 根据DE-9IM进行空间位置分析
     *
     * @param wkt1
     * @param wkt2
     * @param intersectionPattern ，如[FF*FF****]
     * @return
     */
    boolean stRelate(String wkt1, String wkt2, String intersectionPattern) throws ParseException;

    boolean stRelate(Geometry geometry1, Geometry geometry2, String intersectionPattern);

    /**
     * 比较
     *
     * @param wkt1
     * @param wkt2
     * @return
     */
    int stCompareTo(String wkt1, String wkt2) throws ParseException;

    int stCompareTo(Geometry geometry1, Geometry geometry2);

}
