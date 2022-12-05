package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgIndexConfigInfo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :10:08 2018/12/27
 * @Modified By:
 */
@Mapper
public interface AgIndexConfigInfoMapper {

    List<AgIndexConfigInfo> findList(AgIndexConfigInfo agIndexConfigInfo)throws  Exception;

    AgIndexConfigInfo findById(String id) throws Exception;

    void save(AgIndexConfigInfo agIndexConfigInfo)throws Exception;

    void update(AgIndexConfigInfo agIndexConfigInfo)throws Exception;

    void updateShowField(AgIndexConfigInfo showFiled)throws Exception;

    void delById(String id)throws Exception;

    public AgIndexConfigInfo findByIndexName(String indexName) throws Exception;

    List<AgIndexConfigInfo> getAllShowField()throws Exception;

}
