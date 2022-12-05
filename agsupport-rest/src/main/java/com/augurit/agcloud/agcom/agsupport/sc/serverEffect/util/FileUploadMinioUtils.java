package com.augurit.agcloud.agcom.agsupport.sc.serverEffect.util;

import com.augurit.agcloud.agcom.agsupport.common.exception.AgCloudException;
import com.augurit.agcloud.agcom.agsupport.common.exception.ExceptionEnum;
import com.augurit.agcloud.agcom.agsupport.common.util.UUIDUtil;
import com.augurit.agcloud.agcom.agsupport.util.VerifyFileTypeUtils;
import com.augurit.agcloud.agcom.agsupport.util.ZipFileUtils;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @Author: libc
 * @Description: minio文件服务器 文件上传工具类
 * @Date: 2020/9/29 14:23
 * @Version: 1.0
 */
@Component
public class FileUploadMinioUtils {


    private static String endpoint; // ip：port minio服务地址

    private static String accessKey; // 账号

    private static String secretKey; // 密码

    @Value("${minio.endpoint}")  // 静态变量只能使用set方法注入配置信息
    private void setEndpoint(String endpoint) {
        FileUploadMinioUtils.endpoint = endpoint;
    }

    @Value("${minio.accessKey}")
    private void setAccessKey(String accessKey) {
        FileUploadMinioUtils.accessKey = accessKey;
    }

    @Value("${minio.secretKey}")
    private void setSecretKey(String secretKey) {
        FileUploadMinioUtils.secretKey = secretKey;
    }

    private static final Logger log = LoggerFactory.getLogger(FileUploadMinioUtils.class);

    //单例模式-内部类实现
    private static class MinioClientHolder {
        private static MinioClient minioClient;

        static {
//            System.out.println(endpoint);
//            System.out.println(accessKey);
//            System.out.println(secretKey);
            try {
                minioClient = new MinioClient(endpoint, accessKey, secretKey);
                log.info("创建minio实例成功！");
            } catch (Exception e) {
                e.printStackTrace();
                log.info("创建minio实例失败！");
                new AgCloudException(500, "创建minio客户端实例失败！");
            }
        }
    }

    /**
     * @return
     * @Author: libc
     * @Date: 2020/9/29 15:36
     * @tips: 获取minio客户端实例
     */
    public static MinioClient getMinioClient() {
        return MinioClientHolder.minioClient;
    }

    /**
     * @param file       上传文件
     * @param bucketName 上传文件存储桶名称
     * @param dirPath    存储文件目录前缀
     * @return
     * @Author: libc
     * @Date: 2020/9/29 16:32
     * @tips: minio文件上传
     */
    public static String upload(MultipartFile file, String bucketName, String dirPath) {
        if (file.isEmpty() || file.getSize() == 0) {
            throw new AgCloudException(ExceptionEnum.INVALID_FILE_TYPE);
        }
        // 测试判断文件类型
        String type = VerifyFileTypeUtils.getFileType(file);
        log.info("上传的文件类型为：type={}", type);

        try {
            // 获取minio实例
            MinioClient minioClient = getMinioClient();

            //是否存在 bucketName 桶
            if (!minioClient.bucketExists(bucketName)) {
                minioClient.makeBucket(bucketName);
            }
            // 文件名
            String fileName = file.getOriginalFilename();
            // 文件存储的相对路径（相对bucketName桶路径）
            StringBuilder path = new StringBuilder();
            if (StringUtils.isNotBlank(dirPath)) {
                path.append(dirPath);
            }
            // 根据文件分类类型添加分类目录层级
            divideDirByType(type, path);
            // 新的文件名称
//            String newName = UUID.randomUUID().toString().replaceAll("-", "") + fileName.substring(fileName.lastIndexOf("."));
            String newName = UUIDUtil.getUUID() + fileName.substring(fileName.lastIndexOf("."));
            path.append("/");
            path.append(newName);

            //上传
            minioClient.putObject(bucketName, path.toString(), file.getInputStream(), new PutObjectOptions(file.getInputStream().available(), -1));
            log.info("minio服务器文件上传成功！");
            //文件访问路径
            String url = minioClient.getObjectUrl(bucketName, path.toString());
            return url;
        } catch (Exception e) {
            log.error("minio服务器文件上传失败！");
            e.printStackTrace();
            throw new AgCloudException(ExceptionEnum.FILE_SERVER_ERROR);
        }
    }

    /**
     *
     * @Author: libc
     * @Date: 2020/10/27 16:49
     * @tips: 根据文件分类类型添加分类目录层级
     * @param type 文件分类的类型
     * @param path 拼接文件存放路径的字符串
     * @return
     */
    private static void divideDirByType(String type, StringBuilder path) {
        if (VerifyFileTypeUtils.IMAGE_TYPE.equals(type)) {
            //图片
            path.append("/img");
        } else if (VerifyFileTypeUtils.AUDIO_TYPE.equals(type)) {
            //音频
            path.append("/audio");
        } else if (VerifyFileTypeUtils.VIDEO_TYPE.equals(type)) {
            //视频
            path.append("/video");
        } else if (VerifyFileTypeUtils.DOC_TYPE.equals(type)) {
            //文档
            path.append("/doc");
        } else {
            throw new AgCloudException(400, "暂不支持该文件类型的上传！");
        }
    }


}
