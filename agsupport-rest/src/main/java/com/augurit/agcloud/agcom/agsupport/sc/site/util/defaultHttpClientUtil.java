package com.augurit.agcloud.agcom.agsupport.sc.site.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/*
 * 利用HttpClient进行post请求的工具类
        */
    public class defaultHttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(defaultHttpClientUtil.class);
    public String doPost(String url, Map<String, String> map, String charset) {
        if (null == charset) {
            charset = "utf-8";
        }
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            //httpClient = new SSLClient();
            httpClient = HttpClients.createDefault();
            httpPost = new HttpPost(url);

            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> elem = (Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                httpPost.setEntity(entity);
            }
            // 设置五秒超时
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(1000).setConnectionRequestTimeout(1000)
                    .setSocketTimeout(2000).build();
            httpPost.setConfig(requestConfig);
            //增加超时时间
//            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
//            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        }catch (SocketTimeoutException s){
            logger.error("服务连接超时url:"+url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 发送get请求
     *
     * @pa
     * @returnram url     链接地址
     * @param charset 字符编码，若为null则默认utf-8
     */
    public String doGet(String url, String charset) {
        if (null == charset) {
            charset = "utf-8";
        }
        HttpClient httpClient = null;
        HttpGet httpGet = null;
        String result = null;

        try {
            //httpClient = new SSLClient();
            httpClient = HttpClients.createDefault();
//            URL urls=new URL(url);
            httpGet = new HttpGet(url);
            // 设置五秒超时
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(1000).setConnectionRequestTimeout(1000)
                    .setSocketTimeout(2000).build();
            httpGet.setConfig(requestConfig);
            HttpResponse response = httpClient.execute(httpGet);

            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (SocketTimeoutException s){
            logger.error("服务连接超时url:"+url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }
    public String dorzGet(String url, String charset) {
        if (null == charset) {
            charset = "utf-8";
        }
        HttpClient httpClient = null;
        HttpGet httpGet = null;
        String result = null;

        try {
            httpClient = new SSLClient();
            httpGet = new HttpGet(url);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("encoding", "UTF-8"));
            nvps.add(new BasicNameValuePair("user.id","admin"));
            nvps.add(new BasicNameValuePair("user,.password","admin123"));

            httpGet.setHeader("Authorization", "Basic YWRtaW46Z2Vvc2VydmVy");


            HttpResponse response = httpClient.execute(httpGet);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    public String dorzPost(String url, Map<String, String> map, String charset) {
        if (null == charset) {
            charset = "utf-8";
        }
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);

            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("encoding", "UTF-8"));
            nvps.add(new BasicNameValuePair("user.id","admin"));
            nvps.add(new BasicNameValuePair("user.password","admin123"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            while (iterator.hasNext()) {
                Entry<String, String> elem = (Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                httpPost.setEntity(entity);

            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {

        }
        return result;
    }


}
