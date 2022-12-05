package com.augurit.agcloud.agcom.agsupport.sc.stylemanager.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgStyleManager;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgStyleManagerExample;
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
    public List<AgStyleManager> findAllStyle() {
        AgStyleManagerExample example = new AgStyleManagerExample();
        example.setOrderByClause("create_time asc");
        AgStyleManagerExample.Criteria criteria = example.createCriteria();
        List<AgStyleManager> agStyleManagers = styleManagerMapper.selectByExample(example);
        return agStyleManagers;
    }

    @Override
    @Transactional
    public void saveStyle(AgStyleManager styleManager) {
        styleManager.setCreateTime(new Date());
        styleManager.setId(UUID.randomUUID().toString());
        styleManagerMapper.insert(styleManager);
    }

    @Override
    @Transactional
    public void deleteStyle(String id) {
        styleManagerMapper.deleteByPrimaryKey(id);
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

    @Override
    @Transactional
    public void updateStyle(AgStyleManager styleManager) {
        styleManager.setModifyTime(new Date());
        styleManager.setCreateTime(null);
        styleManagerMapper.updateByPrimaryKeySelective(styleManager);
    }

    @Override
    public AgStyleManager findById(String id) {
        return styleManagerMapper.selectByPrimaryKey(id);
    }
}
