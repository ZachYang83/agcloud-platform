package com.augurit.agcloud.agcom.agsupport.sc.coordTrans.controller;

import com.augurit.agcloud.agcom.agsupport.common.util.UploadUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import com.augurit.agcloud.agcom.agsupport.sc.coordTrans.util.CoordTransUtil;
import com.augurit.agcloud.agcom.agsupport.sc.dic.converter.AgDicConverter;
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
import com.baidu.ueditor.upload.Uploader;
import com.common.thread.Executer;
import com.common.util.Common;
import com.common.util.FileUtils;
import com.common.util.HttpRequester;
import com.common.util.HttpRespons;
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
@Api(value = "????????????",description = "????????????????????????")
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
     * ????????????
     */
    @ApiOperation(value = "??????XIAN80???WGS84",notes = "??????XIAN80???WGS84??????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "x",value = "XIAN80???????????????x??????",required = true),
            @ApiImplicitParam(name = "y",value = "XIAN80???????????????y??????????????????" +
                    "?????????x=2550626.144???y=425051.515",required = true),
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

    @ApiOperation(value = "???????????????WGS84",notes = "???????????????WGS84??????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "xMax",value = "x???????????????"),
            @ApiImplicitParam(name = "post",value = "???JSONArray???????????????x???y??????JSONObject,??????:" +
                    "?????????xMax=2550626.144\n" +
                    "post=[{\"x\":2500626.144,\"y\":425051.515}]" +
                    "?????????x=2550626.144???y=425051.515"),
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
                    //????????????38
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

    @ApiOperation(value = "??????????????????",notes = "????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "json",value = "?????????????????????json???????????????????????????\n" +
                    "{\"type\":\"80-84\",\"coincidentPoint\":\"38440020.61,2523155.572 113.416944,22.805833\\n38436094.89,2518772.439 ???\",\"L1\":\"114\",\"L2\":\"114\"}",required = true)
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
//                js = js + "??????????????????????????????\r\n\r\n";
                js = js + parameter.getDX() + "," + parameter.getDY() + "," + parameter.getA() + "," + parameter.getB();
            } else {
                ParameterSeven parameter = ICoordTransApp.CalcuteParameterSeven(coincidentPoint, L1, L2, type);
//                js = "X:  " + parameter.getDX() + "\r\nY:  " + parameter.getDY() + "\r\nZ:  " + parameter.getDZ() + "\r\nM:  " + parameter.getM() + "\r\nWx:  " + parameter.getWX() + "\r\nWy:  " + parameter.getWY() + "\r\nWz:  " + parameter.getWZ() + "\r\n\r\n";
//                js = js + "??????????????????????????????\r\n\r\n";
                js = js + parameter.getDX() + "," + parameter.getDY() + "," + parameter.getDZ() + "," + parameter.getM() + "," + parameter.getWX() + "," + parameter.getWY() + "," + parameter.getWZ();
            }
        } catch (Exception var19) {
            var19.printStackTrace();
        }
        return new ContentResultForm<String>(true,js);
    }

    @ApiOperation(value = "????????????????????????????????????",notes = "??????????????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "json",value = "????????? " +
                    "?????????{\"coor\":\"38436213.83,2540244.805\",\"type\":\"80-84\",\"param\":\"8.814299389719963,-20.63454755395651,-9.398727240972221,1.0000033462452704,-2.575799865622841E-6,6.259805057862167E-6,-1.7068264344999307E-5\",\"L1\":\"114\",\"L2\":\"114\"}")
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

    @ApiOperation(value = "???????????????????????????????????????????????????",notes = "?????????????????????????????????????????????????????????")
    @ApiImplicitParam(name = "json",value = "??????:  " +
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

    @ApiOperation(value = "??????WKT??????????????????",notes = "??????WKT????????????????????????")
    @ApiImplicitParam(name = "json",value = "?????????\n" +
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
    @ApiOperation(value = "?????????????????????????????????????????????????????????WKT?????????",notes = "????????????????????????????????????????????????????????????????????????WKT????????????")
    @ApiImplicitParam(name = "json",value = "????????? " +
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
    @ApiOperation(value = "??????????????????????????????????????????????????????????????????????????????",notes = "???????????????????????????????????????????????????????????????????????????????????????????????????")
    @ApiImplicitParam(name = "json",value = "????????? " +
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
    @ApiOperation(value = "??????????????????JSON????????????WKT??????????????????",notes = "??????????????????JSON????????????WKT?????????????????????")
    @ApiImplicitParam(name = "json",value = "?????????" +
            "json= {\"type\":\"Point\",\"coordinates\":[6,10]}")
    @RequestMapping(value = "/jsonToWkt",method = RequestMethod.POST)
    public ContentResultForm jsonToWkt(String json,HttpServletRequest request) {
        if (Common.isCheckNull(json)) return null;
        return new ContentResultForm<String>(true,SpatialUtil.jsonToWkt(json));
    }

    @ApiOperation(value = "?????????????????????WGS84???WKT??????????????????????????????????????????",notes = "?????????????????????WGS84???WKT?????????????????????????????????????????????")
    @ApiImplicitParam(name = "wkt",value = "????????? " +
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

    @ApiOperation(value = "WKT?????????GeoJson??????",notes = "WKT?????????GeoJson????????????")
    @ApiImplicitParam(name = "wkt")
    @RequestMapping(value = "/wktToGeoJson",method = RequestMethod.POST)
    public ContentResultForm wktToGeoJson(String wkt) {
        return new ContentResultForm<String>(true,SpatialUtil.wktToJson(wkt));
    }

    @ApiOperation(value = "GeoJson?????????WKT??????",notes = "GeoJson?????????WKT????????????")
    @ApiImplicitParam(name = "geoJson")
    @RequestMapping(value = "/GeoJsonToWkt",method = RequestMethod.POST)
    public ContentResultForm GeoJsonToWkt(String geoJson) {
        return new ContentResultForm<String>(true,SpatialUtil.jsonToWkt(geoJson));
    }

    @Autowired
    private UploaderFactory uploaderFactory;
    /**
     * ??????????????????
     * @param request
     * @return
     */
    @ApiOperation(value = "??????Shape????????????",notes = "??????Shape??????????????????")
    @ApiImplicitParam(value = "*????????????????????????????????????????????? 1?????????????????????????????????zip    2???shape????????????????????????3?????????????????????????????????.shp, .dbf, .shx???")
    @RequestMapping(value = "/uploadShapeFile",method = RequestMethod.POST)
    public ContentResultForm uploadShapeFile(HttpServletRequest request) {
        String contentType = request.getContentType();
        ContentResultForm result = new ContentResultForm(true, "??????????????????");

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

                // ????????????????????????????????????
                /*if(originalFilename.matches(".*.zip")) {

                }*/
                // ?????????????????????
                String shapePath = UploadUtil.getUploadShapePath(request);
                // ???????????????????????????
                String transPath = UploadUtil.getTransShapePath(request);
                String uuidDir = UUID.randomUUID().toString();
                File file = new File(shapePath + uuidDir + File.separator + originalFilename);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                // ?????????????????????uuid???????????????????????????????????????????????????uuid??????
                File transFile = new File(transPath + File.separator + uuidDir);
                if (!transFile.exists()){
                    transFile.mkdirs();
                }
                try {
                    shapeZip.transferTo(file);
                } catch (IOException e) {
                    e.printStackTrace();
                    result = new ContentResultForm<String>(false, "??????????????????!");
                }
            }else {
                result = new ContentResultForm<String>(false, "??????????????????!");
            }
        }

        return result;
    }

    /**
     * ??????????????????zip???????????????
     * @param request
     * @return
     */
    @ApiOperation(value = "??????????????????????????????????????????",notes = "????????????????????????????????????????????????")
    @RequestMapping(value = "/shapeZipList",method = RequestMethod.GET)
    public ContentResultForm<List> shapeZipList(HttpServletRequest request) {
        String shapePath = UploadUtil.getUploadShapePath(request);
        String transPath = UploadUtil.getTransShapePath(request);
        List list = new ArrayList();
        File file = new File(shapePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            List<File> listFile = Arrays.asList(files);
            // ??????????????????????????????
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
                        // ?????????????????????????????????????????????????????????
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
     * ????????????????????????
     * @param request
     * @param name ????????????
     * @return
     */
    @ApiOperation(value = "????????????????????????????????????",notes = "??????????????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "?????????",required = true),
            @ApiImplicitParam(name = "uuidDir",value = "?????????uuid",required = true),
            @ApiImplicitParam(name = "type",value = "????????????",required = true),
            @ApiImplicitParam(name = "param",value = "???????????????",required = true),
            @ApiImplicitParam(name = "L1",value = "???????????????"),
            @ApiImplicitParam(name = "L2",value = "??????????????????")
    })
    @RequestMapping(value = "/transShape",method = RequestMethod.POST)
    public ContentResultForm transShape(String name ,String uuidDir,String type,String param,String L1,String L2,HttpServletRequest request) throws Exception {
        // ???????????????????????????
        String uuidDirPath = UploadUtil.getUploadShapePath(request)+uuidDir+File.separator;
        // shape ???zip??????
        String zipPath = uuidDirPath+name;
        // ?????????????????????
        String uuidDirPathAfterTrans = UploadUtil.getTransShapePath(request)  + uuidDir+File.separator;
        // ????????????shape???????????????????????????shape?????????
        String shapeZipName = name.replace(".zip","");

        ZipFileUtils.unZipFiles(zipPath,uuidDirPath ,shapeZipName);
        // ??????????????????shp????????????????????????
        String shpName = ZipFileUtils.getFileName(uuidDirPath + shapeZipName);
        // ???????????????shp????????????
        String srcShpPath = uuidDirPathAfterTrans + shapeZipName + File.separator +shpName;
//        // ???????????????
//        File file = new File(transShapePath + File.separator + shapeZipName);
//        if (!file.exists()){
//            file.mkdirs();
//        }
        // ???????????????shp????????????
        String srcfilepath = uuidDirPath  + shapeZipName + File.separator + shpName;
        // ??????shape????????????
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
            return new ContentResultForm(false, "?????????????????????");
        }catch (ArrayIndexOutOfBoundsException ae){
            FileUtils.delDirectory(new File(uuidDirPathAfterTrans), new Executer());
            return new ContentResultForm(false, "??????????????????,???????????????????????????????????????");

        }

        // ???????????????????????????????????????????????????
        ZipFileUtils.zipShape(uuidDirPathAfterTrans + shapeZipName, false);
        return new ContentResultForm(true, "??????????????????");
    }

    /**
     * ??????shape??????
     * @param request
     * @return
     */
    @ApiOperation(value = "????????????",notes = "??????shape??????????????????")
    @ApiImplicitParam(name = "uuidDir",value = "?????????uuid",required = true)
    @RequestMapping(value = "/deleteShapeFile",method = RequestMethod.DELETE)
    public ContentResultForm deleteShapeFile(String uuidDir, HttpServletRequest request){
        String uploadShapePath = UploadUtil.getUploadShapePath(request) +uuidDir+ File.separator ;
        String transShapePath = UploadUtil.getTransShapePath(request) +uuidDir+ File.separator;
        // ???????????????????????????
        FileUtils.delDirectory(new File(uploadShapePath), new Executer());
        // ???????????????????????????
        FileUtils.delDirectory(new File(transShapePath ), new Executer());
        return new ContentResultForm(true, "???????????????!");
    }

    /**
     * ??????shape??????
     * @param name ????????????
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "??????????????????shape????????????",notes = "??????????????????shape??????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "????????????",required = true),
            @ApiImplicitParam(name = "uuidDir",value = "?????????uuid",required = true)
    })
    @RequestMapping(value = "/downloadShapeFile",method ={RequestMethod.GET, RequestMethod.POST})
    public  void  downloadShapeFile(String name,String uuidDir, HttpServletRequest request, HttpServletResponse response) throws IOException {
        FileInputStream fis = null;
        OutputStream os = null;
        // ???????????????????????????shape  ??????????????????
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
            //??????
            File file = new File(filePath);
            //????????????????????????
            if(file.exists()){
                int length =(int)file.length();
                byte[] buf = new byte[length];
                fis = new FileInputStream(file);
                os = response.getOutputStream();
                int b = 0;
                while (b != -1){
                    b = fis.read(buf);
                    if(b != -1){
                        os.write(buf,0,b);//4.???????????????(out)???
                    }

                }
                os.flush();
            }else{
                System.out.println("????????????????????????????????????");
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
                        // ??????????????????????????????
            InputStream fis = new BufferedInputStream(new FileInputStream(file1));
                        byte[] buffer = new byte[fis.available()];
                         fis.read(buffer);
                         fis.close();
                         // ??????response
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
        // ????????????????????????????????? 0???????????? 1?????????????????? 2???????????? 3???????????????????????????
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
