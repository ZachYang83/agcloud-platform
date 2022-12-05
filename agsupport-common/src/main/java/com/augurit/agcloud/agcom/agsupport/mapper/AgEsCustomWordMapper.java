package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgEsCustomWord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :16:56 2018/12/21
 * @Modified By:
 */

@Mapper
public interface AgEsCustomWordMapper {

    List<AgEsCustomWord> findAll() throws Exception;

    AgEsCustomWord findCustom(String customWord) throws Exception;

    List<AgEsCustomWord> findList(AgEsCustomWord agEsCustomWord) throws Exception;

    void save(AgEsCustomWord agEsCustomWord) throws Exception;

    void update(AgEsCustomWord agEsCustomWord) throws Exception;

    AgEsCustomWord findById(String id) throws Exception;

    AgEsCustomWord findByCustomWord(String customWord) throws Exception;

    void delById(String id) throws Exception;

    void batchDelById(String[] ids)throws Exception;

    AgEsCustomWord getLatestTime() throws Exception;
}
