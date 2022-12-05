package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgDir;
import com.augurit.agcloud.agcom.agsupport.domain.AgUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Augurit on 2017-04-19.
 */
@Mapper
public interface AgDirMapper {

    List<AgDir> findAll() throws Exception;

    List<AgDir> findAllDirSJHC() throws Exception;

    List<AgDir> findSecond() throws Exception;

    List<AgDir> findByIds(@Param("ids") String[] ids) throws Exception;

    List<AgDir> findByXpath(@Param("xpath") String xpath) throws Exception;

    /*
    * 根据目录的xpath和当前用户ID判断目录是否已授权：判断没有权限的标准是目录及子目录下没有任何图层
    * */
    Boolean isAuthorizedToDir(@Param("userId") String userId,@Param("xpath") String xpath ) throws Exception;

    /*
     * 根据目录的xpath和当前用户ID判断目录是否已授权：判断没有权限的标准是目录及子目录下没有任何非底图图层
     * */
    Boolean isAuthorizedDirContainNotBaseLayer(@Param("userId") String userId,@Param("xpath") String xpath ) throws Exception;

    List<AgDir> findByUserId(@Param("userId") String userId) throws Exception;

    AgDir findAgDirByXpath(@Param("xpath") String xpath) throws Exception;

    List<AgDir> findByUsers(@Param("userList") List<AgUser> userList) throws Exception;

    List<AgDir> findByVector() throws Exception;

    List<AgDir> findByDirLayer() throws Exception;

    AgDir findById(@Param("id") String id) throws Exception;

    AgDir findTopDir() throws  Exception;

    String getOrder(@Param("pid") String pid) throws Exception;

    List<AgDir> getChildrenByParentId(@Param("pid") String pid) throws Exception;

    void save(AgDir agDir) throws Exception;

    void update(AgDir agDir) throws Exception;

    void updateBatch(List<AgDir> list) throws Exception;

    void delete(@Param("id") String id) throws Exception;

    void deleteByXpath(@Param("xpath") String xpath) throws Exception;
}
