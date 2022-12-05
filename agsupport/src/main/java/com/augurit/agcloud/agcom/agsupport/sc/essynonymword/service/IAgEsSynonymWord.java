package com.augurit.agcloud.agcom.agsupport.sc.essynonymword.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgEsSynonymWord;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :16:53 2018/12/21
 * @Modified By:
 */
public interface IAgEsSynonymWord {

    List<AgEsSynonymWord> findAll()throws Exception;

    AgEsSynonymWord findWord(String word) throws Exception;

    PageInfo<AgEsSynonymWord> searchAgEsSynonymWord(AgEsSynonymWord agEsCustomWord, Page page) throws Exception;

    AgEsSynonymWord findById(String id) throws Exception;

    AgEsSynonymWord findByWord(String word) throws Exception;

    void saveAgEsSynonymWord(AgEsSynonymWord agEsSynonymWord) throws Exception;

    void updataAgEsSynonymWord(AgEsSynonymWord agEsSynonymWord)throws Exception;

    void delById(String id) throws Exception;

    void batchDelById(String[] ids) throws Exception;

    AgEsSynonymWord getLatestTime() throws Exception;
}
