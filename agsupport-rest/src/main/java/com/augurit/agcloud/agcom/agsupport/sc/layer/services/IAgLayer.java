package com.augurit.agcloud.agcom.agsupport.sc.layer.services;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayer;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 海贼王に、おれはなる!!!
 * Created by Caip on 2017-06-01.
 */
public interface IAgLayer {
    boolean saveVectorLayer(AgLayer agLayer, JSONObject dataObj);

    boolean deleteVectorLayer(AgLayer agLayer, JSONObject dataObj);

    /**
     * 按图层id查找
     *
     * @param layerId
     * @return
     * @throws Exception
     */
    AgLayer findByLayerId(@Param("layerId") String layerId) throws Exception;

    /**
     * 获取所有代理地址  既isproxy=1的url
     * @return
     * @throws Exception
     */
    List<AgLayer> findAllAgentUrlList()throws Exception;

    /**
     * 获取矢量图层
     * @return
     * @throws Exception
     */
    List<AgLayer> getVectorLayer() throws Exception;

    /**
     * 分页获取矢量图层
     * @param page
     * @return
     * @throws Exception
     */
    PageInfo<AgLayer>  getVectorLayerByPage(Page page) throws Exception;
    Integer findUserlayerCount(String userId) throws Exception;

    ContentResultForm getAssociatedInfo(String serviceDirLayerId,String relatedDirLayerId,String jsonParams);
}