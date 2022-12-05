package com.augurit.agcloud.agcom.agsupport.sc.attrTable.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by chendingxing on 2017-08-02.
 */
public interface IAgAttrTable {

    List<Map> query(Layer layer, Page page) throws Exception;

    int update(AgLayer table, String update, String where) throws Exception;

    int insert(AgLayer table, List<Map> insert) throws Exception;

    int delete(AgLayer table, String column, String[] values) throws Exception;
}
