package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgProjectdir;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Augurit on 2017-04-19.
 */
@Mapper
public interface AgProjectdirMapper {

    List<AgProjectdir> getProjectName() throws Exception;

    List<AgProjectdir> findAll() throws Exception;

    AgProjectdir findById(@Param("id") String id) throws Exception;

    List<AgProjectdir> findByPid(@Param("pid") String pid) throws Exception;

    List<AgProjectdir> findByXpath(@Param("xpath") String xpath) throws Exception;

    List<AgProjectdir> findProjectByXpath(@Param("xpath") String xpath) throws Exception;

    List<AgProjectdir> findListByUser(AgUser user) throws Exception;

    Integer getOrder(@Param("pid") String pid) throws Exception;

    void save(AgProjectdir agProjectdir) throws Exception;

    void update(AgProjectdir agProjectdir) throws Exception;

    void delete(@Param("id") String id) throws Exception;

    void deleteByXpath(@Param("xpath") String xpath) throws Exception;

}
