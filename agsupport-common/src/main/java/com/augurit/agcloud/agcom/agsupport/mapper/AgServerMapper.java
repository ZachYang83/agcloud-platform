package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgServer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangmingyang
 * @Description: 服务器管理接口
 * @date 2018-11-12
 */
@Mapper
public interface AgServerMapper {
    int deleteServerById(String id) throws Exception;

    int insert(AgServer record) throws Exception;

    AgServer selectServerById(String id) throws Exception;

    int updateServer(AgServer record) throws Exception;

    List<AgServer> findList(AgServer agServer) throws Exception;
    AgServer selectServerBySiteName(String siteName) throws Exception;

    void changeState(@Param("id") String id,@Param("state") String state) throws Exception;
}