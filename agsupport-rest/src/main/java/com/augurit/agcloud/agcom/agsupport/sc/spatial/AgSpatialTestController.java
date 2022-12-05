package com.augurit.agcloud.agcom.agsupport.sc.spatial;

import com.coordtrans.bean.BaseCoordinate;
import com.coordtrans.bean.EllipsoidParameter;
import com.coordtrans.service.ICoordTransBase;
import com.coordtrans.service.impl.CoordTransBaseImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.*;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-05-05.
 */
@RestController
@RequestMapping("/agsupport/spatial")
public class AgSpatialTestController {

    private static List<Map> createData(String type) {
        List<Map> data = new ArrayList<Map>();
        //伪造
        for (int i = 0; i < 25 * 100 * 5; i++) {
            Map dataMap = new HashMap();
            dataMap.put("name", data.size() + 1);
            dataMap.put("sex", "男");
            dataMap.put("age", 25);
            dataMap.put("wkt", createWkt(type));
            data.add(dataMap);
        }
        for (int i = 0; i < 25 * 100 * 5; i++) {
            Map dataMap = new HashMap();
            dataMap.put("name", data.size() + 1);
            dataMap.put("sex", "女");
            dataMap.put("age", 35);
            dataMap.put("wkt", createWkt(type));
            data.add(dataMap);
        }
        /*for (int i = 0; i < 25 * 100 * 10; i++) {
            Map dataMap = new HashMap();
            String x = String.valueOf(CoordTransUtil.round(180 * Math.random(), 8));
            String y = String.valueOf(CoordTransUtil.round(180 * Math.random(), 8));
            String wkt = "POINT (" + x + " " + y + ")";
            dataMap.put("name", data.size() + 1);
            dataMap.put("sex", "女");
            dataMap.put("age", 15);
            dataMap.put("x", x);
            dataMap.put("y", y);
            dataMap.put("wkt", wkt);
            data.add(dataMap);
        }
        for (int i = 0; i < 25 * 100 * 10; i++) {
            Map dataMap = new HashMap();
            String x = String.valueOf(CoordTransUtil.round(180 * Math.random(), 8));
            String y = String.valueOf(CoordTransUtil.round(180 * Math.random(), 8));
            String wkt = "POINT (" + x + " " + y + ")";
            dataMap.put("name", data.size() + 1);
            dataMap.put("sex", "女");
            dataMap.put("age", 45);
            dataMap.put("x", x);
            dataMap.put("y", y);
            dataMap.put("wkt", wkt);
            data.add(dataMap);
        }*/
        return data;
    }

    private static String createWkt(String type) {
        String wkt = "";
        if (POINT.equals(type)) {
            wkt = type + " (" + 180 * Math.random() + " " + 180 * Math.random() + ")";
        } else if (LINESTRING.equals(type)) {
            double x = 180 * Math.random();
            double y = 180 * Math.random();
            double r = 0.1;
            wkt = type + " (" + x + " " + y + "," + (x + r) + " " + (y + r) + "," + (x + 2 * r) + " " + y + ")";
        } else if (POLYGON.equals(type)) {
            double x = 180 * Math.random();
            double y = 180 * Math.random();
            double r = 0.1;
            wkt = type + " ((" + x + " " + y + "," + (x + r) + " " + y + "," + (x + r) + " " + (y + r) + "," + x + " " + (y + r) + "," + x + " " + y + "))";
        }
        return wkt;
    }

    @RequestMapping("/createData")
    public String mainTest(HttpServletRequest request) {
        String result = "";
        try {
            // dataSourceId spring.datasource
            // tableName ag_spatial_temp ag_spatial_temp_big
            // wkt 夏港街面 POLYGON  ((38453158.8491 2553595.5068, 38453274.6251 2553478.6475, 38453048.7068 2553075.6113, 38452924.9191 2552489.8210, 38452650.8634 2551817.2090, 38452177.2215 2551012.9872, 38451750.6862 2550805.1417, 38451539.8059 2550623.8696, 38451084.0302 2550084.7189, 38450565.9739 2549334.0180, 38450426.6660 2549097.1573, 38450308.9529 2548755.2332, 38450202.1900 2548212.1984, 38449942.8921 2548535.2507, 38448880.0469 2550542.2946, 38448621.6844 2550825.2796, 38448010.2904 2551242.5579, 38447955.5917 2551358.8301, 38447739.5588 2552560.9923, 38447989.1715 2552598.4800, 38447841.3817 2552885.0755, 38448544.9604 2552823.5101, 38449348.4313 2552368.9561, 38449665.3473 2552551.6099, 38449658.5219 2552678.0824, 38449742.4041 2552889.9390, 38450031.5498 2552882.4164, 38450052.1187 2553132.7106, 38450363.9035 2553126.8952, 38450679.4355 2553274.4130, 38450873.6500 2552648.5938, 38450897.2208 2553228.9446, 38450724.8114 2553631.8533, 38451360.4378 2553694.0373, 38451451.4903 2553669.6148, 38451569.8144 2553451.4774, 38451954.1044 2553341.2341, 38452107.5059 2553345.2735, 38452085.0415 2553484.4240, 38452158.2770 2553591.6435, 38452501.2842 2553549.2738, 38452819.0750 2553704.7319, 38452979.0294 2553701.0479, 38453158.8491 2553595.5068))
            // wkt 中心点 38450507.09195 2550958.46515
            // wkt 最小xy 38447739.5588 2548212.1984
            // wkt 最大xy 38453274.6251 2553704.7319
            // wkt 凸包圆半径 2767.53315
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
//        String dataSourceId = "spring.datasource";
//        String tableName = "ag_spatial_temp";
//        String pkColumn = "id";
//        long startMili = System.currentTimeMillis();
//        StringBuffer sql = new StringBuffer("select ").append(" count(*) ").append(STATS_COLUMN).append(" from ")
//                .append(tableName).append(" a where ").append(pkColumn).append(" is not null ");
//        StringBuffer sql = new StringBuffer("select ").append(" count(*) ").append(STATS_COLUMN).append(" from ")
//                .append(tableName);
//        List<Map> data = DBHelper.find(dataSourceId, sql.toString(), null);
//        Integer count = Integer.parseInt(data.get(0).get(STATS_COLUMN).toString());

//        DBHelper.save("spatial_temp", "ag_spatial_temp_polyline", createData(LINESTRING));
//        long endMili = System.currentTimeMillis();
//        System.out.println("--------------------共耗时：" + (endMili - startMili) / 1000.0 + "秒");
//        System.out.println("--------------------记录总数：" + count + "条");
        ICoordTransBase iCoordTransBase = new CoordTransBaseImpl();
        //经纬度坐标
        BaseCoordinate cood_l = new BaseCoordinate(113.5139, 23.0554);
        //84椭球定义
        EllipsoidParameter ep_84 = new EllipsoidParameter();
        ep_84.setA(6378137);
        ep_84.setF(1 / 298.257223563);
        //84的平面坐标
        BaseCoordinate cood_xy = iCoordTransBase.GeoToPla(cood_l, ep_84, 114);
        System.out.println(cood_xy.toString());
    }
}
