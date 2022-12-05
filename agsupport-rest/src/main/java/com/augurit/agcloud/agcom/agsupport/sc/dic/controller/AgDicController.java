package com.augurit.agcloud.agcom.agsupport.sc.dic.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import com.augurit.agcloud.agcom.agsupport.sc.dic.service.IAgDic;
import com.augurit.agcloud.agcom.agsupport.sc.log.service.IAgLog;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcom.common.LoginHelpClient;
import com.common.util.Common;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Augurit on 2017-04-18.
 */
@Api(value = "数据字典",description = "数据字典相关接口")
@RestController
@RequestMapping("/agsupport/dic")
public class AgDicController {
    private static Logger logger = LoggerFactory.getLogger(AgDicController.class);
    @Autowired
    private IAgDic iAgDic;
    @Autowired
    private IAgLog log;
    @Value("${agcloud.opus.admin.orgId}")
    private String orgId;//组织id
    /**
     * 分页查询编码集
     *
     * @return
     */
/*    @RequestMapping(value = "/codeSetList",method = RequestMethod.GET)
    public EasyuiPageInfo<AgDic> codeSetList(AgDic agDic, HttpServletRequest request) throws Exception {
       *//* PageInfo<AgDic> pageInfo =iAgDic.findCodeSetList(agDic, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);*//*

        //调用agcloud接口获取
        //String orgId = SecurityContext.getCurrentOrgId();//组织id
        //String orgId="9c1599b1-0667-42da-9867-8425f53bd362";
        String typeKeyword = agDic.getTypeName();
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");

        PageInfo<AgDic> pageInfo = new PageInfo<>();
        Map<String, String> param = new HashMap<>();
        param.put("orgId", orgId);
        param.put("typeKeyword", typeKeyword);
        param.put("page", page);
        param.put("rows", rows);
        param.put("sort", "typeModifyTime");
        param.put("order", "desc");
        HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/bsc/dic/code/listTypesPage.do", param);
        JSONObject jsonObject = JSONObject.fromObject(httpRespons.getContent());
        int total = Integer.parseInt(jsonObject.get("total").toString());
        JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("rows"));
        List<AgDic> list = new AgDicConverter().convertToList(null, jsonArray);
        pageInfo.setList(list);
        pageInfo.setTotal(total);
        pageInfo.setPages(total / Integer.parseInt(rows));
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }*/

    /**
     * 分页查询编码
     *
     * @param agDic
     * @param
     * @return
     * @throws Exception
     */
  /*  @RequestMapping(value = "refCodeList/{typeId}",method = RequestMethod.GET)
    public EasyuiPageInfo<AgDic> refCodeList(@PathVariable("typeId") String typeId, AgDic agDic, HttpServletRequest request) throws Exception {
        *//*PageInfo<AgDic> pageInfo=iAgDic.findRefCodeList(typeCode, agDic, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);*//*

        //调用agcloud接口获取
        if (typeId == null || "null".equals(typeId)) return null;
        String itemKeyword = agDic.getName();
        String page = request.getParameter("page");
        String rows = request.getParameter("rows");
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        PageInfo<AgDic> pageInfo = new PageInfo<>();
        Map<String, String> param = new HashMap<>();
        param.put("itemKeyword", itemKeyword);
        param.put("page", page);
        param.put("rows", rows);
        param.put("sort", "itemSortNo");
        param.put("order", "asc");
        param.put("typeId", typeId);
        HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/bsc/dic/code/listItems.do", param);
        JSONObject jsonObject = JSONObject.fromObject(httpRespons.getContent());
        int total = Integer.parseInt(jsonObject.get("total").toString());
        JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("rows"));
        List<AgDic> list = new AgDicConverter().convertToList(null, jsonArray);
        pageInfo.setList(list);
        pageInfo.setTotal(total);
        pageInfo.setPages(total / Integer.parseInt(rows));
        pageInfo.setStartRow(1);
        pageInfo.setEndRow(total);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }*/

    /**
     * 根据typCode获取数据字典
     * @param typeCode
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取数据字典数据",notes = "获取数据字典数据接口")
    @ApiImplicitParam(name = "typeCode",value = "类型编码")
    @RequestMapping(value = "/getAgDicByTypeCode/{typeCode}",method = RequestMethod.GET)
    public ContentResultForm getAgDicByTypeCode(@PathVariable("typeCode") String typeCode) throws Exception {
        return new ContentResultForm<List>(true,iAgDic.getAgDicByTypeCode(typeCode));
       /* //调用agcloud获取
        Map<String, String> param = new HashMap<>();
        //param.put("orgId", orgId);
        param.put("typeCode", typeCode);
        HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/bsc/dic/code/lgetItemsByTypeCode.do", param);
        //JSONObject jsonObject = JSONObject.fromObject(httpRespons.getContent());
        JSONArray jsonArray = JSONArray.fromObject(httpRespons.getContent());
        List<AgDic> list = new AgDicConverter().convertToList(null, jsonArray);
        return new ContentResultForm<List>(true,list);*/
    }

    /**
     * 根据typCode和子项名称获取数据字典值
     * agcloud那边是typeId对应
     *
     * @param typeCode
     * @param itemName
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据typCode和子项名称获取数据字典值",notes = "根据typCode和子项名称获取数据字典值接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "typeCode",value = "类型编码",dataType = "String"),
            @ApiImplicitParam(name = "itemName",value = "子项名称",dataType = "String"),
    })
    @RequestMapping(value = "/getAgDicByTypeCodeAndItemName/{typeCode}/{itemName}",method = RequestMethod.GET)
    public ContentResultForm getAgDicByTypeCodeAndItemName(@PathVariable("typeCode") String typeCode,@PathVariable("itemName") String itemName) throws Exception {
        //调用agcloud获取
     /*   Map<String, String> map = new HashMap<>();
        map.put("typeCode", typeCode);
        map.put("itemName", itemName);
        map.put("orgId", orgId);
        HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/bsc/dic/code/listItemByTypeCodeAndItemName.do", map);
        JSONArray jsonArray = JSONArray.fromObject(httpRespons.getContent());
        List<AgDic> list = new AgDicConverter().convertToList(null, jsonArray);
        AgDic agDic = new AgDic();
        if (list.size()>0){
            agDic = list.get(0);
        }*/
        AgDic agdic = iAgDic.getAgDicByTypeCodeAndItemName(typeCode, itemName);
        return new ContentResultForm<AgDic>(true,agdic);
    }

  /*  @RequestMapping(value = "/allDics")
    public String getAllDics(@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {
        //String sql="select id,type_code,type_name ,CODE,name,IS_SYSTEM from ag_refcode";
        String sql = "select * from ag_refcode order by type_code";
        List<Object> values = null;
        List<Map> last = DBHelper.find(sql, values);
        return Common.fromObject(last).toString();
    }*/
    /**
     * 根据坐标系统名称查询坐标系统的数据字典，获取名称对应的值
     */
    @ApiOperation(value = "根据坐标系统名称查询坐标系统的数据字典，获取名称对应的值",notes = "根据坐标系统名称查询坐标系统的数据字典，获取名称对应的值的接口")
    @ApiImplicitParam(name = "name",value = "坐标系统名称",dataType = "String")
    @RequestMapping(value = "/findValueByCoorName/{name}",method = RequestMethod.GET)
    public ContentResultForm findValueByCoorName(@PathVariable("name") String name) throws Exception {
        AgDic agdic = iAgDic.findValueByCoorName(name);
        if (agdic != null) {
            return new ContentResultForm<AgDic>(true,agdic);
        }
        return new ContentResultForm<AgDic>(false,null,"无数据");
    }

    /**
     * 根据坐标转换名称查询坐标系统的数据字典，获取名称对应的值
     */
    @ApiOperation(value = "根据坐标转换名称查询坐标系统的数据字典，获取名称对应的值",notes = "根据坐标转换名称查询坐标系统的数据字典，获取名称对应的值")
    @ApiImplicitParam(name = "name",value = "坐标转换名称",dataType = "String")
    @RequestMapping(value = "/findValueByCoordTransName/{name}",method = RequestMethod.GET)
    public ContentResultForm findValueByCoordTransName(@PathVariable("name") String name) throws Exception {
        AgDic agdic = iAgDic.findValueByCoordTransName(name);
        if (agdic != null) {
            return new ContentResultForm<AgDic>(true,agdic);
        }
        return new ContentResultForm<AgDic>(false,null,"无数据");
    }

    /**
     * 获取日志基本信息 2018-3-12
     *
     * @param request
     * @param funcName
     * @return
     */
    private JSONObject getLogInfo(HttpServletRequest request, String funcName) {
        JSONObject jsonObject = new JSONObject();
        String loginName = LoginHelpClient.getLoginName(request);
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        jsonObject.put("loginName", loginName);
        jsonObject.put("sysName", "agsupport");
        jsonObject.put("ipAddress", Common.getIpAddr(request));
        jsonObject.put("browser", userAgent.getBrowser().getName());
        jsonObject.put("funcName", funcName);
        return jsonObject;
    }
}
