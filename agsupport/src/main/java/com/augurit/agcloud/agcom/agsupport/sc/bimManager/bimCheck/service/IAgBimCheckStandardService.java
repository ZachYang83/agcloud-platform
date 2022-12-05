package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimCheck.service;

import com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckStandard;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author lizih
 * @Date 2020/12/18 16:39
 * @Version 1.0
 */
public interface IAgBimCheckStandardService {
    /**
     * @Author Zihui Li
     * @Date: 2020/12/21 15:54
     * @tips: 
     * @Param [category, page]
     * @return com.github.pagehelper.PageInfo<com.augurit.agcloud.agcom.agsupport.domain.auto.AgBimCheckStandard>
     */
    PageInfo<AgBimCheckStandard> find(String category, Page page);

    /**
     * @Author Zihui Li
     * @Date: 2020/12/18 17:22
     * @tips:
     * @Param [agBimCheckStandard]
     * @return void
     */
    void save(AgBimCheckStandard agBimCheckStandard);

    void deleteByIds(String[] ids);

    void excelImport(MultipartFile file);
}
