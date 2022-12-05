package com.augurit.agcloud.agcom.agsupport.sc.statistics.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgDataOverview;
import com.augurit.agcloud.agcom.agsupport.domain.AgDataSubject;
import com.augurit.agcloud.agcom.agsupport.domain.AgDic;
import com.augurit.agcloud.agcom.agsupport.mapper.AgDataOverviewMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgDataSubjectMapper;
import com.augurit.agcloud.agcom.agsupport.sc.dic.service.IAgDic;
import com.augurit.agcloud.agcom.agsupport.sc.statistics.service.IAgDataOverviewService;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhangmingyang
 * @Description: 数据接口实现类
 * @date 2019-08-22 18:35
 */
@Service
public class AgDataOverviewServiceImpl implements IAgDataOverviewService {
    @Autowired
    private AgDataOverviewMapper agDataOverviewMapper;
    @Autowired
    private AgDataSubjectMapper agDataSubjectMapper;
    @Autowired
    private IAgDic agDic;
    @Override
    public int deleteById(String id) {
        return agDataOverviewMapper.deleteById(id);
    }
    @Override
    public void deleteBySubjectIds(String[] ids) {
        agDataOverviewMapper.deleteBySubjectIds(ids);
    }
    @Override
    public AgDataOverview selectById(String id) {
        return agDataOverviewMapper.selectById(id);
    }

    @Override
    public int insert(AgDataOverview record) {
        return agDataOverviewMapper.insert(record);
    }

    @Override
    public int insertBatch(List<AgDataOverview> list) throws Exception {
        return agDataOverviewMapper.insertBatch(list);
    }

    @Override
    public int updateAgDataOverview(AgDataOverview record) {
        return agDataOverviewMapper.updateAgDataOverview(record);
    }

    @Override
    public PageInfo<AgDataOverview> findAll(AgDataOverview agDataOverview, Page page) {
        AgDataSubject agDataSubject = agDataSubjectMapper.selectById(agDataOverview.getSubjectId());
        agDataOverview.setXpath(agDataSubject.getXpath());
        PageHelper.startPage(page);
        List<AgDataOverview> list = agDataOverviewMapper.findAll(agDataOverview);
        try {
            Map<String ,String> layerTypeMap = getDataType();
            Map<String ,String> datasourceTypeMap = getDatasourceType();
            for (AgDataOverview agData : list){
                String dataType = agData.getDataType();
                String datasourceType = agData.getDatasourceType();
                agData.setDataTypeCn(layerTypeMap.get(dataType));
                agData.setDatasourceTypeCn(datasourceTypeMap.get(datasourceType));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return new PageInfo<>(list);
    }

    private Map getDataType(){
        List<AgDic> agDicList = null;
        try {
            agDicList = agDic.getAgDicByTypeCode("A201");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map map = new HashMap();
        for (AgDic agDic : agDicList){
            map.put(agDic.getCode(),agDic.getName());
        }
        return map;
    }

    private Map getDatasourceType(){
        List<AgDic> agDicList = null;
        try {
            agDicList = agDic.getAgDicByTypeCode("A202");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map map = new HashMap();
        for (AgDic agDic : agDicList){
            map.put(agDic.getCode(),agDic.getName());
        }
        return map;
    }
}
