package com.augurit.agcloud.agcom.agsupport.sc.tag.service.impl;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgTag;
import com.augurit.agcloud.agcom.agsupport.domain.AgTagCatalog;
import com.augurit.agcloud.agcom.agsupport.domain.AgTagLayer;
import com.augurit.agcloud.agcom.agsupport.mapper.AgTagMapper;
import com.augurit.agcloud.agcom.agsupport.sc.tag.service.IAgTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class AgTagImpl implements IAgTag {

    @Autowired
    private AgTagMapper tagMapper;

    @Override
    public void addTagCatalog(AgTagCatalog agTagCatalog) throws Exception{
        tagMapper.addTagCatalog(agTagCatalog);
    }
    @Override
    public List<AgTagCatalog> getAllTagCatalogs() throws Exception {
       return tagMapper.getAllTagCatalogs();
    }

    @Override
    public void addTag(AgTag tag) throws Exception {
        tagMapper.addTag(tag);
    }

    @Override
    public List<AgTag> getAllTags() throws Exception{
       return tagMapper.getAllTags();
    }

    @Override
    public void deleteTagCatalog(String id) throws Exception{
        tagMapper.deleteTagCatalog(id);
    }

    @Override
    public void deleteTag(String id) throws Exception{
        tagMapper.deleteTag(id);
    }

    @Override
    public void updateTag(AgTag tag) throws Exception{
        tagMapper.updateTag(tag);
    }

    @Override
    public void updateTagCatalog(AgTagCatalog tagCatalog) throws Exception{
        tagMapper.updateTagCatalog(tagCatalog);
    }

    @Override
    public AgTag getTagById(String id) throws Exception{
       return tagMapper.getTagById(id);
    }

    @Override
    public AgTagCatalog getTagCatalogById(String id) {
        return tagMapper.getTagCatalogById(id);
    }

    @Override
    public List<AgTag> getAllTagByCatalogId(String id) {
        return tagMapper.getAllTagByCatalogId(id);
    }

    @Override
    public List<AgLayer> getOpenLayerwithoutTag(String serviceName, String tagId) throws Exception {
        return tagMapper.getOpenLayerwithoutTag(serviceName, tagId);
    }

    @Override
    public void insertTagLayer(List<AgTagLayer> tagLayers) throws Exception {
        tagMapper.insertTagLayer(tagLayers);
    }

//    @Override
//    public void insertTagLayer(String tagId, String layerIds) throws Exception {
//        tagMapper.insertTagLayer(tagId, layerIds);
//    }

    @Override
    public List<AgTagLayer> getOpenLayerwithTag(String tagId, String catalogId, String serviceName) throws Exception{


        return  tagMapper.getOpenLayerwithTag(tagId,catalogId,serviceName);//
    }

    @Override
    public void removeTayLayerByLayerIds(String layerIds) throws Exception{
        tagMapper.removeTayLayerByLayerIds(layerIds.split(","));
    }


    @Override
    public List<AgTagLayer> countTagLayerApplyNum() throws Exception {
       return tagMapper.countTagLayerApplyNum();
    }

    @Override
    public int wetherCanDeleteTag(String id, String isTag)  throws Exception {
       List<AgTagLayer> tagLayers = tagMapper.wetherCanDeleteTag(id, isTag);
        return tagLayers.size();
    }
}












