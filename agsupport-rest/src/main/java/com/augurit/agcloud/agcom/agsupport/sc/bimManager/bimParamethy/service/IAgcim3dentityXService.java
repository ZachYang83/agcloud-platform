//package com.augurit.agcloud.agcom.agsupport.sc.bim.service;
//
//
//
//import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dentityA;
//
//import java.util.List;
//
///**
// * Created with IntelliJ IDEA.
// *
// * @Auther: qinyg
// * @Date: 2020/08
// * @Description:
// */
//public interface IAgcim3dentityAService {
//
//    List<Agcim3dentityA> list(Agcim3dentityA agcim);
//
//    List<Agcim3dentityA>  filter(String filterKey, Agcim3dentityA agcim);
//
//    Object count(String countKey, String groupKey, Agcim3dentityA param);
//
//
//}

package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.service;



import com.augurit.agcloud.agcom.agsupport.domain.Agcim3dentityXCustom;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/08
 * @Description:
 */
public interface IAgcim3dentityXService {

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/12 10:12
     * @tips: 通过表名做列表参数
     * @param agcim 查询条件
     * @param tableName 查询表名称
     * @return
     */
    List<Agcim3dentityXCustom> list(Agcim3dentityXCustom agcim, String tableName);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/12 10:13
     * @tips: 过滤不同的字段，只返回filterKey属性
     * @param filterKey 需要返回的字段，多个用逗号分隔
     * @param agcim 查询条件
     * @param tableName 查询表名称
     * @return
     */
    List<Agcim3dentityXCustom>  filter(String filterKey, Agcim3dentityXCustom agcim, String tableName);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/12 10:14
     * @tips: 统计不同属性的数量，且返回属性和数量
     * @param countKey 需要统计的列
     * @param groupKey 需要分组查询的列
     * @param param 查询条件
     * @param tableName 查询表名称
     * @return
     */
    Object statistics(String countKey, String groupKey, Agcim3dentityXCustom param, String tableName);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/12 10:18
     * @tips: 比较两个不同的表列值，返回不同列的某个表的全部数据
     * @param baseTableName 比较表，需要返回的表
     * @param targetTableName 比较表
     * @param columns 需要比较的字段，多个用逗号分隔
     * @return
     */
    PageInfo<Object> compareDoubleTableColumn(String baseTableName, String targetTableName, String columns, Page page);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/12 10:18
     * @tips: 比较两个不同的表删除或者添加的数据
     * @param baseTableName 比较表，需要返回的表
     * @param targetTableName 比较表
     * @return
     */
    PageInfo<Object> compareDoubleTableModify(String baseTableName,  String targetTableName,  Page page);


    /**
     *
     * @Author: libc
     * @Date: 2020/12/16 11:02
     * @tips: 根据构件分类（catagory字段）值分类统计
     * @return Map
     *             columns:构件分类列表（做表头）
     *             countResult:统计结果（一行数据）
     * @param param 查询条件
     * @param tableName 查询表名称
     */
    Map countForCatagoryType(Agcim3dentityXCustom param, String tableName);
}

