package com.augurit.agcloud.agcom.agsupport.util;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: libc
 * @Description: 校验文件类型工具类
 * @Date: 2020/9/29 15:45
 * @Version: 1.0
 */
public class VerifyFileTypeUtils {

    /**
     * 文件后缀类型数组
     */
    private static final String[] IMAGE_TYPES = new String[]{"jpg","jpeg", "png", "gif", "psd","bmp"};
    private static final String[] AUDIO_TYPES = new String[]{"wav", "mp3","wma"};
    private static final String[] VIDEO_TYPES = new String[]{"avi", "mp4","rm", "rmvb", "wmv", "flv", "mpg"};
    private static final String[] DOC_TYPES = new String[]{"pdf", "docx", "doc", "xls" , "xlsx", "ppt" , "pptx","bak"};

    /**
     * 文件分类
     */
    public static final String IMAGE_TYPE = "image";
    public static final String AUDIO_TYPE = "audio";
    public static final String VIDEO_TYPE = "video";
    public static final String DOC_TYPE = "doc";

    /**
     * @param file 文件
     * @return
     * @Author: libc
     * @Date: 2020/9/29 15:32
     * @tips: 根据文件后缀进行分类
     */
    public static String getFileType(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return getFileTypeBySuffix(suffix);
    }

    /**
     * @param suffix 文件后缀名
     * @return
     * @Author: libc
     * @Date: 2020/9/29 15:32
     * @tips: 根据文件后缀进行分类
     */
    public static String getFileTypeBySuffix(String suffix) {
        // 文件类型分类
        if (isValid(suffix, VerifyFileTypeUtils.IMAGE_TYPES)) {
            return VerifyFileTypeUtils.IMAGE_TYPE;
        } else if (isValid(suffix, VerifyFileTypeUtils.AUDIO_TYPES)) {
            return VerifyFileTypeUtils.AUDIO_TYPE;
        } else if (isValid(suffix, VerifyFileTypeUtils.VIDEO_TYPES)) {
            return VerifyFileTypeUtils.VIDEO_TYPE;
        } else if (isValid(suffix, VerifyFileTypeUtils.DOC_TYPES)) {
            return VerifyFileTypeUtils.DOC_TYPE;
        } else {
            return null;
        }
    }

    /**
     *
     * @Author: libc
     * @Date: 2020/9/29 15:35
     * @tips:
     * @param contentType 文件后缀
     * @param allowTypes 匹配的文件类型数组
     * @return
     */
    public static boolean isValid(String contentType, String... allowTypes) {
        if (null == contentType || "".equals(contentType)) {
            return false;
        }
        for (String type : allowTypes) {
            if (contentType.equals(type)) {
                return true;
            }
        }
        return false;
    }
}
