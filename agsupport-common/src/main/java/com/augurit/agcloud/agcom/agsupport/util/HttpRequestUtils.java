package com.augurit.agcloud.agcom.agsupport.util;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Package: testHttpRequest
 * @ClassName: HttpRequestUtils
 * @Description: 向指定url模拟发送一个get请求
 * @UpdateDate: 2019/12/30 11:26
 */
public class HttpRequestUtils {


    private static final int connectTimeout = 1000 * 10;    // 连接超时时间
    private static final int readTimeout = 1000 * 20;    // 读取数据超时时间


    /**
     * 向指定 URL 发送 GET请求
     *
     * @param strUrl        发送请求的 URL
     * @param requestParams 请求参数
     * @return 远程资源的响应结果（输入流）
     */
    public static InputStream sendGet(String strUrl, String requestParams) throws Exception {

        try {
            String strRequestUrl = strUrl;
            if(null != requestParams && !"".equals(requestParams)){
                strRequestUrl = strUrl + "?" + requestParams;
            }
            URL url = new URL(strRequestUrl);
            URLConnection urlConnection = url.openConnection();    // 打开与 URL 之间的连接

            // 设置通用的请求属性
            urlConnection.setRequestProperty("accept", "*/*");
            urlConnection.setRequestProperty("connection", "Keep-Alive");
            urlConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            urlConnection.setConnectTimeout(connectTimeout);
            urlConnection.setReadTimeout(readTimeout);

            urlConnection.connect();    // 建立连接

            return urlConnection.getInputStream();
        } catch (Exception e) {
            throw new Exception("连接异常");
        }

    }
}
