package com.augurit.agcloud.agcom.agsupport.util;

import com.coordtrans.bean.ParameterFour;
import com.coordtrans.bean.ParameterSeven;
import com.coordtrans.service.ICoordTransAppMore;
import com.coordtrans.service.impl.CoordTransAppMoreImpl;
import com.vividsolutions.jts.geom.Geometry;
import net.sf.json.JSONArray;
import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStoreFactorySpi;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangmingyang
 * @Description: 操作shape文件的工具类
 * @date 2018-11-06
 */
public class ShapeFileUtils {
    public static String getWktStr(String shpFilePath) throws Exception {

        JSONArray jsonArray = new JSONArray();
        //源shape文件
        ShapefileDataStore shapeDS = (ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(new File(shpFilePath).toURI().toURL());
        SimpleFeatureSource fs = shapeDS.getFeatureSource(shapeDS.getTypeNames()[0]);
        SimpleFeatureIterator it = fs.getFeatures().features();
        try {
            while (it.hasNext()) {
                SimpleFeature f = it.next();
                Object the_geom = f.getAttribute("the_geom");
                String wkt = the_geom.toString();
                jsonArray.add(wkt);
            }
        }finally {
            it.close();
            shapeDS.dispose();

        }

        return jsonArray.toString();
    }
    /**
     * shape文件坐标转换
     * @param srcfilepath 原目录
     * @param destfilepath 目标目录
     */
    public static void transShape(String srcfilepath, String destfilepath,String param,String type,double L1,double L2) throws IOException,ArrayIndexOutOfBoundsException{
        try {
            //源shape文件
            ShapefileDataStore shapeDS = (ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(new File(srcfilepath).toURI().toURL());
            //创建目标shape文件对象
            Map<String, Serializable> params = new HashMap<String, Serializable>();
            FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();
            params.put(ShapefileDataStoreFactory.URLP.key, new File(destfilepath).toURI().toURL());
            ShapefileDataStore ds = (ShapefileDataStore) factory.createNewDataStore(params);
            // 获取原shape文件的坐标系
            CoordinateReferenceSystem coordinateReferenceSystem = shapeDS.getSchema().getCoordinateReferenceSystem();
            // 设置属性
            SimpleFeatureSource fs = shapeDS.getFeatureSource(shapeDS.getTypeNames()[0]);
            //根据源shape文件的simpleFeatureType可以不用retype，而直接用fs.getSchema设置,设置坐标系 DefaultGeographicCRS.WGS84
            ds.createSchema(SimpleFeatureTypeBuilder.retype(fs.getSchema(), coordinateReferenceSystem));

            //设置writer
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);

            //写记录
            SimpleFeatureIterator it = fs.getFeatures().features();
            ICoordTransAppMore ICoordTransAppMore = new CoordTransAppMoreImpl();
            try {
                while (it.hasNext()) {
                    SimpleFeature f = it.next();
                    SimpleFeature fNew = writer.next();
                    List<Object> attributes = f.getAttributes();
                    //System.out.println(attributes.toString());
                    Object the_geom = f.getAttribute("the_geom");
                    Geometry the_geom1 = (Geometry) f.getAttribute("the_geom");
                    // 获取原文件的坐标值

                    String oldWkt = the_geom.toString();
                    String newWkt = "";
                    if ("4param".equals(type)) {
                        ParameterFour parameter = new ParameterFour(param.split(","));
                        newWkt = ICoordTransAppMore.CS_1ToCS_2(oldWkt, parameter);
                    } else {
                        ParameterSeven parameter = new ParameterSeven(param.split(","));
                        newWkt = ICoordTransAppMore.CS_1ToCS_2(oldWkt, parameter, L1, L2, type);
                    }
                    fNew.setAttributes(f.getAttributes());
                    // 设置转后之后的坐标值
                    fNew.setAttribute("the_geom", newWkt);
                    writer.write();
                }
            } finally {
                it.close();
                writer.close();
                ds.dispose();
                shapeDS.dispose();

            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (ArrayIndexOutOfBoundsException ae){
            ae.printStackTrace();
            throw ae;
        }
    }
}
