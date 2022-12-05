package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgEsSynonymWord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :16:56 2018/12/21
 * @Modified By:
 */

@Mapper
public interface AgEsSynonymWordMapper {

    List<AgEsSynonymWord> findAll() throws Exception;

    List<AgEsSynonymWord> findList(AgEsSynonymWord agEsSynonymWord) throws Exception;

    AgEsSynonymWord findWord(String word) throws Exception;

    void save(AgEsSynonymWord agEsSynonymWord) throws Exception;

    void update(AgEsSynonymWord agEsSynonymWord) throws Exception;

    AgEsSynonymWord findById(String id) throws Exception;

    AgEsSynonymWord findByWord(String word) throws Exception;

    void delById(String id) throws Exception;

    void batchDelById(String[] ids) throws Exception;

    AgEsSynonymWord getLatestTime()throws Exception;

}
