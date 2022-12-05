package com.augurit.agcloud.agcom.agsupport.sc.stylemanager.service;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgStyleManager;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/07
 * @Description:
 */
public interface IAgStyleManagerService {

    /**
     *
     * @Author: libc
     * @Date: 2020/11/13 11:25
     * @tips: 分页查询
     * @param layerType 图层类型
     * @param name 关键字
     * @return
     */
    PageInfo<AgStyleManager> findStyleList(String name, String layerType, Page page);

    List<AgStyleManager> findAllStyle();

    void saveStyle(AgStyleManager styleManager);

    void deleteStyle(String id);

    void deleteStyleBatch(String ids);

    void updateStyle(AgStyleManager styleManager);

    AgStyleManager findById(String id);
}
