package com.augurit.agcloud.agcom.agsupport.sc.bimfile.reslut;

import java.io.*;

/**
 * @Package: com.augurit.agcloud.agcom.agsupport.sc.bimfile.reslut
 * @ClassName: BimVersionCompareUtil
 * @Description:
 * @UpdateDate: 2020/1/7 15:53
 */
public class BimVersionCompareUtil {

    public static Integer bimVersionCompare(String exePath, String resPath, String bimVersionFilePath1, String bimVersionFilePath2) {
        InputStream errorStream = null;
        FileOutputStream fos1 = null;
        try {
            Runtime rt = Runtime.getRuntime();
            String[] cmd = {"cmd", "/c", exePath, resPath, bimVersionFilePath1, bimVersionFilePath2};
            Process proc = rt.exec(cmd);
            errorStream = proc.getErrorStream();
            fos1 = new FileOutputStream(new File(resPath.substring(0, resPath.lastIndexOf(".")) + ".err.log"));
            byte[] b = new byte[1024];
            int read = 0;
            while ((read = errorStream.read(b)) > -1) {
                fos1.write(b, 0, read);
                if (read <= 1024) {
                    throw new Exception("程序错误");
                }
            }
            proc.waitFor();
            return 1;
        } catch (Throwable t) {
            t.printStackTrace();
            return 0;
        } finally {
            if (null != errorStream) {
                try {
                    errorStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fos1) {
                try {
                    fos1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getCompareContent(String resFilePath) {
        FileInputStream fis = null;
        StringBuffer sb = new StringBuffer();
        String resultString = null;
        try {
            fis = new FileInputStream(resFilePath);

            int len = 0;
            byte[] b = new byte[1024];
            while((len = fis.read(b)) > -1 ){
                sb.append(new String(b, 0, len, "ISO-8859-1"));
            }
            resultString = new String(sb.toString().getBytes("ISO-8859-1"), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != fis){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultString;
    }
}
