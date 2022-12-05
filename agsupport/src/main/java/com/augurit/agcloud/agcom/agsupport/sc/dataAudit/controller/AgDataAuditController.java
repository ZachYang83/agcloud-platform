package com.augurit.agcloud.agcom.agsupport.sc.dataAudit.controller;

import com.augurit.agcloud.agcom.agsupport.common.datasource.JDBCUtils;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.sc.dataAudit.service.AgDataAuditDaoService;
import com.augurit.agcloud.agcom.agsupport.sc.dataAudit.util.ConnTimeOutUtil;
import com.augurit.agcloud.agcom.agsupport.sc.dataUpdate.dao.AgDataUpdateDao;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgLayer;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Connection;
import java.util.*;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :15:03 2019/2/20
 * @Modified By:
 */
@Api(value = "数据审核接口",description = "数据审核接口")
@RestController
@RequestMapping("/agsupport/dataAudit")
public class AgDataAuditController {

    @Autowired
    private AgDataAuditDaoService service;

    @Autowired
    private IAgDir iAgDir;

    @Autowired
    private IAgLayer iAgLayer;
    @Autowired
    private AgDataUpdateDao agDataUpdateDao;

    @RequestMapping("/index.html")
    public ModelAndView index(Model model) {
        return new ModelAndView("agcloud/agcom/agsupport/dataAudit/index");
    }

    /**
     *
     * @param , 1新增，2修改，3删除
     * @return
     */
    @ApiOperation(value = "分页获取数据审核信息",notes = "分页获取数据审核信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId",required = true, value = "数据源id", dataType = "String"),
            @ApiImplicitParam(name = "auditType",required = false, value = "审核类型：1新增，2修改，3删除", dataType = "int"),
            @ApiImplicitParam(name = "startTime",required = false, value = "查询开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime",required = false, value = "查询结束时间", dataType = "String"),
            @ApiImplicitParam(name = "page",required = true, value = "分页参数:/agsupport/param/paramList?page=1&rows=10&name=", dataType = "Page")
    })
    @RequestMapping(value = "/getPage",method = RequestMethod.POST)
    public ContentResultForm getPage(String dataSourceId, int auditType, String startTime, String endTime, Page page) {
        Connection conn = null;
        try {
            if ("root".equals(dataSourceId)) dataSourceId = "";//数据源为空或者root则是根目录
            //获取所有矢量图层
            List<AgLayer> layers = iAgLayer.getVectorLayer();
            List<AgLayer> list = new ArrayList<>();
            //根据数据源将其归类
            for (AgLayer agLayer:layers){
                JSONObject data = JSONObject.fromObject(agLayer.getData());
                String sourceId = data.getString("dataSourceId");
                String layerTable = data.getString("layerTable");
                if (dataSourceId.equalsIgnoreCase(sourceId)){
                    list.add(agLayer);
                }
            }
            //根据分页取list里面表的数量
            int pageNum = page.getPageNum();//从1开始
            int pageSize = page.getPageSize();
            int start = (pageNum-1) * pageSize;
            int end = pageNum * pageSize;
            List<AgLayer> pageList = new ArrayList<>();
            for (int i=0;i<list.size();i++){
                if (start<=i && i<end) pageList.add(list.get(i));
            }
            //查询每个表里面是否已经有提交是审核的数据，包括数据需要审核的类型  新增、修改、删除
            List<Map<String, Object>> result = new ArrayList<>();
            //conn = JDBCUtils.getConnection(dataSourceId);
            conn = ConnTimeOutUtil.getConnTimeOut(dataSourceId);
            for (AgLayer layer : pageList) {
                Map<String, Object> map = new HashMap<>();
                int add = 0, edit = 0, del = 0;
                JSONObject data = JSONObject.fromObject(layer.getData());
                String layerTable = data.getString("layerTable");
                //提交的每条数数据的类型 新增、修改、删除(临时表)
                if (conn == null) break;
                List<Integer> dataTypeList =  service.findList(conn, layerTable,auditType,startTime,endTime);
                if (dataTypeList==null) continue;
                for (Integer type : dataTypeList){
                    if (type == 1) add += 1;
                    if (type == 2) edit += 1;
                    if (type == 3) del += 1;
                }
                //组装每一张表的结果
                map.put("dataSourceId",dataSourceId);
                map.put("layerId",layer.getId());
                map.put("layerTable",layerTable);
                map.put("layerTableCn",layer.getNameCn());
                map.put("add",add);
                map.put("edit",edit);
                map.put("del",del);
                map.put("notAudit",add+edit+del);
                //如果没有审核数据的表则过滤不反回
                if ((add+edit+del)>0) result.add(map);
            }
            //返回结果
            JSONObject json = new JSONObject();
            json.put("rows",result);
            json.put("total",result.size());
            if (conn==null) return new ContentResultForm(false,json,"数据库连接超时");
            return new ContentResultForm(true,json,"审核信息数据");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,"","获取失败");
        }finally {
            JDBCUtils.close(conn,null,null);
        }
    }

    /**
     *获取表的审核数据信息
     * @param dataSourceId
     * @param layerTable
     * @param layerId
     * @param auditType 1新增，2修改，3删除,4全部
     * @return
     */
    @ApiOperation(value = "分页获取表的审核数据信息",notes = "分页获取表的审核数据信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId",required = true, value = "数据源id", dataType = "String"),
            @ApiImplicitParam(name = "layerTable",required = true, value = "表名", dataType = "String"),
            @ApiImplicitParam(name = "layerId",required = true, value = "表名id", dataType = "String"),
            @ApiImplicitParam(name = "startTime",required = true, value = "查询开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTiem",required = true, value = "查询结束时间", dataType = "String"),
            @ApiImplicitParam(name = "pageNum",required = true, value = "第几页", dataType = "int"),
            @ApiImplicitParam(name = "pageSize",required = true, value = "每页条数", dataType = "int"),
            @ApiImplicitParam(name = "auditType",required = true, value = "审核类型：1新增，2修改，3删除,4全部", dataType = "int")
    })
    @RequestMapping(value = "/getLayerTableInfoPage",method = RequestMethod.POST)
    public ContentResultForm getLayerTableInfoPage(String dataSourceId, String layerTable,String layerId,String startTime,String endTime, int pageNum,int pageSize,int auditType){
        try {
            //根据layerId获取中文注释
            LinkedList<Map<String, String>> tableFields = iAgDir.findTableFields(dataSourceId,layerTable,layerId);
            //判断表是否存在
            boolean isExistTable = agDataUpdateDao.isExistTable(dataSourceId,layerTable+"_TEMP");
            List<Map<String, String>> list = new ArrayList<>();
            int count = 0;
            if (isExistTable){
                list = service.getLayerTableInfoPage(dataSourceId,layerTable+"_TEMP",startTime,endTime,pageNum,pageSize,auditType,tableFields);
                count = service.getCount(dataSourceId,layerTable+"_TEMP");
            }
            //获取字段名称
            List<Object> fieldColumns = new ArrayList<>();
            Map<String, String> map  = new HashMap<>();
            Map<String, String> keyAndfValue  = new HashMap<>();
            if (list.size()>0){
                map = list.get(0);
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()){
                    Map.Entry entry = it.next();
                    String key = entry.getKey().toString();
                    if (!"上报时间".equals(key) && !"rn".equals(key) && !"操作".equals(key) && !"上报人".equals(key) && !"report_person".equals(key) && !"state".equals(key)&& !"audittype".equals(key)&& !"submit_time".equals(key)&& !"not_pass_reason".equals(key) && !"audit_time".equals(key)){
                        fieldColumns.add(entry.getKey());
                    }
                }
            }
            for (Map<String,String> tableField : tableFields){
                String fieldName = tableField.get("fieldName");
                String fieldNameCn = tableField.get("fieldNameAlias");
                keyAndfValue.put(fieldName,fieldNameCn);//字段名称，字段中文名称
            }
            JSONObject json = new JSONObject();
            json.put("fieldColumns",fieldColumns);
            json.put("keyAndfValue",keyAndfValue);
            json.put("rows",list);
            json.put("total",count);
            return new ContentResultForm(true,json,"分页获取表的审核数据信息");
        }catch (Exception e){
            return new ContentResultForm(true,"","分页获取表的审核数据信息失败");
        }
    }

    /**
     *
     * @param dataSourceId
     * @param layerTable
     * @param state 1,提交审核，2，审核通过，3，驳回
     * @param primaryKey
     * @param keyValues
     * * @param notPassReason 不通过理由
     * @return
     */
    @ApiOperation(value = "保存审核结果",notes = "保存审核结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId" ,value = "数据源id",dataType = "String"),
            @ApiImplicitParam(name = "layerTable" ,value = "表名",dataType = "String"),
            @ApiImplicitParam(name = "state" ,value = "审核状态：1,提交审核，2，审核通过，3，驳回",dataType = "int"),
            @ApiImplicitParam(name = "primaryKey" ,value = "主键",dataType = "String"),
            @ApiImplicitParam(name = "keyValues" ,value = "主键值",dataType = "String"),
            @ApiImplicitParam(name = "notPassReason" ,value = "驳回原因",dataType = "String")
    })
    @RequestMapping(value = "/saveAuditInfo",method = RequestMethod.POST)
    public ResultForm saveAuditInfo(String dataSourceId, String layerTable, int state, String primaryKey, String keyValues, String notPassReason){
        JSONObject json = new JSONObject();
        try {

            int len = service.saveAuditInfo(dataSourceId,layerTable,state,primaryKey,keyValues,notPassReason);
            return new ResultForm(true, "保存成功！");
        } catch (Exception e) {
            return new ResultForm(false, "保存失败！");
        }

    }

    /**
     * 获取数据详情
     * @param dataSourceId 数据源名称
     * @param layerTable 表名称
     * @param layerId 图层Id
     * @param primaryKey 主键名称
     * @param keyValue 主键值
     * @return [{"filedName":"","newValue":"","oldValue":""},...]
     */
    @ApiOperation(value = "获取数据详情",notes = "获取数据详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId" ,value = "数据源id",dataType = "String"),
            @ApiImplicitParam(name = "layerTable" ,value = "表名",dataType = "String"),
            @ApiImplicitParam(name = "layerId" ,value = "表名id",dataType = "String"),
            @ApiImplicitParam(name = "primaryKey" ,value = "主键",dataType = "String"),
            @ApiImplicitParam(name = "keyValues" ,value = "主键值",dataType = "String")
    })
    @RequestMapping(value = "/getDataDetail",method = RequestMethod.POST)
    public ContentResultForm getDataDetail(String dataSourceId, String layerTable, String layerId, String primaryKey, String keyValue) {
        try {
            if ("".equals(dataSourceId))  return new ContentResultForm(true,"","获取数据详情");
            LinkedList<Map<String, String>> fields = iAgDir.findTableFields(dataSourceId, layerTable, layerId); //获取字段名称 和别名
            Map<String, Object> dateList = agDataUpdateDao.compareData(dataSourceId, layerTable, primaryKey, keyValue);
            Map<String, Object> newData = (Map<String, Object>) dateList.get("new");//临时表表数据
            Map<String, Object> oldData = (Map<String, Object>) dateList.get("old");//原表数据
            List<Map<String, Object>> rows = new ArrayList<>();
            for (Map<String, String> field : fields) {
                String name = field.get("fieldName").toUpperCase();
                String value = field.get("fieldNameAlias").toUpperCase();
                Map<String, Object> map = new HashMap<>();
                map.put("fieldName", value);
                map.put("newValue", newData.get(name));
                map.put("oldValue", oldData.get(name));
                if ("SHAPE".equals(name)) continue;//不添加空间字段
                rows.add(map);
            }
            JSONObject json = new JSONObject();
            json.put("rows", JSONArray.fromObject(rows));
            json.put("total",rows.size());
            return new ContentResultForm(true,json,"获取数据详情");
        } catch (Exception e) {
            return new ContentResultForm(false,"","获取数据详情");
        }
    }
}
