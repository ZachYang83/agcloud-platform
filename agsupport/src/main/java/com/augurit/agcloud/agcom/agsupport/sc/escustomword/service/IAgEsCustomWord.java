package com.augurit.agcloud.agcom.agsupport.sc.escustomword.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgEsCustomWord;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.Page;
import org.apache.tools.ant.taskdefs.EchoXML;

import java.util.List;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :16:53 2018/12/21
 * @Modified By:
 */

public interface IAgEsCustomWord {

    List<AgEsCustomWord> findAll()throws Exception;

    AgEsCustomWord findCustom(String customWord) throws Exception;

    PageInfo<AgEsCustomWord> searchAgEsCustomWord(AgEsCustomWord agEsCustomWord, Page page) throws Exception;

    AgEsCustomWord findById(String id) throws Exception;

    AgEsCustomWord findByCustomWord(String customWord) throws Exception;

    void saveAgEsCustomWord(AgEsCustomWord agEsCustomWord) throws Exception;

    void updataAgEsCustomWord(AgEsCustomWord agEsCustomWord)throws Exception;

    void delById(String id) throws Exception;

    void batchDelById(String[] ids)throws Exception;

    AgEsCustomWord getLatestTime()throws Exception;
}
