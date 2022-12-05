package com.augurit.agcloud.agcom.agsupport.sc.spatial.stats.controller;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Range;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.stats.service.IAgDBStats;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-04-28.
 */
@RestController
@RequestMapping("/agsupport/stats")
public class AgStatsController {
    @Autowired
    private IAgDBStats dbStats;

    /**
     * 对圆进行查询
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/statsByOval")
    public String statsByOval(HttpServletRequest request) throws JSONException {
        String js = "";
        try {
            String layerStr = URLDecoder.decode(request.getParameter("layer"), "UTF-8");
            String xyStr = URLDecoder.decode(request.getParameter("xy"), "UTF-8");
            double r = Double.valueOf(request.getParameter("r"));
            Layer layer = (Layer) JSONObject.toBean(JSONObject.fromObject(layerStr), Layer.class);
            Point point = new Point(xyStr.split(","));
            Map result = dbStats.statsByOval(layer, point, r);
            js = JSONObject.fromObject(result).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return js;
    }

    /**
     * 对矩形范围进行查询
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/statsByExtent")
    public String statsByExtent(HttpServletRequest request) throws JSONException {
        String js = "";
        try {
            String layerStr = URLDecoder.decode(request.getParameter("layer"), "UTF-8");
            String extentStr = URLDecoder.decode(request.getParameter("extent"), "UTF-8");
            Layer layer = (Layer) JSONObject.toBean(JSONObject.fromObject(layerStr), Layer.class);
            Range range = new Range(extentStr.split(","));
            Map result = dbStats.statsByExtent(layer, range);
            js = JSONObject.fromObject(result).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return js;
    }

    /**
     * 对线进行查询
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/statsByPolyline")
    public String statsByPolyline(HttpServletRequest request) throws JSONException {
        String js = "";
        try {
            String layerStr = URLDecoder.decode(request.getParameter("layer"), "UTF-8");
            String xys = URLDecoder.decode(request.getParameter("xys"), "UTF-8");
            Layer layer = (Layer) JSONObject.toBean(JSONObject.fromObject(layerStr), Layer.class);
            JSONArray xyAr = JSONArray.fromObject(xys);
            List<Point> points = new ArrayList<Point>();
            for (Object xy : xyAr) {
                points.add(new Point(String.valueOf(xy).split(",")));
            }
            Map result = dbStats.statsByPolyline(layer, points);
            js = JSONObject.fromObject(result).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return js;
    }

    /**
     * 对面范围进行查询
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/statsByPolygon")
    public String statsByPolygon(HttpServletRequest request) throws JSONException {
        String js = "";
        try {
            String layerStr = URLDecoder.decode(request.getParameter("layer"), "UTF-8");
            String xys = URLDecoder.decode(request.getParameter("xys"), "UTF-8");
            Layer layer = (Layer) JSONObject.toBean(JSONObject.fromObject(layerStr), Layer.class);
            JSONArray xyAr = JSONArray.fromObject(xys);
            List<Point> points = new ArrayList<Point>();
            for (Object xy : xyAr) {
                points.add(new Point(String.valueOf(xy).split(",")));
            }
            Map result = dbStats.statsByPolygon(layer, points);
            js = JSONObject.fromObject(result).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return js;
    }

    /**
     * 对输入的WKT空间范围字符串进行查询
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @RequestMapping("/statsByWKT")
    public String statsByWKT(HttpServletRequest request) throws JSONException {
        String js = "";
        try {
            String layerStr = URLDecoder.decode(request.getParameter("layer"), "UTF-8");
            String wkt = URLDecoder.decode(request.getParameter("wkt"), "UTF-8");
            Layer layer = (Layer) JSONObject.toBean(JSONObject.fromObject(layerStr), Layer.class);
            Map result = dbStats.statsByWKT(layer, wkt);
            js = JSONObject.fromObject(result).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return js;
    }
}
