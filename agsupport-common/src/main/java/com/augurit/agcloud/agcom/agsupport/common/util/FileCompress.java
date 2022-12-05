package com.augurit.agcloud.agcom.agsupport.common.util;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Deflater;

/**
 * 文件解压、压缩
 * Created by 陈泽浩 on 2017-06-28.
 */
public class FileCompress {

    private static boolean isCreateSrcDir = false;

    /**
     * @param src     压缩源
     * @param archive 压缩包路径
     * @param comment 压缩包注释
     */
    public static void zipFileCompress(String src, String archive, String comment) {
        FileOutputStream fos = null;
        CheckedOutputStream csum = null;
        ZipOutputStream zos = null;
        BufferedOutputStream out = null;
        try {
            fos = new FileOutputStream(archive);
            csum = new CheckedOutputStream(fos, new CRC32());
            zos = new ZipOutputStream(csum);
            zos.setEncoding("GBK");
            out = new BufferedOutputStream(zos);
            zos.setComment(comment);
            zos.setMethod(ZipOutputStream.DEFLATED);
            zos.setLevel(Deflater.BEST_COMPRESSION);
            File srcFile = new File(src);
            if (!srcFile.exists() || (srcFile.isDirectory() && srcFile.list().length == 0)) {
                throw new FileNotFoundException(
                        "文件不存在或者压缩文件不包含子文件!");
            }
            src = src.replaceAll("\\\\", "/");
            String prefixDir = null;
            if (srcFile.isFile()) {
                prefixDir = src.substring(0, src.lastIndexOf("/") + 1);
            } else {
                prefixDir = (src.replaceAll("/$", "") + "/");
            }
            if (prefixDir.indexOf("/") != (prefixDir.length() - 1) && isCreateSrcDir) {
                prefixDir = prefixDir.replaceAll("[^/]+/$", "");
            }
            writeRecursive(zos, out, srcFile, prefixDir);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
                if (csum != null)
                    csum.close();
                if (zos != null)
                    zos.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Checksum: " + csum.getChecksum().getValue());   //  文件验证
        }
    }

    private static void writeRecursive(ZipOutputStream zos, BufferedOutputStream bo, File srcFile, String prefixDir) {
        ZipEntry zipEntry = null;
        BufferedInputStream bi = null;
        try {
            String filePath = srcFile.getAbsolutePath().replaceAll("\\\\", "/").replaceAll("//", "/");
            if (srcFile.isDirectory()) {
                filePath = filePath.replaceAll("/$", "") + "/";
            }
            String entryName = filePath.replace(prefixDir, "").replaceAll("/$", "");
            if (srcFile.isDirectory()) {
                if (!"".equals(entryName)) {
                    zipEntry = new ZipEntry(entryName + "/");
                    zos.putNextEntry(zipEntry);
                }
                File srcFiles[] = srcFile.listFiles();
                for (int i = 0; i < srcFiles.length; i++) {
                    writeRecursive(zos, bo, srcFiles[i], prefixDir);
                }
            } else {
                bi = new BufferedInputStream(new FileInputStream(srcFile));
                zipEntry = new ZipEntry(entryName);
                zos.putNextEntry(zipEntry);
                byte[] buffer = new byte[1024];
                int readCount = bi.read(buffer);
                while (readCount != -1) {
                    bo.write(buffer, 0, readCount);
                    readCount = bi.read(buffer);
                }
                bo.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bo != null)
                    bo.flush();
                if (bi != null)
                    bi.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        zipFileCompress("C:\\Users\\Augurit\\Desktop\\descompress", "C:\\Users\\Augurit\\Desktop\\descompress.zip", "");
    }
}
