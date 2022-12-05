package com.augurit.agcloud.agcom.agsupport.sc.dataUpdate.service;

import com.augurit.agcloud.agcom.agsupport.sc.dataUpdate.dao.AgDataUpdateDao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.*;


/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :15:39 2019/1/7
 * @Modified By:
 */

@Service
public class AgDataUpdateService {

    @Autowired
    private AgDataUpdateDao agDataUpdateDao;


    public void delFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }
    public void delDir(String filePath) {
        File dir = new File(filePath);
        if (dir.exists()){
            File[] files = dir.listFiles();
            for (File file:files){
                if (file.isDirectory()){
                    delDir(filePath+ File.separator+file.getName());
                }else {
                    file.delete();
                }
            }
            dir.delete();
        }
    }

    /**
     * Shape文件属性列头信息
     * @param filePath
     * @throws IOException
     */
    public List<String> getTitledInfo(String filePath) throws IOException {
        List<String> list = new ArrayList<>();
       /* ShapefileDataStoreFactory factory = new ShapefileDataStoreFactory();
        ShapefileDataStore dataStore = (ShapefileDataStore) factory.createDataStore(new File(filePath).toURI().toURL());
        dataStore.setCharset(Charset.forName("UTF-8"));*/

       /* // 读取文件wktid 即srid
        String typeName = dataStore.getTypeNames()[0];
        FeatureType schema = dataStore.getSchema(typeName);
        String kwid = "";
        CoordinateReferenceSystem crsystem = schema.getCoordinateReferenceSystem();
        int espg = CRS.lookupEpsgCode(crsystem,true);
        System.out.println("WKId:" + espg);*/
        /*CoordinateReferenceSystem str = dataStore.getSchema().getCoordinateReferenceSystem();
        System.out.println(str.getCoordinateSystem());


        List<AttributeDescriptor> attrList = dataStore.getFeatureSource().getSchema()
                .getAttributeDescriptors();
        for (AttributeDescriptor attr : attrList) {
            list.add(attr.getName().toString());
            //System.out.println("TITLE:" + attr.getName().toString());
        }*/

        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        ShapefileDataStore sds = (ShapefileDataStore) dataStoreFactory.createDataStore(new File(filePath).toURI().toURL());
        sds.setCharset(Charset.forName("UTF-8"));
        SimpleFeatureSource featureSource = sds.getFeatureSource();
        SimpleFeatureIterator itertor = featureSource.getFeatures().features();

        while (itertor.hasNext()) {
            SimpleFeature feature = itertor.next();
            Iterator<Property> it = feature.getProperties().iterator();
            System.out.print("shap文件的字段名称: ");
            while (it.hasNext()) {
                Property pro = it.next();
                String key = pro.getName().toString();
                list.add(key);
                System.out.print(key+" ");
            }
            break;//只读取一条
        }
        return list;
    }


    /**
     * 读取shap文件
     * @param filePath
     */
    public List<String> readSHP(String filePath) throws Exception {
        List<String> list = new ArrayList<>();
        ShapefileDataStore shpDataStore = null;
        try {
            shpDataStore = new ShapefileDataStore(new File(filePath).toURI().toURL());
            shpDataStore.setCharset(Charset.forName("UTF-8"));
            String typeName = shpDataStore.getTypeNames()[0];
            FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = null;
            featureSource = shpDataStore.getFeatureSource(typeName);
            FeatureCollection<SimpleFeatureType, SimpleFeature> result = featureSource.getFeatures();
            System.out.println(result.size());
            FeatureIterator<SimpleFeature> itertor = result.features();

            while (itertor.hasNext()) {
                SimpleFeature feature = itertor.next();
                Collection<Property> p = feature.getProperties();
                Iterator<Property> it = p.iterator();
                while (it.hasNext()) {
                    Property pro = it.next();
                    String name = pro.getName().toString();
                    if ("the_geom".equals(name)) {
                        list.add(pro.getValue().toString());
                    }
                    /*if (pro.getValue() instanceof Point) {
                        System.out.println(pro.getName() + " = " + pro.getValue());
                        System.out.println("PointX = " + ((Point) (pro.getValue())).getX());
                        System.out.println("PointY = " + ((Point) (pro.getValue())).getY());
                    } else {
                        System.out.println(pro.getName() + " = " + pro.getValue());
                    }*/
                }
            }
            itertor.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return list;
    }

    /**
     * 读物DBF文件
     * @param filePath 文件路径
     */
    public void readDBF(String  filePath) {
        DbaseFileReader reader = null;
        try {
            reader = new DbaseFileReader(new ShpFiles(filePath), false, Charset.forName("utf-8"));
            DbaseFileHeader header = reader.getHeader();
            int numFields = header.getNumFields();
            //迭代读取记录
            while (reader.hasNext()) {
                try {
                    Object[] entry = reader.readEntry();
                    for (int i=0; i<numFields; i++) {
                        String title = header.getFieldName(i);
                        Object value = entry[i];
                        System.out.println(title+"="+value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                //关闭
                try {reader.close();} catch (Exception e) {}
            }
        }
    }


    /**
     * 解析shp文件 更新到数据库.存在：更新，不存在：插入
     * @param filePath
     * @param keepData  true:删除表存在的数据而更新文件不存在的数据，false:保留
     * @param contactOnlyShpField  表和shp文件关联标识（shp文件字段）
     * @param contactFieldMap map<tableField,shpField>
     * @param dataSourceId
     * @param table   1新增，2修改，3删除  1：更新，2删除，3，插入
     */
    public String readSHP_DBF(String filePath, boolean keepData, String contactOnlyShpField, Map<String, String> contactFieldMap, String dataSourceId, String table) throws Exception{
        long readFileTimeStart = System.currentTimeMillis();
        JSONObject json = new JSONObject();
        boolean procedure = false;//shap文件有字段值超过4000时使用clob类型提交并调用存储过程更新数据
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        String dbType = agDataUpdateDao.getDbType(dataSourceId);
        String srId = agDataUpdateDao.getSrId(dataSourceId, table);
        if (srId==null) srId="4326";
        //String srId = "";
        LinkedList<Map<String, Object>> updataList = new LinkedList<>();
        LinkedList<String> delList = new LinkedList<>();
        LinkedList<Object> upList = new LinkedList<>();
        Map<String, String> shpMap = new HashMap<>();//存储文件和表关联字段的值
        Map<String, String> shpMap_temp = new HashMap<>();
        Map<String, String> beforeUpdateMap = new HashMap<>();//存储表与文件关联字段的值(更新数据之前)
        Map<String, String> afterUpdateMap = new HashMap<>();//存储表与文件关联字段的值
        String oldTable = table.substring(0,table.lastIndexOf("_"));
        beforeUpdateMap = agDataUpdateDao.findAll(dataSourceId, dbType, oldTable, contactOnlyShpField);
        try {
            ShapefileDataStore sds = (ShapefileDataStore) dataStoreFactory.createDataStore(new File(filePath).toURI().toURL());
            sds.setCharset(Charset.forName("UTF-8"));
           /* String typeName = sds.getTypeNames()[0];
            FeatureType schema = sds.getSchema(typeName);
            String kwid = "";
            CoordinateReferenceSystem crsystem = schema.getCoordinateReferenceSystem();
            int espg = CRS.lookupEpsgCode(crsystem,true);
            System.out.println("WKId:" + espg);*/

            //String  wkt = sds.getSchema().getCoordinateReferenceSystem().toWKT();
            //System.out.println("WKT:" + wkt);
           // System.out.println("WKTID:" + CRS.lookupEpsgCode(sds.getSchema(sds.getTypeNames()[0]).getCoordinateReferenceSystem(),true));

            SimpleFeatureSource featureSource = sds.getFeatureSource();
            SimpleFeatureIterator itertor = featureSource.getFeatures().features();

            //删除表存在的数据而更新文件不存在的数据，记录文件更新的数据
            if (keepData) {
                while (itertor.hasNext()) {
                    SimpleFeature feature = itertor.next();
                    Iterator<Property> it = feature.getProperties().iterator();
                    Map<String, Object> map = new HashMap<>();
                    while (it.hasNext()) {
                        Property pro = it.next();
                        String key = pro.getName().toString();
                        Object value = pro.getValue();
                        map.put(key, value);
                        //记录shap文件的数据,唯一标识字段值为key
                        if (key.equals(contactFieldMap.get(contactOnlyShpField))) {
                            shpMap.put(value.toString(), value.toString());
                            shpMap_temp.put(value.toString(), value.toString());
                        }
                        //长度超过4000,使用存储过程
                        if (pro.getValue()!=null && pro.getValue().toString().length()>4000){
                            procedure = true;
                        }
                    }
                    updataList.add(map);
                }
            } else {
                while (itertor.hasNext()) {
                    SimpleFeature feature = itertor.next();
                    Iterator<Property> it = feature.getProperties().iterator();
                    Map<String, Object> map = new HashMap<>();
                    while (it.hasNext()) {
                        Property pro = it.next();
                        String key = pro.getName().toString();
                        Object value = pro.getValue();
                        map.put(key, value);
                        //记录shap文件的数据,唯一标识字段值为key
                        if (key.equals(contactFieldMap.get(contactOnlyShpField))) {
                            shpMap.put(value.toString(),value.toString());
                            shpMap_temp.put(value.toString(), value.toString());
                        }
                        //长度超过4000,使用存储过程
                        if (value!=null && value.toString().length()>4000){
                            procedure = true;
                        }
                    }
                    updataList.add(map);
                }
            }
            itertor.close();
            long readFileTimeEnd = System.currentTimeMillis();
            System.out.println("解析文件"+updataList.size()+"条数据耗时："+(readFileTimeEnd-readFileTimeStart)/1000+"秒");
            agDataUpdateDao.updateOrsave(updataList, dataSourceId, dbType, table, contactOnlyShpField, contactFieldMap, srId,procedure);//更新数据

            //获取临时表和旧表不同的数据
            String fieldNames = "";
            Iterator<Map.Entry<String, String>> fieldNameIt = contactFieldMap.entrySet().iterator();
            while (fieldNameIt.hasNext()){
                Map.Entry<String, String> entry = fieldNameIt.next();
                fieldNames += entry.getKey()+",";
            }
            fieldNames = fieldNames.substring(0,fieldNames.length()-1);
            Map<String,Object> map = agDataUpdateDao.getDifferent(dataSourceId,table,contactOnlyShpField,fieldNames);

            //记录shap文件插入还是更新的数据
            Iterator<Map.Entry<String, String>> before = beforeUpdateMap.entrySet().iterator();
           while (before.hasNext()){
               Map.Entry<String, String> entry = before.next();
               String key = entry.getKey();
               if (shpMap.get(key)!=null){
                   //临时表数据和原来数据内容不相同
                   if ("true".equals(map.get(key))){
                       Map<String,Object> upInfo = new HashMap<>();
                       upInfo.put("fieldValue",key);
                       upInfo.put("type",2);//1：更新，2删除，3，插入 1--2
                       upList.add(upInfo);
                   }
                   shpMap_temp.remove(key);//移除原来存在的数据得到插入的数据
               }
           }
            Iterator<Map.Entry<String, String>> insertIt = shpMap_temp.entrySet().iterator();
           while (insertIt.hasNext()){
               Map.Entry<String, String> entry = insertIt.next();
               Map<String,Object> insertInfo = new HashMap<>();
               insertInfo.put("fieldValue",entry.getValue());
               insertInfo.put("type",1);//1：更新，2删除，3，插入3--1
               upList.add(insertInfo);
           }

            //删除表存在的数据而更新文件不存在的数据
            if (keepData) {
                //文件数据和表数据关联字段作为条件查询表中所有数据
                afterUpdateMap = agDataUpdateDao.findAll(dataSourceId, dbType, table, contactOnlyShpField);
                Iterator<Map.Entry<String, String>> it = afterUpdateMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    //文件中不存在的数据
                    if (shpMap.get(entry.getKey()) == null) {
                        Map<String,Object> delInfo = new HashMap<>();
                        delInfo.put("fieldValue",entry.getKey());
                        delInfo.put("type",3);//1：更新，2删除，3，插入 2--3
                        upList.add(delInfo);
                        delList.add(entry.getKey());
                    }
                }
                //删除多余数据
                agDataUpdateDao.delMunch(delList, dataSourceId, dbType, table, contactOnlyShpField);
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.put("success",false);
            throw new Exception(e.getMessage());
        }finally {
            //删除shp相关文件
        }

        json.put("updataList", JSONArray.fromObject(upList));
        json.put("success",true);
        return json.toString();
    }
}