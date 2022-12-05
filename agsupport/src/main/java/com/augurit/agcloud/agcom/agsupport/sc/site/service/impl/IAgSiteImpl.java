package com.augurit.agcloud.agcom.agsupport.sc.site.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgSite;
import com.augurit.agcloud.agcom.agsupport.mapper.AgLayerMapper;
import com.augurit.agcloud.agcom.agsupport.mapper.AgSiteMapper;
import com.augurit.agcloud.agcom.agsupport.sc.site.service.IAgSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Augurit on 2017-05-08.
 */
@Service
public class IAgSiteImpl implements IAgSite {

    @Autowired
    private AgSiteMapper agSiteMapper;
    @Autowired
    private AgLayerMapper agLayerMapper;

    @Override
    public List<Map> getregis() throws Exception{
        List<AgLayer> list = agLayerMapper.findAll();
        List<Map> feilds = new ArrayList<>();
        for(AgLayer agLayer : list){
            Map item = new HashMap();
            item.put("url",agLayer.getUrl());
            item.put("layer_table",agLayer.getLayerTable());
            item.put("layer_type",agLayer.getLayerType());
            item.put("name_cn",agLayer.getNameCn());
        }
        return feilds;
    }
    @Override
    public AgSite getOrgserbyid(String id) throws Exception {

        return agSiteMapper.getOrgserbyid(id);
    }
    @Override
    public void deleteOrgsById(String id) throws Exception {

        agSiteMapper.deleteOrgsById(id);
    }

    @Override
    public void updateAgOrgs(AgSite AgOrgs) throws Exception {
        if (AgOrgs == null) return;
        agSiteMapper.updateAgOrgs(AgOrgs);
    }
    @Override
    public void insetAgOrgs(AgSite AgOrgs) throws Exception {
        if (AgOrgs == null) return;
        agSiteMapper.insetAgOrgs(AgOrgs);
    }
    @Override
    public String getMaxid() throws Exception {
        String Maxid= agSiteMapper.getMaxid();
        return Maxid;
    }
    @Override
    public List<AgSite> getSiteName(String serType) throws Exception {
        List<AgSite> list= agSiteMapper.getSiteName(serType);
        return list;
    }
    @Override
    public AgSite getSite(String serType, String serAlias) throws Exception {
        AgSite AgOrgs= agSiteMapper.getSite(serType,serAlias);
        return AgOrgs;
    }

    @Override
    public List<AgSite> getAllSite() throws Exception {
        return agSiteMapper.getAllSite();
    }
}
