package com.augurit.agcloud.agcom.agsupport.sc.index.service;

import java.util.List;
import java.util.Map;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-04-14.
 */
public interface IAgIndex {
   // List<Map> getIndexMenu(String userName) throws Exception;

   // List<Map> getIndexMenuDir(String userName) throws Exception;
    String getTitle() throws Exception;

    List<Map> getIndexMenuDirTree(String userName, String dirId, String xPath) throws Exception;
}
