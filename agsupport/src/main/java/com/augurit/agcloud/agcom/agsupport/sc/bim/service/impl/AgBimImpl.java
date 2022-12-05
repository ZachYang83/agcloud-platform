package com.augurit.agcloud.agcom.agsupport.sc.bim.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.config.RedisCache.MapCache;
import com.augurit.agcloud.agcom.agsupport.common.config.condition.RedisCondition;
import com.augurit.agcloud.agcom.agsupport.common.util.DESedeUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.ProxyUtil;
import com.augurit.agcloud.agcom.agsupport.common.util.UploadUtil;
import com.augurit.agcloud.agcom.agsupport.domain.*;
import com.augurit.agcloud.agcom.agsupport.mapper.*;
import com.augurit.agcloud.agcom.agsupport.sc.bim.dao.AgBimDao;
import com.augurit.agcloud.agcom.agsupport.sc.bim.service.IAgBim;
import com.augurit.agcloud.agcom.agsupport.sc.dataUpdate.dao.AgDataUpdateDao;
import com.augurit.agcloud.agcom.agsupport.sc.dir.controller.form.DirTree;
import com.augurit.agcloud.agcom.agsupport.sc.dir.controller.form.LayerForm;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.DirTreeUtil;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.Tree;
import com.augurit.agcloud.agcom.agsupport.sc.dir.util.TreeUtil;
import com.augurit.agcloud.agcom.agsupport.sc.field.dao.AgFieldDao;
import com.augurit.agcloud.agcom.agsupport.sc.field.service.IAgField;
import com.augurit.agcloud.agcom.agsupport.sc.layer.services.IAgDataTrans;
import com.augurit.agcloud.agcom.agsupport.sc.site.service.IAgSite;
import com.augurit.agcloud.agcom.agsupport.sc.site.util.defaultHttpClientUtil;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.util.JsonMapper;
import com.common.dbcp.DBHelper;
import com.common.util.Common;
import com.common.util.Md5Utils;
import com.common.util.ReflectBeans;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Augurit on 2017-04-17.
 */
@Service
public class AgBimImpl implements IAgBim {

    @Autowired
    private IAgField iAgField;

    @Autowired
    private AgParamMapper agParamMapper;

    @Autowired
    private AgBimModelMapper agBimModelMapper;

    @Autowired
    private AgBimMapper agBimMapper;

    @Autowired
    private AgBimLayerMapper agDirLayerMapper;

    @Autowired
    private AgUserMapper agUserMapper;

//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AgBimDao agDirDao;

    @Autowired
    private AgMetadataMapper agMetadataMapper;

    @Autowired
    public IAgSite IAgSite;

    @Autowired
    private AgFieldDao agFieldDao;
    @Autowired
    private AgDataUpdateDao agDataUpdateDao;
    @Autowired
    private AgServerMapper agServerMapper;

    private String prod_db = "jdbc";
    @Autowired
    private IAgDataTrans iAgDataTrans;
    @Autowired
    private AgStyleMapper agStyleMapper;
    @Autowired
    private AgUserLayerMapper agUserLayerMapper;

    @Autowired
    private AgLayerFieldMapper agLayerFieldMapper;
    @Autowired
    private AgTagMapper agTagMapper;
    @Autowired
    private AgServiceMapper agServiceMapper;
    @Autowired
    private AgOpenMapMapper agOpenMapMapper;

    private defaultHttpClientUtil defaultHttpClientUtil = new defaultHttpClientUtil();
    @Value("${agcloud.inf.load}")
    private Boolean agcloudInfLoad;

    @Override
    public List<AgDir> findAllDir() throws Exception {
        return agBimMapper.findAll();
    }

    @Override
    public List<AgDir> findAllDirSJHC() throws Exception {
        return agBimMapper.findAllDirSJHC();
    }

    @Override
    public List<AgDir> findSecondDir() throws Exception {
        return agBimMapper.findSecond();
    }

    @Override
    public List<AgDir> findDirsByXpath(String xpath) throws Exception {
        return agBimMapper.findByXpath(xpath);
    }

    @Override
    public AgDir findDirById(String id) throws Exception {
        return agBimMapper.findById(id);
    }

    @Override
    public String getDirOrder(String pid) throws Exception {
        return agBimMapper.getOrder(pid);
    }

    @Override
    public void saveDir(AgDir agDir) throws Exception {
        if (agDir == null) return;
        agBimMapper.save(agDir);
    }

    @Override
    public void updateDir(AgDir agDir) throws Exception {
        if (agDir == null) return;
        if (RedisCondition.matches() && MapCache.redisIsAvaliable(MapCache.redisIsAvaliableKey)) {
            List<String> keys = new ArrayList<String>();
            List<String> userIds = agUserMapper.findListByDirXpath(agBimMapper.findById(agDir.getId()).getXpath());
            if (userIds.size() > 0) {
                for (String userId : userIds) {
                    if (!keys.contains("tree#dir.layer.dir_layer.role_layer.user_role.map_param:" + userId))
                        keys.add("tree#dir.layer.dir_layer.role_layer.user_role.map_param:" + userId);
                }
            }
            //stringRedisTemplate.delete(keys);//清除redis缓存
        }

        agBimMapper.update(agDir);
    }

    @Override
    public void updateDirBatch(List<AgDir> list) throws Exception {
        if (list == null || list.size() <= 0) return;
        if (RedisCondition.matches() && MapCache.redisIsAvaliable(MapCache.redisIsAvaliableKey)) {
            List<String> keys = new ArrayList<String>();
            for (AgDir agDir : list) {
                agDir = agBimMapper.findById(agDir.getId());
                List<String> userIds = agUserMapper.findListByDirXpath(agDir.getXpath());
                if (userIds.size() > 0) {
                    for (String id : userIds) {
                        if (!keys.contains("tree#dir.layer.dir_layer.role_layer.user_role.map_param:" + id))
                            keys.add("tree#dir.layer.dir_layer.role_layer.user_role.map_param:" + id);
                    }
                }
            }
            //stringRedisTemplate.delete(keys);//清除redis缓存
        }

        agBimMapper.updateBatch(list);
    }

    @Override
    public void deleteDir(String id) throws Exception {
        String xpath = agBimMapper.findById(id).getXpath();
        if (RedisCondition.matches() && MapCache.redisIsAvaliable(MapCache.redisIsAvaliableKey)) {
            List<String> keys = new ArrayList<String>();
            List<String> userIds = agUserMapper.findListByDirXpath(xpath);
            if (userIds.size() > 0) {
                for (String userId : userIds) {
                    if (!keys.contains("tree#dir.layer.dir_layer.role_layer.user_role.map_param:" + userId))
                        keys.add("tree#dir.layer.dir_layer.role_layer.user_role.map_param:" + userId);
                }
            }
            //stringRedisTemplate.delete(keys);//清除redis缓存
        }

        List<AgDirLayer> agDirLayers = this.findDirLayerByDirXpath(xpath);//删除图层关联
        for (AgDirLayer temp : agDirLayers) {
            this.delDirLayer(temp.getId());
        }

        agBimMapper.deleteByXpath(xpath);//删除目录
    }

    @Override
    public PageInfo<AgLayer> searchLayer(String name,String isVector, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AgLayer> list = agBimModelMapper.findList(name,isVector);
        this.setLayerExtend(list);
        this.fillProxyUrl(list);
        return new PageInfo<AgLayer>(list);
    }

    @Override
    public PageInfo<AgLayer> searchLayersByDirId(AgLayer agLayer, Page page, String dirId, String userId, String isContain, String isBaseMap) throws Exception {
        AgDir agDir = null;
        if(dirId.equals("root")){
            //dirId为root时代表在根目录下所有图层中搜索
            agDir= agBimMapper.findTopDir();
            return searchLayer(agLayer, page, agDir.getXpath(), userId, "1", isBaseMap);
        }else {
            //dirId不为空时代表搜索指定目录下用户的权限图层
            agDir = agBimMapper.findById(dirId);
            if(agDir == null){
                return new PageInfo<AgLayer>(new ArrayList<AgLayer>());
            }
            return searchLayer(agLayer, page, agDir.getXpath(), userId, isContain, isBaseMap);
        }

    }
    @Override
    public PageInfo<AgLayer> searchLayer(AgLayer agLayer, Page page, String xpath, String userId, String isContain, String isBaseMap) throws Exception {
        List<AgLayer> list = null;

        // 此处根据目录显示图层
        List<AgUser> userList = new ArrayList<>();
        AgUser user = new AgUser();
        user.setId(userId);
        userList.add(user);
        PageHelper.startPage(page);
        list = agBimModelMapper.findListByXpathUsers(agLayer, xpath, userList, isContain, isBaseMap);
        if (list == null || list.size() <= 0) {
            list = new ArrayList<AgLayer>();
        }

        this.setparamid(list);
        this.setLayerExtend(list);
        this.fillProxyUrl(list);
        for(AgLayer item : list){
            String dataString = item.getData();
            if(!Common.isCheckNull(dataString)) {
                JSONObject data = JSONObject.fromObject(dataString);
                String key = "picture";
                if(data.has(key)) {
                    //图层缩略图
                    String pictureName = data.getString(key);
                    if(!Common.isCheckNull(pictureName)) {
                        String picturePath = UploadUtil.getUploadRelativePath() + getLayerPictureDir() + pictureName;
                        data.put(key, picturePath);
                        item.setData(data.toString());
                    }
                }
            }
        }
        return new PageInfo<AgLayer>(list);
    }

    public static String getLayerPictureDir(){
        return "layer_picture/";
    }

    @Override
    public List<AgLayer> getBaseLayerByUserId(String userId) throws Exception {
        return agBimModelMapper.findBaseLayerByUserId(userId);
    }

    @Override
    public PageInfo<AgLayer> searchLayer(String name, Page page, String userId) throws Exception {
        PageHelper.startPage(page);
        List<AgLayer> list = agBimModelMapper.findListByUserIdAndAgLayer(name, userId);
        this.setLayerExtend(list);
        this.fillProxyUrl(list);
        return new PageInfo<AgLayer>(list);
    }

    @Override
    public List<AgLayer> findLayerByUserId(String name, Page page, String userId) throws Exception {
        List<AgLayer> list = agBimModelMapper.findListByUserIdAndAgLayer(name, userId);
        this.setLayerExtend(list);
        this.fillProxyUrl(list);
        return list;
    }

    @Override
    public PageInfo<AgLayer> searchLayer(String type, String keyWord, String year, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AgLayer> list = agBimModelMapper.findListByKeyWord(type, keyWord, year);
        this.setLayerExtend(list);
        this.fillProxyUrl(list);
        return new PageInfo<AgLayer>(list);
    }

    /**
     * 生成图层的代理地址
     */
    private void fillProxyUrl(List<AgLayer> list) {
        String preUrl = ProxyUtil.getProxyPreUrl();
        for (AgLayer agLayer : list) {
            String proxyUrl = agLayer.getProxyUrl();
            if (!Common.isCheckNull(proxyUrl)) {
                String url = agLayer.getUrl();
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
                agLayer.setProxyUrl(preUrl + substring);
            }

        }
    }

    private void fillProxyUrl(AgLayer agLayer) {
        String preUrl = ProxyUtil.getProxyPreUrl();
        if (agLayer == null) return;
        String proxyUrl = agLayer.getProxyUrl();
        if (!Common.isCheckNull(proxyUrl)) {
            String url = agLayer.getUrl();
            //agLayer.setProxyUrl(preUrl + proxyUrl);
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
            agLayer.setProxyUrl(preUrl + substring);
        }

    }

    @Override
    public List<AgLayer> setparamid(List<AgLayer> list) {
        List<AgLayer> listz = new ArrayList<AgLayer>();
        JSONArray mapar = new JSONArray();
        for (AgLayer agLayer : list) {
            if ("Directorytree".equals(agLayer.getUrl())) {
                JSONObject mapid = new JSONObject();
                Pattern pattern = Pattern.compile("[0-9]*");
                Matcher isNum = pattern.matcher(agLayer.getName());
                if (!isNum.matches()) {
                    mapid.put("name", agLayer.getName());
                    mapid.put("id", agLayer.getId());
                    mapar.add(mapid);
                }

            }
        }
        for (AgLayer agLayer : list) {
            if (agLayer.getFeatureType() != null) {
                if (mapar.size() > 0) {
                    for (int i = 0; i < mapar.size(); i++) {
                        if (agLayer.getUrl() != null && agLayer.getUrl() != "" && agLayer.getUrl().indexOf(mapar.getJSONObject(i).getString("name")) != -1 && agLayer.getFeatureType() != null && agLayer.getFeatureType() != "") {
                            agLayer.set_parentId(mapar.getJSONObject(i).getString("id"));
                        }
                    }
                }
            }
            listz.add(agLayer);
        }

        return listz;
    }

    @Override
    public AgLayer findLayerByLayerId(String layerId) throws Exception {
        AgLayer agLayer = agBimModelMapper.findByLayerId(layerId);
        this.fillLayerExtend(agLayer);
        this.fillProxyUrl(agLayer);
        return agLayer;
    }

    @Override
    public AgLayer findLayerByDirLayerId(String dirLayerId) throws Exception {
        AgLayer agLayer = agBimModelMapper.findByDirLayerId(dirLayerId);
        this.fillLayerExtend(agLayer);
        this.fillProxyUrl(agLayer);
        return agLayer;
    }

    @Override
    public AgLayer findLayerByDLidAndUid(String dirLayerId, String userId) throws Exception {
        AgLayer agLayer = agBimModelMapper.findByDLidAndUid(dirLayerId, userId);
        this.fillLayerExtend(agLayer);
        this.fillProxyUrl(agLayer);
        return agLayer;
    }

    @Override
    public String findBindAuthorizedVector(String dirLayerId, String userId) throws Exception {
        AgLayer agLayer = agBimModelMapper.findByDirLayerId(dirLayerId);
        this.fillLayerExtend(agLayer);
        this.fillProxyUrl(agLayer);
        if (agLayer != null && StringUtils.isNotEmpty(agLayer.getVectorLayerId())) {
            AgDirLayer agDirLayer = agDirLayerMapper.findByIdAndUserId(agLayer.getVectorLayerId(), userId);
            if (agDirLayer != null) {
                return agDirLayer.getId();
            }
        }
        return null;
    }

    @Override
    public AgLayer findLayerByUrl(String url, String layerTable) throws Exception {
        AgLayer agLayer = agBimModelMapper.findByUrl(url, layerTable);
        this.fillLayerExtend(agLayer);
        this.fillProxyUrl(agLayer);
        return agLayer;
    }

    @Override
    public AgLayer findLayerByUrl(String url) throws Exception {
        AgLayer agLayer = agBimModelMapper.findLayerByUrl(url);
        this.fillLayerExtend(agLayer);
        this.fillProxyUrl(agLayer);
        return agLayer;
    }

    @Override
    public AgLayer findLayerByNameCn(String nameCn) throws Exception {
        AgLayer agLayer = agBimModelMapper.findByNameCn(nameCn);
        this.fillLayerExtend(agLayer);
        this.fillProxyUrl(agLayer);
        return agLayer;
    }

    @Override
    public List<AgLayer> findLayerByXpathAndUserId(String xpath, String userId) throws Exception {
        if (userId == null) return null;
        String layerName = null;
        if (xpath.indexOf("/") != -1) {
            if (xpath.endsWith("/")) {
                xpath = xpath.substring(0, xpath.length() - 1);
            }
            layerName = xpath.substring(xpath.lastIndexOf("/") + 1);
            xpath = xpath.substring(0, xpath.lastIndexOf("/"));
        }
        if (layerName != null) layerName = layerName.replace("_", "*_");
        if (xpath != null) xpath = xpath.replace("_", "*_");
        List<AgLayer> list = new ArrayList<>();
        if (!agcloudInfLoad.booleanValue()) {
            list = agBimModelMapper.findListByXpathAndLayerName(xpath, layerName, userId);
        } else {
            /*Map<String, String> param = new HashMap<>();
            param.put("userId", userId);
            HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/opus/ac/getRolesByUserId.do", param);
            JSONArray jsonArray = JSONArray.fromObject(httpRespons.getContent());
            List<Map> listMap = JsonUtils.toList(jsonArray.toString(), HashMap.class);
            List<AgUser> userList = new AgUserConverter().convertToList(null, listMap);
            if (userList.size() > 0) {
                list = agLayerMapper.findListByXpathAndLayerNameUsers(xpath, layerName, userList);
            }*/
            list = agBimModelMapper.findListByXpathAndLayerNameUsers(xpath, layerName, userId);
        }

        return list;
    }

    @Override
    public List<AgLayer> findLayerByDirNameAndLayerName(String dirName, String layerName, String userId) throws Exception {
        List<AgLayer> list = new ArrayList<>();
        if (layerName != null) layerName = layerName.replace("_", "*_");
        if (userId == null) return list;
        if (!agcloudInfLoad.booleanValue()) {
            list = agBimModelMapper.findListByDirNameAndLayerName(dirName, layerName, userId);
        } else {
           /* Map<String, String> param = new HashMap<>();
            param.put("userId", userId);
            HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/opus/ac/getRolesByUserId.do", param);
            JSONArray jsonArray = JSONArray.fromObject(httpRespons.getContent());
            List<Map> listMap = JsonUtils.toList(jsonArray.toString(), HashMap.class);
            List<AgUser> userList = new AgUserConverter().convertToList(null, listMap);
            if (userList.size() > 0) {
                list = agLayerMapper.findListByDirNameAndLayerNameUsers(dirName, layerName, userList);
            }*/

            list = agBimModelMapper.findListByDirNameAndLayerNameUsers(dirName, layerName, userId);
        }
        return list;
    }

    @Override
    //@CacheEvict(value = "user#project_layer_tree",allEntries = true)
    public void saveLayer(AgLayer agLayer) throws Exception {
        if (agLayer == null) return;
        //从扩展字段中获取元数据描述信息，同时删除
        JSONObject data = JSONObject.fromObject(agLayer.getData());
        String picture = (String) data.remove("picture");
        AgMetadata am = new AgMetadata();
        am.setId(UUID.randomUUID().toString());
        am.setPicture(picture);
        am.setCreateTime(new Date());
        am.setIllustration((String) data.remove("illustration"));
        am.setOwner((String) data.remove("owner"));
        agLayer.setData(data.toString());
        //判断图层是否使用代理，获取代理地址的UUID
        if ("1".equals(agLayer.getIsProxy())) {
            String proxyUrl = agLayer.getProxyUrl();
           /* if (Common.isCheckNull(proxyUrl)) {
                proxyUrl = UUIDUtil.getUUID();
            } else {
                //只获取 UUID
                //proxyUrl = proxyUrl.substring(proxyUrl.lastIndexOf("=") + 1, proxyUrl.length());
                proxyUrl = UUIDUtil.getUUID();
            }*/
            agLayer.setProxyUrl(proxyUrl);
        } else {
            agLayer.setProxyUrl("");
        }

        if (StringUtils.isEmpty(agLayer.getId())) { //保存图层
            agLayer.setId(UUID.randomUUID().toString());
            agLayer.setMetadataId(am.getId());
            agBimModelMapper.save(agLayer);
            agMetadataMapper.insertAgMetadata(am);//插入元数据信息2017-07-26
        } else {    //更新图层
            AgLayer oldLayer = this.findLayerByLayerId(agLayer.getId());
            String oldData = oldLayer.getData();
            if (StringUtils.isNotEmpty(oldData)) {
                JSONObject oldJson = JSONObject.fromObject(oldData);
                JSONObject newJson = JSONObject.fromObject(agLayer.getData());
                oldJson.putAll(newJson);
                if (newJson.get("token") == null) {
                    oldJson.remove("token");
                }
                agLayer.setData(oldJson.toString());
            }
            String metadataId = oldLayer.getMetadataId();
            if (StringUtils.isNotEmpty(metadataId)) {
                AgMetadata agMetadata = agMetadataMapper.getAgMetadataById(metadataId);
                String oldPicture = agMetadata.getPicture();
                if (StringUtils.isNotEmpty(picture)) {
                    if (StringUtils.isNotEmpty(oldPicture) && !picture.equals(oldPicture)) {
                        String fullPath =UploadUtil.getUploadAbsolutePath()+ getLayerPictureDir() + oldPicture;
                        //删除已上传的图片
                        File tempFile = new File(fullPath);
                        if (tempFile.exists()) {
                            tempFile.delete();
                        }
                    }
                    agMetadata.setPicture(picture);
                }
                agMetadata.setIllustration(am.getIllustration());
                agLayer.setMetadataId(agMetadata.getId());
                JSONObject json = JSONObject.fromObject(agLayer.getData());
                if (json.has("picture")) {
                    json.put("picture",agMetadata.getPicture());
                }
                agLayer.setData(json.toString());
                agMetadataMapper.updateAgMetadata(agMetadata);
            } else {
                agLayer.setMetadataId(am.getId());
                agMetadataMapper.insertAgMetadata(am);
            }
            if (agLayer.getLayerType().startsWith("01") &&
                    (!agLayer.getLayerTable().equals(oldLayer.getLayerTable()) || !agLayer.getDataSourceId().equals(oldLayer.getDataSourceId()))) {//矢量图层判断
                //清除缓冲数据
                iAgDataTrans.deleteData(oldLayer.getDataSourceId(), oldLayer.getLayerTable());
            }
            this.updateLayer(agLayer);
        }
        if (StringUtils.isNotEmpty(agLayer.getDirId())) {
            AgDirLayer agDirLayer = this.getDirLayer(agLayer.getDirId(), agLayer.getId());
            if (agDirLayer == null && StringUtils.isNotEmpty(agLayer.getDirId()) && StringUtils.isNotEmpty(agLayer.getId())) {
                String maxOrder = this.getLayerOrder();
                if (StringUtils.isEmpty(maxOrder)) {
                    maxOrder = "0";
                }
                int orderNm = Integer.parseInt(maxOrder);

                agDirLayer = new AgDirLayer();
                agDirLayer.setId(UUID.randomUUID().toString());
                agDirLayer.setOrderNm(++orderNm);
                agDirLayer.setDirId(agLayer.getDirId());
                agDirLayer.setLayerId(agLayer.getId());
                this.saveDirLayer(agDirLayer);
            }
        }
    }

    @Override
    //@CacheEvict(value = "user#project_layer_tree",allEntries = true)
    public void updateLayer(AgLayer agLayer) throws Exception {
        if (agLayer == null) return;
        if (RedisCondition.matches() && MapCache.redisIsAvaliable(MapCache.redisIsAvaliableKey)) {
            List<String> list = agUserMapper.findListByLayer(agLayer.getId());
            List<String> keys = new ArrayList<String>();
            if (list.size() > 0) {
                for (String userId : list) {
                    keys.add("tree#dir.layer.dir_layer.role_layer.user_role.map_param:" + userId);
                }
            }
//            stringRedisTemplate.delete(keys);//清除redis缓存
//            stringRedisTemplate.delete("service#layer:" + agLayer.getId());//删除跟agproxy关联的缓存
//            stringRedisTemplate.delete("service#layer:" + agLayer.getProxyUrl());
        }
        agBimModelMapper.update(agLayer);
    }

    @Transactional
    @Override
    //@CacheEvict(value = "user#project_layer_tree",allEntries = true)
    public void deleteLayerBatch(List<AgDirLayer> listAgDirLayer,String filePath) throws Exception {
        List<String> list = new ArrayList<>();
        for (AgDirLayer ag : listAgDirLayer){
            list.add(ag.getLayerId());
        }
        // 删除图层
        //agLayerMapper.deleteLayerBatch(list);
        //  1） 查询出所有的所有的图层数据,删除用户权限表关联关系,并删除缓存
        List<AgLayer> agLayerList = agBimModelMapper.findListByLayerIds(list.toArray(new String[list.size()]));
        for (AgLayer layer : agLayerList) {
            String layerType = layer.getLayerType();
            // 如果是矢量图层
            if ("010001".equals(layerType)) {
                JSONObject data = JSONObject.fromObject(layer.getData());
                String dataSourceId = data.get("dataSourceId").toString();
                layer.setDataSourceId(dataSourceId);
            }
            if (RedisCondition.matches() && MapCache.redisIsAvaliable(MapCache.redisIsAvaliableKey)) {
                List<String> userIds = agUserMapper.findListByLayer(layer.getId());
                List<String> keys = new ArrayList<String>();
                if (list.size() > 0) {
                    for (String userId : userIds) {
                        keys.add("tree#dir.layer.dir_layer.role_layer.user_role.map_param:" + userId);
                    }
                }
//                stringRedisTemplate.delete(keys);//清除redis缓存
//                stringRedisTemplate.delete("service#layer:" + layer.getId());

            }

        }
        List<AgDirLayer> agDirLayers = new ArrayList<>();//agDirLayerMapper.findByDirIdAndLayerIds(dirId, list);
        for (AgDirLayer agDirLayer : listAgDirLayer){
            // 获取图层的实际目录
            String dirId = agDirLayer.getDirId();
            AgDir agDir = agBimMapper.findById(dirId);
            String dirPath = agDir.getXpath();
            String layerId = agDirLayer.getLayerId();
            if (agDirLayer != null){
                agDirLayers.add(agDirLayer);
            }

        }
        if (agDirLayers.size()>0){
            // 这里根据图层所在目录删除图层关联关系
            agUserLayerMapper.deleteByDirLayerIds(agDirLayers);
            //  2) 删除专题图层关联关系，由于专题图层其中layer_id保存的是dir_layer_id
            //agProjectdirLayerMapper.deleteBacthByLayerIds(agDirLayers);
        }

        //  3) 删除图层字段关联关系
        agLayerFieldMapper.deleteBacthByLayerIds(list);
        //  4) 如果是矢量图层，删除矢量图层样式关联关系
        List<String> vectorLayerList = new ArrayList<>();
        for (AgLayer agLayer : agLayerList) {
            String layerType = agLayer.getLayerType();
            if ("010001".equals(layerType)) {
                vectorLayerList.add(agLayer.getId());
                //  5) 如果是矢量图层，删除数据缓冲数据
                // 已经缓冲完的矢量图层需要删除
                if (iAgDataTrans.isTransComplete(agLayer.getDataSourceId(), agLayer.getLayerTable())) {
                    iAgDataTrans.deleteData(agLayer.getDataSourceId(), agLayer.getLayerTable());
                }
            }
        }
        if (vectorLayerList.size() > 0) {
            agStyleMapper.deleteAgStyleBatch(vectorLayerList);
        }
        //  6) 删除标签图层关联关系
        agTagMapper.deleteTagBatch(list);
        //  7) 删除资源中心关联关系，五个表oneMap开头的表的关联关系
        //  8) 删除服务审批关联关系
        // 先根据layerId查询出来需要删除的申请记录id
//        List<AgOpenMapApplyItem> applyItemByLayerIds = agOpenMapMapper.findApplyItemByLayerIds(list);
//        if (applyItemByLayerIds.size() > 0) {
//            agOpenMapMapper.delApplyCartitemByLayerIds(list);
//            agOpenMapMapper.delAuditlistByNo(applyItemByLayerIds);
//            agOpenMapMapper.delAttachfile(applyItemByLayerIds);
//            agOpenMapMapper.delApplyItemByApplyId(applyItemByLayerIds);
//            agOpenMapMapper.delApplyItemByLayerIds(list);
//            agServiceMapper.deleteAgServiceUserinfoBatch(list);
//        }
        //  9) 删除目录图层关联关系
        agDirLayerMapper.deleteBatchByIds(agDirLayers);
        //  10) 删除图层
        agBimModelMapper.deleteLayerBatch(list);
        // 11) 同步删除元数据和图层的缩略图
        for (AgLayer agLayer : agLayerList) {
            if (agLayer.getMetadataId() != null) {
                AgMetadata agMetadata = agMetadataMapper.getAgMetadataById(agLayer.getMetadataId());
                if (agMetadata != null && agMetadata.getPicture() != null) {
                    File file = new File(filePath + getLayerPictureDir() + agMetadata.getPicture());
                    if (file.exists()) {
                        file.delete();
                    }
                }
                agMetadataMapper.deleteAgMetadata(agLayer.getMetadataId());
            }
        }

    }

    @Transactional
    @Override
    //@CacheEvict(value = "user#project_layer_tree",allEntries = true)
    public void deleteLayerBatch(String dirLayerIds,String uploadAbsolutePath) throws Exception {
        String[] arrayDirLayerId = dirLayerIds.split(",");
        List<AgDirLayer> agDirLayerList = agDirLayerMapper.findByIds(arrayDirLayerId);
        if (agDirLayerList != null && agDirLayerList.size() > 0) {
            List<AgDirLayer> listDirLayer = new ArrayList<>();
            for (AgDirLayer agDirLayer : agDirLayerList) {
                // 删除每个图层，先判断与图层目录关联数
                List<AgDirLayer> agDirLayers = findDirLayerByLayerId(agDirLayer.getLayerId());
                // 大于1，则此图层与其他目录有关联，删除关联关系，不删除图层
                if (agDirLayers.size() > 1){
                    delDirLayer(agDirLayer.getId());
                }else { // 等于1，删除关联关系，并删除图层
                    //AgLayer agLayer = iAgDir.findLayerByLayerId(layerId);
                    listDirLayer.add(agDirLayer);
                }

            }
            if (listDirLayer.size() > 0){
                deleteLayerBatch(listDirLayer,uploadAbsolutePath);
            }
        }
    }

    @Override
    public AgDirLayer getDirLayer(String dirId, String layerId) throws Exception {
        return agDirLayerMapper.findByDirIdAndLayerId(dirId, layerId);
    }

    @Override
    public List<AgDirLayer> findAll() throws Exception {
        return agDirLayerMapper.findAll();
    }

    @Override
    public List<AgDirLayer> findByDirId(String dirId) throws Exception {
        return agDirLayerMapper.findByDirId(dirId);
    }

    @Override
    public List<AgDirLayer> findDirLayerByDirXpath(String dirXpath) throws Exception {

        return agDirLayerMapper.findListByDirXpath(dirXpath);
    }

    @Override
    public List<AgDirLayer> findDirLayerByLayerId(String layerId) throws Exception {

        return agDirLayerMapper.findListByLayerId(layerId);
    }

    @Override
    public List<AgDirLayer> findDirLayerByIds(String[] dirLayerIds) throws Exception {
        return agDirLayerMapper.findByIds(dirLayerIds);
    }

    @Override
    public List<AgDir> findDirByIds(String[] dirIds) throws Exception {
        return agBimMapper.findByIds(dirIds);
    }

    @Override
    public String getLayerOrder() throws Exception {
        return agDirLayerMapper.getMaxOrder();
    }

    @Override
    public void saveDirLayer(AgDirLayer agDirLayer) throws Exception {
        if (agDirLayer == null) return;
        agDirLayerMapper.save(agDirLayer);
    }

    @Override
    public void updateDirLayerBatch(List<AgDirLayer> list) throws Exception {
        if (list == null || list.size() <= 0) return;
        if (RedisCondition.matches() && MapCache.redisIsAvaliable(MapCache.redisIsAvaliableKey)) {
            List<String> dirLayerIds = new ArrayList<String>();
            for (AgDirLayer agDirLayer : list) {
                dirLayerIds.add(agDirLayer.getId());
            }
            if (dirLayerIds.size() > 0) {
                List<String> userIds = agUserMapper.findListByDirLayers(dirLayerIds);
                if (userIds.size() > 0) {
                    List<String> keys = new ArrayList<String>();
                    for (String userId : userIds) {
                        keys.add("tree#dir.layer.dir_layer.role_layer.user_role.map_param:" + userId);
                    }
                   // stringRedisTemplate.delete(keys);//清除redis缓存
                }
            }
        }

        agDirLayerMapper.updateBatch(list);
    }

    @Override
    public void delDirLayer(String id) throws Exception {
        if (RedisCondition.matches() && MapCache.redisIsAvaliable(MapCache.redisIsAvaliableKey)) {
            List<String> dirLayerIds = new ArrayList<String>();
            dirLayerIds.add(id);
            if (dirLayerIds.size() > 0) {
                List<String> userIds = agUserMapper.findListByDirLayers(dirLayerIds);
                if (userIds.size() > 0) {
                    List<String> keys = new ArrayList<String>();
                    for (String userId : userIds) {
                        keys.add("tree#dir.layer.dir_layer.role_layer.user_role.map_param:" + userId);
                    }
                    //stringRedisTemplate.delete(keys);//清除redis缓存
                }
            }
            //2017-07-13增加，用于清除和agproxy关联的redis缓存
            AgLayer layer = agBimModelMapper.findByDirLayerId(id);
            if (layer != null && layer.getProxyUrl() != null) {
                //stringRedisTemplate.delete("service#layer:" + layer.getProxyUrl());
            }
        }

       /* List<AgRoleLayer> agRoleLayers = iAgRole.findRoleLayerByDirLayerId(id);//删除角色关联
        for (AgRoleLayer temp : agRoleLayers) {
            iAgRole.deleteRoleLayer(temp.getId());
        }*/

        agDirLayerMapper.delete(id);
    }

    @Override
    public List<Map> findLayersFromProd() throws Exception {
        String sql = "select * from ag_sup_layer t where t.layer_type not in(3)";
        return DBHelper.find(this.prod_db, sql, null);
    }

    @Override
    public String getLayerType(String layerType, String featureType, String tileType) {
        String lType = null;
        String tType = null;
        String firm = null;
        switch (Integer.parseInt(layerType)) {
            case 1:
                lType = "02";
                if (StringUtils.isNotEmpty(featureType)) {
                    switch (Integer.parseInt(featureType)) {
                        case 5:
                        case 7:
                            if ("AGT".equals(tileType)) {
                                tType = "01";
                                firm = "01";
                            } else if ("AST".equals(tileType)) {
                                tType = "02";
                                firm = "02";
                            } else if ("AST-C".equals(tileType)) {
                                tType = "03";
                                firm = "02";
                            }
                            break;
                        case 6:
                            tType = "01";
                            firm = "01";
                            break;
                        default:
                            break;
                    }
                }
                break;
            case 3:
                lType = "01";
                break;
            case 4:
                lType = "03";
                firm = "02";
                break;
            case 5:
                lType = "04";
                firm = "02";
                break;
            case 6:
                lType = "04";
                firm = "04";
                break;
            case 7:
                lType = "05";
                if ("AGT".equals(tileType)) {
                    tType = "01";
                    firm = "01";
                } else {
                    firm = "02";
                }
                break;
            case 10:
                lType = "05";
                firm = "04";
                break;
            case 11:
                lType = "02";
                firm = "05";
                break;
            case 12:
                lType = "02";
                firm = "07";
                break;
            case 13:
                lType = "02";
                firm = "08";
                break;
            case 14:
                lType = "02";
                firm = "09";
                break;
            case 15:
                lType = "02";
                firm = "06";
                break;
            default:
                break;
        }
        if (lType == null) {
            lType = "__";
        }
        if (tType == null) {
            tType = "__";
        }
        if (firm == null) {
            firm = "__";
        }
        String value = lType + tType + firm;
        /*AgDic agDic = agDicMapper.findLayerTypeByValue(value);
        if (agDic != null) {
            return agDic.getValue();
        }*/
        return value;
    }

    @Override
    public boolean checkLayerTable(String dataSourceId, String layerTable, String pkColumn, String geometryColumn) {
        return agDirDao.checkLayerTable(dataSourceId, layerTable, pkColumn, geometryColumn);
    }

    @Override
    //@Cacheable(value = "tree#dir.layer.dir_layer.user_layer.user.map_param", key = "#userId", condition = "#userId != null")
    public String getTreeByUser(String userId) throws Exception {
        List<AgUser> userList = new ArrayList<AgUser>();
        List baselayers =new ArrayList();
        // 2018.8.21 去掉用户角色关联
      /*  if (agcloudInfLoad.booleanValue()) {
            Map<String, String> param = new HashMap<>();
            param.put("userId", userId);
            HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/opus/ac/getRolesByUserId.do", param);
            JSONArray jsonArray = JSONArray.fromObject(httpRespons.getContent());
            List<Map> listMap = JsonUtils.toList(jsonArray.toString(), HashMap.class);
            userList = new AgUserConverter().convertToList(null, listMap);
        }*/

     /*   List<AgDir> list_dir = null;
        if (!agcloudInfLoad.booleanValue()) {
            list_dir = agDirMapper.findByUserId(userId);
        } else {
            if (userList.size() <= 0) {
                list_dir = new ArrayList<AgDir>();
            } else {
                list_dir = agDirMapper.findByUserId(userId);
            }
        }*/
        List<AgDir> list_dir = new ArrayList<AgDir>();
        list_dir = agBimMapper.findByUserId(userId);

        StringBuffer sb = new StringBuffer();
        for (AgDir agDir : list_dir) {
            if (StringUtils.isNotEmpty(agDir.getDirSeq())) {
                sb.append(agDir.getDirSeq()).append(",");
            }
        }
        String[] dirIds = sb.toString().split(",");
        List<AgDir> dirs = agBimMapper.findByIds(dirIds);

        List<DirTree> trees = new ArrayList<DirTree>();
        for (AgDir agDir : dirs) {
            DirTree dirTree = new DirTree();
            dirTree.setId(agDir.getId());
            dirTree.setPid(agDir.getParentId());
            dirTree.setText(agDir.getName());
            trees.add(dirTree);
        }
        List<AgLayer> layers = new ArrayList<AgLayer>();
        layers = agBimModelMapper.findListByUserId(userId);
       /* if (!agcloudInfLoad.booleanValue()) {
            layers = agLayerMapper.findListByUserId(userId);
        } else {
            if (userList.size() <= 0) {
                layers = new ArrayList<AgLayer>();
            } else {
                layers = agLayerMapper.findListByUsers(userList);
            }
        }*/
        this.setLayerExtend(layers);
        String preUrl = "#proxyHost_/";
        for (AgLayer agLayer : layers) {
            //2017-08-18加上token判断，使用token则判断有没有，没有则动态获取;2017-12-14增加serverLink字段
            if (agLayer.getData() != null) {
                JSONObject extendData = JSONObject.fromObject(agLayer.getData());
                Object useToken = extendData.get("useToken");
                if (useToken != null && "1".equals(useToken)) {
                    Object token = extendData.get("token");
                    if (Common.isCheckNull(token)) {
                        String serverLink = null;
                        if (extendData.containsKey("serverLink")) {
                            serverLink = extendData.getString("serverLink");
                        }
                        if (Common.isCheckNull(serverLink)) {
                            String url = agLayer.getUrl();
                            List<AgSite> allAgSite = IAgSite.getAllSite();
                            for (AgSite as : allAgSite) {
                                if (url.contains(as.getSerIp()) && url.contains(":" + as.getSerRestport()) && "ArcGis".equals(as.getSerType())) {
                                    serverLink = as.getId();
                                    extendData.put("serverLink", serverLink);
                                    break;
                                }
                            }
                        }
                        agLayer.setData(extendData.toString());
                    }
                }
            }
            String isProxy = agLayer.getIsProxy();
            //2017-07-06将代理地址赋给url,减少前端判断,2017-11-02 将拼接userId 的 下划线去掉，并将userId使用MD5编码，在agproxy那边有对应编码集匹配
            if ("1".equals(isProxy)) {
                //agLayer.setUrl(preUrl + proxyUrl + "/" + Md5Utils.encrypt16(userId));
                String url = agLayer.getUrl();
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
                agLayer.setUrl(preUrl + substring + "?uuid=" + Md5Utils.encrypt16(agLayer.getUserLayerId()));
            }

        }
        for (DirTree dirTree : trees) {
            for (AgLayer agLayer : layers) {
                if (dirTree.getId().equals(agLayer.getDirId())) {
                    LayerForm layerForm = ReflectBeans.copy(agLayer, LayerForm.class);
                    layerForm.setText(agLayer.getName());
                    layerForm.setType(agLayer.getLayerTypeCn());

                    AgUserLayer agUserLayer = iAgField.getLayerConfigByUserId(layerForm.getDirLayerId(), userId);
                    if (StringUtils.isEmpty(agUserLayer.getIsBaseMap()))
                        layerForm.setIsBaseMap("0");
                    else
                        layerForm.setIsBaseMap(agUserLayer.getIsBaseMap());
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
                    layerForm.setExtent(agUserLayer.getExtent());
                    layerForm.setState(map);

                    AgMapParam agMapParam = agParamMapper.findById(agLayer.getParamId());//查询地图参数
                    if (agMapParam == null) agMapParam = new AgMapParam();
                    layerForm.setMapParam(agMapParam);

                    System.out.println(agMapParam);
                    List list = dirTree.getNodes();
                    if (list == null) list = new ArrayList();
                    if("1".equals(layerForm.getIsBaseMap())){
                        baselayers.add(layerForm);
                    }else {
                        list.add(layerForm);
                    }
                    dirTree.setNodes(list);//添加子节点
                }
            }
        }
        DirTree basedirTree = new DirTree();
        basedirTree.setNodes(baselayers);
        trees.add(basedirTree);
        trees = DirTreeUtil.getFatherNode(trees);
        DirTreeUtil. deleteTree(trees);

        for (int i = 0; i < trees.size(); i++) {
            this.setState(trees.get(i));
        }
        JsonMapper mapper = new JsonMapper(JsonInclude.Include.ALWAYS);//不去掉为空字段
        return mapper.toJson(trees);
    }

    @Override
    public String getToken(String id) {
        try {
            //AgDic agDic = agDicMapper.findById(id);
            AgServer server = agServerMapper.selectServerById(id);
            //Map map = (Map) JSONObject.toBean((JSONObject) Common.fromObject(agDic.getValue()), HashMap.class);
            //String url = "http://" + map.get("ip") + ":" + map.get("port") + "/arcgis/tokens/generateToken";
            String url = "http://" + server.getIp() + ":" + server.getPort() + "/arcgis/tokens/generateToken";
            Map<String, String> createMap = new HashMap<>();
            //createMap.put("password", map.get("password").toString());
            //createMap.put("username", map.get("name").toString());

            createMap.put("password", DESedeUtil.desDecrypt(server.getPassword()));
            createMap.put("username", server.getUserName());
            createMap.put("client", "requestip");
            createMap.put("ip", "");
            createMap.put("referer", "");
            createMap.put("expiration", "10080");
            createMap.put("f", "json");
            String result = defaultHttpClientUtil.doPost(url, createMap, "utf-8");
            if (Common.isCheckNull(result)) {
                url = "https://" + server.getIp() + ":" + server.getPort() + "/arcgis/tokens/generateToken";
                result = defaultHttpClientUtil.doPost(url, createMap, "utf-8");
            }
            result = (String) JSONObject.fromObject(result).get("token");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AgServer> getServerLink() throws Exception {
        List<AgServer> list = agServerMapper.findList(new AgServer());
        return list;
    }

    @Override
    public List<LayerForm> findLayerByUserIdAndFeatureType(String userId, String featureType) throws Exception {
        List<AgLayer> agLayers = null;
        if (!agcloudInfLoad.booleanValue()) {
            agLayers = agBimModelMapper.findListByUserIdAndFeatureType(userId, featureType);
        } else {
            /*Map<String, String> param = new HashMap<>();
            param.put("userId", userId);
            HttpRespons httpRespons = new HttpRequester().sendPost(opusAdminUrl + "/rest/opus/ac/getRolesByUserId.do", param);
            JSONArray jsonArray = JSONArray.fromObject(httpRespons.getContent());
            List<Map> listMap = JsonUtils.toList(jsonArray.toString(), HashMap.class);
            List<AgUser> userList = new AgUserConverter().convertToList(null, listMap);
            if (userList.size() <= 0) {
                agLayers = new ArrayList<AgLayer>();
            } else {
                agLayers = agLayerMapper.findListByUsersAndFeatureType(userList, featureType);
            }*/
            agLayers = agBimModelMapper.findListByUserIdAndFeatureType(userId, featureType);
        }
        this.setLayerExtend(agLayers);
        this.fillProxyUrl(agLayers);

        List<LayerForm> list = new ArrayList<LayerForm>();
        for (AgLayer agLayer : agLayers) {
            LayerForm layerForm = ReflectBeans.copy(agLayer, LayerForm.class);
            layerForm.setText(agLayer.getName());
            layerForm.setType(agLayer.getLayerTypeCn());
            layerForm.setNameCn(agLayer.getNameCn());
            layerForm.setDirLayerId(agLayer.getDirLayerId());
            list.add(layerForm);
        }
        return list;
    }

    @Override
    public String findVectorLayer() throws Exception {
        List<AgDir> list_dir = agBimMapper.findByVector();
        StringBuffer sb = new StringBuffer();
        for (AgDir agDir : list_dir) {
            if (StringUtils.isNotEmpty(agDir.getDirSeq())) {
                sb.append(agDir.getDirSeq()).append(",");
            }
        }
        String[] dirIds = sb.toString().split(",");
        List<AgDir> dirs = agBimMapper.findByIds(dirIds);

        List<Tree> trees = new ArrayList<Tree>();
        for (AgDir agDir : dirs) {
            Tree tree = new Tree();
            tree.setId(agDir.getId());
            tree.setPid(agDir.getParentId());
            tree.setpId(agDir.getParentId());
            tree.setText(agDir.getName());
            tree.setXpath(agDir.getXpath());
            trees.add(tree);
        }

        List<AgLayer> layers = agBimModelMapper.findVectorList();
        this.setLayerExtend(layers);
        this.fillProxyUrl(layers);

        for (Tree tree : trees) {
            for (AgLayer agLayer : layers) {
                if (tree.getId().equals(agLayer.getDirId())) {
                    LayerForm layerForm = ReflectBeans.copy(agLayer, LayerForm.class);
                    layerForm.setText(agLayer.getName());
                    layerForm.setType(agLayer.getLayerTypeCn());
                    layerForm.setId(layerForm.getDirLayerId());//设置叶子节点id为目录图层id

                    List list = tree.getChildren();
                    if (list == null) list = new ArrayList();
                    list.add(layerForm);
                    tree.setChildren(list);//添加子节点
                    //tree.setState("closed");
                }
            }
        }
        trees = TreeUtil.getFatherNode(trees);

        JsonMapper mapper = new JsonMapper(JsonInclude.Include.ALWAYS);//不去掉为空字段
        String json = mapper.toJson(trees).replace("text", "name");
        return json;
    }

    @Override
    public String findRenderLayer() throws Exception {
        List<AgDir> list_dir = agBimMapper.findByDirLayer();
        StringBuffer sb = new StringBuffer();
        for (AgDir agDir : list_dir) {
            if (StringUtils.isNotEmpty(agDir.getDirSeq())) {
                sb.append(agDir.getDirSeq()).append(",");
            }
        }
        String[] dirIds = sb.toString().split(",");
        List<AgDir> dirs = agBimMapper.findByIds(dirIds);

        List<Tree> trees = new ArrayList<Tree>();
        for (AgDir agDir : dirs) {
            Tree tree = new Tree();
            tree.setId(agDir.getId());
            tree.setPid(agDir.getParentId());
            tree.setText(agDir.getName());
            tree.setXpath(agDir.getXpath());
            trees.add(tree);
        }

        List<AgLayer> layers = agBimModelMapper.findListByDirLayer();
        this.setLayerExtend(layers);
        this.fillProxyUrl(layers);

        for (Tree tree : trees) {
            for (AgLayer agLayer : layers) {
                if (tree.getId().equals(agLayer.getDirId())) {
                    LayerForm layerForm = ReflectBeans.copy(agLayer, LayerForm.class);
                    layerForm.setText(agLayer.getName());
                    layerForm.setType(agLayer.getLayerTypeCn());
                    layerForm.setId(layerForm.getDirLayerId());//设置叶子节点id为目录图层id

                    List list = tree.getChildren();
                    if (list == null) list = new ArrayList();
                    list.add(layerForm);
                    tree.setChildren(list);//添加子节点
                    //Map map = new HashMap();
                    //map.put("expanded","false");
                    //tree.setState(map.toString());
                }
            }
        }
        trees = TreeUtil.getFatherNode(trees);

        JsonMapper mapper = new JsonMapper(JsonInclude.Include.ALWAYS);//不去掉为空字段
        String json = mapper.toJson(trees).replace("text", "name");
        return json;
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
                    LayerForm layerForm = (LayerForm) nodes.get(i);
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

    @Override
    public void putPreviewKey(String keyName, String previewKey) throws Exception {
        AgShare agShare = agBimModelMapper.getPreviewKey(keyName);
        if (agShare != null) {
            agShare.setValue(previewKey);
            agBimModelMapper.updatePreviewKey(agShare);
        } else {
            agShare = new AgShare();
            agShare.setId(UUID.randomUUID().toString());
            agShare.setShareKey(keyName);
            agShare.setValue(previewKey);
            agBimModelMapper.putPreviewKey(agShare);
        }
    }

    @Override
    public AgMetadata getAgMetadata(String id) throws Exception {
        return agMetadataMapper.getAgMetadataById(id);
    }

    public List<AgLayer> findLayerByUserIdAndXpath(String userId, String xpath) throws Exception {
        List<AgLayer> agLayers = agBimModelMapper.findListByUserIdAndXpath(userId, xpath);
        this.setLayerExtend(agLayers);
        this.fillProxyUrl(agLayers);
        return agLayers;
    }

    @Override
    public void setLayerUseCache(String dirLayerIds) throws Exception {
        String ids[] = dirLayerIds.split(",");
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] != "") {
                AgLayer layer = agBimModelMapper.findByDirLayerId(ids[i]);
                String extendData = layer.getData();
                JSONObject exObject = JSONObject.fromObject(extendData);
                exObject.put("isCacheText", "1");
                exObject.put("isCacheImage", "1");
                layer.setData(exObject.toString());
                updateLayer(layer);
            }
        }
    }

    @Override
    public List<AgLayer> findLayerListByLayerIds(String[] layerIds) throws Exception {
        List<AgLayer> list = agBimModelMapper.findListByLayerIds(layerIds);
        this.setLayerExtend(list);
        this.fillProxyUrl(list);
        return list;
    }

    @Override
    public List<AgLayerFieldConf> getTableFields(String datasourceId, String tableName) throws Exception {
        List<Map> fieldMap = agFieldDao.getLayerFieldFromDB(datasourceId, tableName);
        List<AgLayerFieldConf> list = new ArrayList<>();
        for (int i = 0; i < fieldMap.size(); i++) {
            Map map = fieldMap.get(i);
            AgLayerFieldConf agLayerFieldConf = new AgLayerFieldConf();
            agLayerFieldConf.setFieldName(String.valueOf(map.get("field_name")));
            list.add(agLayerFieldConf);
        }
        return list;
    }

    @Override
    public List<AgLayerFieldConf> findTableFieldsByDirLayerId(String dirLayerId) throws Exception {
        AgLayer layer = agBimModelMapper.findByDirLayerId(dirLayerId);
        this.fillLayerExtend(layer);
        return getTableFields(layer.getDataSourceId(), layer.getLayerTable());
    }

    @Override
    public AgLayer findLayerBylayerTableAndUserId(String userId, String layerTable) throws Exception {
        AgLayer agLayer = agBimModelMapper.findLayerBylayerTableAndUserId(userId, layerTable);
        this.fillLayerExtend(agLayer);
        this.fillProxyUrl(agLayer);
        return agLayer;
    }

    @Override
    public List<AgLayer> getPageByUserId(String userId) throws Exception {
        List<AgLayer> list = agBimModelMapper.findPageByUserId(userId);
        return list;
    }

    @Override
    public LinkedList<Map<String, String>> findTableFields(String dataSourceId, String layerTable, String layerId) throws Exception {
        List<AgLayerFieldConf> tableFields = getTableFields(dataSourceId, layerTable);
        Map<String, String> alias = agDataUpdateDao.getFieldNameAlias(layerId);
        LinkedList<Map<String, String>> titleAlias = new LinkedList<>();
        for (AgLayerFieldConf fieldName : tableFields) {
            String name = fieldName.getFieldName();
            Map<String, String> map = new HashMap<>();
            if (alias.get(name) != null) {
                map.put("fieldName", name);
                map.put("fieldNameAlias", alias.get(name));
                titleAlias.add(map);
            } else {
                map.put("fieldName", name);
                map.put("fieldNameAlias", name);
                titleAlias.add(map);
            }
        }
        return titleAlias;
    }
}
