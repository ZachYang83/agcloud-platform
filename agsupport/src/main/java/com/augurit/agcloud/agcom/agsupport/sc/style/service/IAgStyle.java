package com.augurit.agcloud.agcom.agsupport.sc.style.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgStyle;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-07-18.
 */
public interface IAgStyle {
    /**
     * 获取目录下所有的图标并标记其是否被使用
     *
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> findAllIcon(String path) throws Exception;

    /**
     * 查询样式
     *
     * @param agStyle
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo<AgStyle> findStyleListPage(AgStyle agStyle, Page page) throws Exception;

    /**
     * 查询样式
     *
     * @param agStyle
     * @return
     * @throws Exception
     */
    List<AgStyle> findStyleList(AgStyle agStyle) throws Exception;

    /**
     * 根据id查询样式
     *
     * @param id
     * @return
     * @throws Exception
     */
    AgStyle findStyleById(String id) throws Exception;


    /**
     * 获取点线面的默认样式
     * @throws Exception
     */
    List<AgStyle> findDefaultStyleList() throws Exception;

    /**
     * 保存样式
     *
     * @param agStyle
     * @throws Exception
     */
    void saveStyle(AgStyle agStyle) throws Exception;

    /**
     * 修改样式
     *
     * @param agStyle
     * @throws Exception
     */
    void updateStyle(AgStyle agStyle) throws Exception;

    /**
     * 删除样式
     *
     * @param ids
     * @throws Exception
     */
    void deleteStyle(String[] ids) throws Exception;

    Map getStyleConfMap(String dirLayerId) throws Exception;

    Map getStyleConfMap(AgLayer agLayer) throws Exception;

    Map loadStyleConf(MultipartFile multipartFile, String dirLayerId, String iconRootPath) throws Exception;

    List<Map> listTableColumnData(String dataSourceId, String tableName, String column) throws Exception;

    List<Map> getStyleConfList(String layerIds) throws Exception;


    List<String> getStyleIdsByLayerId(String layerId) throws Exception;

    void updateLayerStylesRetionship(String layerId, List<String> newStyleIds, List<String> olderStyles) throws Exception;
}
