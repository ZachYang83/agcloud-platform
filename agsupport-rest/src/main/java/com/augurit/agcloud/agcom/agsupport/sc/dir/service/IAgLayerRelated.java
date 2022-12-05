package com.augurit.agcloud.agcom.agsupport.sc.dir.service;

import com.augurit.agcloud.framework.ui.result.ResultForm;

public interface IAgLayerRelated {
    ResultForm getById(String id);
    ResultForm getByServiceDirLayerId(String serviceDirLayerId);
}
