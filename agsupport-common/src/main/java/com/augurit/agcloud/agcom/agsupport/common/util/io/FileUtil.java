package com.augurit.agcloud.agcom.agsupport.common.util.io;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.agcom.agsupport.common.util.UploadUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 *
 * @Project Augurit
 * @Author LiRuiFa
 * @ClassName FileUtil
 * @Date 2019/5/27 10:46
 * @Version 1.0
 **/
public class FileUtil {
    //URL路径
    private static String urlPath = UploadUtil.getUploadRelativePath();
    //文件路径初始化
    private static String rootPath = UploadUtil.getUploadAbsolutePath();

    private static String uploadFilePath = "/home/agsupportFiles/";

    /**
     * 获取文件保存根路径
     *
     * @return
     */
    public static String getPath() {
        return rootPath;
    }

    /**
     * 获取项目访问路径
     *
     * @return
     */
    public static String getUrl() {
        return urlPath;
    }

    /**
     * 获取文件信息
     *
     * @param multipartFile
     * @return
     */
    public static FileEntity getFileEntity(MultipartFile multipartFile) {
        try {
            if (null == multipartFile) return null;
            double kb = ((double) multipartFile.getSize()) / 1024;
            double mb = ((double) multipartFile.getSize()) / 1024 / 1024;
            return new FileEntity.Builder()
                    .setFileName(multipartFile.getOriginalFilename().substring(0, multipartFile.getOriginalFilename().lastIndexOf(".")))
                    .setFileType(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1, multipartFile.getOriginalFilename().length()))
                    .setFileSize(kb < 1024 ? (kb + "KB") : (mb + "M"))
                    .setFileCreateTime(new Date())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取文件信息
     *
     * @param file
     * @return
     */
    public static FileEntity getFileEntity(File file) {
        try {
            if (null == file) return null;
            double kb = ((double) file.length()) / 1024;
            double mb = ((double) file.length()) / 1024 / 1024;
            return new FileEntity.Builder()
                    .setFileName(file.getName().substring(0, file.getName().lastIndexOf(".")))
                    .setFileType(file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()))
                    .setFileSize(kb < 1024 ? (kb + "KB") : (mb + "M"))
                    .setFileCreateTime(new Date())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取文件信息
     *
     * @param multipartFile
     * @param folder
     * @return
     * @throws Exception
     */
    public static UploadFile getUploadFile(MultipartFile multipartFile, String folder) throws Exception {
        try {
            if (null == multipartFile) throw new Exception("multipartFile is not null!");
            if (StringUtils.isBlank(folder)) folder = "file";
            UploadFile uploadFile = new UploadFile();
            uploadFile.setId(UUID.randomUUID().toString());
            uploadFile.setLength((double) multipartFile.getSize());
            uploadFile.setName(multipartFile.getOriginalFilename().substring(0, multipartFile.getOriginalFilename().lastIndexOf(".")));

            uploadFile.setAlias(getRandom());
            uploadFile.setExtension(multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1, multipartFile.getOriginalFilename().length()));

            //文件保存路径
            String path = convertSymbolPath(new StringBuilder()
                    .append(File.separator)
                    .append(folder)
                    .append(File.separator)
                    .append(new SimpleDateFormat("yyyyMMdd").format(new Date()))//日期分割
                    .append(File.separator)
                    .append(uploadFile.getAlias())//文件新名称
                    .append(".")
                    .append(uploadFile.getExtension())
                    .toString());
            uploadFile.setPath(path);
            uploadFile.setUrl(toUrl(path));
            uploadFile.setUploadTime(new Date());
            return uploadFile;
        } catch (Exception e) {
            throw new Exception("获取文件信息异常：" + e.getMessage());
        }
    }

    /**
     * 获取文件信息
     *
     * @param file
     * @param folder
     * @return
     * @throws Exception
     */
    public static UploadFile getUploadFile(File file, String folder) throws Exception {
        try {
            if (null == file) throw new Exception("file is not null!");
            if (StringUtils.isBlank(folder)) folder = "file";
            UploadFile uploadFile = new UploadFile();
            uploadFile.setId(UUID.randomUUID().toString());
            uploadFile.setLength((double) file.length());
            uploadFile.setName(file.getName().substring(0, file.getName().lastIndexOf(".")));
            uploadFile.setAlias(getRandom());
            uploadFile.setExtension(file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()));
            //文件保存路径
            String path = convertSymbolPath(new StringBuilder()
                    .append(File.separator)
                    .append(folder)
                    .append(File.separator)
                    .append(new SimpleDateFormat("yyyyMMdd").format(new Date()))//日期分割
                    .append(File.separator)
                    .append(uploadFile.getAlias())//文件新名称
                    .append(".")
                    .append(uploadFile.getExtension())
                    .toString());
            uploadFile.setPath(path);
            uploadFile.setUrl(toUrl(path));
            uploadFile.setUploadTime(new Date());

            return uploadFile;
        } catch (Exception e) {
            throw new Exception("获取文件信息异常：" + e.getMessage());
        }
    }

    public static UploadFile getUploadFiles(File file, String folder) throws Exception {
        try {
            if (null == file) throw new Exception("file is not null!");
            if (StringUtils.isBlank(folder)) folder = "file";
            UploadFile uploadFile = new UploadFile();
            uploadFile.setId(UUID.randomUUID().toString());
            uploadFile.setLength((double) file.length());
            uploadFile.setName(file.getName().substring(0, file.getName().lastIndexOf(".")));
            uploadFile.setAlias(getRandom());
            uploadFile.setExtension(file.getName().substring(file.getName().lastIndexOf(".") + 1, (file.getName()).length()));

            //文件保存路径
            String path = new StringBuilder()
                    .append(folder)
                    .append(File.separator)
                    .append(uploadFile.getName())//文件新名称
                    .append(".")
                    .append(uploadFile.getExtension())
                    .toString();
            uploadFile.setPath(path);
            uploadFile.setUrl(toUrl(path));
            uploadFile.setUploadTime(new Date());
            return uploadFile;
        } catch (Exception e) {
            throw new Exception("获取文件信息异常：" + e.getMessage());
        }
    }

    /**
     * 获取文件的网络路径
     *
     * @param filePath 文件系统存储路径
     * @return
     */
    public static String toUrl(String filePath) throws Exception {
        try {
            StringBuilder newPath = new StringBuilder();
            char chars[] = (urlPath + filePath).toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (i == 0 && (chars[i] == '/' || chars[i] == '\\')) {
                    newPath.append("/");
                    continue;
                }
                if (i != 0 && (chars[i] == '/' || chars[i] == '\\') && (chars[i - 1] == '/' || chars[i - 1] == '\\')) {
                    continue;
                }
                if (i != 0 && (chars[i] == '/' || chars[i] == '\\') && (chars[i - 1] != '/' || chars[i - 1] != '\\')) {
                    newPath.append("/");
                    continue;
                }
                newPath.append(chars[i]);
            }
            return newPath.toString();
        } catch (Exception e) {
            throw new Exception("获取文件的网络路径异常：" + e.getMessage());
        }
    }

    /**
     * 获取随机数字字符串
     *
     * @return
     */
    public static String getRandom() {
        Random random = new Random();
        // 生成随机文件名：当前年月日时分秒+五位随机数（为了在实际项目中防止文件同名而进行的处理）
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000; // 获取随机数
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String nowTimeStr = sDateFormat.format(new Date()); // 当前时间
        return nowTimeStr + rannum;
    }

    /**
     * 创建文件夹
     *
     * @param folderPath 文件夹路径
     * @return
     * @throws Exception
     */
    public static boolean createFolder(String folderPath) throws Exception {
        try {
            if (StringUtils.isBlank(folderPath))
                throw new Exception("folderPath is not null!");
            File dirPath = new File(folderPath);
            //如果文件夹不存在，则创建文件夹
            if (!dirPath.exists()) {
                dirPath.mkdirs();
            }
            return true;
        } catch (Exception e) {
            throw new Exception("创建文件夹异常：" + e.getMessage());
        }
    }

    /**
     * 创建文件所在的文件夹
     *
     * @param fileFolder
     * @return
     * @throws Exception
     */
    public static boolean createFileFolder(String fileFolder) throws Exception {
        try {
            if (StringUtils.isBlank(fileFolder))
                throw new Exception("fileFolder is not null!");
            int dir = fileFolder.lastIndexOf(File.separator);
            File dirPath = new File(fileFolder.substring(0, dir));
            //如果文件夹不存在，则创建文件夹
            if (!dirPath.exists()) {
                dirPath.mkdirs();
            }
            return true;
        } catch (Exception e) {
            throw new Exception("创建文件夹异常：" + e.getMessage());
        }
    }

    /**
     * 创建文件, 如果文件夹不存在则创建文件夹
     *
     * @param file file类
     */
    public static File createFile(File file) {
        if (file.exists() && file.isFile()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }
        File parentFile = file.getParentFile();
        if (parentFile.exists()) {
            if (parentFile.isFile()) {
                parentFile.delete();
                parentFile.mkdirs();
            }
        } else {
            parentFile.mkdirs();
        }
        try {
            file.createNewFile();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除文件夹
     *
     * @param folderPath
     * @return
     * @throws Exception
     */
    public static boolean deleteFolder(String folderPath) throws Exception {
        try {
            if (StringUtils.isBlank(folderPath))
                throw new Exception("folderPath is not null!");
            deleteAllFile(folderPath); //删除完里面所有内容
            File myFilePath = new File(folderPath);
            myFilePath.delete(); //删除空文件夹
            if (!(new File(folderPath).exists())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new Exception("删除文件夹异常：" + e.getMessage());
        }
    }

    /**
     * 判断是否存在问文件或者文件夹
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static boolean checkFileOrFolder(String path) {
        try {
            if (StringUtils.isBlank(path))
                return false;
            File file = new File(path);
            if (file.exists() || file.isDirectory()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除文件夹下的文件
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static boolean deleteAllFile(String filePath) throws Exception {
        try {
            if (StringUtils.isBlank(filePath))
                throw new Exception("filePath is not null!");
            boolean flag = false;
            File file = new File(filePath);
            if (!file.exists()) {
                return flag;
            }
            if (!file.isDirectory()) {
                return flag;
            }
            String[] tempList = file.list();
            File temp = null;
            for (int i = 0; i < tempList.length; i++) {
                if (filePath.endsWith(File.separator)) {
                    temp = new File(filePath + tempList[i]);
                } else {
                    temp = new File(filePath + File.separator + tempList[i]);
                }
                if (temp.isFile()) {
                    temp.delete();
                }
                if (temp.isDirectory()) {
                    deleteAllFile(filePath + "/" + tempList[i]);//先删除文件夹里面的文件
                    deleteFolder(filePath + "/" + tempList[i]);//再删除空文件夹
                    flag = true;
                }
            }
            return flag;
        } catch (Exception e) {
            throw new Exception("删除文件夹下的文件异常：" + e.getMessage());
        }
    }

    /**
     * 获取文件夹父路径
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String getParent(String filePath) throws Exception {
        try {
            if (StringUtils.isBlank(filePath))
                throw new Exception("路径不能为空!");
            File file = new File(filePath);
            if (null == file)
                throw new Exception("文件不存在!");
            if (!file.exists())
                throw new Exception("文件不存在!");
            return file.getParent();
        } catch (Exception e) {
            throw new Exception("获取文件父节点失败: " + e.getMessage());
        }
    }

    /**
     * 保存文件
     *
     * @param file     文件流
     * @param savePath 文件路径
     * @return
     * @throws Exception
     */
    public static boolean saveFile(File file, String savePath) throws Exception {
        try {
            //创建文件夹
            createFileFolder(savePath);
            FileUtils.copyFile(file, new File(savePath));
            return true;
        } catch (Exception e) {
            throw new Exception("保存文件失败：" + e.getMessage());
        }
    }


    /**
     * 保存文件
     *
     * @param multipartFile 文件流
     * @param savePath      文件路径
     * @return
     * @throws Exception
     */
    public static boolean saveMultipartFile(MultipartFile multipartFile, String savePath) throws Exception {
        //保存文件
        InputStream in = null;
        try {
            //创建文件夹
            createFileFolder(savePath);
            in = multipartFile.getInputStream();
            File out = new File(savePath);
            FileUtils.copyInputStreamToFile(in, out);
            return true;
        } catch (Exception e) {
            throw new Exception("保存文件失败：" + e.getMessage());
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                throw new Exception("保存文件失败：" + e.getMessage());
            }
        }
    }

    /**
     * String转json
     *
     * @param jsonStr
     * @return
     * @throws Exception
     */
    public static Map<String, Object> jsonToMap(String jsonStr) throws Exception {
        try {
            if (StringUtils.isBlank(jsonStr))
                throw new Exception("jsonStr is not null!");
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject json = (JSONObject) JSON.parseObject(jsonStr);
            for (Object k : json.keySet()) {
                Object v = json.get(k);
                if (v instanceof JSONArray) {
                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    Iterator<Object> it = ((JSONArray) v).iterator();
                    while (it.hasNext()) {
                        JSONObject json2 = (JSONObject) it.next();
                        list.add(jsonToMap(json2.toString()));
                    }
                    map.put(k.toString(), list);
                } else {
                    map.put(k.toString(), v);
                }
            }
            return map;
        } catch (Exception e) {
            throw new Exception("Json转Map异常：" + e.getMessage());
        }
    }

    /**
     * 美化json数据
     *
     * @param jsonStr
     * @return
     * @throws Exception
     */
    public static String toBeautifyJson(String jsonStr) throws Exception {
        try {
            if (StringUtils.isBlank(jsonStr))
                throw new Exception("jsonStr is not null!");
            ObjectMapper mapper = new ObjectMapper();
            Object obj = mapper.readValue(jsonStr, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            throw new Exception("格式化json异常：" + e.getMessage());
        }
    }

    /**
     * 读取该根节点下的所有文件
     *
     * @param dir            当前文件流
     * @param fileEntityList 需要保存的集合
     */
    public static void getFileEntityList(File dir, List<FileEntity> fileEntityList) {
        try {
            if (!dir.exists() || null == dir) {
                return;
            }
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    getFileEntityList(new File(dir, children[i]), fileEntityList);
                }
            }
            if (dir.isFile()) {
                double kb = ((double) dir.length()) / 1024;
                double mb = ((double) dir.length()) / 1024 / 1024;
                String link = dir.getPath().replaceAll("\\\\", "/");
                String path = rootPath.replaceAll("\\\\", "/");
                fileEntityList.add(new FileEntity.Builder()
                        .setFileName(dir.getName().substring(0, dir.getName().lastIndexOf(".")))
                        .setFilePath((link.replaceAll(path, "")).replaceAll("/", "\\\\"))
                        .setFileType(dir.getName().substring(dir.getName().lastIndexOf(".") + 1, dir.getName().length()))
                        .setFileLink((urlPath + link.replaceAll(path, "")).replaceAll("//", "/"))
                        .setFileSize(kb < 1024 ? (kb + "KB") : (mb + "M"))
                        .build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**********************************************（文件压缩）***********************************************/
    /**
     * 压缩工具类
     *
     * @Author LRF
     * @ClassName CompressUtil
     * @Date 2019/8/4 20:34
     * @Version 1.0
     **/
    public static class CompressUtil {

        /**
         * 文件解压缩
         *
         * @param oldName
         * @param zipPath
         * @return
         * @throws Exception
         */
        public static boolean unZip(String oldName, String zipPath) throws Exception {
            if (StringUtils.isBlank(zipPath))
                return false;
            if (null == zipPath) {
                return false;
            }
            FileEntity fileEntity = FileUtil.getFileEntity(new File(zipPath));
            if (null == fileEntity) {
                throw new Exception("文件不存在!");
            }

            //判断文件类型(zip/rar)
            if (!fileEntity.getFileType().toLowerCase().equals("zip") && !fileEntity.getFileType().toLowerCase().equals("rar")) {
                throw new Exception("文件解压类型只能为(zip/rar)");
            }
            //解压路径与文件路径一致
            String destDir = zipPath.substring(0, zipPath.lastIndexOf(File.separator));

            // 获取WinRAR.exe的路径，存放路径resources/Plug/WinRAR.exe
            String PlugPath = null;
            try {
                PlugPath = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "Plug/WinRAR.exe").getPath();
            } catch (FileNotFoundException e) {
                throw new Exception("未找到解压工具：" + e.getMessage());
            }

            //判断文件所在路径是否为空
            if (!new File(destDir).exists()) {
                FileUtil.createFileFolder(destDir);
            }

            // 开始调用命令行解压，参数-o+是表示覆盖的意思
            String cmd = PlugPath + " X -o+ " + zipPath + " " + destDir;
            try {
                boolean bool = true;
                Process proc = Runtime.getRuntime().exec(cmd);
                if (proc.waitFor() != 0 && proc.exitValue() == 0)
                    bool = false;
                //想命名的原文件夹的路径
                File file1 = new File(destDir + File.separator + oldName);
                //将原文件夹更改为A，其中路径是必要的。注意
                file1.renameTo(new File(destDir + File.separator + fileEntity.getFileName()));
                FileUtil.deleteFolder(destDir + File.separator + oldName);
                return bool;
            } catch (Exception e) {
                throw new Exception("文件解压缩异常:" + e.getMessage());
            }
        }

        /**
         *
         * @Author: qinyg
         * @Date: 2020/09/24 9:52
         * @tips: 解压zip和rar，把zip和rar解压到outPutPath路径下
         * @param file 压缩文件
         * @param outPutPath 解压路径
         * @return  返回压缩包内中的所有文件
         */
        public static List<File> unZip(MultipartFile file, String outPutPath) throws Exception {
            File zipFile = createZipFile(file, outPutPath);

            FileEntity fileEntity = FileUtil.getFileEntity(zipFile);
            if (null == fileEntity) {
                throw new Exception("文件不存在!");
            }

            //判断文件类型(zip/rar)
            if (!fileEntity.getFileType().toLowerCase().equals("zip") && !fileEntity.getFileType().toLowerCase().equals("rar")) {
                throw new Exception("文件解压类型只能为(zip/rar)");
            }
            //判断文件所在路径是否为空
            File direcotry = new File(outPutPath);
            if (!direcotry.exists()) {
                FileUtil.createFileFolder(outPutPath);
            }

            // 获取WinRAR.exe的路径，存放路径resources/Plug/WinRAR.exe
            String PlugPath = null;
            try {
//                String path = ResourceUtils.CLASSPATH_URL_PREFIX + "Plug/WinRAR.exe";
//                PlugPath = ResourceUtils.getFile(path).getPath();
                //指定 逆向工程配置文件
                ClassPathResource classPathResource = new ClassPathResource("Plug/WinRAR.exe");
                //创建Plug/WinRAR.exe
                PlugPath = outPutPath + File.separator + "WinRAR.exe";
                inputStreamToFile(classPathResource.getInputStream(), new File(PlugPath));
            } catch (Exception e) {
                throw new Exception("未找到解压工具：" + e.getMessage());
            }

            String filePath = outPutPath + File.separator + file.getOriginalFilename();
            // 开始调用命令行解压，参数-o+是表示覆盖的意思
            String cmd = PlugPath + " X -o+ " + filePath + " " + outPutPath;
            try {
                Process proc = Runtime.getRuntime().exec(cmd);
                if (proc.waitFor() != 0 && proc.exitValue() == 0){
                    throw new Exception("解压失败");
                }
                //返回解压后的文件
                File[] direcotryFiles = direcotry.listFiles();
                List<File> returnList = new ArrayList<>();
                if(direcotryFiles != null && direcotryFiles.length > 0){
                    for(File direcotryFile: direcotryFiles){
                        if(direcotryFile.isFile()){
                            returnList.add(direcotryFile);
                        }
                        if(direcotryFile.isDirectory()){
                            getChildrenFile(direcotryFile, returnList);
                        }
                    }
                }
                return returnList;
            } catch (Exception e) {
                throw new Exception("文件解压缩异常:" + e.getMessage());
            }
        }

        /**
         * InputStream转 File类型
         * @param ins
         * @param file
         */
        public static void inputStreamToFile(InputStream ins, File file) {
            OutputStream os = null;
            try {
                os = new FileOutputStream(file);
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }

                ins.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(os != null){
                    try{
                        os.close();
                    }catch (Exception e){

                    }
                }
                if(ins != null){
                    try{
                        ins.close();
                    }catch (Exception e){

                    }
                }
            }
        }
        private static void getChildrenFile(File file, List<File> returnList){
            File[] direcotryFiles = file.listFiles();
            if(direcotryFiles != null && direcotryFiles.length > 0){
                for(File direcotryFile: direcotryFiles){
                    if(direcotryFile.isFile()){
                        returnList.add(direcotryFile);
                    }
                    if(direcotryFile.isDirectory()){
                        getChildrenFile(direcotryFile, returnList);
                    }
                }
            }
        }

        private static File createZipFile(MultipartFile file, String outPath) throws IOException {
            //创建文件
            File outFileDir = new File(outPath);
            if (!outFileDir.exists()) {
                //父目录不存在，创建多个
                File parentFile = outFileDir.getParentFile();
                if(!parentFile.exists()){
                    parentFile.mkdirs();
                }
                outFileDir.mkdirs();
            }
            String uploadFileName = file.getOriginalFilename();
            //创建zip文件
            File zipFile = new File(outPath + File.separator + uploadFileName);
            return writeFileToDisk(file, zipFile);
        }

        private static File writeFileToDisk(MultipartFile file, File targetFile) throws IOException {
            if (!targetFile.exists()) {
                if(!targetFile.getParentFile().exists()){
                    targetFile.getParentFile().mkdirs();
                }
                targetFile.createNewFile();
            }
            InputStream stream = null;
            OutputStream out = null;
            try{
                stream = file.getInputStream();
                //开始读,每次读的大小默认为1024个字节
                out = new FileOutputStream(targetFile);
                byte[] b = new byte[1024];
                int i = 0;
                //判断缓冲区返回数据是否已经到了结尾,如果结尾，则返回-1
                while((i = stream.read(b)) != -1){
                    //从字节数组b中将其实位置0，长度为i个字节的内容输出。
                    out.write(b,0,i);
                }
            }catch (Exception e){
                e.printStackTrace();
                throw new IOException(e.getMessage());
            }finally {
                try{
                    if(out != null){
                        out.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(stream != null){
                        stream.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return targetFile;
        }

        public static void deleteFile(String sourcePath){
            if (!org.springframework.util.StringUtils.isEmpty(sourcePath)) {
                //从磁盘删除存储文件
                //win和linux路径转换
                sourcePath = "/".equals(File.separator) ? sourcePath.replaceAll("/", File.separator) : sourcePath.replaceAll("/", File.separator + File.separator);
                File file = new File(sourcePath);
                if (file.exists()) {
                    deleteFile(file);
                    file.delete();
                }
            }
        }


        /**
         * 循环删除文件
         * @param file
         */
        public static void deleteFile(File file){
            if(file.exists()){
                if(file.isFile()){
                    //是文件，直接删除
                    file.delete();
                }
                if(file.isDirectory()){
                    //是文件夹，判断是否有文件
                    File[] files = file.listFiles();
                    if(files != null && files.length > 0){
                        for(File delFile : files){
                            deleteFile(delFile);
                        }
                    }
                    //直接删除文件夹
                    file.delete();
                }
            }
        }

        /**
         * 文件解压缩
         *
         * @param filePath
         * @return
         */
        public static boolean unZip(String filePath) throws Exception {
            if (StringUtils.isBlank(filePath))
                throw new Exception();
            if (null == filePath) {
                return false;
            }
            FileEntity fileEntity = FileUtil.getFileEntity(new File(filePath));
            if (null == fileEntity) {
                throw new Exception("文件不存在!");
            }

            //判断文件类型(zip/rar)
            if (!fileEntity.getFileType().toLowerCase().equals("zip") && !fileEntity.getFileType().toLowerCase().equals("rar")) {
                throw new Exception("文件解压类型只能为(zip/rar)");
            }
            //解压路径与文件路径一致
            String destDir = filePath.substring(0, filePath.lastIndexOf(File.separator));

            // 获取WinRAR.exe的路径，存放路径resources/Plug/WinRAR.exe
            String PlugPath = null;
            try {
                PlugPath = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "Plug/WinRAR.exe").getPath();
            } catch (FileNotFoundException e) {
                throw new Exception("未找到解压工具：" + e.getMessage());
            }

            //判断文件所在路径是否为空
            if (!new File(destDir).exists()) {
                FileUtil.createFileFolder(destDir);
            }

            // 开始调用命令行解压，参数-o+是表示覆盖的意思
            String cmd = PlugPath + " X -o+ " + filePath + " " + destDir;
            try {
                boolean bool = true;
                Process proc = Runtime.getRuntime().exec(cmd);
                if (proc.waitFor() != 0 && proc.exitValue() == 0)
                    bool = false;
                //想命名的原文件夹的路径
                File file1 = new File(destDir + File.separator + fileEntity.getFileName());
                //将原文件夹更改为A，其中路径是必要的。注意
                file1.renameTo(new File(filePath));
                FileUtil.deleteAllFile(filePath);
                return bool;
            } catch (Exception e) {
                throw new Exception("文件解压缩异常:" + e.getMessage());
            }
        }


        /**
         * 压缩
         *
         * @param filePath 需要压缩的文件夹路径
         * @throws Exception 压缩失败会抛出异常
         */
        public static boolean zip(String filePath)
                throws Exception {
            if (StringUtils.isBlank(filePath)) {
                throw new Exception("filePath is not null!");
            }
            File file = new File(filePath);
            if (!file.exists()) {
                throw new Exception("file is not find!");
            }
            ZipOutputStream zos = null;
            try {
                FileOutputStream out = new FileOutputStream(new File(filePath + ".zip"));
                zos = new ZipOutputStream(out);
                compress(file, zos, file.getName(), true);
                return true;
            } catch (Exception e) {
                throw new Exception("文件压缩失败：" + e.getMessage());
            } finally {
                if (zos != null) {
                    try {
                        zos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        /**
         * 辅助压缩
         *
         * @param sourceFile
         * @param zos
         * @param name
         * @param KeepDirStructure
         * @throws Exception
         */
        private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                     boolean KeepDirStructure) throws Exception {
            byte[] buf = new byte[2 * 1024];
            if (sourceFile.isFile()) {
                // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
                zos.putNextEntry(new ZipEntry(name));
                // copy文件到zip输出流中
                int len;
                FileInputStream in = new FileInputStream(sourceFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            } else {
                File[] listFiles = sourceFile.listFiles();
                if (listFiles == null || listFiles.length == 0) {
                    // 需要保留原来的文件结构时,需要对空文件夹进行处理
                    if (KeepDirStructure) {
                        // 空文件夹的处理
                        zos.putNextEntry(new ZipEntry(name + "/"));
                        // 没有文件，不需要文件的copy
                        zos.closeEntry();
                    }
                } else {
                    for (File file : listFiles) {
                        // 判断是否需要保留原来的文件结构
                        if (KeepDirStructure) {
                            // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                            // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                            compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                        } else {
                            compress(file, zos, file.getName(), KeepDirStructure);
                        }
                    }
                }
            }
        }
    }

    /**
     * String转json
     *
     * @param jsons
     * @return
     * @throws Exception
     */
    public static Map<String, Object> toMaps(String jsons) throws Exception {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject json = (JSONObject) JSON.parseObject(jsons);
            for (Object k : json.keySet()) {
                Object v = json.get(k);
                if (v instanceof JSONArray) {
                    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                    Iterator<Object> it = ((JSONArray) v).iterator();
                    while (it.hasNext()) {
                        JSONObject json2 = (JSONObject) it.next();
                        list.add(toMaps(json2.toString()));
                    }
                    map.put(k.toString(), list);
                } else {
                    map.put(k.toString(), v);
                }
            }
            return map;
        } catch (Exception e) {
            throw new Exception("String转Json失败: " + e.getMessage());
        }
    }

    public static String loadResouceAsString(String fileName) throws IOException {
        InputStream inputStream = new FileInputStream(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null) {
            sb.append(line + System.lineSeparator());
            line = bufferedReader.readLine();
        }
        inputStream.close();
        return sb.toString();
    }

    public static String convertSymbolPath(String path) {
        String symbol = File.separator;
        StringBuilder newPath = new StringBuilder();
        char chars[] = path.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i == 0 && (chars[i] == '/' || chars[i] == '\\')) {
                newPath.append(symbol);
                continue;
            }
            if (i != 0 && (chars[i] == '/' || chars[i] == '\\') && (chars[i - 1] == '/' || chars[i - 1] == '\\')) {
                continue;
            }
            if (i != 0 && (chars[i] == '/' || chars[i] == '\\') && (chars[i - 1] != '/' || chars[i - 1] != '\\')) {
                newPath.append(symbol);
                continue;
            }
            newPath.append(chars[i]);
        }
        return newPath.toString();
    }

    public static String encodeFileName(String fileNames, HttpServletRequest request) {
        String codedFilename = null;
        try {
            String agent = request.getHeader("USER-AGENT");
            if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident") || null != agent && -1 != agent.indexOf("Edge")) {// ie浏览器及Edge浏览器
                String name = java.net.URLEncoder.encode(fileNames, "UTF-8");
                codedFilename = name;
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {
                // 火狐,Chrome等浏览器
                codedFilename = new String(fileNames.getBytes("UTF-8"), "iso-8859-1");
            } else {
                codedFilename = fileNames;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codedFilename;
    }

    public static void writerFile(byte[] bytes, String fileName, boolean isNeedDownload, HttpServletResponse response) {
        ServletOutputStream outputStream = null;
        try {
            if (bytes != null && bytes.length != 0) {
                response.addHeader("Content-Length", bytes.length + "");
                String s = new String(fileName.getBytes());
                response.addHeader("Content-Disposition", "filename=" + s);
                if(isNeedDownload){
                    //下面展示的是下载的弹框提示，如果要打开，就不需要设置
                    response.setContentType("application/octet-stream");
                }
                outputStream = response.getOutputStream();
                outputStream.write(bytes);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(rootPath);
        System.out.println(urlPath);
        File file = new File("/home/agsupportFiles/test.txt");
        System.out.println(JSON.toJSONString(getFileEntity(file)));
        UploadFile uploadFile = getUploadFile(file, "a");
        System.out.println(JSON.toJSONString(uploadFile));
        System.out.println(saveFile(file, rootPath + uploadFile.getPath()));
    }
}

