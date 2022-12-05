package com.augurit.agcloud.agcom.agsupport.mapper;

import com.augurit.agcloud.agcom.agsupport.domain.AgDefaultValue;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :14:50 2019/3/19
 * @Modified By:
 */
@Mapper
public interface AgDefaultValueMapper {
    void save(AgDefaultValue agDefaultValue) throws Exception;

    void update(AgDefaultValue agDefaultValue) throws Exception;

    AgDefaultValue findByKey(String key)throws Exception;

}
