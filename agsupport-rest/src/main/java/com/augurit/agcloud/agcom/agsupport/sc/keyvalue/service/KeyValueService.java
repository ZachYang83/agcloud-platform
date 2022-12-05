package com.augurit.agcloud.agcom.agsupport.sc.keyvalue.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgKeyValue;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface KeyValueService {
    PageInfo<AgKeyValue> findKeyValueList(String name, Page page);

    void update(AgKeyValue keyvalue);

    void save(AgKeyValue keyvalue);

    void delete(List<String> ids);

    List<AgKeyValue> findKeyValue(String domain, String key);
}
