package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimProject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * bim项目管理mapper
 * Created by fanghh on 2019/12/5.
 */
@Mapper
public interface AgBimProjectMapper {

    void save(AgBimProject project);

    void update(AgBimProject project);

    int delete(String id);

    List<AgBimProject> findAll();

    AgBimProject find(String id);

    List<AgBimProject> findByParentId(String projectId);

    List<AgBimProject> findParentIsNull();

}
