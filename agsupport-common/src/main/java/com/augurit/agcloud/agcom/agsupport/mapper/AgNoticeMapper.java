package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgNotice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by czh on 2018-05-16.
 */
@Mapper
public interface AgNoticeMapper {

    void save(AgNotice agNotice) throws Exception;

    void update(AgNotice agNotice) throws Exception;

    void delete(String id) throws Exception;

    AgNotice getNotice(String id) throws Exception;

    List<AgNotice> findList(@Param("userId") String userId) throws Exception;
}
