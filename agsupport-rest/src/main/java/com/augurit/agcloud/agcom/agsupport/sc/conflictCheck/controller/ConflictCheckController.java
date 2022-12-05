package com.augurit.agcloud.agcom.agsupport.sc.conflictCheck.controller;


import com.augurit.agcloud.aeaMap.util.DocumentHandler;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.topology.service.IAgOperate;
import com.augurit.agcloud.agcom.agsupport.sc.topology.service.IAgSpatialUtilsService;
import com.augurit.agcloud.agcom.agsupport.sc.topology.service.impl.AgSpatialUtilsServiceImpl;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.common.util.Common;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

@RestController
@RequestMapping("/agsupport/conflictCheck")
public class ConflictCheckController {

    private final static int THRESHOLD = 600;

    private static Logger logger = LoggerFactory.getLogger(ConflictCheckController.class);

    @Autowired
    private IAgOperate operate;

    @Autowired
    private IAgSpatialUtilsService agSpatialUtilsService;


    /*
     * 批量计算地块相交
     * @param features1: json字符串, e.g:[{wkt:"",index:""}]
     * @param features2: json字符串, e.g:[{wkt:"",index:""}]
     * */
    @RequestMapping("/calcIntersection")
    public String calcIntersetions(HttpServletRequest request) throws Exception {

        String features1Str = request.getParameter("features1");
        String features2Str = request.getParameter("features2");
        try {

            ObjectMapper mapper = new ObjectMapper();//定义 org.codehaus.jackson
            List<Map> features1 = mapper.readValue(features1Str,new TypeReference<List<Map>>() { });

            List<Map> features2 = mapper.readValue(features2Str,new TypeReference<List<Map>>() { });

            List<Map> calcItems = constructCalcItems(features1, features2);

            List calcResult = batchCalcIntersection(calcItems);
            return JsonUtils.toJson(new ContentResultForm<>(true, calcResult, "计算几何要素相交成功"));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ContentResultForm<>(false, "计算几何要素相交失败"));
    }


    /*
     * 冲突分析
     * @param features1: json字符串, e.g:[{wkt:"",index:""}] 图层1 的feature
     * @param features2: json字符串, e.g:[{wkt:"",index:""}] 图层2 的feature
     * @param checkFeatures: json字符串, e.g:[{wkt:"",index:""}] 检测范围 的feature
     * */
    @RequestMapping("/conflictAnalysis")
    public String conflictAnalysis(HttpServletRequest request) throws Exception {

        List analysisResult = new ArrayList();
        try {

        //读取请求参数
        String features1Str = request.getParameter("features1");
        String features2Str = request.getParameter("features2");
        String checkFeaturesStr =     request.getParameter("checkFeatures");

         ObjectMapper mapper = new ObjectMapper();//定义 org.codehaus.jackson
         List<Map> features = mapper.readValue(checkFeaturesStr,new TypeReference<List<Map>>() { });


        //计算第一个图层 与 检测范围 的 相交部分
        List<Map> features1 = mapper.readValue(features1Str,new TypeReference<List<Map>>() { });
        List calcItems1 = constructCalcItems(features, features1);
        List<Map> calcIntersectionResult1 = batchCalcIntersection(calcItems1);

        //计算第二个图层 与 检测范围 的 相交部分
        List<Map> features2 = mapper.readValue(features2Str,new TypeReference<List<Map>>() { });
        List calcItems2 = constructCalcItems(features, features2);
        List calcIntersectionResult2 = batchCalcIntersection(calcItems2);

        //计算第一个图层与第二个图层的相交部分
        List<Map> calcAreaItems = constructCalcItems(calcIntersectionResult1, calcIntersectionResult2);
        List calcResult3 = batchCalcIntersection(calcAreaItems);


        //计算相交部分的面积
        analysisResult = batchCalcArea(calcResult3);

         return JsonUtils.toJson(new ContentResultForm<>(true, analysisResult, "冲突分析成功"));
        }catch (Exception e) {
            e.printStackTrace();
        }

        return JsonUtils.toJson(new ContentResultForm<>(false, "冲突分析成功"));
    }


    /**
     *  构造要计算的项
     * */
    private List<Map> constructCalcItems(List<Map> features1, List<Map> features2) {
        List<Map> calcItems = new ArrayList<>();

        try {
            for (Map f1 : features1) {
                String index1 = f1.get("index").toString();
                String wkt1 = (String) f1.get("wkt");

                for (Map f2 : features2) {
                    String index2 = f2.get("index").toString();
                    String wkt2 = (String) f2.get("wkt");
                    String index = "";

                    if(StringUtils.isNotEmpty(index1) && StringUtils.isNotEmpty(index2)) {
                        index = index1 + "_" + index2;
                    }else {
                       index = StringUtils.isNotEmpty(index1) ? index1 : index2;
                    }

                    Map<String, String> map = new HashMap<>();
                    map.put("wkt1", wkt1);
                    map.put("wkt2", wkt2);
                    map.put("index", index);
                    calcItems.add(map);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return calcItems;
    }

    private List<Map> batchCalcIntersection(List calcItems) {
        List<Map> calcResult = new ArrayList<>();
        try {
            int processorNum = Runtime.getRuntime().availableProcessors() + 1;
            ExecutorService executor = Executors.newFixedThreadPool(processorNum);
            CompletionService<List<Map>> completionService = new ExecutorCompletionService<List<Map>>(executor);

            int taskNum = (int) Math.ceil(calcItems.size() / (THRESHOLD + 0.0));

            for (int i = 1; i <= taskNum; i++) {
                final int key = i;
                completionService.submit(new Callable<List<Map>>() {

                    @Override
                    public List<Map> call() throws Exception {
                        return getIntersection((key - 1) * THRESHOLD, key * THRESHOLD, calcItems);
                    }
                });
            }

            for (int i = 0; i < taskNum; i++) {
                List<Map> res = completionService.take().get();
                calcResult.addAll(res);
            }
            executor.shutdown();// 所有任务已经完成,关闭线程
        }catch (Exception e) {
            e.printStackTrace();
        }

        return calcResult;
    }

    private List<Map> getIntersection(int start, int end, List<Map> items) {
        List<Map> res = new ArrayList<>();

        IAgSpatialUtilsService spatialUtilsService = new AgSpatialUtilsServiceImpl();

        for(int i = start; i < end && i < items.size(); i++) {
            Map map = items.get(i);

            Map<String, String> r = new HashMap<>();
            String wkt1 = (String) map.get("wkt1");
            String wkt2 = (String) map.get("wkt2");
            String intersection = spatialUtilsService.stIntersection(wkt1, wkt2); //TODO

            if(!intersection.equals("POLYGON EMPTY") && intersection.contains("POLYGON")) {
                r.put("index", (String) map.get("index"));
                r.put("wkt",  intersection);
                res.add(r);
            }
        }

        return res;
    }



    /*
     * 批量计算地块面积
     * */
    @RequestMapping("/calcArea")
    public String calcArea(HttpServletRequest request) throws Exception {
        Map paramMap = request.getParameterMap();
        Set entrys = paramMap.entrySet();
        ObjectMapper mapper = new ObjectMapper();//定义 org.codehaus.jackson
        List<Map> features = new ArrayList<>();

        for(Object itemSet : entrys) {
            String[] values = (String[]) ((Map.Entry)itemSet).getValue();
            String featuresStr = values[0];
            List<Map> featuresSet = mapper.readValue(featuresStr, new TypeReference<List<Map>>() { });
            features.addAll(featuresSet);
        }

        try {

            int processorNum = Runtime.getRuntime().availableProcessors() + 1;
            ExecutorService executor = Executors.newFixedThreadPool(processorNum);
            CompletionService<List> completionService = new ExecutorCompletionService<List>(executor);

            int taskNum = (int) Math.ceil(features.size() / (THRESHOLD + 0.0));

            for (int i = 1; i <= taskNum; i++) {
                final int key = i;
                completionService.submit(new Callable<List>() {

                    @Override
                    public List call() throws Exception {
                        return getArea((key - 1) * THRESHOLD, key * THRESHOLD, features);
                    }
                });
            }

            List calcResult = new ArrayList();
            for (int i = 0; i < taskNum; i++) {
                List res = completionService.take().get();
                calcResult.addAll(res);
            }
            executor.shutdown();// 所有任务已经完成,关闭线程
            return JsonUtils.toJson(new ContentResultForm<>(true, calcResult, "计算几何要素面积成功"));

        }catch (Exception e){
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ContentResultForm<>(false, "计算几何要素面积失败"));
    }

    private List getArea(int start, int end, List<Map> items) {
        List<Map> res = new ArrayList<>();

        IAgSpatialUtilsService spatialUtilsService = new AgSpatialUtilsServiceImpl();

        for(int i = start; i < end && i < items.size(); i++) {
            Map jsonObject = items.get(i);
            String wkt = (String) jsonObject.get("wkt");
            String index =  jsonObject.get("index").toString();

            String area = spatialUtilsService.stArea(wkt); //TODO
            Map<String, String> map = new HashMap<>();
            map.put("index", index);
            map.put("area", area);
            map.put("wkt", wkt);
            res.add(map);
        }
        return res;
    }


   private List batchCalcArea(List features) {
       List calcResult = new ArrayList();
        try {
            int processorNum = Runtime.getRuntime().availableProcessors() + 1;
            ExecutorService executor = Executors.newFixedThreadPool(processorNum);
            CompletionService<List> completionService = new ExecutorCompletionService<List>(executor);

            int taskNum = (int) Math.ceil(features.size() / (THRESHOLD + 0.0));

            for (int i = 1; i <= taskNum; i++) {
                final int key = i;
                completionService.submit(new Callable<List>() {

                    @Override
                    public List call() throws Exception {
                        return getArea((key - 1) * THRESHOLD, key * THRESHOLD, features);
                    }
                });
            }
            for (int i = 0; i < taskNum; i++) {
                List res = completionService.take().get();
                calcResult.addAll(res);
            }
            executor.shutdown();// 所有任务已经完成,关闭线程
        }catch (Exception e){
            e.printStackTrace();
        }
        return calcResult;
    }

    /**
     * 冲突检测报告打印
     */

    @RequestMapping("grantReport")
    public String conflictPOST(String time,
                               String landType,
                               String dataLayers,
                                String totalArea,
                               String image,
                               String tableStr,
                                String dataStr,
                              HttpServletRequest request,
                               HttpServletResponse response) throws Exception {

        response.reset();
        Map<String, Object> content = new HashMap<String, Object>();
        String webPath = request.getSession().getServletContext()
                .getRealPath("/");
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            if (!Common.isCheckNull(time)) {
                String timeStamp = Common.checkNull(time, "");
                Calendar c = Calendar.getInstance();
                c.setTime(format.parse(timeStamp));
                // "upload/控制线检测/2015/07/201570722123两规冲突分析报告.doc"

                String uploadDirPath = "uploads";

                String filePath = uploadDirPath
                        .concat("\\冲突检测\\")
                        .concat(String.valueOf(c.get(Calendar.YEAR)))
                        .concat(File.separator)
                        .concat(String.valueOf(c.get(Calendar.MONTH) + 1))
                        .concat(File.separator).concat(timeStamp)
                        .concat("两规冲突分析报告.doc");
                File f = new File(webPath.concat(filePath));
                if (!f.exists()) {
                    content.put("landType", Common.checkNull(landType, ""));
                    SimpleDateFormat format2 = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm");
                    content.put("time", format2.format(format.parse(timeStamp)));
                    content.put("dataLayers", Common.checkNull(dataLayers, ""));
                    content.put("totalArea", Common.checkNull(totalArea, ""));
                    content.put("image", Common.checkNull(image, ""));
                    List<Map> table = new ArrayList<Map>();
                    JSONArray tableArr = JSONArray.fromObject(Common.checkNull(
                            tableStr, "[]"));
                    content.put("table", table);
                    Map<String, String> map;
                    for (int i = 0; i < tableArr.size(); i++) {
                        map = tableArr.getJSONObject(i);
                        table.add(map);
                    }
                    List<Map> dataList = new ArrayList<Map>();
                    JSONArray listArr = JSONArray.fromObject(Common.checkNull(
                            dataStr, "[]"));
                    content.put("dataList", dataList);
                    Map<String, Object> map2;
                    for (int i = 0; i < listArr.size(); i++) {
                        JSONObject object = listArr.getJSONObject(i);
                        map2 = new HashMap<String, Object>();
                        map2.put("classification",
                                object.getString("classification"));
                        List<Map> dataMap = new ArrayList<Map>();
                        JSONArray mapArr = object.getJSONArray("dataMap");
                        map2.put("dataMap", dataMap);
                        Map<String, String> map3;
                        for (int j = 0; j < mapArr.size(); j++) {
                            map3 = mapArr.getJSONObject(j);
                            dataMap.add(map3);
                        }
                        dataList.add(map2);
                    }
                    DocumentHandler handler = new DocumentHandler(webPath);
                    handler.createDoc(content, "两规冲突分析报告_template.flt",
                            filePath);
                }
                outputFile(response, webPath.concat(filePath));
            } else {
                return null;// "系统出错了，请联系系统管理员";
            }

        } catch (Exception e) {
            return null;// e.getMessage();
        }
        return null;
    }


    /**
     * 读取文件输出到浏览器
     *
     * @param outputFilePath
     *            文件路径
     * @param response
     * @throws IOException
     */
    private void outputFile(HttpServletResponse response, String outputFilePath)
            throws IOException {
        response.setContentType("application/msword");

        String saveName = new String(outputFilePath.substring(
                outputFilePath.lastIndexOf("\\") + 1).getBytes("GBK"),
                "ISO8859-1");
        response.setHeader("Content-disposition", "inline; filename=\""
                + saveName + "\";");

        BufferedOutputStream bout = new BufferedOutputStream(
                response.getOutputStream());

        InputStream fileInputStream = FileUtils.openInputStream(new File(
                outputFilePath));

        // 将 word 文件 输入到浏览器中
        IOUtils.copy(fileInputStream, bout);

        // 关闭流
        IOUtils.closeQuietly(bout);
        IOUtils.closeQuietly(fileInputStream);

    }




}
