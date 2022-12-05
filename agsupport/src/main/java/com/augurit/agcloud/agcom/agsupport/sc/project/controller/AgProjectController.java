package com.augurit.agcloud.agcom.agsupport.sc.project.controller;

import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.agcom.agsupport.common.config.condition.MultiNodeCondition;
import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgMapParam;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import com.augurit.agcloud.agcom.agsupport.sc.log.service.IAgLog;
import com.augurit.agcloud.agcom.agsupport.sc.param.service.IAgParam;
import com.augurit.agcloud.agcom.agsupport.sc.project.controller.form.MapParamResult;
import com.augurit.agcloud.agcom.agsupport.sc.project.service.IAgProject;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.agcom.agsupport.util.LogUtil;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonMapper;
import com.augurit.agcom.common.LoginHelpClient;
import com.common.util.Common;
import com.common.util.HttpRequester;
import com.common.util.HttpRespons;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by chendingxing on 2017-12-22.
 * 这里对专题的操作都是在文件层面，将文件数据加载成json树，并进行相关操作；
 * 专题数据存储在webApp/agcom/project 目录下，一个专题一个json文件；
 * 这里在多节点情况下会根据配置的主节点url，统一到主节点获取专题数据
 */
@Api(value = "地图专题",description = "地图专题接口")
@RestController
@RequestMapping("/agsupport/project")
public class AgProjectController {

    @Autowired
    private IAgUser iAgUser;

    @Autowired
    private IAgProject iAgProject;

    @Autowired
    private IAgParam iAgParam;

    @Autowired
    private IAgLog log;

    private static final String ROWS = "rows";
    private static final String PAGESIZE = "page";


    /**
     * 主界面
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/index.html")
    public ModelAndView index(HttpServletRequest request, Model model) throws Exception {
        return new ModelAndView("agcloud/agcom/agsupport/project/index");
    }

    /**
     * agsupport专题管理专题下拉列表
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "专题管理专题下拉列表",notes = "专题管理专题下拉列表")
    @RequestMapping(value = "/projectList",method = RequestMethod.GET)
    public ContentResultForm projectList(HttpServletRequest request) throws Exception {
        if(MultiNodeCondition.matches()){
            return new ContentResultForm<String>(true,doSend(request,null));
        }else{
            String loginName = LoginHelpClient.getLoginName(request);
            String realPath = getPath(request);
            List<Map> list = iAgProject.projectList(loginName);
            return new ContentResultForm<List<Map>>(true,list);
        }
    }

    /**
     * agsupport运维专题管理加载专题树
     * @param projectName
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "专题管理专题下拉列表",notes = "专题管理专题下拉列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectName" ,value = "专题名称",dataType = "String",paramType = "path")
    })
    @RequestMapping(value = "/tree/{projectName}",method = RequestMethod.GET)
    public ContentResultForm tree(@PathVariable("projectName") String projectName, HttpServletRequest request) throws Exception {
        if("null".equals(projectName)){
            return new ContentResultForm<JSONArray>(false,new JSONArray(),"项目名称不能为空");//返回这个字符串，树就会消失
        }
        if(MultiNodeCondition.matches()){
            return new ContentResultForm<String>(true, doSend(request,null));
        }else{
            String realPath = getPath(request);
            String result = iAgProject.getProject(realPath,projectName);
            return new ContentResultForm<String>(true,result);
        }
    }

    /**
     * 新增专题
     * @param projectName
     * @param mapParamId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "新增专题",notes = "新增专题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectName" ,value = "专题名称",dataType = "String"),
            @ApiImplicitParam(name = "mapParamId" ,value = "地图参数id",dataType = "String"),
    })
    @RequestMapping(value = "/addProject",method = RequestMethod.POST)
    public ResultForm addProject(String projectName,String mapParamId,HttpServletRequest request) throws Exception {
        String funcName = "新增图层目录";
        ResultForm result = iAgProject.addProject(projectName,mapParamId);
        if(result.isSuccess()){
            log.info(LogUtil.createSuccessLogInfo(request,funcName).toString());
        }else{
            log.error(LogUtil.createFailLogInfo(request,funcName,result.getMessage()).toString());
        }
        return result;
    }

    /**
     * 修改专题
     * @param projectName
     * @param mapParamId
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "修改专题",notes = "修改专题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectName" ,value = "专题名称",dataType = "String"),
            @ApiImplicitParam(name = "oldProjectName" ,value = "原来专题名称",dataType = "String"),
            @ApiImplicitParam(name = "mapParamId" ,value = "地图参数id",dataType = "String"),
            @ApiImplicitParam(name = "projectOrder" ,value = "排序",dataType = "int")
    })
    @RequestMapping(value = "/updateProject",method = RequestMethod.POST)
    public ResultForm updateProject(String projectId,String projectName,String mapParamId,HttpServletRequest request) throws Exception {
        if(Common.isCheckNull(projectName)){
            return  new ResultForm(false,"专题名称不能为空");
        }
        if(MultiNodeCondition.matches()){
            //带修改和测试
            Map params = new HashMap();
            params.put("projectName",projectName);
            params.put("oldProjectName","oldProjectName");
            params.put("mapParamId",mapParamId);
            params.put("projectOrder","projectOrder");
            return new ContentResultForm<String>(true,doSend(request,params));
        }else{
            String funcName = "新增图层目录";
            ResultForm result = iAgProject.updateProject(projectId,projectName,mapParamId);
            if(result.isSuccess()){
                log.info(LogUtil.createSuccessLogInfo(request,funcName).toString());
            }else{
                log.error(LogUtil.createFailLogInfo(request,funcName,result.getMessage()).toString());
            }
            return result;
        }
    }

    /**
     * 删除专题
     * @param projectName
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "删除专题",notes = "删除专题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectName" ,value = "专题名称",dataType = "String")
    })
    @RequestMapping(value = "/deleteProject",method = RequestMethod.DELETE)
    public ContentResultForm deleteProject(String projectName,HttpServletRequest request) throws Exception {
        if(MultiNodeCondition.matches()){
            Map params = new HashMap();
            params.put("projectName",projectName);
            return new ContentResultForm<String>(true,doSend(request,params));
        }else{
            String realPath = getPath(request);
            iAgProject.deleteProject(realPath, projectName);
            return new ContentResultForm(true);
        }
    }


    /**
     * 新增目录
     * @param projectName
     * @param id
     * @param dirName
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "新增目录",notes = "新增目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectName" ,value = "专题名称",dataType = "String"),
            @ApiImplicitParam(name = "id" ,value = "id",dataType = "String"),
            @ApiImplicitParam(name = "dirName" ,value = "目录名称",dataType = "String")
    })
    @RequestMapping(value = "/addDir",method = RequestMethod.POST)
    public ContentResultForm addDir(String projectName,String id,String dirName,HttpServletRequest request) throws Exception {
        if(MultiNodeCondition.matches()){
            Map params = new HashMap();
            params.put("projectName",projectName);
            params.put("id",id);
            params.put("dirName",dirName);
            return new ContentResultForm<String>(true, doSend(request,params));
        }else{
            String realPath = getPath(request);
            String nodeId = iAgProject.addDir(realPath,projectName, id, dirName);
            return new ContentResultForm<String>(true,nodeId);
        }
    }

    /**
     * 修改专题目录
     * @param projectName
     * @param id
     * @param dirName
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "修改专题目录",notes = "修改专题目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectName" ,value = "专题名称",dataType = "String"),
            @ApiImplicitParam(name = "id" ,value = "id",dataType = "String"),
            @ApiImplicitParam(name = "dirName" ,value = "目录名称",dataType = "String")
    })
    @RequestMapping(value = "/updateDir",method = RequestMethod.POST)
    public ContentResultForm updateDir(String projectName,String id,String dirName,HttpServletRequest request) throws Exception {
        if(MultiNodeCondition.matches()){
            Map params = new HashMap();
            params.put("projectName",projectName);
            params.put("id",id);
            params.put("dirName",dirName);
            return new ContentResultForm<String>(true,doSend(request,params));
        }else{
            String realPath = getPath(request);
            iAgProject.updateDir(realPath,projectName, id, dirName);
            return new ContentResultForm<String>(true,id);
        }
    }

    /**
     * 删除目录
     * @param projectName
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "删除目录",notes = "删除目录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectName" ,value = "专题名称",dataType = "String"),
            @ApiImplicitParam(name = "id" ,value = "id",dataType = "String")
    })
    @RequestMapping(value = "/deleteDir",method = RequestMethod.DELETE)
    public ContentResultForm deleteDir(String projectName,String id,HttpServletRequest request) throws Exception {
        if(MultiNodeCondition.matches()){
            Map params = new HashMap();
            params.put("projectName",projectName);
            params.put("id",id);
            return new ContentResultForm<String>(true, doSend(request,params));
        }else{
            String realPath = getPath(request);
            String nodeId = iAgProject.deleteDir(realPath,projectName,id);
            return new ContentResultForm<String>(true,nodeId);
        }
    }

    /**
     * 添加图层
     * @param projectName
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "添加图层",notes = "添加图层")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectName" ,value = "专题名称",dataType = "String"),
            @ApiImplicitParam(name = "id" ,value = "id",dataType = "String"),
            @ApiImplicitParam(name = "ids" ,value = "图层id",dataType = "String")
    })
    @RequestMapping(value = "/addLayer",method = RequestMethod.POST)
    public ContentResultForm addLayer(String projectName,String id,String ids,HttpServletRequest request) throws Exception {
        if(MultiNodeCondition.matches()){
            Map params = new HashMap();
            params.put("projectName",projectName);
            params.put("id",id);
            params.put("ids",ids);
            return new ContentResultForm<String>(true, doSend(request,params));
        }else{
            String realPath = getPath(request);
            String nodeId = iAgProject.addLayer(realPath,projectName,id,ids);
            return new ContentResultForm<String>(true,nodeId);
        }
    }

    /**
     * 移除专题下的图层
     * @param projectName
     * @param id
     * @param ids
     * @param request
     * @return
     * @throws Exception
     */
	@ApiOperation(value = "移除专题下的图层",notes = "移除专题下的图层")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectName" ,value = "专题名称",dataType = "String"),
            @ApiImplicitParam(name = "id" ,value = "id",dataType = "String"),
            @ApiImplicitParam(name = "ids" ,value = "图层id",dataType = "String")
    })
    @RequestMapping(value = "/removeLayer",method = {RequestMethod.GET,RequestMethod.DELETE})
    public ContentResultForm removeLayer(String projectName,String id,String ids,HttpServletRequest request) throws Exception {
        if(MultiNodeCondition.matches()){
            Map params = new HashMap();
            params.put("projectName",projectName);
            params.put("id",id);
            params.put("ids",ids);
            return new ContentResultForm<String>(true,doSend(request,params));
        }else{
            String realPath = getPath(request);
            boolean result = iAgProject.removeLayer(realPath,projectName,id,ids);
            if(result){
                return new ContentResultForm<JSONArray>(result,new JSONArray(),"删除成功");
            }
            else{
                return new ContentResultForm<JSONArray>(result,new JSONArray(),"删除失败");
            }
        }
    }

    /**
     * 查询专题目录下的图层
     * @param projectName
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询专题目录下的图层",notes = "查询专题目录下的图层")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectName" ,value = "专题名称",dataType = "String"),
            @ApiImplicitParam(name = "id" ,value = "目录id",dataType = "String"),
            @ApiImplicitParam(name = "name" ,value = "图层名称",dataType = "String")
    })
    @RequestMapping(value = "/getProjectDirLayer",method = RequestMethod.GET)
    public ContentResultForm getProjectDirLayer(String projectName, String id, String name, Page page,HttpServletRequest request) throws Exception {
        if(MultiNodeCondition.matches()){
            Map params = new HashMap();
            params.put("projectName",projectName);
            params.put("id",id);
            return new ContentResultForm<String>(true,doSend(request, params));
        }else{
            String realPath = getPath(request);
            PageInfo<AgLayer> layers = iAgProject.getProjectDirLayer(realPath, projectName, id,name,page);
            if(Common.isCheckNull(layers)){
                JSONArray jsonArray = new JSONArray();
                return new ContentResultForm<JSONArray>(false,jsonArray);
            }else{
                EasyuiPageInfo newObject = PageHelper.toEasyuiPageInfo(layers);
                //JsonMapper mapper = new JsonMapper(JsonInclude.Include.ALWAYS);//不去掉为空字段
                return new ContentResultForm<EasyuiPageInfo>(true,newObject);
            }
        }
    }

    /**
     * 获取专题授权的用户数据
     * @param projectName
     * @param userName 用户名称
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取专题授权的用户数据",notes = "获取专题授权的用户数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectName" ,value = "专题名称",dataType = "String"),
            @ApiImplicitParam(name = "id" ,value = "目录id",dataType = "String"),
            @ApiImplicitParam(name = "page" ,value = "分页参数:/agsupport/param/paramList?page=1&rows=10&name=",dataType = "Page")
    })
    @RequestMapping(value = "/getProjectUser",method = RequestMethod.GET)
    public ContentResultForm getProjectUser(String projectName, String userName, Page page, HttpServletRequest request) throws Exception {
        page.setPageNum(Integer.valueOf(request.getParameter(PAGESIZE)));
        page.setPages(Integer.valueOf(request.getParameter(ROWS)));
        if(MultiNodeCondition.matches()){
            Map params = new HashMap();
            params.put("projectName",projectName);
            params.put("name",userName);
            //return doSend(request, params);
            String result = doSend(request, params);
            return new ContentResultForm<String>(true,result);
        }else{
            String realPath = getPath(request);
            PageInfo<AgUser> users = iAgProject.getProjectUser(realPath, projectName,userName,page);
            if(Common.isCheckNull(users)){
                //return null;
                return new ContentResultForm<JSONArray>(false,new JSONArray(),"找不到项目人员");
            }else{
                EasyuiPageInfo newObject = new EasyuiPageInfo();
                newObject.setRows(users.getList());
                newObject.setTotal(users.getTotal());
                JsonMapper mapper = new JsonMapper(JsonInclude.Include.ALWAYS);//不去掉为空字段
                //return mapper.toJson(newObject);
                return new ContentResultForm<EasyuiPageInfo>(true,newObject);
            }
        }
    }
    /**
     * 目录拖拽排序
     *
     * @param dirIds
     * @return
     */
    @ApiOperation(value = "目录拖拽排序",notes = "目录拖拽排序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirIds" ,value = "目录id",dataType = "String")
    })
    @RequestMapping(value = "/dirOrder",method = RequestMethod.GET)
    public ResultForm dirOrder(String dirIds) {
//        String ids[] = null;
//        if (StringUtils.isNotEmpty(dirIds)) {
//            ids = dirIds.split(",");
//        }
//        List<AgProjectdir> list = new ArrayList<AgProjectdir>();
//        if (ids != null && ids.length > 0) {
//            int i = 1;
//            for (String id : ids) {
//                AgProjectdir agDir = new AgProjectdir();
//                agDir.setId(id);
//                agDir.setOrderNm(i++);
//                list.add(agDir);
//            }
//        }

        try {
            iAgProject.updateDirBatch(dirIds);
            //iAgDir.updateDirBatch(list);
            return new ResultForm(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false);
    }

    /**
     * 专题授权
     * @param projectName
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "专题授权",notes = "专题授权")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectName" ,value = "专题名称",dataType = "String"),
            @ApiImplicitParam(name = "ids" ,value = "专题id",dataType = "String"),
            @ApiImplicitParam(name = "isAdd" ,value = "isAdd",dataType = "boolean")
    })
    @RequestMapping(value = "/projectAuthor",method = RequestMethod.POST)
    public ContentResultForm projectAuthor(String projectName,String ids, boolean isAdd, HttpServletRequest request) throws Exception {
        if(MultiNodeCondition.matches()){
            Map params = new HashMap();
            params.put("projectName",projectName);
            params.put("ids",ids);
            params.put("isAdd",isAdd);
            return new ContentResultForm<String>(true,doSend(request, params));
        }else{
            String realPath = getPath(request);
            String result = iAgProject.projectAuthor(realPath, projectName,ids,isAdd);

            return new ContentResultForm<String>(result != null);
        }
    }

    /**
     * 获取前端专题图层树
     * @param projectName
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取前端专题图层树",notes = "获取前端专题图层树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectName" ,value = "专题名称",dataType = "String"),
            @ApiImplicitParam(name = "userId" ,value = "用户id",dataType = "String")
    })
    @RequestMapping(value = "/getProjectLayerTree",method = RequestMethod.GET)
    public ContentResultForm getProjectLayerTree(String projectName,String userId,HttpServletRequest request) throws Exception{
        if(MultiNodeCondition.matches()){
            Map params = new HashMap();
            params.put("projectName",projectName);
            params.put("userId",userId);
            return new ContentResultForm<String>(true, doSend(request, params));
        }else{
            String realPath = getPath(request);
            AgUser user = iAgUser.findUserById(userId);
            if(Common.isCheckNull(user)){
                JSONArray jsonArray = new JSONArray();
                return new ContentResultForm<JSONArray>(false,jsonArray,"找不到用户");
            }
            List result = iAgProject.getProjectLayerTree(realPath, projectName,user);
            return new ContentResultForm(true,result);
        }
    }

    /**
     * 获取前端专题列表详细信息
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取前端专题列表详细信息",notes = "获取前端专题列表详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId" ,value = "用户id",dataType = "String",paramType = "path")
    })
    @RequestMapping(value = "/getProjectInfo/{userId}",method = RequestMethod.GET)
    public ContentResultForm getProjectInfo(@PathVariable("userId") String userId, HttpServletRequest request) throws Exception{
        if(MultiNodeCondition.matches()){
            return new ContentResultForm<String>(true,doSend(request,null));
        }else{
            String realPath = getPath(request);
            AgUser user = iAgUser.findUserById(userId);
            if(Common.isCheckNull(user)){
                JSONArray jsonArray = new JSONArray();
                return new ContentResultForm<JSONArray>(false,jsonArray,"找不到用户");
            }
            //List<Map> list = iAgProject.projectList(realPath,user.getLoginName());
            //前端专题只获取该用户角色拥有的专题
            List<Map> list = iAgProject.projectList(user.getLoginName());

            for(Map map: list){
                String mapParamId = (String) map.get("mapParamId");
                AgMapParam param = iAgParam.findMapParamById(mapParamId);
                if(param != null) {
                    map.put("param",paramTrans(param));
                }
            }
            return new ContentResultForm<List<Map>>(true,list);

        }
    }

    /**
     * 获取所有地图参数
     */
    @ApiOperation(value = "获取所有地图参数",notes = "获取所有地图参数")
    @RequestMapping(value = "/getMapParams",method = RequestMethod.GET)
    public ContentResultForm getMapParams() throws Exception{
        List<AgMapParam> agMapParams = iAgParam.searchAll();
        List<MapParamResult> results = new ArrayList<>();
        for(AgMapParam amp : agMapParams){
            results.add(paramTrans(amp));
        }
        return new ContentResultForm<List<MapParamResult>>(true,results);
    }

    private MapParamResult paramTrans(AgMapParam param){
        MapParamResult result = new MapParamResult();
        if(param != null) {
            result.setId(param.getId());
            result.setExtent(param.getExtent());
            result.setOrigin(param.getOrigin());
            result.setTileOrigin(param.getTileOrigin());
            result.setName(param.getName());
            result.setReference(param.getReference());
            result.setCenter(param.getCenter());
            result.setZoom(param.getZoom());
            result.setMinZoom(param.getMinZoom());
            result.setMaxZoom(param.getMaxZoom());
            result.setResolutions(param.getScales());
            if(param.getCenter() != null && param.getScales()!=null) {
                String[] center = param.getCenter().split(",");
                Double flag = Double.parseDouble(center[0]);
                String scales[] = param.getScales().split(",");
                String scalesStr = "";
                int index = 0;
                if (flag >= -400 && flag <= 400) {
                    //经纬度坐标
                    for (String s : scales) {
                        float v = Float.parseFloat(s);
                        double ss = v * 96 * 2 * Math.PI * 6378137 / (0.0254 * 360);
                        if (index == scales.length - 1) {
                            scalesStr += ss;
                        } else {
                            scalesStr += ss + ",";
                        }
                        index++;
                    }
                } else {
                    for (String s : scales) {
                        float v = Float.parseFloat(s);
                        double ss = (96 / 0.0254) * v;
                        if (index == scales.length - 1) {
                            scalesStr += ss;
                        } else {
                            scalesStr += ss + ",";
                        }
                        index++;
                    }
                }
                result.setScales(scalesStr);
            }
        }
        return result;
    }

    /**
     * 负责转发请求
     * @param request
     * @param param
     * @return
     */
    private String doSend(HttpServletRequest request, Map param){
        String requestURI = request.getRequestURI();
        //注意目前的 主站点链接是格局配置的ip端口 加上请求的uri组成的，要求从站点的server.context-path要跟主站点一致
        String url = MultiNodeCondition.mainServer + requestURI;
        HttpRequester hr = new HttpRequester();
        String method = request.getMethod();
        HttpRespons httpRespons = null;
        String result = null;
        try {
            if("post".equalsIgnoreCase(method)){
                httpRespons = hr.sendPost(url, param);
            }else{
                httpRespons = hr.sendGet(url,param);
            }
            result = httpRespons.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    private String getPath(HttpServletRequest request){
        return request.getSession().getServletContext().getRealPath("/") + "ui-static/agcloud/agcom/project/";
    }
    @ApiOperation(value = "根据用户名称获取专题信息",notes = "根据用户名称获取专题信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName" ,value = "用户名称",dataType = "String"),
            @ApiImplicitParam(name = "projectName" ,value = "专题名称",dataType = "String",paramType = "path")
    })
    @RequestMapping(value = "/getProjectInfo2/{userName}",method = RequestMethod.GET)
    public ContentResultForm getProjectInfo2(@PathVariable("userName") String userName, String projectName, HttpServletRequest request) throws Exception{
        if(MultiNodeCondition.matches()){
            return new ContentResultForm<String>(true,doSend(request,null));
        }else{
            String realPath = getPath(request);
            AgUser user = iAgUser.findUserByName(userName);
            if(Common.isCheckNull(user)){
                JSONArray jsonArray = new JSONArray();
                return new ContentResultForm<JSONArray>(false,jsonArray,"找不到用户");
            }
            List reuslt =iAgProject.getProjectLayerTree(realPath, projectName,user);
            return new ContentResultForm(true,reuslt);
        }
    }
    @ApiOperation(value = "根据标注id查询图层",notes = "根据标注id查询图层")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lableId" ,value = "标注id",dataType = "String"),
            @ApiImplicitParam(name = "projectName" ,value = "专题名称",dataType = "String"),
            @ApiImplicitParam(name = "asc" ,value = "asc",dataType = "String")
    })
    @RequestMapping(value = "/findLayerByLayerLabel",method = RequestMethod.GET)
    public ContentResultForm findLayerByLayerLabel(String lableId,String projectName,String asc,HttpServletRequest request) throws Exception{
        if(MultiNodeCondition.matches()){
            return new ContentResultForm<String>(true,doSend(request,null));
        }else{
            String realPath = getPath(request);
            AgUser user = iAgUser.findUserByName(lableId);
            if(Common.isCheckNull(user)){
                JSONArray jsonArray = new JSONArray();
                return new ContentResultForm<JSONArray>(false,jsonArray,"找不到用户");
            }
            List result =iAgProject.getProjectLayerTree(realPath, projectName,user);
            return new ContentResultForm(true,result);
        }
    }
  /*  @ApiOperation(value = "目录排序",notes = "目录排序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirid" ,value = "目录id",dataType = "String"),
            @ApiImplicitParam(name = "dirLayerIds" ,value = "目录图层id",dataType = "String")
    })
    @RequestMapping(value = "/changeorder",method = RequestMethod.GET)
    public ResultForm changeorder(String dirid,String dirLayerIds) throws Exception{
      String turn= iAgProject.changeorder(dirid,dirLayerIds);
        if("false".equals(turn)){
            return new ResultForm(false);
        }
        return new ResultForm(true);

    }
    @ApiOperation(value = "目录排序",notes = "目录排序")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirid" ,value = "目录id",dataType = "String"),
            @ApiImplicitParam(name = "dirLayerIds" ,value = "目录图层id",dataType = "String")
    })
    @RequestMapping(value = "/changeorders",method = RequestMethod.GET)
    public ResultForm changeorders(String dirid,String dirLayerIds) throws Exception{
        String turn= iAgProject.changeorders(dirid,dirLayerIds);
        if("false".equals(turn)){
            return new ResultForm(false);
        }
        return new ResultForm(true);

    }*/
}
