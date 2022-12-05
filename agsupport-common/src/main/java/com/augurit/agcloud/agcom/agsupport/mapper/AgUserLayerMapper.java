package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgDirLayer;
import com.augurit.agcloud.agcom.agsupport.domain.AgUserLayer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author:zrr
 * @Description:
 * @Date:created in :17:02 2018/7/13
 * @Modified By:
 */
@Mapper
public interface AgUserLayerMapper {
    /**
     * 按id查找
     *
     * @param id
     * @return
     * @throws Exception
     */
    AgUserLayer findById(@Param("id") String id) throws Exception;

    /**
     * 按用户id、目录图层id查找用户图层关联
     *
     * @param userId
     * @param dirLayerId
     * @return
     * @throws Exception
     */
    AgUserLayer findByUidAndDid(@Param("userId") String userId, @Param("dirLayerId") String dirLayerId) throws Exception;

    /**
     * 按用户id查找用户图层关联，不包含Extent
     *
     * @param userId
     * @return
     * @throws Exception
     */
    List<AgUserLayer> findListByUserIdNotWithExtent(@Param("userId") String userId) throws Exception;

    /**
     * 按用户id查找
     *
     * @param userId
     * @return
     * @throws Exception
     */
    List<AgUserLayer> findListByUserId(@Param("userId") String userId) throws Exception;


    /**
     * 按目录图层id查找
     *
     * @param dirLayerId
     * @return
     * @throws Exception
     */
    List<AgUserLayer> findListByDirLayerId(@Param("dirLayerId") String dirLayerId) throws Exception;

    /**
     * 批量保存
     *
     * @param list
     * @throws Exception
     */
    void saveBatch(List<AgUserLayer> list) throws Exception;
    /**
     * 更新
     *
     * @param userLayer
     * @throws Exception
     */
    void updateUserLayer(AgUserLayer userLayer) throws Exception;

    /**
     * 删除
     *
     * @param id
     * @throws Exception
     */
    void delete(@Param("id") String id) throws Exception;

    void deleteByDirLayerIds(@Param("agDirLayers") List<AgDirLayer> agDirLayers) throws Exception;
    /**
     * 通过userId和dirLayerId删除ag_userLayer里的记录
     * @param userId
     * @param dirLayerId
     * @throws Exception
     */
   void deleteByUiAndDi(@Param("userId") String userId, @Param("dirLayerId") String dirLayerId)throws Exception;

   List<AgUserLayer> findByDirLayerIds(@Param("agDirLayers") List<AgDirLayer> agDirLayers) throws Exception;

}
