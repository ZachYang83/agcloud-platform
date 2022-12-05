package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.impl;


import com.augurit.agcloud.agcom.agsupport.domain.Agcim3dentityResultCustom;
import com.augurit.agcloud.agcom.agsupport.domain.Agcim3dentityVbXCustom;
import com.augurit.agcloud.agcom.agsupport.domain.Agcim3dentityVbXCustomExample;
import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dproject;
import com.augurit.agcloud.agcom.agsupport.mapper.Agcim3dentityVbXCustomMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.IAgcim3dentityVbXService;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.service.IAgcim3dprojectService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: qinyg
 * @Date: 2020/10/14
 * @tips:
 */
@Service
public class Agcim3dentityVbXServiceImpl implements IAgcim3dentityVbXService {

    @Autowired
    private Agcim3dentityVbXCustomMapper agcim3dentityVbXCustomMapper;
    @Autowired
    private IAgcim3dprojectService agcim3dprojectService;


    @Override
    public Agcim3dentityResultCustom find(String tableName, String id, String objectid, String name, Long version, String infotype, String profession, String level, String catagory, String materialid, String categorypath, String projectname, String projectcode, Page page, String filterType) {

        if ("agcim3dproject".equals(tableName)) {
            Agcim3dproject project = new Agcim3dproject();
//            project.setProjectcode(projectcode);
//            project.setProjectname(projectname);
            project.setName(projectname);
            project.setId(id);
            PageInfo<Agcim3dproject> list = agcim3dprojectService.list(project, page);
            Agcim3dentityResultCustom resultCustom = new Agcim3dentityResultCustom(list.getList(), list.getTotal());
            return resultCustom;
        }

        Agcim3dentityVbXCustom param = new Agcim3dentityVbXCustom();
        param.setId(id);
        param.setObjectid(objectid);
        param.setName(name);
        param.setVersion(version);
        param.setInfotype(infotype);
        param.setProfession(profession);
        param.setLevel(level);
        param.setCatagory(catagory);
        param.setMaterialid(materialid);


        Agcim3dentityVbXCustomExample example = new Agcim3dentityVbXCustomExample();
        //agcim3dentity_via 表和agcim3dentity_vb1，agcim3dentity_vbx表结构一样，可使用相同的方式
        //用此种方式就是避免tableName的sql注入，多个直接复制
        if ("agcim3dentity_via".equals(tableName)) {
            return getAgcim3dentityResultCustom(tableName, page, filterType, param, example);
        }
        //是否匹配表名
        boolean tableIsMatch = false;
        //自动判断表名是否在此规则，目前支持1000个表名
        for(int i = 1; i <= 1000; i++){
            if(("agcim3dentity_vb" + i).equals(tableName)){
                tableIsMatch = true;
                break;
            }
        }
        if(!tableIsMatch){
            //不匹配直接返回树，避免sql注入
            return null;
        }
        //表名存在，直接查询数据返回
        return getAgcim3dentityResultCustom(tableName, page, filterType, param, example);
    }

    private Agcim3dentityResultCustom getAgcim3dentityResultCustom(String tableName, Page page, String filterType, Agcim3dentityVbXCustom param, Agcim3dentityVbXCustomExample example) {
        example.setTableName(tableName);
        example.setDistinct(true);
        getAgcim3dentityExample(example, param);
        Page startPage = PageHelper.startPage(page);
        List<Agcim3dentityVbXCustom> list = null;
        //filterType不为空，需要返回指定属性
        if (StringUtils.isEmpty(filterType)) {
            list = agcim3dentityVbXCustomMapper.selectAllByExample(example);
        } else {
            list = agcim3dentityVbXCustomMapper.selectDefineByExample(example);
        }
        Agcim3dentityResultCustom resultCustom = new Agcim3dentityResultCustom(list, startPage.getTotal());
        return resultCustom;
    }

    private void getAgcim3dentityExample(Agcim3dentityVbXCustomExample example, Agcim3dentityVbXCustom agcim) {
        Agcim3dentityVbXCustomExample.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(agcim.getId())){
            criteria.andIdEqualTo(agcim.getId());
        }
        if(!StringUtils.isEmpty(agcim.getObjectid())){
            criteria.andObjectidEqualTo(agcim.getObjectid());
        }
        if(!StringUtils.isEmpty(agcim.getName())){
            criteria.andNameEqualTo(agcim.getName());
        }
        if(!StringUtils.isEmpty(agcim.getVersion())){
            criteria.andVersionEqualTo(agcim.getVersion());
        }
        if(!StringUtils.isEmpty(agcim.getInfotype())){
            criteria.andInfotypeEqualTo(agcim.getInfotype());
        }
        if(!StringUtils.isEmpty(agcim.getProfession())){
            criteria.andProfessionEqualTo(agcim.getProfession());
        }
        //level支持in查询，参数多个用逗号分隔
        if(!StringUtils.isEmpty(agcim.getLevel())){
            List<String> levels = new ArrayList<>();
            for(String level: agcim.getLevel().split(",")){
                levels.add(level);
            }
            criteria.andLevelIn(levels);
        }
        //catagory支持in查询，参数多个用逗号分隔
        if(!StringUtils.isEmpty(agcim.getCatagory())){
            List<String> cagatorys = new ArrayList<>();
            for(String catagory: agcim.getCatagory().split(",")){
                cagatorys.add(catagory);
            }
            criteria.andCatagoryIn(cagatorys);
        }
        if(!StringUtils.isEmpty(agcim.getMaterialid())){
            criteria.andMaterialidEqualTo(agcim.getMaterialid());
        }
        if(!StringUtils.isEmpty(agcim.getElementattributes())){
            criteria.andElementattributesEqualTo(agcim.getElementattributes());
        }
        if(!StringUtils.isEmpty(agcim.getGeometry())){
            criteria.andGeometryEqualTo(agcim.getGeometry());
        }
        if(!StringUtils.isEmpty(agcim.getTopologyelements())){
            criteria.andTopologyelementsEqualTo(agcim.getTopologyelements());
        }
        if(!StringUtils.isEmpty(agcim.getBoundingbox())){
            criteria.andBoundingboxEqualTo(agcim.getBoundingbox());
        }
    }

    @Override
    public Object statistics(String tableName, String name, String level, String catagory) {

        if ("agcim3dentity_via".equals(tableName)) {
            return getAgcim3dentityStatistics(tableName, name, level, catagory);
        }
        //是否匹配表名
        boolean tableIsMatch = false;
        //自动判断表名是否在此规则，目前支持1000个表名
        for(int i = 1; i <= 1000; i++){
            if(("agcim3dentity_vb" + i).equals(tableName)){
                tableIsMatch = true;
                break;
            }
        }
        if(!tableIsMatch){
            //不匹配直接返回树，避免sql注入
            return null;
        }
        return getAgcim3dentityStatistics(tableName, name, level, catagory);
    }

    private Object getAgcim3dentityStatistics(String tableName, String name, String level, String catagory) {
        Map<String, Object> map = new HashMap<>();
        if (!StringUtils.isEmpty(name)) {
            StringBuffer sb = new StringBuffer();
            sb.append("select name as name, count(name) as value  from " + tableName + " group by name");
            List<Object> list = agcim3dentityVbXCustomMapper.selectBySql(sb.toString());
            map.put("name", list);
        }
        if (!StringUtils.isEmpty(level)) {
            StringBuffer sb = new StringBuffer();
            sb.append("select level as name, count(level) as value  from " + tableName + " group by level");
            List<Object> list = agcim3dentityVbXCustomMapper.selectBySql(sb.toString());
            map.put("level", list);
        }
        if (!StringUtils.isEmpty(catagory)) {
            StringBuffer sb = new StringBuffer();
            sb.append("select catagory as name, count(catagory) as value  from " + tableName + " group by catagory");
            List<Object> list = agcim3dentityVbXCustomMapper.selectBySql(sb.toString());
            map.put("catagory", list);
        }
        return map;
    }

    @Override
    public Map getGlb(String tableName, String id){
        if ("agcim3dentity_via".equals(tableName)) {
            return get(tableName, id);
        }
        //是否匹配表名
        boolean tableIsMatch = false;
        //自动判断表名是否在此规则，目前支持1000个表名
        for(int i = 1; i <= 1000; i++){
            if(("agcim3dentity_vb" + i).equals(tableName)){
                tableIsMatch = true;
                break;
            }
        }
        if(!tableIsMatch){
            //不匹配直接返回树，避免sql注入
            return null;
        }
        return get(tableName, id);
    }

    private Map get(String tableName, String id) {
        String sql = "select glb from " + tableName + " where id='" + id + "'";
        List<Object> objects = agcim3dentityVbXCustomMapper.selectBySql(sql);
        if(objects != null && objects.size() > 0){
            return (Map) objects.get(0);
        }
        return null;
    }
}
