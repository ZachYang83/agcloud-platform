package com.augurit.agcloud.agcom.agsupport.sc.labelImport.service.impl;

import com.augurit.agcloud.agcom.agsupport.sc.labelImport.service.IAgShpDataImport;
import com.augurit.agcloud.agcom.agsupport.util.ShapeFileUtils;
import com.augurit.agcloud.agcom.agsupport.util.ZipFileUtils;
import com.common.util.Common;
import com.augurit.agcloud.agcom.agsupport.common.util.AoInitUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.WorkspaceAGUtil;
import com.coordtrans.bean.*;
import com.coordtrans.service.impl.CoordTransAppImpl;
import com.coordtrans.service.impl.CoordTransAppMoreImpl;
import com.coordtrans.service.impl.CoordTransBaseImpl;
import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.lang3.StringUtils;
import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Feature;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import com.esri.arcgis.carto.CadFeatureLayer;
import com.esri.arcgis.carto.IFeatureLayer;
import com.esri.arcgis.datasourcesfile.ICadDrawingLayers;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.geodatabase.IFeatureCursor;
import com.esri.arcgis.geodatabase.IQueryFilter;
import com.esri.arcgis.geodatabase.QueryFilter;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.IGeometryCollection;
import com.esri.arcgis.geometry.IPath;
import com.esri.arcgis.geometry.IPoint;
import com.esri.arcgis.geometry.IPointCollection;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.AoInitialize;
import com.esri.arcgis.system.Cleaner;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by 陈泽浩 on 2017-05-08.
 */
@Service
public class AgShpDataImportImpl implements IAgShpDataImport {

    public static String shpType = "dbf,prj,sbn,sbx,shp,shp.xml,shx";
    private com.coordtrans.service.ICoordTransApp ICoordTransApp = new CoordTransAppImpl();
    private com.coordtrans.service.ICoordTransBase ICoordTransBase = new CoordTransBaseImpl();
    private com.coordtrans.service.ICoordTransAppMore ICoordTransAppMore = new CoordTransAppMoreImpl();
    public HashSet<String> set = new HashSet<String>();
    private static List coincidentPoint = null;

    @Override
    public String getShpData(HttpServletRequest request, String flag) {
        String strVectorFile = uploadFile(request, flag);
        JSONArray json = new JSONArray();
        String type = strVectorFile.substring(strVectorFile.indexOf(".") + 1, strVectorFile.length());
        if (shpType.indexOf(type) > -1) {
            set.add(type);
        }
        if (set.size() == 7) {
            String path = strVectorFile.substring(0, strVectorFile.indexOf(".")) + ".shp";
            ogr.RegisterAll();
            gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
            gdal.SetConfigOption("SHAPE_ENCODING", "CP936");
            DataSource ds = ogr.Open(path, 0);
            if (ds == null) {
                System.out.println("打开文件失败！");
                return null;
            }
            Layer layer = ds.GetLayer(0);
            layer.ResetReading();
            Feature feature = null;
            while ((feature = layer.GetNextFeature()) != null) {
                json.add(feature.GetGeometryRef().ExportToWkt());
            }
            set.clear();
        }
        return json.toString();
    }

    @Override
    public String getShpDataByZipFile(String zipPath) {
        //path: zip包的全路径
        String wkts = "";
        try {
            File zipFile = new File(zipPath);
            if(zipFile.exists()){
                //解压zip包
                String desPath = zipPath.split("\\.")[0];
                ZipFileUtils.unZipFiles(zipFile, desPath);
                //读取解压包下的shp文件

                String shpFileName = ZipFileUtils.getFileName(desPath);
                String shpFilePath = (desPath + File.separator + shpFileName).replace("\\","/");
                wkts = ShapeFileUtils.getWktStr(shpFilePath);

            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return wkts;
    }

    public String getShp(String path) {
        JSONArray json = new JSONArray();
        try {
            ogr.RegisterAll();
            gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
            gdal.SetConfigOption("SHAPE_ENCODING", "CP936");
            DataSource ds = ogr.Open(path, 0);
            if (ds == null) {
                System.out.println("打开文件失败！");
                return null;
            }
            Layer layer = ds.GetLayer(0);
            layer.ResetReading();
            Feature feature = null;
            while ((feature = layer.GetNextFeature()) != null) {
                json.add(feature.GetGeometryRef().ExportToWkt());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    @Override
    public String getDxfData(HttpServletRequest request, String flag) {
        String strVectorFile = uploadFile(request, flag);
        ogr.RegisterAll();
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
        DataSource ds = ogr.Open(strVectorFile, 0);
        if (ds == null) {
            System.out.println("打开文件失败！");
            return null;
        }
        Layer layer = ds.GetLayer(0);
        if (layer == null) {
            System.out.println("打开文件失败");
            return null;
        }
        layer.ResetReading();
        Feature feature = null;
        JSONArray json = new JSONArray();
        while ((feature = layer.GetNextFeature()) != null) {
            json.add(feature.GetGeometryRef().ExportToWkt());
        }
        return json.toString();
    }

    public String readDwgData(String filePath, String coordType, String projectId, String fileName) throws Exception {
        AoInitialize aoInit = null;
        try {
            //AO初始化
            AoInitUtil aoInitUtil = new AoInitUtil();
            aoInit = aoInitUtil.initializeEngine(aoInit);

            if (aoInit == null)
                throw new Exception("AO初始化失败");

            IFeatureClass featureClass = WorkspaceAGUtil.getCADFeatureClass(
                    filePath, "Polyline");

            IFeatureLayer featureLayer = new CadFeatureLayer();
            featureLayer.setFeatureClassByRef(featureClass);
            // 关键接口ICadDrawingLayers，此接口能获取CAD文件中的分层信息
            ICadDrawingLayers cadDrawingLayers = (ICadDrawingLayers) featureLayer;

            IQueryFilter queryFilter = new QueryFilter();
            //可视要素的where条件
            queryFilter.setWhereClause(getVisibleClause(cadDrawingLayers));

            IFeatureCursor featureCursor = featureClass.search(queryFilter,
                    true);

            //下面的代码是把要素拼接成指定格式的字符串
            StringBuffer builder = new StringBuffer();
            builder.append("[");

            int count = 0;
            IFeature pFeature = null;
            while ((pFeature = featureCursor.nextFeature()) != null) {
                IGeometry geo = pFeature.getShapeCopy();

                count++;
                if (builder.length() > 2)
                    builder.append(",");

                builder.append("{");
                builder.append("\"label\":\" 图形" + count + "\",");
                builder.append("\"mark\":[],\"geometry\":{");
                builder.append("\"paths\":[");

                IGeometryCollection paths = (IGeometryCollection) geo;
                for (int j = 0; j < paths.getGeometryCount(); j++) {
                    if (j > 0)
                        builder.append(",");

                    IPath pPath = (IPath) paths.getGeometry(j);

                    //输出一个Path（线）的字符串
                    appendPath(builder, pPath, coordType);
                }

                builder.append("]");
                builder.append("}");
                builder.append("}");
            }

            builder.append("]");

            Cleaner.release(featureCursor);
            String cadData = builder.toString();
            ///将项目的cad文件数据保存到数据表中
            if (StringUtils.isNotBlank(projectId)) {
//                bxXmProjectInfoService.saveProjectCADData(projectId, fileName, cadData);
            }

            return cadData;

        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        } finally {
            aoInit.shutdown();
        }
    }
    /**
     * @param str=11,11
     * @return 11, 11
     */
    private String getCoordTransData(String str) {
        String js = "";
        if (str == null || str.equalsIgnoreCase("") || str.split(",").length != 2) {
            return str;
        }
        BaseCoordinate tmp_c = new BaseCoordinate(Double.parseDouble(str.split(",")[0].trim()), Double.parseDouble(str.split(",")[1].trim()));
        BaseCoordinate cc = ICoordTransBase.CS_1ToCS_2(tmp_c, coincidentPoint, 10000);
        js = cc.getX() + "," + cc.getY();
        return js;
    }
    /**
     * 输出一个Path（线）的字符串
     *
     * @param builder
     * @param path
     * @throws AutomationException
     * @throws Exception
     */
    private void appendPath(StringBuffer builder, IPath path, String coordType)
            throws AutomationException, Exception {
        builder.append("[");

        IPointCollection pointCollection = (IPointCollection) path;
        for (int i = 0; i < pointCollection.getPointCount(); i++) {
            if (i > 0)
                builder.append(",");

            IPoint pPoint = pointCollection.getPoint(i);
            builder.append("[");
            if (coordType.trim().toLowerCase().equalsIgnoreCase("bd")) {
                String tmpStr = getCoordTransData(pPoint.getX() + "," + pPoint.getY());
                builder.append(tmpStr);
            } else if (coordType.trim().toLowerCase().equalsIgnoreCase("")) {
                builder.append(pPoint.getX());
                builder.append(",");
                builder.append(pPoint.getY());
            } else {
                builder.append(pPoint.getX());
                builder.append(",");
                builder.append(pPoint.getY());
            }

            builder.append("]");
        }

        builder.append("]");
    }


    /**
     * 获取可视要素的where条件
     *
     * @param cadDrawingLayers
     * @return
     * @throws AutomationException
     * @throws Exception
     */
    private String getVisibleClause(ICadDrawingLayers cadDrawingLayers)
            throws AutomationException, Exception {
        int visibleCount = 0;
        int unvisibleCount = 0;

        StringBuffer builder1 = new StringBuffer();
        StringBuffer builder2 = new StringBuffer();

        for (int i = 0; i < cadDrawingLayers.getDrawingLayerCount(); i++) {
            if (!cadDrawingLayers.isDrawingLayerVisible(i)) {
                unvisibleCount++;

                if (builder1.length() > 0) {
                    builder1.append(" AND ");
                }

                builder1.append("LAYER<>'"
                        + cadDrawingLayers.getDrawingLayerName(i) + "'");
            } else {
                visibleCount++;
                if (builder2.length() > 0) {
                    builder2.append(" OR ");
                }

                builder2.append("LAYER='"
                        + cadDrawingLayers.getDrawingLayerName(i) + "'");
            }
        }

        return visibleCount > unvisibleCount ? builder1.toString() : builder2
                .toString();
    }

    /**
     * @param request
     * @param flag    判断 返回绝对路径 or 相对路径
     * @return
     */
    @Override
    public String uploadFile(HttpServletRequest request, String flag) {
        String js = null;
        BufferedInputStream in = null;
        BufferedOutputStream outputStream = null;
        try {
            request.setCharacterEncoding("UTF-8");
            String relativePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/uploads";
            File tempDirPath = new File(request.getSession().getServletContext().getRealPath("/") + "\\uploads\\");
            if (!tempDirPath.exists()) {
                tempDirPath.mkdirs();
            }
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
            Iterator<String> it = req.getFileNames();
            while (it.hasNext()) {
                MultipartFile file = req.getFile(it.next());
                String fileNames = file.getOriginalFilename();
                File itemFile = new File(tempDirPath + "\\" + fileNames);
                if (itemFile.exists()) {
                    itemFile.delete();
                }
                in = new BufferedInputStream(file.getInputStream());
                outputStream = new BufferedOutputStream(new FileOutputStream(itemFile));
                Streams.copy(in, outputStream, true);
                if (flag.equals("relative"))
                    js = relativePath + "/" + fileNames;
                else
                    js = (tempDirPath + "\\" + fileNames).replace("\\", "/");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return js;
    }

    public boolean deleteFile(String path) {
        boolean flag = false;
        String ctx = Common.getByKey("server.context-path");
        String clsPath = Class.class.getClass().getResource("/").getPath();
        String localhost = clsPath.replaceAll("^/(.*" + ctx + ")/.*$", "$1");
        path.substring(path.lastIndexOf("/"));
        path = localhost + "/src/main/webapp/uploads" + path.substring(path.lastIndexOf("/"));
        try {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                if (file.delete()) flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public String readCsvFile(String path) throws IOException {
        File file = new File(path);
        BufferedReader reader = null;
        CsvReader creader = null;
        JSONObject json = null;
        JSONArray arr = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            creader = new CsvReader(reader, ',');
            creader.readHeaders();
            arr = new JSONArray();
            int j = creader.getHeaderCount();
            while (creader.readRecord()) {
                json = new JSONObject();
                for (int k = 0; k < j; k++) {
                    json.put(creader.getHeader(k), creader.get(creader.getHeader(k)));
                }
                arr.add(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (creader != null) creader.close();
            if (reader != null) reader.close();
        }
        return arr.toString();
    }
    public  String readExcel(String filePath){
        Workbook wb = null;
        Sheet sheet = null;
        Row row = null;
        List<Map<String,String>> list = null;
        String cellData = null;
//        String columns[] = {"lon","lat"};
        JSONArray arr = null;
        JSONObject json = null;
        if(filePath==null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                wb = new XSSFWorkbook(is);
            }else{
                wb = null;
            }
            //第一行为标题

//                lists.add(list)

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(wb != null){
            //用来存放表中数据
            list = new ArrayList<Map<String,String>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //获取第一行
            String columns[]=new String[rownum];
            row = sheet.getRow(0);
            int colnum = row.getPhysicalNumberOfCells();
            if(row !=null){
                for (int j=0;j<colnum;j++){
                    cellData = (String) getCellFormatValue(row.getCell(j));
                    columns[j]=cellData;
                }
            }
            //获取最大列数


            for (int i = 1; i<rownum; i++) {
                Map<String,String> map = new LinkedHashMap<String,String>();
                row = sheet.getRow(i);
                if(row !=null){
                    for (int j=0;j<colnum;j++){
                        cellData = (String) getCellFormatValue(row.getCell(j));
                        map.put(columns[j], cellData);
                    }
                }else{
                    break;
                }
                list.add(map);
            }
        }
        arr = new JSONArray();
        //遍历解析出来的list
        for (Map<String,String> map : list) {
            json = new JSONObject();
            for (Entry<String,String> entry : map.entrySet()) {
                json.put(entry.getKey(),entry.getValue());
            }
            arr.add(json);
        }


        return arr.toString();
    }
    public static Object getCellFormatValue(Cell cell){
        Object cellValue = null;
        if(cell!=null){
            //判断cell类型
            switch(cell.getCellType()){
                case Cell.CELL_TYPE_NUMERIC:{
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                }
                case Cell.CELL_TYPE_FORMULA:{
                    //判断cell是否为日期格式
                    if(DateUtil.isCellDateFormatted(cell)){
                        //转换为日期格式YYYY-mm-dd
                        cellValue = cell.getDateCellValue();
                    }else{
                        //数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:{
                    cellValue = cell.getRichStringCellValue().getString();
                    break;
                }
                default:
                    cellValue = "";
            }
        }else{
            cellValue = "";
        }
        return cellValue;
    }


    public String writeCsvFile(String path, String text) throws IOException {
        File file = new File(path);
        CsvWriter csvWriter = null;
        BufferedWriter writer = null;
        boolean flag = true;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            csvWriter = new CsvWriter(writer, ',');
            JSONArray arr = JSONArray.fromObject(text);
            for (Object str : arr) {
                JSONObject json = JSONObject.fromObject(str);
                Iterator<String> sIterator = json.keys();
                String inString = "";
                String heads = "";
                while (sIterator.hasNext()) {
                    String key = sIterator.next();
                    String value = json.getString(key);
                    heads = heads + key + ",";
                    inString = inString + value + ",";
                }
                if (flag) {
                    heads = heads.substring(0, heads.lastIndexOf(","));
                    csvWriter.writeRecord(heads.split(","), true);
                    flag = false;
                }
                inString = inString.substring(0, inString.lastIndexOf(","));
                csvWriter.writeRecord(inString.split(","), true);
            }
            csvWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (csvWriter != null) csvWriter.close();
            if (writer != null) csvWriter.close();
        }
        return null;
    }

}
