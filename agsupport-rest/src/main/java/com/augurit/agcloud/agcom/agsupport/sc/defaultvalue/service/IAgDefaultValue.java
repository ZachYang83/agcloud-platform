package com.augurit.agcloud.agcom.agsupport.sc.defaultvalue.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgDefaultValue;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :15:06 2019/3/19
 * @Modified By:
 */
public interface IAgDefaultValue {

    void save(AgDefaultValue agDefaultValue) throws Exception;

    void update(AgDefaultValue agDefaultValue) throws Exception;

    AgDefaultValue findByKey(String key)throws Exception;
}
