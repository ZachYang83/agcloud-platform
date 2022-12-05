package com.augurit.agcloud.agcom.agsupport.sc.dir.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgLayerRelated;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.github.pagehelper.Page;

import java.util.List;

public interface IAgLayerRelated {
    ContentResultForm getPropertyTablesByDataSourceId(String dataSourceId);
    ContentResultForm getServiceFieldsByDirLayerId(String dirLayerId);
    ContentResultForm getTableFieldsByDirLayerId(String dirLayerId);
    ResultForm addLayerRelated(AgLayerRelated agLayerRelated);
    ResultForm deleteLayerRelated(String id);
    ResultForm deleteLayerRelated(List<String> ids);
    ResultForm editLayerRelated(AgLayerRelated agLayerRelated);
    ResultForm getByServiceDirLayerId(Page page,String serviceDirLayerId);
    ResultForm getById(String id);
}
