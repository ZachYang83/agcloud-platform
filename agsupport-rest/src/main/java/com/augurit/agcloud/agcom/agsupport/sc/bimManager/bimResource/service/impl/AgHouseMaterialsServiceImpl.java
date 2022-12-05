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
        //查询条件
        if(!StringUtils.isEmpty(sourceId)){
            criteria.andSourceIdEqualTo(sourceId);
        }
        if(!StringUtils.isEmpty(floor)){
            criteria.andFloorEqualTo(floor);
        }
        if(!StringUtils.isEmpty(name)){
            criteria.andNameLike("%" + name + "%");
        }
        //是否需要查询范围数据，不需要的话直接查询数据库返回
        if(StringUtils.isEmpty(mapArray)){
            Page startPage = PageHelper.startPage(page);
            List<AgHouseMaterialsPoint> points = houseMaterialsPointMapper.selectByExample(example);
            //使用缓存需要序列化，重新组装返回值
            return getAgHouseMaterialsPointCustomPageInfo(startPage, points);
        }

        //-------------------------------------------------------------------------

        //需要查询该范围的数据id,然后重新查询
        List<String> paramIds = new ArrayList<>();
        List<AgHouseMaterialsPoint> list = houseMaterialsPointMapper.selectByExample(example);
        if(list != null && list.size() > 0){
            for(AgHouseMaterialsPoint materials : list){
                String coordinates = materials.getCoordinates();
                if(!StringUtils.isEmpty(coordinates)){
                    String[] coordinatesArr = coordinates.split(",");
                    //获取x，y，x，只要x， y
                    if(coordinatesArr != null && coordinatesArr.length >= 2){
                        //经度
                        String x = coordinatesArr[0];
                        //维度
                        String y = coordinatesArr[1];
                        //判断是否在多边形内
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
        //使用缓存需要序列化，重新组装返回值
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
        //用来清除缓存
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

                        //获取坐标
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

                        //获取楼层
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
        //查询所有的构件数据
        Page startPage = PageHelper.startPage(page);
        AgHouseMaterialsExample houseMaterialsExample = new AgHouseMaterialsExample();
        houseMaterialsExample.setOrderByClause(" create_time desc");
        List<AgHouseMaterials> agHouseMaterials = agHouseMaterialsMapper.selectByExample(houseMaterialsExample);
        if(agHouseMaterials == null && agHouseMaterials.size() == 0){
            return new PageInfo<>();
        }

        //返回值
        List<AgHouseMaterialsCustom> agHouseMaterialsCustoms = new ArrayList<>();

        //遍历所有的构件点
        for(AgHouseMaterials dbMaterials: agHouseMaterials){
            AgHouseMaterialsCustom custom = new AgHouseMaterialsCustom();
            BeanUtils.copyProperties(dbMaterials, custom);
            //设置默认数量
            custom.setTotal(0);
            //查询该构件的所有数据
            AgHouseMaterialsPointExample example = new AgHouseMaterialsPointExample();
            AgHouseMaterialsPointExample.Criteria criteria = example.createCriteria();
            criteria.andSourceIdEqualTo(dbMaterials.getId());
            List<AgHouseMaterialsPoint> agHouseMaterialsPoint = houseMaterialsPointMapper.selectByExample(example);
            //遍历数据
            if(agHouseMaterialsPoint != null && agHouseMaterialsPoint.size() > 0){
                //统计数量
                int countMaterials = 0;
                //是否有查询参数，如果没有，查询所有，否则要根据参数，统计数量
                if(!StringUtils.isEmpty(mapArray)){
                    for(AgHouseMaterialsPoint materials : agHouseMaterialsPoint){
                        String coordinates = materials.getCoordinates();
                        if(!StringUtils.isEmpty(coordinates)){
                            String[] coordinatesArr = coordinates.split(",");
                            //获取x，y，x，只要x， y
                            if(coordinatesArr != null && coordinatesArr.length >= 2){
                                //经度
                                String x = coordinatesArr[0];
                                //维度
                                String y = coordinatesArr[1];
                                //判断是否在多边形内
                                boolean inInBox = Map2DCheckInPolygon.isInPolygon(x, y, mapArray);
                                if(inInBox){
                                    countMaterials ++;
                                }
                            }
                        }
                    }
                    //赋值构件数量
                    custom.setTotal(countMaterials);
                }
                //是否有查询参数，如果没有，查询所有，否则要根据参数，统计数量
                if(StringUtils.isEmpty(mapArray)){
                    //赋值构件数量
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

