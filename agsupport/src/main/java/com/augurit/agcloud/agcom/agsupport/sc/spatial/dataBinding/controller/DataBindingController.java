package com.augurit.agcloud.agcom.agsupport.sc.spatial.dataBinding.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.dataBinding.service.IDataBinding;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.util.SpatialUtil;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.common.dbcp.DBHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.augurit.agcloud.agcom.agsupport.sc.spatial.config.SpatialConfig.WKT_COLUMN;

/**
 * Created by chendingxing on 2018-03-15.
 */
@Api(value = "数据结合",description = "数据结合相关接口")
@RestController
@RequestMapping("/agsupport/dataBinding")
public class DataBindingController {

    @Autowired
    private IDataBinding iDataBinding;

    /**
     *
     * @param userId
     * @param type 图层类型
     * @param featureType point polyline polygon
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据用户id和图层类型，查找图层，并使用featureType过滤",notes = "根据用户id和图层类型，查找图层，并使用featureType过滤接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "图层类型", dataType = "String"),
            @ApiImplicitParam(name = "featureType", value = "point polyline polygon", dataType = "String")
    })
    @RequestMapping(value = "/findLayerByType",method = RequestMethod.GET)
    public ContentResultForm findLayerByType(String userId, String type, String featureType) throws Exception {
        List<AgLayer> layers = iDataBinding.findListByUserIdAndType(userId, type, featureType);
        return new ContentResultForm(true,JsonUtils.toJson(layers));
    }

    @ApiOperation(value = "根据用户id和图层类型，查找图层",notes = "根据用户id和图层类型，查找图层接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "图层类型", dataType = "String")
    })
    @RequestMapping(value = "/findLayer",method = RequestMethod.GET)
    public ContentResultForm findLayer(String userId,String type) throws Exception {
        List<AgLayer> layers = iDataBinding.findAllByUserId(userId, type);
        return new ContentResultForm(true,JsonUtils.toJson(layers));
    }

    @ApiOperation(value = "根据用户id和图层类型，查找编辑图层",notes = "根据用户id和图层类型，查找编辑图层接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "图层类型", dataType = "String")
    })
    @RequestMapping(value = "/findEditableLayer",method = RequestMethod.GET)
    public ContentResultForm findEditableLayer(String userId,String type) throws Exception {
        List<AgLayer> layers = iDataBinding.findAllEditableByUserId(userId, type);
        return new ContentResultForm(true,layers);
    }

    @ApiOperation(value = "根据用户id和图层ID，查找数据",notes = "根据用户id和图层ID，查找数据接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String"),
            @ApiImplicitParam(name = "dirLayerId", value = "图层ID", dataType = "String")
    })
    @RequestMapping(value = "/getBindingData",method = RequestMethod.GET)
    public ContentResultForm getBindingData(String userId, String dirLayerId, HttpServletRequest request) throws Exception {
        String attrWhere = request.getParameter("attrWhere");
        String spatialWhere = request.getParameter("spatialWhere");
        List<String> conditions = new ArrayList<>();
        conditions.add(attrWhere);//第一个默认是 属性表条件
        conditions.add(spatialWhere);//第二个是空间表条件
        List<Map> heatData = iDataBinding.findBindingData(userId,dirLayerId, conditions);
        return new ContentResultForm(true,heatData);
    }
    @ApiOperation(value = "根据用户id和表名，查找数据",notes = "根据用户id和表名，查找数据接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String"),
            @ApiImplicitParam(name = "tableName", value = "表名", dataType = "String")
    })
    @RequestMapping(value = "/queryData",method = RequestMethod.GET)
    public ContentResultForm queryData(String userId, String tableName, HttpServletRequest request) throws Exception {
        String attrWhere = request.getParameter("attrWhere");
        String spatialWhere = request.getParameter("spatialWhere");
        List<String> conditions = new ArrayList<>();
        conditions.add(attrWhere);//第一个默认是 属性表条件
        conditions.add(spatialWhere);//第二个是空间表条件
        List<Map> heatData = iDataBinding.queryData(userId,tableName, conditions);
        return new ContentResultForm(true,heatData);
    }

    @ApiOperation(value = "查找表字段",notes = "查找表字段接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirLayerId", value = "目录图层ID", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String")
    })
    @RequestMapping(value = "/findFieldByTableId",method = RequestMethod.GET)
    public ContentResultForm findFieldByTableId(String dirLayerId,String userId) throws Exception {
        List fields = iDataBinding.findFieldByTableId(dirLayerId,userId);
        return new ContentResultForm(true,fields);
    }

    @ApiOperation(value = "查找表所有字段",notes = "查找表所有字段接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirLayerId", value = "目录图层ID", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String")
    })
    @RequestMapping(value = "/findAllFieldByTableId",method = RequestMethod.GET)
    public ContentResultForm findAllFieldByTableId(String dirLayerId,String userId) throws Exception {
        List fields = iDataBinding.findAllFieldByTableId(dirLayerId,userId);
        return new ContentResultForm(true,fields);
    }

    @ApiOperation(value = "getChartsData",notes = "getChartsData接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "date", value = "", dataType = "String")
    })
    @RequestMapping(value = "/getChartsData",method = RequestMethod.GET)
    public ContentResultForm getChartsData(String date) throws Exception {
        List<Map> airmMaps = new ArrayList<>();
        String sql1 = "select \n" +
                "        t.dept_id dept_id,\n" +
                "        t.cust_type cust_type,\n" +
                "        sum(t.qty) qty \n" +
                "from \n" +
                "(select * from v_wl_stat_cust_dd_hy_day\n" +
                "         where cust_type is not null and qty_date='"+ date +"') t \n" +
                "         group by (t.dept_id,t.cust_type) order by dept_id, sum(t.qty) desc";
        List<Map> attrMaps = DBHelper.find("yw_db2", sql1, null);
        if(attrMaps.size() > 0){
            List<Map> spaMaps = new ArrayList<>();
            StringBuffer sql = new StringBuffer("select b.dept_id").append(",");
            sql.append(" db2gse.st_astext(b.").append("shape").append(") ").append(WKT_COLUMN);
            sql.append(" from ").append("g_wl_psz_pt_hy").append(" b");
            spaMaps = DBHelper.find("sde_db2", sql.toString(), null);
            spaMaps = SpatialUtil.formatDataMapList(spaMaps);
            if(spaMaps.size() > 0){
                for(Map am : attrMaps){
                    for(Map sm: spaMaps){
                        if(am.containsKey("dept_id") && am.get("dept_id").equals(sm.get("dept_id"))){
                            am.putAll(sm);
                            airmMaps.add(am);
                            break;
                        }
                    }
                }
            }
        }
        return new ContentResultForm(true,airmMaps);
    }
}
