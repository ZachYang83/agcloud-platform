package com.augurit.agcloud.agcom.agsupport.sc.topology.web;

import com.augurit.agcloud.agcom.agsupport.common.util.ElementUtil;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.topology.service.IAgSpatialUtilsService;
import com.common.util.HttpRequester;
import com.common.util.HttpRespons;
import com.common.util.RequestUtil;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author：李德文
 * @CreateDate： 2014-07-01
 * @Description: 拓扑接口控制类
 * @Copyright: 广州奥格智能科技有限公司
 */
@Controller
@RequestMapping(value = "/agsupport/Topology")
public class AgTopologyController {
    @Autowired
    private IAgSpatialUtilsService agSpatialUtilsService;

    @Autowired
    private IAgDir iAgDir;

    private String geometryURL;

    @PostConstruct
    private void init() {
        try {
            geometryURL = iAgDir.findLayerByNameCn("geometry.url") != null ? iAgDir.findLayerByNameCn("geometry.url").getUrl() : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 当第二个几何完全被第一个几何包含的时候返回true
     *
     * @param destination 几何体wkt格式(不能为空)
     * @param source      几何体wkt格式(不能为空)
     * @param rtnType     返回格式（默认为xml）rtnType=json rtnType=xml
     * @return
     */
    @RequestMapping("/contains")
    @ResponseBody
    public String contains(String destination, String source, String rtnType) {
        try {
            JSONObject object = new JSONObject();
            String rtnString = agSpatialUtilsService.stContains(destination, source).toString();
            if (rtnType != null && "json".equals(rtnType)) {
                object.put("isContains", rtnString);
                return object.toString();
            }
            Document doc = ElementUtil.getRestFunctionXML(rtnString, "topology", "contains", "当第二个几何完全被第一个几何包含的时候返回true");
            return doc.asXML();
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    @RequestMapping("/containsPost")
    @ResponseBody
    public String containsPost(HttpServletRequest request) {
        String destination = request.getParameter("destination");
        String source = request.getParameter("source");
        String rtnType = request.getParameter("rtnType");
        return this.contains(destination, source, rtnType);
    }

    /**
     * 当两个几何相交的时候返回true
     *
     * @param destination 几何体wkt格式(不能为空)
     * @param source      几何体wkt格式(不能为空)
     * @param rtnType     返回格式（默认为xml）rtnType=json rtnType=xml
     * @return
     */
    @RequestMapping("/intersects")
    @ResponseBody
    public String intersects(String destination, String source, String rtnType) {
        try {
            JSONObject object = new JSONObject();
            String rtnString = agSpatialUtilsService.stIntersects(destination, source).toString();
            if (rtnType != null && "json".equals(rtnType)) {
                object.put("isIntersects", rtnString);
                return object.toString();
            }
            Document doc = ElementUtil.getRestFunctionXML(rtnString, "topology", "intersects", "当两个几何相交的时候返回true");
            return doc.asXML();
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    @RequestMapping("/intersectsPost")
    @ResponseBody
    public String intersectsPost(HttpServletRequest request) {
        String destination = request.getParameter("destination");
        String source = request.getParameter("source");
        String rtnType = request.getParameter("rtnType");
        return this.intersects(destination, source, rtnType);
    }

    /**
     * 如果两个几何的交集生成空集，则返回true
     *
     * @param destination 几何体wkt格式(不能为空)
     * @param source      几何体wkt格式(不能为空)
     * @param rtnType     返回格式（默认为xml）rtnType=json rtnType=xml
     * @return
     */
    @RequestMapping("/disjoint")
    @ResponseBody
    public String disjoint(String destination, String source, String rtnType) {
        try {
            JSONObject object = new JSONObject();
            String rtnString = agSpatialUtilsService.stDisjoint(destination, source).toString();
            if (rtnType != null && "json".equals(rtnType)) {
                object.put("isDisjoint", rtnString);
                return object.toString();
            }
            Document doc = ElementUtil.getRestFunctionXML(rtnString, "topology", "disjoint", "如果两个几何的交集生成空集，则返回true");
            return doc.asXML();
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    @RequestMapping("/disjointPost")
    @ResponseBody
    public String disjointPost(HttpServletRequest request) {
        String destination = request.getParameter("destination");
        String source = request.getParameter("source");
        String rtnType = request.getParameter("rtnType");
        return this.disjoint(destination, source, rtnType);
    }

    /**
     * 如果这两个几何完全相同，则返回true
     *
     * @param destination 几何体wkt格式(不能为空)
     * @param source      几何体wkt格式(不能为空)
     * @param rtnType     返回格式（默认为xml）rtnType=json rtnType=xml
     * @return
     */
    @RequestMapping("/equals")
    @ResponseBody
    public String equals(String destination, String source, String rtnType) {
        try {
            JSONObject object = new JSONObject();
            String rtnString = agSpatialUtilsService.stEquals(destination, source).toString();
            if (rtnType != null && "json".equals(rtnType)) {
                object.put("isEquals", rtnString);
                return object.toString();
            }
            Document doc = ElementUtil.getRestFunctionXML(rtnString, "topology", "equals", "如果这两个几何完全相同，则返回true");
            return doc.asXML();
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    @RequestMapping("/equalsPost")
    @ResponseBody
    public String equalsPost(HttpServletRequest request) {
        String destination = request.getParameter("destination");
        String source = request.getParameter("source");
        String rtnType = request.getParameter("rtnType");
        return this.equals(destination, source, rtnType);
    }

    /**
     * 求几何的凸包
     *
     * @param source  几何体wkt格式(不能为空)
     * @param rtnType 返回格式（默认为xml）rtnType=json rtnType=xml
     * @return
     */
    @RequestMapping("/convexHull")
    @ResponseBody
    public String convexHull(String source, String rtnType) {
        try {
            JSONObject object = new JSONObject();
            String rtnString = agSpatialUtilsService.stConvexHull(source);
            if (rtnType != null && "json".equals(rtnType)) {
                object.put("wkt", rtnString);
                return object.toString();
            }
            Document doc = ElementUtil.getRestFunctionXML(rtnString, "topology", "convexHull", "求几何的凸包");
            return doc.asXML();
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    @RequestMapping("/convexHullPost")
    @ResponseBody
    public String convexHullPost(HttpServletRequest request) {
        String source = request.getParameter("source");
        String rtnType = request.getParameter("rtnType");
        return this.convexHull(source, rtnType);
    }

    /**
     * 求几何的边界
     *
     * @param source  几何体wkt格式(不能为空)
     * @param rtnType 返回格式（默认为xml）rtnType=json rtnType=xml
     * @return
     */
    @RequestMapping("/boundary")
    @ResponseBody
    public String boundary(String source, String rtnType) {
        try {
            JSONObject object = new JSONObject();
            String rtnString = agSpatialUtilsService.stBoundary(source);
            if (rtnType != null && "json".equals(rtnType)) {
                object.put("wkt", rtnString);
                return object.toString();
            }
            Document doc = ElementUtil.getRestFunctionXML(rtnString, "topology", "boundary", "求几何的边界");
            return doc.asXML();
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    @RequestMapping("/boundaryPost")
    @ResponseBody
    public String boundaryPost(HttpServletRequest request) {
        String source = request.getParameter("source");
        String rtnType = request.getParameter("rtnType");
        return this.boundary(source, rtnType);
    }

    /**
     * 求几何的形心
     *
     * @param source  几何体wkt格式(不能为空)
     * @param rtnType 返回格式（默认为xml）rtnType=json rtnType=xml
     * @return
     */
    @RequestMapping("/centroid")
    @ResponseBody
    public String centroid(String source, String rtnType) {
        try {
            JSONObject object = new JSONObject();
            String rtnString = agSpatialUtilsService.stCentroid(source);
            if (rtnType != null && "json".equals(rtnType)) {
                object.put("wkt", rtnString);
                return object.toString();
            }
            Document doc = ElementUtil.getRestFunctionXML(rtnString, "topology", "centroid", "求几何的形心");
            return doc.asXML();
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    @RequestMapping("/centroidPost")
    @ResponseBody
    public String centroidPost(HttpServletRequest request) {
        String source = request.getParameter("source");
        String rtnType = request.getParameter("rtnType");
        return this.centroid(source, rtnType);
    }

    /**
     * 根据几何对象和距离，求围绕源对象的缓冲区的几何对象
     *
     * @param source   几何体wkt格式(不能为空)
     * @param distance 缓冲距离
     * @param rtnType  返回格式（默认为xml）rtnType=json rtnType=xml
     * @return
     */
    @RequestMapping("/buffer")
    @ResponseBody
    public String buffer(String source, String distance, String rtnType) {
        try {
            JSONObject object = new JSONObject();
            String rtnString = agSpatialUtilsService.stBuffer(source, distance).toString();
            if (rtnType != null && "json".equals(rtnType)) {
                object.put("wkt", rtnString);
                return object.toString();
            }
            Document doc = ElementUtil.getRestFunctionXML(rtnString, "topology", "buffer", "根据几何对象和距离，求围绕源对象的缓冲区的几何对象");
            return doc.asXML();
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    @RequestMapping("/bufferPost")
    @ResponseBody
    public String bufferPost(HttpServletRequest request) {
        String distance = request.getParameter("distance");
        String source = request.getParameter("source");
        String rtnType = request.getParameter("rtnType");
        return this.buffer(source, distance, rtnType);
    }

    /**
     * 如果两个几何相交的部分都不在两个几何的内部，则返回true
     *
     * @param destination 几何体wkt格式(不能为空)
     * @param source      几何体wkt格式(不能为空)
     * @param rtnType     返回格式（默认为xml）rtnType=json rtnType=xml
     * @return
     */
    @RequestMapping("/touches")
    @ResponseBody
    public String touches(String destination, String source, String rtnType) {
        try {
            JSONObject object = new JSONObject();
            String rtnString = agSpatialUtilsService.stTouches(destination, source).toString();
            if (rtnType != null && "json".equals(rtnType)) {
                object.put("isTouches", rtnString);
                return object.toString();
            }
            Document doc = ElementUtil.getRestFunctionXML(rtnString, "topology", "touches", "如果两个几何相交的部分都不在两个几何的内部，则返回true");
            return doc.asXML();
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    @RequestMapping("/touchesPost")
    @ResponseBody
    public String touchesPost(HttpServletRequest request) {
        String distance = request.getParameter("distance");
        String source = request.getParameter("source");
        String rtnType = request.getParameter("rtnType");
        return this.touches(source, distance, rtnType);
    }

    /**
     * 求2个几何相交的部分
     *
     * @param destination 几何体wkt格式(不能为空)
     * @param source      几何体wkt格式(不能为空)
     * @param rtnType     返回格式（默认为xml）rtnType=json rtnType=xml
     * @return
     */
    @RequestMapping("/intersection")
    @ResponseBody
    public String intersection(String destination, String source, String rtnType) {
        //解决自相交的问题
        String geom1 = agSpatialUtilsService.buffer0(destination);
        try {
            JSONObject object = new JSONObject();
            String rtnString = agSpatialUtilsService.stIntersection(destination, source);
            if (rtnType != null && "json".equals(rtnType)) {
                object.put("wkt", rtnString);
                return object.toString();
            }
            Document doc = ElementUtil.getRestFunctionXML(rtnString, "topology", "intersection", "求2个几何相交的部分");
            return doc.asXML();
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    @RequestMapping("/intersectionPost")
    @ResponseBody
    public String intersectionPost(HttpServletRequest request) {
        String distance = request.getParameter("distance");
        String source = request.getParameter("source");
        String rtnType = request.getParameter("rtnType");
        return this.intersection(source, distance, rtnType);
    }

    /**
     * 求2个几何不相交的部分
     *
     * @param destination 几何体wkt格式(不能为空)
     * @param source      几何体wkt格式(不能为空)
     * @param rtnType     返回格式（默认为xml）rtnType=json rtnType=xml
     * @return
     */
    @RequestMapping("/symmetricDiff")
    @ResponseBody
    public String symmetricDiff(String destination, String source, String rtnType) {
        try {
            JSONObject object = new JSONObject();
            String rtnString = agSpatialUtilsService.stSymmetricDiff(destination, source);
            if (rtnType != null && "json".equals(rtnType)) {
                object.put("wkt", rtnString);
                return object.toString();
            }
            Document doc = ElementUtil.getRestFunctionXML(rtnString, "topology", "symmetricDiff", "求2个几何不相交的部分");
            return doc.asXML();
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    @RequestMapping("/symmetricDiffPost")
    @ResponseBody
    public String symmetricDiffPost(HttpServletRequest request) {
        String distance = request.getParameter("distance");
        String source = request.getParameter("source");
        String rtnType = request.getParameter("rtnType");
        return this.symmetricDiff(source, distance, rtnType);
    }

    /**
     * 求2个几何的并集
     *
     * @param destination 几何体wkt格式(不能为空)
     * @param source      几何体wkt格式(不能为空)
     * @param rtnType     返回格式（默认为xml）rtnType=json rtnType=xml
     * @return
     */
    @RequestMapping("/union")
    @ResponseBody
    public String union(String destination, String source, String rtnType) {
        try {
            JSONObject object = new JSONObject();
            String rtnString = agSpatialUtilsService.stUnion(destination, source);
            if (rtnType != null && "json".equals(rtnType)) {
                object.put("wkt", rtnString);
                return object.toString();
            }
            Document doc = ElementUtil.getRestFunctionXML(rtnString, "topology", "union", "求2个几何的并集");
            return doc.asXML();
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    @RequestMapping("/unionPost")
    @ResponseBody
    public String unionPost(HttpServletRequest request) {
        String distance = request.getParameter("distance");
        String source = request.getParameter("source");
        String rtnType = request.getParameter("rtnType");
        return this.union(source, distance, rtnType);
    }

    /**
     * 求2个几何的最短距离
     *
     * @param destination 几何体wkt格式(不能为空)
     * @param source      几何体wkt格式(不能为空)
     * @param rtnType     返回格式（默认为xml）rtnType=json rtnType=xml
     * @return
     */
    @RequestMapping("/distance")
    @ResponseBody
    public String distance(String destination, String source, String rtnType) {
        try {
            JSONObject object = new JSONObject();
            String rtnString = agSpatialUtilsService.stDistance(destination, source);
            if (rtnType != null && "json".equals(rtnType)) {
                object.put("distance", rtnString);
                return object.toString();
            }
            Document doc = ElementUtil.getRestFunctionXML(rtnString, "topology", "distance", "求2个几何的最短距离");
            return doc.asXML();
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    @RequestMapping("/distancePost")
    @ResponseBody
    public String distancePost(HttpServletRequest request) {
        String destination = request.getParameter("destination");
        String source = request.getParameter("source");
        String rtnType = request.getParameter("rtnType");
        return this.distance(destination, source, rtnType);
    }

    /**
     * 求几何体的周长
     *
     * @param rtnType 返回格式（默认为xml）rtnType=json rtnType=xml
     * @return
     */
    @RequestMapping("/length")
    @ResponseBody
    public String length(String source, String rtnType) {
        try {
            JSONObject object = new JSONObject();
            String rtnString = agSpatialUtilsService.stLength(source);
            if (rtnType != null && "json".equals(rtnType)) {
                object.put("length", rtnString);
                return object.toString();
            }
            Document doc = ElementUtil.getRestFunctionXML(rtnString, "topology", "length", "求几何体的周长");
            return doc.asXML();
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    @RequestMapping("/lengthPost")
    @ResponseBody
    public String lengthPost(HttpServletRequest request) {
        String source = request.getParameter("source");
        String rtnType = request.getParameter("rtnType");
        return this.length(source, rtnType);
    }

    /**
     * 求几何体的面积
     *
     * @param rtnType 返回格式（默认为xml）rtnType=json rtnType=xml
     * @return
     */
    @RequestMapping("/area")
    @ResponseBody
    public String area(String source, String rtnType) {
        try {
            JSONObject object = new JSONObject();
            String rtnString = agSpatialUtilsService.stArea(source);
            if (rtnType != null && "json".equals(rtnType)) {
                object.put("area", rtnString);
                return object.toString();
            }
            Document doc = ElementUtil.getRestFunctionXML(rtnString, "topology", "area", "求几何体的面积");
            return doc.asXML();
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    @RequestMapping("/areaPost")
    @ResponseBody
    public String areaPost(HttpServletRequest request) {
        String source = request.getParameter("source");
        String rtnType = request.getParameter("rtnType");
        return this.area(source, rtnType);
    }

    /**
     * 将一种地图投影点的坐标变换为另一种地图投影点的坐标
     */
    @RequestMapping("/project")
    @ResponseBody
    public String project(HttpServletRequest request) {
        try {
            Map<String, String> params = RequestUtil.getMapByRequest(request);
            HttpRequester geometryRequest = new HttpRequester();
            String url = geometryURL + "/project";
            HttpRespons response = geometryRequest.sendPost(url, params, null);
            String content = response.getContent();
            content = content.replaceAll("(\\s*\r?\n)+", "\r\n");//去掉文本里面的空行
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    /**
     * 计算几何对象的标注点
     */
    @RequestMapping("/labelPoints")
    @ResponseBody
    public String labelPoints(HttpServletRequest request) {
        try {
            Map<String, String> params = RequestUtil.getMapByRequest(request);
            HttpRequester geometryRequest = new HttpRequester();
            String url = geometryURL + "/labelPoints";
            HttpRespons response = geometryRequest.sendPost(url, params, null);
            String content = response.getContent();
            content = content.replaceAll("(\\s*\r?\n)+", "\r\n");//去掉文本里面的空行
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    /**
     * 多要素相交分析服务
     */
    @RequestMapping("/mIntersect")
    @ResponseBody
    public String intersect(HttpServletRequest request) {
        try {
            Map<String, String> params = RequestUtil.getMapByRequest(request);
            HttpRequester geometryRequest = new HttpRequester();
            String url = geometryURL + "/intersect";
            HttpRespons response = geometryRequest.sendPost(url, params, null);
            String content = response.getContent();
            content = content.replaceAll("(\\s*\r?\n)+", "\r\n");//去掉文本里面的空行
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    /**
     * 多要素相差分析服务
     */
    @RequestMapping("/mDifference")
    @ResponseBody
    public String difference(HttpServletRequest request) {
        try {
            Map<String, String> params = RequestUtil.getMapByRequest(request);
            HttpRequester geometryRequest = new HttpRequester();
            String url = geometryURL + "/difference";
            HttpRespons response = geometryRequest.sendPost(url, params, null);
            String content = response.getContent();
            content = content.replaceAll("(\\s*\r?\n)+", "\r\n");//去掉文本里面的空行
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    /**
     * 多要素并分析服务
     */
    @RequestMapping("/mUnion")
    @ResponseBody
    public String union(HttpServletRequest request) {
        try {
            Map<String, String> params = RequestUtil.getMapByRequest(request);
            HttpRequester geometryRequest = new HttpRequester();
            String url = geometryURL + "/union";
            HttpRespons response = geometryRequest.sendPost(url, params, null);
            String content = response.getContent();
            content = content.replaceAll("(\\s*\r?\n)+", "\r\n");//去掉文本里面的空行
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return "{error:" + e.getMessage() + "}";
        }
    }

    /**
     * 获取元数据描述信息
     * @return
     */
/*	@GET
    @Path("getMetaData")
	@Produces(MediaType.TEXT_XML)
	public String getMetaData(@Context HttpServletRequest request){
		Document doc = DocumentHelper.createDocument();
		String serverName = "topology";
		String rtnString = "xml/json";
		Element methods = ElementUtil.getMetaDataHead(doc,serverName,"综合展示平台提供的外部空间拓扑接口");
		
		Element f1 = ElementUtil.getMetaDataFunction(methods, "contains", "当第二个几何完全被第一个几何包含的时候返回true", rtnString, request ,serverName);
		ElementUtil.getMetaDataParam(f1,"destination",String.class.toString(),"目标几何体wkt格式(不能为空)");
		ElementUtil.getMetaDataParam(f1,"source",String.class.toString(),"源几何体wkt格式(不能为空)");
		ElementUtil.getMetaDataParam(f1,"f",String.class.toString(),"数据返回格式（f=json）,默认不传递则返回xml");
		
		Element f2 = ElementUtil.getMetaDataFunction(methods, "intersects", "当两个几何相交的时候返回true", rtnString, request ,serverName);
		ElementUtil.getMetaDataParam(f2,"destination",String.class.toString(),"目标几何体wkt格式(不能为空)");
		ElementUtil.getMetaDataParam(f2,"source",String.class.toString(),"源几何体wkt格式(不能为空)");
		ElementUtil.getMetaDataParam(f2,"f",String.class.toString(),"数据返回格式（f=json）,默认不传递则返回xml");
		
		Element f3 = ElementUtil.getMetaDataFunction(methods, "touches", "如果两个几何相交的部分都不在两个几何的内部，则返回true", rtnString, request ,serverName);
		ElementUtil.getMetaDataParam(f3,"destination",String.class.toString(),"目标几何体wkt格式(不能为空)");
		ElementUtil.getMetaDataParam(f3,"source",String.class.toString(),"源几何体wkt格式(不能为空)");
		ElementUtil.getMetaDataParam(f3,"f",String.class.toString(),"数据返回格式（f=json）,默认不传递则返回xml");
		
		Element f4 = ElementUtil.getMetaDataFunction(methods, "intersection", "求2个几何相交的部分", rtnString, request ,serverName);
		ElementUtil.getMetaDataParam(f4,"destination",String.class.toString(),"目标几何体wkt格式(不能为空)");
		ElementUtil.getMetaDataParam(f4,"source",String.class.toString(),"源几何体wkt格式(不能为空)");
		ElementUtil.getMetaDataParam(f4,"f",String.class.toString(),"数据返回格式（f=json）,默认不传递则返回xml");
		
		Element f5 = ElementUtil.getMetaDataFunction(methods, "symmetricDiff", "求2个几何不相交的部分", rtnString, request ,serverName);
		ElementUtil.getMetaDataParam(f5,"destination",String.class.toString(),"目标几何体wkt格式(不能为空)");
		ElementUtil.getMetaDataParam(f5,"source",String.class.toString(),"源几何体wkt格式(不能为空)");
		ElementUtil.getMetaDataParam(f5,"f",String.class.toString(),"数据返回格式（默认为xml）f=json f=xml");
		
		Element f6 = ElementUtil.getMetaDataFunction(methods, "union", "求2个几何的并集", rtnString, request ,serverName);
		ElementUtil.getMetaDataParam(f6,"destination",String.class.toString(),"目标几何体wkt格式(不能为空)");
		ElementUtil.getMetaDataParam(f6,"source",String.class.toString(),"源几何体wkt格式(不能为空)");
		ElementUtil.getMetaDataParam(f6,"f",String.class.toString(),"数据返回格式（默认为xml）f=json f=xml");
		
		Element f7 = ElementUtil.getMetaDataFunction(methods, "distance", "求2个几何的最短距离", rtnString, request ,serverName);
		ElementUtil.getMetaDataParam(f7,"destination",String.class.toString(),"目标几何体wkt格式(不能为空)");
		ElementUtil.getMetaDataParam(f7,"source",String.class.toString(),"源几何体wkt格式(不能为空)");
		ElementUtil.getMetaDataParam(f7,"f",String.class.toString(),"数据返回格式（默认为xml）f=json f=xml");
		
		Element f8 = ElementUtil.getMetaDataFunction(methods, "length", "求几何体的周长", rtnString, request ,serverName);
		ElementUtil.getMetaDataParam(f8,"wkt",String.class.toString(),"几何体wkt格式(不能为空)");
		ElementUtil.getMetaDataParam(f8,"f",String.class.toString(),"数据返回格式（默认为xml）f=json f=xml");
		
		Element f9 = ElementUtil.getMetaDataFunction(methods, "area", "求几何体的面积", rtnString, request ,serverName);
		ElementUtil.getMetaDataParam(f9,"wkt",String.class.toString(),"几何体wkt格式(不能为空)");
		ElementUtil.getMetaDataParam(f9,"f",String.class.toString(),"数据返回格式（默认为xml）f=json f=xml");
		
		return doc.asXML();
	}*/
}