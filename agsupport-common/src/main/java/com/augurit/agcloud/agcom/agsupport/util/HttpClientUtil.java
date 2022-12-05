package com.augurit.agcloud.agcom.agsupport.util;

import com.common.util.HttpRespons;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * @author zhangmingyang
 * @Description: HttpClient工具类
 * @date 2019-07-16 13:36
 */
public class HttpClientUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

    private static RequestConfig requestConfig = null;
    public static final int CONNECTION_TIMEOUT = 2000;// 连接超时时间

    public static final int CONNECTION_REQUEST_TIMEOUT = 2000;// 请求超时时间

    public static final int SOCKET_TIMEOUT = 3000;// 数据读取等待超时

    public static final String DEFAULT_ENCODING = "UTF-8";// 默认编码

   /* private static final Map<String, String> HEADERS;
    static
    {
        HEADERS = new HashMap<String, String>();
        HEADERS.put("a", "b");
        HEADERS.put("c", "d");
    }*/

    static
    {
        // 设置请求和传输超时时间
        requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT).build();
    }

    public static String get(String url){
        return get(url,null,null,DEFAULT_ENCODING);
    }

    public static String get(String url,Map<String,String> params,String encoding){
        return get(url,params,null,encoding);
    }

    public static String getByToken(String url,Map<String,String> params,String token,String encoding){
        // 请求头加token
        Map headers = new HashMap();
        // bearer 后要带空格
        headers.put("Authorization","bearer " + token);
        return get(url,params,headers,encoding);
    }

    public static String get(String url, Map<String,String> params,Map<String,String> headers,String encoding){
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = "";
        try {
            URIBuilder uriBuilder =new URIBuilder(url);
            // 拼装参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (params != null && params.size() > 0){
                for (Map.Entry<String,String> entry:params.entrySet()){
                    nvps.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
                }
                uriBuilder.setParameters(nvps);
            }
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.setConfig(requestConfig);

            //默认请求头Accept
            httpGet.setHeader("Accept","application/json, text/javascript, */*; q=0.01");
            //httpGet.setHeader("Content-Type","application/x-www-form-urlencoded");
            httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");

            // 拼装请求头
            if (headers != null && headers.size() > 0){
                for (Map.Entry<String,String> entry : headers.entrySet()){
                    httpGet.setHeader(entry.getKey(),entry.getValue());
                }
            }
            response = client.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, encoding);
            }else {
                LOGGER.error("get 请求失败:url:"+url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (response != null){
                try {
                    response.close();
                    client.close();
                } catch (IOException e) {
                    LOGGER.error("请求释放出错 url:"+url);
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String post(String url, Map<String,String> params,Map<String,String> headers,String encoding){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        packageHeader(headers, httpPost);
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        String result = "";
        try {
            // 封装请求参数
            packageParam(params, httpPost);
            result = getHttpClientResult(httpResponse, httpClient, httpPost);
        }catch (Exception e){

        }
        finally {
            // 释放资源
            try {
                release(httpResponse, httpClient);
            }catch (Exception e){

            }

        }
        return result;
    }

    public static void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        // 封装请求头
        if (params != null) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     *  封装请求参数
     *
     * @param params
     * @param httpMethod
     * @throws UnsupportedEncodingException
     */
    public static void packageParam(Map<String, String> params, HttpEntityEnclosingRequestBase httpMethod)
            throws UnsupportedEncodingException {
        // 封装请求参数
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            // 设置到请求的http对象中
            httpMethod.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        }
    }

    /**
     * 获得响应结果
     *
     * @param httpResponse
     * @param httpClient
     * @param httpMethod
     * @return
     * @throws Exception
     */
    public static String getHttpClientResult(CloseableHttpResponse httpResponse,
                                                       CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws Exception {
        httpResponse = httpClient.execute(httpMethod);
        String result = "";
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity, "utf-8");
        }else {
            LOGGER.error("get 请求失败:url:");
        }
        return result;
    }

    /**
     * 释放资源
     *
     * @param httpResponse
     * @param httpClient
     * @throws IOException
     */
    public static void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) throws IOException {
        // 释放资源
        if (httpResponse != null) {
            httpResponse.close();
        }
        if (httpClient != null) {
            httpClient.close();
        }
    }

    public static String getBySslPost(String url, Map<String, String> params, Map<String, String> headers, String encode) {
        HttpRespons httpRespons = new HttpRespons();

        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        InputStream inputStream = null;
        String result = "";
        try {

            client = getClientBySsl();
            HttpPost httpPost = new HttpPost(url);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            if (params != null){
                Iterator iterator = params.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                    list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
                }
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "utf-8");
                httpPost.setEntity(entity);
            }
            RequestConfig config = HttpClientUtil.requestConfig = RequestConfig.custom().setSocketTimeout(4000).setConnectTimeout(3000).setConnectionRequestTimeout(3000).build();
            httpPost.setConfig(config);
            //默认请求头Accept
            httpPost.setHeader("Accept", "application/json, text/javascript, */*; q=0.01");
            //httpGet.setHeader("Content-Type","application/x-www-form-urlencoded");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");

            // 拼装请求头
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            response = client.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity, "utf-8");
            } else {
                result = "";
                LOGGER.error("get 请求失败:url:" + url);
            }
        } catch (Exception e) {
            result = "";
            LOGGER.error("请求异常url:"+url+"参数:"+params.toString(),e);
        }finally {
            if (response != null) {
                try {
                    response.close();

                } catch (IOException e) {
                    LOGGER.error("请求释放出错 url:" + url);
                    e.printStackTrace();
                }
            }
            try {
                client.close();
                if (inputStream != null){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static CloseableHttpClient getClientBySsl() throws Exception {
        SSLContextBuilder builder = new SSLContextBuilder().loadTrustMaterial(null, (TrustStrategy) (x509Certificates, s) -> true);
        HostnameVerifier hostnameVerifierAllowAll = new HostnameVerifier() {
            @Override
            public boolean verify(String name, SSLSession session) {
                return true;
            }
        };

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(),
                new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.1", "TLSv1.2"}, null, hostnameVerifierAllowAll);
        HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                // 重试设置
                if (executionCount >= 5) {
                    // Do not retry if over max retry count
                    return false;
                }
                if (exception instanceof InterruptedIOException) {
                    // Timeout
                    return false;
                }
                if (exception instanceof UnknownHostException) {
                    // Unknown host
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {
                    // Connection refused
                    return false;
                }
                if (exception instanceof SSLException) {
                    // SSL handshake exception
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                if (idempotent) {
                    return true;
                }
                return false;
            }
        };
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECTION_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT)// 超时设置
                .build();
        CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).setRetryHandler(myRetryHandler)// 重试设置
                .setDefaultRequestConfig(requestConfig).build();
        return client;
    }
}
