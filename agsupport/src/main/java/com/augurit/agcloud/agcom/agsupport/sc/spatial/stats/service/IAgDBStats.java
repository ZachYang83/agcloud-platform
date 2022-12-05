package com.augurit.agcloud.agcom.agsupport.sc.spatial.stats.service;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Range;

import java.util.List;
import java.util.Map;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-04-18.
 */
public interface IAgDBStats {

    /**
     * 计算表数据总数量
     *
     * @param layer
     * @return
     */
    Integer statsCount(Layer layer);

    /**
     * 统计所有数据
     *
     * @param layer 图层表
     * @return
     */
    Map<String, Double> statsAll(Layer layer);

    /**
     * 对圆进行统计
     *
     * @param layer 图层表
     * @param point 圆心
     * @param r     半径
     * @return
     */
    Map<String, Double> statsByOval(Layer layer, Point point, double r);

    /**
     * 对矩形范围进行统计
     *
     * @param layer 图层表
     * @param range 矩形范围
     * @return
     */
    Map<String, Double> statsByExtent(Layer layer, Range range);

    /**
     * 对线进行统计
     *
     * @param layer
     * @param points 坐标列
     * @return
     */
    Map<String, Double> statsByPolyline(Layer layer, List<Point> points);

    /**
     * 对面范围进行统计
     *
     * @param layer
     * @param points 坐标列
     * @return
     */
    Map<String, Double> statsByPolygon(Layer layer, List<Point> points);

    /**
     * 对输入的WKT空间范围字符串进行统计
     *
     * @param layer
     * @param wkt
     * @return
     */
    Map<String, Double> statsByWKT(Layer layer, String wkt);

    /**
     * 对输入的WKT空间范围字符串进行统计 data中至少要有wkt值和x y值其中一组 优先读取wkt
     *
     * @param data        统计数据
     * @param statsColumn 统计字段
     * @param groupColumn 分组字段
     * @param type        统计类型
     * @param wkt         统计范围
     * @return
     */
    Map<String, Double> statsByWKT(List<Map> data, String statsColumn, String groupColumn, String type, String wkt);
}
