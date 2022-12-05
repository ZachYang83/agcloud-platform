package com.augurit.agcloud.agcom.agsupport.sc.bimfile.service;

import com.augurit.agcloud.agcom.agsupport.domain.AgBimFile;
import com.augurit.agcloud.agcom.agsupport.domain.AgFileStore;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.reslut.MultipartFileParam;
import com.augurit.agcloud.agcom.agsupport.sc.bimfile.dto.BimFileListDTO;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName IBimFile
 * @Description TODO
 * @Author Administrator
 * @Date 2019/12/4 15:48
 * @Version 1.0
 **/
public interface IBimFile {

    /**
     * 根据id获取数据
     *
     * @param id
     * @return
     * @throws RuntimeException
     */
    AgBimFile getById(String id) throws RuntimeException;

    /**
     * 获取所有数据
     *
     * @return
     * @throws RuntimeException
     */
    List<AgBimFile> getAll() throws RuntimeException;

    /**
     * 根据id删除一条数据
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteById(String id) throws Exception;

    /**
     * 删除更多数据
     *
     * @param stringList
     * @return
     * @throws Exception
     */
    boolean deleteMany(List<String> stringList) throws Exception;

    /**
     * 添加一条数据
     *
     * @param agBimFile
     * @return
     * @throws RuntimeException
     */
    boolean add(AgBimFile agBimFile) throws RuntimeException;

    /**
     * 编辑一条数据
     *
     * @param agBimFile
     * @return
     * @throws RuntimeException
     */
    boolean update(AgBimFile agBimFile) throws RuntimeException;

    /**
     * 特殊条件Or分页查询
     *
     * @param projectId
     * @param keyword
     * @param page
     * @return
     * @throws RuntimeException
     */
    PageInfo<AgBimFile> getByOrKeyWords(String projectId,String keyword, Page page) throws RuntimeException;

    /**
     * 特殊条件And分页查询
     *
     * @param agBimFile
     * @param page
     * @return
     * @throws RuntimeException
     */
    List<AgBimFile> getByAndKeyWords(AgBimFile agBimFile, Page page) throws RuntimeException;

    /**
     * 根据md5获取文件信息
     * @param md5
     * @return
     * @throws RuntimeException
     */
    AgBimFile getByMd5(String md5) throws RuntimeException;

    /**
     * 发布bim服务
     * @param id
     */
    ContentResultForm startService(String id,boolean polling);

    /*    *//**
     /**
     * 大文件上传
     * @param param
     * @throws Exception
     */
    boolean uploadFileByMappedByteBuffer(MultipartFileParam param) throws Exception;

    /**
     * 添加bimFile和bimVersion
     * @param fileName
     * @param md5
     * @param projectId
     * @return
     */
    boolean addFileAndVersion(String fileName, String md5,String projectId);

    /**
     * 根据url模拟get请求获取模型文件保存
     * @param url
     * @return
     */
    Integer saveFileByUrl(String url);

    List<AgFileStore> getFileByModuleCode(AgFileStore store, String bimId);


    /**
     * 检查已发布的BIM模型，得出文件列表
     * @param id
     * @return
     */
    ContentResultForm checkBimInfo(String id) throws IOException;

    /**
     * 下载BIM模型中的指定文件
     * @param pkId
     * @param fileName
     * @param request
     * @param response
     */
    void downloadBIMinZip(String pkId, String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * 批量生成bim模型
     * @param fileListDTO
     */
    void saveList(BimFileListDTO fileListDTO);

    void saveBimFile(AgBimFile bimFile);

    void updateBimFile(AgBimFile agBimFile);

    /**
     * 根据AgBimFile生成目录和图册
     */
    void saveDirAndLayer(AgBimFile bimFile) throws Exception;

    List<AgBimFile> findByProjectId(String projectId);
}
