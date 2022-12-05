package com.augurit.agcloud.agcom.agsupport.sc.elasticsearch.util;

import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author:Dreram
 * @Description:
 * @Date:created in :16:56 2019/5/7
 * @Modified By:
 */
public class HttpClientUtil {
    public static String HttpPost(String url, Map<String, String> param) throws Exception {
        List<NameValuePair> list = new LinkedList<NameValuePair>();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        HttpPost httpPost = new HttpPost(url);
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list, "utf-8");
        httpPost.setEntity(formEntity);
        DefaultHttpClient httpCient = new DefaultHttpClient();
        HttpResponse httpresponse = null;
        try {
            httpresponse = httpCient.execute(httpPost);
            HttpEntity httpEntity = httpresponse.getEntity();
            String response = EntityUtils.toString(httpEntity, "utf-8");
            return response;
        } catch (ClientProtocolException e) {
            System.out.println("http请求失败");
        } catch (IOException e) {
            System.out.println("http请求失败");
        }
        return null;
    }

    public static JSONObject httpGet(String url) {
        //get请求返回结果
        JSONObject jsonResult = null;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);

            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                /**把json字符串转换成json对象**/
                jsonResult = JSONObject.fromObject(strResult);
                url = URLDecoder.decode(url, "UTF-8");
            } else {
                System.out.println("get请求提交失败:" + url);
            }
        } catch (IOException e) {
            System.out.println("get请求提交失败:" + url);

        }
        return jsonResult;
    }
}
