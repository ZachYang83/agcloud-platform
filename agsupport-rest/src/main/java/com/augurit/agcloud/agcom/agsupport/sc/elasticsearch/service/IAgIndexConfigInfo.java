package com.augurit.agcloud.agcom.agsupport.sc.elasticsearch.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgIndexConfigInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :9:49 2018/12/27
 * @Modified By:
 */
public interface IAgIndexConfigInfo {

    PageInfo<AgIndexConfigInfo> searchAgIndexConfigInfo(AgIndexConfigInfo agIndexConfigInfo, Page page)throws  Exception;

    AgIndexConfigInfo findById(String id) throws Exception;

    public AgIndexConfigInfo findByIndexName(String indexName) throws Exception;

    void updateAgIndexConfigInfo(AgIndexConfigInfo agIndexConfigInfo) throws Exception;

    void updateShowField(AgIndexConfigInfo showField)throws Exception;

    void saveAgIndexConfigInfo(AgIndexConfigInfo agIndexConfigInfo)throws Exception;

    void delById(String id)throws Exception;

    List<AgIndexConfigInfo> getAllShowField()throws Exception;
}
