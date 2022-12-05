package com.augurit.agcloud.agcom.agsupport.sc.layer.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayerFieldConf;
import com.augurit.agcloud.agcom.agsupport.domain.AgSupDatasource;
import com.augurit.agcloud.agcom.agsupport.domain.AgUserLayer;
import com.augurit.agcloud.agcom.agsupport.sc.agdatasource.service.IAgSupDatasource;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.field.service.IAgField;
import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgLayer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Point;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Range;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.evacuate.Evacuate;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.query.service.IAgDBQuery;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.stats.service.IAgDBStats;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.SpatialUtil;
import com.augurit.agcloud.agcom.agsupport.sc.style.service.IAgStyle;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.common.util.ReflectBeans;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spatial.GeometryApplication;
import spatial.GeometryOperate;

import java.util.*;

import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.*;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-04-28.
 */
@Api(value = "查询矢量数据或配置",description = "查询矢量数据或配置相关接口")
@RestController
@RequestMapping("/agsupport/layer")
public class AgLayerController {
    @Autowired
    private IAgDBQuery iAgDBQuery;
    @Autowired
    private IAgDBStats iAgDBStats;
    @Autowired
    private IAgField iAgField;
    @Autowired
    private IAgDir iAgDir;
    @Autowired
    private IAgLayer iAgLayer;
    @Autowired
    private IAgStyle iAgStyle;
    @Autowired
    private IAgSupDatasource iAgSupDatasource;

    /**
     * 分页查询矢量数据
     *
     * @param dirLayerId 目录图层Id
     * @param userId     用户Id
     * @param where      属性查询where条件
     * @param type       空间查询类型 all（全图） extent（范围） polyline（线） envelop||polygon（面）
     * @param points     点数组 格式参考leaflet LatLon
     * @param page       分页对象
     * @return
     */
    @RequestMapping(value = "/queryVectorLayerPage",method = RequestMethod.POST)
    public EasyuiPageInfo<Map> queryVectorLayerPage(String dirLayerId, String userId, String where, String type, String points, Page page) {
        PageInfo<Map> result = new PageInfo<Map>();
        try {
            //获取图层基本信息
            AgLayer agLayer = iAgDir.findLayerByDirLayerId(dirLayerId);
            //获取图层配置信息
            //AgRoleLayer agRoleLayer = iAgField.getLayerConfigByUserId(dirLayerId, userId);
            AgUserLayer agUserLayer = iAgField.getLayerConfigByUserId(dirLayerId, userId);
            //获取有权限的字段
            List<AgLayerFieldConf> layerFieldConfs = iAgField.getLayerFieldsByUserId(dirLayerId, userId);
            if (agLayer.getLayerType().startsWith("01")) {//矢量图层判断
                Layer layer = ReflectBeans.copy(agLayer, Layer.class);
                List<String> fields = new ArrayList<String>();
                for (AgLayerFieldConf agLayerField : layerFieldConfs) {
                    //只查询显示的字段
                    if ("1".equals(agLayerField.getViewInResult())) {
                        fields.add(agLayerField.getFieldName());
                    }
                }
                //设置查询字段
                layer.setFields(fields);
                //设置where条件
                layer.setWhere(where);
                if (type.equals("all")) {
                    result = iAgDBQuery.queryByWKTPage(layer, agUserLayer.getExtent(), page);
                } else if (type.equals("extent")) {
                    List<Point> pointList = SpatialUtil.formatPoints(points);
                    Range range = new Range(pointList.get(0), pointList.get(1));
                    if (GeometryApplication.checkWkt(agUserLayer.getExtent())) {
                        String wkt = GeometryOperate.intersect(SpatialUtil.rangeToWKT(range), agUserLayer.getExtent());
                        result = iAgDBQuery.queryByWKTPage(layer, wkt, page);
                    } else {
                        result = iAgDBQuery.queryByExtentPage(layer, range, page);
                    }
                } else if (type.equals("polyline")) {
                    List<Point> pointList = SpatialUtil.formatPoints(points);
                    if (GeometryApplication.checkWkt(agUserLayer.getExtent())) {
                        String wkt = GeometryOperate.intersect(SpatialUtil.pointListToWKT(pointList, LINESTRING), agUserLayer.getExtent());
                        result = iAgDBQuery.queryByWKTPage(layer, wkt, page);
                    } else {
                        result = iAgDBQuery.queryByPolylinePage(layer, pointList, page);
                    }
                } else if (type.equals("envelop") || type.equals("polygon")) {
                    List<Point> pointList = SpatialUtil.formatPoints(points);
                    if (GeometryApplication.checkWkt(agUserLayer.getExtent())) {
                        String wkt = GeometryOperate.intersect(SpatialUtil.pointListToWKT(pointList, POLYGON), agUserLayer.getExtent());
                        result = iAgDBQuery.queryByWKTPage(layer, wkt, page);
                    } else {
                        result = iAgDBQuery.queryByPolygonPage(layer, pointList, page);
                    }
                } else if (type.equals("geoJson")) {
                    String wkt = SpatialUtil.jsonToWkt(points);
                    if (GeometryApplication.checkWkt(agUserLayer.getExtent())) {
                        wkt = GeometryOperate.intersect(wkt, agUserLayer.getExtent());
                    }
                    result = iAgDBQuery.queryByWKTPage(layer, wkt, page);
                } else if (type.equals("wkt")) {
                    String wkt = points;
                    if (GeometryApplication.checkWkt(agUserLayer.getExtent())) {
                        wkt = GeometryOperate.intersect(wkt, agUserLayer.getExtent());
                    }
                    result = iAgDBQuery.queryByWKTPage(layer, wkt, page);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo<Map> pageInfo=result;
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping(value = "/statsVectorLayer",method = RequestMethod.POST)
    public Map statsVectorLayer(String dirLayerId, String userId, String statsColumn, String groupColumn, String statsType, String type, String points) {
        Map result = new HashMap();
        try {
            //获取图层基本信息
            AgLayer agLayer = iAgDir.findLayerByDirLayerId(dirLayerId);
            //获取图层配置信息
            //AgRoleLayer agRoleLayer = iAgField.getLayerConfigByUserId(dirLayerId, userId);
            AgUserLayer agUserLayer = iAgField.getLayerConfigByUserId(dirLayerId, userId);
            if (agLayer.getLayerType().startsWith("01")) {//矢量图层判断
                Layer layer = ReflectBeans.copy(agLayer, Layer.class);
                layer.setStatsColumn(statsColumn);
                layer.setGroupColumn(groupColumn);
                layer.setStatsType(statsType);
                if (type.equals("all")) {
                    result = iAgDBStats.statsByWKT(layer, agUserLayer.getExtent());
                } else if (type.equals("extent")) {
                    List<Point> pointList = SpatialUtil.formatPoints(points);
                    Range range = new Range(pointList.get(0), pointList.get(1));
                    if (GeometryApplication.checkWkt(agUserLayer.getExtent())) {
                        String wkt = GeometryOperate.intersect(SpatialUtil.rangeToWKT(range), agUserLayer.getExtent());
                        result = iAgDBStats.statsByWKT(layer, wkt);
                    } else {
                        result = iAgDBStats.statsByExtent(layer, range);
                    }
                } else if (type.equals("polyline")) {
                    List<Point> pointList = SpatialUtil.formatPoints(points);
                    if (GeometryApplication.checkWkt(agUserLayer.getExtent())) {
                        String wkt = GeometryOperate.intersect(SpatialUtil.pointListToWKT(pointList, LINESTRING), agUserLayer.getExtent());
                        result = iAgDBStats.statsByWKT(layer, wkt);
                    } else {
                        result = iAgDBStats.statsByPolyline(layer, pointList);
                    }
                } else if (type.equals("envelop") || type.equals("polygon")) {
                    List<Point> pointList = SpatialUtil.formatPoints(points);
                    if (GeometryApplication.checkWkt(agUserLayer.getExtent())) {
                        String wkt = GeometryOperate.intersect(SpatialUtil.pointListToWKT(pointList, POLYGON), agUserLayer.getExtent());
                        result = iAgDBStats.statsByWKT(layer, wkt);
                    } else {
                        result = iAgDBStats.statsByPolygon(layer, pointList);
                    }
                } else if (type.equals("geoJson")) {
                    String wkt = SpatialUtil.jsonToWkt(points);
                    if (GeometryApplication.checkWkt(agUserLayer.getExtent())) {
                        wkt = GeometryOperate.intersect(wkt, agUserLayer.getExtent());
                    }
                    result = iAgDBStats.statsByWKT(layer, wkt);
                } else if (type.equals("wkt")) {
                    String wkt = points;
                    if (GeometryApplication.checkWkt(agUserLayer.getExtent())) {
                        wkt = GeometryOperate.intersect(wkt, agUserLayer.getExtent());
                    }
                    result = iAgDBStats.statsByWKT(layer, wkt);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 查询矢量数据
     *
     * @param dirLayerId 目录图层Id
     * @param userId     用户Id
     * @param where      属性查询where条件
     * @param type       空间查询类型 All（全图） Extent（范围） Polyline（线） Envelop||Polygon（面）
     * @param points     点数组 格式参考leaflet LatLon
     * @return
     */
    @RequestMapping(value = "/queryVectorLayer",method = RequestMethod.POST)
    public List<Map> queryVectorLayer(String dirLayerId, String userId, String where, String type, String points) {
        List<Map> result = new ArrayList<Map>();
        try {
            //获取图层基本信息
            AgLayer agLayer = iAgDir.findLayerByDirLayerId(dirLayerId);
            //获取图层配置信息
            // AgRoleLayer agRoleLayer = iAgField.getLayerConfigByUserId(dirLayerId, userId);
            AgUserLayer agUserLayer = iAgField.getLayerConfigByUserId(dirLayerId, userId);
            if (agLayer.getLayerType().startsWith("01")) {//矢量图层判断
                Layer layer = ReflectBeans.copy(agLayer, Layer.class);
                //设置查询字段
                layer.setFields(null);
                //设置where条件
                layer.setWhere(where);
                if (type.equals("all")) {
                    result = iAgDBQuery.queryByWKT(layer, agUserLayer.getExtent());
                } else if (type.equals("extent")) {
                    List<Point> pointList = SpatialUtil.formatPoints(points);
                    Range range = new Range(pointList.get(0), pointList.get(1));
                    if (GeometryApplication.checkWkt(agUserLayer.getExtent())) {
                        String wkt = GeometryOperate.intersect(SpatialUtil.rangeToWKT(range), agUserLayer.getExtent());
                        result = iAgDBQuery.queryByWKT(layer, wkt);
                    } else {
                        result = iAgDBQuery.queryByExtent(layer, range);
                    }
                } else if (type.equals("polyline")) {
                    List<Point> pointList = SpatialUtil.formatPoints(points);
                    if (GeometryApplication.checkWkt(agUserLayer.getExtent())) {
                        String wkt = GeometryOperate.intersect(SpatialUtil.pointListToWKT(pointList, LINESTRING), agUserLayer.getExtent());
                        result = iAgDBQuery.queryByWKT(layer, wkt);
                    } else {
                        result = iAgDBQuery.queryByPolyline(layer, pointList);
                    }
                } else if (type.equals("envelop") || type.equals("polygon")) {
                    List<Point> pointList = SpatialUtil.formatPoints(points);
                    if (GeometryApplication.checkWkt(agUserLayer.getExtent())) {
                        String wkt = GeometryOperate.intersect(SpatialUtil.pointListToWKT(pointList, POLYGON), agUserLayer.getExtent());
                        result = iAgDBQuery.queryByWKT(layer, wkt);
                    } else {
                        result = iAgDBQuery.queryByPolygon(layer, pointList);
                    }
                } else if (type.equals("geoJson")) {
                    String wkt = SpatialUtil.jsonToWkt(points);
                    if (GeometryApplication.checkWkt(agUserLayer.getExtent())) {
                        wkt = GeometryOperate.intersect(wkt, agUserLayer.getExtent());
                    }
                    result = iAgDBQuery.queryByWKT(layer, wkt);
                } else if (type.equals("wkt")) {
                    String wkt = points;
                    if (GeometryApplication.checkWkt(agUserLayer.getExtent())) {
                        wkt = GeometryOperate.intersect(wkt, agUserLayer.getExtent());
                    }
                    result = iAgDBQuery.queryByWKT(layer, wkt);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 保存图层信息
     *
     * @param dirLayerId 目录图层Id
     * @param data       图层信息json
     * @return
     */
    @RequestMapping(value = "/saveLayer",method = RequestMethod.POST)
    public String saveLayer(String dirLayerId, String data) {
        String result = JsonUtils.toJson(new ResultForm(true));
        try {
            //获取图层基本信息
            AgLayer agLayer = iAgDir.findLayerByDirLayerId(dirLayerId);
            if (agLayer.getLayerType().startsWith("01")) {
                JSONObject dataObj = JSONObject.fromObject(data);
                result = JsonUtils.toJson(new ResultForm(iAgLayer.saveVectorLayer(agLayer, dataObj)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除图层
     *
     * @param dirLayerId 目录图层Id
     * @param data       图层信息json
     * @return
     */
    @RequestMapping(value = "/deleteLayer",method = RequestMethod.POST)
    public String deleteLayer(String dirLayerId, String data) {
        String result = JsonUtils.toJson(new ResultForm(true));
        try {
            //获取图层基本信息
            AgLayer agLayer = iAgDir.findLayerByDirLayerId(dirLayerId);
            if (agLayer.getLayerType().startsWith("01")) {
                JSONObject dataObj = JSONObject.fromObject(data);
                result = JsonUtils.toJson(new ResultForm(iAgLayer.deleteVectorLayer(agLayer, dataObj)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 矢量渲染
     *
     * @param dirLayerId 图层目录Id
     * @param userId     用户Id
     * @param extent     地图当前显示范围
     * @param zoom       地图当前显示等级
     * @return
     */
    @RequestMapping(value = "/loadVectorLayer",method = RequestMethod.POST)
    public String loadVectorLayer(String dirLayerId, String userId, String extent, int zoom) {
        Map result = new HashMap();
        try {
            //获取图层基本信息
            AgLayer agLayer = iAgDir.findLayerByDirLayerId(dirLayerId);
            if(userId == null) {
                List<AgLayerFieldConf> layerField = iAgField.getLayerFieldsByUserId(dirLayerId, agLayer.getId());
                //获取样式配置
                Map styleConfMap = iAgStyle.getStyleConfMap(agLayer);
                List<Map> resultData = new ArrayList<Map>();
                if (agLayer.getLayerType().startsWith("01")) {//矢量图层判断
                    Layer layer = ReflectBeans.copy(agLayer, Layer.class);
                    List<String> fields = new ArrayList<String>();
                    for (AgLayerFieldConf agLayerField : layerField) {
                        if ("1".equals(agLayerField.getViewInResult())) {
                            fields.add(agLayerField.getFieldName());
                        }
                    }
                    //设置查询字段
                    layer.setFields(fields);
                    List<Point> pointList = SpatialUtil.formatPoints(extent);
                    Range range = SpatialUtil.getRangeByPointList(pointList);
                    resultData = iAgDBQuery.queryByExtent(layer, range);
                }
                result.put("data", resultData);
            }else {


            //获取图层权限信息
            //AgRoleLayer roleLayer = iAgField.getLayerConfigByUserId(dirLayerId, userId);
            AgUserLayer userLayer = iAgField.getLayerConfigByUserId(dirLayerId, userId);
            //获取有权限的字段
            List<AgLayerFieldConf> layerField = iAgField.getLayerFieldsByUserId(dirLayerId, agLayer.getId());
            //获取样式配置
            Map styleConfMap = iAgStyle.getStyleConfMap(agLayer);
            List<Map> resultData = new ArrayList<Map>();
            if (agLayer.getLayerType().startsWith("01")) {//矢量图层判断
                Layer layer = ReflectBeans.copy(agLayer, Layer.class);
                List<String> fields = new ArrayList<String>();
                for (AgLayerFieldConf agLayerField : layerField) {
                    if ("1".equals(agLayerField.getViewInResult())) {
                        fields.add(agLayerField.getFieldName());
                    }
                }
                //设置查询字段
                layer.setFields(fields);
                List<Point> pointList = SpatialUtil.formatPoints(extent);
                Range range = SpatialUtil.getRangeByPointList(pointList);
                if (GeometryApplication.checkWkt(userLayer.getExtent())) {
                    String wkt = GeometryOperate.intersect(SpatialUtil.rangeToWKT(range), userLayer.getExtent());
                    resultData = iAgDBQuery.queryByWKT(layer, wkt);
                } else {
                    resultData = iAgDBQuery.queryByExtent(layer, range);
                }
                if ("point".equals(agLayer.getFeatureType()) && zoom > 0 && resultData.size() > 5) {
                    Evacuate evacuate = new Evacuate();
                    resultData = evacuate.beginEvacuate(resultData, zoom);
                }
                result.put("styleConf", styleConfMap);
                result.put("fieldConf", layerField);
                result.put("layerConf", userLayer);
                result.put("data", resultData);
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String s = JsonUtils.toJson(result);
        return JsonUtils.toJson(result);
    }

    /**
     * 获取一定时间点之后更新过的图层信息
     *
     * @param userId  用户Id
     * @param extent  当前地图显示范围
     * @param options
     * @return
     */
    @RequestMapping(value = "/getVectorLayerUpdate",method = RequestMethod.POST)
    public List<Map> getVectorLayerUpdate(String userId, String options, String extent) {
        List<Map> results = new ArrayList<Map>();
        List<HashMap> optionsList = com.common.util.JsonUtils.toList(options, HashMap.class);
        for (int j = 0; j < optionsList.size(); j++) {
            Map option = optionsList.get(j);
            try {
                String dirLayerId = String.valueOf(option.get("dirLayerId"));
                String beginTime = String.valueOf(option.get("beginTime"));
                Date beginDate = sdf.parse(beginTime);
                //获取图层基本信息
                AgLayer agLayer = iAgDir.findLayerByDirLayerId(dirLayerId);
                //获取图层配置信息
                //AgRoleLayer roleLayer = iAgField.getLayerConfigByUserId(dirLayerId, userId);
                AgUserLayer userLayer = iAgField.getLayerConfigByUserId(dirLayerId, userId);
                //获取有权限的字段
                List<AgLayerFieldConf> layerField = iAgField.getLayerFieldsByUserId(dirLayerId, userId);
                //获取样式配置
                Map styleConfMap = iAgStyle.getStyleConfMap(agLayer);
                if (agLayer.getLayerType().startsWith("01")) {//矢量图层判断
                    Layer layer = ReflectBeans.copy(agLayer, Layer.class);
                    List<String> fields = new ArrayList<String>();
                    for (AgLayerFieldConf agLayerField : layerField) {
                        if ("1".equals(agLayerField.getViewInResult())) {
                            fields.add(agLayerField.getFieldName());
                        }
                    }
                    fields.add(CREATETIME_COLUMN);
                    //设置查询字段
                    layer.setFields(fields);
                    List<Point> pointList = SpatialUtil.formatPoints(extent);
                    Range range = SpatialUtil.getRangeByPointList(pointList);
                    List<Map> resultData = new ArrayList<Map>();
                    List<Map> data = new ArrayList<Map>();
                    if (GeometryApplication.checkWkt(userLayer.getExtent())) {
                        String wkt = GeometryOperate.intersect(SpatialUtil.rangeToWKT(range), userLayer.getExtent());
                        data = iAgDBQuery.queryByWKT(layer, wkt);
                    } else {
                        data = iAgDBQuery.queryByExtent(layer, range);
                    }
                    for (int i = 0; i < data.size(); i++) {
                        Map map = data.get(i);
                        Date createDate = sdf.parse(String.valueOf(map.get(CREATETIME_COLUMN)));
                        if (createDate.after(beginDate)) {
                            resultData.add(map);
                        }
                    }
                    Map result = new HashMap();
                    result.put("option", option);
                    result.put("styleConf", styleConfMap);
                    result.put("fieldConf", layerField);
                    result.put("layerConf", userLayer);
                    result.put("data", resultData);
                    results.add(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    /**
     * 按图层id查找
     *
     * @param layerId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getExtentByLayerId",method = RequestMethod.GET)
    public String getExtentByLayerId(String layerId) throws Exception {
        AgLayer agLayer = iAgLayer.findByLayerId(layerId);
        return agLayer.getExtent();
    }

    /**
     * 获取所有代理地址
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getAgentUrlList",method = RequestMethod.GET)
    public List<AgLayer> getAentUrlList()throws Exception{
        List<AgLayer> list = iAgLayer.findAllAgentUrlList();
        return list;
    }

    /**
     * 获取矢量图层的数据源名称，表名称，根据数据源ID进行归类
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取矢量图层的数据源名称，表名称",notes = "获取矢量图层的数据源名称，表名称")
    @RequestMapping(value = "/findVectorLayerTree",method = RequestMethod.GET)
    public ContentResultForm findVectorLayer()throws Exception{
        List<AgLayer> list = iAgLayer.getVectorLayer();
        List<Map<String,String>> layers = new ArrayList<>();
        Set<String> sets = new HashSet<>();
        for (AgLayer agLayer:list){
            JSONObject json = JSONObject.fromObject(agLayer.getData());
            String dataSourceId = json.getString("dataSourceId");
            String layerTable = json.getString("layerTable");
            Map<String ,String > map = new HashMap<>();
            map.put("dataSourceId",dataSourceId);
            map.put("layerId",agLayer.getId());
            map.put("layerTable",layerTable);
            map.put("layerTableCn",agLayer.getNameCn());
            layers.add(map);
            sets.add(dataSourceId);
        }
        //根据dataSourceId获取数据库名称
        List<Object> arrayList = new ArrayList<>();
        Map<String,Object> tree = new HashMap<>();
        List<Object> dataList = new ArrayList<>();//数据库列表
        tree.put("id","root");
        tree.put("name","数据库");
        tree.put("open",true);
        for (String id:sets){
            AgSupDatasource dataSource = iAgSupDatasource.selectDataSourceById(id);
            if (dataSource!=null){
                Map<String ,Object> dataMap = new HashMap<>();
                dataMap.put("id",id);
                dataMap.put("name",dataSource.getName());
                dataMap.put("pId","root");
                dataMap.put("open",false);
                List<Object> tableList = new ArrayList<>();//数据库表列表
                for (Map<String,String> map :layers){
                    if (map.get("dataSourceId").equals(id)){
                        Map<String ,Object> tableMap = new HashMap<>();
                        tableMap.put("id",map.get("layerId"));
                        tableMap.put("pId",id);
                        tableMap.put("name",map.get("layerTableCn"));
                        tableMap.put("layerTable",map.get("layerTable"));
                        tableMap.put("open",true);
                        tableList.add(tableMap);
                    }
                }
                dataMap.put("children",tableList);
                dataList.add(dataMap);
            }
        }
        tree.put("children",dataList);
        String str = JSONArray.fromObject(tree).toString();
        return new ContentResultForm(true,str,"获取矢量图层的数据源名称，表名称");
    }

    /**
     * 用户拥有专题的所有图层数
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取用户的专题下的图层数",notes = "获取用户的专题下的图层数接口")
    @ApiImplicitParam(value = "用户名称",name = "userId",dataType = "string",required = true)
    @RequestMapping("/findUserlayerCount")
    public ContentResultForm findUserlayerCount(String userId) throws Exception{
        Integer userlayerCount = iAgLayer.findUserlayerCount(userId);
        return new ContentResultForm(true,userlayerCount);
    }
}
