package com.augurit.agcloud.agcom.agsupport.sc.field.controller;

import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.field.service.IAgField;
import com.augurit.agcloud.agcom.agsupport.sc.log.service.IAgLog;
import com.augurit.agcloud.agcom.agsupport.sc.role.service.IAgRole;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcom.common.LoginHelpClient;
import com.common.util.Common;
import com.common.util.ReflectBeans;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Author：李德文
 * CreateDate：2017-04-27
 * Description: 图层配置、字段配置Controller
 */
@Api(value = "图层配置、字段配置",description = "图层配置、字段配置接口")
@RestController
@RequestMapping("/agsupport/field")
public class AgFieldController {

    @Autowired
    private IAgField iAgField;
    @Autowired
    private IAgRole iAgRole;
    @Autowired
    private IAgDir iAgDir;
    @Autowired
    private IAgLog log;
    @Autowired
    private IAgUser iAgUser;

/*    *//**
     * 图层配置页面
     *//*
    @RequestMapping("/layerConf.html")
    public ModelAndView layerConfig(Model model, HttpServletRequest request) throws Exception {
        String dirLayerId = request.getParameter("dirLayerId");
        AgLayer agLayer = iAgDir.findLayerByDirLayerId(dirLayerId);
        model.addAttribute("dirLayerId", dirLayerId);
        model.addAttribute("userId", request.getParameter("userId"));
        model.addAttribute("agLayer", agLayer);
        return new ModelAndView("agcloud/agcom/agsupport/field/index");
    }

    *//**
     * 字段配置页面
     *//*
    @RequestMapping("/fieldConf.do")
    public ModelAndView fieldConf(Model model, HttpServletRequest request) throws Exception {
        String dirLayerId = request.getParameter("dirLayerId");
        AgLayer agLayer = iAgDir.findLayerByDirLayerId(dirLayerId);
        model.addAttribute("dirLayerId", dirLayerId);
        model.addAttribute("userId", request.getParameter("userId"));
        model.addAttribute("agLayer", agLayer);
        return new ModelAndView("/agcom/field/fieldConf");
    }*/

    /**
     * 获取图层配置
     */
    /*@RequestMapping("/getLayerConfig")
    public String getLayerConfig(String roleId, String dirLayerId) throws Exception {
        AgRoleLayer agRoleLayer = iAgRole.getRoleLayer(roleId, dirLayerId);
        if (agRoleLayer == null || StringUtils.isEmpty(agRoleLayer.getId())) return null;
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("agRoleLayer", agRoleLayer);
        jsonObj.put("data", iAgDir.findLayerByDirLayerId(agRoleLayer.getDirLayerId()).getData());
        return jsonObj.toString();
    }*/


    @ApiOperation(value = "获取图层配置",notes = "获取图层配置(服务和用户绑定之后的，原来是服务和角色绑定的)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId" ,value = "用户id",dataType = "String"),
            @ApiImplicitParam(name = "dirLayerId" ,value = "目录图层id",dataType = "String")
    })
    @RequestMapping(value = "/getLayerConfig",method = RequestMethod.POST)
    public ContentResultForm getLayerConfig(String userId, String dirLayerId) throws Exception {
        AgUserLayer agUserLayer = iAgUser.getUserLayer(userId,dirLayerId);
        JSONObject jsonObj = new JSONObject();
        if (agUserLayer == null || StringUtils.isEmpty(agUserLayer.getId())) {
            return new ContentResultForm<JSONObject>(false,jsonObj);
        }
        else {
            jsonObj.put("agUserLayer", agUserLayer);
            jsonObj.put("data", iAgDir.findLayerByDirLayerId(agUserLayer.getDirLayerId()).getData());
            return new ContentResultForm<JSONObject>(true,jsonObj);
        }
    }


    @ApiOperation(value = "保存图层配置",notes = "保存图层配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agUserLayer" ,value = "用户图层",dataType = "AgUserLayer"),
            @ApiImplicitParam(name = "data" ,value = "图层json数据",dataType = "String")
    })
    @RequestMapping(value = "/saveLayerConfig",method = RequestMethod.POST)
    public ResultForm saveLayerConfig(AgUserLayer agUserLayer, String data, HttpServletRequest request) {
        JSONObject jsonObject = this.getLogInfo(request, "修改图层配置");
        try {
            JSONObject jsonData = JSONObject.fromObject(data);
            iAgField.saveLayerConfig(agUserLayer);
            AgLayer agLayer = iAgDir.findLayerByDirLayerId(agUserLayer.getDirLayerId());
            String oldData = agLayer.getData();
            if (StringUtils.isNotEmpty(oldData)) {
                JSONObject oldJson = JSONObject.fromObject(oldData);
                oldJson.putAll(jsonData);
                agLayer.setData(oldJson.toString());
            }
            if ("1".equals(agLayer.getIsProxy())) {
                String proxyUrl = agLayer.getProxyUrl();
                if (!Common.isCheckNull(proxyUrl)) {
                    //只获取 UUID
                    proxyUrl = proxyUrl.substring(proxyUrl.lastIndexOf("/") + 1, proxyUrl.length());
                }
                // 修改图层配置不用修改代理地址
                agLayer.setProxyUrl(null);
            }
            iAgDir.updateLayer(agLayer);
            jsonObject.put("operResult", "成功");
            log.info(jsonObject.toString());
            return new ResultForm(true);
        } catch (Exception e) {
            jsonObject.put("operResult", "失败");
            if (e.getMessage() != null) {
                jsonObject.put("exceptionMessage", e.getMessage().substring(0, e.getMessage().length() > 500 ? 500 : e.getMessage().length()));
            }
            log.error(jsonObject.toString());
            e.printStackTrace();
        }
        return  new ResultForm(false);
    }


    @ApiOperation(value = "获取字段配置",notes = "获取字段配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirLayerId" ,value = "目录图层id",dataType = "String"),
            @ApiImplicitParam(name = "layerId" ,value = "图层id",dataType = "String")
    })
    @RequestMapping(value = "/getLayerFieldConf",method = RequestMethod.POST)
    public ContentResultForm getLayerFieldConf(String dirLayerId,String layerId) throws Exception {
        //AgRoleLayer agRoleLayer = iAgRole.getRoleLayer(userId, dirLayerId);
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("agUserLayer", iAgField.getLayerFields(dirLayerId, layerId));
        jsonObj.put("data", iAgDir.findLayerByDirLayerId(dirLayerId).getData());
        return new ContentResultForm(true,jsonObj,"获取字段配置");
    }

    @ApiOperation(value = "刷新字段",notes = "刷新字段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userLayerId" ,value = "用户图层id",dataType = "String"),
            @ApiImplicitParam(name = "layerId" ,value = "图层id",dataType = "String")
    })
    @RequestMapping(value = "/refreshField",method = RequestMethod.POST)
    public ContentResultForm  refreshField(String userLayerId, String layerId) throws Exception {
        List<AgLayerFieldConf> list =  iAgField.getRefreshFields(userLayerId, layerId);
        return new ContentResultForm(true,list,"刷新字段成功");
    }


    @ApiOperation(value = "保存字段配置",notes = "保存字段配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data" ,value = "保存数据",dataType = "String"),
            @ApiImplicitParam(name = "commit" ,value = "提交数据",dataType = "String"),
            @ApiImplicitParam(name = "issaved" ,value = "是否保存",dataType = "String")
    })
    @RequestMapping(value = "/saveLayerFieldConf",method = RequestMethod.POST)
    public ResultForm saveLayerFieldConf(String data, String commit, String issaved) {
        JSONArray array = JSONArray.fromObject(data);
        try {
            //保存字段配置
            List<AgLayerFieldConf> layerFieldlist = new ArrayList<AgLayerFieldConf>();//更新原有的字段配置
            List<AgLayerFieldConf> layerFieldlistSave = new ArrayList<AgLayerFieldConf>();//保存经过刷新后从服务后得到的字段配置
            for (int i = 0, il = array.size(); i < il; ++i) {
                JSONObject jsobj = array.getJSONObject(i);
                AgLayerFieldConf temp = ReflectBeans.copy(jsobj, AgLayerFieldConf.class);
                //AgLayerField layerField = ReflectBeans.copy(temp, AgLayerField.class);
                //AgFieldAuthorize fieldAuthorize = ReflectBeans.copy(temp, AgFieldAuthorize.class);
                String fieldName = temp.getFieldName();
                String layerId = temp.getLayerId();
                // 根据layerId跟字段名称查询字段是否存在
                AgLayerField agLayerField = iAgField.getLayerFieldByLayerIdAndFieldName(layerId, fieldName);
                if (agLayerField == null){
                    temp.setId(UUID.randomUUID().toString());
                }else {
                    temp.setId(agLayerField.getId());
                }

                temp.setOrderNm(String.valueOf(i + 1));
                if (iAgField.getLayerFieldById(temp.getId())==null){//不存在记录，保存
                    layerFieldlistSave.add(temp);
                }else {
                    layerFieldlist.add(temp);//存在记录，跟新
                }

            }
            iAgField.saveLayerFields(layerFieldlistSave);
            iAgField.saveLayerFields(layerFieldlist);

            //iAgField.saveFieldAuthorize(fieldAuthorizelist);
            JSONObject jsonData = JSONObject.fromObject(commit);
            String userId = SecurityContext.getCurrentUserId();
            jsonData.put("userId",userId);
            if (jsonData.size() < 3) {
                return new ResultForm(true);
            }
            JSONObject jsonDatas = JSONObject.fromObject(jsonData.getString("data"));
            //AgRoleLayer agRoleLayer = iAgRole.getRoleLayer(jsonData.getString("roleId"), jsonData.getString("dirLayerId"));
            AgLayer agLayer = iAgDir.findLayerByDirLayerId(jsonData.getString("dirLayerId"));
            String oldData = agLayer.getData();
            if (StringUtils.isNotEmpty(oldData)) {
                JSONObject oldJson = JSONObject.fromObject(oldData);
                oldJson.putAll(jsonDatas);
                agLayer.setData(oldJson.toString());
            }
            iAgDir.updateLayer(agLayer);
            return new ResultForm(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false);
    }

    @ApiOperation(value = "根据用户id获取图层配置",notes = "根据用户id获取图层配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirLayerId" ,value = "目录图层id",dataType = "String"),
            @ApiImplicitParam(name = "userId" ,value = "用户id",dataType = "String"),
            @ApiImplicitParam(name = "layerId" ,value = "图层id",dataType = "String")
    })
    @RequestMapping(value = "/getLayerConfByUserId",method = RequestMethod.POST)
    public ContentResultForm getLayerConfByUserId(String dirLayerId, String userId,String layerId) {
       try {
           List<AgLayerFieldConf> layerField = iAgField.getLayerFieldsByUserId(dirLayerId, layerId);
           //AgRoleLayer roleLayer = iAgField.getLayerConfigByUserId(dirLayerId, userId);
           AgUserLayer userLayer = iAgField.getLayerConfigByUserId(dirLayerId, userId);
           List<AgLayerRelated> listLayerRelated = iAgField.getLayerRelatedByServiceDirLayerId(dirLayerId);
           Map result = new HashMap();
           result.put("fieldConf", layerField);
           result.put("layerConf", userLayer);
           result.put("layerRelatedConf", listLayerRelated);
           return new ContentResultForm(true,result,"获取图层配置成功");
       }catch (Exception e){
           return new ContentResultForm(false,"","获取图层配置失败");
       }
    }

    @ApiOperation(value = "根据目录图层id获取字段配置信息",notes = "根据目录图层id获取字段配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirLayerId" ,value = "目录图层id",dataType = "String"),
            @ApiImplicitParam(name = "userId" ,value = "用户id",dataType = "String")
    })
    @RequestMapping(value = "/getFiledConfByDirLayerId",method = RequestMethod.POST)
    public ContentResultForm getFiledConfByDirLayerId(String dirLayerId,String userId,HttpServletRequest request) {
       try {
           String myUserId;
           if(userId == null){
               String loginName = LoginHelpClient.getLoginName(request);
               AgUser user = iAgUser.findUserByName(loginName);
               if(user == null)
                   return null;
               myUserId = user.getId();
           }else{
               myUserId = userId;
           }
           List<AgLayerFieldConf> layerField = iAgField.getLayerFieldsByUserId(dirLayerId, myUserId);
           return new ContentResultForm(true,layerField,"获取字段配置信息成功");
       }catch (Exception e){
           return new ContentResultForm(false,"","获取字段配置信息失败");
       }
    }


    @ApiOperation(value = "根据layerId获取字段信息",notes = "根据layerId获取字段信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "layerId" ,value = "图层id",dataType = "String")
    })
    @RequestMapping(value = "/getLayerFieldsByLayerId",method = RequestMethod.GET)
    public ContentResultForm  getLayerFieldsByLayerId(String layerId) {
        try {
            List<AgLayerField> list = iAgField.findLayerFieldByLayerId(layerId);
            return new ContentResultForm(true,list,"获取成功");
        }catch (Exception e){
            return new ContentResultForm(false,"","获取失败");
        }
    }


    @ApiOperation(value = "根据dirLayerId,userId获取图层配置信息",notes = "根据dirLayerId,userId获取图层配置信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirLayerId" ,value = "目录图层id",dataType = "String"),
            @ApiImplicitParam(name = "userId" ,value = "用户id",dataType = "String")
    })
    @RequestMapping(value = "/getLayerConfigByUserId",method = RequestMethod.POST)
    public ContentResultForm getLayerConfigByUserId(String dirLayerId, String userId) {
       try {
           // AgRoleLayer agRoleLayer = iAgField.getLayerConfigByUserId(dirLayerId, userId);
           AgUserLayer agUserLayer = iAgField.getLayerConfigByUserId(dirLayerId, userId);

           JSONObject annotation = null;
           JSONObject data = JSONObject.fromObject(iAgDir.findLayerByDirLayerId(agUserLayer.getDirLayerId()).getData());
           if (data != null) {
               annotation = (JSONObject) data.get("annotation");
               if (annotation != null) {
                   JSONObject fieldObj = (JSONObject) annotation.get("fieldObj");
                   Iterator it = fieldObj.keys();
                   while (it.hasNext()) {
                       String key = (String) it.next();
                       String value = fieldObj.getString(key);
                       String fieldName = iAgField.getLayerFieldById(value).getFieldName();
                       fieldObj.put(key, fieldName);
                   }
               }
           }
           return new ContentResultForm(true,annotation,"");
       }catch (Exception e){
           return new ContentResultForm(false,"","获取图层配置信息失败");
       }
    }

    /**
     * 获取日志基本信息 2017-12-07
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
