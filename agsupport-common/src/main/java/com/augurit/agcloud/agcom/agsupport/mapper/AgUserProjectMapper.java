package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgUserProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Augurit on 2017-04-19.
 */
@Mapper
public interface AgUserProjectMapper {
    void save(AgUserProject agRoleProject) throws Exception;

    AgUserProject findByUserIdAndProjectId(@Param("userId") String userId, @Param("projectId") String projectId)throws Exception;

    void delete(@Param("id") String id) throws Exception;

    List<AgUserProject> findListByProjectId(@Param("projectId") String projectId) throws Exception;

    List<AgUserProject> findListByUserId(@Param("userId") String userId) throws Exception;

}
