package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgStyle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-07-21.
 */
@Mapper
public interface AgStyleMapper {


    List<AgStyle> findAll() throws Exception;

    /**
     * 按条件查询
     *
     * @param agStyle
     * @return
     * @throws Exception
     */
    List<AgStyle> findAgStyleList(AgStyle agStyle) throws Exception;

    /**
     * 查询已被使用的符号集合
     * @return AgStyle集合
     * @throws Exception
     */
    List<AgStyle> findUsedAgStyleList() throws Exception;
    /**
     * 查询一条记录
     *
     * @param id
     * @throws Exception
     */
    AgStyle findAgStyleById(@Param("id") String id) throws Exception;


    /*
    *  获取点线面的默认样式
    * */
    List<AgStyle> findDefaultStyleList() throws Exception ;


    /**
     * 保存
     *
     * @param agStyle
     * @throws Exception
     */
    void saveAgStyle(AgStyle agStyle) throws Exception;

    /**
     * 修改
     *
     * @param agStyle
     * @throws Exception
     */
    void updateAgStyle(AgStyle agStyle) throws Exception;

    /**
     * 删除
     *
     * @param ids
     * @throws Exception
     */
    void deleteAgStyle(@Param("ids") String[] ids) throws Exception;

    void deleteAgStyleBatch(@Param("layerIds") List<String> layerIds) throws Exception;
    /**
     * 获取矢量图层当前配置的样式Id
     * @param layerId
     * @throws Exception
     */
    List<String> findStyleIdsByLayerId(@Param("layerId") String layerId) throws Exception;

    /**
     * 删除矢量图层与样式的对应关系
     *
     */
    void deleteLayerStyle(@Param("layerId") String layerId, @Param("styleIds") List<String> styleIds) throws Exception;

    /**
     * 保存矢量图层与样式的对应关系
     *
     */
    void saveLayerStyle(@Param("layerId") String layerId, @Param("items") List<Map> items) throws Exception;

}
