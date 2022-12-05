package com.augurit.agcloud.agcom.agsupport.sc.portal.config.service;

import com.augurit.agcloud.agcom.agsupport.sc.buildingComponent.entity.TreeNode;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @Author: libc
 * @Description: portal 系统配置业务接口
 * @Date: 2020/12/23 14:25
 * @Version: 1.0
 */
public interface ISysConfigService {
    /**
     *
     * @Author: libc
     * @Date: 2020/12/23 15:34
     * @tips: 根据目录类型获取对应目录树集合（本地文件目录）
     * @param dirType 目录类型
     * @return List<TreeNode>
     */
    List<TreeNode<File>> tree(String dirType);

    /**
     *
     * @Author: libc
     * @Date: 2020/12/28 10:00
     * @tips: 上传节点目录对应的md说明文档
     * @param file md文件
     * @param dirPath 对应目录文件路径
     */
    void uploadFile(MultipartFile file, String dirPath);

    /**
     *
     * @Author: libc
     * @Date: 2020/12/28 16:31
     * @tips: 获取md文件
     * @param filePath md文档路径
     * @return String
     */
    String getMdFile(String filePath);

    /**
     *
     * @Author: libc
     * @Date: 2020/12/29 14:39
     * @tips: 新增节点（文件夹）
     * @param dirPath 新增文件夹父级路径
     * @param newFileName 文件夹名称
     */
    void add(String dirPath, String newFileName);

    /**
     *
     * @Author: libc
     * @Date: 2020/12/29 15:38
     * @tips: 删除节点（文件夹）
     * @param dirPath 要删除的文件夹路径
     */
    void delete(String dirPath);

    /**
     *
     * @Author: libc
     * @Date: 2020/12/29 16:45
     * @tips: 修改节点（文件夹）
     * @param dirPath 要修改的原文件夹路径
     * @param modifyFileName 新的文件夹名称
     */
    void update(String dirPath, String modifyFileName);
}
