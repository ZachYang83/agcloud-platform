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
import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgLayer;
import com.augurit.agcloud.agcom.agsupport.sc.log.service.IAgLog;
import com.augurit.agcloud.agcom.agsupport.sc.param.service.IAgParam;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.agcom.agsupport.util.LogUtil;
import com.augurit.agcloud.agcom.syslog.service.annotation.SysLog;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonMapper;
import com.augurit.agcom.common.LoginHelpClient;
import com.common.util.*;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Augurit on 2017-04-17.
 */
@Api(value = "??????????????????", description = "??????????????????????????????")
@RestController
@RequestMapping("/agsupport/dir")
public class AgDirController {

    private static Logger logger = LoggerFactory.getLogger(AgDirController.class);
    @Autowired
    private IAgDir iAgDir;

    @Autowired
    private IAgLayer iAgLayer;

    @Autowired
    private IAgParam iAgParam;

    @Autowired
    private IAgUser iAgUser;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
     * ????????????????????????
     *
     * @param list_dir     ????????????
     * @param parentId     ????????? id
     * @param childListDir ???????????????????????????
     * @return
     */
    private List<AgDir> getChildDir(List<AgDir> list_dir, String parentId, List<AgDir> childListDir) {
        for (AgDir dir : list_dir) {
            String id = dir.getId();
            if (id.equals(parentId) && !"root".equals(id)) {
                childListDir.add(dir);
                if (dir.getParentId() != null && !"".equals(dir.getParentId())) {
                    getChildDir(list_dir, dir.getParentId(), childListDir);
                }
            }
        }
        return childListDir;
    }

    /**
     * ???????????????
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "????????????????????????", notes = "??????????????????????????????")
    @ApiImplicitParam(name = "id", value = "?????????id", paramType = "path", required = true)
    @RequestMapping(value = "/tree/{id}", method = RequestMethod.GET)
    public ContentResultForm tree(@PathVariable("id") String id, HttpServletRequest request) throws Exception {
        List<AgDir> list_dir = iAgDir.findAllDir();
        List<AgDir> listProject = new ArrayList<AgDir>();//???????????????List
        for (AgDir item : list_dir) {
            if (item.getParentId() != null && item.getParentId().equals("root")) {
                listProject.add(item);
            }
        }
        List<Ztree> listZtreeResult = new ArrayList<>();
        if ("sjhc_all".equals(id)) {
            List<AgDir> list_dir_hasData = iAgDir.findAllDirSJHC();//???????????? ???????????????????????????????????????
            List<AgDir> list = new ArrayList<>();
            for (AgDir agDir : list_dir_hasData) {
                String parentId = agDir.getParentId();
                list.add(agDir);
                List<AgDir> childListDir = new ArrayList<>();
                List<AgDir> resultList = new ArrayList<>();
                resultList = getChildDir(list_dir, parentId, childListDir);
                list.addAll(resultList);
            }
            list_dir = list;
        }

        //???????????????????????????
        for (AgDir agDir : list_dir) {
            Ztree ztree = new Ztree();
            ztree.setId(agDir.getId());
            ztree.setName(agDir.getName());
            ztree.setpId(agDir.getParentId());
            ztree.setInfo(agDir.getInfo());
            ztree.setOrder(agDir.getOrderNm());
            if (listProject.contains(agDir)) {
                ztree.setMapParamId(agDir.getMapParamId());
            } else {
                //????????????????????????????????????????????????
                for (AgDir item : listProject) {
                    if (agDir.getXpath().indexOf(item.getXpath()) == 0) {
                        ztree.setMapParamId(item.getMapParamId());
                    }
                }
            }
            if (null == agDir.getParentId()) {
                ztree.setOpen("true");
            } else {
                ztree.setOpen("false");
            }
            listZtreeResult.add(ztree);
        }
        return new ContentResultForm<List>(true, listZtreeResult);
    }

    /**
     * ????????????
     *
     * @param name
     * @param pid
     * @return
     */
    @SysLog(sysName = "????????????", funcName = "??????????????????")
    @ApiOperation(value = "??????????????????", notes = "????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "??????????????????", dataType = "AgDir"),
            @ApiImplicitParam(name = "info", value = "????????????", dataType = "String"),
            @ApiImplicitParam(name = "pid", value = "?????????ID", dataType = "String", paramType = "path", required = true)
    })
    @RequestMapping(value = "/saveDir/{pid}", method = RequestMethod.POST)
    public ResultForm saveDir(String name, @PathVariable("pid") String pid, String info, HttpServletRequest request) {
        String funcName = "??????????????????";
        try {
            AgDir agDir = new AgDir();
            agDir.setName(name);
            agDir.setId(UUID.randomUUID().toString());
            agDir.setInfo(info);
            if (!Common.isCheckNull(pid)) {
                AgDir parentDir = iAgDir.findDirById(pid);
                if (iAgDir.checkRepeatName(parentDir.getId(), name, null)) {
                    String errorInfo = "???????????????";
                    return new ResultForm(false, errorInfo);
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
            return new ResultForm(true, "????????????!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false, "????????????!");
    }

    /**
     * ????????????
     *
     * @param dirId
     * @param name
     * @return
     */
    @SysLog(sysName = "????????????", funcName = "????????????????????????")
    @ApiOperation(value = "????????????????????????", notes = "??????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirId", value = "???????????????ID", paramType = "path", required = true),
            @ApiImplicitParam(name = "name", value = "????????????", required = true),
            @ApiImplicitParam(name = "info", value = "????????????", dataType = "String"),
    })
    @RequestMapping(value = "/updateDir/{dirId}", method = RequestMethod.POST)
    public ResultForm updateDir(@PathVariable("dirId") String dirId, String name, String info, HttpServletRequest request) {
        String funcName = "??????????????????";
        try {
            AgDir agDir = iAgDir.findDirById(dirId);
            agDir.setInfo(info);
//            agDir.setMapParamId(mapParamId); ??????????????????

            if (iAgDir.checkRepeatName(agDir.getParentId(), name, dirId)) {
                String errorInfo = "???????????????";
                return new ResultForm(false, errorInfo);
            }

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
            return new ResultForm(true, "????????????!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false, "????????????!");
    }

    @SysLog(sysName = "????????????", funcName = "??????????????????")
    @ApiOperation(value = "????????????", notes = "??????????????????")
    @ApiImplicitParam(name = "id", value = "??????ID", required = true, paramType = "path")
    @RequestMapping(value = "/deleteDir/{id}", method = RequestMethod.DELETE)
    @Caching(evict = {@CacheEvict(value = "user#project_layer_tree", allEntries = true),
            @CacheEvict(value = "user#project_info", allEntries = true)})
    public ResultForm deleteDir(@PathVariable("id") String id, HttpServletRequest request) {
        String uploadPath = UploadUtil.getUploadAbsolutePath();
        String funcName = "??????????????????";
        List<AgDirLayer> agDirLayerTmp = new ArrayList<>();

        try {
            // ???????????????????????????
            List<AgDirLayer> all = iAgDir.findAll();
            // ??????????????????
            List<AgDir> list = iAgDir.findAllDir();

            List<AgDir> chdldList = new ArrayList<>();
            //List<AgDir> childDirs = getChildDirs(list, id,chdldList);
            AgDir dirById = iAgDir.findDirById(id);
            List<AgDir> childDirs = iAgDir.findDirsByXpath(dirById.getXpath());
            // ?????????????????????
            childDirs.add(dirById);
            for (AgDir agDir : childDirs) {
                List<AgDirLayer> dirLayers = new ArrayList<>();
                List<String> dirLayerIds = new ArrayList<>();
                String dirId = agDir.getId();
                // ???????????????????????????????????????
                List<AgDirLayer> agDirLayers = iAgDir.findByDirId(dirId);
                for (AgDirLayer agDirLayer : agDirLayers) {
                    String haslayerId = agDirLayer.getLayerId();
                    //String hasDirId = agDirLayer.getDirId();
                    // ??????????????????????????????????????????
                    int cnt = 0;
                    for (AgDirLayer allDirLayer : all) {
                        if (haslayerId.equals(allDirLayer.getLayerId())) {
                            cnt++;
                        }
                    }
                    // ??????1 ?????????????????????????????????????????????id;
                    // ????????????????????????????????????????????????????????????layerId
                    if (cnt > 1) {
                        // ??????????????????????????????????????????id
                        dirLayerIds.add(agDirLayer.getId());
                    } else {
                        dirLayers.add(agDirLayer);
                    }

                }
                // ?????????????????????????????????????????????
                if (dirLayers.size() > 0) {
                    iAgDir.deleteLayerBatch(dirLayers, uploadPath);
                }
                if (dirLayerIds.size() > 0) {
                    System.out.println("dirLayerIds:" + dirLayerIds.toString());
                    for (String dirLayerId : dirLayerIds) {
                        iAgDir.delDirLayer(dirLayerId);
                    }

                }

            }
            List<AgDir> listDir = iAgDir.findAllDir();
            iAgDir.deleteDir(id);
            if (listDir == null || listDir.size() <= 0) {
                AgDir agDir = new AgDir();
                agDir.setId("root");
                agDir.setName("????????????");
                agDir.setXpath("/????????????");
                agDir.setDirSeq("root");
                iAgDir.saveDir(agDir);
            }
            return new ResultForm(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false);
    }

    @Autowired
    private IAgDataTrans iAgDataTrans;

    /**
     * ??????????????????
     *
     * @param page
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "????????????", notes = "??????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "????????????", dataType = "String"),
            @ApiImplicitParam(name = "isVector", value = "??????????????????,1:??? 0:???", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "????????????:page=1&rows=10", dataType = "String")
    })
    @RequestMapping(value = "/layerList", method = RequestMethod.GET)
    public ContentResultForm layerList(String name, String isVector, Page page) throws Exception {
        PageInfo<AgLayer> pageInfo = iAgDir.searchLayer(name, isVector, page);
        List<AgLayer> list = pageInfo.getList();
        // ??????????????????????????????
        for (AgLayer agLayer : list) {
            double progress = iAgDataTrans.getProgress(agLayer.getDataSourceId(), agLayer.getLayerTable());
            String data = agLayer.getData();
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(data);
            jsonObject.put("persent", progress);
            agLayer.setData(jsonObject.toJSONString());
        }
        pageInfo.setList(list);
        return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(pageInfo));
    }

    /**
     * ?????????????????????????????????
     *
     * @param dirId
     * @param isContain
     * @param agLayer
     * @param page
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "?????????????????????????????????", notes = "???????????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirId", value = "??????ID", paramType = "path", required = true),
            @ApiImplicitParam(name = "isBaseMap", value = "???????????????,1:??? 0:??? ", dataType = "String"),
            @ApiImplicitParam(name = "isContain", value = "?????????????????????,1:??? 0:???", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "????????????", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "????????????:page=1&rows=10", dataType = "Page")
    })
    @RequestMapping(value = "/layersByXpath/{dirId}", method = RequestMethod.GET)
    public ContentResultForm layersByXpath(HttpServletRequest request, @PathVariable("dirId") String dirId, String isBaseMap, String isContain, AgLayer agLayer, Page page) throws Exception {
        AgUser user = iAgUser.findUserByName(LoginHelpClient.getLoginName(request));
        //String userId = SecurityContext.getCurrentUserId();
        if (Common.isCheckNull(user)) {
            return null;
        }
        PageInfo<AgLayer> pageInfo = iAgDir.searchLayersByDirId(agLayer, page, dirId, user.getId(), isContain, isBaseMap);
        List<AgLayer> list = pageInfo.getList();
        for (AgLayer eachLayer : list) {
            eachLayer.setDirPath(eachLayer.getDirPath().replace("/????????????", ""));
        }
        return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(pageInfo));
    }

    @ApiOperation(value = "?????????????????????????????????????????????", notes = "?????????????????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirId", value = "??????ID", paramType = "path", required = true),
            @ApiImplicitParam(name = "isBaseMap", value = "???????????????,1:??? 0:??? ", dataType = "String"),
            @ApiImplicitParam(name = "isContain", value = "?????????????????????,1:??? 0:???", dataType = "String"),
            @ApiImplicitParam(name = "name", value = "????????????", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "????????????:page=1&rows=10", dataType = "Page")
    })
    @RequestMapping(value = "/dataTransLayersByXpath/{dirId}", method = RequestMethod.GET)
    public ContentResultForm dataTransLayersByXpath(HttpServletRequest request, @PathVariable("dirId") String dirId, String isBaseMap, String isContain, AgLayer agLayer, Page page) throws Exception {
        AgUser user = iAgUser.findUserByName(LoginHelpClient.getLoginName(request));
        //String userId = SecurityContext.getCurrentUserId();
        if (Common.isCheckNull(user)) {
            return null;
        }
        PageInfo<AgLayer> pageInfo = iAgDir.searchLayersByDirId(agLayer, page, dirId, user.getId(), isContain, isBaseMap);
        // ??????????????????????????????
        List<AgLayer> list = pageInfo.getList();
        for (AgLayer layer : list) {
            double progress = iAgDataTrans.getProgress(layer.getDataSourceId(), layer.getLayerTable());
            String data = layer.getData();
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(data);
            jsonObject.put("persent", progress);
            layer.setData(jsonObject.toJSONString());
        }
        return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(pageInfo));
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @ApiOperation(value = "????????????ID????????????", notes = "????????????ID??????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "??????ID", dataType = "String", paramType = "path", required = true),
            @ApiImplicitParam(name = "name", value = "????????????", dataType = "String"),
            @ApiImplicitParam(name = "page", value = "????????????:page=1&rows=10", dataType = "String"),
    })
    @RequestMapping(value = "/layerListByUser/{userId}", method = RequestMethod.GET)
    public ContentResultForm layerListByUser(@PathVariable("userId") String userId, String name, Page page) throws Exception {
        PageInfo<AgLayer> pageInfo = iAgDir.searchLayer(name, page, userId);
        return new ContentResultForm(true, PageHelper.toEasyuiPageInfo(pageInfo));
    }


    /**
     * ??????????????????
     *
     * @param dirLayerIds
     * @return
     */
    @SysLog(sysName = "????????????", funcName = "????????????")
    @ApiOperation(value = "??????ID??????????????????", notes = "??????ID????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "layerIds", value = "??????ID,??????ID????????????", required = true),
            @ApiImplicitParam(name = "dirId", value = "??????ID", required = true),
            @ApiImplicitParam(name = "layerList", value = "????????????id,dirLayerId,dirPath???????????????json?????????", required = true)
    })
    @RequestMapping(value = "/deleteLayers", method = RequestMethod.DELETE)
    public ResultForm deleteLayers(String dirLayerIds, HttpServletRequest request) {

        //String uploadPath = UploadUtil.getUploadPath(request);
        String uploadAbsolutePath = UploadUtil.getUploadAbsolutePath();
        //JSONObject jsonObject = this.getLogInfo(request, "????????????");
        String funcName = "????????????";

        try {
            iAgDir.deleteLayerBatch(dirLayerIds, uploadAbsolutePath);
            return new ContentResultForm(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false);
    }

    /**
     * ????????????????????????
     *
     * @param dirLayerIds
     * @return
     */
    @SysLog(sysName = "????????????", funcName = "?????????????????????")
    @ApiOperation(value = "??????????????????????????????", notes = "????????????????????????????????????")
    @ApiImplicitParam(name = "dirLayerIds", value = "??????????????????ID", required = true)
    @RequestMapping(value = "/delDirLayer", method = RequestMethod.DELETE)
    public ResultForm delDirLayer(String dirLayerIds, HttpServletRequest request) {
        String ids[] = dirLayerIds.split(",");
        String funcName = "????????????????????????";
        boolean result = false;
        try {
            if (ids != null && ids.length > 0) {
                for (String id : ids) {
                    iAgDir.delDirLayer(id);
                    result = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    @SysLog(sysName = "????????????", funcName = "????????????????????????")
    @ApiOperation(value = "????????????????????????", notes = "??????????????????????????????,??????????????????")
    @ApiImplicitParam(name = "dirIds", value = "????????????????????????????????????ID?????????????????????,????????????")
    @RequestMapping(value = "/dirmoveBottomOrDown", method = RequestMethod.POST)
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
                return new ResultForm(true, "????????????!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResultForm(false, "????????????!");
    }

    @SysLog(sysName = "????????????", funcName = "??????????????????")
    @ApiOperation(value = "??????????????????", notes = "????????????????????????")
    @ApiImplicitParam(name = "dirLayerIds", value = "??????????????????ID,?????????????????????ID?????????????????????????????????", paramType = "body")
    @RequestMapping(value = "/moveBottomOrDown", method = RequestMethod.POST)
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
     * ????????????????????????
     *
     * @param dirId
     * @param layerIds
     * @return
     */
    @SysLog(sysName = "????????????", funcName = "?????????????????????")
    @ApiOperation(value = "????????????????????????", notes = "??????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirId", value = "??????ID"),
            @ApiImplicitParam(name = "layerIds", value = "??????ID,??????ID????????????"),
    })
    @RequestMapping(value = "saveDirLayer", method = RequestMethod.POST)
    public ResultForm saveDirLayer(String dirId, String layerIds, HttpServletRequest request) {
        String ids[] = layerIds.split(",");
        List<String> deleteKeys = new ArrayList<>();
        String funcName = "????????????????????????";
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
                    //2017-07-13????????????????????????agproxy?????????redis??????
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
            return new ResultForm(true, "????????????!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultForm(false, "????????????!");
    }


    /**
     * ???????????????
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
        if ("03".equals(layerType) && "02".equals(firm)) {//??????wms
            if (url.indexOf("WMSServer") != -1) {//??????wms????????????
                url += "?request=GetCapabilities&service=WMS";
                if (!ReadUrlUtil.checkService(url)) {
                    return JsonUtils.toJson(new ResultForm(false, "???????????????????????????"));
                }
                map = ReadUrlUtil.queryWMSLayerTable(url);
            }
        } else if ("04".equals(layerType) && "02".equals(firm)) {//??????wfs
            if (url.indexOf("WFSServer") != -1) {//??????wfs????????????
                url += "?request=GetCapabilities&service=WFS";
                if (!ReadUrlUtil.checkService(url)) {
                    return JsonUtils.toJson(new ResultForm(false, "???????????????????????????"));
                }
                map = ReadUrlUtil.queryOGC_WFSLayerTable(url);
            }
        } else if ("07".equals(layerType) && "02".equals(firm)) {//??????FeatureServer
            if (url.indexOf("FeatureServer") != -1) {//??????FeatureServer????????????
                url += "?f=pjson";
                if (!ReadUrlUtil.checkService(url)) {
                    return JsonUtils.toJson(new ResultForm(false, "???????????????????????????"));
                }
                map = ReadUrlUtil.queryFeatureServerLayerTableByRest(url);
            }
        } else if ("02".equals(tileType)) {//??????MapServer
            url += "?f=pjson";
            if (!ReadUrlUtil.checkService(url)) {
                return JsonUtils.toJson(new ResultForm(false, "???????????????????????????"));
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
            return JsonUtils.toJson(new ResultForm(false, "??????????????????????????????"));
        }
    }*/
    @ApiOperation(value = "??????????????????", notes = "????????????????????????")
    @RequestMapping(value = "/getProxyUrl", method = RequestMethod.GET)
    @ApiImplicitParam(name = "key", value = "?????????????????????")
    public ContentResultForm<String> getProxyUrlFromKey(String key) {
        if(StringUtils.isEmpty(key)){
            String agProxyUrl = ProxyUtil.getProxyPreUrl() + UUIDUtil.getUUID();
            //        String js = "{\"success\":true,\"message\":\"" + agProxyUrl + "\"}";
            return new ContentResultForm<String>(true, agProxyUrl, "????????????????????????");
        }
        String agProxyUrl = ProxyUtil.getProxyPreUrl(key);
        //        String js = "{\"success\":true,\"message\":\"" + agProxyUrl + "\"}";
        return new ContentResultForm<String>(true, agProxyUrl, "????????????????????????");
    }

    @ApiOperation(value = "??????????????????", notes = "???????????????????????????3dtlies???")
    @RequestMapping(value = "/getProxyUrl2", method = RequestMethod.GET)
    public ContentResultForm<String> getProxyUrl2(String layerType, HttpServletRequest request) {
        try {
            // 3dtiles ???????????????  http://ip:port/proxy/rest/services/userid/layerID/layerType
            // ??????????????????
            String loginName = LoginHelpClient.getLoginName(request);
            AgUser agUser = iAgUser.findUserByName(loginName);
            // ????????????id
            String userId = agUser.getId();
            // ??????
            String agProxyUrl = ProxyUtil.getProxyPreUrl() + "rest/services/" + userId + "/" + UUIDUtil.getUUID();
            //        String js = "{\"success\":true,\"message\":\"" + agProxyUrl + "\"}";
            return new ContentResultForm<String>(true, agProxyUrl, "????????????????????????");
        } catch (Exception e) {
            return new ContentResultForm<String>(false, "", "?????????????????????????????????????????????????????????");
        }
    }

    @ApiOperation(value = "????????????uuid", notes = "????????????uuid??????")
    @ApiImplicitParam(name = "previewUuid", value = "??????uuid")
    @RequestMapping(value = "/checkPreviewUuid", method = RequestMethod.GET)
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

    @ApiOperation(value = "????????????Key???", notes = "????????????Key?????????")
    @RequestMapping(value = "/getPreviewKey", method = RequestMethod.GET)
    public ContentResultForm getPreviewKey() throws Exception {
        String previewKey = UuidChanger.getUuid();
        String keyName = "proxyCache:previewKey";
        //???????????????redis?????????????????????KEY?????????redis???
        if (RedisCondition.matches() && MapCache.redisIsAvaliable(MapCache.redisIsAvaliableKey)) {
            //??????????????????agsupport???????????????KEY????????????????????????????????????KEY???????????????????????????agsupport??????????????????
            //???previewKey?????????redis??????agproxy??????????????????????????????????????????proxyCache:???????????????agproxy????????????????????????
            stringRedisTemplate.opsForValue().set(keyName, previewKey, 30, TimeUnit.MINUTES);
        } else {
            //????????????????????????????????????????????????
            iAgDir.putPreviewKey(keyName, previewKey);
        }
        return new ContentResultForm(true, previewKey);
    }

    @ApiOperation(value = "????????????????????????????????????????????????", notes = "??????????????????????????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dataSourceId", value = "?????????ID"),
            @ApiImplicitParam(name = "layerTable", value = "??????"),
            @ApiImplicitParam(name = "pkColumn", value = "????????????"),
            @ApiImplicitParam(name = "geometryColumn", value = "????????????")
    })
    @RequestMapping(value = "/checkLayerTable", method = RequestMethod.GET)
    public ResultForm checkLayerTable(String dataSourceId, String layerTable, String pkColumn, String geometryColumn) {
        if (iAgDir.checkLayerTable(dataSourceId, layerTable, pkColumn, geometryColumn)) {
            return new ResultForm(true);
        } else {
            return new ResultForm(false, "???????????????????????????????????????????????????!");
        }
    }

    /**
     * ??????id????????????????????? bootstrap-tree
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "??????id?????????????????????", notes = "??????id???????????????????????????")
    @ApiImplicitParam(name = "userId", value = "??????ID", required = true, paramType = "path")
    @RequestMapping(value = "/getDirTree/{userId}", method = RequestMethod.GET)
    public ContentResultForm getDirTree(@PathVariable("userId") String userId) throws Exception {
        String treeJson = iAgDir.getTreeByUser(userId);
        treeJson = treeJson.replaceAll("#proxyHost_", ConfigProperties.getByKey("agproxy.url"));
        return new ContentResultForm(true, treeJson, "???????????????");
    }

    /**
     * ??????layerId??????layer????????????????????????????????????
     *
     * @param layerId layer??????
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "??????layerId??????layer????????????????????????????????????", notes = "??????layerId??????layer??????????????????????????????????????????")
    @ApiImplicitParam(name = "layerId", value = "??????ID", required = true, paramType = "path")
    @RequestMapping(value = "/findLayerByLayerId/{layerId}", method = RequestMethod.GET)
    public ContentResultForm findLayerByLayerId(@PathVariable("layerId") String layerId) throws Exception {
        return new ContentResultForm<AgLayer>(true, iAgDir.findLayerByLayerId(layerId));
    }

    /**
     * ??????dirLayerId??????layer????????????????????????????????????
     *
     * @param dirLayerId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "??????dirLayerId??????layer????????????????????????????????????", notes = "??????dirLayerId??????layer??????????????????????????????????????????")
    @ApiImplicitParam(name = "dirLayerId", value = "??????????????????ID", required = true, paramType = "path")
    @RequestMapping(value = "/findLayerByDirLayerId/{dirLayerId}", method = RequestMethod.GET)
    public ContentResultForm findLayerByDirLayerId(@PathVariable("dirLayerId") String dirLayerId) throws Exception {
        return new ContentResultForm<AgLayer>(true, iAgDir.findLayerByDirLayerId(dirLayerId));
    }

    @ApiOperation(value = "??????dirLayerId?????????????????????????????????id", notes = "??????dirLayerId?????????????????????????????????id??????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dirLayerId", value = "??????????????????ID", required = true),
            @ApiImplicitParam(name = "userId", value = "??????ID", required = true)
    })
    @RequestMapping(value = "/findBindAuthorizedVector", method = RequestMethod.GET)
    public ContentResultForm findBindAuthorizedVector(String dirLayerId, String userId) throws Exception {

        return new ContentResultForm(true, iAgDir.findBindAuthorizedVector(dirLayerId, userId));
    }

    /**
     * ?????????id?????????????????????????????????
     *
     * @param userId
     * @param featureType
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "?????????id?????????????????????????????????", notes = "?????????id???????????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "??????ID", required = true),
            @ApiImplicitParam(name = "featureType", value = "????????????", required = true)
    })
    @RequestMapping(value = "/layersByUserIdAndFeatureType", method = RequestMethod.GET)
    public ContentResultForm layersByUserIdAndFeatureType(String userId, String featureType) throws Exception {
        List<LayerForm> list = iAgDir.findLayerByUserIdAndFeatureType(userId, featureType);
        JsonMapper mapper = new JsonMapper(JsonInclude.Include.ALWAYS);//?????????????????????
        return new ContentResultForm(true, list);
    }

    /**
     * ????????????????????????
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/findVectorLayer")
    public ContentResultForm findVectorLayer() throws Exception {
        String result = iAgDir.findVectorLayer();
        return new ContentResultForm<String>(true, result);
    }

    @RequestMapping("/findRenderLayer")
    public ContentResultForm findRenderLayer() throws Exception {
        String result = iAgDir.findRenderLayer();
        return new ContentResultForm<String>(true, result);
    }

    /**
     * ???????????????????????????
     *
     * @param nameCn
     * @return
     */
    @RequestMapping("/findLayerByNameCn")
    public ContentResultForm findLayerByNameCn(String nameCn) throws Exception {
        AgLayer agLayer = iAgDir.findLayerByNameCn(nameCn);
        return new ContentResultForm<AgLayer>(true, agLayer);
    }

    @RequestMapping("/findLayerByXpathAndLoginName")
    public ContentResultForm findLayerByXpathAndLoginName(String xpath, String loginName) throws Exception {
        AgUser agUser = iAgUser.findUserByName(loginName);
        String userId = null;
        if (agUser != null) {
            userId = agUser.getId();
        }
        List<AgLayer> list = iAgDir.findLayerByXpathAndUserId(xpath, userId);
        return new ContentResultForm<List<AgLayer>>(true, list);
    }

    @RequestMapping("/findLayerByDirNameAndLayerName")
    public ContentResultForm findLayerByDirNameAndLayerName(String dirName, String layerName, String loginName) throws Exception {
        AgUser agUser = iAgUser.findUserByName(loginName);
        String userId = null;
        if (agUser != null) {
            userId = agUser.getId();
        }
        List<AgLayer> list = iAgDir.findLayerByDirNameAndLayerName(dirName, layerName, userId);
        return new ContentResultForm<List<AgLayer>>(true, list);
    }

    /**
     * ????????????--?????????????????????
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
        PageInfo<AgLayer> pageInfo = iAgDir.searchLayer(type, keyWord, year, page);
        EasyuiPageInfo<AgLayer> result = PageHelper.toEasyuiPageInfo(pageInfo);
        return new ContentResultForm<EasyuiPageInfo<AgLayer>>(true, result);
    }

    @RequestMapping("/getToken")
    public ContentResultForm getToken(String id) throws Exception {
        String token = iAgDir.getToken(id);
        return new ContentResultForm<String>(true, token);
    }

    @RequestMapping("/getServerLink")
    public ContentResultForm getServerLink() throws Exception {
        List<AgServer> serverLink = iAgDir.getServerLink();
        return new ContentResultForm<List<AgServer>>(true, serverLink);
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param request
     * @param userId
     * @return
     */
    @RequestMapping("/getSpecialLayer/{userId}")
    public ContentResultForm getSpecialLayerByRole(HttpServletRequest request, @PathVariable("userId") String userId) {
        ContentResultForm<List<ReflectMap>> result = new ContentResultForm<List<ReflectMap>>(false);
        try {
            List<ReflectMap> list_result = new ArrayList<ReflectMap>();
            List<AgDir> agDirs = iAgDir.findSecondDir();
            for (AgDir agDir : agDirs) {
                // ????????????????????????????????????
                List<AgLayer> AgLayers = iAgDir.findLayerByUserIdAndXpath(userId, agDir.getXpath());
                AgMapParam agMapParam = iAgParam.findMapParamById(agDir.getMapParamId());
                String reference = agMapParam.getReference();
                //??????????????? ??????WGS84
                String transReference = request.getParameter("transReference") == null ? "WGS84" : request.getParameter("transReference");
                String name = reference + "???" + transReference;
                //AgDic agDic = iAgDic.findValueByCoorName(name);

             /*   //??????agcloud??????
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
                rfmap.put("origin", agMapParam.getOrigin()); //????????????
                rfmap.put("extent", agMapParam.getExtent());        //????????????
                rfmap.put("center", agMapParam.getCenter());        //?????????
                rfmap.put("scales", agMapParam.getScales());        //?????????
                rfmap.put("zoom", agMapParam.getZoom());        //???????????????
                rfmap.put("reference", reference); // ????????????
                if (agDic.getValue() != null) {
                    JSONObject paramJson = JSONObject.fromObject(agDic.getValue());
                    rfmap.put("sevenParam", paramJson.get("sevenParam") == null ? "" : paramJson.getString("sevenParam")); // ?????????
                    rfmap.put("fourParam", paramJson.get("fourParam") == null ? "" : paramJson.getString("fourParam")); // ?????????
                    rfmap.put("transSevenParam", paramJson.get("transSevenParam") == null ? "" : paramJson.getString("transSevenParam")); // 84???7??????
                    rfmap.put("transFourParam", paramJson.get("transFourParam") == null ? "" : paramJson.getString("transFourParam")); // 84???4??????
                    rfmap.put("ellipsoidParam", paramJson.get("ellipsoidParam") == null ? "" : paramJson.getString("ellipsoidParam")); // ????????????
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
        return new ResultForm(true, "???????????????");
    }


    @RequestMapping("/findTableFields")
    public ContentResultForm findTableFields(String dataSourceId, String layerTable, String layerId) throws Exception {
        List<Map<String, String>> titleAlias = iAgDir.findTableFields(dataSourceId, layerTable, layerId);
        return new ContentResultForm<List<Map<String, String>>>(true, titleAlias);
    }

    @RequestMapping("/findTableFieldsByDirLayerId")
    public ContentResultForm findTableFieldsByDirLayerId(String dirLayerId) throws Exception {
        List<AgLayerFieldConf> tableFields = iAgDir.findTableFieldsByDirLayerId(dirLayerId);
        return new ContentResultForm<List<AgLayerFieldConf>>(true, tableFields);
    }

    /**
     * ??????????????????id??????????????????
     *
     * @param userId
     * @return
     */
    @RequestMapping("/findLayerByUserId")
    public ContentResultForm findLayerByUserId(String userId) throws Exception {
        List<AgLayer> agLayers = iAgDir.getPageByUserId(userId);
        return new ContentResultForm<List<AgLayer>>(true, agLayers);
    }

    /**
     * ???????????????????????? 2017-12-07
     *
     * @param request
     * @param funcName
     * @return
     */
    private JSONObject getLogInfo(HttpServletRequest request, String funcName) {
        JSONObject jsonObject = new JSONObject();
        //String loginName = SecurityContext.getCurrentUser().getLoginName();
        String loginName = LoginHelpClient.getLoginName(request);
        if ("anonymousUser".equals(loginName)) {
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

    /**
     * ?????????????????????
     *
     * @param agLayer
     * @return
     */
    @ApiOperation(value = "?????????????????????", notes = "???????????????????????????")
    @ApiImplicitParam(name = "agLayer", value = "????????????", dataType = "AgLayer")
    @RequestMapping(value = "/saveLayer", method = RequestMethod.POST)
    public ResultForm saveLayer(AgLayer agLayer, HttpServletRequest request) {
        //?????????????????????????????????
        if (!ProxyUtil.checkLayerProxyUrlAndUrl(agLayer)) {
            return new ResultForm(false, "??????????????????????????????????????????????????????");
        }
        ResultForm result = null;
        String funcName = null;
        try {
            if (Common.isCheckNull(agLayer.getId())) {
                result = addLayer(agLayer, request);
                funcName = "????????????";
            } else {
                result = updateLayer(agLayer, request);
                funcName = "????????????";
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = new ResultForm(false, "???????????????" + e.getMessage());
        }
        return result;
    }

    private ResultForm addLayer(AgLayer agLayer, HttpServletRequest request) throws Exception {
        //????????????
        AgLayer temp = iAgDir.findLayerByUrl(agLayer.getUrl(), agLayer.getLayerTable());
        if (temp == null) {
            if (!iAgDir.existSameNameLayer(agLayer)) {
                setLayer(agLayer, request);
                iAgDir.saveLayer(agLayer);
                String dirId = agLayer.getDirId();
                String layerId = agLayer.getId();
                if (StringUtils.isNotEmpty(dirId) && StringUtils.isNotEmpty(layerId)) {
                    this.saveDirLayer(dirId, layerId, request);
                }
                return new ResultForm(true, "??????????????????");
            } else {
                return new ResultForm(false, "??????????????????????????????");
            }
        } else {
            return new ResultForm(false, "???????????????");
        }
    }

    private ResultForm updateLayer(AgLayer agLayer, HttpServletRequest request) throws Exception {
        AgLayer temp = iAgDir.findLayerByUrl(agLayer.getUrl(), agLayer.getLayerTable());
        if (temp == null || agLayer.getId().equals(temp.getId())) {
            if (!iAgDir.existSameNameLayer(agLayer)) {
                setLayer(agLayer, request);
                iAgDir.updateLayer(agLayer);
                return new ResultForm(true, "??????????????????");
            } else {
                return new ResultForm(false, "??????????????????????????????");
            }
        } else {
            return new ResultForm(false, "???????????????");
        }
    }

    private void setLayer(AgLayer agLayer, HttpServletRequest request) throws Exception {
        String loginName = LoginHelpClient.getLoginName(request);
        //AgUser agUser=IAgUser
        AgUser agUser = iAgUser.findUserByName(loginName);
        if (agUser != null) {
            agLayer.setCreator(agUser.getUserName());
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//??????????????????
        if (org.apache.commons.lang3.StringUtils.isBlank(agLayer.getId())) {
            agLayer.setCreateDate(df.format(new Date()).toString());
        }
        JSONObject data = JSONObject.fromObject(agLayer.getData());
        data.put("layerTypeCn", agLayer.getLayerTypeCn());
        data.put("owner", loginName);
        String pictureName = saveLayerPicture(request);
        data.put("picture", pictureName);
        agLayer.setData(data.toString());
        agLayer.setLayerTable(agLayer.getLayerTables());
        agLayer.setFeatureType(agLayer.getFeatureTypes());
        /**
         * date:2020.7.22
         * des??? ??????????????????url??????, ???????????????????????????????????????
         * "120010" : 3dtiles ?????????????????????
         */
        String proxyUrl = "";
        if (agLayer.getId() != null && "120010".equals(agLayer.getLayerType())) {
            /**
             *
             * ???????????????
             * isProxy=0, ????????????
             * isProxy=1, proxy????????????
             * isProxy=2, nginx Version??????
             * @Author: qinyg
             * @Date: 2020/08/12
             * @Description:
             */
            if("1".equals(agLayer.getIsProxy())){
                proxyUrl = getProxyUrl2(agLayer, agUser);
            }else if("2".equals(agLayer.getIsProxy())){
                //????????????????????????????????????????????????????????????
                proxyUrl = agLayer.getProxyUrl();
            }
        } else {
            //nginx??????????????????3dtiles
            if("120010".equals(agLayer.getLayerType()) && "2".equals(agLayer.getIsProxy())){
                //????????????????????????????????????????????????????????????
                proxyUrl = agLayer.getProxyUrl();
            }else{
                //??????????????????
                proxyUrl = getProxyUrl(agLayer.getUrl());
            }
        }
        agLayer.setProxyUrl(proxyUrl);
    }

    public String saveLayerPicture(HttpServletRequest request) throws Exception {
        String pictrueName = null;
        String contentType = request.getContentType();  //??????Content-Type
        if ((contentType != null) && (contentType.toLowerCase().startsWith("multipart/"))) {
            //???????????????
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            //???????????????????????????
            for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
                MultipartFile file = entity.getValue();
                if (file.getSize() > 0) {
                    String filePath = UploadUtil.getUploadAbsolutePath() + AgDirImpl.getLayerPictureDir();
                    File layerPictureDir = new File(filePath);
                    if (!layerPictureDir.exists()) {
                        layerPictureDir.mkdir();
                    }
                    String fileName = file.getOriginalFilename();
                    String suffixName = fileName.substring(fileName.lastIndexOf("."));
                    fileName = new Date().getTime() + suffixName; //??????????????????
                    File temp = new File(filePath + fileName);
                    file.transferTo(temp);
                    pictrueName = fileName;
                }
            }
        }
        return pictrueName;
    }

    private String getProxyUrl(String layerUrl) throws Exception {
        String proxUrl = null;
        if (StringUtils.isNotEmpty(layerUrl)) {
            Pattern pattern = Pattern.compile("/");
            Matcher findMatcher = pattern.matcher(layerUrl);
            int number = 0;
            while (findMatcher.find()) {
                number++;
                if (number == 3) {//??????/??????3??????????????????
                    break;
                }
            }
            int i = findMatcher.start();// ???/??? ???3??????????????????
            String substring = layerUrl.substring(i + 1, layerUrl.length());
            proxUrl = ProxyUtil.getProxyPreUrl() + substring;
        }
        return proxUrl;
    }

    private String getProxyUrl2(AgLayer agLayer, AgUser agUser) throws Exception {
        // ????????????id
        String userId = agUser.getId();
        // ????????????url?????????http://ip:port/proxy/rest/services/userid/layerID/layerType
        String proxUrl = ProxyUtil.getProxyPreUrl() + "rest/services/" + userId + "/" + agLayer.getId() + "/" + agLayer.getLayerType();
        return proxUrl;
    }
}
