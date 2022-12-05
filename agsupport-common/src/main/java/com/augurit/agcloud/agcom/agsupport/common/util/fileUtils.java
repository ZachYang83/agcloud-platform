package com.augurit.agcloud.agcom.agsupport.common.util;

import java.io.*;

/**
 * 文件删除，读取流
 * Created by 陈泽浩 on 2017-06-12.
 */
public class fileUtils {

    public static String readFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) return null;
        BufferedReader reader = null;
        String text = "";
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gbk"));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                text += tempString;
            }
        } finally {
            if (reader != null)
                reader.close();
        }
        return text;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        if (inStream == null) return null;
        ByteArrayOutputStream outSteam = null;
        try {
            outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int len = -1;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.flush();
            return outSteam.toByteArray();
        } finally {
            if (outSteam != null) outSteam.close();
            inStream.close();
        }
    }

    /**
     * 删除文件或者目录下的所有文件
     *
     * @param sPath
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = true;
        try {
            File file = new File(sPath);
            if (!file.exists()) return false;
            if (file.isFile()) {
                flag = file.delete();
            } else {
                flag = deleteCatalog(sPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static boolean deleteCatalog(String sPath) {
        boolean flag = true;
        try {
            if (!sPath.endsWith(File.separator)) {
                sPath = sPath + File.separator;
            }
            File dirFile = new File(sPath);
            if (!dirFile.exists() || !dirFile.isDirectory()) {
                flag = false;
            } else {
                File[] files = dirFile.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile()) {
                        flag = files[i].delete();
                        if (!flag) break;
                    } else {
                        flag = deleteCatalog(files[i].getAbsolutePath());
                        if (!flag) break;
                    }
                }
            }
            if (!flag) return flag;
            flag = dirFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 拷贝文件
     *
     * @param sourcefile
     * @param targetFile
     * @throws IOException
     */
    public static void copyFile(File sourcefile, File targetFile) throws IOException {
        FileInputStream input = null;
        BufferedInputStream inbuff = null;
        FileOutputStream out = null;
        BufferedOutputStream outbuff = null;
        try {
            input = new FileInputStream(sourcefile);
            inbuff = new BufferedInputStream(input);
            out = new FileOutputStream(targetFile);
            outbuff = new BufferedOutputStream(out);
            byte[] b = new byte[1024 * 2];
            int len = 0;
            while ((len = inbuff.read(b)) != -1) {
                outbuff.write(b, 0, len);
            }
            outbuff.flush();
        } finally {
            if (outbuff != null) outbuff.close();
            if (inbuff != null) inbuff.close();
            if (out != null) out.close();
            if (input != null) input.close();
        }
    }

    /**
     * 拷贝文件夹
     *
     * @param sourceDir
     * @param targetDir
     * @throws IOException
     */
    public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
        File newfile = new File(targetDir);
        if (!newfile.exists()) {
            newfile.mkdirs();
        }
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                File sourceFile = file[i];
                File targetFile = new File(newfile.getAbsolutePath() + File.separator + file[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                String dir1 = sourceDir + "/" + file[i].getName();
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }
}
