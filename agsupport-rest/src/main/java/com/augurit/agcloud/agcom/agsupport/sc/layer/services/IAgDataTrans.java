package com.augurit.agcloud.agcom.agsupport.sc.layer.services;

import com.augurit.agcloud.agcom.agsupport.sc.spatial.base.Layer;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-05-02.
 */
public interface IAgDataTrans {
    /**
     * 数据清洗方法
     *
     * @param layer 开始需要清理的矢量图层
     */
    String startDataTrans(Layer layer);

    /**
     * 停止表layerTable的数据清洗
     *
     * @param dataSourceId 数据源
     * @param layerTable   图层表名
     * @return
     */
    boolean stopDataTrans(String dataSourceId, String layerTable);

    /**
     * 清除清洗的数据
     *
     * @param dataSourceId 数据源
     * @param layerTable   图层表名
     * @return
     */
    String clearDataTrans(String dataSourceId, String layerTable);

    /**
     * 获取图层数据清洗进度
     *
     * @param dataSourceId 数据源
     * @param layerTable   图层表名
     * @return 执行进度
     */
    double getProgress(String dataSourceId, String layerTable);

    /**
     * 获取图层数据清洗进度信息
     *
     * @param dataSourceId 数据源
     * @param layerTable   图层表名
     * @return 执行进度
     */
    String getProgressMsg(String dataSourceId, String layerTable);

    /**
     * 获取正在清洗的图层
     *
     * @return
     */
    String getDataTransKey();

    /**
     * 判断图层是否清洗完成
     *
     * @param dataSourceId 数据源
     * @param layerTable   图层表名
     * @return
     */
    boolean isTransComplete(String dataSourceId, String layerTable);

    /**
     * 更新或新增一条缓冲数据
     *
     * @param layer      需要缓冲的矢量图层
     * @param primaryKey 主键值
     * @return
     */
    boolean updateOrSaveByPk(Layer layer, String primaryKey);

    /**
     * 批量更新或新增缓冲数据
     *
     * @param layer  需要缓冲的矢量图层
     * @param pkList 主键list
     * @return
     */
    boolean updateOrSaveByPks(Layer layer, List<String> pkList);

    /**
     * 清除数据缓冲
     *
     * @param dataSourceId 数据源
     * @param layerTable   清除的图层表名
     * @return
     */
    boolean deleteData(String dataSourceId, String layerTable);

    /**
     * 通过主键删除某一条缓冲数据
     *
     * @param layerTable 表名
     * @param primaryKey 主键值
     * @return
     */
    boolean deleteDataByPk(String dataSourceId, String layerTable, String primaryKey);

    /**
     * 清除redis缓存
     *
     * @param cacheTable
     * @return
     */
    boolean clearRedisCache(String cacheTable);

    String start(Layer layer) throws InterruptedException, ExecutionException;

    void clearHashMap(String dataSourceId,String layerTable);

    boolean mongoDataTranIsRun(String dataSourceId,String layerTable);
}
