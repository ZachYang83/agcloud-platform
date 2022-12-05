package com.augurit.agcloud.agcom.agsupport.sc.app.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.mapper.*;
import com.augurit.agcloud.agcom.agsupport.sc.app.controller.form.ApiLayerForm;
import com.augurit.agcloud.agcom.agsupport.sc.app.service.AppProjectService;
import com.augurit.agcloud.agcom.agsupport.sc.dir.controller.form.DirTree;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.DirTreeUtil;
import com.augurit.agcloud.agcom.agsupport.sc.field.service.IAgField;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.common.util.Common;
import com.common.util.ReflectBeans;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @Auther: zhangmingyang
 * @Date: 2018/9/30 09:36
 * @Description:
 */
@Service
public class AppProjectServiceImpl implements AppProjectService {
    @Autowired
    private AgProjectdirMapper agProjectdirMapper;
    @Autowired
    private IAgUser iAgUser;
    @Autowired
    private AgUserProjectMapper agUserProjectMapper;
    @Autowired
    private AgParamMapper agParamMapper;
    @Autowired
    private AgLayerMapper agLayerMapper;
    @Autowired
    private AgMetadataMapper agMetadataMapper;
    @Autowired
    private AgDirMapper agDirMapper;
    @Autowired
    private com.augurit.agcloud.agcom.agsupport.sc.site.service.IAgSite IAgSite;
    @Autowired
    private IAgField iAgField;
    @Autowired
    private AgProjectdirLayerMapper agProjectdirLayerMapper;
    @Autowired
    private AgProjectMapper agProjectMapper;
    @Override
    public List<Map> getProjectList(String loginName) throws Exception{
        //过滤权限
        AgUser user = iAgUser.findUserByName(loginName);
        if (Common.isCheckNull(user)) {
            return null;
        }


        List<Map> result = getPrjectJson(user.getId());
    /*    for (Map map : prjectJson) {
            String owner = (String) map.get("owner");
            String projectId = map.get("id").toString();
            List<AgUserProject> agUserProjectList = agUserProjectMapper.findListByProjectId(projectId);
            // 专题与用户关联关系
            List userIds = new ArrayList();
            if (null != agUserProjectList && agUserProjectList.size() > 0) {
                for (AgUserProject roleProject : agUserProjectList) {
                    userIds.add(roleProject.getUserId());
                }
            }
            // 如果登陆用户有权限
            if (loginName.equals(owner) || (userIds != null && userIds.contains(user.getId()))) {
                result.add(map);
            }
        }*/
        for(Map map : result){
            Object mapParamId = map.get("mapParam");
            if (null != mapParamId){
                AgMapParam mapParam = agParamMapper.findById(mapParamId.toString());
                map.put("mapParam",mapParam);
            }
        }
        return result;
    }

    /**
     * 获取专题的json数据
     * @return
     */
    private List<Map> getPrjectJson(String userId) throws Exception{

        List<AgUserProject> listProject = agUserProjectMapper.findListByUserId(userId);
        List<AgProjectdir> projectdirs = new ArrayList<>();
        for (AgUserProject agUserProject : listProject){
            AgProjectdir project = agProjectdirMapper.findById(agUserProject.getProjectId());
            if (project != null){
                projectdirs.add(project);
            }

        }

        projectdirs.sort(Comparator.comparing(AgProjectdir::getProjectorder));
        List<Map> list = new ArrayList<>();
        for (AgProjectdir ag : projectdirs){
            Map map = new HashMap();
            //后期json树，没有则顺带初始化
            map.put("id", ag.getId());
            map.put("name", ag.getName());
            //map.put("owner", ag.getOwner());
            map.put("mapParam", ag.getMapParamId());
            list.add(map);
        }

        return list;
    }

    /**
     * 根据专题id获取专题目录图层树
     * @param projectId
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public List<DirTree> getTreeByProjectId(String projectId,String userId) throws Exception{
        // 1、根据专题id获取专题目录树
        AgProjectdir projectdir = agProjectdirMapper.findById(projectId);
        String agProjectdirXpath = projectdir.getXpath();
        List<AgProjectdir> agProjectdirs = agProjectdirMapper.findProjectByXpath(agProjectdirXpath);
        List<DirTree> trees = new ArrayList<DirTree>();
        for (AgProjectdir agProjectdir : agProjectdirs) {
            DirTree dirTree = new DirTree();
            dirTree.setId(agProjectdir.getId());
            dirTree.setPid(agProjectdir.getParentId());
            dirTree.setText(agProjectdir.getName());

            List<AgProjectdirLayer> projectdirLayerList = agProjectdirLayerMapper.findListByDirId(agProjectdir.getId());
            if (projectdirLayerList.size() > 0){
                // 2、获取专题目录树的授权的所有图层
                List<AgLayer> layers = new ArrayList<AgLayer>();
                for (AgProjectdirLayer agProjectdirLayer : projectdirLayerList){
                    //AgLayer agLayer = agLayerMapper.findByLayerId(agProjectdirLayer.getLayerId());
                    AgLayer agLayer=agProjectMapper.findLayerByLayerId(agProjectdirLayer.getLayerId(),"");
                    System.out.println(agLayer);
                    if (agLayer != null){
                        layers.add(agLayer);
                    }
                }
                if (null != layers && layers.size()>0){
                    this.setLayerExtend(layers);
                    List list = new ArrayList();
                    for (AgLayer agLayer : layers) {
                        ApiLayerForm layerForm = ReflectBeans.copy(agLayer, ApiLayerForm.class);
                        layerForm.setText(agLayer.getName());
                        layerForm.setType(agLayer.getLayerTypeCn());

                        AgUserLayer agUserLayer = iAgField.getLayerConfigByUserId(layerForm.getDirLayerId(), userId);
                        Map map = new HashMap();
                        if (null != agUserLayer){
                            if (StringUtils.isEmpty(agUserLayer.getIsBaseMap()))
                                layerForm.setIsBaseMap("0");
                            else
                                layerForm.setIsBaseMap(agUserLayer.getIsBaseMap());

                            if ("1".equals(agUserLayer.getIsShow())) {
                                map.put("checked", true);
                            } else {
                                map.put("checked", false);
                            }
                            map.put("editable", agUserLayer.getEditable());
                            map.put("queryable", agUserLayer.getQueryable());
                            map.put("queryCon", agUserLayer.getQueryCon());
                        }else {
                            layerForm.setIsBaseMap("0");
                            map.put("checked", false);
                        }
                        layerForm.setState(map);
                        AgMapParam agMapParam = agParamMapper.findById(agLayer.getParamId());//查询地图参数
                        if (agMapParam == null) agMapParam = new AgMapParam();
                        layerForm.setMapParam(agMapParam);
                        list.add(layerForm);
                        dirTree.setNodes(list);//添加子节点
                    }
                }
            }
            trees.add(dirTree);
        }

        trees = DirTreeUtil.getFatherNode(trees);
        DirTreeUtil.deleteTree(trees);
        for (int i = 0; i < trees.size(); i++) {
            this.setState(trees.get(i));
        }
        return trees;
    }

    /**
     * 递归设置目录勾选状态
     *
     * @param dirTree
     */
    public boolean setState(DirTree dirTree) {
        List nodes = dirTree.getNodes();
        if (nodes != null && nodes.size() > 0) {
            boolean flag = true;
            for (int i = 0; i < nodes.size(); i++) {
                if (nodes.get(i) instanceof DirTree) {
                    if (!setState((DirTree) nodes.get(i))) flag = false;
                } else {
                    ApiLayerForm layerForm = (ApiLayerForm) nodes.get(i);
                    if (layerForm.getState().containsKey("checked")) {
                        if (!(boolean) layerForm.getState().get("checked")) flag = false;
                    } else {
                        flag = false;
                    }
                }
            }
            Map map = new HashMap();
            if (flag) {
                map.put("checked", true);
                dirTree.setState(map);
            } else {
                map.put("checked", false);
                dirTree.setState(map);
            }
            return flag;
        }
        return false;
    }

    public void setLayerExtend(List<AgLayer> list) throws Exception {
        for (AgLayer agLayer : list) {
            this.fillLayerExtend(agLayer);
        }
    }

    private void fillLayerExtend(AgLayer agLayer) throws Exception {
        if (agLayer == null) return;
        JSONObject jsonObject = null;
        String extendData = agLayer.getData();
        if (!Common.isCheckNull(extendData)) {
            jsonObject = JSONObject.fromObject(extendData);
            Iterator keys = jsonObject.keys();
            Field[] declaredFields = agLayer.getClass().getDeclaredFields();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                for (int i = 0; i < declaredFields.length; i++) {
                    if (declaredFields[i].getName().equals(key)) {
                        declaredFields[i].setAccessible(true);
                        declaredFields[i].set(agLayer, jsonObject.get(key).toString());
                        break;
                    }
                }
            }
        }
        //将元数据和图层的扩展字段data中
        String metadataId = agLayer.getMetadataId();
        if (!Common.isCheckNull(metadataId)) {
            AgMetadata am = agMetadataMapper.getAgMetadataById(metadataId);
            if (jsonObject != null) {
                JSONObject temp = JSONObject.fromObject(am);
                if (temp != null && temp.size() > 0) {
                    temp.remove("id");//防止覆盖主表id
                    jsonObject.putAll(temp);
                }
            } else {
                jsonObject = JSONObject.fromObject(am);
            }
            agLayer.setData(jsonObject.toString());
        }
    }

    /**
     * 获取当前用户有权限的图层目录树
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public List<DirTree> getLayersDirTree(String userId) throws Exception {
        if (StringUtils.isBlank(userId)){
            return null;
        }
        List<AgUser> userList = new ArrayList<AgUser>();
        List<AgDir> list_dir = new ArrayList<AgDir>();
        list_dir = agDirMapper.findByUserId(userId);

        StringBuffer sb = new StringBuffer();
        for (AgDir agDir : list_dir) {
            if (StringUtils.isNotEmpty(agDir.getDirSeq())) {
                sb.append(agDir.getDirSeq()).append(",");
            }
        }
        String[] dirIds = sb.toString().split(",");
        List<AgDir> dirs = agDirMapper.findByIds(dirIds);

        List<DirTree> trees = new ArrayList<DirTree>();
        for (AgDir agDir : dirs) {
            DirTree dirTree = new DirTree();
            dirTree.setId(agDir.getId());
            dirTree.setPid(agDir.getParentId());
            dirTree.setText(agDir.getName());
            trees.add(dirTree);
        }
        List<AgLayer> layers = new ArrayList<AgLayer>();
        layers = agLayerMapper.findListByUserId(userId);
        this.setLayerExtend(layers);
        for (DirTree dirTree : trees) {
            for (AgLayer agLayer : layers) {
                if (dirTree.getId().equals(agLayer.getDirId())) {
                    ApiLayerForm apiLayerForm = ReflectBeans.copy(agLayer, ApiLayerForm.class);
                    apiLayerForm.setText(agLayer.getName());
                    //layerForm.setType(agLayer.getLayerTypeCn());
                    AgUserLayer agUserLayer = iAgField.getLayerConfigByUserId(apiLayerForm.getDirLayerId(), userId);
                    if (StringUtils.isEmpty(agUserLayer.getIsBaseMap()))
                        apiLayerForm.setIsBaseMap("0");
                    else
                        apiLayerForm.setIsBaseMap(agUserLayer.getIsBaseMap());
                    Map map = new HashMap();
                    if ("1".equals(agUserLayer.getIsShow())) {
                        map.put("checked", true);
                    } else {
                        map.put("checked", false);
                    }
                    //2017-12-12将后面添加的字段 增加到状态字段 state 中，图层范围在图层配置中获取
                    map.put("editable", agUserLayer.getEditable());
                    map.put("queryable", agUserLayer.getQueryable());
                    map.put("queryCon", agUserLayer.getQueryCon());
                    apiLayerForm.setState(map);
                    AgMapParam agMapParam = agParamMapper.findById(agLayer.getParamId());//查询地图参数
                    if (agMapParam == null) agMapParam = new AgMapParam();
                    apiLayerForm.setMapParam(agMapParam);
                    List list = dirTree.getNodes();
                    if (list == null) list = new ArrayList();
                    list.add(apiLayerForm);
                    dirTree.setNodes(list);//添加子节点
                }
            }
        }

        trees = DirTreeUtil.getFatherNode(trees);
        for (int i = 0; i < trees.size(); i++) {
            this.setState(trees.get(i));
        }
        return trees;
    }
}
