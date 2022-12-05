package com.augurit.agcloud.agcom.agsupport.sc.dir.util;

import com.augurit.agcloud.agcom.agsupport.util.HttpClientUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


/**
 * @FileName : ReadUrlUtil.java
 * @Author : 陈俊杰
 * @CreaterDate : 2012-05-21
 * @Description : ReadUrlUtil工具类
 * @Version : 1.0
 * @Modify :
 * @Copyright : Copyright (c) 2012 广州奥格智能科技有限公司
 */
public class ReadUrlUtil {
    public static void main(String args[]) {
        ReadUrlUtil readUrlUtil = new ReadUrlUtil();
        Map<String, String> map = queryOGC_WFSLayerTable("http://192.168.30.195:6080/arcgis/services/TESTZ/MapServer/WFSServer?request=GetCapabilities&service=WFS");
        for (String string : map.keySet()) {
            System.out.println("===>" + string + "=====" + map.get(string));
        }
        queryWfsFeatureType("http://192.168.19.6:8180/geoserver/luogang/ows?service=wfs&request=GetFeature&version=1.0.0&typename=polygon1&featureID=1&outputFormat=json");
    }

    /**
     * 根据URL地址获取返回的XML文件字符串
     *
     * @param urlStr
     * @return
     */
    public static String getStringByUrl(String urlStr) {
        String result = HttpClientUtil.get(urlStr);
    /*    URL url = null;
        InputStream urlStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder sb = new StringBuilder();
        try {
            url = new URL(urlStr);
            URLConnection URLconnection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
            // 设置获取连接超时时间
            httpConnection.setConnectTimeout(5000);
            // 设置读取返回数据超时时间
            httpConnection.setReadTimeout(4000);
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                urlStream = httpConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(urlStream, "utf-8"));
                String sCurrentLine = null;
                while ((sCurrentLine = bufferedReader.readLine()) != null) {
                    if (!sCurrentLine.trim().startsWith("<"))
                        sb.append(" ").append(sCurrentLine);
                    else
                        sb.append(sCurrentLine);
                }
            } else {
                System.err.println("连接失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (urlStream != null) {
                    urlStream.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        return result;
    }

    /**
     * 根据URL地址查询WFS的字段信息
     *
     * @param urlStr
     * @return
     */
    public static Map<String, String> queryWfsField(String urlStr) {
        Document document = null;
        try {
            String xmlStr = getStringByUrl(urlStr);
            // 把字符串转化成xml文档
            document = DocumentHelper.parseText(new String(xmlStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        return traverseWFSXMLFile(root, map);
    }

    /**
     * 解析XML文件中的WFS字段信息
     *
     * @param rootElement
     * @param map
     * @return
     */
    private static Map<String, String> traverseWFSXMLFile(Element rootElement, Map<String, String> map) {
        String splitStr = "~_~";
        List<Element> elements = rootElement.elements();
        //有子元素
        if (elements.size() != 0) {
            for (Iterator<Element> it = elements.iterator(); it.hasNext(); ) {
                Element element = it.next();
                if (element.getName().equalsIgnoreCase("sequence")) {
                    List<Element> childElements = element.elements();
                    for (Iterator<Element> iterator = childElements.iterator(); iterator.hasNext(); ) {
                        Element childElement = iterator.next();
                        if (childElement.getName().equalsIgnoreCase("element")) {
                            String key = childElement.attributeValue("name");
                            String value = "";
                            if (childElement.attributeValue("type") != null) {
                                value = childElement.attributeValue("type");
                            } else {
                                List<Element> simpleTypeElements = childElement.elements();
                                for (Element simpleTypeElement : simpleTypeElements) {
                                    if (simpleTypeElement.getName().equalsIgnoreCase("simpleType")) {
                                        List<Element> restrictionElements = simpleTypeElement.elements();
                                        for (Element restrictionElement : restrictionElements) {
                                            if (restrictionElement.getName().equalsIgnoreCase("restriction")) {
                                                value = restrictionElement.attributeValue("base");
                                            }
                                        }
                                    }
                                }
                            }
                            map.put(key, value);
                        }
                    }
                }
                //递归遍历
                traverseWFSXMLFile(element, map);
            }
        }
        return map;
    }

    /**
     * 根据URL地址查询WFS的主键字段
     *
     * @param urlStr
     * @return
     */
    public static String queryWfsKeyField(String urlStr) {
        Document document = null;
        try {
            String xmlStr = getStringByUrl(urlStr);
            // 把字符串转化成xml文档
            document = DocumentHelper.parseText(new String(xmlStr));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        LinkedHashMap<String, Map<String, String>> map = new LinkedHashMap<String, Map<String, String>>();
        getWfsFields(root, map);
        for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
            Map<String, String> tempMap = entry.getValue();
            String minOccurs = tempMap.get("minOccurs");
            String nillable = tempMap.get("nillable");
            if ("1".equals(minOccurs) && "false".equals(nillable)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * 根据URL地址查询MapServer的主键字段名称、别名
     *
     * @param urlStr
     * @return
     */
    public static String queryMapServerKeyField(String urlStr) {
        StringBuffer sb = new StringBuffer();
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            String xmlStr = getStringByUrl(url).trim();

            JSONObject jsonObj = JSONObject.fromObject(xmlStr);
            Object fieldsObj = jsonObj.get("fields");
            if (fieldsObj != null && !"null".equals(fieldsObj) && !"".equals(fieldsObj) && !fieldsObj.equals(JSONNull.getInstance())) {
                JSONArray layArray = (JSONArray) fieldsObj;
                for (int i = 0; i < layArray.size(); i++) {
                    String type = layArray.getJSONObject(i).get("type").toString();
                    if ("esriFieldTypeOID".equals(type)) {
                        sb.append(layArray.getJSONObject(i).get("name").toString() + ",");
                        sb.append(layArray.getJSONObject(i).get("alias").toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static Map<String, Map<String, String>> getWfsFields(Element rootElement, Map<String, Map<String, String>> map) {
        List<Element> elements = rootElement.elements();
        //有子元素
        if (elements.size() != 0) {
            for (Iterator<Element> it = elements.iterator(); it.hasNext(); ) {
                Element element = it.next();
                if (element.getName().equalsIgnoreCase("sequence")) {
                    List<Element> childElements = element.elements();
                    for (Iterator<Element> iterator = childElements.iterator(); iterator.hasNext(); ) {
                        Element childElement = iterator.next();
                        if (childElement.getName().equalsIgnoreCase("element")) {
                            Map<String, String> temp = new HashMap<String, String>();
                            String key = childElement.attributeValue("name");
                            String value = "";
                            if (childElement.attributeValue("type") != null) {
                                value = childElement.attributeValue("type");
                            } else {
                                List<Element> simpleTypeElements = childElement.elements();
                                for (Element simpleTypeElement : simpleTypeElements) {
                                    if (simpleTypeElement.getName().equalsIgnoreCase("simpleType")) {
                                        List<Element> restrictionElements = simpleTypeElement.elements();
                                        for (Element restrictionElement : restrictionElements) {
                                            if (restrictionElement.getName().equalsIgnoreCase("restriction")) {
                                                value = restrictionElement.attributeValue("base");
                                            }
                                        }
                                    }
                                }
                            }
                            temp.put("name", key);
                            temp.put("type", value);
                            String maxOccurs = "";
                            if (childElement.attributeValue("maxOccurs") != null) {
                                maxOccurs = childElement.attributeValue("maxOccurs");
                            }
                            temp.put("maxOccurs", maxOccurs);
                            String minOccurs = "";
                            if (childElement.attributeValue("minOccurs") != null) {
                                minOccurs = childElement.attributeValue("minOccurs");
                            }
                            temp.put("minOccurs", minOccurs);
                            String nillable = "";
                            if (childElement.attributeValue("nillable") != null) {
                                nillable = childElement.attributeValue("nillable");
                            }
                            temp.put("nillable", nillable);
                            map.put(key, temp);
                        }
                    }
                }
                //递归遍历
                getWfsFields(element, map);
            }
        }
        return map;
    }

    /**
     * GeoServer根据URL地址查询WFS的图层要素
     *
     * @param urlStr
     * @return
     */
    public static String queryWfsFeatureType(String urlStr) {
        String type = "";
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            JSONObject dataObj = JSONObject.fromObject(getStringByUrl(url));
            type = (((JSONObject)((JSONObject)((JSONArray)dataObj.get("features")).get(0)).get("geometry")).get("type")).toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return type;
    }

    /**
     * 根据WMTS服务地址解析XML，获取服务名称
     *
     * @param urlStr
     * @return
     */
    public static String queryWMTSServiceName(String urlStr) {
        String serviceName = "";
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            Document document = null;
            String xmlStr = getStringByUrl(url);
            xmlStr = xmlStr.replaceAll("\"xmlns", "\" xmlns").replaceAll("\"version", "\" version");
            // 把字符串转化成xml文档
            document = DocumentHelper.parseText(xmlStr);
            List<Element> elements = document.getRootElement().elements();
            //有子元素
            if (elements.size() != 0) {
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("ServiceIdentification")) {
                        List<Element> setElements = element.elements();
                        for (Element setElement : setElements) {
                            if (setElement.getName().equalsIgnoreCase("Title")) {
                                serviceName = setElement.getText();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceName;
    }

    /**
     * 根据WMTS服务地址,查询图层表信息
     *
     * @param urlStr
     * @return
     */
    public static Map<String, String> queryWMTSLayerTable(String urlStr) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            Document document = null;
            String xmlStr = getStringByUrl(url);
            //xmlStr = xmlStr.replaceAll("\"xmlns", "\" xmlns").replaceAll("\"version", "\" version");
            // 把字符串转化成xml文档
            document = DocumentHelper.parseText(xmlStr);
            List<Element> elements = document.getRootElement().elements();
            //有子元素
            if (elements.size() != 0) {
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("Contents")) {
                        List<Element> layerElements = element.elements();
                        for (Element layerElement : layerElements) {
                            if (layerElement.getName().equalsIgnoreCase("Layer")) {
                                List<Element> tableElements = layerElement.elements();
                                String key = "";
                                String value = "";
                                for (Element tableElement : tableElements) {
                                    if (tableElement.getName().equalsIgnoreCase("Identifier")) {
                                        key = tableElement.getText();
                                    }
                                    if (tableElement.getName().equalsIgnoreCase("Title")) {
                                        value = tableElement.getText();
                                    }
                                    if (StringUtils.isNotBlank(key)) {
                                        rtnMap.put(key, value);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnMap;
    }

    /**
     * 根据WMTS服务地址,查询指定图层表的名称
     *
     * @param urlStr
     * @return
     */
    public static String queryWMTSLayerTableName(String urlStr, String layerTable) {
        String layerTableName = layerTable;
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            Document document = null;
            String xmlStr = getStringByUrl(url);
            //xmlStr = xmlStr.replaceAll("\"xmlns", "\" xmlns").replaceAll("\"version", "\" version");
            // 把字符串转化成xml文档
            document = DocumentHelper.parseText(xmlStr);
            List<Element> elements = document.getRootElement().elements();
            //有子元素
            if (elements.size() != 0) {
                OK:
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("Contents")) {
                        List<Element> layerElements = element.elements();
                        for (Element layerElement : layerElements) {
                            if (layerElement.getName().equalsIgnoreCase("Layer")) {
                                List<Element> linkElements = layerElement.elements();
                                for (Element linkElement : linkElements) {
                                    if (linkElement.getName().equalsIgnoreCase("Identifier")) {
                                        if (linkElement.getText() != null && linkElement.getText().equals(layerTable)) {
                                            List<Element> titleElements = layerElement.elements();
                                            for (Element titleElement : titleElements) {
                                                if (titleElement.getName().equalsIgnoreCase("Title")) {
                                                    layerTableName = titleElement.getText();
                                                    break OK;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return layerTableName;
    }

    /**
     * 根据WMTS服务地址,查询指定图层表的原点
     *
     * @param urlStr
     * @return
     */
    public static String getWMTSLayerOrigin(String urlStr, String layerTable) {
        String layerOrigin = "";
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            Document document = null;
            String xmlStr = getStringByUrl(url);
            //xmlStr = xmlStr.replaceAll("\"xmlns", "\" xmlns").replaceAll("\"version", "\" version");
            // 把字符串转化成xml文档
            document = DocumentHelper.parseText(xmlStr);
            List<Element> elements = document.getRootElement().elements();
            //有子元素
            if (elements.size() != 0) {
                OK:
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("Contents")) {
                        List<Element> layerElements = element.elements();
                        for (Element layerElement : layerElements) {
                            if (layerElement.getName().equalsIgnoreCase("Layer")) {
                                List<Element> linkElements = layerElement.elements();
                                for (Element linkElement : linkElements) {
                                    if (linkElement.getName().equalsIgnoreCase("Identifier")) {
                                        if (linkElement.getText() != null && linkElement.getText().equals(layerTable)) {
                                            List<Element> titleElements = layerElement.elements();
                                            for (Element titleElement : titleElements) {
                                                if (titleElement.getName().equalsIgnoreCase("WGS84BoundingBox")) {
                                                    List<Element> originElements = titleElement.elements();
                                                    String lower = "";
                                                    String uper = "";
                                                    for (Element originElement : originElements) {
                                                        if (originElement.getName().equalsIgnoreCase("LowerCorner")) {
                                                            lower = originElement.getText();
                                                        }
                                                        if (originElement.getName().equalsIgnoreCase("UpperCorner")) {
                                                            uper = originElement.getText();
                                                        }
                                                    }
                                                    if (StringUtils.isNotEmpty(lower) && StringUtils.isNotEmpty(uper)) {
                                                        layerOrigin = lower.split(" ")[0] + "," + uper.split(" ")[1];
                                                    }
                                                    break OK;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return layerOrigin;
    }

    /**
     * 根据WMTS服务地址解析XML，获取指定图层表坐标系统投影信息
     *
     * @param urlStr
     * @param layerTable
     * @return
     */
    public static List<String> queryWMTSLayerTableSrs(String urlStr, String layerTable) {
        List<String> rtnList = new ArrayList<String>();
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            Document document = null;
            String xmlStr = getStringByUrl(url);
            //xmlStr = xmlStr.replaceAll("\"xmlns", "\" xmlns").replaceAll("\"version", "\" version");
            // 把字符串转化成xml文档
            document = DocumentHelper.parseText(xmlStr);
            List<Element> elements = document.getRootElement().elements();
            //有子元素
            if (elements.size() != 0) {
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("Contents")) {
                        List<Element> layerElements = element.elements();
                        for (Element layerElement : layerElements) {
                            if (layerElement.getName().equalsIgnoreCase("Layer")) {
                                List<Element> linkElements = layerElement.elements();
                                for (Element linkElement : linkElements) {
                                    if (linkElement.getName().equalsIgnoreCase("Identifier")) {
                                        if (linkElement.getText() != null && linkElement.getText().equals(layerTable)) {
                                            List<Element> matrixElements = layerElement.elements();
                                            for (Element matrixElement : matrixElements) {
                                                if (matrixElement.getName().equalsIgnoreCase("TileMatrixSetLink")) {
                                                    List<Element> srsElements = matrixElement.elements();
                                                    for (Element srsElement : srsElements) {
                                                        if (srsElement.getName().equalsIgnoreCase("TileMatrixSet")) {
                                                            rtnList.add(srsElement.getText());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnList;
    }

    /**
     * 根据WMTS服务地址解析XML，获取指定坐标系统投影的瓦片大小
     *
     * @param urlStr
     * @param srs
     * @return
     */
    public static String queryWMTSTileSize(String urlStr, String srs) {
        String tileSize = "256";
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            Document document = null;
            String xmlStr = getStringByUrl(url);
            //xmlStr = xmlStr.replaceAll("\"xmlns", "\" xmlns").replaceAll("\"version", "\" version");
            // 把字符串转化成xml文档
            document = DocumentHelper.parseText(xmlStr);
            List<Element> elements = document.getRootElement().elements();
            //有子元素
            if (elements.size() != 0) {
                OK:
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("Contents")) {
                        List<Element> setElements = element.elements();
                        for (Element setElement : setElements) {
                            if (setElement.getName().equalsIgnoreCase("TileMatrixSet")) {
                                List<Element> identElements = setElement.elements();
                                for (Element identElement : identElements) {
                                    if (identElement.getName().equalsIgnoreCase("Identifier")) {
                                        if (identElement.getText() != null && identElement.getText().equals(srs)) {
                                            List<Element> matrixElements = setElement.elements();
                                            for (Element matrixElement : matrixElements) {
                                                if (matrixElement.getName().equalsIgnoreCase("TileMatrix")) {
                                                    List<Element> tileSizeElements = matrixElement.elements();
                                                    for (Element tileSizeElement : tileSizeElements) {
                                                        if (tileSizeElement.getName().equalsIgnoreCase("TileWidth")) {
                                                            tileSize = tileSizeElement.getText();
                                                            break OK;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tileSize;
    }

    /**
     * 根据WMTS服务地址解析XML，获取指定坐标系统投影的瓦片大小 和 地图偏移级别 2016.11.10 增加 chendingxing
     *
     * @param urlStr
     * @param srs
     * @return
     */
    public static String queryWMTSTileSizeAndZoomOffset(String urlStr, String srs) {
        String tileSizeAndZoomOffset = null;
        String tileSize = "256";
        String zoomOffset = "0";
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            Document document = null;
            String xmlStr = getStringByUrl(url);
            //xmlStr = xmlStr.replaceAll("\"xmlns", "\" xmlns").replaceAll("\"version", "\" version");
            // 把字符串转化成xml文档
            document = DocumentHelper.parseText(xmlStr);
            List<Element> elements = document.getRootElement().elements();
            //有子元素
            if (elements.size() != 0) {
                OK:
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("Contents")) {
                        List<Element> setElements = element.elements();
                        for (Element setElement : setElements) {
                            if (setElement.getName().equalsIgnoreCase("TileMatrixSet")) {
                                List<Element> identElements = setElement.elements();
                                for (Element identElement : identElements) {
                                    if (identElement.getName().equalsIgnoreCase("Identifier")) {
                                        if (identElement.getText() != null && identElement.getText().equals(srs)) {
                                            List<Element> matrixElements = setElement.elements();
                                            for (Element matrixElement : matrixElements) {
                                                if (matrixElement.getName().equalsIgnoreCase("TileMatrix")) {
                                                    List<Element> tempElements = matrixElement.elements();
                                                    for (Element tempElement : tempElements) {
                                                        if (tempElement.getName().equalsIgnoreCase("Identifier")) {
                                                            if ("c".equalsIgnoreCase(srs) || "w".equalsIgnoreCase(srs)) {
                                                                //天地图级别默认比其他图层大一级
                                                                String text = tempElement.getText();
                                                                zoomOffset = Integer.valueOf(text) - 1 + "";
                                                            } else {
                                                                //OGC格式需要处理
                                                                String text = tempElement.getText();
                                                                if (text.startsWith("EPSG")) {
                                                                    String ts[] = text.split(":");
                                                                    text = ts[ts.length - 1];
                                                                }
                                                                zoomOffset = text;
                                                            }
                                                        }
                                                        if (tempElement.getName().equalsIgnoreCase("TileWidth")) {
                                                            tileSize = tempElement.getText();
                                                            break OK;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                tileSizeAndZoomOffset = tileSize + "," + zoomOffset;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tileSizeAndZoomOffset;
    }

    /**
     * 根据WMTS服务地址解析XML，获取指定图层表图片后缀信息
     *
     * @param urlStr
     * @param layerTable
     * @return
     */
    public static List<String> queryWMTSLayerTableSuffix(String urlStr, String layerTable) {
        List<String> rtnList = new ArrayList<String>();
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            Document document = null;
            String xmlStr = getStringByUrl(url);
            //xmlStr = xmlStr.replaceAll("\"xmlns", "\" xmlns").replaceAll("\"version", "\" version");
            // 把字符串转化成xml文档
            document = DocumentHelper.parseText(xmlStr);
            List<Element> elements = document.getRootElement().elements();
            //有子元素
            if (elements.size() != 0) {
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("Contents")) {
                        List<Element> layerElements = element.elements();
                        for (Element layerElement : layerElements) {
                            if (layerElement.getName().equalsIgnoreCase("Layer")) {
                                List<Element> linkElements = layerElement.elements();
                                for (Element linkElement : linkElements) {
                                    if (linkElement.getName().equalsIgnoreCase("Identifier")) {
                                        if (linkElement.getText() != null && linkElement.getText().equals(layerTable)) {
                                            List<Element> suffixElements = layerElement.elements();
                                            for (Element suffixElement : suffixElements) {
                                                if (suffixElement.getName().equalsIgnoreCase("Format")) {
                                                    rtnList.add(suffixElement.getText());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnList;
    }

    /**
     * 根据WMS服务地址解析XML，获取WMS服务的图层表名
     *
     * @param urlStr
     * @return
     */
    public static Map<String, String> queryWMSLayerTable(String urlStr) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            Document document = null;
            String xmlStr = getStringByUrl(url);
            document = DocumentHelper.parseText(xmlStr);
            List<Element> elements = document.getRootElement().elements();
            if (elements != null && elements.size() > 0) {
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("Capability")) {
                        List<Element> layersElements = element.elements();
                        if (layersElements != null && layersElements.size() > 0) {
                            for (Element layersElement : layersElements) {
                                if (layersElement.getName().equalsIgnoreCase("Layer")) {
                                    List<Element> layerElements = layersElement.elements();
                                    if (layerElements != null && layerElements.size() > 0) {
                                        for (Element layerElement : layerElements) {
                                            if (layerElement.getName().equalsIgnoreCase("Layer")) {
                                                List<Element> childElements = layerElement.elements();
                                                if (childElements != null && childElements.size() > 0) {
                                                    String key = "";
                                                    String value = "";
                                                    for (Element childElement : childElements) {
                                                        if (childElement.getName().equalsIgnoreCase("Name")) {
                                                            key = childElement.getText();
                                                        }
                                                        if (childElement.getName().equalsIgnoreCase("Title")) {
                                                            value = childElement.getText();
                                                        }

                                                        if (StringUtils.isNotBlank(key)) {
                                                            rtnMap.put(key, value);
                                                        }
                                                        if (childElement.getName().equalsIgnoreCase("Layer")) {
                                                            List<Element> cldElements = childElement.elements();
                                                            if (cldElements != null && cldElements.size() > 0) {
                                                                String ckey = "";
                                                                String cvalue = "";
                                                                for (Element cldElement : cldElements) {
                                                                    if (cldElement.getName().equalsIgnoreCase("Name")) {
                                                                        ckey = cldElement.getText();
                                                                    }
                                                                    if (cldElement.getName().equalsIgnoreCase("Title")) {
                                                                        cvalue = cldElement.getText();
                                                                    }
                                                                    if (StringUtils.isNotBlank(ckey)) {
                                                                        rtnMap.put(ckey, cvalue);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnMap;
    }


    /**
     * 根据rest服务地址解析Json，获取WMS服务的图层表名
     *
     * @param urlStr
     * @return
     */
    public static Map<String, String> queryWMSLayerTableByRest(String urlStr) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            String xmlStr = getStringByUrl(url).trim();

            JSONObject jsonObj = JSONObject.fromObject(xmlStr);
            Object layersObj = jsonObj.get("layers");
            if (layersObj != null && !"null".equals(layersObj) && !"".equals(layersObj) && !layersObj.equals(JSONNull.getInstance())) {
                JSONArray layArray = (JSONArray) layersObj;
                for (int i = 0; i < layArray.size(); i++) {
                    if ("null".equals(layArray.getJSONObject(i).get("subLayerIds").toString()) || layArray.getJSONObject(i).get("subLayerIds").toString() == null) {
                        rtnMap.put(layArray.getJSONObject(i).get("id").toString(), layArray.getJSONObject(i).get("name").toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnMap;
    }

    /**
     * 根据WMS服务地址及图层表解析XML，获取WMS服务图层图例的URL
     *
     * @param urlStr
     * @return
     */
    public static String queryWMSLegendUrl(String urlStr, String layerTable) {
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            Document document = null;
            String xmlStr = getStringByUrl(url);
            document = DocumentHelper.parseText(xmlStr);
            List<Element> elements = document.getRootElement().elements();
            if (elements != null && elements.size() > 0) {
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("Capability")) {
                        List<Element> layersElements = element.elements();
                        if (layersElements != null && layersElements.size() > 0) {
                            for (Element layersElement : layersElements) {
                                if (layersElement.getName().equalsIgnoreCase("Layer")) {
                                    List<Element> layerElements = layersElement.elements();
                                    if (layerElements != null && layerElements.size() > 0) {
                                        for (Element layerElement : layerElements) {
                                            if (layerElement.getName().equalsIgnoreCase("Layer")) {
                                                List<Element> childElements = layerElement.elements();
                                                if (childElements != null && childElements.size() > 0) {
                                                    for (Element childElement : childElements) {
                                                        if (childElement.getName().equalsIgnoreCase("Name")) {
                                                            if (childElement.getText() != null && layerTable.equals(childElement.getText())) {
                                                                List<Element> childEles = layerElement.elements();
                                                                if (childEles != null && childEles.size() > 0) {
                                                                    for (Element childEle : childEles) {
                                                                        if (childEle.getName().equalsIgnoreCase("Style")) {
                                                                            List<Element> styleEles = childEle.elements();
                                                                            if (styleEles != null && styleEles.size() > 0) {
                                                                                for (Element styleEle : styleEles) {
                                                                                    if (styleEle.getName().equalsIgnoreCase("LegendURL")) {
                                                                                        List<Element> urlEles = styleEle.elements();
                                                                                        if (urlEles != null && urlEles.size() > 0) {
                                                                                            for (Element urlEle : urlEles) {
                                                                                                if (urlEle.getName().equalsIgnoreCase("OnlineResource")) {
                                                                                                    //System.out.println("=============>"+urlEle.attribute("href").getValue());
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据栅格瓦片服务URL解析config文件，获取图层原点
     *
     * @param urlStr
     * @return
     */
    public static Map<String, String> loadGridLayerConfig(String urlStr) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        String origin = "";
        String tileSize = "";
        String suffix = "";
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            Document document = null;
            String xmlStr = getStringByUrl(url);
            xmlStr = xmlStr.replaceAll("\"xmlns", "\" xmlns").replaceAll("\"version", "\" version");
            // 把字符串转化成xml文档
            document = DocumentHelper.parseText(xmlStr);
            List<Element> elements = document.getRootElement().elements();
            //有子元素
            if (elements.size() != 0) {
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("TileCacheInfo")) {
                        List<Element> infoElements = element.elements();
                        if (infoElements != null && infoElements.size() > 0) {
                            for (Element infoElement : infoElements) {
                                //获取图层原点
                                if (infoElement.getName().equalsIgnoreCase("TileOrigin")) {
                                    List<Element> xyElements = infoElement.elements();
                                    if (xyElements != null && xyElements.size() > 0) {
                                        for (Element xyElement : xyElements) {
                                            if (xyElement.getName().equalsIgnoreCase("x")) {
                                                origin += xyElement.getText();
                                            }
                                            if (xyElement.getName().equalsIgnoreCase("y")) {
                                                origin += "," + xyElement.getText();
                                            }
                                        }
                                    }
                                }
                                //获取瓦片大小
                                if (infoElement.getName().equalsIgnoreCase("TileCols")) {
                                    tileSize = infoElement.getText();
                                }
                            }
                        }
                    }
                    //获取后缀
                    if (element.getName().equalsIgnoreCase("TileImageInfo")) {
                        List<Element> imageInfoElements = element.elements();
                        if (imageInfoElements != null && imageInfoElements.size() > 0) {
                            for (Element imageInfoElement : imageInfoElements) {
                                if (imageInfoElement.getName().equalsIgnoreCase("CacheTileFormat")) {
                                    suffix = imageInfoElement.getText();
                                    if (suffix.toLowerCase().indexOf("png") != -1) {
                                        suffix = "png";
                                    } else if (suffix.toLowerCase().indexOf("gif") != -1) {
                                        suffix = "gif";
                                    } else if (suffix.toLowerCase().indexOf("jpg") != -1) {
                                        suffix = "jpg";
                                    } else if (suffix.toLowerCase().indexOf("jpeg") != -1) {
                                        suffix = "jpeg";
                                    }
                                }
                            }
                        }
                    }
                }
            }
            rtnMap.put("origin", origin);
            rtnMap.put("tileSize", tileSize);
            rtnMap.put("suffix", suffix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnMap;
    }

    /**
     * 根据WMTS服务地址解析XML，获取指定图层表坐标系统投影下拉列表
     *
     * @param urlStr
     * @return
     */
    public static List<String> queryWMSLayerSrs(String urlStr) {
        List<String> rtnList = new ArrayList<String>();
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            Document document = null;
            String xmlStr = getStringByUrl(url);
            document = DocumentHelper.parseText(xmlStr);
            List<Element> elements = document.getRootElement().elements();
            if (elements != null && elements.size() > 0) {
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("Capability")) {
                        List<Element> layersElements = element.elements();
                        if (layersElements != null && layersElements.size() > 0) {
                            for (Element layersElement : layersElements) {
                                if (layersElement.getName().equalsIgnoreCase("Layer")) {
                                    List<Element> layerElements = layersElement.elements();
                                    if (layerElements != null && layerElements.size() > 0) {
                                        for (Element layerElement : layerElements) {
                                            if (layerElement.getName().equalsIgnoreCase("Layer")) {
                                                List<Element> childElements = layerElement.elements();
                                                if (childElements != null && childElements.size() > 0) {
                                                    for (Element childElement : childElements) {
                                                        if (childElement.getName().equalsIgnoreCase("CRS") || childElement.getName().equalsIgnoreCase("SRS")) {
                                                            rtnList.add(childElement.getText());
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnList;
    }

    /**
     * 根据OGC_WFS服务地址解析XML，获取OGC_WFS服务的图层表名
     *
     * @param urlStr
     * @return
     */
    public static Map<String, String> queryOGC_WFSLayerTable(String urlStr) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            Document document = null;
            String xmlStr = getStringByUrl(url);
            System.out.println("=========>" + urlStr);
            System.out.println("=========>" + xmlStr);
            document = DocumentHelper.parseText(xmlStr);
            List<Element> elements = document.getRootElement().elements();
            if (elements != null && elements.size() > 0) {
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("FeatureTypeList")) {
                        List<Element> layersElements = element.elements();
                        if (layersElements != null && layersElements.size() > 0) {
                            for (Element layersElement : layersElements) {
                                if (layersElement.getName().equalsIgnoreCase("FeatureType")) {
                                    List<Element> layerElements = layersElement.elements();
                                    if (layerElements != null && layerElements.size() > 0) {
                                        String key = "";
                                        String value = "";
                                        for (Element layerElement : layerElements) {
                                            if (layerElement.getName().equalsIgnoreCase("Name")) {
                                                key = layerElement.getText();
                                            }
                                            if (layerElement.getName().equalsIgnoreCase("Title")) {
                                                value = layerElement.getText();
                                            }
                                            System.out.println("=========>" + key + "====" + value);
                                            rtnMap.put(key, value);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return rtnMap;
    }

    /**
     * 根据OGC_WFS服务地址解析XML，获取指定图层表坐标系统投影下拉列表
     *
     * @param urlStr
     * @return
     */
    public static List<String> queryOGC_WFSLayerSrs(String urlStr) {
        List<String> rtnList = new ArrayList<String>();
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            Document document = null;
            String xmlStr = getStringByUrl(url);

            document = DocumentHelper.parseText(xmlStr);
            List<Element> elements = document.getRootElement().elements();
            if (elements != null && elements.size() > 0) {
                OK:
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("FeatureTypeList")) {
                        List<Element> layersElements = element.elements();
                        if (layersElements != null && layersElements.size() > 0) {
                            for (Element layersElement : layersElements) {
                                if (layersElement.getName().equalsIgnoreCase("FeatureType")) {
                                    List<Element> layerElements = layersElement.elements();
                                    if (layerElements != null && layerElements.size() > 0) {
                                        for (Element layerElement : layerElements) {
                                            if (layerElement.getName().equalsIgnoreCase("DefaultSRS") ||
                                                    layerElement.getName().equalsIgnoreCase("DefaultCRS") ||
                                                    layerElement.getName().equalsIgnoreCase("OtherSRS") ||
                                                    layerElement.getName().equalsIgnoreCase("SRS")) {
                                                String srs = layerElement.getText();
                                                if (srs.startsWith("urn:ogc:def:crs")) {
                                                    String[] strs = srs.split(":");
                                                    rtnList.add("EPSG:" + strs[strs.length - 1]);
                                                } else {
                                                    rtnList.add(srs);
                                                }
                                            }
                                        }
                                    }
                                    break OK;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnList;
    }

    /**
     * 根据OGC_WFS服务地址解析XML，获取指定图层表坐标系统投影下拉列表
     *
     * @param urlStr
     * @return
     */
    public static Map<String, String> queryOGC_WFSLayerNsXmlAndSpName(String urlStr) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            Document document = null;
            String xmlStr = getStringByUrl(url);
            document = DocumentHelper.parseText(xmlStr);
            Element rootElement = document.getRootElement();
            String targetNamespace = rootElement.attributeValue("targetNamespace");
            rtnMap.put("nsXml", rootElement.getNamespaceForURI(targetNamespace).asXML());
            List<Element> elements = rootElement.elements();
            if (elements != null && elements.size() > 0) {
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("complexType")) {
                        List<Element> complexTypeElements = element.elements();
                        if (complexTypeElements != null && complexTypeElements.size() > 0) {
                            for (Element complexTypeElement : complexTypeElements) {
                                if (complexTypeElement.getName().equalsIgnoreCase("complexContent")) {
                                    List<Element> complexContentElements = complexTypeElement.elements();
                                    if (complexContentElements != null && complexContentElements.size() > 0) {
                                        for (Element complexContentElement : complexContentElements) {
                                            if (complexContentElement.getName().equalsIgnoreCase("extension")) {
                                                List<Element> extensionElements = complexContentElement.elements();
                                                if (extensionElements != null && extensionElements.size() > 0) {
                                                    for (Element extensionElement : extensionElements) {
                                                        if (extensionElement.getName().equalsIgnoreCase("sequence")) {
                                                            List<Element> sequenceElements = extensionElement.elements();
                                                            if (sequenceElements != null && sequenceElements.size() > 0) {
                                                                for (Element sequenceElement : sequenceElements) {
                                                                    if (sequenceElement.getName().equalsIgnoreCase("element")) {
                                                                        String type = sequenceElement.attributeValue("type");
                                                                        if (StringUtils.isNotEmpty(type)) {
                                                                            if ("gml".equals(type.split(":")[0].toLowerCase())) {
                                                                                rtnMap.put("spName", sequenceElement.attributeValue("name"));
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnMap;
    }

    /**
     * 根据WCS服务地址解析XML，获取WCS服务的图层表名
     *
     * @param urlStr
     * @return
     */
    public static Map<String, String> queryWCSLayerTable(String urlStr) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            Document document = null;
            String xmlStr = getStringByUrl(url);
            document = DocumentHelper.parseText(xmlStr);
            List<Element> elements = document.getRootElement().elements();
            if (elements != null && elements.size() > 0) {
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("ContentMetadata")) {
                        List<Element> metadataElements = element.elements();
                        if (metadataElements != null && metadataElements.size() > 0) {
                            for (Element metadataElement : metadataElements) {
                                if (metadataElement.getName().equalsIgnoreCase("CoverageOfferingBrief")) {
                                    List<Element> coverageElements = metadataElement.elements();
                                    if (coverageElements != null && coverageElements.size() > 0) {
                                        String key = "";
                                        String value = "";
                                        for (Element coverageElement : coverageElements) {
                                            if (coverageElement.getName().equalsIgnoreCase("name")) {
                                                key = coverageElement.getText();
                                            }
                                            if (coverageElement.getName().equalsIgnoreCase("label")) {
                                                value = coverageElement.getText();
                                            }
                                            rtnMap.put(key, value);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnMap;
    }

    /**
     * 解析XMl地图范围
     *
     * @param attachment
     * @return
     */
    public static String loadMapextent(Attachment attachment, int size) {
        String mapextent = "";
        try {
            Document document = DocumentHelper.parseText(getAttachmentString(attachment, size));
            List<Element> elements = document.getRootElement().elements();
            if (elements != null && elements.size() > 0) {
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("XMin")) {
                        mapextent += element.getText() + ",";
                    } else if (element.getName().equalsIgnoreCase("YMin")) {
                        mapextent += element.getText() + ",";
                    } else if (element.getName().equalsIgnoreCase("XMax")) {
                        mapextent += element.getText() + ",";
                    } else if (element.getName().equalsIgnoreCase("YMax")) {
                        mapextent += element.getText() + ",";
                    }
                }
                mapextent = mapextent.substring(0, mapextent.length() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapextent;
    }

    /**
     * 解析XMl读取分辨率
     *
     * @param attachment
     * @return
     */
    public static String loadResolution(Attachment attachment, int size) {
        String resolution = "";
        try {
            Document document = DocumentHelper.parseText(getAttachmentString(attachment, size));
            List<Element> elements = document.getRootElement().elements();
            if (elements != null && elements.size() > 0) {
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("TileCacheInfo")) {
                        List<Element> infoElements = element.elements();
                        if (infoElements != null && infoElements.size() > 0) {
                            for (Element infoElement : infoElements) {
                                if (infoElement.getName().equalsIgnoreCase("LODInfos")) {
                                    List<Element> lodInfosElements = infoElement.elements();
                                    if (lodInfosElements != null && lodInfosElements.size() > 0) {
                                        for (Element lodInfosElement : lodInfosElements) {
                                            if (lodInfosElement.getName().equalsIgnoreCase("LODInfo")) {
                                                List<Element> lodInfoElements = lodInfosElement.elements();
                                                if (lodInfoElements != null && lodInfoElements.size() > 0) {
                                                    for (Element lodInfoElement : lodInfoElements) {
                                                        if (lodInfoElement.getName().equalsIgnoreCase("Resolution")) {
                                                            resolution += lodInfoElement.getText() + ",";
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        resolution = resolution.substring(0, resolution.length() - 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resolution;
    }

    /**
     * 根据rest服务地址解析Json，获取MapServer服务的图层表名
     *
     * @param urlStr
     * @return
     */
    public static Map<String, String> queryMapServerLayerTableByRest(String urlStr) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            String xmlStr = getStringByUrl(url).trim();

            JSONObject jsonObj = JSONObject.fromObject(xmlStr);
            Object layersObj = jsonObj.get("layers");
            if (layersObj != null && !"null".equals(layersObj) && !"".equals(layersObj) && !layersObj.equals(JSONNull.getInstance())) {
                JSONArray layArray = (JSONArray) layersObj;
                for (int i = 0; i < layArray.size(); i++) {
                    if (layArray.getJSONObject(i).get("subLayerIds") != null) {
                        if ("null".equals(layArray.getJSONObject(i).get("subLayerIds").toString()) || layArray.getJSONObject(i).get("subLayerIds").toString() == null) {
                            rtnMap.put(layArray.getJSONObject(i).get("id").toString(), layArray.getJSONObject(i).get("name").toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnMap;
    }

    /**
     * 根据rest服务地址解析Json，获取MapServer服务的字段信息
     *
     * @param urlStr
     * @return
     */
    public static List<Map> queryMapServerFieldsByRest(String urlStr) {
        List<Map> fields = new ArrayList<>();
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            String xmlStr = getStringByUrl(url).trim();
            if (org.apache.commons.lang.StringUtils.isNotBlank(xmlStr)){
//                JSONObject jsonObj = JSONObject.fromObject(xmlStr);
//                Object fieldsObj = jsonObj.get("fields");
//                if (fieldsObj != null && !"null".equals(fieldsObj) && !"".equals(fieldsObj) && !fieldsObj.equals(JSONNull.getInstance())) {
//                    JSONArray layArray = (JSONArray) fieldsObj;
//                    for (int i = 0; i < layArray.size(); i++) {
//                        rtnMap.put(layArray.getJSONObject(i).get("name").toString(), layArray.getJSONObject(i).get("type").toString());
//                    }
//                }
                //2019-09-06 字段配置返回字段别名
                ObjectMapper mapper = new ObjectMapper();
                Map<String,Object> map = mapper.readValue(xmlStr, Map.class);
                fields = (ArrayList)map.get("fields");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fields;
    }

    /**
     * 根据rest服务地址解析Json，获取FeatureServer服务的图层表名
     *
     * @param urlStr
     * @return
     */
    public static Map<String, String> queryFeatureServerLayerTableByRest(String urlStr) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            String xmlStr = getStringByUrl(url).trim();

            JSONObject jsonObj = JSONObject.fromObject(xmlStr);
            Object layersObj = jsonObj.get("layers");
            if (layersObj != null && !"null".equals(layersObj) && !"".equals(layersObj) && !layersObj.equals(JSONNull.getInstance())) {
                JSONArray layArray = (JSONArray) layersObj;
                for (int i = 0; i < layArray.size(); i++) {
                    if (!"null".equals(layArray.getJSONObject(i).get("id").toString()) && layArray.getJSONObject(i).get("id").toString() != null) {
                        rtnMap.put(layArray.getJSONObject(i).get("id").toString(), layArray.getJSONObject(i).get("name").toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnMap;
    }

    /**
     * 根据rest服务地址解析Json，获取FeatureServer服务的字段信息
     *
     * @param urlStr
     * @return
     */
    public static List<Map> queryFeatureServerFieldsByRest(String urlStr) {
        List<Map> fields = new ArrayList<>();
        try {
            String url = new String(urlStr.getBytes("ISO-8859-1"), "UTF-8");
            String xmlStr = getStringByUrl(url).trim();

//            JSONObject jsonObj = JSONObject.fromObject(xmlStr);
//            Object fieldsObj = jsonObj.get("fields");
//            if (fieldsObj != null && !"null".equals(fieldsObj) && !"".equals(fieldsObj) && !fieldsObj.equals(JSONNull.getInstance())) {
//                JSONArray layArray = (JSONArray) fieldsObj;
//                for (int i = 0; i < layArray.size(); i++) {
//                    rtnMap.put(layArray.getJSONObject(i).get("name").toString(), layArray.getJSONObject(i).get("type").toString());
//                }
//            }
            //2019-09-06 字段配置返回字段别名
            if(org.apache.commons.lang.StringUtils.isNotBlank(xmlStr)) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String,Object> map = mapper.readValue(xmlStr, Map.class);
                fields = (ArrayList)map.get("fields");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fields;
    }

    /**
     * 读取长传配置文件，以字符串返回结果
     */
    public static String getAttachmentString(Attachment attachment, int size) {
        InputStream inStream = null;
        BufferedReader bufferedReader = null;
        String rtnString = "";
        try {
            inStream = new BufferedInputStream(new FileInputStream(attachment.getFile()), size);
            StringBuilder sb = new StringBuilder();
            bufferedReader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
            String sCurrentLine = null;
            while ((sCurrentLine = bufferedReader.readLine()) != null) {
                sb.append(sCurrentLine);
            }
            rtnString = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rtnString;
    }

    /**
     * 根据URL地址查询服务是否有误
     *
     * @param urlStr
     * @return
     */
    public static boolean checkService(String urlStr) {
        StringBuilder sb = new StringBuilder();
        URL url = null;
        InputStream urlStream = null;
        BufferedReader bufferedReader = null;
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            url = new URL(urlStr);
            URLConnection URLconnection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
            // 设置获取连接超时时间
            httpConnection.setConnectTimeout(3000);
            // 设置读取返回数据超时时间
            httpConnection.setReadTimeout(2000);
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                urlStream = httpConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(urlStream));
                String sCurrentLine = null;
                while ((sCurrentLine = bufferedReader.readLine()) != null) {
                    sb.append(sCurrentLine + "\r\n");
                }
                //不包含ExceptionReport 表示服务正常
                //System.out.println("url:" + urlStr + "服务正常");
//System.out.println("url:" + urlStr + "服务抛出异常");
                return sb.toString().indexOf("ExceptionReport") == -1;
            }
        } catch (Exception e) {

        } finally {
            try {
                if (urlStream != null) {
                    urlStream.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {

            }
        }
        //	System.out.println("url:" + urlStr + "服务无法连接");
        return false;
    }
    public static boolean checkUrl(String urlStr) {
        StringBuilder sb = new StringBuilder();
        URL url = null;
        InputStream urlStream = null;
        BufferedReader bufferedReader = null;
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            url = new URL(urlStr);
            URLConnection URLconnection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
            // 设置获取连接超时时间
            httpConnection.setConnectTimeout(3000);
            // 设置读取返回数据超时时间
            httpConnection.setReadTimeout(2000);
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                urlStream = httpConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(urlStream));
                String sCurrentLine = null;
                while ((sCurrentLine = bufferedReader.readLine()) != null) {
                    sb.append(sCurrentLine + "\r\n");
                }
                //不包含ExceptionReport 表示服务正常

                    //System.out.println("url:" + urlStr + "服务正常");
                    return true;


            }

        } catch (Exception e) {

        } finally {
            try {
                if (urlStream != null) {
                    urlStream.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {

            }
        }
        //	System.out.println("url:" + urlStr + "服务无法连接");
        return false;
    }
}
