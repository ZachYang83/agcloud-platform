package com.augurit.agcloud.agcom.agsupport.sc.bimManager.bimResource.service;


import com.augurit.agcloud.agcom.agsupport.domain.auto.AgHouse;
import com.augurit.agcloud.agcom.agsupport.domain.auto.AgSysSetting;
import com.augurit.agcloud.agcom.agsupport.sc.bimManager.util.AgHouseCustom;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: qinyg
 * @Date: 2020/09
 * @Description:
 */
public interface ISysAgHouseService2 {


    /**
     * 获取所有资源数据
     * @param resource
     * @param page
     * @return
     */
    PageInfo<AgHouse> find(AgHouse resource, Page page);

    /**
     * 获取系统设置信息
     * @return
     */
    List<AgSysSetting> getAllSysSetting();

    /**
     * 查找所有的户型图
     * @param id
     * @return
     */
    List<AgHouse> findHouseDir(String id);

    /**
     * 查询缩略图
     * @param id
     * @return base64编码信息
     */
    String findThumb(String id);


    /**
     * 获取系统设置的路径
     * @return
     * @throws FileNotFoundException
     */
    String getBaseFilePath() throws FileNotFoundException;


    /**
     * 修改资源信息
     * @param resource
     * @param thumbFile 缩略图
     * @param dirFiles 户型图
     *
     */
    void update(AgHouseCustom resource, MultipartFile thumbFile, MultipartFile dirFiles[]);



    /**
     * 删除资源，以及资源关联的所有数据
     * @param id
     */
    void delete(String id);

    /**
     * 删除资源，以及资源关联的所有数据
     * @param ids
     */
    void batchDelete(String ids);

    /**
     * 从磁盘删除文件
     * @param sourcePath
     */
    void deleteFileFromCategorySourcePath(String sourcePath);

    /**
     * 后台列表过滤统计
     * @return
     */
    Object statistics();

    /**
     * 保存房屋模型
     * @param modelFile zip压缩包
     */
    void saveRvtZip(MultipartFile modelFile);

    /**
     * 保存房屋模型
     * @param modelFile
     */
    void add3dtilesZip(MultipartFile modelFile);


}
