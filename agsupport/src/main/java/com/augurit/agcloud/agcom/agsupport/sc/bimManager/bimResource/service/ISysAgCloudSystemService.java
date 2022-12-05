package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service;

import com.augurit.agcloud.agcom.agsupport.sc.bimManager.util.AgUserCustom;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * 调用agcloud的接口
 *
 * @Auther: qinyg
 * @Date: 2020/09
 * @Description:
 */
public interface ISysAgCloudSystemService {

    /**
     * 通过orgId获取用户列表
     * @param orgId
     * @return
     */
    List<AgUserCustom> getUserList(String orgId);

    /**
     * 从建筑构件分类数据字典里面过滤，获取名称
     * @param tableCode
     * @return
     */
    String getBuildComponentDictionaryName(String tableCode);

    /**
     * 获取建筑构件信息名称
     * @param filterType 过滤查询类型（1：大类、2：中类、3：小类、4：细类）
     * @param tableCode
     * @return
     */
    String getBuildComponentName(String filterType, String tableCode, String largeCode, String mediumCode, String smallCode, String detailCode);

}
