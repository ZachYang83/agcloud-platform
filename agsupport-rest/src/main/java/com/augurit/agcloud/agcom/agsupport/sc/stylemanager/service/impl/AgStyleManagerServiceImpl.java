package com.augurit.agcloud.agcom.agsupport.sc.stylemanager.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgStyleManager;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgStyleManagerExample;
import com.augurit.agcloud.agcom.agsupport.mapper.AgLayerMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.auto.AgStyleManagerMapper;
import com.augurit.agcloud.agcom.agsupport.sc.stylemanager.service.IAgStyleManagerService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/07
 * @Description:
 */
@Service
public class AgStyleManagerServiceImpl implements IAgStyleManagerService {

    @Autowired
    private AgStyleManagerMapper styleManagerMapper;
    @Autowired
    private AgLayerMapper layerMapper;

    @Override
    public AgStyleManager findById(String id) {
        return styleManagerMapper.selectByPrimaryKey(id);
    }


    @Override
    public PageInfo<AgStyleManager> findStyleList(String name, String layerType, Page page) {
        AgStyleManagerExample example = new AgStyleManagerExample();
        example.setOrderByClause("create_time asc");
        AgStyleManagerExample.Criteria criteria = example.createCriteria();
        //参数封装，如果不为null，需要添加条件
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (!StringUtils.isEmpty(layerType)) {
            criteria.andLayerTypeEqualTo(layerType);
        }
        PageHelper.startPage(page);
        List<AgStyleManager> agStyleManagers = styleManagerMapper.selectByExample(example);
        return new PageInfo<AgStyleManager>(agStyleManagers);
    }

    @Override
    @Transactional
    public void saveStyle(AgStyleManager styleManager) {
        // 保存样式信息
        styleManager.setCreateTime(new Date());
        styleManager.setId(UUID.randomUUID().toString());
        styleManagerMapper.insert(styleManager);
    }

    @Override
    @Transactional
    public void deleteStyle(String id) {
        // 1. 先删除图层关联的样式id
        // 1.1 查询样式id关联的所有图层
        List<AgLayer> layers = layerMapper.findByStyleManagerId(id);
        // 1.2 删除样式
        styleManagerMapper.deleteByPrimaryKey(id);
        // 2.2 遍历图层集合，清空图层关联改样式id数据
        layers.forEach(layer -> {
            layer.setStyleManagerId("");
            try {
                layerMapper.update(layer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    @Transactional
    public void deleteStyleBatch(String ids) {
        if (!StringUtils.isEmpty(ids)) {
            String[] idArrays = ids.split(",");
            if (idArrays != null && idArrays.length > 0) {
                for (String id : idArrays) {
                    this.deleteStyle(id);
                }
            }
        }
    }


    /**
     *
     * @Author: libc
     * @Date: 2020/10/13 10:26
     * @tips:
     * @param styleManager 样式对象
     * @param paramType 修改类型
     * @param layerId 图层id
     * @return
     */
    @Override
    @Transactional
    public void updateStyle(AgStyleManager styleManager, String paramType, String layerId) throws Exception {
        // 1. 修改样式信息
        styleManager.setModifyTime(new Date());
        styleManager.setCreateTime(null);
        styleManagerMapper.updateByPrimaryKeySelective(styleManager);
        // 判断是否需要绑定图层
        if ("2".equals(paramType)){
            // 2. 绑定图层
            AgLayer layer = layerMapper.findByLayerId(layerId);
            layer.setStyleManagerId(styleManager.getId());
            layerMapper.update(layer);
        }
    }
}
