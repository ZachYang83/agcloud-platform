package com.augurit.agcloud.agcom.agsupport.sc.project.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.util.ProxyUtil;
import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.mapper.*;
import com.augurit.agcloud.agcom.agsupport.sc.dir.controller.form.LayerForm;
import com.augurit.agcloud.agcom.agsupport.sc.dir.service.IAgDir;
import com.augurit.agcloud.agcom.agsupport.sc.project.service.IAgProject;
import com.augurit.agcloud.agcom.agsupport.sc.user.service.IAgUser;
import com.common.util.Common;
import com.common.util.Md5Utils;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "user#project_layer_tree", key = "#projectName+#agUser.id")
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
//                List<AgDir> childrenOfProject = agDirMapper.findByXpath(fn.getXpath());//获取子节点(不包括专题本身)
                Map jsono = new HashMap();
                jsono.put("id", fn.getId());
                jsono.put("text", fn.getName());
                jsono.put("iconCls", "icon-folder");
                jsono.put("mapParamId", fn.getParentId());
                jsono.put("projectOrder", fn.getOrderNm());
                jsono.put("info", fn.getInfo());
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
                                //AgLayer layerc = agLayerMapper.findByLayerId(agDirLayerForProject.getLayerId());
                                AgLayer layerc = agLayerMapper.findByLayerIdAndEnabled(agDirLayerForProject.getLayerId());
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
                                        /**
                                         * 2020.07.22  添加三维代理地址判断
                                         * 120010: 3dtiles  图层类型
                                         */
                                        if ("120010".equals(layerc.getLayerType())){
                                            layerForm.setUrl(proxyUrl);
                                        }else {
                                            layerForm.setUrl(preUrl + substring);
                                        }
                                        //这个Uuid是启用代理时，代理服务需d要验证的标识，是必须的，不能删除
                                        layerForm.setUuid(Md5Utils.encrypt16(userLayer.getId()));
                                    }
                                    if (StringUtils.isEmpty(userLayer.getIsBaseMap()) || "0".equals(userLayer.getIsBaseMap())) {
                                        //非底图
                                        layerForm.setIsBaseMap("0");
                                        if(!"000001".equals(layerForm.getLayerType())) {
                                            //非属性表；000001是属性表类型
                                            layers.add(layerForm);
                                        }
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
                item.put("info", agChildDir.getInfo());
                List layers = new ArrayList();
                List<AgDirLayer> agDirLayersForChild = agDirLayerMapper.findByDirId(agChildDir.getId());//
                if (agDirLayersForChild != null && agDirLayersForChild.size() > 0) {
                    for (AgDirLayer agDirLayerForChild : agDirLayersForChild) {
                        List<AgUserLayer> userLayerList = agUserLayerMapper.findListByDirLayerId(agDirLayerForChild.getId());
                        for (AgUserLayer userLayer : userLayerList) {
                            if (userLayer.getUserId().equals(userId)) {
                                //AgLayer layerc = agLayerMapper.findByLayerId(agDirLayerForChild.getLayerId());
                                AgLayer layerc = agLayerMapper.findByLayerIdAndEnabled(agDirLayerForChild.getLayerId());
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
                                    if(layerForm.getNameCn().equals("堤防_2000")){
                                        int a=1;
                                    }
                                    /**
                                     * 改造代理，改造判断，目前是isProxy=1，改造后isProxy=1 || isProxy=2
                                     * 添加判断： || "2".equals(layerc.getIsProxy())
                                     */
                                    if ("1".equals(layerc.getIsProxy()) || "2".equals(layerc.getIsProxy())) {
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
                                        /**
                                         * 2020.07.22  添加三维代理地址判断
                                         * 120010: 3dtiles  图层类型
                                         */
                                        if ("120010".equals(layerc.getLayerType())){
                                            layerForm.setUrl(proxyUrl);
                                        }else {
                                            layerForm.setUrl(preUrl + substring);
                                        }
                                        //这个Uuid是启用代理时，代理服务需要验证的标识，是必须的，不能删除
                                        layerForm.setUuid(Md5Utils.encrypt16(userLayer.getId()));
                                    }
                                    if (StringUtils.isEmpty(userLayer.getIsBaseMap()) || "0".equals(userLayer.getIsBaseMap())) {
                                        layerForm.setIsBaseMap("0");
                                        if(!"000001".equals(layerForm.getLayerType())) {
                                            layers.add(layerForm);
                                        }
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
                map.put("info", fn.getInfo());
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
