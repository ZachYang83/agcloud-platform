package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayerFieldConf;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zhangmingyang
 * @Description: TODO
 * @date 2019-01-18 14:00
 */
@Mapper
public interface AgLayerFieldConfMapper {
    //List<AgLayerFieldConf> findByDirLayerId(String dirLayerId) throws Exception;

    List<AgLayerFieldConf> findByLayerId(String layerId) throws Exception;
}
