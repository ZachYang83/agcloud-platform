package com.augurit.agcloud.agcom.agsupport.sc.attrTable.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayerFieldConf;
import com.augurit.agcloud.agcom.agsupport.sc.attrTable.service.IAgAttrTable;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.field.service.impl.AgFieldImpl;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by chendingxing on 2017-08-02.
 */
@RestController
@RequestMapping("/agsupport/attrTable")
public class AgAttrTableController {

    @Autowired
    private IAgAttrTable iAgAttrTable;

    @Autowired
    private IAgDir iAgDir;

    @Autowired
    private AgFieldImpl field;

    /**
     *
     * @param layerId 属性表ID  传layerId就可以不传数据源ID和属性表名
     * @param dataSourceId 数据源ID
     * @param layerTable 属性表名
     * @param select  查询字段
     * @param where   查询条件
     * @param startRow 查询起始行
     * @param endRow   查询结束行
     * @param orderBy  排序字段
     * @return
     * @throws Exception
     */
    @RequestMapping("/query_1")
    public String query_1(String layerId,String dataSourceId,String layerTable,
                        String select,String where,String startRow,String endRow,String orderBy) throws Exception{
        Layer layer = new Layer();
        if(layerId != null){
            AgLayer table = iAgDir.findLayerByLayerId(layerId);
            if(table == null) JsonUtils.toJson(new ResultForm(false,"查询不到对应的属性表"));
            layer.setDataSourceId(table.getDataSourceId());
            layer.setLayerTable(table.getLayerTable());
        }else if(dataSourceId != null && layerTable != null){
            layer.setDataSourceId(dataSourceId);
            layer.setLayerTable(layerTable);
        }else{
            return JsonUtils.toJson(new ResultForm(false,"查询不到对应的属性表"));
        }
        layer.setWhere(where);
        if(select != null && !"*".equals(select)){
            String[] fields = select.split(",");
            layer.setFields(Arrays.asList(fields));
        }
        Page page = new Page();
        page.setStartRow(startRow == null ? 0 : Integer.parseInt(startRow));
        page.setEndRow(endRow == null ? 0 : Integer.parseInt(endRow));
        page.setOrderBy(orderBy);
        return JsonUtils.toJson(iAgAttrTable.query(layer,page));
    }

    @RequestMapping("/query")
    public String query(String userId,String dirLayerId,String where,String startRow,String endRow,String orderBy) throws Exception{
        Layer layer = new Layer();
        AgLayer table = iAgDir.findLayerByDLidAndUid(dirLayerId,userId);
        if(table != null){
            layer.setDataSourceId(table.getDataSourceId());
            layer.setLayerTable(table.getLayerTable());
        }else{
            return JsonUtils.toJson(new ResultForm(false,"查询不到对应的属性表"));
        }
        List<String> queryFields = new ArrayList<>();
        List<AgLayerFieldConf> allFields = field.getLayerFieldsByUserId(dirLayerId, userId);
        for(AgLayerFieldConf alfc : allFields){
            if("1".equals(alfc.getViewInResult())){
                queryFields.add(alfc.getFieldName());
            }
        }
        layer.setFields(queryFields);
        layer.setWhere(where);
        Page page = new Page();
        page.setStartRow(startRow == null ? 0 : Integer.parseInt(startRow));
        page.setEndRow(endRow == null ? 0 : Integer.parseInt(endRow));
        page.setOrderBy(orderBy);
        return JsonUtils.toJson(iAgAttrTable.query(layer,page));
    }

    @RequestMapping("/update")
    public String update(String layerId,String dataSourceId,String layerTable,String update,String where){
        try{
            AgLayer table = iAgDir.findLayerByLayerId(layerId);
            if(table == null && dataSourceId != null && layerTable != null){
                table = new AgLayer();
                table.setLayerTable(layerTable);
                table.setDataSourceId(dataSourceId);
            }
            int cols = iAgAttrTable.update(table, update, where);
            return JsonUtils.toJson(new ResultForm(true,"更新" + cols + "行成功"));
        }catch (Exception e){
            return JsonUtils.toJson(new ResultForm(false,"更新操作失败！"));
        }
    }

    @RequestMapping("/insert")
    public String insert(String layerId,String insert){
        try{
            AgLayer table = iAgDir.findLayerByLayerId(layerId);
            if(table == null) return JsonUtils.toJson(new ResultForm(false,"查找不到属性表！"));
            Map map = new HashMap<>();
            String[] kvs = insert.split(",");
            for(String kvstr : kvs){
                String[] temp = kvstr.split("=");
                map.put(temp[0],temp[1]);
            }
            int cols = iAgAttrTable.insert(table,Arrays.asList(map));
            return JsonUtils.toJson(new ResultForm(true,"插入" + cols + "行成功"));
        }catch (Exception e){
            return JsonUtils.toJson(new ResultForm(false,"插入操作失败！"));
        }
    }

    @RequestMapping("/delete")
    public String delete(String layerId,String dataSourceId,String layerTable,String where){
        try{
            AgLayer table = iAgDir.findLayerByLayerId(layerId);
            if(table == null && dataSourceId != null && layerTable != null){
                table = new AgLayer();
                table.setLayerTable(layerTable);
                table.setDataSourceId(dataSourceId);
            }
            Map map = new HashMap<>();
            String[] kvs = where.split("=");
            String column = kvs[0];
            String[] value = kvs[1].split(",");
            int cols = iAgAttrTable.delete(table,column,value);
            return JsonUtils.toJson(new ResultForm(true,"删除成功"));
        }catch (Exception e){
            return JsonUtils.toJson(new ResultForm(false,"删除操作失败！"));
        }
    }
}
