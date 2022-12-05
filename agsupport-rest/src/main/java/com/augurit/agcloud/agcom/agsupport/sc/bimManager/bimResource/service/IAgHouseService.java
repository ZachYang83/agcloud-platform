package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgHouse;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgSysSetting;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/07
 * @Description:
 */
public interface IAgHouseService {




    /**
     * 获取所有资源数据
     * @param resource
     * @param page
     * @return
     */
    PageInfo<AgHouse> list(AgHouse resource, Page page);


    /**
     * 是否具有全部权限
     * @param userId
     * @return
     */
    boolean isHaveFullPermission(String userId);

    /**
     * 获取用户已分配的权限
     * @param userId
     * @return
     */
    List<String> getUserPermission(String userId);

    /**
     * 获取系统设置信息
     * @return
     */
    List<AgSysSetting> getAllSysSetting();

//    /**
//     * 通过参数，获取资源访问路径
//     * @param map 格式是/categoryName/categoryName/.../categoryName/file.jpg
//     * @return
//     */
//    String getResourceViewPath(Map<String, String> map);
//
//
//
//    /**
//     * 获取父分类路径,包含当前参数的分类
//     * @param categoryId
//     * @return
//     */
//    String getParentCategoryPath(String categoryId);
//    /**
//     * 获取父分类路径,不包含当前参数的分类
//     * @param categoryId
//     * @return
//     */
//    String getParentCategoryPathNoIncloudParam(String categoryId);


    /**
     * 查找所有的户型图
     * @param id
     * @return
     */
    List<AgHouse> findResourceDir(String id);




    /**
     * 获取系统设置的路径
     * @return
     * @throws FileNotFoundException
     */
    String getBaseFilePath() throws FileNotFoundException;

}
