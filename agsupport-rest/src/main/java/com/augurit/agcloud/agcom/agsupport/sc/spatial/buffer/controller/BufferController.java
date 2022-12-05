package com.augurit.agcloud.agcom.agsupport.sc.spatial.buffer.controller;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Range;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.buffer.service.IAgDBBuffer;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
@Api(value = "数据缓存",description = "数据缓冲相关接口")
@RestController
@RequestMapping("/agsupport/buffer")
public class BufferController {
    @Autowired
    private IAgDBBuffer dbBuffer;

    /**
     * 对点进行缓冲分析
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @ApiOperation(value = "对点进行缓冲分析",notes = "对点进行缓冲分析接口")
    @RequestMapping(value = "/bufferQueryByPoint",method = RequestMethod.GET)
    public ContentResultForm bufferQueryByPoint(HttpServletRequest request) throws JSONException {
        String js = "";
        try {
            String layerStr = URLDecoder.decode(request.getParameter("layer"), "UTF-8");
            String xyStr = URLDecoder.decode(request.getParameter("xy"), "UTF-8");
            double distance = Double.valueOf(request.getParameter("distance"));
            Layer layer = (Layer) JSONObject.toBean(JSONObject.fromObject(layerStr), Layer.class);
            Point point = new Point(xyStr.split(","));
            List<Map> result = dbBuffer.bufferQueryByPoint(layer, point, distance);
            js = JSONObject.fromObject(result).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ContentResultForm(true,js);
    }

    /**
     * 对矩形范围进行缓冲分析
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @ApiOperation(value = "对矩形范围进行缓冲分析",notes = "对矩形范围进行缓冲分析接口")
    @RequestMapping(value = "/bufferQueryByExtent",method = RequestMethod.GET)
    public ContentResultForm bufferQueryByExtent(HttpServletRequest request) throws JSONException {
        String js = "";
        try {
            String layerStr = URLDecoder.decode(request.getParameter("layer"), "UTF-8");
            String extentStr = URLDecoder.decode(request.getParameter("extent"), "UTF-8");
            Layer layer = (Layer) JSONObject.toBean(JSONObject.fromObject(layerStr), Layer.class);
            Range range = new Range(extentStr.split(","));
            List<Map> result = dbBuffer.bufferQueryByExtent(layer, range);
            js = JSONObject.fromObject(result).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ContentResultForm(true,js);
    }

    /**
     * 对线范围进行缓冲分析
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @ApiOperation(value = "对线范围进行缓冲分析",notes = "对线范围进行缓冲分析接口")
    @RequestMapping(value = "/bufferQueryByPolyline",method = RequestMethod.GET)
    public ContentResultForm bufferQueryByPolyline(HttpServletRequest request) throws JSONException {
        String js = "";
        try {
            String layerStr = URLDecoder.decode(request.getParameter("layer"), "UTF-8");
            String xys = URLDecoder.decode(request.getParameter("xys"), "UTF-8");
            double distance = Double.valueOf(request.getParameter("distance"));
            Layer layer = (Layer) JSONObject.toBean(JSONObject.fromObject(layerStr), Layer.class);
            JSONArray xyAr = JSONArray.fromObject(xys);
            List<Point> points = new ArrayList<Point>();
            for (Object xy : xyAr) {
                points.add(new Point(String.valueOf(xy).split(",")));
            }
            List<Map> result = dbBuffer.bufferQueryByPolyline(layer, points, distance);
            js = JSONObject.fromObject(result).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ContentResultForm(true,js);
    }

    /**
     * 对面范围进行缓冲分析
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @ApiOperation(value = "对面范围进行缓冲分析",notes = "对面范围进行缓冲分析接口")
    @RequestMapping(value = "/bufferQueryByPolygon",method = RequestMethod.GET)
    public ContentResultForm bufferQueryByPolygon(HttpServletRequest request) throws JSONException {
        String js = "";
        try {
            String layerStr = URLDecoder.decode(request.getParameter("layer"), "UTF-8");
            String xys = URLDecoder.decode(request.getParameter("xys"), "UTF-8");
            double distance = Double.valueOf(request.getParameter("distance"));
            Layer layer = (Layer) JSONObject.toBean(JSONObject.fromObject(layerStr), Layer.class);
            JSONArray xyAr = JSONArray.fromObject(xys);
            List<Point> points = new ArrayList<Point>();
            for (Object xy : xyAr) {
                points.add(new Point(String.valueOf(xy).split(",")));
            }
            List<Map> result = dbBuffer.bufferQueryByPolygon(layer, points, distance);
            js = JSONObject.fromObject(result).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ContentResultForm(true,js);
    }

    /**
     * 对输入的WKT空间范围字符串进行缓冲分析
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @ApiOperation(value = "对输入的WKT空间范围字符串进行缓冲分析",notes = "对输入的WKT空间范围字符串进行缓冲分析接口")
    @RequestMapping(value = "/bufferQueryByWKT",method = RequestMethod.GET)
    public ContentResultForm bufferQueryByWKT(HttpServletRequest request) throws JSONException {
        String js = "";
        try {
            String layerStr = URLDecoder.decode(request.getParameter("layer"), "UTF-8");
            String wkt = URLDecoder.decode(request.getParameter("wkt"), "UTF-8");
            double distance = Double.valueOf(request.getParameter("distance"));
            Layer layer = (Layer) JSONObject.toBean(JSONObject.fromObject(layerStr), Layer.class);
            List<Map> result = dbBuffer.bufferQueryByWKT(layer, wkt, distance);
            js = JSONObject.fromObject(result).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ContentResultForm(true,js);
    }

    /**
     * 对点进行缓冲统计
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @ApiOperation(value = "对点进行缓冲统计",notes = "对点进行缓冲统计接口")
    @RequestMapping(value = "/bufferStatsByPoint",method = RequestMethod.GET)
    public ContentResultForm bufferStatsByPoint(HttpServletRequest request) throws JSONException {
        String js = "";
        try {
            String layerStr = URLDecoder.decode(request.getParameter("layer"), "UTF-8");
            String xyStr = URLDecoder.decode(request.getParameter("xy"), "UTF-8");
            double distance = Double.valueOf(request.getParameter("distance"));
            Layer layer = (Layer) JSONObject.toBean(JSONObject.fromObject(layerStr), Layer.class);
            Point point = new Point(xyStr.split(","));
            Map result = dbBuffer.bufferStatsByPoint(layer, point, distance);
            js = JSONObject.fromObject(result).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ContentResultForm(true,js);
    }

    /**
     * 对矩形范围进行缓冲统计
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @ApiOperation(value = "对矩形范围进行缓冲统计",notes = "对矩形范围进行缓冲统计接口")
    @RequestMapping(value = "/bufferStatsByExtent",method = RequestMethod.GET)
    public ContentResultForm bufferStatsByExtent(HttpServletRequest request) throws JSONException {
        String js = "";
        try {
            String layerStr = URLDecoder.decode(request.getParameter("layer"), "UTF-8");
            String extentStr = URLDecoder.decode(request.getParameter("extent"), "UTF-8");
            Layer layer = (Layer) JSONObject.toBean(JSONObject.fromObject(layerStr), Layer.class);
            Range range = new Range(extentStr.split(","));
            Map result = dbBuffer.bufferStatsByExtent(layer, range);
            js = JSONObject.fromObject(result).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ContentResultForm(true,js);
    }

    /**
     * 对线范围进行缓冲统计
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @ApiOperation(value = "对线范围进行缓冲统计",notes = "对线范围进行缓冲统计接口")
    @RequestMapping(value = "/bufferStatsByPolyline",method = RequestMethod.GET)
    public ContentResultForm bufferStatsByPolyline(HttpServletRequest request) throws JSONException {
        String js = "";
        try {
            String layerStr = URLDecoder.decode(request.getParameter("layer"), "UTF-8");
            String xys = URLDecoder.decode(request.getParameter("xys"), "UTF-8");
            double distance = Double.valueOf(request.getParameter("distance"));
            Layer layer = (Layer) JSONObject.toBean(JSONObject.fromObject(layerStr), Layer.class);
            JSONArray xyAr = JSONArray.fromObject(xys);
            List<Point> points = new ArrayList<Point>();
            for (Object xy : xyAr) {
                points.add(new Point(String.valueOf(xy).split(",")));
            }
            Map result = dbBuffer.bufferStatsByPolyline(layer, points, distance);
            js = JSONObject.fromObject(result).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ContentResultForm(true,js);
    }

    /**
     * 对面范围进行缓冲统计
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @ApiOperation(value = "对面范围进行缓冲统计",notes = "对面范围进行缓冲统计接口")
    @RequestMapping(value = "/bufferStatsByPolygon",method = RequestMethod.GET)
    public ContentResultForm bufferStatsByPolygon(HttpServletRequest request) throws JSONException {
        String js = "";
        try {
            String layerStr = URLDecoder.decode(request.getParameter("layer"), "UTF-8");
            String xys = URLDecoder.decode(request.getParameter("xys"), "UTF-8");
            double distance = Double.valueOf(request.getParameter("distance"));
            Layer layer = (Layer) JSONObject.toBean(JSONObject.fromObject(layerStr), Layer.class);
            JSONArray xyAr = JSONArray.fromObject(xys);
            List<Point> points = new ArrayList<Point>();
            for (Object xy : xyAr) {
                points.add(new Point(String.valueOf(xy).split(",")));
            }
            Map result = dbBuffer.bufferStatsByPolygon(layer, points, distance);
            js = JSONObject.fromObject(result).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ContentResultForm(true,js);
    }

    /**
     * 对输入的WKT空间范围字符串进行缓冲统计
     *
     * @param request
     * @return
     * @throws JSONException
     */
    @ApiOperation(value = "对输入的WKT空间范围字符串进行缓冲统计",notes = "对输入的WKT空间范围字符串进行缓冲统计接口")
    @RequestMapping(value = "/bufferStatsByWKT",method = RequestMethod.GET)
    public ContentResultForm bufferStatsByWKT(HttpServletRequest request) throws JSONException {
        String js = "";
        try {
            String layerStr = URLDecoder.decode(request.getParameter("layer"), "UTF-8");
            String wkt = URLDecoder.decode(request.getParameter("wkt"), "UTF-8");
            double distance = Double.valueOf(request.getParameter("distance"));
            Layer layer = (Layer) JSONObject.toBean(JSONObject.fromObject(layerStr), Layer.class);
            Map result = dbBuffer.bufferStatsByWKT(layer, wkt, distance);
            js = JSONObject.fromObject(result).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ContentResultForm(true,js);
    }
}
