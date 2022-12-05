package com.augurit.agcloud.agcom.agsupport.sc.param.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.config.RedisCache.MapCache;
import com.augurit.agcloud.agcom.agsupport.common.config.condition.RedisCondition;
import com.augurit.agcloud.agcom.agsupport.common.util.UploadUtil;
import com.augurit.agcloud.agcom.agsupport.domain.AgMapParam;
import com.augurit.agcloud.agcom.agsupport.mapper.AgParamMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgUserMapper;
import com.augurit.agcloud.agcom.agsupport.sc.param.service.IAgParam;
import com.augurit.agcloud.agcom.syslog.service.annotation.SysLog;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.common.dbcp.DBHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Augurit on 2017-04-18.
 */
@Service
public class AgParamImpl implements IAgParam {

    @Autowired
    private AgParamMapper agParamMapper;

    @Autowired
    private AgUserMapper agUserMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private String prod_db = "jdbc";

    @Override
    public List<AgMapParam> searchAll() throws Exception {
        return agParamMapper.searchAll();
    }

    @Override
    public PageInfo<AgMapParam> searchParam(String name, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AgMapParam> list = agParamMapper.findList(name);
        return new PageInfo<AgMapParam>(list);
    }

    @Override
    public AgMapParam findMapParamById(String id) throws Exception {

        return agParamMapper.findById(id);
    }

    @Override
    public List<AgMapParam> checkName(String name) throws Exception {
        System.out.println(agParamMapper.findByName(name));
        List<AgMapParam> list = agParamMapper.findByName(name);
        return list;
    }

    @Override
    public AgMapParam findMapParam(AgMapParam agMapParam) throws Exception {

        return agParamMapper.find(agMapParam);
    }

    @Override
    @SysLog(sysName = "地图运维",funcName = "保存地图参数")
    public ResultForm saveMapParam(AgMapParam agMapParam) throws Exception {
        if (agMapParam == null){
            return new ResultForm(false,"地图参数对象异常");
        }
        String defaultMapParemFlag = "1"; //默认地图参数的值是"1"
        List<AgMapParam> list = agParamMapper.findDefaultMap(defaultMapParemFlag);
        if(defaultMapParemFlag.equals(agMapParam.getDefaultMap())){
            if(list!=null){
                for(int i=0;i<list.size();i++){
                    list.get(i).setDefaultMap("0");
                    agParamMapper.update(list.get(i));
                }
            }
            agParamMapper.save(agMapParam);
            return new ResultForm(true,"保存成功");
        }else{
            if(list==null || list.size()==0){
                //如果原先没有默认的地图参数就将新增的记录设置为默认的地图参数
                agMapParam.setDefaultMap(defaultMapParemFlag);
                agParamMapper.save(agMapParam);
                return new ResultForm(true,"没有默认的地图参数，新增的地图参数被设置为默认参数");
            }else{
                agParamMapper.save(agMapParam);
                return new ResultForm(true,"保存成功");
            }
        }

    }

    @Override
    public void saveMapParamBatch(List<AgMapParam> list) throws Exception {
        if (list == null || list.size() <= 0) return;
        agParamMapper.saveBatch(list);
    }

    @Override
    @SysLog(sysName = "地图运维",funcName = "修改地图参数")
    public ResultForm updateMapParam(AgMapParam agMapParam) throws Exception {
        if (agMapParam == null) return new ResultForm(false,"地图参数对象异常");
        String defaultMapParemFlag = "1"; //默认地图参数的值是"1"
        if (RedisCondition.matches() && MapCache.redisIsAvaliable(MapCache.redisIsAvaliableKey)) {
            String[] ids = new String[1];
            ids[0] = agMapParam.getId();
            List<String> userIds = agUserMapper.findListByParams(ids);
            List<String> keys = new ArrayList<String>();
            if (userIds.size() > 0) {
                for (String userId : userIds) {
                    if (!keys.contains("tree#dir.layer.dir_layer.role_layer.user_role.map_param:" + userId))
                        keys.add("tree#dir.layer.dir_layer.role_layer.user_role.map_param:" + userId);
                }
            }
            stringRedisTemplate.delete(keys);//清除redis缓存
        }
        if(defaultMapParemFlag.equals(agMapParam.getDefaultMap())){
            List<AgMapParam> list = agParamMapper.findDefaultMap(defaultMapParemFlag);
            if(list!=null){
                for(int i=0;i<list.size();i++){
                    list.get(i).setDefaultMap("0");
                    agParamMapper.update(list.get(i));
                }
            }
        }else{
            AgMapParam agMapParamBefore = agParamMapper.findById(agMapParam.getId());
            if(agMapParamBefore.getDefaultMap()!=null && agMapParamBefore.getDefaultMap().equals(defaultMapParemFlag)){
                return new ResultForm(false,"需要保留一个地图参数作为默认参数");
            }
        }
        agParamMapper.update(agMapParam);
        return new ResultForm(true,"修改成功");
    }

    @Override
    public ResultForm deleteMapParamBatch(String[] ids) throws Exception {
        ResultForm result = null;
        String defaultMapParemFlag = "1"; //默认地图参数的值是"1"
        if (RedisCondition.matches() && MapCache.redisIsAvaliable(MapCache.redisIsAvaliableKey)) {
            List<String> userIds = agUserMapper.findListByParams(ids);
            List<String> keys = new ArrayList<String>();
            if (userIds.size() > 0) {
                for (String userId : userIds) {
                    if (!keys.contains("tree#dir.layer.dir_layer.role_layer.user_role.map_param:" + userId))
                        keys.add("tree#dir.layer.dir_layer.role_layer.user_role.map_param:" + userId);
                }
            }
            stringRedisTemplate.delete(keys);//清除redis缓存
        }
        //查询默认地图参数，默认地图参数在业务上只有唯一的一个，这里返回List只是想直接用原有的方法
        List<AgMapParam> list = agParamMapper.findDefaultMap(defaultMapParemFlag);
        if(list != null){
            for(String id : ids){
                List<AgMapParam> listDefault = list.stream().filter(p->p.getId().equals(id)).collect(Collectors.toList());
                if(listDefault != null && listDefault.size()>0){
                    result = new ResultForm(false,"不能删除默认地图参数");
                    break;
                }
            }
        }
        if(result == null){
            agParamMapper.deleteBatchNotDefaultParam(ids);
            result = new ResultForm(true,"删除成功");
        }
        return result;
    }

    @Override
    public List<Map> findMapParamFromProd() throws Exception {
        String sql = "select * from ag_sup_map_param";
        return DBHelper.find(this.prod_db, sql, null);
    }

    @Override
    public Map findParamFromProdByLayerId(String layerId) throws Exception {
        String sql = "select * from ag_sup_map_param where id in (select distinct map_param_id from ag_sup_project where id in (select project_id from ag_sup_project_layer where layer_id=?))";
        List<Object> values = new ArrayList<Object>();
        values.add(layerId);
        return DBHelper.findFirst(this.prod_db, sql, values);
    }

    @Override
    public List<Map> findOriginFromProd(String paramId) throws Exception {
        String sql_origin = "select origin from ag_sup_layer where id in (select layer_id from ag_sup_project_layer where project_id in(select id from ag_sup_project where map_param_id= ?))";
        List<Object> values = new ArrayList<Object>();
        values.add(paramId);
        return DBHelper.find(this.prod_db, sql_origin, values);
    }

    @Override
    public String loadMapextent(InputStream in) {
        String mapextent = "";
        try {
            Document document = DocumentHelper.parseText(UploadUtil.getAttachmentString(in, 2048));
            List<Element> elements = document.getRootElement().elements();
            if (elements != null && elements.size() > 0) {
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("XMin")) {
                        mapextent += element.getText() + ",";
                    } else if (element.getName().equalsIgnoreCase("YMin")) {
                        mapextent += element.getText() + ",";
                    } else if (element.getName().equalsIgnoreCase("XMax")) {
                        mapextent += element.getText() + ",";
                    } else if (element.getName().equalsIgnoreCase("YMax")) {
                        mapextent += element.getText() + ",";
                    }
                }
                mapextent = mapextent.substring(0, mapextent.length() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapextent;
    }

    @Override
    public String loadResolution(InputStream in) {
        String resolution = "";
        try {
            Document document = DocumentHelper.parseText(UploadUtil.getAttachmentString(in, 2048));
            List<Element> elements = document.getRootElement().elements();
            if (elements != null && elements.size() > 0) {
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("TileCacheInfo")) {
                        List<Element> infoElements = element.elements();
                        if (infoElements != null && infoElements.size() > 0) {
                            for (Element infoElement : infoElements) {
                                if (infoElement.getName().equalsIgnoreCase("LODInfos")) {
                                    List<Element> lodInfosElements = infoElement.elements();
                                    if (lodInfosElements != null && lodInfosElements.size() > 0) {
                                        for (Element lodInfosElement : lodInfosElements) {
                                            if (lodInfosElement.getName().equalsIgnoreCase("LODInfo")) {
                                                List<Element> lodInfoElements = lodInfosElement.elements();
                                                if (lodInfoElements != null && lodInfoElements.size() > 0) {
                                                    for (Element lodInfoElement : lodInfoElements) {
                                                        if (lodInfoElement.getName().equalsIgnoreCase("Resolution")) {
                                                            resolution += lodInfoElement.getText() + ",";
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        resolution = resolution.substring(0, resolution.length() - 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resolution;
    }

    @Override
    public Map<String, String> readTileOrigin(InputStream in) {
        Map<String, String> map = new HashMap<String, String>();
        String tileOrigin = "";
//        String cutMapOrigin = "";
        try {
            Document document = DocumentHelper.parseText(UploadUtil.getAttachmentString(in, 2048));
            List<Element> elements = document.getRootElement().elements();
            if (elements != null && elements.size() > 0) {
                for (Element element : elements) {
                    if (element.getName().equalsIgnoreCase("TileCacheInfo")) {
                        List<Element> infoElements = element.elements();
                        if (infoElements != null && infoElements.size() > 0) {
                            for (Element infoElement : infoElements) {
//                                if (infoElement.getName().equalsIgnoreCase("SpatialReference")) {
//                                    List<Element> lodInfosElements = infoElement.elements();
//                                    if (lodInfosElements != null && lodInfosElements.size() > 0) {
//                                        for (Element lodInfosElement : lodInfosElements) {
//                                            if (lodInfosElement.getName().equalsIgnoreCase("XOrigin")) {
//                                                cutMapOrigin += lodInfosElement.getText() + ",";
//                                            }
//                                            if (lodInfosElement.getName().equalsIgnoreCase("YOrigin")) {
//                                                cutMapOrigin += lodInfosElement.getText();
//                                            }
//                                        }
//                                    }
//                                }
                                if (infoElement.getName().equalsIgnoreCase("TileOrigin")) {
                                    List<Element> lodInfosElements = infoElement.elements();
                                    if (lodInfosElements != null && lodInfosElements.size() > 0) {
                                        for (Element lodInfosElement : lodInfosElements) {
                                            if (lodInfosElement.getName().equalsIgnoreCase("x")) {
                                                tileOrigin += lodInfosElement.getText() + ",";
                                            }
                                            if (lodInfosElement.getName().equalsIgnoreCase("y")) {
                                                tileOrigin += lodInfosElement.getText();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
//            map.put("cutMapOrigin", cutMapOrigin);
            map.put("tileOrigin", tileOrigin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
