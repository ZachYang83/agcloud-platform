package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckStandard;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @Author lizih
 * @Date 2020/12/23 9:22
 * @Version 1.0
 */
public interface IAgBimCheckStandardService {
    /**
     * @Author Zihui Li
     * @Date: 2020/12/23 9:31
     * @tips: 
     * @Param category 条文所属类型。如果不传此参数，返回所有条文列表；如果传参，返回本类所有条文
     * @Param page 页码
     * @return com.github.pagehelper.PageInfo<com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckStandard>
     */
    PageInfo<AgBimCheckStandard> find(String category, Page page);
    
    /**
     * @Author Zihui Li
     * @Date: 2020/12/23 16:45
     * @tips: 
     * @Param category 条文所属类型。如果不传此参数，返回所有条文列表；如果传参，返回本类所有条文
     * @return java.util.List<com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckStandard>
     */
    List<AgBimCheckStandard> get(String category);

    /**
     * @Author Zihui Li
     * @Date: 2020/12/28 11:20
     * @tips: 返回所有条文所属类型
     * @return java.util.List<java.lang.String>
     */
    List<String> getCategories();

    /**
     * @Author Zihui Li
     * @Date: 2020/12/28 11:21
     * @tips: 返回某个条文类型下所有不重复的规范库名
     * @Param category 条文所属类型
     * @return java.util.List<java.lang.String>
     */
    List<String> getClauses(String category);

    /**
     * @Author Zihui Li
     * @Date: 2020/12/28 11:23
     * @tips:
     * @Param category 条文所属类型
     * @Param clause 条文规范库名 例如《住宅建筑规范》
     * @return java.util.List<com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckStandard>
     */
    List<AgBimCheckStandard> getClauseContents(String category, String clause);

    /**
     * @Author Zihui Li
     * @Date: 2020/12/28 11:24
     * @tips: 根据条文序号树结构返回条文内容
     * @Param category 条文所属类型
     * @Param clause 条文规范库名 例如《住宅建筑规范》
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */
    Map<String, Object> tree(String category, String clause);
}
