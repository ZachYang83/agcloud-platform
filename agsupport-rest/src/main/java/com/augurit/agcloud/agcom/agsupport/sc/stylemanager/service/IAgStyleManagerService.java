package com.augurit.agcloud.agcom.agsupport.sc.stylemanager.service;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgStyleManager;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/07
 * @Description:
 */
public interface IAgStyleManagerService {


    AgStyleManager findById(String id);

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


    void saveStyle(AgStyleManager styleManager);

    void deleteStyle(String id);

    void deleteStyleBatch(String ids);

    /**
     *
     * @Author: libc
     * @Date: 2020/10/13 10:26
     * @tips:
     * @param styleManager 样式对象
     * @param paramType 修改类型
     * @param layerId 图层id
     * @return
     */
    void updateStyle(AgStyleManager styleManager, String paramType, String layerId) throws Exception;
}
