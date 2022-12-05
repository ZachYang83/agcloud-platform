//package com.augurit.agcloud.agcom.agsupport.sc.bim.service;
//
//
//import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
//import com.augurit.agcloud.agcom.agsupport.domain.auto.AgCategory;
//import com.augurit.agcloud.agcom.agsupport.domain.auto.AgHouse;
//import com.augurit.agcloud.agcom.agsupport.domain.auto.AgSysSetting;
//import com.augurit.agcloud.agcom.agsupport.sc.bimManager.util.AgcloudUrlUtil.AgHouseCustom;
//import com.augurit.agcloud.agcom.agsupport.sc.bim.util.Ztree;
//import com.github.pagehelper.Page;
//import com.github.pagehelper.PageInfo;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.FileNotFoundException;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created with IntelliJ IDEA.
// *
// * @Auther: qinyg
// * @Date: 2020/09
// * @Description:
// */
//public interface ISysAgHouseService {
//
//    /**
//     * 保存资源信息
//     * @param resource
//     * @param file
//     * @throws SourceException
//     */
//    void saveResource(AgHouse resource, MultipartFile file) throws SourceException;
//
//    /**
//     * 保存文件到mongodb
//     * @param collcetionName
//     * @param file
//     */
//    void saveTableFileToMongodb(String collcetionName, MultipartFile file);
//
//    /**
//     *  根据文件夹目录结构，生成分类，同时保存资源,modelFiles为父资源
//     * @param resource
//     * @param thumbFile
//     * @param modelFiles
//     * @param dirFiles
//     * @param tableFile
//     * @throws SourceException
//     */
//    void save(AgHouse resource, MultipartFile thumbFile, MultipartFile modelFiles[], MultipartFile dirFiles[], MultipartFile tableFile) throws SourceException;
//
//    /**
//     * 保存分类
//     */
//    Map<String, String> saveCategoryFromFileList(String categoryId, MultipartFile[] dirFiles);
//
//
//    /**
//     * 获取所有分类
//     * @return
//     */
//    List<Ztree> getAllCategory();
//
//    /**
//     * 获取所有资源数据
//     * @param resource
//     * @param page
//     * @return
//     */
//    PageInfo<AgHouseCustom> find(AgHouse resource, Page page);
//
//
//    /**
//     * 添加分类
//     * @param category
//     * @return
//     */
//    AgCategory saveCategory(AgCategory category);
//
//    /**
//     * 删除分类
//     * @param id
//     */
//    void deleteCategory(String id);
//
//    /**
//     * 修改分类
//     * @param category
//     */
//    void updateCategory(AgCategory category);
//
//    /**
//     * 获取系统设置信息
//     * @return
//     */
//    List<AgSysSetting> getAllSysSetting();
//
//    /**
//     * 通过参数，获取资源访问路径
//     * @param map 格式是/categoryName/categoryName/.../categoryName/file.jpg
//     * @return
//     */
//    String getResourceViewPath(Map<String, String> map);
//
//    /**
//     * 获取树形菜单路径
//     * @param sourceId
//     * @return
//     */
//    String getTreeDirPath(String sourceId);
//
//    /**
//     * 获取父分类路径,包含当前参数的分类
//     * @param categoryId
//     * @return
//     */
//    String getParentCategoryPath(String categoryId);
//    /**
//     * 获取父分类路径,不包含当前参数的分类
//     * @param categoryId
//     * @return
//     */
//    String getParentCategoryPathNoIncloudParam(String categoryId);
//
//
//    /**
//     * 查找所有的户型图
//     * @param id
//     * @return
//     */
//    List<AgHouse> findHouseDir(String id);
//
//    /**
//     * 查询缩略图
//     * @param id
//     * @return base64编码信息
//     */
//    String findThumb(String id);
//
//
//    /**
//     * 获取系统设置的路径
//     * @return
//     * @throws FileNotFoundException
//     */
//    String getBaseFilePath() throws FileNotFoundException;
//
//    /**
//     * 修改资源信息
//     * @param resource
//     * @param thumbFile 缩略图
//     * @param dirFiles 户型图
//     *
//     */
//    void update(AgHouseCustom resource, MultipartFile thumbFile, MultipartFile dirFiles[]);
//
//
//
//    /**
//     * 删除资源，以及资源关联的所有数据
//     * @param id
//     */
//    void delete(String id);
//
//    /**
//     * 删除资源，以及资源关联的所有数据
//     * @param ids
//     */
//    void batchDelete(String ids);
//
//    /**
//     * 从磁盘删除文件
//     * @param sourcePath
//     */
//    void deleteFileFromCategorySourcePath(String sourcePath);
//
//    /**
//     * 后台列表过滤统计
//     * @return
//     */
//    Object statistics();
//
//    /**
//     * 保存房屋模型
//     * @param categoryId 分类id
//     * @param modelFile zip压缩包
//     */
//    void saveRvtZip(String categoryId, MultipartFile modelFile);
//
//    /**
//     * 保存房屋模型
//     * @param categoryId
//     * @param modelFile
//     */
//    void add3dtilesZip(String categoryId, MultipartFile modelFile);
//
//
//
//}
