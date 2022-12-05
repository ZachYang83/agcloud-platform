package com.augurit.agcloud.agcom.agsupport.common.util.io;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 数据转换工具类
 *
 * @Author LRF
 * @ClassName DataConversionUtil
 * @Date 2019/8/4 20:24
 * @Version 1.0
 **/
public class DataConversionUtil {


    /**
     * 单表改变Map的key
     *
     * @param map    map
     * @param oldKey 旧的key
     * @param newKey 新的key
     * @return
     */
    public static Map<String, Object> changeMapKey(Map<String, Object> map, String[] oldKey, String[] newKey) {
        try {
            if (null == map || map.size() < 1) {
                return null;
            }
            for (int i = 0; i < oldKey.length; i++) {
                Object obj = map.get(oldKey[i]);
                map.remove(oldKey[i]);
                map.put(newKey[i], obj);
            }
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 改变List<Map<String, Object>>中key名称
     *
     * @param mapList
     * @param oldKey
     * @param newKey
     * @return
     */
    public static List<Map<String, Object>> changeMoreMapKey(List<Map<String, Object>> mapList, String[] oldKey, String[] newKey) {
        try {
            if (null == mapList || mapList.size() < 1) {
                return mapList;
            }
            for (int i = 0; i < mapList.size(); i++) {
                changeMapKey(mapList.get(i), oldKey, newKey);
            }
            return mapList;
        } catch (Exception e) {
            return mapList;
        }
    }


    /**
     * 设置下载参数
     *
     * @param request
     * @param response
     * @param fileName
     */
    public static boolean setHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("content-type", "application/octet-stream");
            String browser = request.getHeader("User-Agent");
            if (-1 < browser.indexOf("MSIE 6.0") || -1 < browser.indexOf("MSIE 7.0")) {
                // IE6, IE7 浏览器
                response.addHeader("content-disposition", "attachment;filename="
                        + new String(fileName.getBytes(), "ISO8859-1"));
            } else if (-1 < browser.indexOf("MSIE 8.0")) {
                // IE8
                response.addHeader("content-disposition", "attachment;filename="
                        + URLEncoder.encode(fileName, "UTF-8"));
            } else if (-1 < browser.indexOf("MSIE 9.0")) {
                // IE9
                response.addHeader("content-disposition", "attachment;filename="
                        + URLEncoder.encode(fileName, "UTF-8"));
            } else if (-1 < browser.indexOf("Chrome")) {
                // 谷歌
                response.addHeader("content-disposition",
                        "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
            } else if (-1 < browser.indexOf("Safari")) {
                // 苹果
                response.addHeader("content-disposition", "attachment;filename="
                        + new String(fileName.getBytes(), "ISO8859-1"));
            } else {
                // 火狐或者其他的浏览器
                response.addHeader("content-disposition",
                        "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置json返回
     *
     * @param response
     * @param json
     */
    public static boolean setResponseJson(HttpServletResponse response, String json) {
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json;charset=utf-8");
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.println(json);
            } catch (IOException ex) {
                return false;
            } finally {
                if(null != out)
                    out.close();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
