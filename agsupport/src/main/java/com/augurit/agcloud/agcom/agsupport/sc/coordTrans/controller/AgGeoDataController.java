package com.augurit.agcloud.agcom.agsupport.sc.coordTrans.controller;

import com.augurit.agcloud.agcom.agsupport.util.ZipFileUtils;
import com.augurit.agcloud.agcom.agsupport.domain.AgWktFeature;
import com.augurit.agcloud.agcom.agsupport.sc.coordTrans.util.AgGeoDataDao;
import com.augurit.agcloud.agcom.agsupport.sc.labelImport.service.IAgShpDataImport;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.vividsolutions.jts.geom.*;
import net.sf.json.JSONObject;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.*;


@RestController
@RequestMapping("/agsupport/geoData")
@Validated
public class AgGeoDataController {

    @Autowired
    private IAgShpDataImport agShpDataImport;

    @Autowired
    private AgGeoDataDao geoDataDao;

   private String   churangtuchengKeys = "OACASENO,LANDUSEPERMITNAME,LANDUSEPERMITCODE,CHHISDISPATCHNO,APPLICANT1,APPLICANT2,APPLICANT3,PROJECTNAME,LANDADDRESS,CONSTRUCTSCALE,LANDUSETYPE1,LANDUSETYPE2,LANDUSETYPE3,LANDUSETYPE4,TOTALAREA,ROADAREA,RIVERAREA,GREENAREA,OTHERAREA,CONSTRUCTLANDAREA,BUILD_DEN,BUILD_RATIO,GREEN_RATIO,BUILD_AREA,BUILD_HGHT,PUBFAC,PARKS,PEOPLE_CAP,MAPNAME,APPROVEDDATE,DRAWUNIT,LANDDATELIMIT,PARCELID,HISPERMIT,SPATIAL,TYPE,CLBZ,SHQK,XMDZ,XMDWMC,GGSSPT1,GGSSPT,TZBZ,YSZBX,QTTJ,PTSS,JZXG,JZMD,LDL,RJL,ZJZMJ,QTDZYDMJ,GHJSYDMJ,ZZDMJ,YDXZ,YDXZDM,DH,QFRQ,PWH";
   private String[]   quyupingguKeys = {"PROJECTNAME","REMARK","HOST","CONTACT","TELEPHONE"};
   private String[] approveprojKeys = {"BSM","XMMC","JSNR","YDGM","ZTZ","QTDW","XMLB","SSXZQ","JSQZSJ2","XMBH","SSJD","HYLB","XMXL","FGPROJECTCODE"};
    private String[] lsjcxmKeys = "objectid,bsm,xmmc,jsnr,ydgm,ztz,qtdw,xmlb,ssxzq,jsqzsj2,xmbh,ssjd,hylb,xmxl,fgprojectcode,shape".toUpperCase().split(",");

    /*
    * 将审批项目信息保存到sde
    * @params type 0: 出让图层，1：区域评估，2：审批项目布局
    * */
    @RequestMapping("/shp/{type}/importProjectFeature")
    public String importShpIntoSDE(@NotNull String wkt, @NotNull @PathVariable("type") String type, @NotNull String xmdm, String attrs) throws Exception {

       boolean isSuccess = false;
       String uuid = UUID.randomUUID().toString();
        try {
         String field = "id,xmdm,geom,attr";
         JSONObject jsonObject =  JSONObject.fromObject(attrs);
         Iterator iterator = jsonObject.keys();

            String[] mathcKeys;
            String tableName;
            if(type.equals("2")) {
                mathcKeys = approveprojKeys;
                tableName = "sde.sde_approveproject_layout";
            }else if(type.equals("0")){

                mathcKeys = churangtuchengKeys.split(",");
                tableName = "sde.sde_churanglayer";
            }
            else if(type.equals("3")) {
                mathcKeys = lsjcxmKeys;
                tableName = "sde.lsjcxm";
            }
            else {
                mathcKeys = quyupingguKeys;
                tableName = "sde.sde_quyupinggu";
            }


            StringBuffer sb = new StringBuffer("'");

            String values = sb.append(uuid)
                    .append("'")
                    .append(",")
                    .append("'")
                    .append(xmdm)
                    .append("'")
                    .append(",")
                    .append("'")
                    .append(wkt)
                    .append("'")
                    .append(",")
                    .append("'")
                    .append(attrs)
                    .append("'").toString();

         while (iterator.hasNext()){
             String key = (String) iterator.next();
             if(Arrays.asList(mathcKeys).contains(key.toUpperCase()) ){
             field = field + ",";
             field = field + key;

             String value = jsonObject.getString(key);
             values = values + ",'";
             values = values + value + "'";
             }
         }


         isSuccess = geoDataDao.inserGeometryIntoSDE(tableName,field, values);
            System.out.println(isSuccess);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(isSuccess) {
            Map<String, String> content = new HashMap<String, String>();
            content.put("id",uuid);
            content.put("xmdm", xmdm);
            return JsonUtils.toJson(new ContentResultForm<Map>(true,content,"shp导入sde成功"));
        }else {
            return JsonUtils.toJson(new ResultForm(false,"shp导入sde失败"));
        }

    }

    @RequestMapping("/shp/{type}/getProjInfo")
    public String getApproveProjectInfoByxmdm(@NotNull @PathVariable("type") String type, String condition)  {
           List result;

        String tableName;
        if(type.equals("2")) {
            tableName = "sde.sde_approveproject_layout";
        }else if(type.equals("0")){
            tableName = "sde.sde_churanglayer";
        }
        else if(type.equals("3")) {
            tableName = "sde.lsjcxm";
        }
        else {
            tableName = "sde.sde_quyupinggu";
        }
        try {
              // if(xmdm != null || id != null) {
                  result = geoDataDao.getProjectInfoByxmdm(tableName, condition);
                  return JsonUtils.toJson(new ContentResultForm<>(true,result,"获取项目信息成功"));
               ///}
           }catch (Exception e) {
               e.printStackTrace();
           }
          return JsonUtils.toJson(new ResultForm(false,"获取项目信息失败"));
    }

    @RequestMapping("/shp/{type}/delete")
    public String delApproveProjectInfoByxmdm(@PathVariable("type") String type , String condition)  {
        boolean isSuccess = false;

        String tableName;
        if(type.equals("2")) {
            tableName = "sde.sde_approveproject_layout";
        }else if(type.equals("0")){
            tableName = "sde.sde_churanglayer";
        }
        else if(type.equals("3")) {
            tableName = "sde.lsjcxm";
        }
        else {
            tableName = "sde.sde_quyupinggu";
        }

        try {
            //if(xmdm != null || id != null) {
                isSuccess = geoDataDao.delProjectInfoByxmdm(tableName,condition);
           // }
        }catch (Exception e) {
            e.printStackTrace();
        }
        if(isSuccess) {
            return JsonUtils.toJson(new ContentResultForm<>(true,"删除项目信息成功"));
        }else {
            return JsonUtils.toJson(new ContentResultForm<>(true,"删除项目信息失败"));
        }
    }

    @RequestMapping("/shp/{type}/update")
    public String updateShapeInfo(@PathVariable("type") String type, String condition, String attrs) {


        boolean isSuccess = false;
        try {
            JSONObject jsonObject =  JSONObject.fromObject(attrs);
            Iterator iterator = jsonObject.keys();

            String tableName;
            if(type.equals("2")) {
                tableName = "sde.sde_approveproject_layout"; //审批项目布局表
            }else if(type.equals("0")){
                tableName = "sde.sde_churanglayer";         //出让图层表
            }
            else if(type.equals("3")) {
                tableName = "sde.lsjcxm";
            }
            else {
                tableName = "sde.sde_quyupinggu";           //区域评估表
            }

            String keyValue = "";

            while (iterator.hasNext()){
                String key = (String) iterator.next();

                keyValue = keyValue + key + "='";
                String value = jsonObject.getString(key);
                keyValue = keyValue + value + "'";

                if(iterator.hasNext()) {
                    keyValue = keyValue + ",";
                }
            }

            //if(xmdm != null || id != null) {
                isSuccess = geoDataDao.updateGeometryInfo(tableName,condition,keyValue);
            //}


        }catch (Exception e){
            e.printStackTrace();
        }
        if(isSuccess) {
            return JsonUtils.toJson(new ResultForm(true,"更新成功"));
        }else {
            return JsonUtils.toJson(new ResultForm(false,"更新失败"));
        }

    }



    @RequestMapping("/wktToShpFile")
    @Valid
    public String wktConvertToShpFile(
            HttpServletRequest request,
            @NotNull String fileName,
            @NotNull String wkt,
            @NotNull String geomType) throws Exception {
        String uploadDirPath = request.getSession().getServletContext().getRealPath("/") + "uploads/shpfiles";
        String shpDirPath = (uploadDirPath + File.separator + fileName).replace("\\", "/");

        File uploadDir = new File(uploadDirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File shpDir = new File(shpDirPath);
        if (shpDir.exists()) {
            shpDir.delete();
        }
        shpDir.mkdirs();


        String shpFilePath = (shpDirPath + File.separator + fileName + ".shp").replace("\\", "/");

        File shpFile = new File(shpFilePath);
        if (!shpFile.exists()) {
            shpFile.createNewFile();
        }

        try {


            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(ShapefileDataStoreFactory.URLP.key, shpFile.toURI().toURL());
            ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(params);


            //设置图形信息和属性信息
            SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
            // sb.setCRS(new DefaultGeographicCRS());

            Class<?> geoType = null;
            switch (geomType) {
                case "Point":
                    geoType = Point.class;
                    break;
                case "MultiPoint":
                    geoType = MultiPoint.class;
                    break;
                case "LineString":
                    geoType = LineString.class;
                    break;
                case "MultiLineString":
                    geoType = MultiLineString.class;
                    break;
                case "Polygon":
                    geoType = Polygon.class;
                    break;
                case "MultiPolygon":
                    geoType = MultiPolygon.class;
                    break;
            }

            featureTypeBuilder.add("the_geom", geoType);
            featureTypeBuilder.add("object_id", Long.class);
            featureTypeBuilder.setName("shapefile");
            ds.createSchema(featureTypeBuilder.buildFeatureType());
            Charset charset = Charset.forName("GBK");
            ds.setCharset(charset);
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);

            SimpleFeature f = writer.next();
            f.setAttribute("the_geom", wkt);
            f.setAttribute("object_id", 0);

            writer.write();
            writer.close();
            ds.dispose();

            //将shp文件压缩
            ZipFileUtils.zipShape(shpDirPath, false);

            HashMap<String, String> content = new HashMap<String, String>();
            content.put("fileName", fileName+".zip");

            return JsonUtils.toJson(new ContentResultForm<>(true, content));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return JsonUtils.toJson(new ResultForm(false, "导出shp失败"));
    }




    @RequestMapping("/wktsToShpFile")
    @Valid
    public String wktsConvertToShpFile(
            HttpServletRequest request,
            @NotNull String fileName,
            @NotNull String wktFeatures,
            @NotNull String geomType) throws Exception {
        String uploadDirPath = request.getSession().getServletContext().getRealPath("/") + "uploads/shpfiles";
        String shpDirPath = (uploadDirPath + File.separator + fileName).replace("\\", "/");

        File uploadDir = new File(uploadDirPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File shpDir = new File(shpDirPath);
        if (shpDir.exists()) {
            shpDir.delete();
        }
        shpDir.mkdirs();


        String shpFilePath = (shpDirPath + File.separator + fileName + ".shp").replace("\\", "/");

        File shpFile = new File(shpFilePath);
        if (!shpFile.exists()) {
            shpFile.createNewFile();
        }

        try {


            Map<String, Serializable> params = new HashMap<String, Serializable>();
            params.put(ShapefileDataStoreFactory.URLP.key, shpFile.toURI().toURL());
            ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createDataStore(params);


            //设置图形信息和属性信息
            SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
            // sb.setCRS(new DefaultGeographicCRS());

            Class<?> geoType = null;
            switch (geomType) {
                case "Point":
                    geoType = Point.class;
                    break;
                case "MultiPoint":
                    geoType = MultiPoint.class;
                    break;
                case "LineString":
                    geoType = LineString.class;
                    break;
                case "MultiLineString":
                    geoType = MultiLineString.class;
                    break;
                case "Polygon":
                    geoType = Polygon.class;
                    break;
                case "MultiPolygon":
                    geoType = MultiPolygon.class;
                    break;
            }

            ArrayList<AgWktFeature> wktFeatureList = (ArrayList<AgWktFeature>) JsonUtils.parseList(AgWktFeature.class,wktFeatures);

            featureTypeBuilder.add("the_geom", geoType);
            featureTypeBuilder.add("object_id", Long.class);

            Map<String, String> props = wktFeatureList.get(0).getProperties();
            Iterator iters = props.keySet().iterator();
            ArrayList<String> fields = new ArrayList<>();
            while (iters.hasNext()) {
                String key = (String) iters.next();

                key = key.length() > 10 ? key.substring(0, 9) : key; //geoTool限制 最长10

                featureTypeBuilder.add(key, String.class);
                fields.add(key);
            }



            featureTypeBuilder.setName("shapefile");
            ds.createSchema(featureTypeBuilder.buildFeatureType());
            Charset charset = Charset.forName("UTF-8");
            ds.setCharset(charset);
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0], Transaction.AUTO_COMMIT);

            //ArrayList<AgWktFeature> wktFeatureList = new ArrayList<AgWktFeature>();


            for(int i=0; i<wktFeatureList.size();i++) {

                AgWktFeature wktFeature = wktFeatureList.get(i);
                SimpleFeature f = writer.next();
                f.setAttribute("the_geom", wktFeature.getWkt());
                f.setAttribute("object_id", i);


                //设置其它属性
                Map<String, String> properities = wktFeature.getProperties();
                Iterator iterator = properities.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    String value =(String) properities.get(key);
                    if(value == null){
                        value = "";
                    }
                    if((!key.equals("object_id")) && fields.contains(key)) {
                        key = key.length() > 10 ? key.substring(0, 9) : key; //geoTool限制 最长10
                        f.setAttribute(key, value);
                    }
                }
                writer.write();
            }

            writer.close();
            ds.dispose();

            //将shp文件压缩
            ZipFileUtils.zipShape(shpDirPath, false);

            HashMap<String, String> content = new HashMap<String, String>();
            content.put("fileName", fileName+".zip");

            return JsonUtils.toJson(new ContentResultForm<>(true, content));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return JsonUtils.toJson(new ResultForm(false, "导出shp失败"));
    }



}
