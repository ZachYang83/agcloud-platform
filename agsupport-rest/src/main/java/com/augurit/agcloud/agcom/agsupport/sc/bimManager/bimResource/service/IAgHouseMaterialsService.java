package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service;



import com.augurit.agcloud.agcom.agsupport.domain.AgHouseMaterialsCustom;
import com.augurit.agcloud.agcom.agsupport.domain.AgHouseMaterialsPointCustom;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgHouseMaterialsPoint;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * @Author: qinyg
 * @Date: 2020/10/21
 * @tips:
 */
public interface IAgHouseMaterialsService {

    /**
     * 统计分析点列表查询
     * @param sourceId 来源id
     * @param type 类型
     * @param name 名称
     * @param floor 楼层
     * @param page 分页参数
     * @param mapArray 范围面坐标点
     * @param cacheableKey 缓存的key
     * @return
     */
    PageInfo<AgHouseMaterialsPointCustom> findMaterialsPoint(String sourceId, String type, String name, String floor, Page page, String mapArray, String cacheableKey);

    /**
     * 添加分析点
     * @param entity
     */
    void addMaterialsPoint(AgHouseMaterialsPoint entity);

    /**
     * 删除分析点
     * @param ids
     */
    void deleteMaterialsPoint(String ids);

    /**
     * 导入数据【不对外开放】
     * @param json
     * @param entity
     */
    void importMaterialsPointJsonStr(String json, AgHouseMaterialsPoint entity);

    /**
     * 统计所有的类型
     * @param page 分页参数
     * @param mapArray  范围面坐标点
     * @param cacheableKey 缓存的key
     * @return
     */
    PageInfo<AgHouseMaterialsCustom> statisticsMaterialsisInPolygon(Page page, String mapArray, String cacheableKey);
}
