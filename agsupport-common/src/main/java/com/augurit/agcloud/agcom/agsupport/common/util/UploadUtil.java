package com.augurit.agcloud.agcom.agsupport.common.util;

import com.common.util.Common;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Date;
import java.util.Map;


/**
 * Created by Augurit on 2017-07-19.
 */
public class UploadUtil {
    public static String getUploadRelativePath(){
        return "upload/";
    }
    public static String getUploadAbsolutePath(){
/*        StringBuilder sb = new StringBuilder();
        URL jarUrl = AgsupportApplication.class.getProtectionDomain().getCodeSource().getLocation();
        String jarPath = jarUrl.getFile();
        String jarTag = ".jar!";
        String[] arrJarPath = jarPath.split("/");
        for(int i = 0;i<arrJarPath.length;i++){
            String itemPath = arrJarPath[i];
            if(itemPath.contains(jarTag)){
                break;
            }
            if(!Common.isCheckNull(itemPath)){
                sb.append(itemPath+"/");
            }
        }
        String jarDir = sb.toString();//jar所在目录
        if(jarDir.startsWith("file:/")){
            jarDir = jarDir.substring(6);
        }
        File file = new File(jarDir);
        if(!file.exists()){
           System.out.println("严重的异常:jar目录不存在");
        }
        String parentPath = file.getParent();
        String uploadDir=parentPath.replace("\\","/");
        if(!uploadDir.endsWith("/")){
            uploadDir+="/";
        }
        String uploadAbsolutePath = uploadDir+getUploadRelativePath();*/
        String filePath = Common.getByKey("upload.filePath", "/home/agsupportFiles");
        return filePath;
    }

    /**
     * 上传shape文件的目录
     * @param request
     * @return
     */
    public static String getUploadShapePath(HttpServletRequest request) {
        //默认上传到项目目录下的upload文件夹中的shape文件夹
        //String shapePath = request.getServletContext().getRealPath("\\") + File.separator + "upload" + File.separator + "shape";
        String shapePath = getUploadAbsolutePath()+"shape/";
        return shapePath;
    }

    /**
     * 转换shape文件坐标后存放目录
     * @param request
     * @return
     */
    public static String getTransShapePath(HttpServletRequest request) {
        //默认上传到项目目录下的upload文件夹中的shape文件夹
        //String transformPath = request.getServletContext().getRealPath("\\") + File.separator + "upload" + File.separator + "transform";
        String transformPath =  getUploadAbsolutePath()+"transform/";
        return transformPath;
    }
    /**
     * 读取长传配置文件，以字符串返回结果
     */
    public static String getAttachmentString(InputStream in, int size) {
        InputStream inStream = null;
        BufferedReader bufferedReader = null;
        String rtnString = "";
        try {
            inStream = new BufferedInputStream(in, size);
            StringBuilder sb = new StringBuilder();
            bufferedReader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
            String sCurrentLine = null;
            while ((sCurrentLine = bufferedReader.readLine()) != null) {
                sb.append(sCurrentLine);
            }
            rtnString = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rtnString;
    }

    public static void saveUploadFile(MultipartFile file,String saveDirectory,boolean isReplace) throws Exception{
        if (file.getSize() > 0) {
            File fileDirectory = new File(saveDirectory);
            if(!fileDirectory.exists()){
                fileDirectory.mkdirs();
            }
            String fileName = file.getOriginalFilename();
            if(!isReplace){
                fileName = new Date().getTime() +"_"+fileName; //解决中文问题
            }
            File filePath = new File(saveDirectory + fileName);
            file.transferTo(filePath);
        }
    }

    public static void saveUploadFile(Map<String, MultipartFile> mapFile, String saveDirectory, boolean isReplace) throws Exception{
        for (Map.Entry<String, MultipartFile> entity : mapFile.entrySet()) {
            MultipartFile file = entity.getValue();
            saveUploadFile(file,saveDirectory,isReplace);
        }
    }

}
