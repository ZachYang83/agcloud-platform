package com.augurit.agcloud.agcom.agsupport.sc.elasticsearch.controller;

import com.augurit.agcloud.agcom.agsupport.common.util.DESedeUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgIndexConfigInfo;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgSupDatasource;
import com.augurit.agcloud.agcom.agsupport.sc.elasticsearch.service.IAgIndexConfigInfo;
import com.augurit.agcloud.agcom.agsupport.sc.elasticsearch.util.HttpClientUtil;
import com.augurit.agcloud.agcom.agsupport.sc.agdatasource.service.IAgSupDatasource;
import com.augurit.agcloud.agcom.agsupport.sc.dataUpdate.dao.AgDataUpdateDao;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgLayer;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :10:41 2018/12/20
 * @Modified By:
 */
@Api(value = "全文检索管理接口",description = "全文检索管理接口")
@RestController
@RequestMapping("/agsupport/elasticsearch")
public class AgElasticSearchController {

    @Autowired
    private IAgSupDatasource supDatasource;
    @Autowired
    private IAgLayer iAgLayer;

    @Autowired
    private IAgDir iAgDir;

    @Autowired
    private AgDataUpdateDao agDataUpdateDao;

    //搜索引擎地址
    @Value("${address.url}")
    private String addressUrl;

    @Autowired
    private IAgIndexConfigInfo iAgIndexConfigInfo;

    @RequestMapping(value = "/index.html")
    public ModelAndView index(Model model) {
        model.addAttribute("elasticsearchAddress",addressUrl);
        return new ModelAndView("agcloud/agcom/agsupport/elasticsearch/index");
    }

    @ApiOperation(value = "获取所有数据源名称",notes = "获取所有数据源名称")
    @RequestMapping(value = "/getDatabaseNames",method = RequestMethod.GET)
    public ContentResultForm getDatabaseNames() {
        try {
            List<Object> result = new ArrayList<>();
            List<AgSupDatasource> dataSourceList = supDatasource.findAllList();
            for (AgSupDatasource dataSource : dataSourceList) {
                JSONObject json = new JSONObject();
                json.put("dataSourceName", dataSource.getName());
                json.put("dataSouceId", dataSource.getId());
                result.add(json);
            }
            return new ContentResultForm(true,result,"获取所有数据源名称");
        } catch (Exception e) {
            return new ContentResultForm(false,"","获取所有数据源名称失败");
        }
    }

    @ApiOperation(value = "根据数据源id获取表",notes = "根据数据源id获取表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSouceId",required = true, value = "数据源id", dataType = "String")
    })
    @RequestMapping(value = "/getTablesByDataSourceId",method = RequestMethod.POST)
    public ContentResultForm getTablesByDataBaseName(String dataSouceId) {
        try {
            List<AgLayer> list = iAgLayer.getVectorLayer();
            List<Object> result = new ArrayList<>();
            for (AgLayer agLayer : list) {
                JSONObject dataJson = JSONObject.fromObject(agLayer.getData());
                String sourceId = dataJson.getString("dataSourceId");
                String layerTable = dataJson.getString("layerTable");
                if (dataSouceId.equals(sourceId)) {
                    JSONObject json = new JSONObject();
                    json.put("tableName", layerTable);
                    json.put("tableNameCn", agLayer.getNameCn());
                    json.put("layerId", agLayer.getId());
                    result.add(json);
                }
            }
            return new ContentResultForm(true,result,"根据数据源id获取表");
        } catch (Exception e) {
            return new ContentResultForm(true,"","根据数据源id获取表失败");
        }
    }

    @ApiOperation(value = "根据数据源id表名获取表字段",notes = "根据数据源id表名获取表字段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSouceId",required = true, value = "数据源id", dataType = "String"),
            @ApiImplicitParam(name = "tableName",required = true, value = "表名", dataType = "String"),
            @ApiImplicitParam(name = "layerId",required = true, value = "表名id", dataType = "String")

    })
    @RequestMapping(value = "/getFields",method = RequestMethod.POST)
    public ContentResultForm getFields(String dataSourceId,String tableName,String layerId){
        try {
            LinkedList<Map<String, String>> titleAlias = iAgDir.findTableFields(dataSourceId,tableName,layerId);
            List<Map<String, String>> result = new ArrayList<>();
            for (Map<String, String> map:titleAlias){
                result.add(map);
            }
            return new ContentResultForm(true,result,"根据数据源id表名获取表字段");
        }catch (Exception e){
            return new ContentResultForm(false,"","获取表字段失败");
        }
    }

    /**
     * 获取图层字段别名
     * @param layerId
     * @return
     */
    @ApiOperation(value = "获取图层字段别名",notes = "获取图层字段别名")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "layerId",required = true, value = "图层id", dataType = "String")
    })
    @RequestMapping(value = "/getFieldsByLayerId",method = RequestMethod.POST)
    public ContentResultForm getFieldsByLayerId(String layerId){
        try {
            Map<String,String> alias = agDataUpdateDao.getFieldNameAlias(layerId);
            return new ContentResultForm(true,alias,"获取图层字段别名");
        }catch (Exception e){
            return new ContentResultForm(false,"","获取图层字段别名失败");
        }
    }


    /**
     *创建索引
     * @param dataSourceId
     * @param tableName
     * @param tableNameCn
     * @param fields 索引字段
     * @param noFenCiFields 索引字段中的不分词字段
     * @param showField 结果显示字段
     * @param editFlag 是否是修改索引那里重建的索引 是：true
     * @return
     */
    @RequestMapping("/createIndex")
    public String createIndex(@RequestParam String dataSourceId,@RequestParam String layerId,
                              @RequestParam String tableName,String tableNameCn,@RequestParam String fields,
                              @RequestParam String noFenCiFields,String showField,boolean editFlag) {
        try {
            AgIndexConfigInfo agIndexConfigInfo = iAgIndexConfigInfo.findByIndexName(tableName);
            if (agIndexConfigInfo!=null && !editFlag) return JsonUtils.toJson(new ContentResultForm(false, "", "该索引已经创建,请删除后再创建或者重建索引"));
            AgSupDatasource datasource = supDatasource.selectDataSourceById(dataSourceId);
            String dbUrl = datasource.getDbUrl();
            String userName = datasource.getUserName();
            String password = datasource.getPassword();
            String dbType = datasource.getDbType();

            //从地图资源管理获取注册矢量图层的空间字段
            AgLayer agLayer = iAgLayer.findByLayerId(layerId);
            JSONObject dataJson = JSONObject.fromObject(agLayer.getData());
            String geometryColumn = "";
            if (dataJson.has("geometryColumn")){
                geometryColumn = dataJson.getString("geometryColumn");
            }

            //对数据库信息进行加密
            dbUrl = DESedeUtil.desEncrypt(dbUrl);
            userName = DESedeUtil.desEncrypt(userName);
            //password = DESedeUtil.desEncrypt(password);


            Map<String,String> param = new HashMap<>();
            param.put("dbUrl",dbUrl);
            param.put("userName",userName);
            param.put("password",password);
            param.put("dbType",dbType);
            param.put("tableName",tableName);
            param.put("fields",fields);
            param.put("noFenCiFields",noFenCiFields);
            param.put("geometryColumn",geometryColumn);
            param.put("showField",showField);
            param.put("rtnType","json");
            String result = HttpClientUtil.HttpPost(addressUrl+"/createIndex2",param);
            if (result==null)return JsonUtils.toJson(new ContentResultForm(false, "", "无法连接搜索引擎"));
            JSONObject object = JSONObject.fromObject(result);
            JSONObject json = new JSONObject();
            if (object.getBoolean("success")) {
                json = JSONObject.fromObject( object.get("content"));
                AgIndexConfigInfo info = new AgIndexConfigInfo();
                info.setId(UUID.randomUUID().toString());
                info.setDbname(datasource.getName());
                info.setDataSourceId(dataSourceId);
                info.setIndexName(tableName.toLowerCase());
                info.setTableName(tableName);
                info.setLayerId(layerId);
                info.setTableNameCn(tableNameCn);
                info.setDocNumber(Integer.parseInt(json.getString("total")));
                info.setCoordinateType(json.getString("coordinateType"));
                info.setIsWgs84(json.getBoolean("isWgs84")? "经纬度":"其他");
                info.setIndexField(fields);
                info.setNotAnalysisWord(noFenCiFields);
                info.setCreateTime(new Date());
                info.setShowField(showField);
                if (editFlag){
                    info.setId(agIndexConfigInfo.getId());
                    iAgIndexConfigInfo.updateAgIndexConfigInfo(info);
                }else {
                    iAgIndexConfigInfo.saveAgIndexConfigInfo(info);
                }
            }
            return JsonUtils.toJson(new ContentResultForm(true, json, ""));
        }catch (Exception e){
            return JsonUtils.toJson(new ContentResultForm(false, "", "创建失败"));
        }
    }

    /**
     * 重建索引
     * @param id
     * @return
     */
    @RequestMapping("/rebuildIndex")
    public String createIndex(@RequestParam String id){
        try {
            AgIndexConfigInfo info = iAgIndexConfigInfo.findById(id);
            AgSupDatasource datasource = supDatasource.selectDataSourceById(info.getDataSourceId());
            String dbUrl = datasource.getDbUrl();
            String userName = datasource.getUserName();
            String password = datasource.getPassword();
            String dbType = datasource.getDbType();

            //对数据库信息进行加密
            dbUrl = DESedeUtil.desEncrypt(dbUrl);
            userName = DESedeUtil.desEncrypt(userName);
            //password = DESedeUtil.desEncrypt(password);


            //从地图资源管理获取注册矢量图层的空间字段
            AgLayer agLayer = iAgLayer.findByLayerId(info.getLayerId());
            JSONObject dataJson = JSONObject.fromObject(agLayer.getData());
            String geometryColumn = "";
            if (dataJson.has("geometryColumn")){
                geometryColumn = dataJson.getString("geometryColumn");
            }

            Map<String,String> param = new HashMap<>();
            param.put("dbUrl",dbUrl);
            param.put("userName",userName);
            param.put("password",password);
            param.put("dbType",dbType);
            param.put("tableName",info.getTableName());
            param.put("fields",info.getIndexField());
            param.put("noFenCiFields",info.getNotAnalysisWord());
            param.put("geometryColumn",geometryColumn);
            param.put("showField",info.getShowField());
            param.put("rtnType","json");
            String result = HttpClientUtil.HttpPost(addressUrl+"/createIndex2",param);
            if (result==null)return JsonUtils.toJson(new ContentResultForm(false, "", "无法连接搜索引擎"));
            return result;
        }catch (Exception e){
            return JsonUtils.toJson(new ContentResultForm(false, "", "重建失败"));
        }
    }

    /**
     * 根据索引名称获取正在创建索引的进度
     * @param indexName
     * @return
     */
    @RequestMapping("/getRateCount")
    public String getRateCount(@RequestParam String indexName) {
        try {
            Map<String, String> param = new HashMap<>();
            param.put("indexName", indexName);
            String result = HttpClientUtil.HttpPost(addressUrl + "/getRateCount", param);
            JSONObject object = JSONObject.fromObject(result);
            if (object.getBoolean("success")) {
                return object.toString();
            } else {
                return JsonUtils.toJson(new ContentResultForm(false, "", "获取进度失败"));
            }
        } catch (Exception e) {
            return JsonUtils.toJson(new ContentResultForm(false, "", "获取进度失败"));
        }
    }

    /**
     * 数据查询接口
     * @param keyword
     * @param indexNames
     * @param fields
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/ordinarySearch")
    public String ordinarySearch(@RequestParam String keyword, String indexNames, String fields,
                                 @RequestParam int pageNum, @RequestParam int pageSize) {
        JSONObject json = new JSONObject();
        try {
            Map<String, String> param = new HashMap<>();
            param.put("keyword", keyword);
            param.put("indexNames", indexNames);
            param.put("fields", fields);
            param.put("pageNum", String.valueOf(pageNum));
            param.put("pageSize", String.valueOf(pageSize));
            String result = HttpClientUtil.HttpPost(addressUrl + "/ordinarySearch", param);

            if (result==null){
                json.put("list", "");
                json.put("total", 0);
                json.put("totalPages", 0);
                return JsonUtils.toJson(new ContentResultForm(false, json, "无法连接搜索引擎"));
            }
            return JsonUtils.toJson(new ContentResultForm(true, result, ""));
        } catch (Exception e) {
            return JsonUtils.toJson(new ContentResultForm(false, json, "查询失败"));
        }
    }
}
