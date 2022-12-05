package com.augurit.agcloud.agcom.agsupport.util;


import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author zhangmingyang
 * @Description: Zip格式解压缩文件工具类
 * @date 2018-11-01 16:15
 */
public class ZipFileUtils {

    private static final Logger log = LoggerFactory.getLogger(ZipFileUtils.class);

    /**
     * 解压zip文件到指定目录
     *
     * @param zipPath zip文件路径
     * @param descDir 目标路径
     * @throws IOException
     */
    public static void unZipFiles(String zipPath, String descDir, String shapeZipName) throws Exception {
        //unZipFiles(new File(zipPath), descDir);
        unZip(zipPath, descDir, shapeZipName);
    }

    /**
     * <p>
     * 解压压缩包
     * </p>
     *
     * @param zipFilePath 压缩文件路径
     * @param destDir     压缩包释放目录
     * @throws Exception
     */
    public static void unZip(String zipFilePath, String destDir, String shapeZipName) throws Exception {
        ZipFile zipFile = new ZipFile(zipFilePath, Charset.forName("GBK"));
        Enumeration<?> emu = zipFile.entries();
        BufferedInputStream bis;
        FileOutputStream fos;
        BufferedOutputStream bos;
        File file, parentFile;
        ZipEntry entry;
        byte[] cache = new byte[1024];
        while (emu.hasMoreElements()) {
            entry = (ZipEntry) emu.nextElement();
            if (entry.isDirectory()) {
                new File(destDir + entry.getName()).mkdirs();
                continue;
            }
            bis = new BufferedInputStream(zipFile.getInputStream(entry));
            // 这个统一处理解压后存入压缩文件名的文件夹内，方便压缩处理
            String entryName = entry.getName();
            if (entryName.contains(shapeZipName)) {
                entryName = entryName.substring(shapeZipName.length() + 1, entryName.length());
            }
            file = new File(destDir + File.separator + shapeZipName + File.separator + entryName);
            parentFile = file.getParentFile();
            if (parentFile != null && (!parentFile.exists())) {
                parentFile.mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos, 1024);
            int nRead = 0;
            while ((nRead = bis.read(cache, 0, 1024)) != -1) {
                fos.write(cache, 0, nRead);
            }
            bos.flush();
            bos.close();
            fos.close();
            bis.close();
        }
        zipFile.close();
    }

    /**
     * @param zipFile zip文件
     * @param descDir 目标路径
     * @throws IOException
     */
    @SuppressWarnings("rawtypes")
    public static void unZipFiles(File zipFile, String descDir) {
        File pathFile = new File(descDir);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        //解决zip文件中有中文目录或者中文文件
        ZipFile zip = null;
        try {
            zip = new ZipFile(zipFile, Charset.forName("GBK"));
            for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                String outPath = descDir;
                if (!entry.isDirectory()) {
                    outPath = (descDir + File.separator + zipEntryName).replaceAll("\\*", "/");
                }

                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                //输出文件路径信息
                //System.out.println(outPath);
                OutputStream out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 文件大小byte 转KB MB GB
     *
     * @param size
     * @return
     */
    public static String getfileSizeDescription(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        } else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        } else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        } else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }


    /**
     * 压缩shape目录文件
     *
     * @param path 需要压缩的文件目录
     */
    public static void zipShape(String path, boolean withDir) {
        log.info("开始压缩文件！");
        File file = new File(path);
        ZipOutputStream zipOutputStream = null;
        try {
            // 如果压缩文件存在，直接覆盖
            zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file.getAbsolutePath() + ".zip")));
            String fileName = withDir ? file.getName() : "";

            compressZip(zipOutputStream, file, fileName);

        } catch (Exception e) {
            log.error("压缩文件失败！");
            e.printStackTrace();
        } finally {
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.close();
                    //zipOutputStream.closeEntry();
                } catch (IOException e) {
                    log.error("压缩文件失败！");
                    e.printStackTrace();
                }
            }
            log.info("压缩文件成功！");
        }

    }

    /**
     * 因为子文件夹中可能还有文件夹，所以进行递归
     */
    public static void compressZip(ZipOutputStream zipOutput, File file, String base) throws IOException {

        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();// 列出所有的文件

            for (File fi : listFiles) {
                if (fi.isDirectory()) {
                    compressZip(zipOutput, fi, base + "/" + fi.getName());
                } else {
                    zip(zipOutput, fi, base);
                }
            }
        } else {
            zip(zipOutput, file, base);
        }
    }

    /**
     * 压缩的具体操作
     */
    public static void zip(ZipOutputStream zipOutput, File file, String base) {
        ZipEntry zEntry = new ZipEntry(base + File.separator + file.getName());
        BufferedInputStream bis = null;
        try {
            zipOutput.putNextEntry(zEntry);
            bis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = bis.read(buffer)) != -1) {
                zipOutput.write(buffer, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    /**
     * 获取路径shp后缀的文件名称
     *
     * @return
     */
    public static String getFileName(String path) {
        File file = new File(path);
        String name = "";
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File f : files) {
                String fileName = f.getName();
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                if ("shp".equals(suffix)) {
                    name = fileName;
                    break;
                }
            }
        }
        return name;
    }


    /**
     * @param inputStream zip文件流
     * @param outPath     输出文件目录
     * @return Boolean 保存是否成功
     * @Author: libc
     * @Date: 2020/9/16 13:40
     * @tips: 解压zip文件并保存解压后文件目录  （UTF-8编码）
     */
    public static Boolean unZipAndSaveFromZip(InputStream inputStream, String outPath) throws IOException {
        log.info("zip文件解压保存开始！");
        Boolean isSucc = false;
        org.apache.tools.zip.ZipFile zip = null;
        File targetFile = null;
        try {
            File outFileDir = new File(outPath);
            if (!outFileDir.exists()) {
                outFileDir.mkdirs();
            }
            //inputStream转File
            targetFile = new File(outPath + File.separator + "targetFile.tmp");
            FileUtils.copyInputStreamToFile(inputStream, targetFile);
            zip = new org.apache.tools.zip.ZipFile(targetFile, "utf-8");
            // 获取zip中文件
            for (Enumeration enumeration = zip.getEntries(); enumeration.hasMoreElements(); ) {
                org.apache.tools.zip.ZipEntry entry = (org.apache.tools.zip.ZipEntry) enumeration.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = zip.getInputStream(entry);

                    if (entry.isDirectory()) {      //处理压缩文件包含文件夹的情况
                        File fileDir = new File(outPath + File.separator + zipEntryName);
                        zipEntryName = zipEntryName.replaceAll("/", "/".equals(File.separator) ? File.separator : File.separator + File.separator);
                        if (!fileDir.exists()) {
                            fileDir.mkdir();
                        }
                        continue;
                    }


                    String[] dirs = zipEntryName.split("/");
                    if (dirs != null && dirs.length > 1) {
                        String dir = zipEntryName.substring(0, zipEntryName.lastIndexOf("/"));
                        dir = dir.replaceAll("/", "/".equals(File.separator) ? File.separator : File.separator + File.separator);
                        File fileDir = new File(outPath + File.separator + dir);
                        if (!fileDir.exists()) {
                            fileDir.mkdirs();
                        }
                    }

                    //封装文件
                    File file = new File(outPath, zipEntryName);
                    file.createNewFile();

                    out = new FileOutputStream(file);
                    byte[] buff = new byte[1024];
                    int len;
                    while ((len = in.read(buff)) > 0) {
                        out.write(buff, 0, len);
                    }
                } catch (Exception e) {
                    log.error("zip文件解压保存失败！");
                    e.printStackTrace();
                    throw new IOException(e.getMessage());
                } finally {
                    closeStream(out, in);
                }
            }
        } catch (Exception e) {
            log.error("zip文件解压保存失败！");
            e.printStackTrace();
            throw new IOException(e.getMessage());
        } finally {
            closeStream(inputStream, zip);
            // 删除临时文件
            if (targetFile != null) {
                targetFile.delete();
            }
        }
        isSucc = true;
        log.info("zip文件解压保存成功！");
        return isSucc;
    }

    /**
     * @param in
     * @param out
     * @return
     * @Author: libc
     * @Date: 2020/10/16 11:31
     * @tips: 关闭文件流
     */
    private static void closeStream(OutputStream out, InputStream in) {
        try {
            if (out != null) {
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (in != null) {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * @param inputStream
     * @param zip
     * @return
     * @Author: libc
     * @Date: 2020/10/16 11:31
     * @tips: 关闭文件流
     */
    private static void closeStream(InputStream inputStream, org.apache.tools.zip.ZipFile zip) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (zip != null) {
                zip.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        zipShape("C:/home/serverManagerFiles/content\\Tongfeng_3d_a",true);
//
//    }

}
