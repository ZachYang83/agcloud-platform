package com.augurit.agcloud.agcom.agsupport.sc.elasticsearch.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgIndexConfigInfo;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayerField;
import com.augurit.agcloud.agcom.agsupport.sc.elasticsearch.util.HttpClientUtil;
import com.augurit.agcloud.agcom.agsupport.sc.field.service.IAgField;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.agcom.agsupport.sc.elasticsearch.service.IAgIndexConfigInfo;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :9:45 2018/12/27
 * @Modified By:
 */
@Api(value = "索引配置信息接口",description = "索引配置信息接口")
@RestController
@RequestMapping("/agsupport/indexConfigInfo")
public class IndexConfigInfoController {


    @Autowired
    private IAgIndexConfigInfo iAgIndexConfigInfo;

    @Autowired
    private IAgField iAgField;

    @Value("${address.url}")
    private String addressUrl;

    @ApiOperation(value = "分页获取索引配置信息",notes = "分页获取索引配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agIndexConfigInfo",required = true, value = "索引配置信息对象", dataType = "AgIndexConfigInfo"),
            @ApiImplicitParam(name = "page",required = true, value = "分页参数:/agsupport/param/paramList?page=1&rows=10&name=", dataType = "Page")
    })
    @RequestMapping("/getByPage")
    public ContentResultForm getByPage(AgIndexConfigInfo agIndexConfigInfo, Page page) throws Exception {
        if (agIndexConfigInfo.getIndexName()==null)agIndexConfigInfo.setIndexName("");
        PageInfo<AgIndexConfigInfo> pageInfo=iAgIndexConfigInfo.searchAgIndexConfigInfo(agIndexConfigInfo, page);
        return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(pageInfo),"");
    }

    @ApiOperation(value = "保存索引配置信息",notes = "保存索引配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agIndexConfigInfo" ,value = "索引配置信息对象",dataType = "AgIndexConfigInfo")
    })
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ResultForm save(AgIndexConfigInfo agIndexConfigInfo)throws Exception{
         try {
             agIndexConfigInfo.setId(UUID.randomUUID().toString());
             agIndexConfigInfo.setIndexName(agIndexConfigInfo.getIndexName().toLowerCase());
             agIndexConfigInfo.setCreateTime(new Date());

             //将索引字段作为显示字段
             String[] showField = agIndexConfigInfo.getIndexField().split(",");
             JSONObject json = new JSONObject();
             for (String field:showField){
                 json.put(field,true);
             }
             agIndexConfigInfo.setShowField(json.toString());
             iAgIndexConfigInfo.saveAgIndexConfigInfo(agIndexConfigInfo);
             return new ResultForm(true, "保存成功");
         }catch (Exception e){
             return new ResultForm(false, "保存失败");
         }
    }

    @ApiOperation(value = "修改索引配置信息",notes = "修改索引配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agIndexConfigInfo" ,value = "索引配置信息对象",dataType = "AgIndexConfigInfo")
    })
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResultForm update(AgIndexConfigInfo agIndexConfigInfo)throws Exception{
        try {
            agIndexConfigInfo.setUpdataTime(new Date());
            iAgIndexConfigInfo.updateAgIndexConfigInfo(agIndexConfigInfo);
            return new ResultForm(true, "修改成功");
        }catch (Exception e){
            return new ResultForm(false, "修改失败");
        }
    }



    @ApiOperation(value = "根据id获取配置信息",notes = "根据id获取配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id" ,value = "配置信息id",dataType = "String")
    })
    @RequestMapping(value = "/findAgIndexConfigInfo",method = RequestMethod.POST)
    public ContentResultForm findAgIndexConfigInfo(String  id) {
        try {
            return new ContentResultForm(true,iAgIndexConfigInfo.findById(id),"根据id获取配置信息");
        }catch (Exception e){
            return new ContentResultForm(false,"","获取数据失败");
        }
    }

    /**
     * 删除配置信息里的索引和索引库对应的索引
     *
     * 配置信息是根据id删除，索引库的是根据索引名称删除
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delIndex",method = RequestMethod.DELETE)
    public String delete(String id) throws Exception {
        try {
            AgIndexConfigInfo info = iAgIndexConfigInfo.findById(id);
            String indexName = info.getIndexName();
            Map<String, String> param = new HashMap<>();
            param.put("indexName", indexName);
            param.put("rtnType", "json");
            String result = HttpClientUtil.HttpPost(addressUrl + "/delIndex", param);
            JSONObject object = JSONObject.fromObject(result);
            if (object.getBoolean("success")) {
                iAgIndexConfigInfo.delById(id);//删除配置信息里的索引
            }
            return result;
        } catch (Exception e) {
            return JsonUtils.toJson(new ResultForm(false, "删除失败"));
        }
    }

    /**
     * 根据索引库名称分页获取数据
     * @param indexName
     * @param keyword
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/getPageByIndexName")
    public ContentResultForm ordinarySearch(String indexName, String keyword, int page,  int rows) {
        try {
            Map<String,String> param = new HashMap<>();
            param.put("indexName",indexName);
            param.put("keyword",keyword);
            param.put("pageNum",String.valueOf(page));
            param.put("pageSize",String.valueOf(rows));
            param.put("rtnType","json");
            String result = HttpClientUtil.HttpPost(addressUrl+"/queryPageByIndexName",param);
            if (result==null) {
                JSONObject json = new JSONObject();
                json.put("rows",new JSONArray());
                json.put("total",0);
                json.put("message","无法连接搜索引擎");
                return new ContentResultForm(false, json.toString(),"无法连接搜索引擎");
            }
            JSONObject object = JSONObject.fromObject(result.toLowerCase());
            return new ContentResultForm(true, object,"获取成功");
        }catch (Exception e){
            JSONObject json = new JSONObject();
            json.put("rows",new JSONArray());
            json.put("total",0);
            return new ContentResultForm(false, json,"获取失败");
        }
    }

    /**
     * 获取所有索引配置的显示字段
     * @return
     */
    @RequestMapping(value = "/getAllShowField")
    public ContentResultForm getAllShowField(){
        List<Map<String,Object>> reslut = new ArrayList<>();
        try {
            List<AgLayerField> agLayerFields = new ArrayList<>();//字段的别名
            List<AgIndexConfigInfo> list = iAgIndexConfigInfo.getAllShowField();
            for (AgIndexConfigInfo info :list){
                Map<String,Object> map = new HashMap<>();
                Map<String,String> fieldMap = new HashMap<>();
                List<Map<String,String>> fieldlList = new ArrayList<>();
                map.put("tableName",info.getTableName());
                agLayerFields = iAgField.getFieldNameAlias(info.getLayerId());
                String[] showField = info.getShowField()==null ? new String[0]:info.getShowField().split(",");
                //没有配置查询显示字段默认使用索引字段的前两个作为显示字段
                if (showField.length<1){
                    showField = info.getIndexField().split(",");
                }

                List<Object> listMap = new ArrayList<>();
                for (int i= 0;i<showField.length;i++){
                    Map<String,String> nameAliasMap = new HashMap<>();
                    String fieldName = "";
                    String fieldAlias = "";
                    if (agLayerFields.size()<1){//没有配置字段别名
                        fieldName = showField[i];
                        fieldAlias = showField[i];
                    }else {
                        for (int j=0;j<agLayerFields.size();j++){
                            String showFieldName = showField[i];
                            String name = agLayerFields.get(j).getFieldName();
                            if (showFieldName.equalsIgnoreCase(name)) {
                                fieldName = showField[i];
                                fieldAlias = agLayerFields.get(j).getFieldNameCn();
                            }
                        }
                    }
                    nameAliasMap.put("fieldName",fieldName);
                    nameAliasMap.put("fieldAlias",fieldAlias);
                    listMap.add(nameAliasMap);
                }

                map.put("showFields",listMap);
                reslut.add(map);
            }
            return new ContentResultForm(true, reslut,"获取成功");
        }catch (Exception e){
            e.printStackTrace();
            return new ContentResultForm(false, "","获取失败");
        }
    }
    @ApiOperation(value = "保存或修改显示字段",notes = "保存或修改显示字段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agIndexConfigInfo" ,value = "索引配置信息对象",dataType = "AgIndexConfigInfo")
    })
    @RequestMapping(value = "/saveOrUpdateShowField",method = RequestMethod.POST)
    public ResultForm saveOrUpdateShowField(AgIndexConfigInfo agIndexConfigInfo) {
        try {
            iAgIndexConfigInfo.updateShowField(agIndexConfigInfo);
            return new ResultForm(true, "保存成功");
        }catch (Exception e){
            return new ResultForm(false, "保存失败");
        }
    }


    /**
     *@param id 配置信息id
     * @param indexName
     * @param ids
     * @return
     */
    @RequestMapping(value = "/deleteDataByIds", method = RequestMethod.DELETE)
    public ResultForm deleteDataByIds(String id, String indexName, String ids) {
        try {
            int len = ids.split(",").length;
            Map<String, String> param = new HashMap<>();
            param.put("indexName", indexName);
            param.put("ids", ids);
            param.put("rtnType", "json");
            String result = HttpClientUtil.HttpPost(addressUrl + "/deleteDataByIds", param);
            JSONObject json = JSONObject.fromObject(result);
            if (json.getBoolean("success")) {
                AgIndexConfigInfo info = iAgIndexConfigInfo.findById(id);
                info.setDocNumber(info.getDocNumber() - len);
                iAgIndexConfigInfo.updateAgIndexConfigInfo(info);
                return new ResultForm(true, "删除成功");
            } else {
                return new ResultForm(false, "删除失败" + result);
            }
        } catch (Exception e) {
            return new ResultForm(false, "删除失败");
        }
    }
}
