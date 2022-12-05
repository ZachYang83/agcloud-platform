package com.augurit.agcloud.agcom.agsupport.sc.spatial.buffer.service;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Range;

import java.util.List;
import java.util.Map;

/**
 * Created by q on 2017-03-31.
 */
public interface IAgDBBuffer {

    /**
     * 对点进行缓冲分析
     *
     * @param layer    图层表
     * @param point    点坐标
     * @param distance 距离
     * @return
     */
    List<Map> bufferQueryByPoint(Layer layer, Point point, double distance);

    /**
     * 对矩形范围进行缓冲分析
     *
     * @param layer 图层表
     * @param range 矩形范围
     * @return
     */
    List<Map> bufferQueryByExtent(Layer layer, Range range);

    /**
     * 对线范围进行缓冲分析
     *
     * @param layer
     * @param points   坐标列
     * @param distance 距离
     * @return
     */
    List<Map> bufferQueryByPolyline(Layer layer, List<Point> points, double distance);

    /**
     * 对面范围进行缓冲分析
     *
     * @param layer
     * @param points   坐标列
     * @param distance 距离
     * @return
     */
    List<Map> bufferQueryByPolygon(Layer layer, List<Point> points, double distance);

    /**
     * 对输入的WKT空间范围字符串进行缓冲分析
     *
     * @param layer
     * @param wkt
     * @param distance 距离
     * @return
     */
    List<Map> bufferQueryByWKT(Layer layer, String wkt, double distance);

    /**
     * 对输入的WKT空间范围字符串进行缓冲分析 data中至少要有wkt值和x y值其中一组 优先读取wkt
     *
     * @param data
     * @param wkt
     * @param distance 距离
     * @return
     */
    List<Map> bufferQueryByWKT(List<Map> data, String wkt, double distance);

    /**
     * 对点进行缓冲统计
     *
     * @param layer    图层表
     * @param point    点坐标
     * @param distance 距离
     * @return
     */
    Map bufferStatsByPoint(Layer layer, Point point, double distance);

    /**
     * 对矩形范围进行缓冲统计
     *
     * @param layer 图层表
     * @param range 矩形范围
     * @return
     */
    Map bufferStatsByExtent(Layer layer, Range range);

    /**
     * 对线范围进行缓冲统计
     *
     * @param layer
     * @param points   坐标列
     * @param distance 距离
     * @return
     */
    Map bufferStatsByPolyline(Layer layer, List<Point> points, double distance);

    /**
     * 对面范围进行缓冲统计
     *
     * @param layer
     * @param points   坐标列
     * @param distance 距离
     * @return
     */
    Map bufferStatsByPolygon(Layer layer, List<Point> points, double distance);

    /**
     * 对输入的WKT空间范围字符串进行缓冲统计
     *
     * @param layer
     * @param wkt
     * @param distance 距离
     * @return
     */
    Map bufferStatsByWKT(Layer layer, String wkt, double distance);

    /**
     * 对输入的WKT空间范围字符串进行缓冲统计 data中至少要有wkt值和x y值其中一组 优先读取wkt
     *
     * @param data        统计数据
     * @param statsColumn 统计字段
     * @param groupColumn 分组字段
     * @param type        统计类型
     * @param wkt         统计范围
     * @param distance    距离
     * @return
     */
    Map bufferStatsByWKT(List<Map> data, String statsColumn, String groupColumn, String type, String wkt, double distance);
}
