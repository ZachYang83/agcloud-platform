package com.augurit.agcloud.agcom.agsupport.sc.bimfile.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimRelationFile;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * bim关联文件service
 * Created by fanghh on 2019/12/5.
 */
public interface IBimRelationFile {

    /**
     * 根据bimId获取关联文件
     * @param bimId
     * @return
     */
    PageInfo findBimRelationFile(String bimId, Page page);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int delete(String id);

    /**
     * 保存
     * @param relationFile
     */
    void save(AgBimRelationFile relationFile);

    /**
     * 保存关联文件
     * @param bimId
     * @param fileIds
     */
    void saveBimRelationFile(String bimId, List<String> fileIds);

    /**
     * 根据bimId获取文件的id
     * @param bimId
     * @return
     */
    List<String> findFileIdByBimId(String bimId);
}
