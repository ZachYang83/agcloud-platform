package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimParamethy.service;

import com.augurit.agcloud.agcom.agsupport.domain.auto.Agcim3dbuilding;

import java.util.List;

/**
 * @Author: libc
 * @Description: Agcim三维BIM 业务接口
 * @Date: 2020/11/25 14:12
 * @Version: 1.0
 */
public interface IAgcim3dbuildingService {
    /**
     *
     * @Author: libc
     * @Date: 2020/11/25 14:14
     * @tips: 新增
     * @param agcim3dbuilding 对象
     */
    void add(Agcim3dbuilding agcim3dbuilding);

    /**
     *
     * @Author: libc
     * @Date: 2020/11/25 15:32
     * @tips: 根据BIM模型信息表名删除记录
     * @param infoRelTableName 关联表名称
     */
    void deleteByEntitytable(String infoRelTableName);

    /**
     *
     * @Author: libc
     * @Date: 2020/12/10 16:56
     * @tips: 根据条件获取模型集合
     * @param param 查询条件
     * @return List<Agcim3dbuilding>
     */
    List<Agcim3dbuilding> listBuildings(Agcim3dbuilding param);
}
