package com.augurit.agcloud.agcom.agsupport.sc.labelImport.controller;


import com.augurit.agcloud.agcom.agsupport.common.util.UploadUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.WriteFile;
import com.augurit.agcloud.agcom.agsupport.sc.labelImport.service.IAgShpDataImport;
import com.common.util.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * 标注导入
 * Created by 陈泽浩 on 2017-05-05.
 */
@RestController
@RequestMapping("/agsupport/labelImport")
public class AgLaberController {

    @Autowired
    public IAgShpDataImport shpDataImport;


    public static String RELATIVE = "relative";

    public static String ABSOLUTE = "absolute";

    @RequestMapping("/getShpData")
    public String getShpData(HttpServletRequest request) {
        String js = "";
        try {
            js = shpDataImport.getShpData(request, ABSOLUTE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return js;
    }

    @RequestMapping("/getShp")
    public String getShp(HttpServletRequest request) {
        String js = "";
        try {
            String tempDirPath = UploadUtil.getUploadAbsolutePath() + "uploads\\";
            //js = shpDataImport.getShp((tempDirPath + request.getParameter("fileName")).replace("\\", "/"));
            js = shpDataImport.getShpDataByZipFile((tempDirPath + request.getParameter("fileName")).replace("\\", "/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return js;
    }

    @RequestMapping("/getDwgData")
    public String getDwgData(HttpServletRequest request) {
        String js = "";
        try {
            String tempDirPath = UploadUtil.getUploadAbsolutePath() + "uploads\\";
            js = shpDataImport.readDwgData((tempDirPath + request.getParameter("fileName")).replace("\\", "/"),"","",request.getParameter("fileName"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return js;
    }

    @RequestMapping("/getDxfData")
    public String getDxfData(HttpServletRequest request) {
        String js = "";
        try {
            js = shpDataImport.getDxfData(request, ABSOLUTE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return js;
    }

    /**
     * 上传文件后返回相对路径
     *
     * @param request
     * @return
     */
    @RequestMapping("/uploadFile")
    public String uploadFile(HttpServletRequest request) {
        String js = "";
        try {
            js = shpDataImport.uploadFile(request, RELATIVE);
            if (js != null && !js.equals(""))
                js = "{\"success\":true,\"path\":\"" + js + "\"}";
        } catch (Exception e) {
            e.printStackTrace();
            js = "{'success':false}";
        }
        return js;
    }

    @RequestMapping("/deleteFile")
    public String deleteFile(String path) {
        String result = "{'success':false}";
        if (Common.isCheckNull(path)) return result;
        if (shpDataImport.deleteFile(path)) result = "{'success':true}";
        return result;
    }

    @RequestMapping("/getGeoJson")
    public String getGeoJson(HttpServletRequest request) {
        String js = "";
        try {
            String path = shpDataImport.uploadFile(request, ABSOLUTE);
            js = WriteFile.readFile(path);
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return js;
    }

    @RequestMapping("/getCsvCoordinates")
    public String getCsvCoordinates(HttpServletRequest request) {
        String js = "";
        String path = UploadUtil.getUploadAbsolutePath() + "\\uploads\\" + request.getParameter("fileName");
        try {
            js = shpDataImport.readCsvFile(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return js;
    }

    @RequestMapping("/getExeclCoordinates")
    public String getExeclCoordinates(HttpServletRequest request) {
        String js = "";
        String path = UploadUtil.getUploadAbsolutePath() + "\\uploads\\" + request.getParameter("fileName");
        try {
            js = shpDataImport.readExcel(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return js;
    }

    @RequestMapping("/writeCsvCoordinates")
    public void writeCsvCoordinates(HttpServletRequest request) {
        try {
            String coordinates = request.getParameter("coordinates");
            String path = UploadUtil.getUploadAbsolutePath() + "\\uploads\\" + request.getParameter("fileName");
            shpDataImport.writeCsvFile(path, coordinates);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
