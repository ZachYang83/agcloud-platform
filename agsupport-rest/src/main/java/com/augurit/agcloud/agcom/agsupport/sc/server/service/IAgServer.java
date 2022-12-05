package com.augurit.agcloud.agcom.agsupport.sc.server.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgServer;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author zhangmingyang
 * @Description: TODO
 * @date 2018-11-12 15:56
 */
public interface IAgServer {
    int deleteServerById(String id) throws Exception;

    int insert(AgServer record) throws Exception;

    AgServer selectServerById(String id) throws Exception;

    int updateServer(AgServer record) throws Exception;

    PageInfo<AgServer> findList(AgServer agServer, Page page) throws Exception;

    void save(AgServer agServer) throws Exception;

    List<AgServer> findListByState(AgServer agServer) throws Exception;
}
