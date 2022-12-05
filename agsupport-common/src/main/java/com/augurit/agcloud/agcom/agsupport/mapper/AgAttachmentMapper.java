package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgAttachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by caokp on 2017-08-28.
 */
@Mapper
public interface AgAttachmentMapper {

    AgAttachment findById(@Param("id") String id) throws Exception;

    List<AgAttachment> findList(AgAttachment agAttachment) throws Exception;

    void save(AgAttachment agAttachment) throws Exception;

    void delete(@Param("id") String id) throws Exception;
}
