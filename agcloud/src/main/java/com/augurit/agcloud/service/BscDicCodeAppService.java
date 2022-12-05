package com.augurit.agcloud.service;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;

import java.util.List;

/**
 * @ClassName BscDicCodeService
 * @Description TODO
 * @Author Administrator
 * @Date 2019/11/25 11:48
 * @Version 1.0
 **/

public interface BscDicCodeAppService {

    List<BscDicCodeItem> getRootOrgActiveItemsByTypeCode(String typeCode);

    List<BscDicCodeItem> getActiveItemsByTypeCode(String typeCode, String orgId);

}
