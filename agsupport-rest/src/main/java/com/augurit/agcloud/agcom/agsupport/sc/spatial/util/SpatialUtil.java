package com.augurit.agcloud.agcom.agsupport.sc.spatial.util;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Range;
import com.common.dbcp.DBHelper;
import com.common.thread.Executer;
import com.common.thread.Job;
import com.common.util.DBUtil;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.geojson.geom.GeometryJSON;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.*;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017.
 */
public class SpatialUtil {

    /**
     * 将Point对象转换为点wkt
     *
     * @param point 点坐标
     * @return String wkt
     */
    public static String pointToWKT(Point point) {
        String wkt = POINT + "  (" + point.x + " " + point.y + ")";
        return wkt;
    }

    /**
     * 将点wkt转换为Point对象
     *
     * @param wkt 点wkt
     * @return
     */
    public static Point WKTToPoint(String wkt) {
        Point point = new Point();
        try {
            wkt = formatWKT(wkt);
            if (wkt.startsWith(POINT)) {
                point = new Point(wkt.substring(wkt.indexOf("(") + 1, wkt.lastIndexOf(")")).split(" "));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return point;
    }

    /**
     * 将Range对象转换为面wkt
     *
     * @param range 矩形范围
     * @return
     */
    public static String rangeToWKT(Range range) {
        String wkt = POLYGON + "  ((" + range.getMinX() + " " + range.getMinY() + ", " + range.getMinX() + " " + range.getMaxY()
                + ", " + range.getMaxX() + " " + range.getMaxY() + ", " + range.getMaxX() + " " + range.getMinY() + ", "
                + range.getMinX() + " " + range.getMinY() + "))";
        return wkt;
    }

    /**
     * 将Point列表转换为线、面wkt
     *
     * @param points Point列
     * @param type   LINESTRING 线 POLYGON 面 MULTIPOINT 多点 MULTILINESTRING 多线 MULTIPOLYGON 多面
     * @return
     */
    public static String pointListToWKT(List<Point> points, String type) {
        String wkt = "";
        if (type.equals(POINT)) {
            return pointToWKT(points.get(0));
        }
        if (type.equals(LINESTRING)) {
            wkt = LINESTRING + "  (";
        } else if (type.equals(POLYGON)) {
            wkt = POLYGON + "  ((";
        } else if (type.equals(MULTIPOINT)) {
            wkt = MULTIPOINT + "  (";
        } else if (type.equals(MULTILINESTRING)) {
            wkt = MULTILINESTRING + "  ((";
        } else if (type.equals(MULTIPOLYGON)) {
            wkt = MULTIPOLYGON + "  (((";
        }
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            wkt += point.x + " " + point.y;
            if (i < points.size() - 1) {
                wkt += ", ";
            } else {
                if (type.equals(LINESTRING)) {
                    wkt += ")";
                } else if (type.equals(POLYGON)) {
                    //保证闭合面
                    Point p0 = points.get(0);
                    if (p0 == point || (p0.x == point.x && p0.y == p0.y)) {
                        wkt += "))";
                    } else {
                        wkt += ", " + p0.x + " " + p0.y + "))";
                    }
                } else if (type.equals(MULTIPOINT)) {
                    wkt += ")";
                } else if (type.equals(MULTILINESTRING)) {
                    wkt += "))";
                } else if (type.equals(MULTIPOLYGON)) {
                    //保证闭合面
                    Point p0 = points.get(0);
                    if (p0 == point || (p0.x == point.x && p0.y == p0.y)) {
                        wkt += ")))";
                    } else {
                        wkt += ", " + p0.x + " " + p0.y + ")))";
                    }
                }
            }
        }
        return wkt;
    }

    /**
     * 将wkt转换为pointList
     *
     * @param wkt
     * @return
     */
    public static List<Point> WKTToPointList(String wkt) {
        List<Point> pointLs = new ArrayList<Point>();
        try {
            wkt = formatWKT(wkt);
            if (wkt.startsWith(POINT)) {
                Point point = new Point(wkt.substring(wkt.indexOf("(") + 1, wkt.lastIndexOf(")")).split(" "));
                pointLs.add(point);
            } else if (wkt.startsWith(LINESTRING)) {
                String[] points = wkt.substring(wkt.indexOf("(") + 1, wkt.lastIndexOf(")")).split(",");
                for (int i = 0; i < points.length; i++) {
                    Point point = new Point(points[i].split(" "));
                    pointLs.add(point);
                }
            } else if (wkt.startsWith(POLYGON)) {
                String[] polygons = wkt.substring(wkt.indexOf("(") + 2, wkt.lastIndexOf(")") - 1).split("\\),\\(");
                for (int j = 0; j < polygons.length; j++) {
                    String[] points = polygons[j].split(",");
                    for (int i = 0; i < points.length; i++) {
                        Point point = new Point(points[i].split(" "));
                        pointLs.add(point);
                    }
                }
            } else if (wkt.startsWith(MULTIPOINT)) {
                String[] points = wkt.substring(wkt.indexOf("(") + 1, wkt.lastIndexOf(")")).split(",");
                for (int i = 0; i < points.length; i++) {
                    Point point = new Point(points[i].split(" "));
                    pointLs.add(point);
                }
            } else if (wkt.startsWith(MULTILINESTRING)) {
                String[] polygons = wkt.substring(wkt.indexOf("(") + 2, wkt.lastIndexOf(")") - 1).split("\\),\\(");
                for (int j = 0; j < polygons.length; j++) {
                    String[] points = polygons[j].split(",");
                    for (int i = 0; i < points.length; i++) {
                        Point point = new Point(points[i].split(" "));
                        pointLs.add(point);
                    }
                }
            } else if (wkt.startsWith(MULTIPOLYGON)) {
                String[] multipolygons = wkt.substring(wkt.indexOf("(") + 3, wkt.lastIndexOf(")") - 2).split("\\)\\),\\(\\(");
                for (int k = 0; k < multipolygons.length; k++) {
                    String[] polygons = multipolygons[k].split("\\),\\(");
                    for (int j = 0; j < polygons.length; j++) {
                        String[] points = polygons[j].split(",");
                        for (int i = 0; i < points.length; i++) {
                            Point point = new Point(points[i].split(" "));
                            pointLs.add(point);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return pointLs;
    }

    /**
     * 获取点列表的xy最大范围
     *
     * @param points
     * @return
     */
    public static Range getRangeByPointList(List<Point> points) {
        Range range = null;
        try {
            if (points != null && points.size() > 0) {
                for (Point point : points) {
                    if (null == range) {
                        range = new Range(point, point);
                    } else {
                        if (range.getMinX() > point.x) {
                            range.setMinX(point.x);
                        }
                        if (range.getMaxX() < point.x) {
                            range.setMaxX(point.x);
                        }
                        if (range.getMinY() > point.y) {
                            range.setMinY(point.y);
                        }
                        if (range.getMaxY() < point.y) {
                            range.setMaxY(point.y);
                        }
                    }
                }
            } else {
                range = new Range(0, 0, 0, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return range;
    }

    /**
     * 获取WKT的xy最大范围
     *
     * @param wkt
     * @return
     */
    public static Range getRangeByWKT(String wkt) {
        Range range = null;
        try {
            List<Point> pointLs = WKTToPointList(wkt);
            range = getRangeByPointList(pointLs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return range;
    }

    /**
     * 格式化wkt为适合本util解析的wkt
     *
     * @param wkt_1
     * @return
     */
    public static String formatWKT(String wkt_1) {
        String wkt_2 = wkt_1;
        try {
            wkt_1 = wkt_1.toUpperCase();
            wkt_1 = wkt_1.replace("  ", " ");
            wkt_1 = wkt_1.replace("( ", "(");
            wkt_1 = wkt_1.replace(") ", ")");
            wkt_1 = wkt_1.replace(", ", ",");
            wkt_1 = wkt_1.replace(" (", "(");
            wkt_1 = wkt_1.replace(" )", ")");
            wkt_1 = wkt_1.replace(" ,", ",");
            if (wkt_1.startsWith(POINT)) {
                String[] point = wkt_1.substring(wkt_1.indexOf("(") + 1, wkt_1.lastIndexOf(")")).split(" ");
                wkt_2 = POINT + "  (" + point[0] + " " + point[1] + ")";
            } else if (wkt_1.startsWith(LINESTRING)) {
                wkt_2 = LINESTRING + "  (";
                String[] points = wkt_1.substring(wkt_1.indexOf("(") + 1, wkt_1.lastIndexOf(")")).split(",");
                for (int i = 0; i < points.length; i++) {
                    String[] point = points[i].split(" ");
                    wkt_2 += point[0] + " " + point[1];
                    if (i < points.length - 1) {
                        wkt_2 += ",";
                    }
                }
                wkt_2 += ")";
            } else if (wkt_1.startsWith(POLYGON)) {
                wkt_2 = POLYGON + "  ((";
                String[] polygons = wkt_1.substring(wkt_1.indexOf("(") + 2, wkt_1.lastIndexOf(")") - 1).split("\\),\\(");
                for (int j = 0; j < polygons.length; j++) {
                    String[] points = polygons[j].split(",");
                    for (int i = 0; i < points.length; i++) {
                        String[] point = points[i].split(" ");
                        wkt_2 += point[0] + " " + point[1];
                        if (i < points.length - 1) {
                            wkt_2 += ",";
                        }
                    }
                    if (j < polygons.length - 1) {
                        wkt_2 += "),(";
                    }
                }
                wkt_2 += "))";
            } else if (wkt_1.startsWith(MULTIPOINT)) {
                wkt_2 = MULTIPOINT + "  (";
                String[] points = wkt_1.substring(wkt_1.indexOf("(") + 1, wkt_1.lastIndexOf(")")).split(",");
                for (int i = 0; i < points.length; i++) {
                    String[] point = points[i].split(" ");
                    wkt_2 += point[0] + " " + point[1];
                    if (i < points.length - 1) {
                        wkt_2 += ",";
                    }
                }
                wkt_2 += ")";
            } else if (wkt_1.startsWith(MULTILINESTRING)) {
                wkt_2 = MULTILINESTRING + "  ((";
                String[] polygons = wkt_1.substring(wkt_1.indexOf("(") + 2, wkt_1.lastIndexOf(")") - 1).split("\\),\\(");
                for (int j = 0; j < polygons.length; j++) {
                    String[] points = polygons[j].split(",");
                    for (int i = 0; i < points.length; i++) {
                        String[] point = points[i].split(" ");
                        wkt_2 += point[0] + " " + point[1];
                        if (i < points.length - 1) {
                            wkt_2 += ",";
                        }
                    }
                    if (j < polygons.length - 1) {
                        wkt_2 += "),(";
                    }
                }
                wkt_2 += "))";
            } else if (wkt_1.startsWith(MULTIPOLYGON)) {
                wkt_2 = MULTIPOLYGON + "  (((";
                String[] multipolygons = wkt_1.substring(wkt_1.indexOf("(") + 3, wkt_1.lastIndexOf(")") - 2).split("\\)\\),\\(\\(");
                for (int k = 0; k < multipolygons.length; k++) {
                    String[] polygons = multipolygons[k].split("\\),\\(");
                    for (int j = 0; j < polygons.length; j++) {
                        String[] points = polygons[j].split(",");
                        for (int i = 0; i < points.length; i++) {
                            String[] point = points[i].split(" ");
                            wkt_2 += point[0] + " " + point[1];
                            if (i < points.length - 1) {
                                wkt_2 += ",";
                            }
                        }
                        if (j < polygons.length - 1) {
                            wkt_2 += "),(";
                        }
                    }
                    if (k < multipolygons.length - 1) {
                        wkt_2 += ")),((";
                    }
                }
                wkt_2 += ")))";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wkt_2;
    }

    public static String getDbTypeById(String datasourceId) {
        String dbtype = "";
        DBUtil db = null;
        try {
            db = DBHelper.getDBUtil(datasourceId);
            dbtype = db.getDbType();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
        return dbtype;
    }

    /**
     * 将double类型数据转换为百分比格式，并保留小数点前IntegerDigits位和小数点后FractionDigits位
     *
     * @param d
     * @param FractionDigits 小数点后保留几位
     * @return
     */
    public static String getPercentFormat(double d, int FractionDigits) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(FractionDigits);
        String str = nf.format(d);
        return str;
    }

    /**
     * 生成字段名方法 MD5加密并截取其中一部分
     *
     * @param code
     * @return
     */
    public static String generateMD5Code(String code) {
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update((code).getBytes("utf-8"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        byte[] messageDigest = algorithm.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; ++i) {
            if (Integer.toHexString(0xFF & messageDigest[i]).length() == 1) {
                hexString.append("0").append(Integer.toHexString(0xFF & messageDigest[i]));
            } else {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
        }

        return hexString.toString().toLowerCase();
    }

    /**
     * 数据源id放在表名后，横杠-转下滑杠_
     * @param dataSourceId
     * @param layerTable
     * @return
     */
    public static String getKey(String dataSourceId, String layerTable) {
        dataSourceId = dataSourceId.replaceAll("-","_");
        return layerTable+ "_" +dataSourceId;
    }

    public static List<Point> formatPoints(String points) {
        List<Point> pointList = new ArrayList<Point>();
        try {
            List<HashMap> pointMap = com.common.util.JsonUtils.toList(points, HashMap.class);
            for (HashMap map : pointMap) {
                double lng = Double.valueOf(map.get("lng").toString());
                double lat = Double.valueOf(map.get("lat").toString());
                pointList.add(new Point(lng, lat));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pointList;
    }

    public static String wktToJson(String wkt) {
        String json = null;
        try {
            WKTReader reader = new WKTReader();
            Geometry geometry = reader.read(wkt);
            StringWriter writer = new StringWriter();
            GeometryJSON gJson = new GeometryJSON();
            gJson.write(geometry, writer);
            json = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String jsonToWkt(String geoJson) {
        String wkt = null;
        GeometryJSON gJson = new GeometryJSON();
        Reader reader = new StringReader(geoJson);
        try {
            Geometry geometry = gJson.read(reader);
            wkt = geometry.toText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wkt;
    }

    public static Map formatDataMap(Map data) {
        Map result = data;
        try {
            if (result.get(WKT_COLUMN) != null) {
                String wkt = String.valueOf(result.get(WKT_COLUMN));
                wkt = SpatialUtil.formatWKT(wkt);
                String geoJson = SpatialUtil.wktToJson(wkt);
                result.put(GEO_COLUMN, geoJson);
                result.put(WKT_COLUMN, wkt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Map> formatDataMapList(List<Map> dataMap) {
        List<Map> result = new ArrayList<Map>();
        try {
            int size = dataMap.size();
            //当前计算机核数
            int rNum = Runtime.getRuntime().availableProcessors();
            //避免对计算机造成太大压力 当判断计算机为多核时 线程数为核数减一
            int nThreads = rNum > 1 ? (rNum - 1) : rNum;
            Executer exe = new Executer(nThreads);
            List<Future<List<Map>>> futures = new ArrayList<Future<List<Map>>>(nThreads);
            for (int i = 0; i < nThreads; i++) {
                //将List<Map> data分为nThreads段 放入nThreads个线程中分别执行
                final List<Map> subList = dataMap.subList(size / nThreads * i, (nThreads == (i + 1) ? size : size / nThreads * (i + 1)));
                Job<List<Map>> task = new Job<List<Map>>() {
                    public List<Map> execute() {
                        List<Map> list = new ArrayList<Map>();
                        for (Map dataMap : subList) {
                            list.add(formatDataMap(dataMap));
                        }
                        return list;
                    }
                };
                futures.add(exe.fork(task));
            }

            for (Future<List<Map>> future : futures) {
                //将执行结果添加到result中
                result.addAll(future.get());
            }
            exe.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

