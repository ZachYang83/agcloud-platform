package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgTag;
import com.augurit.agcloud.agcom.agsupport.domain.AgTagCatalog;
import com.augurit.agcloud.agcom.agsupport.domain.AgTagLayer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AgTagMapper {

  void addTagCatalog(AgTagCatalog agTagCatalog) throws Exception;

  List<AgTagCatalog> getAllTagCatalogs() throws Exception;

  void addTag(AgTag agTag) throws Exception;

  List<AgTag> getAllTags() throws Exception;

  void deleteTagCatalog(@Param("id") String id) throws Exception;

  void deleteTag(@Param("id") String id) throws Exception;

  void deleteTagBatch(@Param("layerIds") List<String> layerIds) throws Exception;

  void updateTag(AgTag tag) throws Exception;

  void updateTagCatalog(AgTagCatalog tagCatalog) throws Exception;

  AgTag getTagById(String id) throws Exception;

  AgTagCatalog getTagCatalogById(@Param("id") String id);

  List<AgTag>  getAllTagByCatalogId(@Param("id") String id);

  List<AgLayer> getOpenLayerwithoutTag(@Param("serviceName") String serviceName, @Param("tagId") String tagId) throws Exception;

  void insertTagLayer(List<AgTagLayer> tagLayers) throws Exception;

  List<AgTagLayer> getOpenLayerwithTag(@Param("tagId") String tagId,
                                       @Param("catalogId") String catalogId,
                                       @Param("serviceName") String serviceName) throws Exception;

  void removeTayLayerByLayerIds(@Param("layerIds") String[] layerIds) throws Exception;

  List<AgTagLayer> countTagLayerApplyNum() throws Exception;

  List<AgTagLayer> wetherCanDeleteTag(@Param("id") String id, @Param("isTag") String isTag) throws Exception;

}
