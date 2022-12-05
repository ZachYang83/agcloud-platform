package com.augurit.agcloud.agcom.agsupport.sc.bimfile.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimVersion;
import com.augurit.agcloud.agcom.agsupport.domain.AgBimVersionCompare;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.reslut.MultipartFileParam;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @ClassName IBimFile
 * @Description TODO
 * @Author Administrator
 * @Date 2019-12-12 13:37
 * @Version 1.0
 **/
public interface IBimVersion {

    /**
     * 特殊条件Or分页查询
     *
     * @param pkId
     * @param keyword
     * @param page
     * @return
     */
    PageInfo<AgBimVersion> getByOrKeyWords(String pkId, String keyword, Page page) throws Exception;

    /**
     * 添加一条数据
     * @param agBimVersion
     * @return
     */
    boolean add(AgBimVersion agBimVersion);

    /**
     * 根据id删除数据
     * @param paramId
     * @return
     */
    boolean deleteById(String paramId) throws Exception;

    /**
     * 根据idsList删除多条数据
     * @param idsList
     * @return
     */
    boolean deleteMany(List<String> idsList) throws Exception;

    /**
     * 更新一条数据
     * @param agBimVersion
     * @return
     */
    boolean update(AgBimVersion agBimVersion);

    /**
     * 文件上传
     * @param param
     * @return
     */
    boolean uploadFileByMappedByteBuffer(MultipartFileParam param) throws Exception;

    /**
     * 根据表单和上传的文件信息完善agBimVersion, 如果表单和文件信息中同时具有某一属性的非空值，以表单为准
     * @param agBimVersion
     * @param param
     * @return 0：没有上传文件(当判断文件是重复上传时，前端标记文件skipped，此时没有上传文件)
     *         1：有上传文件且信息完善成功；
     *         2：有上传文件但完善不成功（表单填写版本号已存在）
     *         null：其它异常
     */
    Integer completeVersion(AgBimVersion agBimVersion, MultipartFileParam param) throws Exception;

    /**
     * 根据md5获取数据
     * @param md5
     * @return
     */
    List<AgBimVersion> getByMd5(String md5);

    /**
     * 两个不同的版本同时指向同一文件时，两个版本的文件相关信息相同
     * @param agBimVersion
     * @return
     */
    Boolean completeVersionWithExistVersion(AgBimVersion agBimVersion);

    /**
     * 根据pkId查找当前正在使用的版本
     * @param pkId
     * @return
     */
    AgBimVersion getInUseByPkId(String pkId);

    /**
     * 模型比对
     * @param id1
     * @param id2
     * @return
     */
    AgBimVersionCompare bimCompare(String id1, String id2);

    /**
     * 获取模型比对数据
     * @param id1
     * @param id2
     * @return
     */
    AgBimVersionCompare getCompareData(String id1, String id2);

    /**
     * 获取模型最大版本
     * @return
     */
    String findMaxVersion(String bimId);


}
