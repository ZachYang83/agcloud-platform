package com.augurit.agcloud.agcom.agsupport.sc.marker.util;


import org.apache.commons.lang3.StringUtils;



/**
 * Created by yzq on 2019-10-20.
 */
public class tollutil {

    /**
     * base64开头部分
     * @param suffix
     * @return
     */
    public static String imgsBase (String suffix){
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("data:image/");
        sBuilder.append(suffix);
        sBuilder.append(";base64,");
        return sBuilder.toString();
    }

    /**
     * 获取文件扩展名称
     * @param fileName
     * @return
     */
    public static String suffixName (String fileName){
        if (StringUtils.isNotBlank(fileName)) {
            return fileName.trim().substring(fileName.trim().lastIndexOf(".")+1);
        }
        return null;
    }
}
