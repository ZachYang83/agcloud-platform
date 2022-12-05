package com.augurit.agcloud.agcom.agsupport.sc.portal.config.service.impl;

import com.augurit.agcloud.agcom.agsupport.common.exception.AgCloudException;
import com.augurit.agcloud.agcom.agsupport.common.exception.ExceptionEnum;
import com.augurit.agcloud.agcom.agsupport.common.util.UUIDUtil;
import com.augurit.agcloud.agcom.agsupport.sc.buildingComponent.entity.TreeNode;
import com.augurit.agcloud.agcom.agsupport.sc.buildingComponent.util.TreeUtils;
import com.augurit.agcloud.agcom.agsupport.sc.portal.config.service.ISysConfigService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * @Author: libc
 * @Description: portal 系统配置业务实现类
 * @Date: 2020/12/23 14:25
 * @Version: 1.0
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService {

    private final Logger logger = LoggerFactory.getLogger(SysConfigServiceImpl.class);

    /**
     * 项目统一 文件存储路径
     */
    @Value("${upload.filePath}")
    private String baseFilePath;

    /**
     * portal统一 文件存储相对路径
     */
    private final String BASE_PORTAL_FILE_PATH = "portal";

    /**
     * 门户系统配置（左边）菜单数组
     */
    private final String[] PORTAL_SYS_CONFIG_MENU_ARRAY = new String[]{"API&SDK", "tool"};

    /**
     * 菜单对应的目录下md文件（节点的说明文档）
     */
    private final String PORTAL_TREE_MENU_MD_FILE_DEFAULT_NAME = "default.md";

    /**
     * @param dirType 目录类型
     * @return List<TreeNode>
     * @Author: libc
     * @Date: 2020/12/23 15:34
     * @tips: 根据目录类型获取对应目录树集合（本地文件目录）
     */
    @Override
    public List<TreeNode<File>> tree(String dirType) {
        if (StringUtils.isEmpty(dirType)) {
            throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        // 获取要查询根路径
        StringBuffer rootPath = new StringBuffer(baseFilePath).append(BASE_PORTAL_FILE_PATH);
        if (PORTAL_SYS_CONFIG_MENU_ARRAY[0].equals(dirType)) {
            // API&SDK菜单
            rootPath.append(File.separator).append(dirType).append(File.separator).append("后台接口");
        } else if (PORTAL_SYS_CONFIG_MENU_ARRAY[1].equals(dirType)) {
            // tool（工具）菜单
            rootPath.append(File.separator).append(dirType);
        } else {
            logger.error(ExceptionEnum.INVALID_PARAM_ERROR.getMessage());
            throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        try {
            List<TreeNode<File>> treeNodeList = new ArrayList<>();
            // 把目录结构转树节点集合
            treeNodeList = getTreeNodeList(treeNodeList, rootPath.toString(), "0");
            // 将树节点集合转树结构集合
            treeNodeList = TreeUtils.nodeToTree(treeNodeList);
            return treeNodeList;
        } catch (Exception e) {
            logger.error("目录结构转树结构集合异常！", e);
            throw new AgCloudException(500, "目录结构转树结构集合异常！");
        }


    }

    /**
     * @param file    md文件
     * @param dirPath 对应目录文件路径
     * @Author: libc
     * @Date: 2020/12/28 10:00
     * @tips: 上传节点目录对应的md说明文档
     */
    @Override
    @Transactional
    public void uploadFile(MultipartFile file, String dirPath) {
        if (file == null) {
            logger.error(ExceptionEnum.INVALID_FILE_TYPE.getMessage());
            throw new AgCloudException(ExceptionEnum.INVALID_FILE_TYPE);
        }

        if (StringUtils.isEmpty(dirPath)) {
            logger.error(ExceptionEnum.INVALID_PARAM_ERROR.getMessage());
            throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        try {
            // 本地md文件
            File localMDFile = new File(dirPath, PORTAL_TREE_MENU_MD_FILE_DEFAULT_NAME);
            // 判断是否已存在md文件
            if (localMDFile.exists()) {
                // 存在的，直接覆盖
                FileUtils.copyInputStreamToFile(file.getInputStream(), localMDFile);
            } else {
                // 不存在的，创建文件，复制上传文件内容给新建文件 （md文件默认名称： default.md）
                localMDFile.createNewFile();
                FileUtils.copyInputStreamToFile(file.getInputStream(), localMDFile);
            }
        } catch (AgCloudException ae) {
            throw new AgCloudException(ae.getStatus(), ae.getMessage());
        } catch (Exception e) {
            logger.error(ExceptionEnum.FILE_UPLOAD_ERROR.getMessage(), e);
            throw new AgCloudException(ExceptionEnum.FILE_UPLOAD_ERROR);
        }
    }

    /**
     * @param filePath md文档路径
     * @return String
     * @Author: libc
     * @Date: 2020/12/28 16:31
     * @tips: 获取md文件
     */
    @Override
    public String getMdFile(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            throw new AgCloudException(ExceptionEnum.INVALID_PARAM_ERROR);
        }

        File file = new File(filePath);

        InputStreamReader read = null;
        BufferedReader bufferedReader = null;
        InputStream fileInputStream = null;
        StringBuffer strBuff = new StringBuffer();
        String mdFileString = null;
        try {
            fileInputStream = new FileInputStream(file);
            read = new InputStreamReader(fileInputStream, "utf-8");
            bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                strBuff.append(lineTxt);
                strBuff.append("\n");
            }
            mdFileString = strBuff.toString();
        } catch (Exception e) {
            logger.error(ExceptionEnum.FILE_READ_ERROR.getMessage(), e);
            throw new AgCloudException(ExceptionEnum.FILE_READ_ERROR);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    logger.error(ExceptionEnum.STREAM_RESOURCES_CLOSE_ERROR.getMessage(), e);
                    throw new AgCloudException(ExceptionEnum.STREAM_RESOURCES_CLOSE_ERROR);
                }
            }
            if (read != null) {
                try {
                    read.close();
                } catch (IOException e) {
                    logger.error(ExceptionEnum.STREAM_RESOURCES_CLOSE_ERROR.getMessage(), e);
                    throw new AgCloudException(ExceptionEnum.STREAM_RESOURCES_CLOSE_ERROR);
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    logger.error(ExceptionEnum.STREAM_RESOURCES_CLOSE_ERROR.getMessage(), e);
                    throw new AgCloudException(ExceptionEnum.STREAM_RESOURCES_CLOSE_ERROR);
                }
            }
        }
        return mdFileString;
    }

    /**
     * @param dirPath     新增文件夹父级路径
     * @param newFileName 文件夹名称
     * @Author: libc
     * @Date: 2020/12/29 14:39
     * @tips: 新增节点（文件夹）
     */
    @Override
    @Transactional
    public void add(String dirPath, String newFileName) {
        if (StringUtils.isEmpty(dirPath) || StringUtils.isEmpty(newFileName)) {
            logger.error(ExceptionEnum.INVALID_FILE_TYPE.getMessage());
            throw new AgCloudException(ExceptionEnum.INVALID_FILE_TYPE);
        }

        try {
            // 新文件夹
            File newDirFile = new File(dirPath, newFileName);
            // 判断文件夹是否已存在
            if (newDirFile.exists()) {
                // 存在的，直接抛出异常
                logger.error("文件夹名称不能重复！");
                throw new AgCloudException(500, "文件夹名称不能重复！");
            } else {
                // 不存在的，创建文件夹
                FileUtils.forceMkdir(newDirFile);
            }
        } catch (AgCloudException ae) {
            throw new AgCloudException(ae.getStatus(), ae.getMessage());
        } catch (Exception e) {
            logger.error(ExceptionEnum.INSERT_OPERATION_FAIL.getMessage(), e);
            throw new AgCloudException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    /**
     * @param dirPath 要删除的文件夹路径
     * @Author: libc
     * @Date: 2020/12/29 15:38
     * @tips: 删除节点（文件夹）
     */
    @Override
    @Transactional
    public void delete(String dirPath) {
        if (StringUtils.isEmpty(dirPath)) {
            logger.error(ExceptionEnum.INVALID_FILE_TYPE.getMessage());
            throw new AgCloudException(ExceptionEnum.INVALID_FILE_TYPE);
        }

        try {
            // 要删除文件夹
            File delDirFile = new File(dirPath);
            // 判断文件夹是否已存在
            if (delDirFile.exists()) {
                // 存在的，删除文件或文件夹
                FileUtils.deleteQuietly(delDirFile);
            } else {
                // 不存在，抛异常
                logger.error(ExceptionEnum.INVALID_FILE_TYPE.getMessage());
                throw new AgCloudException(ExceptionEnum.INVALID_FILE_TYPE);
            }
        } catch (AgCloudException ae) {
            throw new AgCloudException(ae.getStatus(), ae.getMessage());
        } catch (Exception e) {
            logger.error(ExceptionEnum.INSERT_OPERATION_FAIL.getMessage(), e);
            throw new AgCloudException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    /**
     * @param dirPath        要修改的原文件夹路径
     * @param modifyFileName 新的文件夹名称
     * @Author: libc
     * @Date: 2020/12/29 16:45
     * @tips: 修改节点（文件夹）
     */
    @Override
    @Transactional
    public void update(String dirPath, String modifyFileName) {
        if (StringUtils.isEmpty(dirPath) || StringUtils.isEmpty(modifyFileName)) {
            logger.error(ExceptionEnum.INVALID_FILE_TYPE.getMessage());
            throw new AgCloudException(ExceptionEnum.INVALID_FILE_TYPE);
        }

        File oldFile = new File(dirPath);
        if (!oldFile.exists()) {
            // 原文件不存在， 抛异常
            logger.error(ExceptionEnum.INVALID_FILE_TYPE.getMessage());
            throw new AgCloudException(ExceptionEnum.INVALID_FILE_TYPE);
        }

        try {
            // 获取要修改文件夹的父文件目录
            File oldParentFile = oldFile.getParentFile();
            // 新文件夹
            File newDirFile = new File(oldParentFile, modifyFileName);
            // 判断文件夹是否已存在
            if (newDirFile.exists()) {
                // 存在的，直接抛出异常
                logger.error("文件夹名称不能重复！");
                throw new AgCloudException(500, "文件夹名称不能重复！");

            } else {
                // 不存在的，创建文件夹
                FileUtils.forceMkdir(newDirFile);
                // 复制原文件底下所有子文件到新文件夹中
                FileUtils.copyDirectory(oldFile, newDirFile);
                // 删除原文件以及其底下所有子文件
                FileUtils.deleteQuietly(oldFile);
            }
        } catch (AgCloudException ae) {
            throw new AgCloudException(ae.getStatus(), ae.getMessage());
        } catch (Exception e) {
            logger.error(ExceptionEnum.INSERT_OPERATION_FAIL.getMessage(), e);
            throw new AgCloudException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    /**
     * @param treeNodeList 树节点集合
     * @param dirPath      指定的文件夹路径
     * @param pid          父节点key
     * @return List<TreeNode < File>> 树节点集合<文件对象>
     * @Author: libc
     * @Date: 2020/12/24 16:25
     * @tips: 根据指定文件夹路径把目录结构转树节点集合
     */
    private List<TreeNode<File>> getTreeNodeList(List<TreeNode<File>> treeNodeList, String dirPath, String pid) {
        File file = new File(dirPath);
        if (file == null || !file.exists() || file.isFile()) {
            // 找不到对应文件
            return treeNodeList;
        }

        if (file.isDirectory()) {
            File[] fileArr = file.listFiles();
            List<File> fileList = Arrays.asList(fileArr);
            //对读到的本地文件夹进行排序
            Collections.sort(fileList, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1.isDirectory() && o2.isFile())
                        return -1;
                    if (o1.isFile() && o2.isDirectory())
                        return 1;
                    return o1.getName().compareTo(o2.getName());
                }
            });

            for (int i = 0; i < fileList.size(); i++) {
                if (fileList.get(i).isFile()) {
                    continue;
                }
                TreeNode node = new TreeNode();
                node.setParentKey(pid);
                // 子文件的key
                String subKey = UUIDUtil.getUUID();
                node.setKey(subKey);
                node.setTitle(fileList.get(i).getName());
                node.setNode(fileList.get(i));
                treeNodeList.add(node);
                //进行递归，此时的pid为上一级的id
                getTreeNodeList(treeNodeList, fileList.get(i).getPath(), subKey);
            }
        }
        return treeNodeList;
    }


}
