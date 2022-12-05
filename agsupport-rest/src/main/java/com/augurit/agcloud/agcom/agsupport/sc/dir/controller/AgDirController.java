package com.augurit.agcloud.agcom.agsupport.sc.dir.controller;

import com.augurit.agcloud.agcom.agsupport.common.config.RedisCache.MapCache;
import com.augurit.agcloud.agcom.agsupport.common.config.condition.RedisCondition;
import com.augurit.agcloud.agcom.agsupport.common.util.ProxyUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.UUIDUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.UploadUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.UuidChanger;
import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.sc.dic.service.IAgDic;
import com.augurit.agcloud.agcom.agsupport.sc.dir.controller.form.LayerForm;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.impl.AgDirImpl;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.Ztree;
import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgDataTrans;
import com.augurit.agcloud.agcom.agsupport.sc.log.service.IAgLog;
import com.augurit.agcloud.agcom.agsupport.sc.param.service.IAgParam;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.agcom.agsupport.util.LogUtil;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonMapper;
import com.augurit.agcom.common.LoginHelpClient;
import com.common.util.Common;
import com.common.util.ConfigProperties;
import com.common.util.ReflectBeans;
import com.common.util.ReflectMap;
import com.fasterxml.jackson.annotation.JsonInclude;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Augurit on 2017-04-17.
 */
@Api(value = "地图资源管理",description = "地图资源管理相关接口")
@RestController
@RequestMapping("/agsupport/dir")
public class AgDirController {

    private static Logger logger = LoggerFactory.getLogger(AgDirController.class);
    @Autowired
    private IAgDir iAgDir;

    @Autowired
    private IAgParam iAgParam;

    @Autowired
    private IAgUser iAgUser;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IAgLog log;
    @Autowired
    public IAgDic iAgDic;
    @Value("${agcloud.opus.admin.orgId}")
    private String orgId;
    @Autowired
    public com.augurit.agcloud.agcom.agsupport.sc.dic.service.IAgDic IAgDic;

    @RequestMapping("/index.html")
    public ModelAndView index(HttpServletRequest request, Model model) throws Exception {
        //String loginName = LoginHelpClient.getLoginName(request);
        //AgUser agUser = iAgUser.findUserByName(loginName);
        return new ModelAndView("agcloud/agcom/agsupport/dir/index");
    }


    /**
     * 递归获取孩子节点
     * @param list_dir 所有目录
     * @param parentId 父节点 id
     * @param childListDir 递归得到的孩子节点
     * @return
     */
   private List<AgDir> getChildDir(List<AgDir> list_dir, String parentId, List<AgDir> childListDir){
       for (AgDir dir:list_dir){
           String id = dir.getId();
           if (id.equals(parentId) && !"root".equals(id)){
               childListDir.add(dir);
               if (dir.getParentId()!=null && !"".equals(dir.getParentId())){
                   getChildDir(list_dir,dir.getParentId(),childListDir);
               }
           }
       }
       return childListDir;
   }
    /**
     * 加载目录树
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "返回图层目录数据",notes = "返回图层目录数据接口")
    @ApiImplicitParam(name = "id",value = "父节点id",paramType = "path",required = true)
    @RequestMapping(value = "/tree/{id}",method = RequestMethod.GET)
    public ContentResultForm tree(@PathVariable("id") String id,HttpServletRequest request) throws Exception {
        List<AgDir> list_dir  = iAgDir.findAllDir();
        List<AgDir> listProject = new ArrayList<AgDir>();//存储专题的List
        for (AgDir item : list_dir){
            if(item.getParentId()!=null &&item.getParentId().equals("root")){
                listProject.add(item);
            }
        }
        List<Ztree> listZtreeResult = new ArrayList<>();
        if ("sjhc_all".equals(id)){
            List<AgDir> list_dir_hasData = iAgDir.findAllDirSJHC();//数据缓冲 只返回矢量图层有数据的目录
            List<AgDir> list = new ArrayList<>();
            for (AgDir agDir:list_dir_hasData){
                String parentId = agDir.getParentId();
                list.add(agDir);
                List<AgDir> childListDir = new ArrayList<>();
                List<AgDir> resultList = new ArrayList<>();
                resultList = getChildDir(list_dir,parentId,childListDir);
                list.addAll(resultList);
            }
            list_dir = list;
        }

        //获取地图资源目录树
        for (AgDir agDir : list_dir){
            Ztree ztree = new Ztree();
            ztree.setId(agDir.getId());
            ztree.setName(agDir.getName());
            ztree.setpId(agDir.getParentId());
            if(listProject.contains(agDir)) {
                ztree.setMapParamId(agDir.getMapParamId());
            }else {
                for(AgDir item : listProject){
                    if(agDir.getXpath().indexOf(item.getXpath())==0){
                        ztree.setMapParamId(item.getMapParamId());
                    }
                }
            }
            if (null == agDir.getParentId()){
                ztree.setOpen("true");
            }else {
                ztree.setOpen("false");
            }
            listZtreeResult.add(ztree);
        }
        return new ContentResultForm<List>(true,listZtreeResult);
    }

    /**
     * 保存目录
     *
     * @param name
     * @param pid
     * @return
     */
    @ApiOperation(value = "新增图层目录",notes = "新增图层目录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "图层目录名称",dataType = "AgDir"),
            @ApiImplicitParam(name = "pid",value = "父节点ID",dataType = "String",paramType = "path",required = true)
    })
    @RequestMapping(value = "/saveDir/{pid}",method = RequestMethod.POST)
    public ResultForm saveDir(String name, @PathVariable("pid") String pid, HttpServletRequest request) {
        String funcName = "新增图层目录";
        try {
            AgDir agDir = new AgDir();
            agDir.setName(name);
            agDir.setId(UUID.randomUUID().toString());
            if (!Common.isCheckNull(pid)) {
                AgDir parentDir = iAgDir.findDirById(pid);
                List<AgDir> AgDirs=iAgDir.findDirsByXpath(parentDir.getXpath());
                for (AgDir temp : AgDirs) {
                    //agHistChangeService.logAgHistAppWithOrgDelete(temp);
                    if(null != temp.getName() && temp.getName().equals(agDir.getName())){
                        String errorInfo = "目录名重复";
                        log.error(LogUtil.createFailLogInfo(request,funcName,errorInfo).toString());
                        return new ResultForm(false,errorInfo);
                    }
                }
                agDir.setXpath(parentDir.getXpath() + "/" + agDir.getName());
                agDir.setParentId(pid);
                if (StringUtils.isNotEmpty(parentDir.getDirSeq())) {
                    agDir.setDirSeq(agDir.getId() + "," + parentDir.getDirSeq());
                } else {
                    agDir.setDirSeq(agDir.getId());
                }
            } else {
                agDir.setXpath("/" + agDir.getName());
                agDir.setDirSeq(agDir.getId());
            }

            String maxOrder = iAgDir.getDirOrder(pid);
            if (StringUtils.isEmpty(maxOrder)) {
                maxOrder = "0";
            }
            int orderNm = Integer.parseInt(maxOrder);
            agDir.setOrderNm(++orderNm);

            iAgDir.saveDir(agDir);
            log.info(LogUtil.createSuccessLogInfo(request,funcName).toString());
            return new ResultForm(true, "保存成功!");
        } catch (Exception e) {
            log.error(LogUtil.createFailLogInfo(request,funcName,e.getMessage()).toString());
            e.printStackTrace();
        }
        return new ResultForm(false,"保存出错!");
    }

    /**
     * 修改目录
     *
     * @param dirId
     * @param name
     * @return
     */
    @ApiOperation(value = "修改图层目录名称",notes = "修改图层目录名称接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirId",value = "修改的目录ID",paramType = "path",required = true),
            @ApiImplicitParam(name = "name",value = "目录名称",required = true)
    })
    @RequestMapping(value = "/updateDir/{dirId}",method = RequestMethod.POST)
    public ResultForm updateDir(@PathVariable("dirId") String dirId, String name, HttpServletRequest request) {
        String funcName = "修改图层目录";
        try {
            AgDir agDir = iAgDir.findDirById(dirId);
//            agDir.setMapParamId(mapParamId); 取消参数设置

            String xpath = "";
            String oldXpath = "";
            if (name != null) {
                agDir.setName(name);
                oldXpath = agDir.getXpath();
                xpath = oldXpath.substring(0, oldXpath.lastIndexOf("/") + 1) + name;
                agDir.setXpath(xpath);
            }
            if (agDir.getId() != null) {
                iAgDir.updateDir(agDir);
                if (!xpath.equals(oldXpath)) {
                    List<AgDir> list = iAgDir.findDirsByXpath(oldXpath);
                    for (AgDir dir : list) {
                        dir.setXpath(dir.getXpath().replaceFirst(oldXpath, xpath));
                    }
                    if (list.size() > 0) {
                        iAgDir.updateDirBatch(list);
                    }
                }
            }
            log.info(LogUtil.createSuccessLogInfo(request,funcName).toString());
            return new ResultForm(true,"保存成功!");
        } catch (Exception e) {
            log.error(LogUtil.createFailLogInfo(request,funcName,e.getMessage()).toString());
            e.printStackTrace();
        }
        return new ResultForm(false,"保存失败!");
    }

    @ApiOperation(value = "删除目录",notes = "删除目录接口")
    @ApiImplicitParam(name = "id",value = "目录ID",required = true,paramType = "path")
    @RequestMapping(value = "/deleteDir/{id}",method = RequestMethod.DELETE)
    public ResultForm deleteDir(@PathVariable("id") String id, HttpServletRequest request) {
        String uploadPath = UploadUtil.getUploadAbsolutePath();
        String funcName = "删除图层目录";
        List<AgDirLayer> agDirLayerTmp = new ArrayList<>();

        try {
            // 查询所有的关联关系
            List<AgDirLayer> all = iAgDir.findAll();
            // 查询所有目录
            List<AgDir> list = iAgDir.findAllDir();

            List<AgDir> chdldList = new ArrayList<>();
            //List<AgDir> childDirs = getChildDirs(list, id,chdldList);
            AgDir dirById = iAgDir.findDirById(id);
            List<AgDir> childDirs = iAgDir.findDirsByXpath(dirById.getXpath());
            // 包括选中的目录
            childDirs.add(dirById);
            for (AgDir agDir : childDirs){
                List<AgDirLayer> dirLayers = new ArrayList<>();
                List<String> dirLayerIds = new ArrayList<>();
                String dirId = agDir.getId();
                // 查询目录拥有的图层关联关系
                List<AgDirLayer> agDirLayers = iAgDir.findByDirId(dirId);
                for (AgDirLayer agDirLayer : agDirLayers){
                    String haslayerId = agDirLayer.getLayerId();
                    //String hasDirId = agDirLayer.getDirId();
                    // 判断此图层与其他目录关联条数
                    int cnt = 0;
                    for(AgDirLayer allDirLayer : all){
                        if (haslayerId.equals(allDirLayer.getLayerId())){
                            cnt++;
                        }
                    }
                    // 大于1 则与其他目录有关联，记录关联的id;
                    // 否则只与当前目录关联，直接记录需要删除的layerId
                    if(cnt > 1){
                        // 记录需要删除的目录图层关联的id
                        dirLayerIds.add(agDirLayer.getId());
                    }else {
                        dirLayers.add(agDirLayer);
                    }

                }
                // 批量删除图层和目录图层关联关系
                if (dirLayers.size()>0){
                    iAgDir.deleteLayerBatch(dirLayers,uploadPath);
                }
                if (dirLayerIds.size()>0){
                    System.out.println("dirLayerIds:"+dirLayerIds.toString());
                    for (String dirLayerId : dirLayerIds){
                        iAgDir.delDirLayer(dirLayerId);
                    }

                }

            }
            List<AgDir> listDir = iAgDir.findAllDir();
            iAgDir.deleteDir(id);
            if (listDir == null || listDir.size() <= 0) {
                AgDir agDir = new AgDir();
                agDir.setId("root");
                agDir.setName("目录管理");
                agDir.setXpath("/目录管理");
                agDir.setDirSeq("root");
                iAgDir.saveDir(agDir);
            }
            log.info(LogUtil.createSuccessLogInfo(request,funcName).toString());
            return new ResultForm(true);
        } catch (Exception e) {
            log.error(LogUtil.createFailLogInfo(request,funcName,e.getMessage()).toString());
            e.printStackTrace();
        }
        return new ResultForm(false);
    }

    @Autowired
    private IAgDataTrans iAgDataTrans;
    /**
     * 获取所有图层
     * @param page
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "查询图层",notes = "分页查询图层相关接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "图层名称",dataType = "String"),
            @ApiImplicitParam(name = "isVector",value = "是否矢量图层,1:是 0:否",dataType = "String"),
            @ApiImplicitParam(name = "page",value = "分页参数:page=1&rows=10",dataType = "String")
    })
    @RequestMapping(value = "/layerList",method = RequestMethod.GET)
    public ContentResultForm layerList(String name,String isVector, Page page) throws Exception {
        PageInfo<AgLayer> pageInfo =iAgDir.searchLayer(name,isVector, page);
        List<AgLayer> list = pageInfo.getList();
        // 获取缓冲图层进度信息
        for (AgLayer agLayer : list){
            double progress = iAgDataTrans.getProgress(agLayer.getDataSourceId(), agLayer.getLayerTable());
            String data = agLayer.getData();
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(data);
            jsonObject.put("persent",progress);
            agLayer.setData(jsonObject.toJSONString());
        }
        pageInfo.setList(list);
        return new ContentResultForm(true,PageHelper.toEasyuiPageInfo(pageInfo));
    }
    /**
     * 获取指定目录包含的图层
     *
     * @param dirId
     * @param isContain
     * @param agLayer
     * @param page
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取指定目录包含的图层",notes = "获取指定目录包含的图层接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirId",value = "目录ID",paramType = "path",required = true),
            @ApiImplicitParam(name = "isBaseMap",value = "是否仅底图,1:是 0:否 ",dataType = "String"),
            @ApiImplicitParam(name = "isContain",value = "是否包含子节点,1:是 0:否",dataType = "String"),
            @ApiImplicitParam(name = "name",value = "服务名称",dataType = "String"),
            @ApiImplicitParam(name = "page",value = "分页参数:page=1&rows=10",dataType = "Page")
    })
    @RequestMapping(value = "/layersByXpath/{dirId}",method = RequestMethod.GET)
    public ContentResultForm layersByXpath(HttpServletRequest request, @PathVariable("dirId") String dirId, String isBaseMap, String isContain, AgLayer agLayer, Page page) throws Exception {
        AgUser user = iAgUser.findUserByName(LoginHelpClient.getLoginName(request));
        //String userId = SecurityContext.getCurrentUserId();
        if (Common.isCheckNull(user)){
            return null;
        }

        PageInfo<AgLayer>  pageInfo =iAgDir.searchLayersByDirId(agLayer, page, dirId, user.getId(), isContain, isBaseMap);
        return new ContentResultForm(true,PageHelper.toEasyuiPageInfo(pageInfo));
    }

    /**
     * 获取用户包含的图层
     *
     * @return
     */
    @ApiOperation(value = "根据用户ID查询图层",notes = "根据用户ID查询图层接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户ID",dataType = "String",paramType = "path",required = true),
            @ApiImplicitParam(name = "name",value = "图层名称",dataType = "String"),
            @ApiImplicitParam(name = "page",value = "分页参数:page=1&rows=10",dataType = "String"),
    })
    @RequestMapping(value = "/layerListByUser/{userId}",method = RequestMethod.GET)
    public ContentResultForm layerListByUser(@PathVariable("userId") String userId, String name, Page page) throws Exception {
        PageInfo<AgLayer> pageInfo =iAgDir.searchLayer(name, page, userId);
        return new ContentResultForm(true,PageHelper.toEasyuiPageInfo(pageInfo));
    }

    /**
     * 保存或修改图层
     *
     * @param agLayer
     * @return
     */
    @ApiOperation(value = "保存或修改图层",notes = "保存或修改图层接口")
    @ApiImplicitParam(name = "agLayer",value = "图层对象",dataType = "AgLayer")
    @RequestMapping(value = "/saveLayer",method = RequestMethod.POST)
    public ResultForm saveLayer(AgLayer agLayer, HttpServletRequest request) {
        //检测代理地址或服务地址
        if(!ProxyUtil.checkLayerProxyUrlAndUrl(agLayer)){
            return new ResultForm(false, "图层的代理地址或服务地址设置不正确！");
        }
        String funcName = null;
        if (StringUtils.isNotEmpty(agLayer.getId()))
            funcName = "修改图层";
        else
            funcName =  "新增图层";
        try {
            //获取登录名,用于服务创建人,先放到扩展字段中，在保存时拿出来，封装成AgMetadata对象保存到数据库中
            String loginName = LoginHelpClient.getLoginName(request);
            JSONObject data = JSONObject.fromObject(agLayer.getData());
            data.put("layerTypeCn", agLayer.getLayerTypeCn());
            data.put("owner", loginName);
            String contentType = request.getContentType();  //获取Content-Type
            if ((contentType != null) && (contentType.toLowerCase().startsWith("multipart/"))) {
                //保存缩略图
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
                for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
                    MultipartFile file = entity.getValue();
                    if (file.getSize() > 0) {
                        String filePath = UploadUtil.getUploadAbsolutePath()+AgDirImpl.getLayerPictureDir();
                        File layerPictureDir = new File(filePath);
                        if(!layerPictureDir.exists()){
                            layerPictureDir.mkdir();
                        }
                        String fileName = file.getOriginalFilename();
                        String suffixName = fileName.substring(fileName.lastIndexOf("."));
                        fileName = new Date().getTime() + suffixName; //解决中文问题
                        File temp = new File(filePath + fileName);
                        file.transferTo(temp);
                        data.put("picture", fileName);
                    }
                }
            }

            agLayer.setData(data.toString());
            String prefix = agLayer.getLayerType().substring(0, 2);
            String middle = agLayer.getLayerType().substring(2, 4);
            if ("03".equals(middle) || "00".equals(prefix)) {//奥格静态瓦片、一般网址单独保存
                try {
                    agLayer.setAddFlag("2");//新增或修改时更改flag为2
                    agLayer.setLayerTable(agLayer.getLayerTables());
                    agLayer.setFeatureType(agLayer.getFeatureTypes());
                    AgLayer temp = iAgDir.findLayerByUrl(agLayer.getUrl(), agLayer.getLayerTable());
                    if (temp != null && !temp.getId().equals(agLayer.getId())) {
                        logger.info(temp.getUrl() + temp.getLayerTable() + "---重复记录不保存!---");
                        if (StringUtils.isNotEmpty(agLayer.getDirId())) {
                            this.saveDirLayer(agLayer.getDirId(), temp.getId(), request);
                            return new ResultForm(false,agLayer.getNameCn()+"服务已注册过,不再重新注册!");  //  阻止保存两条操作日志
                        }
                    } else {
                        temp = iAgDir.findLayerByNameCn(agLayer.getNameCn());
                        if (temp != null && !temp.getId().equals(agLayer.getId())) {
                            String errorInfo = agLayer.getNameCn()+"服务已注册过,不再重新注册!";
                            log.error(LogUtil.createFailLogInfo(request,funcName,errorInfo).toString());
                            return new ResultForm(false, errorInfo);
                        }
                        iAgDir.saveLayer(agLayer);
                    }
                    log.info(LogUtil.createSuccessLogInfo(request,funcName).toString());
                    return new ResultForm(true,"保存成功");
                } catch (Exception e) {
                    log.error(LogUtil.createFailLogInfo(request,funcName,e.getMessage()).toString());
                    e.printStackTrace();
                    return new ResultForm(false, "图层保存失败，请联系管理员！");
                }
            }
            // 如果是矢量图层,
            if (agLayer.getLayerType().equals("010001")){
                agLayer.setName(agLayer.getNameCn());
            }
            if (StringUtils.isNotEmpty(agLayer.getName())) {//除 奥格静态瓦片、一般网址 之外 的 服务类型
                agLayer.setAddFlag("2");//新增或修改时更改flag为2
                //下面的"".split(",")这里只是让程序不会因为空引用报错
                String[] layerTables =agLayer.getLayerTables()==null?"".split(","): agLayer.getLayerTables().split(",");
                String[] names = agLayer.getName().split(",");
                String[] nameCns = agLayer.getNameCn().split(",");
                String[] featureTypes = agLayer.getFeatureTypes()==null?"".split(","):agLayer.getFeatureTypes().split(",");
                if (layerTables!=null && layerTables.length == names.length) {
                    for (int i = 0; i < layerTables.length; i++) {
                        AgLayer saveLayer = ReflectBeans.copy(agLayer, AgLayer.class);
                        saveLayer.setLayerTable(layerTables[i]);
                        saveLayer.setName(names[i]);
                        saveLayer.setNameCn(nameCns[i]);
                        saveLayer.setFeatureType(featureTypes[i]);
                        AgLayer temp = iAgDir.findLayerByUrl(saveLayer.getUrl(), saveLayer.getLayerTable());
                        if (temp != null && !temp.getId().equals(saveLayer.getId())) {
                            logger.info(temp.getUrl() + temp.getLayerTable() + "---重复记录不保存!---");
                            if(saveLayer.getId() == null){
                                String errorInfo = saveLayer.getNameCn()+"别名已存在，新增失败";
                                log.error(LogUtil.createFailLogInfo(request,funcName,errorInfo).toString());
                                return new ResultForm(false,  errorInfo);
                            }
                            if (StringUtils.isNotEmpty(agLayer.getDirId())) {
                                this.saveDirLayer(agLayer.getDirId(), temp.getId(), request);
                            }
                            continue;
                        } else {
                            temp = iAgDir.findLayerByNameCn(agLayer.getNameCn());
                            if (temp != null && temp.getUrl() == null){
                                temp.setUrl("");
                            }
                            if (temp != null && temp.getUrl().equals(agLayer.getUrl()) && temp.getLayerType().equals(agLayer.getLayerType()) &&!temp.getId().equals(agLayer.getId())) {
                                String errorInfo = agLayer.getNameCn()+"别名已存在，新增失败";
                                log.error(LogUtil.createFailLogInfo(request,funcName,errorInfo).toString());
                                return new ResultForm(false, errorInfo);
                            }

                        }
                        iAgDir.saveLayer(saveLayer);

                    }
                    log.info(LogUtil.createSuccessLogInfo(request,funcName).toString());
                    return new ResultForm(true,"保存成功!");
                }else{
                    return new ResultForm(true,"保存失败，图层表为空!");
                }

            } else {
                String errorInfo = "图层表名或名称不能为空";
                log.error(LogUtil.createFailLogInfo(request,funcName,errorInfo).toString());
                return new ResultForm(false, "图层表名或名称不能为空！");
            }
        } catch (Exception e) {
            log.error(LogUtil.createFailLogInfo(request,funcName,e.getMessage()).toString());
            e.printStackTrace();
        }
        return new ResultForm(false, "图层保存失败，请联系管理员！");
    }

    /**
     * 批量删除图层
     *
     * @param dirLayerIds
     * @return
     */
    @ApiOperation(value = "根据ID批量删除图层",notes = "根据ID批量删除图层接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "layerIds",value = "图层ID,多个ID逗号分隔",required = true),
            @ApiImplicitParam(name = "dirId",value = "目录ID",required = true),
            @ApiImplicitParam(name = "layerList",value = "包含图层id,dirLayerId,dirPath字段和值得json字符串",required = true)
    })
    @RequestMapping(value = "/deleteLayers",method = RequestMethod.DELETE)
    public ResultForm deleteLayers(String dirLayerIds, HttpServletRequest request) {

        //String uploadPath = UploadUtil.getUploadPath(request);
        String uploadAbsolutePath = UploadUtil.getUploadAbsolutePath();
        //JSONObject jsonObject = this.getLogInfo(request, "删除图层");
        String funcName = "删除图层";

        try {
            iAgDir.deleteLayerBatch(dirLayerIds,uploadAbsolutePath);
            log.info(LogUtil.createSuccessLogInfo(request,funcName).toString());
            return new  ContentResultForm (true);
        } catch (Exception e) {
            log.error(LogUtil.createFailLogInfo(request,funcName,e.getMessage()).toString());
            e.printStackTrace();
        }
        return new ResultForm(false);
    }

    /**
     * 删除目录图层关联
     *
     * @param dirLayerIds
     * @return
     */
    @ApiOperation(value = "删除目录图层关联关系",notes = "删除目录图层关联关系接口")
    @ApiImplicitParam(name = "dirLayerIds",value = "目录图层关联ID",required = true)
    @RequestMapping(value = "/delDirLayer",method = RequestMethod.DELETE)
    public ResultForm delDirLayer(String dirLayerIds, HttpServletRequest request) {
        String ids[] = dirLayerIds.split(",");
        String funcName = "删除目录图层关联";
        boolean result = false;
        try {
            if (ids != null && ids.length > 0) {
                for (String id : ids) {
                    iAgDir.delDirLayer(id);
                    result =true;
                }
            }
        } catch (Exception e) {
            log.error(LogUtil.createFailLogInfo(request,funcName,e.getMessage()).toString());
            e.printStackTrace();
        }
        if(result){
            log.info(LogUtil.createSuccessLogInfo(request,funcName).toString());
        }
        return new ResultForm(result);
    }


/*
    @RequestMapping("/moveTopOrUp")
    public String moveTopOrUp(String dirLayerIds) {
        String[] ids = null;
        if (StringUtils.isNotEmpty(dirLayerIds)) {
            ids = dirLayerIds.split(",");
        }
        if (ids != null && ids.length > 0) {
            String temp = null;
            for (int i = ids.length - 1; i > 0; i--) {
                if (i == ids.length - 1) {
                    temp = ids[i];
                }
                ids[i] = ids[i - 1];
            }
            if (temp != null) ids[0] = temp;
            try {
                List<AgDirLayer> list = iAgDir.findDirLayerByIds(ids);
                List<AgDirLayer> listUpdate = new ArrayList<AgDirLayer>();
                if (list != null && list.size() > 0 && list.size() == ids.length) {
                    for (int i = 0; i < list.size(); i++) {
                        AgDirLayer agDirLayer = new AgDirLayer();
                        agDirLayer.setId(ids[i]);
                        agDirLayer.setOrderNm(list.get(i).getOrderNm());
                        listUpdate.add(agDirLayer);
                    }
                }
                if (listUpdate.size() > 0) {
                    iAgDir.updateDirLayerBatch(listUpdate);
                }
                return JsonUtils.toJson(new ResultForm(true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return JsonUtils.toJson(new ResultForm(false));
    }*/
    @ApiOperation(value = "图层目录拖拽排序",notes = "图层目录拖拽排序接口,同级拖拽排序")
    @ApiImplicitParam(name = "dirIds",value = "拖拽目录之后的同级目录的ID拼接成的字符串,逗号分隔")
    @RequestMapping(value = "/dirmoveBottomOrDown",method = RequestMethod.POST)
    public ResultForm dirmoveBottomOrDown(String dirIds) {
        String[] ids = null;
        if (StringUtils.isNotEmpty(dirIds)) {
            ids = dirIds.split(",");
        }
        if (ids != null && ids.length > 0) {
            String temp = null;
//            for (int i = 0; i < ids.length - 1; i++) {
//                if (i == 0) {
//                    temp = ids[i];
//                }
//                ids[i] = ids[i + 1];
//            }
            if (temp != null) ids[ids.length - 1] = temp;
            try {
                List<AgDir> list = iAgDir.findDirByIds(ids);
                List<AgDir> listUpdate = new ArrayList<AgDir>();
                if (list != null && list.size() > 0 && list.size() == ids.length) {
                    for (int i = 0; i < list.size(); i++) {
                        AgDir agDir = new AgDir();
                        agDir.setId(ids[i]);
                        agDir.setOrderNm(list.get(i).getOrderNm());
                        listUpdate.add(agDir);
                    }
                }
                if (listUpdate.size() > 0) {
                    iAgDir.updateDirBatch(listUpdate);
                }
                return new ResultForm(true,"更新成功!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResultForm(false,"更新失败!");
    }

    @ApiOperation(value = "图层拖拽移动",notes = "图层拖拽移动接口")
    @ApiImplicitParam(name = "dirLayerIds",value = "目录图层关联ID,移动后目录图层ID拼接的字符串，逗号分隔",paramType = "body")
    @RequestMapping(value = "/moveBottomOrDown",method = RequestMethod.POST)
    public ResultForm moveBottomOrDown(String dirLayerIds) {
        String[] ids = null;
        if (StringUtils.isNotEmpty(dirLayerIds)) {
            ids = dirLayerIds.split(",");
        }
        if (ids != null && ids.length > 0) {
            String temp = null;
//            for (int i = 0; i < ids.length - 1; i++) {
//                if (i == 0) {
//                    temp = ids[i];
//                }
//                ids[i] = ids[i + 1];
//            }
            if (temp != null) ids[ids.length - 1] = temp;
            try {
                List<AgDirLayer> list = iAgDir.findDirLayerByIds(ids);
                List<AgDirLayer> listUpdate = new ArrayList<AgDirLayer>();
                if (list != null && list.size() > 0 && list.size() == ids.length) {
                    for (int i = 0; i < list.size(); i++) {
                        AgDirLayer agDirLayer = new AgDirLayer();
                        agDirLayer.setId(ids[i]);
                        agDirLayer.setOrderNm(list.get(i).getOrderNm());
                        listUpdate.add(agDirLayer);
                    }
                }
                if (listUpdate.size() > 0) {
                    iAgDir.updateDirLayerBatch(listUpdate);
                }
                return new ResultForm(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResultForm(false);
    }

    /**
     * 保存目录图层关联
     *
     * @param dirId
     * @param layerIds
     * @return
     */
    @ApiOperation(value = "保存目录图层关联",notes = "保存目录图层关联接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirId",value = "目录ID"),
            @ApiImplicitParam(name = "layerIds",value = "图层ID,多个ID逗号分隔"),
    })
    @RequestMapping(value = "saveDirLayer",method = RequestMethod.POST)
    public ResultForm saveDirLayer(String dirId, String layerIds, HttpServletRequest request) {
        String ids[] = layerIds.split(",");
        List<String> deleteKeys = new ArrayList<>();
        String funcName = "新增目录图层关联";
        try {
            if (ids != null && ids.length > 0) {
                List<AgDirLayer> list = new ArrayList<AgDirLayer>();
                for (String layerId : ids) {
                    AgDirLayer agDirLayer = iAgDir.getDirLayer(dirId, layerId);
                    if (agDirLayer == null && StringUtils.isNotEmpty(dirId) && StringUtils.isNotEmpty(layerId)) {
                        agDirLayer = new AgDirLayer();
                        agDirLayer.setId(UUID.randomUUID().toString());
                        agDirLayer.setDirId(dirId);
                        agDirLayer.setLayerId(layerId);
                        list.add(agDirLayer);
                    }
                    //2017-07-13增加，用于清除和agproxy关联的redis缓存
                    AgLayer layer = iAgDir.findLayerByLayerId(layerId);
                    if (layer.getProxyUrl() != null) {
                        deleteKeys.add("service#layer:" + layer.getProxyUrl());
                    }
                }
                if (deleteKeys.size() > 0 && RedisCondition.matches() && MapCache.redisIsAvaliable(MapCache.redisIsAvaliableKey)) {
                    stringRedisTemplate.delete(deleteKeys);
                }
                String maxOrder = iAgDir.getLayerOrder();
                if (StringUtils.isEmpty(maxOrder)) {
                    maxOrder = "0";
                }
                int orderNm = Integer.parseInt(maxOrder);
                for (AgDirLayer agDirLayer : list) {
                    agDirLayer.setOrderNm(++orderNm);
                    iAgDir.saveDirLayer(agDirLayer);
                }
            }
            log.info(LogUtil.createSuccessLogInfo(request,funcName).toString());
            return new ResultForm(true,"保存成功!");
        } catch (Exception e) {
            log.error(LogUtil.createFailLogInfo(request,funcName,e.getMessage()).toString());
            e.printStackTrace();
        }
        return new ResultForm(false,"保存失败!");
    }


    /**
     * 获取子图层
     *
     * @param
     * @param
     * @return
     */
 /*   @RequestMapping("/getChildLayers")
    public String getChildLayers(String url, String typeCode) {
        String layerType = typeCode.substring(0, 2);
        String tileType = typeCode.substring(2, 4);
        String firm = typeCode.substring(4, 6);

        Map<String, String> map = new HashMap<String, String>();
        if ("03".equals(layerType) && "02".equals(firm)) {//解析wms
            if (url.indexOf("WMSServer") != -1) {//筛选wms服务图层
                url += "?request=GetCapabilities&service=WMS";
                if (!ReadUrlUtil.checkService(url)) {
                    return JsonUtils.toJson(new ResultForm(false, "服务地址无法访问！"));
                }
                map = ReadUrlUtil.queryWMSLayerTable(url);
            }
        } else if ("04".equals(layerType) && "02".equals(firm)) {//解析wfs
            if (url.indexOf("WFSServer") != -1) {//筛选wfs服务图层
                url += "?request=GetCapabilities&service=WFS";
                if (!ReadUrlUtil.checkService(url)) {
                    return JsonUtils.toJson(new ResultForm(false, "服务地址无法访问！"));
                }
                map = ReadUrlUtil.queryOGC_WFSLayerTable(url);
            }
        } else if ("07".equals(layerType) && "02".equals(firm)) {//解析FeatureServer
            if (url.indexOf("FeatureServer") != -1) {//筛选FeatureServer服务图层
                url += "?f=pjson";
                if (!ReadUrlUtil.checkService(url)) {
                    return JsonUtils.toJson(new ResultForm(false, "服务地址无法访问！"));
                }
                map = ReadUrlUtil.queryFeatureServerLayerTableByRest(url);
            }
        } else if ("02".equals(tileType)) {//解析MapServer
            url += "?f=pjson";
            if (!ReadUrlUtil.checkService(url)) {
                return JsonUtils.toJson(new ResultForm(false, "服务地址无法访问！"));
            }
            map = ReadUrlUtil.queryMapServerLayerTableByRest(url);
        }
        if (map.size() > 0) {
            StringBuffer sb = new StringBuffer("[");
            for (String layerTable : map.keySet()) {
                sb.append("{'layerTable' : '" + layerTable + "','name' : '" + map.get(layerTable) + "'},");
            }
            sb.deleteCharAt(sb.length() - 1).append("]");
            return JsonUtils.toJson(new ResultForm(true, sb.toString()));
        } else {
            return JsonUtils.toJson(new ResultForm(false, "该图层下没有子图层！"));
        }
    }*/
    @ApiOperation(value = "获取代理地址",notes = "获取代理地址接口")
    @RequestMapping(value = "/getProxyUrl",method = RequestMethod.GET)
    public ContentResultForm<String> getProxyUrl() {
        String agProxyUrl = ProxyUtil.getProxyPreUrl() + UUIDUtil.getUUID();
        //        String js = "{\"success\":true,\"message\":\"" + agProxyUrl + "\"}";
        return new ContentResultForm<String>(true,agProxyUrl,"获取代理地址成功");
    }

    @ApiOperation(value = "检验预览uuid",notes = "检验预览uuid接口")
    @ApiImplicitParam(name = "previewUuid",value = "预览uuid")
    @RequestMapping(value = "/checkPreviewUuid",method = RequestMethod.GET)
    public ResultForm checkPreviewUuid(String previewUuid) {
        if (previewUuid.equals(UuidChanger.getUuid()))
            return new ResultForm(true);
        else
            return new ResultForm(false);
    }

   /* @RequestMapping("/layerPreview")
    public ModelAndView layerPreview() {
        return new ModelAndView("/agcom/dir/layerPreview");
    }*/

    @ApiOperation(value = "获取预览Key值",notes = "获取预览Key值接口")
    @RequestMapping(value = "/getPreviewKey",method = RequestMethod.GET)
    public ContentResultForm getPreviewKey() throws Exception {
        String previewKey = UuidChanger.getUuid();
        String keyName = "proxyCache:previewKey";
        //如果配置了redis缓存，则将预览KEY存储到redis中
        if (RedisCondition.matches() && MapCache.redisIsAvaliable(MapCache.redisIsAvaliableKey)) {
            //为了避免多个agsupport产生的预览KEY不一致，统一使用缓存中的KEY，没有则由任意一个agsupport创建，并缓存
            //将previewKey存储到redis中，agproxy可以直接从缓存中获取并验证，proxyCache:前缀是要跟agproxy中的缓存前缀统一
            stringRedisTemplate.opsForValue().set(keyName, previewKey, 30, TimeUnit.MINUTES);
        } else {
            //否则存储到数据库中，从数据库共享
            iAgDir.putPreviewKey(keyName, previewKey);
        }
        return new ContentResultForm(true,previewKey);
    }

    @ApiOperation(value = "校验图层配置信息和数据源是否正确",notes = "校验图层配置信息和数据源是否正确接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId",value = "数据源ID"),
            @ApiImplicitParam(name = "layerTable",value = "表名"),
            @ApiImplicitParam(name = "pkColumn",value = "图层主键"),
            @ApiImplicitParam(name = "geometryColumn",value = "空间字段")
    })
    @RequestMapping(value = "/checkLayerTable",method = RequestMethod.GET)
    public ResultForm checkLayerTable(String dataSourceId, String layerTable, String pkColumn, String geometryColumn) {
        if (iAgDir.checkLayerTable(dataSourceId, layerTable, pkColumn, geometryColumn)) {
            return new ResultForm(true);
        } else {
            return new ResultForm(false, "请检查图层配置信息和数据源是否正确!");
        }
    }

    /**
     * 按用id获取目录图层树 bootstrap-tree
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "按用id获取目录图层树",notes = "按用id获取目录图层树接口")
    @ApiImplicitParam(name = "userId",value = "用户ID",required = true,paramType = "path")
    @RequestMapping(value = "/getDirTree/{userId}",method = RequestMethod.GET)
    public ContentResultForm getDirTree(@PathVariable("userId") String userId) throws Exception {
        String treeJson = iAgDir.getTreeByUser(userId);
        treeJson = treeJson.replaceAll("#proxyHost_", ConfigProperties.getByKey("agproxy.url"));
        return new ContentResultForm(true,treeJson,"目录图层树");
    }

    /**
     * 根据layerId获取layer详细信息（包括拓展字段）
     *
     * @param layerId layer主键
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据layerId获取layer详细信息（包括拓展字段）",notes = "根据layerId获取layer详细信息（包括拓展字段）接口")
    @ApiImplicitParam(name = "layerId",value = "图层ID",required = true,paramType = "path")
    @RequestMapping(value = "/findLayerByLayerId/{layerId}",method = RequestMethod.GET)
    public ContentResultForm findLayerByLayerId(@PathVariable("layerId") String layerId) throws Exception {
        return new ContentResultForm<AgLayer>(true,iAgDir.findLayerByLayerId(layerId));
    }

    /**
     * 根据dirLayerId获取layer详细信息（包括拓展字段）
     *
     * @param dirLayerId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据dirLayerId获取layer详细信息（包括拓展字段）",notes = "根据dirLayerId获取layer详细信息（包括拓展字段）接口")
    @ApiImplicitParam(name = "dirLayerId",value = "目录图层关联ID",required = true,paramType = "path")
    @RequestMapping(value = "/findLayerByDirLayerId/{dirLayerId}",method = RequestMethod.GET)
    public ContentResultForm findLayerByDirLayerId(@PathVariable("dirLayerId") String dirLayerId) throws Exception {
        return new ContentResultForm<AgLayer>(true,iAgDir.findLayerByDirLayerId(dirLayerId));
    }

    @ApiOperation(value = "根据dirLayerId查询图层关联的矢量图层id",notes = "根据dirLayerId查询图层关联的矢量图层id接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirLayerId",value = "目录图层关联ID",required = true),
            @ApiImplicitParam(name = "userId",value = "用户ID",required = true)
    })
    @RequestMapping(value = "/findBindAuthorizedVector",method = RequestMethod.GET)
    public ContentResultForm findBindAuthorizedVector(String dirLayerId, String userId) throws Exception {

        return new ContentResultForm(true,iAgDir.findBindAuthorizedVector(dirLayerId, userId));
    }

    /**
     * 按用户id，要素类型获取图层信息
     *
     * @param userId
     * @param featureType
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "按用户id，要素类型获取图层信息",notes = "按用户id，要素类型获取图层信息接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户ID",required = true),
            @ApiImplicitParam(name = "featureType",value = "要素类型",required = true)
    })
    @RequestMapping(value = "/layersByUserIdAndFeatureType",method = RequestMethod.GET)
    public ContentResultForm layersByUserIdAndFeatureType(String userId, String featureType) throws Exception {
        List<LayerForm> list = iAgDir.findLayerByUserIdAndFeatureType(userId, featureType);
        JsonMapper mapper = new JsonMapper(JsonInclude.Include.ALWAYS);//不去掉为空字段
        return new ContentResultForm(true,list);
    }

    /**
     * 获取所有矢量图层
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/findVectorLayer")
    public ContentResultForm findVectorLayer() throws Exception {
        String result =iAgDir.findVectorLayer();
        return new ContentResultForm<String>(true,result);
    }

    @RequestMapping("/findRenderLayer")
    public ContentResultForm findRenderLayer() throws Exception {
        String result = iAgDir.findRenderLayer();
        return new ContentResultForm<String>(true,result);
    }

    /**
     * 按图层别名查找图层
     *
     * @param nameCn
     * @return
     */
    @RequestMapping("/findLayerByNameCn")
    public ContentResultForm findLayerByNameCn(String nameCn) throws Exception {
        AgLayer agLayer = iAgDir.findLayerByNameCn(nameCn);
        return new ContentResultForm<AgLayer>(true,agLayer);
    }

    @RequestMapping("/findLayerByXpathAndLoginName")
    public ContentResultForm findLayerByXpathAndLoginName(String xpath, String loginName) throws Exception {
        AgUser agUser = iAgUser.findUserByName(loginName);
        String userId = null;
        if (agUser != null) {
            userId = agUser.getId();
        }
        List<AgLayer> list = iAgDir.findLayerByXpathAndUserId(xpath, userId);
        return new ContentResultForm<List<AgLayer>>(true,list);
    }

    @RequestMapping("/findLayerByDirNameAndLayerName")
    public ContentResultForm findLayerByDirNameAndLayerName(String dirName, String layerName, String loginName) throws Exception {
        AgUser agUser = iAgUser.findUserByName(loginName);
        String userId = null;
        if (agUser != null) {
            userId = agUser.getId();
        }
        List<AgLayer> list = iAgDir.findLayerByDirNameAndLayerName(dirName, layerName, userId);
        return new ContentResultForm<List<AgLayer>>(true,list);
    }

    /**
     * 通用服务--按条件查询服务
     *
     * @param type
     * @param keyWord
     * @param year
     * @return
     */
    @RequestMapping("/queryService")
    public ContentResultForm queryService(String type, String keyWord, String year, Page page) throws Exception {
        if (!Common.isCheckNull(type)) {
            switch (type) {
                case "WMS":
                    type = "03____";
                    break;
                case "WFS":
                    type = "04____";
                    break;
                case "WMTS":
                    type = "05____";
                    break;
                case "REST":
                    type = "020202";
                    break;
                default:
                    type = null;
                    break;
            }
        }
        PageInfo<AgLayer> pageInfo=iAgDir.searchLayer(type, keyWord, year, page);
        EasyuiPageInfo<AgLayer> result = PageHelper.toEasyuiPageInfo(pageInfo);
        return new ContentResultForm<EasyuiPageInfo<AgLayer>>(true, result);
    }

    @RequestMapping("/getToken")
    public ContentResultForm getToken(String id) throws Exception {
        String token = iAgDir.getToken(id);
        return new ContentResultForm<String>(true,token);
    }

    @RequestMapping("/getServerLink")
    public ContentResultForm getServerLink() throws Exception {
        List<AgServer> serverLink = iAgDir.getServerLink();
        return new ContentResultForm<List<AgServer>>(true,serverLink);
    }

    /**
     * 根据用户扮演的角色查询专题下的图层
     *
     * @param request
     * @param userId
     * @return
     */
    @RequestMapping("/getSpecialLayer/{userId}")
    public  ContentResultForm getSpecialLayerByRole(HttpServletRequest request, @PathVariable("userId") String userId) {
        ContentResultForm<List<ReflectMap>> result = new ContentResultForm<List<ReflectMap>>(false);
        try {
            List<ReflectMap> list_result = new ArrayList<ReflectMap>();
            List<AgDir> agDirs = iAgDir.findSecondDir();
            for (AgDir agDir : agDirs) {
                // 获取某个专题下的所有图层
                List<AgLayer> AgLayers = iAgDir.findLayerByUserIdAndXpath(userId, agDir.getXpath());
                AgMapParam agMapParam = iAgParam.findMapParamById(agDir.getMapParamId());
                String reference = agMapParam.getReference();
                //转换坐标系 默认WGS84
                String transReference = request.getParameter("transReference") == null ? "WGS84" : request.getParameter("transReference");
                String name = reference + "转" + transReference;
                //AgDic agDic = iAgDic.findValueByCoorName(name);

             /*   //调用agcloud获取
                Map<String, String> param = new HashMap<>();
                param.put("typeCode","A103");
                param.put("orgId",orgId);
                HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/bsc/dic/code/lgetItemsByTypeCode.do", param);
                JSONArray jsonArray = JSONArray.fromObject(httpRespons.getContent());
                List<AgDic> list = new AgDicConverter().convertToList(null, jsonArray);
                AgDic agDic = list.get(0);*/
                AgDic agDic = iAgDic.findAgDicByCode("A103");

                ReflectMap rfmap = new ReflectMap();
                rfmap.put("projectName", agDir.getName());
                rfmap.put("layers", AgLayers);
                rfmap.put("id", agDir.getId());
                rfmap.put("mapid", agMapParam.getId());
                rfmap.put("origin", agMapParam.getOrigin()); //切图原点
                rfmap.put("extent", agMapParam.getExtent());        //地图范围
                rfmap.put("center", agMapParam.getCenter());        //中心点
                rfmap.put("scales", agMapParam.getScales());        //分辨率
                rfmap.put("zoom", agMapParam.getZoom());        //初试化等级
                rfmap.put("reference", reference); // 坐标系统
                if (agDic.getValue() != null) {
                    JSONObject paramJson = JSONObject.fromObject(agDic.getValue());
                    rfmap.put("sevenParam", paramJson.get("sevenParam") == null ? "" : paramJson.getString("sevenParam")); // 七参数
                    rfmap.put("fourParam", paramJson.get("fourParam") == null ? "" : paramJson.getString("fourParam")); // 四参数
                    rfmap.put("transSevenParam", paramJson.get("transSevenParam") == null ? "" : paramJson.getString("transSevenParam")); // 84转7参数
                    rfmap.put("transFourParam", paramJson.get("transFourParam") == null ? "" : paramJson.getString("transFourParam")); // 84转4参数
                    rfmap.put("ellipsoidParam", paramJson.get("ellipsoidParam") == null ? "" : paramJson.getString("ellipsoidParam")); // 椭球参数
                }
                list_result.add(rfmap);
            }
            result.setSuccess(true);
            result.setContent(Common.fromObject(list_result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/setLayerUseCache")
    public ResultForm setLayerUseCache(String dirLayerIds) throws Exception {
        iAgDir.setLayerUseCache(dirLayerIds);
        return new ResultForm(true, "设置成功！");
    }



    /*
     * 在数据库中查出指定表的全部字段返回前端，如果字段在Agsupport中配置了别名，则返回别名
     * */
    @RequestMapping("/findTableFields")
    public ContentResultForm findTableFields(String dataSourceId, String layerTable,String layerId) throws Exception {
        List<Map<String, String>> titleAlias = iAgDir.findTableFields(dataSourceId,layerTable,layerId);
        return new ContentResultForm<List<Map<String, String>>>(true,titleAlias);
    }

    @RequestMapping("/findTableFieldsByDirLayerId")
    public ContentResultForm findTableFieldsByDirLayerId(String dirLayerId) throws Exception {
        List<AgLayerFieldConf> tableFields = iAgDir.findTableFieldsByDirLayerId(dirLayerId);
        return new ContentResultForm<List<AgLayerFieldConf>>(true,tableFields);
    }

    /**
     * 根据登录用户id获取图层列表
     * @param userId
     * @return
     */
    @RequestMapping("/findLayerByUserId")
    public ContentResultForm findLayerByUserId(String userId) throws Exception {
        List<AgLayer> agLayers = iAgDir.getPageByUserId(userId);
        return new ContentResultForm<List<AgLayer>>(true,agLayers);
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
        //String loginName = SecurityContext.getCurrentUser().getLoginName();
        String loginName = LoginHelpClient.getLoginName(request);
        if ("anonymousUser".equals(loginName)){
            loginName = request.getParameter("loginName");
        }
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        jsonObject.put("loginName", loginName);
        jsonObject.put("sysName", "agsupport");
        jsonObject.put("ipAddress", Common.getIpAddr(request));
        jsonObject.put("browser", userAgent.getBrowser().getName());
        jsonObject.put("funcName", funcName);
        return jsonObject;
    }
}
