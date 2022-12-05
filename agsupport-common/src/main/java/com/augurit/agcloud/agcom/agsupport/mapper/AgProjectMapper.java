package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017-12-29.
 */
@Mapper
public interface AgProjectMapper {

    List<AgLayer> findLayerByNameCn(@Param("nameCns") List nameCns) throws Exception;

    List<AgLayer> findLayerByDirLayerId(@Param("dirLayerId") List dirLayerId, @Param("name") String name) throws Exception;

    AgLayer findLayerByLayerId(@Param("dirLayerId") String dirLayerId, @Param("name") String name) throws Exception;

    List<AgLayer> findLayerByRoot(@Param("dirLayerId") List dirLayerId, @Param("userid") String userid) throws Exception;

    List<AgRole> findRoleByIds(@Param("ids") List ids, @Param("name") String name) throws Exception;

    AgRole findRoleById(@Param("id") Object id, @Param("name") String name) throws Exception;

    List<AgRole> findRoleByLoginName(@Param("loginName") String loginName) throws Exception;

    public Integer getProjectNextOrder() throws Exception;

}
