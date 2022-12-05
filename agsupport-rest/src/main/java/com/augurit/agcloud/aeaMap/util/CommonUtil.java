package com.augurit.agcloud.aeaMap.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CommonUtil {
    public static String DEFAULT_ATTACHMENT_FILEPATH = "upload";
    static String[] units = {"", "十", "百", "千", "万", "十万", "百万", "千万", "亿",
            "十亿", "百亿", "千亿", "万亿"};
    static char[] numArray = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};
    private static final int BUFFEREDSIZE = 1024;

    public static String formatInteger(int num) {
        char[] val = String.valueOf(num).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String m = val[i] + "";
            int n = Integer.valueOf(m);
            boolean isZero = n == 0;
            String unit = units[(len - 1) - i];
            if (isZero) {
                if ('0' == val[i - 1]) {
                    continue;
                } else {
                    if (i != len - 1) {
                        sb.append(numArray[n]);
                    }
                }
            } else {
                sb.append(numArray[n]);
                sb.append(unit);
            }
        }
        return sb.toString();
    }

    /**
     * zip打包shape
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public static void zipShapeFile(String filePath, String fileName) throws IOException {
        String zipPath = filePath + fileName + ".zip";
        String inputFilename = filePath + fileName + ".dbf," + filePath + fileName + ".shp," + filePath + fileName + ".shx";
        String[] fileNames = inputFilename.split(",");
        File[] inputFiles = new File[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            inputFiles[i] = new File(fileNames[i]);
        }
        zip(inputFiles, zipPath);
    }

    public static void zip(File[] inputFiles, String zipPath) throws IOException {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
                zipPath));
        try {
            for (int i = 0; i < inputFiles.length; i++) {
                startZip(inputFiles[i], out, "");
            }
        } catch (IOException e) {
            throw e;
        } finally {
            out.close();
        }
    }

    public static void startZip(File inputFile, ZipOutputStream out, String base)
            throws IOException {
        if (inputFile.isDirectory()) {
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
        } else {
            if (base.length() > 0) {
                out.putNextEntry(new ZipEntry(base));
            } else {
                out.putNextEntry(new ZipEntry(inputFile.getName()));
            }
            FileInputStream in = new FileInputStream(inputFile);
            try {
                int c;
                byte[] by = new byte[BUFFEREDSIZE];
                while ((c = in.read(by)) != -1) {
                    out.write(by, 0, c);
                }
            } catch (IOException e) {
                throw e;
            } finally {
                in.close();
            }
        }
        inputFile.delete();
    }
}
