package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.agcom.agsupport.domain.AgHouseMaterialsCustom;
import com.augurit.agcloud.agcom.agsupport.domain.AgHouseMaterialsPointCustom;
import com.augurit.agcloud.agcom.agsupport.domain.auto.*;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgHouseMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgHouseMaterialsMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgHouseMaterialsPointMapper;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service.IAgHouseMaterialsService;
import com.augurit.agcloud.agcom.agsupport.util.Map2DCheckInPolygon;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Author: qinyg
 * @Date: 2020/10/21
 * @tips:
 */
@Service
public class AgHouseMaterialsServiceImpl implements IAgHouseMaterialsService {
    @Autowired
    private AgHouseMaterialsPointMapper houseMaterialsPointMapper;
    @Autowired
    private AgHouseMaterialsMapper agHouseMaterialsMapper;

    @Override
    @Cacheable(value= "checkInPolygonCache", key = "#cacheableKey")
    public PageInfo<AgHouseMaterialsPointCustom> findMaterialsPoint(String sourceId, String type, String name, String floor, Page page, String mapArray, String cacheableKey) {
        AgHouseMaterialsPointExample example = new AgHouseMaterialsPointExample();
        example.setOrderByClause(" create_time desc");
        AgHouseMaterialsPointExample.Criteria criteria = example.createCriteria();
        //????????????
        if(!StringUtils.isEmpty(sourceId)){
            criteria.andSourceIdEqualTo(sourceId);
        }
        if(!StringUtils.isEmpty(floor)){
            criteria.andFloorEqualTo(floor);
        }
        if(!StringUtils.isEmpty(name)){
            criteria.andNameLike("%" + name + "%");
        }
        //???????????????????????????????????????????????????????????????????????????
        if(StringUtils.isEmpty(mapArray)){
            Page startPage = PageHelper.startPage(page);
            List<AgHouseMaterialsPoint> points = houseMaterialsPointMapper.selectByExample(example);
            //???????????????????????????????????????????????????
            return getAgHouseMaterialsPointCustomPageInfo(startPage, points);
        }

        //-------------------------------------------------------------------------

        //??????????????????????????????id,??????????????????
        List<String> paramIds = new ArrayList<>();
        List<AgHouseMaterialsPoint> list = houseMaterialsPointMapper.selectByExample(example);
        if(list != null && list.size() > 0){
            for(AgHouseMaterialsPoint materials : list){
                String coordinates = materials.getCoordinates();
                if(!StringUtils.isEmpty(coordinates)){
                    String[] coordinatesArr = coordinates.split(",");
                    //??????x???y???x?????????x??? y
                    if(coordinatesArr != null && coordinatesArr.length >= 2){
                        //??????
                        String x = coordinatesArr[0];
                        //??????
                        String y = coordinatesArr[1];
                        //???????????????????????????
                        boolean inInBox = Map2DCheckInPolygon.isInPolygon(x, y, mapArray);
                        if(inInBox){
                            paramIds.add(materials.getId());
                        }
                    }
                }
            }
        }
        example.clear();
        example.setOrderByClause(" create_time desc");
        AgHouseMaterialsPointExample.Criteria exampleCriteria = example.createCriteria();
        if(paramIds.size() != 0){
            exampleCriteria.andIdIn(paramIds);
        }
        Page startPage = PageHelper.startPage(page);
        List<AgHouseMaterialsPoint> points = houseMaterialsPointMapper.selectByExample(example);
        return getAgHouseMaterialsPointCustomPageInfo(startPage, points);
    }

    private PageInfo<AgHouseMaterialsPointCustom> getAgHouseMaterialsPointCustomPageInfo(Page startPage, List<AgHouseMaterialsPoint> points) {
        //???????????????????????????????????????????????????
        List<AgHouseMaterialsPointCustom> rsList = new ArrayList<>();
        if (points != null && points.size() > 0) {
            for (AgHouseMaterialsPoint point : points) {
                AgHouseMaterialsPointCustom custom = new AgHouseMaterialsPointCustom();
                BeanUtils.copyProperties(point, custom);
                rsList.add(custom);
            }
        }
        PageInfo<AgHouseMaterialsPointCustom> customPageInfo = new PageInfo<>(rsList);
        customPageInfo.setTotal(startPage.getTotal());
        return customPageInfo;
    }

    @Override
    @Transactional
    @CacheEvict(value= "checkInPolygonCache", allEntries = true)
    public void addMaterialsPoint(AgHouseMaterialsPoint entity) {
        entity.setId(UUID.randomUUID().toString());
        entity.setCreateTime(new Date());
        houseMaterialsPointMapper.insert(entity);
    }

    @Override
    @Transactional
    @CacheEvict(value= "checkInPolygonCache", allEntries = true)
    public void deleteMaterialsPoint(String ids) {
        for(String id: ids.split(",")){
            houseMaterialsPointMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    @CacheEvict(value= "checkInPolygonCache", allEntries = true)
    public void importMaterialsPointJsonStr(String jsonStr, AgHouseMaterialsPoint entity) {
        //??????????????????
        if("123".equals(entity.getId())){
            return ;
        }
        JSONObject json = JSONObject.parseObject(jsonStr);
        if(json != null){
            JSONArray features = json.getJSONArray("features");
            if(features != null){
                for(int i = 0; i <features.size(); i++){
                    JSONObject jsonObject = features.getJSONObject(i);
                    if(jsonObject != null){
                        AgHouseMaterialsPoint materials = new AgHouseMaterialsPoint();
                        materials.setId(UUID.randomUUID().toString());
                        materials.setCreateTime(new Date());
                        materials.setName(entity.getName());
                        materials.setSourceId(entity.getSourceId());

                        //????????????
                        JSONObject geometry = jsonObject.getJSONObject("geometry");
                        JSONArray coordinates = geometry.getJSONArray("coordinates");
                        if(coordinates != null && coordinates.size() >0){
                            String cord = "";
                            for(int j = 0; j < coordinates.size() ; j ++){
                                cord += coordinates.get(j) + ",";
                            }
                            cord = cord.substring(0, cord.length() - 1);
                            materials.setCoordinates(cord);
                        }

                        //????????????
                        JSONObject properties = jsonObject.getJSONObject("properties");
                        if(properties != null){
                            String floor = properties.getString("floor");
                            materials.setFloor(floor);
                        }
                        houseMaterialsPointMapper.insert(materials);
                    }
                }
            }
        }
    }

    @Override
    @Cacheable(value= "checkInPolygonCache", key = "#cacheableKey")
    public PageInfo<AgHouseMaterialsCustom> statisticsMaterialsisInPolygon(Page page, String mapArray, String cacheableKey) {
        //???????????????????????????
        Page startPage = PageHelper.startPage(page);
        AgHouseMaterialsExample houseMaterialsExample = new AgHouseMaterialsExample();
        houseMaterialsExample.setOrderByClause(" create_time desc");
        List<AgHouseMaterials> agHouseMaterials = agHouseMaterialsMapper.selectByExample(houseMaterialsExample);
        if(agHouseMaterials == null && agHouseMaterials.size() == 0){
            return new PageInfo<>();
        }

        //?????????
        List<AgHouseMaterialsCustom> agHouseMaterialsCustoms = new ArrayList<>();

        //????????????????????????
        for(AgHouseMaterials dbMaterials: agHouseMaterials){
            AgHouseMaterialsCustom custom = new AgHouseMaterialsCustom();
            BeanUtils.copyProperties(dbMaterials, custom);
            //??????????????????
            custom.setTotal(0);
            //??????????????????????????????
            AgHouseMaterialsPointExample example = new AgHouseMaterialsPointExample();
            AgHouseMaterialsPointExample.Criteria criteria = example.createCriteria();
            criteria.andSourceIdEqualTo(dbMaterials.getId());
            List<AgHouseMaterialsPoint> agHouseMaterialsPoint = houseMaterialsPointMapper.selectByExample(example);
            //????????????
            if(agHouseMaterialsPoint != null && agHouseMaterialsPoint.size() > 0){
                //????????????
                int countMaterials = 0;
                //??????????????????????????????????????????????????????????????????????????????????????????
                if(!StringUtils.isEmpty(mapArray)){
                    for(AgHouseMaterialsPoint materials : agHouseMaterialsPoint){
                        String coordinates = materials.getCoordinates();
                        if(!StringUtils.isEmpty(coordinates)){
                            String[] coordinatesArr = coordinates.split(",");
                            //??????x???y???x?????????x??? y
                            if(coordinatesArr != null && coordinatesArr.length >= 2){
                                //??????
                                String x = coordinatesArr[0];
                                //??????
                                String y = coordinatesArr[1];
                                //???????????????????????????
                                boolean inInBox = Map2DCheckInPolygon.isInPolygon(x, y, mapArray);
                                if(inInBox){
                                    countMaterials ++;
                                }
                            }
                        }
                    }
                    //??????????????????
                    custom.setTotal(countMaterials);
                }
                //??????????????????????????????????????????????????????????????????????????????????????????
                if(StringUtils.isEmpty(mapArray)){
                    //??????????????????
                    custom.setTotal(agHouseMaterialsPoint.size());
                }
            }
            agHouseMaterialsCustoms.add(custom);
        }
        PageInfo<AgHouseMaterialsCustom> agHouseMaterialsPageInfo = new PageInfo<>(agHouseMaterialsCustoms);
        agHouseMaterialsPageInfo.setTotal(startPage.getTotal());
        return agHouseMaterialsPageInfo;
    }
}

