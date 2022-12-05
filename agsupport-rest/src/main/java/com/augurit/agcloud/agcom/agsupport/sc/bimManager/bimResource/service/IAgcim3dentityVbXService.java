package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service;


import com.augurit.agcloud.agcom.agsupport.domain.Agcim3dentityResultCustom;
import com.github.pagehelper.Page;

import java.util.Map;

/**
 * @Author: qinyg
 * @Date: 2020/10/14
 * @tips: agcim3dentity_vbx的表通用方法
 */
public interface IAgcim3dentityVbXService {
    Agcim3dentityResultCustom find(String tableName, String id, String objectid, String name, Long version, String infotype,
                                   String profession, String level, String catagory, String materialid, String categorypath,
                                   String projectname, String projectcode, Page page, String filterType);

    Object statistics(String tableName, String name, String level, String catagory);

    Map getGlb(String tableName, String id);
}
