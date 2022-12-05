package com.augurit.agcloud.agcom.agsupport.sc.threedheatmap.controller;

import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.agcom.agsupport.sc.threedheatmap.entity.RenderData;
import com.augurit.agcloud.agcom.agsupport.sc.threedheatmap.utils.PositionDataHelper;
import com.augurit.agcloud.agcom.agsupport.sc.threedheatmap.utils.Transform;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.apache.commons.io.FileUtils.readFileToString;

/**
 * @author Liangjh
 * @version 1.0
 * @date 2020/1/15 11:49
 * @description 3D热力图
 */
@RestController
@RequestMapping("/agsupport/threedheatmap")
public class AgThreeDHeatMapController {
    private static Logger logger = LoggerFactory.getLogger(AgThreeDHeatMapController.class);
    private Transform transform = new Transform();
    @Value("${heatMapData.historyPath}")
    private String historyPath;
    @Value("${heatMapData.txDataPath}")
    private String txDataPath;

    @RequestMapping("index")
    public String index() {
        logger.info("/agsupport/threedheatmap/index");
        return "index";
    }

    //处理好的历史数据
    @RequestMapping("getHistoryData")
    public ContentResultForm getHistoryData(String address, String extent, String date,
                                            String time,
                                            String cellSize) {
        ContentResultForm contentResultForm = new ContentResultForm(false);
        //testCode
        String fileName = "";//2019-12-27_00-00-00_100.json
        String filePath = historyPath + date + File.separator;
        fileName = date + "_" + time + "_" + cellSize + ".json";
        try {
            File file = new File(filePath + fileName);
            String jsonStr = readFileToString(file, "utf-8");
            //转化json
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            contentResultForm.setSuccess(true);
            contentResultForm.setContent(jsonObject);
            return contentResultForm;
        } catch (Exception e) {
            logger.error("访问历史数据失败", e);
            return contentResultForm;
        }
    }


    //实时计算数据
    @RequestMapping("/getDataByParams")
    public ContentResultForm getDataByParams(String address, String extent, String date,
                                             String time,
                                             String cellSize) {
        if (date == null || date.isEmpty()) {
            date = "2020-01-20";
        }
        if (time == null || time.isEmpty()) {
            time = "08:30:00";
        }
        if (cellSize == null || cellSize.isEmpty()) {
            cellSize = "100";
        }
        ContentResultForm contentResultForm = new ContentResultForm(false);
//      txDataPath = "C:\\dev\\WorkSpaces\\qq_map\\";//test
        String fileName = ""; //datas.txt //2020-01-20.txt
        //调用python程序获取结果，并把返回结果按日期写入到txDataPath目录的文件中，返回文件名 (腾讯那边以后应该会开通按时间点给数据，比如date=2020-01-28 time=11:05:00 的数据)
        //fileName = call(pythonCommand);//正式代码
        fileName = date + ".txt";

        //读取python下载回来的文件内容
        File file = new File(txDataPath + fileName);//
        if (!file.exists()) {
            logger.info("获取txApi数据失败，现在尝试获取历史数据...");
            time = time.replaceAll(":", "-");
            return getHistoryData(address, extent, date, time, cellSize);
        }
        String jsonStr = null;
        try {
            jsonStr = readFileToString(file, "utf-8");

            //转化json
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                if (entry.getKey().equals(date + " " + time)) {
                    //取得某个 date-time 中所有点（x1,y1,w1|...）的字符串(原始数据 w=1，则没有w,需要补全)
                    String xywPoints = (String) entry.getValue();//原始数据点
                    StringBuilder xywPointsFormed = new StringBuilder();//转换坐标后的数据点

                    //转换坐标：
//                if( xywPoints==null || xywPoints.isEmpty())
//                    return null ;
                    String[] xywTemps = xywPoints.split("\\|");
                    for (String xywTemp : xywTemps) {
                        String[] xyw = xywTemp.split(",");
                        Double tempX = Double.parseDouble(xyw[1]);
                        Double tempY = Double.parseDouble(xyw[0]);
                        double x = (10000 * 113.35236525515938 + tempX) / 10000;
                        double y = (10000 * 23.098317499051777 + tempY) / 10000;
                        Transform.Point point = transform.gcj02towgs84(x, y);
                        x = -11565826.9276565 + 102439.472684981 * point.X;
                        y = -2353028.10631748 + 111668.974674894 * point.Y;
                        double w = 0;
                        if (xyw.length == 2) {
                            w = 1;
                        } else {
                            w = Double.parseDouble(xyw[2]);
                        }
                        xywPointsFormed.append(x + "," + y + "," + w + "|");
                    }
                    System.out.println("GCJ02 转换为 WGS84 坐标转换完毕");

                    //按照需要的网格大小进行处理：
                    System.out.println("开始网格处理");
                    RenderData renderData = PositionDataHelper.TransformData2(xywPointsFormed.toString(), Integer.valueOf(cellSize));
                    JSONObject result = new JSONObject();
                    System.out.println("计算完毕，输出结果..");
                    result.put("key", "2019-12-26_11-40-00_25");
                    result.put("data", renderData);
                    contentResultForm.setSuccess(true);
                    contentResultForm.setContent(result);
                    return contentResultForm;//success
                }
            }

        } catch (IOException e) {
            logger.info("getDataByParams，读取文件失败：" + file.getAbsolutePath(),e);
            e.printStackTrace();
        } catch (Exception e1){
            logger.info("getDataByParams，处理失败", e1);
        }
        return contentResultForm;//fail
    }


}
