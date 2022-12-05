package com.augurit.agcloud.agcom.agsupport.sc.project.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.util.ProxyUtil;
import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.mapper.*;
import com.augurit.agcloud.agcom.agsupport.sc.dir.controller.form.LayerForm;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.Tree;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.TreeUtil;
import com.augurit.agcloud.agcom.agsupport.sc.project.service.IAgProject;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.common.util.Common;
import com.common.util.Md5Utils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chendingxing on 2017-12-22.
 */

@Service
public class AgProjectImpl implements IAgProject {
    @Autowired
    private IAgDir iAgDir;
    @Autowired
    private AgDirLayerMapper agDirLayerMapper;

    @Autowired
    private AgParamMapper agParamMapper;

    @Autowired
    private AgLayerMapper agLayerMapper;

    @Autowired
    private AgDirMapper agDirMapper;
    @Autowired
    private AgUserLayerMapper agUserLayerMapper;

    @Autowired
    private IAgUser iAgUser;

    List baselayers = new ArrayList();

    @Value("${agcloud.inf.load}")
    private Boolean agcloudInfLoad;

    @Value("${layerTreeFilter}")
    private Boolean layerTreeFilter;
    @Override
    @Cacheable(value = "user#project_info", key = "#loginName")
    public List<Map> projectList(String loginName) throws Exception {
        //查询全部专题
        List<Map> treeJsonList = getTreeJsonList();
        //获取当前用户
        AgUser user = iAgUser.findUserByName(loginName);
        if (Common.isCheckNull(user)) {
            return null;
        }
        List<Map> result = new ArrayList<>();
/*        List<AgRole> roles = null;
        if (!agcloudInfLoad.booleanValue()) {
            roles = agRoleMapper.findListByUser(new AgRole(), user.getId());
        } else {
            Map<String, String> param = new HashMap<>();
            param.put("userId", user.getId());
            HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/opus/ac/getRolesByUserId.do", param);
            JSONArray jsonArray = JSONArray.fromObject(httpRespons.getContent());
            List<Map> listMap = JsonUtils.toList(jsonArray.toString(), HashMap.class);
            roles = new AgRoleConverter().convertToList(null, listMap);
        }*/

        for (Map map : treeJsonList) {
            String projectId = map.get("id").toString();
            String xpath = map.get("xpath")==null ? "":map.get("xpath").toString();
            // 判断当前用户是否有当前专题的权限：只要用户有专题或其子目录的图层权限就代表用户有该专题的权限
            Boolean isAuthorizedToProject = agDirMapper.isAuthorizedToDir(user.getId(),xpath);
            // 如果登陆用户有权限
            if (isAuthorizedToProject) {
                result.add(map);
            }

        }
        return result;
    }



    @Override
    public String getProject(String projectPath, String projectName) throws Exception {
        List<AgDir> list_dir = agDirMapper.findAll();
        List<Tree> list = new ArrayList<Tree>();
        for (AgDir agDir : list_dir) {
            Tree tree = new Tree();
            tree.setId(agDir.getId());
            tree.setText(agDir.getName());
            tree.setPid(agDir.getParentId());
            tree.setXpath(agDir.getXpath());

            Map map = new HashMap<String, Object>();
            map.put("mapParamId", agDir.getMapParamId());
            map.put("projectOrder", agDir.getOrderNm());
            tree.setAttributes(map);

            list.add(tree);
        }
        List<Tree> trees = null;
        String id = "";
        List<AgDir> listProject = agDirMapper.findSecond();
        for (AgDir fn : listProject) {
            if (fn.getName().equals(projectName)) {
                id = fn.getId();
            }
        }
//        if (id.trim().equals("all")) {
        trees = TreeUtil.getFatherNode(id, list);

//    } else {  trees = TreeUtil.getChildrenNode(id, list);
//        }
        return com.augurit.agcloud.framework.util.JsonUtils.toJson(trees);
        // return JsonTreeUtil.getProject(projectPath, projectName);
    }

    @Override
    public ResultForm addProject(String projectName, String mapParamId) throws Exception {

        try {
            AgDir agDir = new AgDir();
            agDir.setName(projectName);
            agDir.setId(UUID.randomUUID().toString());
            agDir.setMapParamId(mapParamId);
            AgDir topDir = agDirMapper.findTopDir();
            String pid = topDir.getId();
            agDir.setParentId(pid);
            if (!Common.isCheckNull(pid)) {
                AgDir parentDir = agDirMapper.findById(pid);
                List<AgDir> AgDirs=agDirMapper.findByXpath(parentDir.getXpath());
                for (AgDir temp : AgDirs) {
                    //agHistChangeService.logAgHistAppWithOrgDelete(temp);
                    if(null != temp.getName() && temp.getName().equals(agDir.getName())){
                        String errorInfo = "专题已存在";

                        return new ResultForm(false,errorInfo);
                    }
                }
                agDir.setXpath(parentDir.getXpath() + "/" + agDir.getName());
                agDir.setParentId(pid);
                if (org.apache.commons.lang.StringUtils.isNotEmpty(parentDir.getDirSeq())) {
                    agDir.setDirSeq(agDir.getId() + "," + parentDir.getDirSeq());
                } else {
                    agDir.setDirSeq(agDir.getId());
                }
            } else {
                agDir.setXpath("/" + agDir.getName());
                agDir.setDirSeq(agDir.getId());
            }

            String maxOrder = agDirMapper.getOrder(pid);
            if (StringUtils.isEmpty(maxOrder)) {
                maxOrder = "0";
            }
            int orderNm = Integer.parseInt(maxOrder);
            agDir.setOrderNm(++orderNm);
            agDirMapper.save(agDir);
            return new ResultForm(true, "保存成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false,e.getMessage());
        }
    }

    @Override
    @Caching(evict={@CacheEvict(value = "user#project_layer_tree",allEntries = true),
                    @CacheEvict(value = "user#project_info",allEntries = true)})
    public ResultForm updateProject(String projectId, String projectName, String mapParamId) throws Exception {
        try {
            AgDir agDir = iAgDir.findDirById(projectId);
            if(agDir.getName().equals(projectName)&&agDir.getMapParamId()!=null&&agDir.getMapParamId().equals(mapParamId)){
                return  new ResultForm(true,"保存成功");
            }
            agDir.setMapParamId(mapParamId);
            agDir.setName(projectName);
            String xpath = "";
            String oldXpath = agDir.getXpath();
            if (projectName != null) {

                xpath = oldXpath.substring(0, oldXpath.lastIndexOf("/") + 1) + projectName;
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
                        agDirMapper.updateBatch(list);
                    }
                }
            }
            return new ResultForm(true,"保存成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false,e.getMessage());
        }

    }

    @Override
    @CacheEvict(value = "user#project_layer_tree",allEntries = true)
    public String deleteProject(String projectPath, String projectName) throws Exception {
        return null;
        /*List<AgProjectdir> list_dir = agProjectdirMapper.getProjectName();
        for (AgProjectdir fn : list_dir) {
            if (fn.getName().equals(projectName)) {
                agProjectdirMapper.deleteByXpath(fn.getXpath());
                agProjectdirMapper.delete(fn.getId());
            }
        }
        return null;*/
    }



    @Override
    public String deleteProjectDir(String projectPath, String projectName, String id) {
        return null;
    }

    @Override
    @CacheEvict(value = "user#project_layer_tree",allEntries = true)
    public String addDir(String projectPath, String projectName, String id, String dirName) {
        return null;
        /*AgProjectdir agProjectdir = new AgProjectdir();
        try {

            agProjectdir.setId(UUID.randomUUID().toString());
            agProjectdir.setName(dirName);
            if (!Common.isCheckNull(id)) {
                AgProjectdir patrentdir = agProjectdirMapper.findById(id);
                List<AgProjectdir> Projectdirs = agProjectdirMapper.findByXpath(patrentdir.getXpath());
                for (AgProjectdir temp : Projectdirs) {
                    if (temp.getName().equals(dirName)) {
                        return "目录名重复";
                    }
                }
                agProjectdir.setXpath(patrentdir.getXpath() + "/" + dirName);
                agProjectdir.setParentId(id);
                if (StringUtils.isNotEmpty(patrentdir.getDirSeq())) {
                    agProjectdir.setDirSeq(agProjectdir.getId() + "," + patrentdir.getDirSeq());
                } else {
                    agProjectdir.setDirSeq(agProjectdir.getId());
                }
            }
            Integer maxOrder = agProjectdirMapper.getOrder(id);
            int orderNm = Common.isCheckNull(maxOrder)?0:maxOrder;
            agProjectdir.setOrderNm(++orderNm);

            agProjectdirMapper.save(agProjectdir);
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
        return agProjectdir.getId();*/
    }

    @Override
    @CacheEvict(value = "user#project_layer_tree",allEntries = true)
    public String updateDir(String projectPath, String projectName, String id, String dirName) throws Exception {
        return null;
        /*AgProjectdir dirs = agProjectdirMapper.findById(id);
        if (!dirs.getName().equals(dirName)) {
            String oldXpath = dirs.getXpath();
            String xpath = oldXpath.substring(0, oldXpath.lastIndexOf("/") + 1) + dirName;
            List<AgProjectdir> list = agProjectdirMapper.findByXpath(oldXpath);
            for (AgProjectdir dir : list) {
                dir.setXpath(dir.getXpath().replaceFirst(oldXpath, xpath));
                agProjectdirMapper.update(dir);
            }
            dirs.setName(dirName);
            dirs.setXpath(xpath);
            agProjectdirMapper.update(dirs);
        }
        return null;*/
    }

    @Override
    @CacheEvict(value = "user#project_layer_tree",allEntries = true)
    public String deleteDir(String projectPath, String projectName, String id) throws Exception {
        return null;
        /*AgProjectdir dirs = agProjectdirMapper.findById(id);
        agProjectdirMapper.deleteByXpath(dirs.getXpath());
        agProjectdirMapper.delete(id);
        return null;*/
    }

    @Override
    @CacheEvict(value = "user#project_layer_tree",allEntries = true)
    public String updateDirBatch(String dirIds) throws Exception {
        return  null;
        /*String ids[] = null;
        if (StringUtils.isNotEmpty(dirIds)) {
            ids = dirIds.split(",");
        }
        // List<AgProjectdir> list = new ArrayList<AgProjectdir>();
        if (ids != null && ids.length > 0) {
            int i = 1;
            for (String id : ids) {
                AgProjectdir agProjectdir = agProjectdirMapper.findById(id);
                agProjectdir.setOrderNm(i++);
                agProjectdirMapper.update(agProjectdir);

            }
        }
        return null;*/
    }

    @Override
    @CacheEvict(value = "user#project_layer_tree",allEntries = true)
    public String addLayer(String projectPath, String projectName, String id, String ids) throws Exception {
        return null;
        /*String layerids[] = ids.split(",");
        if (layerids != null && layerids.length > 0) {
            for (String layerId : layerids) {
                AgProjectdirLayer projectlayer = agProjectdirLayerMapper.findByDirIdAndLayerId(id, layerId);
                if (projectlayer == null) {
                    projectlayer = new AgProjectdirLayer();
                    projectlayer.setId(UUID.randomUUID().toString());
                    projectlayer.setProjectdirId(id);
                    projectlayer.setLayerId(layerId);
                    String maxOrder = agProjectdirLayerMapper.getMaxOrder();
                    if (StringUtils.isEmpty(maxOrder)) {
                        maxOrder = "0";
                    }
                    int orderNm = Integer.parseInt(maxOrder);
                    projectlayer.setOrderNm(++orderNm);
                    agProjectdirLayerMapper.save(projectlayer);
                }
            }

        }
//        JsonTreeUtil.addProjectIds(projectPath, projectName, id, ids, "layerIds");
        return null;*/
    }

    @Override
    @CacheEvict(value = "user#project_layer_tree",allEntries = true)
    public boolean removeLayer(String projectPath, String projectName, String id, String ids) throws Exception {
        return false;
        /*boolean result = false;
        if(ids != null) {
            StringBuilder sb = new StringBuilder();
            String layerIds[] = ids.split(",");
            if (layerIds != null && layerIds.length > 0) {
                agProjectdirLayerMapper.deleteByDirIdAndLayerIds(id,layerIds);
                result = true;
            }
        }
        //JsonTreeUtil.removeProjectIds(projectPath, projectName, id, ids, "layerIds");
        return result;*/
    }

    @Override
    public PageInfo<AgLayer> getProjectDirLayer(String projectPath, String projectName, String id, String layerName,Page page) throws Exception {
        return  null;
       /* PageHelper.startPage(page);
        List<AgLayer> layers = agLayerMapper.findListByProjectDirId(id,layerName);
        return new PageInfo<AgLayer>(layers);*/
        /*List<AgLayer> layers = new ArrayList<>();
        List ids = new ArrayList();
        if (agProjectdirLayer != null && agProjectdirLayer.size() > 0) {
            for (AgProjectdirLayer dir : agProjectdirLayer) {
                ids.add(dir.getLayerId());
                AgLayer layer = agProjectMapper.findLayerByLayerId(dir.getLayerId(), layerName);
                if (layer != null) {
                    layers.add(layer);
                }
            }
            return  new PageInfo<AgLayer>(layers);

        }
        return null;*/

    }

    @Override
    public PageInfo<AgUser> getProjectUser(String projectPath, String projectName, String userName, Page page) throws Exception {
        return  null;
        /*List<AgProjectdir> agProjectdirs = agProjectdirMapper.getProjectName();
        String projectId = null;
        List ids = new ArrayList();
        for (AgProjectdir fn : agProjectdirs) {
            if (fn.getName().equals(projectName)) {
                projectId = fn.getId();
            }
        }
        List<AgUserProject> agRoleProjects = agUserProjectMapper.findListByProjectId(projectId);
        //ids = (List) roleids;
        if (agRoleProjects != null && agRoleProjects.size() > 0) {
            for (AgUserProject dir : agRoleProjects) {
                ids.add(dir.getUserId());
            }

        }
        PageInfo<AgUser> userPage = null;
        List<AgUser> list = new ArrayList<>();
        //List ids = JsonTreeUtil.getProjectIds(projectPath, projectName, "root", "roleIds");
        if (!Common.isCheckNull(ids) && ids.size() > 0) {
            List<AgRole> roles = null;
            if (!agcloudInfLoad.booleanValue()) {
                roles = agProjectMapper.findRoleByIds(ids, userName);

            } else {
                String userIds = "";
                for (Object o : ids) {
                    userIds += o.toString() + ",";
                }
                Map<String, String> param = new HashMap<>();
                param.put("userIds", userIds);
                param.put("userName", userName);
                param.put("page", String.valueOf(page.getPageNum()));
                param.put("rows", String.valueOf(page.getPageSize()));
                HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/opus/om/listUserByUserIds.do", param);
                JSONObject jsonObject = JSONObject.fromObject(httpRespons.getContent());
                List<Map> listMap = JsonUtils.toList(jsonObject.get("rows").toString(), HashMap.class);
                list = new AgUserConverter().convertToList(null, listMap);
                userPage = new PageInfo<>();
                userPage.setList(list);
                userPage.setTotal(Long.valueOf(jsonObject.get("total").toString()));
            }
            return userPage;
        } else {
            return null;
        }*/
    }

    /**
     * @param projectPath
     * @param projectName
     * @param ids
     * @param isAdd       true表示 增加授权用户，false表示 移除用户
     * @return
     * @throws Exception
     */
    @Override
    @CacheEvict(value = "user#project_layer_tree",allEntries = true)
    public String projectAuthor(String projectPath, String projectName, String ids, Boolean isAdd) throws Exception {
        return  null;
       /* String result = null;
        if (isAdd) {
            List<AgProjectdir> list_dir = agProjectdirMapper.getProjectName();
            for (AgProjectdir dir : list_dir) {
                if (dir.getName().equals(projectName)) {
                    String[] id = ids.split(",");
                    if (id != null && id.length > 0) {
                        for (String userId : id) {
                            AgUserProject agUserProject = agUserProjectMapper.findByUserIdAndProjectId(userId, dir.getId());
                            if (agUserProject == null) {
                                AgUserProject agroleproject = new AgUserProject();
                                agroleproject.setId(UUID.randomUUID().toString());
                                agroleproject.setProjectId(dir.getId());
                                agroleproject.setUserId(userId);
                                agUserProjectMapper.save(agroleproject);
                                result="保存成功";
                            }

                        }

                    }
                }
            }
            //JsonTreeUtil.addProjectIds(projectPath, projectName, "root", ids, "roleIds");
        } else {
            List<AgProjectdir> list_dir = agProjectdirMapper.getProjectName();
            for (AgProjectdir dir : list_dir) {
                if (dir.getName().equals(projectName)) {
                    String[] id = ids.split(",");
                    if (id != null && id.length > 0) {
                        for (String userId : id) {
                            AgUserProject agUserProject = agUserProjectMapper.findByUserIdAndProjectId(userId, dir.getId());
                            if (agUserProject != null) {
                                agUserProjectMapper.delete(agUserProject.getId());
                                result= "删除成功";
                            }

                        }

                    }
                }
            }
            //JsonTreeUtil.removeProjectIds(projectPath, projectName, "root", ids, "roleIds");
        }
        return result;*/
    }

    @Override
    @Cacheable(value = "user#project_layer_tree", key = "#projectName+#userId")
    public List getProjectLayerTree(String projectPath, String projectName, AgUser agUser) throws Exception {
        AgUser user = agUser;
        if(user == null){
            return new ArrayList();
        }
        baselayers.clear();
        String preUrl = ProxyUtil.getProxyPreUrl();
        Map<String, Object> jsonTreeNodes = new HashMap<>();
//        JSONObject jsonTreeNode=new JSONObject();
        List<AgDir> allProject = agDirMapper.findSecond();//找到所有专题(不包含子节点）
        for (AgDir fn : allProject) {
            if (fn.getName().equals(projectName)) {
                List<AgDir> childrenOfProject = agDirMapper.findByXpath(fn.getXpath());//获取子节点(不包括专题本身)
                Map jsono = new HashMap();
                jsono.put("id", fn.getId());
                jsono.put("text", fn.getName());
                jsono.put("iconCls", "icon-folder");
                jsono.put("mapParamId", fn.getParentId());
                jsono.put("projectOrder", fn.getOrderNm());
                //jsono.put("owner", fn.getOwner());
                List layerIds = new ArrayList();
                List layers = new ArrayList();
                //agDirLayersForProject是Project直接相关的图层
                List<AgDirLayer> agDirLayersForProject = agDirLayerMapper.findByDirId(fn.getId());
                if (agDirLayersForProject != null && agDirLayersForProject.size() > 0) {
                    for (AgDirLayer agDirLayerForProject : agDirLayersForProject) {
                        List<AgUserLayer> userLayerList = agUserLayerMapper.findListByDirLayerId(agDirLayerForProject.getId());//找出指定目录图层的关系用户
                        for (AgUserLayer userLayer : userLayerList) {
                            //找出当前用户的权限目录图层
                            if (userLayer.getUserId().equals(user.getId())) {
                                AgLayer layerc = agLayerMapper.findByDirLayerId(agDirLayerForProject.getLayerId());
                                if (layerc != null) {
                                    LayerForm layerForm = LayerForm.createInstance(layerc);
                                    AgMapParam agMapParam = agParamMapper.findById(layerc.getParamId());
                                    layerForm.setMapParam(agMapParam);
                                    layerForm.setUserId(userLayer.getUserId()==null ? "": userLayer.getUserId());
                                    layerForm.setDirLayerId(userLayer.getDirLayerId()==null ? "": userLayer.getDirLayerId());
                                    layerForm.setOrder(agDirLayerForProject.getOrderNm());
                                    Map map = new HashMap();
                                    if ("1".equals(userLayer.getIsShow())) {
                                        map.put("checked", true);
                                    } else {
                                        map.put("checked", false);
                                    }
                                    //2017-12-12将后面添加的字段 增加到状态字段 state 中，图层范围在图层配置中获取
                                    map.put("editable", userLayer.getEditable()==null?"0":userLayer.getEditable());
                                    map.put("queryable", userLayer.getQueryable()==null?"0":userLayer.getQueryable());
                                    map.put("queryCon", userLayer.getQueryCon()==null?"0":userLayer.getQueryCon());
                                    map.put("isBaseMap", userLayer.getIsBaseMap()==null?"0":userLayer.getIsBaseMap());
                                    map.put("isShow", userLayer.getIsShow()==null?"0":userLayer.getIsShow());
                                    map.put("extent", userLayer.getExtent()==null ? "": userLayer.getExtent());
                                    //layerForm.setExtent(userLayer.getExtent());
                                    layerForm.setState(map);
                                    String proxyUrl = layerc.getProxyUrl();
                                    //2017-07-06将代理地址赋给url,减少前端判断,2017-11-02 将拼接userId 的 下划线去掉，并将userId使用MD5编码，在agproxy那边有对应编码集匹配
                                    if (!Common.isCheckNull(proxyUrl)) {
                                        //agLayer.setUrl(preUrl + proxyUrl + "/" + Md5Utils.encrypt16(userId));
                                        String url = layerc.getUrl();
                                        Pattern pattern = Pattern.compile("/");
                                        Matcher findMatcher = pattern.matcher(url);
                                        int number = 0;
                                        while (findMatcher.find()) {
                                            number++;
                                            if (number == 3) {//当“/”第3次出现时停止
                                                break;
                                            }
                                        }
                                        int i = findMatcher.start();// “/” 第3次出现的位置
                                        String substring = url.substring(i + 1, url.length());
                                        layerForm.setUrl(preUrl + substring);
                                        //这个Uuid是启用代理时，代理服务需要验证的标识，是必须的，不能删除
                                        layerForm.setUuid(Md5Utils.encrypt16(userLayer.getId()));
                                    }
                                    if (StringUtils.isEmpty(userLayer.getIsBaseMap()) || "0".equals(userLayer.getIsBaseMap())) {
                                        layerForm.setIsBaseMap("0");
                                        layers.add(layerForm);
                                    } else {
                                        layerForm.setIsBaseMap(userLayer.getIsBaseMap());
                                        baselayers.add(layerForm);


                                    }

                                }
                            }
                        }
                    }
                }
                jsono.put("layers", layers);
                List children = getchild(fn.getId(), user.getId());//根据专题和当前用户找出
                jsono.put("children", children);
                jsono.put("checked", "");
                jsono.put("disabled", false);
                jsono.put("open", "");
                jsono.put("state", "closed");
                jsonTreeNodes.put(fn.getId(), jsono);

            }
        }
        //如果没有权限的专题就返回NULL
        if (jsonTreeNodes.size() == 0) {
            return new JSONArray();
        }

        List tree = new ArrayList();
        if (jsonTreeNodes.size() > 0) {
            for (Map.Entry<String, Object> entry : jsonTreeNodes.entrySet()) {
                Map mapValue =(Map) entry.getValue();
                if (mapValue.get("text").equals(projectName)) {
                    tree.add(entry.getValue());
                }
            }
        }
        tree.add(baselayers);
        //String project = tree.toString();
        return tree;
    }

    public List getchild(String pid, String userId) throws Exception {
        String preUrl = ProxyUtil.getProxyPreUrl();
        List<AgDir> agChildrenDirs = agDirMapper.getChildrenByParentId(pid);//根据父Id查出所有子目录
        List jsons = new ArrayList();
        if (agChildrenDirs != null && agChildrenDirs.size() > 0) {
            for (AgDir agChildDir : agChildrenDirs) {
                Map item = new HashMap();
                item.put("id", agChildDir.getId());
                item.put("text", agChildDir.getName());
                item.put("iconCls", "icon-folder");
                item.put("pid", agChildDir.getParentId());
                item.put("dirOrder", agChildDir.getOrderNm());
                List layers = new ArrayList();
                List<AgDirLayer> agDirLayersForChild = agDirLayerMapper.findByDirId(agChildDir.getId());//
                if (agDirLayersForChild != null && agDirLayersForChild.size() > 0) {
                    for (AgDirLayer agDirLayerForChild : agDirLayersForChild) {
                        List<AgUserLayer> userLayerList = agUserLayerMapper.findListByDirLayerId(agDirLayerForChild.getId());
                        for (AgUserLayer userLayer : userLayerList) {
                            if (userLayer.getUserId().equals(userId)) {
                                AgLayer layerc = agLayerMapper.findByLayerId(agDirLayerForChild.getLayerId());
                                if (layerc != null) {
                                    LayerForm layerForm = LayerForm.createInstance(layerc);
                                    layerForm.setUserId(userLayer.getUserId()==null ? "": userLayer.getUserId());
                                    layerForm.setDirLayerId(userLayer.getDirLayerId()==null ? "": userLayer.getDirLayerId());
                                    Map map = new HashMap();
                                    if ("1".equals(userLayer.getIsShow())) {
                                        map.put("checked", true);
                                    } else {
                                        map.put("checked", false);
                                    }
                                    //2017-12-12将后面添加的字段 增加到状态字段 state 中，图层范围在图层配置中获取
                                    map.put("editable", userLayer.getEditable()==null?"0":userLayer.getEditable());
                                    map.put("queryable", userLayer.getQueryable()==null?"0":userLayer.getQueryable());
                                    map.put("queryCon", userLayer.getQueryCon()==null?"0":userLayer.getQueryCon());
                                    map.put("isBaseMap", userLayer.getIsBaseMap()==null?"0":userLayer.getIsBaseMap());
                                    map.put("isShow", userLayer.getIsShow()==null?"0":userLayer.getIsShow());
                                    map.put("extent", userLayer.getExtent()==null ? "": userLayer.getExtent());
                                    //layerForm.setExtent(userLayer.getExtent());
                                    layerForm.setState(map);
                                    String proxyUrl = layerc.getProxyUrl();
                                    //2017-07-06将代理地址赋给url,减少前端判断,2017-11-02 将拼接userId 的 下划线去掉，并将userId使用MD5编码，在agproxy那边有对应编码集匹配
                                    if ("1".equals(layerc.getIsProxy())) {
                                        //agLayer.setUrl(preUrl + proxyUrl + "/" + Md5Utils.encrypt16(userId));
                                        String url = layerc.getUrl();
                                        Pattern pattern = Pattern.compile("/");
                                        Matcher findMatcher = pattern.matcher(url);
                                        int number = 0;
                                        while (findMatcher.find()) {
                                            number++;
                                            if (number == 3) {//当“/”第3次出现时停止
                                                break;
                                            }
                                        }
                                        int i = findMatcher.start();// “/” 第3次出现的位置
                                        String substring = url.substring(i + 1, url.length());
                                        layerForm.setUrl(preUrl + substring);
                                        //这个Uuid是启用代理时，代理服务需要验证的标识，是必须的，不能删除
                                        layerForm.setUuid(Md5Utils.encrypt16(userLayer.getId()));
                                    }
                                    if (StringUtils.isEmpty(userLayer.getIsBaseMap()) || "0".equals(userLayer.getIsBaseMap())) {
                                        layerForm.setIsBaseMap("0");
                                        layers.add(layerForm);
                                    } else {
                                        layerForm.setIsBaseMap(userLayer.getIsBaseMap());
                                        baselayers.add(layerForm);
                                    }

                                }

                            }
                        }
//                          layerIds.add(agProjectdirLayer.getLayerId());
                    }
                }
                item.put("layers", layers);
                List children = getchild(agChildDir.getId(), userId);
                item.put("children", children);
                if (agDirMapper.isAuthorizedDirContainNotBaseLayer(userId,agChildDir.getXpath())) {
                    jsons.add(item);
                }else if(!layerTreeFilter){
                    //如果目录不包含非底图图层视为空目录，空目录根据layerTreeFilter配置决定是否返回前端展示
                    jsons.add(item);
                }

            }


        }

        return jsons;


    }



    private JSONObject createItem(AgLayer al, AgUserLayer config) {
        JSONObject item = new JSONObject();
        item.put("checked", "1".equals(config.getIsShow()) ? "true" : "false");
        item.put("editable", "1".equals(config.getEditable()) ? "true" : "false");
        item.put("queryable", "1".equals(config.getQueryable()) ? "true" : "false");
        item.put("child", "");
        item.put("dependId", "");
        item.put("disabled", false);
        item.put("id", al.getId());
        item.put("open", "");
        item.put("text", al.getNameCn());
        item.put("children", new JSONArray());
        item.put("userdata", "");
        return item;
    }


    private JSONObject createUserdata(AgLayer al, int order) {
        JSONObject userdata = new JSONObject();
        userdata.put("tile_url", al.getUrl());
        userdata.put("name", al.getNameCn());
        userdata.put("id", al.getId());
        userdata.put("layerId", al.getId());
        userdata.put("layerType", al.getLayerType());
        userdata.put("layerOrder", order);
        userdata.put("selectable", "1");
        userdata.put("multiScreen", "");
        userdata.put("serial_name", "SEQ_FOR_ARCSDE");
        userdata.put("layer_table", al.getLayerTable());
        userdata.put("isBaseMap", "");
        userdata.put("alpha", "1.0");
        userdata.put("featureType", "4");
        userdata.put("annoField", "");
        userdata.put("srs", "");
        userdata.put("tileSize", "");
        userdata.put("origin", "");
        userdata.put("suffix", "");
        userdata.put("defaultShow", "");
        userdata.put("layerReadType", "0");
        userdata.put("server_name", "");
        userdata.put("maxScale", "0");
        userdata.put("minScale", "0");
        return userdata;
    }

    private List<Map> getTreeJsonList() {
        List<Map> list = new ArrayList<>();
        try {
            List<AgDir> listProject = agDirMapper.findSecond();
            for (AgDir fn : listProject) {
                Map map = new HashMap();

                //后期json树，没有则顺带初始化
                map.put("id", fn.getId());
                map.put("parentId", fn.getParentId());
                map.put("name", fn.getName());
                map.put("value", fn.getName());
                //map.put("owner", fn.getOwner());
                //map.put("roleIds", fn.getRoleids());
                map.put("mapParamId", fn.getMapParamId());
                map.put("projectOrder", fn.getOrderNm());
                map.put("xpath", fn.getXpath());
                list.add(map);
            }
            //根据projectOrder排序,升序，即1在第一个
            Collections.sort(list, new Comparator<Map>() {
                @Override
                public int compare(Map o1, Map o2) {
                    return (int) o1.get("projectOrder") - (int) o2.get("projectOrder");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
