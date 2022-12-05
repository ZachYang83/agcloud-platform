package com.augurit.agcloud.agcom.agsupport.sc.widgetAssests.service;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetAssetsColumns;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgWidgetAssetsTable;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author: qinyg
 * @Date: 2020/11/17
 * @tips:
 */
public interface IAgWidgetAssetsTableService {

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/17 18:57
     * @tips: 专题表列表接口
     * @param appSoftId 应用id
     * @param tableName 表名（查询条件）
     * @return
     */
    PageInfo<AgWidgetAssetsTable> find(String appSoftId, String tableName, Page page);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/17 19:03
     * @tips: 添加专题表数据
     * @param table 表名相关信息
     * @param columns 表列属性
     * @return
     */
    void add(AgWidgetAssetsTable table, List<AgWidgetAssetsColumns> columns);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/17 19:04
     * @tips: 删除表专题
     * @param ids 多个用逗号分隔
     * @return
     */
    void deletes(String ids);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/17 19:14
     * @tips:
     * @param table 表名相关信息
     * @param columns 添加或者修改的字段
     * @param deleteColumnIds 需要删除的字段id，多个用逗号分隔
     * @return
     */
    void update(AgWidgetAssetsTable table, List<AgWidgetAssetsColumns> columns, String deleteColumnIds);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/20 9:45
     * @tips: 获取专题表属性
     * @param thematicTableId 专题表id
     * @return
     */
    List<AgWidgetAssetsColumns> getThematicColumns(String thematicTableId);

    /**
     *
     * @Author: qinyg
     * @Date: 2020/11/20 9:47
     * @tips: 修改项目标识，需要修改专题表的表名
     * @param thematicProjectId 专题表项目映射id
     * @param updateUniqueIdf 专题表需要修改的唯一标识
     * @return
     */
    void updateThematicProjectNeedAlterThematicTable(String thematicProjectId, String updateUniqueIdf);
}
