package com.augurit.agcloud.agcom.agsupport.sc.param.controller;

import com.augurit.agcloud.agcom.agsupport.domain.AgMapParam;
import com.augurit.agcloud.agcom.agsupport.sc.log.service.IAgLog;
import com.augurit.agcloud.agcom.agsupport.sc.param.service.IAgParam;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcom.common.LoginHelpClient;
import com.common.util.Common;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Augurit on 2017-04-18.
 */
@Api(value = "地图参数设置",description = "地图参数设置相关接口")
@RestController
@RequestMapping("/agsupport/param")
public class ParamController {

    private static Logger logger = LoggerFactory.getLogger(ParamController.class);

    @Autowired
    private IAgParam iAgParam;

    @Autowired
    private IAgLog log;

    @RequestMapping("/index.html")
    public ModelAndView index(HttpServletRequest request, Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/param/index");
    }

    @ApiOperation(value = "分页获取地图参数信息",notes = "分页获取地图参数接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",required = false, value = "地图参数名称", dataType = "String"),
            @ApiImplicitParam(name = "page",required = true, value = "分页参数:/agsupport/param/paramList?page=1&rows=10&name=", dataType = "Page")
    })
    @RequestMapping(value = "/paramList",method = RequestMethod.GET)
    public ContentResultForm paramList(String name, Page page) throws Exception {
        PageInfo<AgMapParam> pageInfo=iAgParam.searchParam(name, page);
        return new ContentResultForm<EasyuiPageInfo>(true,PageHelper.toEasyuiPageInfo(pageInfo));
    }

    /**
     * 获取所有地图参数
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取所有地图参数",notes = "获取所有地图参数接口")
    @RequestMapping(value = "/searchAll",method = RequestMethod.GET)
    public ContentResultForm searchAll() throws Exception {
        return new ContentResultForm<List>(true,iAgParam.searchAll());
    }

    /**
     * 导入地图参数
     *
     * @return
     */
   /* @RequestMapping("/importMapParam")
    public String importMapParam() {
        List<AgMapParam> list = new ArrayList<AgMapParam>();
        try {
            List<Map> result_param = iAgParam.findMapParamFromProd();
            if (result_param.size() > 0) {
                for (Map map : result_param) {
                    AgMapParam agMapParam = new AgMapParam();
                    agMapParam.setAddFlag("1");
                    if (map.containsKey("id") && map.get("id") != null) {
                        String paramId = map.get("id").toString();
                        List<Map> result_origin = iAgParam.findOriginFromProd(paramId);
                        if (result_origin != null && result_origin.size() > 0) {
                            for (Map map_origin : result_origin) {
                                if (map_origin.get("origin") != null) {
                                    agMapParam.setOrigin(map_origin.get("origin").toString());
                                    break;
                                }
                            }
                        }
                    }
                    if (map.containsKey("name") && map.get("name") != null) {
                        agMapParam.setName(map.get("name").toString());
                    }
                    if (map.containsKey("reference") && map.get("reference") != null) {
                        agMapParam.setReference(map.get("reference").toString());
                    }
                    if (map.containsKey("zoom") && map.get("zoom") != null) {
                        agMapParam.setZoom(map.get("zoom").toString());
                    }
                    if (map.containsKey("zoomscales") && map.get("zoomscales") != null) {
                        agMapParam.setScales(map.get("zoomscales").toString());
                    }
                    if (map.containsKey("mapxy") && map.get("mapxy") != null) {
                        agMapParam.setCenter(map.get("mapxy").toString());
                    }
                    if (map.containsKey("is_base_map") && map.get("is_base_map") != null) {
                        agMapParam.setOrigin(map.get("is_base_map").toString());
                    }
                    if (map.containsKey("mapextent") && map.get("mapextent") != null) {
                        agMapParam.setExtent(map.get("mapextent").toString());
                    }
                    AgMapParam temp = iAgParam.findMapParam(agMapParam);
                    if (temp == null) {
                        list.add(agMapParam);
                    } else {
                        if (temp.getAddFlag() != null && "1".equals(temp.getAddFlag())) {
                            agMapParam.setId(temp.getId());
                            list.add(agMapParam);
                        }
                    }
                }
            }
            if (list.size() > 0) {
                for (AgMapParam agMapParam : list) {
                    if (StringUtils.isEmpty(agMapParam.getId())) {
                        agMapParam.setId(UUID.randomUUID().toString());
                        iAgParam.saveMapParam(agMapParam);
                    } else {
                        iAgParam.updateMapParam(agMapParam);
                    }
                }
            }
            return JsonUtils.toJson(new ResultForm(true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ResultForm(false));
    }*/

    @ApiOperation(value = "新增或修改地图参数信息",notes = "新增或修改地图参数信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agMapParam" ,value = "地图参数信息对象",dataType = "AgMapParam")
    })
    @RequestMapping(value = "/saveParam",method = RequestMethod.POST)
    public ResultForm saveParam(AgMapParam agMapParam, HttpServletRequest request) {
        JSONObject jsonObject = null;
        try {
            AgMapParam temp = iAgParam.findMapParam(agMapParam);
            if (temp != null && !temp.getId().equals(agMapParam.getId())) {
                return new ResultForm(false, "该地图参数已存在，保存失败！");
            }
            if (StringUtils.isEmpty(agMapParam.getId())) {
                agMapParam.setId(UUID.randomUUID().toString());
                iAgParam.saveMapParam(agMapParam);
                jsonObject = this.getLogInfo(request, "保存地图参数--" + agMapParam.getName());
            } else {
                jsonObject = this.getLogInfo(request, "修改地图参数--" + agMapParam.getName());
                iAgParam.updateMapParam(agMapParam);
            }
            jsonObject.put("operResult", "成功");
            log.info(jsonObject.toString());
            return new ResultForm(true,"保存成功!");
        } catch (Exception e) {
            jsonObject.put("operResult", "失败");
            if (e.getMessage() != null) {
                jsonObject.put("exceptionMessage", e.getMessage().substring(0, e.getMessage().length() > 500 ? 500 : e.getMessage().length()));
            }
            log.error(jsonObject.toString());
            e.printStackTrace();
        }
        return new ResultForm(false, "保存失败！");
    }

    /**
     * 删除地图参数
     *
     * @param paramIds
     * @return
     */
    @ApiOperation(value = "删除地图参数",notes = "删除地图参数接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "paramIds",value = "地图参数id,多个地图参数英文逗号分隔",required = true,dataType = "String")
    })
    @RequestMapping(value = "/deleteParam",method = RequestMethod.DELETE)
    public ResultForm deleteParam(String paramIds, HttpServletRequest request) {
        JSONObject jsonObject = this.getLogInfo(request, "批量删除地图参数");
        String ids[] = null;
        if (StringUtils.isNotEmpty(paramIds)) {
            ids = paramIds.split(",");
        }
        try {
            if (ids != null && ids.length > 0) {
                iAgParam.deleteMapParamBatch(ids);
            }
            jsonObject.put("operResult", "成功");
            log.info(jsonObject.toString());
            return new ResultForm(true,"删除成功");
        } catch (Exception e) {
            jsonObject.put("operResult", "失败");
            if (e.getMessage() != null) {
                jsonObject.put("exceptionMessage", e.getMessage().substring(0, e.getMessage().length() > 500 ? 500 : e.getMessage().length()));
            }
            log.error(jsonObject.toString());
            e.printStackTrace();
        }
        return new ResultForm(false,"删除失败");
    }

    /**
     * 加载地图范围,加载分辨率
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "加载地图范围,加载分辨率",notes = "加载地图范围,加载分辨率接口")
    @RequestMapping(value = "/loadMapextent",method = RequestMethod.POST)
    public ContentResultForm loadMapextent(HttpServletRequest request) {
        String result = "";
        Map resultMap = new HashMap();
        try {
            request.setCharacterEncoding("UTF-8");
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
            Iterator<String> it = req.getFileNames();
            while (it.hasNext()) {
                MultipartFile file = req.getFile(it.next());
                String fileNames = file.getOriginalFilename();
                int split = fileNames.lastIndexOf(".");
                if (fileNames.substring(split + 1, fileNames.length()).equals("cdi")) {
                    String mapextent = iAgParam.loadMapextent(file.getInputStream());
                    //result += "mapextent:" + mapextent.replaceAll("\r?\n", "").replace("\"", "") ;

                    resultMap.put("mapextent",mapextent.replaceAll("\r?\n", "").replace("\"", ""));
                }
                if (fileNames.substring(split + 1, fileNames.length()).equals("xml")) {
                    String mapextent = iAgParam.loadResolution(file.getInputStream());
                    Map map = iAgParam.readTileOrigin(file.getInputStream());
                    //result += "resolution:" + mapextent.replaceAll("\r?\n", "").replace("\"", "") +"tileOrigin:" + map.get("tileOrigin") +"cutMapOrigin:" + map.get("cutMapOrigin") ;
                    resultMap.put("resolution",mapextent.replaceAll("\r?\n", "").replace("\"", "") );
                    resultMap.put("tileOrigin",map.get("tileOrigin"));
                    resultMap.put("cutMapOrigin",map.get("cutMapOrigin"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<Map>(false,resultMap,"加载出错!");
        }
        return new ContentResultForm<Map>(true,resultMap,"加载成功!");
    }

    @ApiOperation(value = "校验地图参数",notes = "校验地图参数接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "地图参数名称",dataType = "String"),
            @ApiImplicitParam(name = "id",value = "地图参数ID",dataType = "String"),
    })
    @RequestMapping(value = "/checkName",method = RequestMethod.GET)
    public ResultForm checkName(String name, String id) throws Exception {
        ResultForm resultForm = new ResultForm(true);
        if (iAgParam.checkName(name).size() < 1 && id == null) {
            resultForm.setSuccess(false);
        } else if (iAgParam.checkName(name).size() < 2 && id != null) {
            resultForm.setSuccess(false);
        }
        return resultForm;
    }
   /* @ApiOperation(value = "导入地图参数",notes = "导入地图参数接口,上传地图参数文件")
    @RequestMapping("/importParam")
    public String importParam(HttpServletRequest request) {
        JSONObject jsonObject = this.getLogInfo(request, "导入地图参数");
        try {
            request.setCharacterEncoding("UTF-8");
            StandardMultipartHttpServletRequest req = (StandardMultipartHttpServletRequest) request;
            Iterator<String> it = req.getFileNames();
            StringBuffer sb = new StringBuffer();
            if (it.hasNext()) {
                MultipartFile file = req.getFile(it.next());
                BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                String lineText;
                while ((lineText = reader.readLine()) != null) {
                    sb.append(lineText);
                }
                reader.close();
            }
            if (sb.toString().length() > 0) {
                jsonObject.put("operResult", "成功");
                log.info(jsonObject.toString());
                return JsonUtils.toJson(new ContentResultForm<String>(true, sb.toString()));
            }
        } catch (Exception e) {
            jsonObject.put("operResult", "失败");
            if (e.getMessage() != null) {
                jsonObject.put("exceptionMessage", e.getMessage().substring(0, e.getMessage().length() > 500 ? 500 : e.getMessage().length()));
            }
            log.error(jsonObject.toString());
            e.printStackTrace();
        }
        return JsonUtils.toJson(new ResultForm(false));
    }*/

 /*   @RequestMapping("/exportParam")
    public void exportParam(HttpServletResponse response, String jsonString, HttpServletRequest request) {
        JSONObject jsonObject = this.getLogInfo(request, "导出地图参数");
        response.setContentType("text/plain");
        response.addHeader("Content-Disposition",
                "attachment;filename=param.json");// filename指定默认的名字
        BufferedOutputStream buff = null;
        ServletOutputStream outSTr = null;
        try {
            outSTr = response.getOutputStream();
            buff = new BufferedOutputStream(outSTr);
            buff.write(jsonString.getBytes("UTF-8"));
            buff.flush();
            buff.close();
            jsonObject.put("operResult", "成功");
            log.info(jsonObject.toString());
        } catch (Exception e) {
            jsonObject.put("operResult", "失败");
            if (e.getMessage() != null) {
                jsonObject.put("exceptionMessage", e.getMessage().substring(0, e.getMessage().length() > 500 ? 500 : e.getMessage().length()));
            }
            log.error(jsonObject.toString());
            e.printStackTrace();
        } finally {
            try {
                buff.close();
                outSTr.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    @ApiOperation(value = "根据Id获取地图参数",notes = "根据Id获取地图参数接口")
    @ApiImplicitParam(name = "paramId",value = "地图参数ID",dataType = "String")
    @RequestMapping(value = "/findMapParamById",method = RequestMethod.GET)
    public ContentResultForm findMapParamById(String paramId) throws Exception {
        AgMapParam mapParam = iAgParam.findMapParamById(paramId);
        return new ContentResultForm<AgMapParam>(true,mapParam);
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
