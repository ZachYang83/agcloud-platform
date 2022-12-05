package com.augurit.agcloud.agcom.agsupport.common.util;

import java.io.*;

/**
 * 文件写操作
 * Created by 陈泽浩 on 2017-06-26.
 */
public class WriteFile {

    public static boolean writeFile(File file, String text) throws IOException {
        boolean flag = false;
        String temp = null;
        FileInputStream fis = null;
        InputStreamReader is = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        PrintWriter pw = null;
        try {
            fis = new FileInputStream(file);
            is = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(is);
            StringBuffer buffer = new StringBuffer();
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                buffer = buffer.append(System.getProperty("line.separator"));
            }
            buffer.append(text);
            fos = new FileOutputStream(file);
            pw = new PrintWriter(fos);
            pw.write(buffer.toString().toCharArray());
            pw.flush();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
            if (fos != null) {
                fos.close();
            }
            if (br != null) {
                br.close();
            }
            if (is != null) {
                is.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
        return flag;
    }

    public static String readFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) return null;
        BufferedReader reader = null;
        String text = "";
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
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
}
