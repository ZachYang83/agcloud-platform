package com.augurit.agcloud.agcom.agsupport.sc.coordTrans.controller;

import com.augurit.agcloud.agcom.agsupport.common.util.UploadUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import com.augurit.agcloud.agcom.agsupport.sc.coordTrans.util.CoordTransUtil;
import com.augurit.agcloud.agcom.agsupport.sc.dic.service.IAgDic;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.SpatialUtil;
import com.augurit.agcloud.agcom.agsupport.util.ShapeFileUtils;
import com.augurit.agcloud.agcom.agsupport.util.ZipFileUtils;
import com.augurit.agcloud.bsc.upload.MongoDbAchieve;
import com.augurit.agcloud.bsc.upload.UploadFileStrategy;
import com.augurit.agcloud.bsc.upload.UploadType;
import com.augurit.agcloud.bsc.upload.factory.UploaderFactory;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.common.thread.Executer;
import com.common.util.Common;
import com.common.util.FileUtils;
import com.coordtrans.bean.BaseCoordinate;
import com.coordtrans.bean.CoincidentPoint;
import com.coordtrans.bean.ParameterFour;
import com.coordtrans.bean.ParameterSeven;
import com.coordtrans.service.impl.CoordTransAppImpl;
import com.coordtrans.service.impl.CoordTransAppMoreImpl;
import com.coordtrans.service.impl.CoordTransBaseImpl;
import com.coordtrans.web.CoordTransAppAction;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by Administrator on 2017-05-15.
 */
@Api(value = "坐标转换",description = "坐标转换相关接口")
@RestController
@RequestMapping("/agsupport/coordTrans")
public class AgCoordTransController {
    private int ProjNo = 38;
    @Value("${agcloud.opus.admin.orgId}")
    private String orgId;
    @Autowired
    public IAgDic IAgDic;
    private com.coordtrans.service.ICoordTransApp ICoordTransApp = new CoordTransAppImpl();
    private com.coordtrans.service.ICoordTransBase ICoordTransBase = new CoordTransBaseImpl();
    private com.coordtrans.service.ICoordTransAppMore ICoordTransAppMore = new CoordTransAppMoreImpl();

    @RequestMapping("/index.html")
    public ModelAndView index(HttpServletRequest request, Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/coordTrans/index");
    }

    /**
     * 坐标转换
     */
    @ApiOperation(value = "通过XIAN80转WGS84",notes = "通过XIAN80转WGS84接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x",value = "XIAN80坐标系下的x坐标",required = true),
            @ApiImplicitParam(name = "y",value = "XIAN80坐标系下的y坐标，例如：" +
                    "参数：x=2550626.144；y=425051.515",required = true),
    })
    @RequestMapping(value = "/getWGS84ByXiAn80",method = RequestMethod.POST)
    public ContentResultForm getWGS84ByXiAn80(String x, String y) {
        BaseCoordinate coor_80 = new BaseCoordinate(Double.parseDouble(x), Double.parseDouble(y));
        String p7 = "8.975541479885578,-21.010086234658957,-9.568853195756674,1.0000034156441904,-2.7047560862136777E-6,6.55955117534468E-6,-1.6930718297558656E-5";
        ParameterSeven parameterSeven = new ParameterSeven(p7.split(","));
        BaseCoordinate coor_84 = ICoordTransApp.CS_1ToCS_2(coor_80, parameterSeven, 114, 114, "80-84");
        String js = "{\"x\":\"" + CoordTransUtil.round(coor_84.getX(), 6) + "\",\"y\":\"" + CoordTransUtil.round(coor_84.getY(), 6) + "\"}";
        return new ContentResultForm<String>(true,js);
    }

    @ApiOperation(value = "百度坐标转WGS84",notes = "百度坐标转WGS84接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "xMax",value = "x坐标最大值"),
            @ApiImplicitParam(name = "post",value = "以JSONArray格式存储的x和y坐标JSONObject,例如:" +
                    "参数：xMax=2550626.144\n" +
                    "post=[{\"x\":2500626.144,\"y\":425051.515}]" +
                    "参数：x=2550626.144；y=425051.515"),
    })
    @RequestMapping(value = "/getByBaiduList",method = RequestMethod.POST)
    public ContentResultForm getByBaiduList(String xMax,String post,HttpServletRequest request) {
        double xMAX = Double.valueOf(xMax);
        if (post.equals("")) {
            post = "{}";
        }
        try {
            post = URLDecoder.decode(post, "UTF-8");
        } catch (Exception e) {

        }
        JSONArray pointList84 = JSONArray.fromObject(post);
        JSONArray pointList80 = new JSONArray();
        for (int i = 0; i < pointList84.size(); i++) {
            JSONObject pointJSON = JSONObject.fromObject(pointList84.get(i));
            if (pointJSON != null) {
                double x = Double.parseDouble(pointJSON.get("x").toString());
                double y = Double.parseDouble(pointJSON.get("y").toString());
                BaseCoordinate coor_84 = CoordTransUtil.bd2wgs(new BaseCoordinate(x, y));
                String js = "";
                if (xMAX > 180) {
                    String p7 = "-8.974272914230824,21.00936523079872,9.568448803387582,0.9999965842617238,2.704787235519035E-6,-6.559479342804764E-6,1.6930607218412774E-5";
                    ParameterSeven parameterSeven = new ParameterSeven(p7.split(","));
                    BaseCoordinate coor_80 = ICoordTransApp.CS_1ToCS_2(coor_84, parameterSeven, 114, 114, "84-80");
                    //加上带号38
                    ICoordTransBase.addProjNo(coor_80, ProjNo);
                    js = "{'x':'" + CoordTransUtil.round(coor_80.getX(), 6) + "','y':'" + CoordTransUtil.round(coor_80.getY(), 6) + "'}";
                } else {
                    js = "{'x':'" + CoordTransUtil.round(coor_84.getX(), 6) + "','y':'" + CoordTransUtil.round(coor_84.getY(), 6) + "'}";
                }
                pointList80.add(js);
            }
        }
        return new ContentResultForm<String>(true,pointList80.toString());
    }

    @ApiOperation(value = "计算坐标参数",notes = "计算坐标参数接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "json",value = "包含相关参数的json格式的数据，例如：\n" +
                    "{\"type\":\"80-84\",\"coincidentPoint\":\"38440020.61,2523155.572 113.416944,22.805833\\n38436094.89,2518772.439 …\",\"L1\":\"114\",\"L2\":\"114\"}",required = true)
    })
    @RequestMapping(value = "/CalcuteParamServlet",method = RequestMethod.POST)
    public ContentResultForm CalcuteParamServlet(HttpServletRequest request, String json) {
//        response.setContentType("text/html; charset=UTF-8");
//        response.setDateHeader("Expires", 1L);
//        response.addHeader("Pragma", "no-cache");
//        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
//        PrintWriter pw = null;
        String js = "";

        try {
            String post = URLDecoder.decode(json, "utf-8");
            if (post == null || "".equals(post)) {
                post = "[{}]";
            }

            JSONObject postJson = JSONObject.fromObject(post);
            String type = postJson.getString("type");
            String coincidentPointStr = postJson.getString("coincidentPoint");
            double L1 = Double.parseDouble(postJson.getString("L1"));
            double L2 = Double.parseDouble(postJson.getString("L2"));
            List<CoincidentPoint> coincidentPoint = new ArrayList<CoincidentPoint>();
            String[] coincidentPointStrs = coincidentPointStr.split("\n");
            for (int i = 0; i < coincidentPointStrs.length; i++) {
                String point = coincidentPointStrs[i];
                if (!"".equals(point) && null != point) {
                    BaseCoordinate coor_1 = new BaseCoordinate(point.split(" ")[0].split(","));
                    BaseCoordinate coor_2 = new BaseCoordinate(point.split(" ")[1].split(","));
                    coincidentPoint.add(new CoincidentPoint(coor_1, coor_2));
                }
            }
            if ("4param".equals(type)) {
                ParameterFour parameter = ICoordTransApp.CalcuteParameterFour(coincidentPoint);
//                js = "X:  " + parameter.getDX() + "\r\nY:  " + parameter.getDY() + "\r\na:  " + parameter.getA() + "\r\nb:  " + parameter.getB() + "\r\n\r\n";
//                js = js + "可直接复制下面参数：\r\n\r\n";
                js = js + parameter.getDX() + "," + parameter.getDY() + "," + parameter.getA() + "," + parameter.getB();
            } else {
                ParameterSeven parameter = ICoordTransApp.CalcuteParameterSeven(coincidentPoint, L1, L2, type);
//                js = "X:  " + parameter.getDX() + "\r\nY:  " + parameter.getDY() + "\r\nZ:  " + parameter.getDZ() + "\r\nM:  " + parameter.getM() + "\r\nWx:  " + parameter.getWX() + "\r\nWy:  " + parameter.getWY() + "\r\nWz:  " + parameter.getWZ() + "\r\n\r\n";
//                js = js + "可直接复制下面参数：\r\n\r\n";
                js = js + parameter.getDX() + "," + parameter.getDY() + "," + parameter.getDZ() + "," + parameter.getM() + "," + parameter.getWX() + "," + parameter.getWY() + "," + parameter.getWZ();
            }
        } catch (Exception var19) {
            var19.printStackTrace();
        }
        return new ContentResultForm<String>(true,js);
    }

    @ApiOperation(value = "通过坐标参数进行坐标转换",notes = "通过坐标参数进行坐标转换接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "json",value = "例如： " +
                    "参数：{\"coor\":\"38436213.83,2540244.805\",\"type\":\"80-84\",\"param\":\"8.814299389719963,-20.63454755395651,-9.398727240972221,1.0000033462452704,-2.575799865622841E-6,6.259805057862167E-6,-1.7068264344999307E-5\",\"L1\":\"114\",\"L2\":\"114\"}")
    })
    @RequestMapping(value = "/CoordTransByParamServlet",method = RequestMethod.POST)
    public ContentResultForm CoordTransByParamServlet(HttpServletRequest request, String json) {
//        response.setContentType("text/html; charset=UTF-8");
//        response.setDateHeader("Expires", 1L);
//        response.addHeader("Pragma", "no-cache");
//        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
//        PrintWriter pw = null;
        String js = "";
        try {
            String post = URLDecoder.decode(json, "utf-8");
            if (post == null || "".equals(post)) {
                post = "[{}]";
            }
            JSONObject postJson = JSONObject.fromObject(post);
            String coorStr = postJson.getString("coor");
            String type = postJson.getString("type");
            String param = postJson.getString("param");
            double L1 = Double.parseDouble(postJson.getString("L1"));
            double L2 = Double.parseDouble(postJson.getString("L2"));
            BaseCoordinate coor = new BaseCoordinate(coorStr.split(","));
            BaseCoordinate coor_ans = new BaseCoordinate();
            if ("4param".equals(type)) {
                ParameterFour parameter = new ParameterFour(param.split(","));
                coor_ans = ICoordTransApp.CS_1ToCS_2(coor, parameter);
            } else {
                ParameterSeven parameter = new ParameterSeven(param.split(","));
                coor_ans = ICoordTransApp.CS_1ToCS_2(coor, parameter, L1, L2, type);
            }
            js = CoordTransAppAction.doubleToString(coor_ans.getX()) + "," + CoordTransAppAction.doubleToString(coor_ans.getY()) + "," + CoordTransAppAction.doubleToString(coor_ans.getZ());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return new ContentResultForm<String>(true,js);
    }

    @ApiOperation(value = "通过系统系统提供的参数进行坐标转换",notes = "通过系统系统提供的参数进行坐标转换接口")
    @ApiImplicitParam(name = "json",value = "例如:  " +
            "{\"coor\":\"38436213.83,2540244.805\",\"type\":\"80-84\",\"L1\":\"114\",\"L2\":\"114\"}")
    @RequestMapping(value = "/CoordTransServlet",method = RequestMethod.POST)
    public ContentResultForm CoordTransServlet(HttpServletRequest request, String json) {
        String js = "";
        try {
            String post = URLDecoder.decode(json, "utf-8");
            if (post == null || "".equals(post)) {
                post = "[{}]";
            }
            JSONObject postJson = JSONObject.fromObject(post);
            String coorStr = postJson.getString("coor");
            String type = postJson.getString("type");
            AgDic params = IAgDic.getAgDicByTypeCodeAndItemName("B001", type);
            String param = params.getValue();
            //type = type.split("_")[0].substring(type.split("_")[0].length() - 2) + "-" + type.split("_")[1].substring(type.split("_")[1].length() - 2);
            double L1 = Double.parseDouble(postJson.getString("L1"));
            double L2 = Double.parseDouble(postJson.getString("L2"));
            BaseCoordinate coor = new BaseCoordinate(coorStr.split(","));
            BaseCoordinate coor_ans = new BaseCoordinate();
            if ("4param".equals(type)) {
                ParameterFour parameter = new ParameterFour(param.split(","));
                coor_ans = ICoordTransApp.CS_1ToCS_2(coor, parameter);
            } else {
                ParameterSeven parameter = new ParameterSeven(param.split(","));
                coor_ans = ICoordTransApp.CS_1ToCS_2(coor, parameter, L1, L2, type);
            }
            js = CoordTransAppAction.doubleToString(coor_ans.getX()) + "," + CoordTransAppAction.doubleToString(coor_ans.getY()) + "," + CoordTransAppAction.doubleToString(coor_ans.getZ());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return new ContentResultForm<String>(true,js);
    }

    @ApiOperation(value = "通过WKT进行坐标转换",notes = "通过WKT进行坐标转换接口")
    @ApiImplicitParam(name = "json",value = "例如：\n" +
            "{\"wkt_1\":\"POINT ( 38440020.61 2523155.572)\\n\",\"type\":\"54-80\",\"param\":\"8.814299389719963,-20.63454755395651,-9.398727240972221,1.0000033462452704,-2.575799865622841E-6,6.259805057862167E-6,-1.7068264344999307E-5\",\"L1\":\"114\",\"L2\":\"114\"}")
    @RequestMapping(value = "/WKTTransByParamServlet",method = RequestMethod.POST)
    public ContentResultForm WKTTransByParamServlet(HttpServletRequest request, String json) {
        String js = "";
        try {
            String post = URLDecoder.decode(json, "utf-8");
            if (post == null || "".equals(post)) {
                post = "[{}]";
            }
            JSONObject postJson = JSONObject.fromObject(post);
            String[] wkt = postJson.getString("wkt_1").split("\n");
            String type = postJson.getString("type");
            String param = postJson.getString("param");
            double L1 = Double.parseDouble(postJson.getString("L1"));
            double L2 = Double.parseDouble(postJson.getString("L2"));
            String wkt_2 = "";
            for (int i = 0; i < wkt.length; i++) {
                if ("4param".equals(type)) {
                    ParameterFour parameter = new ParameterFour(param.split(","));
                    wkt_2 += ICoordTransAppMore.CS_1ToCS_2(wkt[i], parameter) + "\r\n";
                } else {
                    ParameterSeven parameter = new ParameterSeven(param.split(","));
                    wkt_2 += ICoordTransAppMore.CS_1ToCS_2(wkt[i], parameter, L1, L2, type) + "\r\n";
                }
            }
            js = wkt_2;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return new ContentResultForm<String>(true,js);
    }
    @ApiOperation(value = "根据系统系统提供的参数，对传入坐标进行WKT转换。",notes = "该接口用于根据系统系统提供的参数，对传入坐标进行WKT转换接口")
    @ApiImplicitParam(name = "json",value = "例如： " +
            "{\"wkt_1\":\"POINT ( 38440020.61 2523155.572)\",\"type\":\"80-84\",\"L1\":\"114\",\"L2\":\"114\"}")
    @RequestMapping(value = "/WKTTransServlet",method = RequestMethod.POST)
    public ContentResultForm WKTTransServlet(HttpServletRequest request, String json) {
        String js = "";
        try {
            String post = URLDecoder.decode(json, "utf-8");
            if (post == null || "".equals(post)) {
                post = "[{}]";
            }
            JSONObject postJson = JSONObject.fromObject(post);
            String[] wkt = postJson.getString("wkt_1").split("\n");
            String type = postJson.getString("type");
            AgDic params = IAgDic.getAgDicByTypeCodeAndItemName("B001", type);
            String param = params.getValue();
            //type = type.split("_")[0].substring(type.split("_")[0].length() - 2) + "-" + type.split("_")[1].substring(type.split("_")[1].length() - 2);
            double L1 = Double.parseDouble(postJson.getString("L1"));
            double L2 = Double.parseDouble(postJson.getString("L2"));
            String wkt_2 = "";
            for (int i = 0; i < wkt.length; i++) {
                if ("4param".equals(type)) {
                    wkt_2 += ICoordTransAppMore.CS_1ToCS_2(wkt[i]) + "\r\n";
                } else {
                    ParameterSeven parameter = new ParameterSeven(param.split(","));
                    wkt_2 += ICoordTransAppMore.CS_1ToCS_2(wkt[i], parameter, L1, L2, type) + "\r\n";
                }
            }
            js = wkt_2;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return new ContentResultForm<String>(true,js);
    }
    @ApiOperation(value = "根据用户传入的两个坐标系，计算两个坐标系之间的误差。",notes = "该接口用于根据用户传入的两个坐标系，计算两个坐标系之间的误差的接口")
    @ApiImplicitParam(name = "json",value = "例如： " +
            "{coor_1:\"38436271.87876486,2540303.6948415893,0.0002789907\",coor_2: \"113.379167,22.96\"}")
    @RequestMapping(value = "/AnalyErrorServlet",method = RequestMethod.POST)
    public ContentResultForm AnalyErrorServlet(HttpServletRequest request, String json) {
        String js = "";
        try {
            String post = URLDecoder.decode(json, "utf-8");
            if (post == null || "".equals(post)) {
                post = "[{}]";
            }
            JSONObject postJson = JSONObject.fromObject(post);
            String coor_1 = postJson.getString("coor_1");
            String coor_2 = postJson.getString("coor_2");
            BaseCoordinate c_1 = new BaseCoordinate(coor_1.split(","));
            BaseCoordinate c_2 = new BaseCoordinate(coor_2.split(","));
            double e = ICoordTransApp.CalcuteError(c_1, c_2);
            js = CoordTransAppAction.doubleToString(e);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return new ContentResultForm<String>(true,js);
    }
    @ApiOperation(value = "根据用户传入JSON，转化为WKT字符串格式。",notes = "根据用户传入JSON，转化为WKT字符串格式接口")
    @ApiImplicitParam(name = "json",value = "例如：" +
            "json= {\"type\":\"Point\",\"coordinates\":[6,10]}")
    @RequestMapping(value = "/jsonToWkt",method = RequestMethod.POST)
    public ContentResultForm jsonToWkt(String json,HttpServletRequest request) {
        if (Common.isCheckNull(json)) return null;
        return new ContentResultForm<String>(true,SpatialUtil.jsonToWkt(json));
    }

    @ApiOperation(value = "根据用户传入的WGS84下WKT格式字符串转广州番禺坐标系。",notes = "根据用户传入的WGS84下WKT格式字符串转广州番禺坐标系接口")
    @ApiImplicitParam(name = "wkt",value = "例如： " +
            "wkt= POINT ( 38440020.61 2523155.572)")
    @RequestMapping(value = "/WKTTransWGS84ToGZPY",method = RequestMethod.POST)
    public ContentResultForm wktTransWGS84ToGZPY(HttpServletRequest request) {
        String wkt_wgs = Common.checkNull(request.getParameter("wkt"));
        String wkt_gz = "";
        if (!wkt_wgs.equals("")) {
            String p7_py = "-8.261292181909084,19.382795698940754,8.830387133639306,0.9999968859126824,2.6568299220652847E-6,-6.44476627065238E-6,1.6981431304685657E-5";
            ParameterSeven p7 = new ParameterSeven(p7_py.split(","));
            String wkt_xian = ICoordTransAppMore.CS_1ToCS_2(wkt_wgs, p7, 114, 114, "84-80");
            String p4_py = "-374202.6173505783,-2531705.4465705156,0.999944877924861,0.004794726949512551";
            ParameterFour p4 = new ParameterFour(p4_py.split(","));
            wkt_gz = ICoordTransAppMore.CS_1ToCS_2(wkt_xian, p4);
        }
        return new ContentResultForm<String>(true,SpatialUtil.jsonToWkt(wkt_gz));
    }

    @ApiOperation(value = "WKT数据转GeoJson数据",notes = "WKT数据转GeoJson数据接口")
    @ApiImplicitParam(name = "wkt")
    @RequestMapping(value = "/wktToGeoJson",method = RequestMethod.POST)
    public ContentResultForm wktToGeoJson(String wkt) {
        return new ContentResultForm<String>(true,SpatialUtil.wktToJson(wkt));
    }

    @ApiOperation(value = "GeoJson数据转WKT数据",notes = "GeoJson数据转WKT数据接口")
    @ApiImplicitParam(name = "geoJson")
    @RequestMapping(value = "/GeoJsonToWkt",method = RequestMethod.POST)
    public ContentResultForm GeoJsonToWkt(String geoJson) {
        return new ContentResultForm<String>(true,SpatialUtil.jsonToWkt(geoJson));
    }

    @Autowired
    private UploaderFactory uploaderFactory;
    /**
     * 上传文件接口
     * @param request
     * @return
     */
    @ApiOperation(value = "上传Shape坐标文件",notes = "上传Shape坐标文件接口")
    @ApiImplicitParam(value = "*说明：请保证上传文件格式正确： 1、上传的压缩文件格式为zip    2、shape格式文件最少包含3个文件，他们的后缀是：.shp, .dbf, .shx。")
    @RequestMapping(value = "/uploadShapeFile",method = RequestMethod.POST)
    public ContentResultForm uploadShapeFile(HttpServletRequest request) {
        String contentType = request.getContentType();
        ContentResultForm result = new ContentResultForm(true, "上传文件成功");

        if (contentType != null && (contentType.toLowerCase().startsWith("multipart/"))) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile shapeZip = multipartRequest.getFile("file");
            if (shapeZip != null && shapeZip.getSize() > 0) {
                String originalFilename = shapeZip.getOriginalFilename();
                try {
                    UploadFileStrategy uploadFileStrategy = uploaderFactory.create(UploadType.MONGODB.getValue());
                    MongoDbAchieve mongoDbAchieve = (MongoDbAchieve) uploadFileStrategy;
                    String id = mongoDbAchieve.uploadMultipartFile(shapeZip);
                    System.out.println(id);
                }catch (Exception e){
                    e.printStackTrace();
                }

                // 如果是上传压缩文件则处理
                /*if(originalFilename.matches(".*.zip")) {

                }*/
                // 上传文件的目录
                String shapePath = UploadUtil.getUploadShapePath(request);
                // 转换坐标之后的目录
                String transPath = UploadUtil.getTransShapePath(request);
                String uuidDir = UUID.randomUUID().toString();
                File file = new File(shapePath + uuidDir + File.separator + originalFilename);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                // 在上传目录创建uuid文件夹之后，在转换目录也创建一样的uuid目录
                File transFile = new File(transPath + File.separator + uuidDir);
                if (!transFile.exists()){
                    transFile.mkdirs();
                }
                try {
                    shapeZip.transferTo(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    result = new ContentResultForm<String>(false, "上传文件出错!");
                }
            }else {
                result = new ContentResultForm<String>(false, "上传文件失败!");
            }
        }

        return result;
    }

    /**
     * 获取坐标转换zip文件的属性
     * @param request
     * @return
     */
    @ApiOperation(value = "获取上传的压缩的坐标文件列表",notes = "获取上传的压缩的坐标文件列表接口")
    @RequestMapping(value = "/shapeZipList",method = RequestMethod.GET)
    public ContentResultForm<List> shapeZipList(HttpServletRequest request) {
        String shapePath = UploadUtil.getUploadShapePath(request);
        String transPath = UploadUtil.getTransShapePath(request);
        List list = new ArrayList();
        File file = new File(shapePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            List<File> listFile = Arrays.asList(files);
            // 文件按照时间降序排序
            listFile.sort((f1,f2) -> (int) (f2.lastModified() - f1.lastModified()));
            for (File fs : listFile) {
                String uuidDir = fs.getName();
                ShapeZip a = new ShapeZip();
                a.setUuidDir(uuidDir);

                String shapeUuidPath = shapePath  + uuidDir;
                File shapeFile = new File(shapeUuidPath);
                File[] zipfiles = shapeFile.listFiles();
                for (File f : zipfiles){
                    if (!f.isDirectory()) {
                        String fsName = f.getName();
                        // 判断转换目录中是否有已转换好的压缩文件
                        File zipFile = new File(transPath + uuidDir + File.separator + fsName);
                        if (zipFile.exists()){
                            a.setStatus("0");
                        }else {
                            a.setStatus("2");
                        }
                        long fileSize = f.length();

                        a.setName(fsName);
                        a.setFileSize(ZipFileUtils.getfileSizeDescription(fileSize));
                        list.add(a);
                    }
                }

            }
        }
        PageInfo<Object> pageInfo = new PageInfo<>();
        pageInfo.setTotal(list.size());
        pageInfo.setList(list);
        PageHelper.toEasyuiPageInfo(pageInfo);
        //return PageHelper.toEasyuiPageInfo(pageInfo);
        return new ContentResultForm<List>(true,list);
    }

    /**
     * 批量转换坐标接口
     * @param request
     * @param name 文件名称
     * @return
     */
    @ApiOperation(value = "对上传的坐标文件进行转换",notes = "对上传的坐标文件进行转换接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "文件名",required = true),
            @ApiImplicitParam(name = "uuidDir",value = "文件的uuid",required = true),
            @ApiImplicitParam(name = "type",value = "转换类型",required = true),
            @ApiImplicitParam(name = "param",value = "计算的参数",required = true),
            @ApiImplicitParam(name = "L1",value = "原中央经线"),
            @ApiImplicitParam(name = "L2",value = "目标中央经线")
    })
    @RequestMapping(value = "/transShape",method = RequestMethod.POST)
    public ContentResultForm transShape(String name ,String uuidDir,String type,String param,String L1,String L2,HttpServletRequest request) throws Exception {
        // 获取上传文件的路径
        String uuidDirPath = UploadUtil.getUploadShapePath(request)+uuidDir+File.separator;
        // shape 的zip文件
        String zipPath = uuidDirPath+name;
        // 获取转换后路径
        String uuidDirPathAfterTrans = UploadUtil.getTransShapePath(request)  + uuidDir+File.separator;
        // 将上传的shape压缩文件去掉后缀，shape文件名
        String shapeZipName = name.replace(".zip","");

        ZipFileUtils.unZipFiles(zipPath,uuidDirPath ,shapeZipName);
        // 获取解压后的shp文件路径包括名称
        String shpName = ZipFileUtils.getFileName(uuidDirPath + shapeZipName);
        // 转换后重写shp文件路径
        String srcShpPath = uuidDirPathAfterTrans + shapeZipName + File.separator +shpName;
//        // 先创建目录
//        File file = new File(transShapePath + File.separator + shapeZipName);
//        if (!file.exists()){
//            file.mkdirs();
//        }
        // 解压之后的shp文件路径
        String srcfilepath = uuidDirPath  + shapeZipName + File.separator + shpName;
        // 转换shape文件坐标
        double C1 = 0;
        if (StringUtils.isNotBlank(L1)){
            C1 = Double.parseDouble(L1);
        }
        double C2 = 0;
        if (StringUtils.isNotBlank(L2)){
            C2 = Double.parseDouble(L2);
        }
        try {
            File file = new File(srcShpPath);
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            ShapeFileUtils.transShape(srcfilepath,srcShpPath,param,type,C1,C2);
        }catch (FileNotFoundException e){
            FileUtils.delDirectory(new File(uuidDirPathAfterTrans), new Executer());
            return new ContentResultForm(false, "文件转换出错！");
        }catch (ArrayIndexOutOfBoundsException ae){
            FileUtils.delDirectory(new File(uuidDirPathAfterTrans), new Executer());
            return new ContentResultForm(false, "文件转换出错,参数错误！请选择正确的参数");

        }

        // 转换完之后，重新压缩好文件提供下载
        ZipFileUtils.zipShape(uuidDirPathAfterTrans + shapeZipName, false);
        return new ContentResultForm(true, "转换文件成功");
    }

    /**
     * 删除shape文件
     * @param request
     * @return
     */
    @ApiOperation(value = "删除文件",notes = "删除shape压缩文件接口")
    @ApiImplicitParam(name = "uuidDir",value = "文件的uuid",required = true)
    @RequestMapping(value = "/deleteShapeFile",method = RequestMethod.DELETE)
    public ContentResultForm deleteShapeFile(String uuidDir, HttpServletRequest request){
        String uploadShapePath = UploadUtil.getUploadShapePath(request) +uuidDir+ File.separator ;
        String transShapePath = UploadUtil.getTransShapePath(request) +uuidDir+ File.separator;
        // 先删除上传的文件夹
        FileUtils.delDirectory(new File(uploadShapePath), new Executer());
        // 再删除转换的文件夹
        FileUtils.delDirectory(new File(transShapePath ), new Executer());
        return new ContentResultForm(true, "文件已删除!");
    }

    /**
     * 下载shape文件
     * @param name 文件名称
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "下载转换好的shape压缩文件",notes = "下载转换好的shape压缩文件接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "文件名称",required = true),
            @ApiImplicitParam(name = "uuidDir",value = "文件的uuid",required = true)
    })
    @RequestMapping(value = "/downloadShapeFile",method ={RequestMethod.GET, RequestMethod.POST})
    public  void  downloadShapeFile(String name,String uuidDir, HttpServletRequest request, HttpServletResponse response) throws IOException {
        FileInputStream fis = null;
        OutputStream os = null;
        // 获取已转换好坐标的shape  压缩文件目录
        String transShapePath = UploadUtil.getTransShapePath(request);
        String filePath = transShapePath  +uuidDir + File.separator+ name;
        //String fileName = "D:\\yi\\Work\\Project\\Source\\AGCOM\\trunk\\agcloud_sepr\\agsupport\\src\\main\\webapp\\upload\\transform\\b159290f-8a21-4ae7-9b33-c1b89ac2e710\\aa.zip";
        try {
            response.reset();
            response.setHeader("Content-Disposition", "attachment;filename=\""+name+"\"");
            response.setHeader("Content-Type","application/octet-stream");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setHeader("Access-Control-Allow-Origin", "*");
            //文件
            File file = new File(filePath);
            //判断文件是否存在
            if(file.exists()){
                int length =(int)file.length();
                byte[] buf = new byte[length];
                fis = new FileInputStream(file);
                os = response.getOutputStream();
                int b = 0;
                while (b != -1){
                    b = fis.read(buf);
                    if(b != -1){
                        os.write(buf,0,b);//4.写到输出流(out)中
                    }

                }
                os.flush();
            }else{
                System.out.println("您下载的资源已经不存在了");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            os.close();
            fis.close();
        }
    }

    /*public ResponseEntity<OutputStream> export(File file) {
        if (!file.exists()) {
            return null;
        }
        try {
            File file1 =new File("D:\\yi\\Work\\Project\\Source\\AGCOM\\trunk\\agcloud_sepr\\agsupport\\src\\main\\webapp\\upload\\transform\\b159290f-8a21-4ae7-9b33-c1b89ac2e710\\PY01.zip");
            String filename=file1.getName();
                        // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(file1));
                        byte[] buffer = new byte[fis.available()];
                         fis.read(buffer);
                         fis.close();
                         // 清空response
                         response.reset();

                         response.setContentType("application/octet-stream;charset=UTF-8");
                         String fileName = new String(filename.getBytes("gb2312"), "iso8859-1");
                         response.setHeader("Content-disposition", "attachment;filename=" + fileName);
                        OutputStream ouputStream = response.getOutputStream();
                        ouputStream.write(buffer);
                        ouputStream.flush();
                         ouputStream.close();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            return  new ResponseEntity<OutputStream>(fis,headers, HttpStatus.OK);
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentLength(fileSystemResource.contentLength())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileSystemResource);
        }
        catch (IOException ex){
                ex.printStackTrace();
            return  null;
        }
    }*/

    public static class ShapeZip{
        String id;
        String name;
        String fileSize;
        // 上传文件是否转换的状态 0：已转换 1：正在转换中 2：为转换 3：上传文件格式有误
        String status;

        String uuidDir;

        public String getUuidDir() {
            return uuidDir;
        }

        public void setUuidDir(String uuidDir) {
            this.uuidDir = uuidDir;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getFileSize() {
            return fileSize;
        }

        public void setFileSize(String fileSize) {
            this.fileSize = fileSize;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
