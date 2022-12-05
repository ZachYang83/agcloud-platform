package com.augurit.agcloud.agcom.agsupport.sc.bimManager.util;

import com.augurit.agcloud.agcom.agsupport.common.exception.SourceException;
import com.augurit.agcloud.agcom.agsupport.common.util.UUIDUtil;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ExtraDataRecord;
import net.lingala.zip4j.model.FileHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: qinyg
 * @Date: 2020/9/22
 * @tips:
 */
public class BimZipUtils {
    private static final Logger log = LoggerFactory.getLogger(BimZipUtils.class);

    /**
     * 获取系统编码 （解决文件名乱码问题）
     */
    private final static String SYSTEM_ENCODING = System.getProperty("sun.jnu.encoding");


    /**
     * 解决中文乱码问题
     *
     * @param fileHeader
     * @return
     */
    private static String getFileNameFromExtraData(FileHeader fileHeader) {
        /**
         * 通过阅读ZIP的协议文档，我们可以发现，Info-ZIP Unicode Path Extra Field (0x7075)
         * 这个额外信息可以解决我们的问题,据笔者测试，WinRAR和百度压缩等使用GBK作为文件编码的压缩软件，
         * 在这个区域会记录文件名的UTF-8编码的名称，但是因为这个字段不是必要字段，文件名使用UTF-8编码的
         * MacOS归档、Deepin归档等软件不会填充这个信息。
         */
        if (fileHeader.getExtraDataRecords() != null) {
            for (ExtraDataRecord extraDataRecord : fileHeader.getExtraDataRecords()) {
                long identifier = extraDataRecord.getHeader();
                if (identifier == 0x7075) {
                    byte[] bytes = extraDataRecord.getData();
                    ByteBuffer buffer = ByteBuffer.wrap(bytes);
                    byte version = buffer.get();
                    assert (version == 1);
                    int crc32 = buffer.getInt();
                    return new String(bytes, 5, buffer.remaining(), StandardCharsets.UTF_8);
                }
            }
        }
        return fileHeader.getFileName();
    }

    /**
     * 从压缩包里面获取glb，name去除掉压缩包的前缀，FileBytes是glb字节
     *
     * @param file
     * @param outPath
     * @return
     * @throws IOException
     */
    public static List<FileEntity> getGlbFileFromZip(MultipartFile file, String outPath) throws IOException {
        //返回值封装
        List<FileEntity> returnList = new ArrayList<>();
        //创建文件
        File zipFile = createZipFile(file, outPath);
        ZipFile zip = new ZipFile(zipFile);
        try {

            //设置编码
            zip.setCharset(Charset.forName("utf-8"));
            zip.getFileHeaders().forEach(v -> {
                String realName = getFileNameFromExtraData(v);
                try {
                    zip.extractFile(v, outPath, realName);
                } catch (ZipException e) {
                    log.info("-------压缩文件识别错误，请重新压缩-------" + e.getMessage());
                    throw new SourceException("压缩文件识别错误，请重新压缩");
                }
                //是文件就添加，不需要文件夹
                File localFile = new File(outPath, realName);
                if (localFile.isFile()) {
                    FileEntity entity = new FileEntity();
                    //截取文件名
                    entity.setName(realName.substring(realName.lastIndexOf("/") + 1));
                    entity.setFile(localFile);
                    returnList.add(entity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return returnList;
    }


    /**
     * 获取zip房屋模型
     *
     * @param file
     * @param outPath
     * @return
     * @throws IOException
     */
    public static Map<String, Object> getHouseRvtFileFromZip(MultipartFile file, String outPath) throws IOException {
        //返回值封装
        Map<String, Object> returnMap = new HashMap<>();
        //创建文件
        File zipFile = createZipFile(file, outPath);
        ZipFile zip = new ZipFile(zipFile);
        try {
            List<FileEntity> modelFiles = new ArrayList<>();
            FileEntity excelFile = new FileEntity();


            //设置编码
            zip.setCharset(Charset.forName("utf-8"));
            zip.getFileHeaders().forEach(v -> {
                String realName = getFileNameFromExtraData(v);
                try {
                    zip.extractFile(v, outPath, realName);
                } catch (ZipException e) {
                    log.info("-------压缩文件识别错误，请重新压缩-------" + e.getMessage());
                    throw new SourceException("压缩文件识别错误，请重新压缩");
                }

                //是否是excel
                if (realName.endsWith("xls") || realName.endsWith("xlsx")) {
                    //去掉第一个文件名称
                    excelFile.setName(realName.substring(realName.indexOf("/") + 1));
                    excelFile.setFile(new File(outPath, realName));
                } else {
                    //是文件就添加，不需要文件夹
                    File localFile = new File(outPath, realName);
                    if (localFile.isFile()) {
                        FileEntity entity = new FileEntity();
                        //去掉第一个文件名称
                        entity.setName(realName.substring(realName.indexOf("/") + 1));
                        entity.setFile(localFile);
                        modelFiles.add(entity);
                    }
                }
            });
            returnMap.put("excel", excelFile);
            returnMap.put("modelFiles", modelFiles);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return returnMap;
    }

    /**
     * 读取zip房屋模型
     *
     * @param file
     * @param outPath
     * @return
     * @throws IOException
     */
    public static Map<String, Object> getHouse3dtilesFileFromZip(MultipartFile file, String outPath) throws IOException {
        //返回值封装
        Map<String, Object> returnMap = new HashMap<>();
        //创建文件
        File zipFile = createZipFile(file, outPath);
        ZipFile zip = new ZipFile(zipFile);
        try {
            List<FileEntity> modelFiles = new ArrayList<>();
            List<FileEntity> models = new ArrayList<>();

            //设置编码
            zip.setCharset(Charset.forName("utf-8"));
            zip.getFileHeaders().forEach(v -> {
                String realName = getFileNameFromExtraData(v);
                try {
                    zip.extractFile(v, outPath, realName);
                } catch (ZipException e) {
                    log.info("-------压缩文件识别错误，请重新压缩-------" + e.getMessage());
                    throw new SourceException("压缩文件识别错误，请重新压缩");
                }

                // 解决realName反斜杠匹配不上异常问题
                realName = realName.replaceAll("\\\\", "/");

                //是否是模型文件夹，即模型名称
                if (realName.split("/").length == 3 && realName.endsWith("tileset.json")) {
                    FileEntity model = new FileEntity();
                    //去掉第一个文件名称
                    model.setName(realName.substring(realName.indexOf("/") + 1));
                    models.add(model);
                }

                //是文件就添加，不需要文件夹
                File localFile = new File(outPath, realName);
                if (localFile.isFile()) {
                    FileEntity entity = new FileEntity();
                    //去掉第一个文件名称
                    entity.setName(realName.substring(realName.indexOf("/") + 1));
                    entity.setFile(localFile);
                    modelFiles.add(entity);
                }
            });

            returnMap.put("models", models);
            returnMap.put("modelFiles", modelFiles);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return returnMap;
    }

    /**
     * 从压缩包获取材料构件rfa
     *
     * @param file
     * @param outPath
     * @return
     * @throws IOException
     */
    public static Map<String, Object> getMaterialsRfaFileFromZip(MultipartFile file, String outPath) throws IOException {
        //返回值封装
        Map<String, Object> returnMap = new HashMap<>();
        //创建文件
        File zipFile = createZipFile(file, outPath);
        ZipFile zip = new ZipFile(zipFile);
        try {
            List<FileEntity> modelFiles = new ArrayList<>();
            FileEntity excelFile = new FileEntity();

            //设置编码
            zip.setCharset(Charset.forName("utf-8"));
            zip.getFileHeaders().forEach(v -> {
                String realName = getFileNameFromExtraData(v);
                try {
                    zip.extractFile(v, outPath, realName);
                } catch (ZipException e) {
                    log.info("-------压缩文件识别错误，请重新压缩-------" + e.getMessage());
                    throw new SourceException("压缩文件识别错误，请重新压缩");
                }

                //是否是excel
                if (realName.endsWith("xls") || realName.endsWith("xlsx")) {
                    excelFile.setName(realName);
                    excelFile.setFile(new File(outPath, realName));
                } else {
                    //是文件就添加，不需要文件夹
                    File localFile = new File(outPath, realName);
                    if (localFile.isFile()) {
                        FileEntity entity = new FileEntity();
                        entity.setName(realName);
                        entity.setFile(localFile);
                        modelFiles.add(entity);
                    }
                }
            });

            returnMap.put("excel", excelFile);
            returnMap.put("modelFiles", modelFiles);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return returnMap;
    }

    public static Map<String, Object> getMaterialsGlbFileFromZip(MultipartFile file, String outPath) throws IOException {
        //返回值封装
        Map<String, Object> returnMap = new HashMap<>();
        File zipFile = createZipFile(file, outPath);
        ZipFile zip = new ZipFile(zipFile);
        try {
            List<FileEntity> modelFiles = new ArrayList<>();

            //设置编码
            zip.setCharset(Charset.forName("utf-8"));
            zip.getFileHeaders().forEach(v -> {
                String realName = getFileNameFromExtraData(v);
                try {
                    zip.extractFile(v, outPath, realName);
                } catch (ZipException e) {
                    log.info("-------压缩文件识别错误，请重新压缩-------" + e.getMessage());
                    throw new SourceException("压缩文件识别错误，请重新压缩");
                }
                //是文件就添加，不需要文件夹
                File localFile = new File(outPath, realName);
                if (localFile.isFile()) {
                    FileEntity entity = new FileEntity();
                    //去掉第一个文件名称
                    entity.setName(realName.substring(realName.indexOf("/") + 1));
                    entity.setFile(localFile);
                    modelFiles.add(entity);
                }
            });
            returnMap.put("modelFiles", modelFiles);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
        return returnMap;
    }

    private static File createZipFile(MultipartFile file, String outPath) throws IOException {
        //创建文件
        File outFileDir = new File(outPath);
        if (!outFileDir.exists()) {
            outFileDir.mkdirs();
        }
        String uploadFileName = file.getOriginalFilename();
        //创建zip文件
        File zipFile = new File(outPath + File.separator + uploadFileName);
        if (!zipFile.exists()) {
            zipFile.mkdir();
        }
        file.transferTo(zipFile);
        return zipFile;
    }

    /**
     * @param file            zip文件
     * @param outPath         解压基础目录路径
     * @param isWithoutZipDir 是否去除zip文件夹名称（写出文件少一层目录）
     * @param isAddUniqueness 是否保证模型文件唯一 （加一层唯一标识的目录）
     * @return map
     * @Author: libc
     * @Date: 2020/11/19 15:38
     * @tips: 从zip文件中获取3dtiles文件以及其他文件
     */
    public static Map<String, Object> get3dtilesFileFromZip(MultipartFile file, String outPath, Boolean isWithoutZipDir, Boolean isAddUniqueness) throws IOException {
        //返回值封装
        Map<String, Object> returnMap = new HashMap<>();
        //创建文件
        File zipFile = createZipFile(file, outPath);
        ZipFile zip = new ZipFile(zipFile);
        try {
            List<FileEntity> modelFiles = new ArrayList<>();
            List<FileEntity> models = new ArrayList<>();

            //设置编码 （文件名可能会乱码）
            zip.setCharset(Charset.forName("GBK".equals(SYSTEM_ENCODING) ? SYSTEM_ENCODING : "UTF-8"));
            String uuidDir = UUIDUtil.getUUID();
            zip.getFileHeaders().forEach(v -> {
                String realName = getFileNameFromExtraData(v);

                // 解决realName反斜杠匹配不上异常问题
                realName = realName.replaceAll("\\\\", "/");
                if (isWithoutZipDir) {
                    realName = realName.substring(realName.indexOf("/") + 1);
                }
                if (isAddUniqueness) {
                    realName = uuidDir + "/" + realName;
                }

                try {

                    zip.extractFile(v, outPath, realName);
                } catch (ZipException e) {
                    log.error("-------压缩文件识别错误，请重新压缩-------", e);
                    throw new SourceException("压缩文件识别错误，请重新压缩");
                }

                //是否是模型文件夹，即模型名称
                if (realName.endsWith("tileset.json")) {
                    FileEntity model = new FileEntity();
                    //去掉第一个文件名称
                    model.setName(realName);
                    models.add(model);
                }

                //是文件就添加，不需要文件夹
                File localFile = new File(outPath, realName);
                if (localFile.isFile()) {
                    FileEntity entity = new FileEntity();
                    //去掉第一个文件名称
                    entity.setName(realName);
                    entity.setFile(localFile);
                    modelFiles.add(entity);
                }
            });

            returnMap.put("models", models);
            returnMap.put("modelFiles", modelFiles);
        } catch (Exception e) {
            log.error("从zip包提取3dtiles文件失败！", e);
            throw new IOException(e.getMessage());
        }
        return returnMap;
    }

    /**
     * @param file    zip文件
     * @param outPath 解压基础目录路径
     * @return map
     * @Author: libc
     * @Date: 2020/11/19 15:38
     * @tips: 从zip文件中获取3dtiles文件以及其他文件
     */
    public static Map<String, Object> get3dtilesFileFromZip(MultipartFile file, String outPath) throws IOException {
        return get3dtilesFileFromZip(file, outPath, false, false);
    }

    /**
     * @param file            zip文件
     * @param outPath         解压基础目录路径
     * @param isWithoutZipDir 是否去除zip文件夹名称（写出文件少一层目录）
     * @return map
     * @Author: libc
     * @Date: 2020/11/19 15:38
     * @tips: 从zip文件中获取3dtiles文件以及其他文件
     */
    public static Map<String, Object> get3dtilesFileFromZip(MultipartFile file, String outPath, Boolean isWithoutZipDir) throws IOException {
        return get3dtilesFileFromZip(file, outPath, isWithoutZipDir, false);
    }

}
