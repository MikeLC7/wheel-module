package com.xcdh.target.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Http帮助类，封装了httpComponent方法
 * @author
 */
public class HttpClientUtil {
    private static String APP_VERSION = "1.0";
    private static final int REQUEST_TIMEOUT = 5000;

    /**
     * 获取app_version，默认1.0
     * @return
     */
    public static String getAppVersion() {
        return APP_VERSION;
    }

    /**
     * 设置appVersion，默认1.0
     * @param appVersion
     */
    public static void setAppVersion(String appVersion) {
        HttpClientUtil.APP_VERSION = appVersion;
    }

    /**
     * 发送Post请求
     * @param url 请求地址
     * @return 返回body
     * @throws IOException
     */
    public static String post(String url) throws IOException {
        return post(url, null);
    }

    /**
     * 发送Post请求
     * @param url 请求地址
     * @param nvps post参数
     * @return body
     * @throws IOException
     */
    public static String post(String url, Map<String, String> nvps) throws IOException {
        HttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("charset", HTTP.UTF_8);

        // 默认超时时间为5s。
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(REQUEST_TIMEOUT)
                .setConnectTimeout(REQUEST_TIMEOUT)
                .setConnectionRequestTimeout(REQUEST_TIMEOUT).build();
        httpPost.setConfig(requestConfig);

        // 请求的参数信息传递
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (nvps != null) {
            Set<String> keys = nvps.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                params.add(new BasicNameValuePair(key, URLDecoder.decode(nvps.get(key), HTTP.UTF_8)));
            }
        }
        if (params.size() > 0) {
            HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            httpPost.setEntity(entity);
        }

        HttpResponse response = client.execute(httpPost);
        int status = response.getStatusLine().getStatusCode();
        if (status < HttpStatus.SC_OK || status >= 300) {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }

        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity, "UTF-8");
        return body;
    }

    /**
     * 发送Get请求
     * @param url 请求地址
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        HttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("charset", "utf-8");
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(REQUEST_TIMEOUT)
                .setConnectTimeout(REQUEST_TIMEOUT)
                .setConnectionRequestTimeout(REQUEST_TIMEOUT).build();
        httpGet.setConfig(requestConfig);
        HttpResponse response = client.execute(httpGet);
        int status = response.getStatusLine().getStatusCode();
        if (status < HttpStatus.SC_OK || status >= HttpStatus.SC_MULTIPLE_CHOICES) {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }

        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity, "UTF-8");
        return body;
    }

    /**
     * Get请求后台ServletAPI
     * @param urlComponent 请求ServletAPI的Key(相对地址)
     * @throws ClientProtocolException 请求servletAPI抛出http异常
     * @throws IOException 请求servletAPI抛出io异常
     */
    public static String get(String urlComponent, int timeOut) throws ClientProtocolException, IOException {
        HttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(urlComponent);
        httpGet.setHeader("charset", HTTP.UTF_8);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeOut).setConnectTimeout(timeOut)
                .setConnectionRequestTimeout(timeOut).build();
        httpGet.setConfig(requestConfig);
        HttpResponse response = client.execute(httpGet);
        int status = response.getStatusLine().getStatusCode();
        if (status < HttpStatus.SC_OK || status >= HttpStatus.SC_MULTIPLE_CHOICES) {
            throw new ClientProtocolException("Unexpected response status: " + status);
        }

        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity, "UTF-8");

        return body;

    }

    /**
     * POST JSON请求
     * @param urlString 请求地址
     * @param param JSON参数
     * @param headers 头参数
     * @return 返回结果
     * @throws Exception 其他异常
     */
    public static String jsonPostToString(String urlString, JSONObject param, Map<String, String> headers) throws Exception {
        return new String(jsonPost(urlString, param, headers), "utf-8");
    }

    public static byte[] jsonPost(String urlString, JSONObject param, Map<String, String> headers) throws Exception {
        if (StringUtils.isBlank(urlString) || param == null) {
            return null;
        }

        // 拼接post请求对象
        String content = param.toString();
        StringEntity entity = new StringEntity(content, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");

        HttpPost post = new HttpPost(urlString);
        // 设置请求的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(REQUEST_TIMEOUT)
                .setConnectTimeout(REQUEST_TIMEOUT)
                .setConnectionRequestTimeout(REQUEST_TIMEOUT).build();
        post.setConfig(requestConfig);
        post.setEntity(entity);
        post.setHeader("Content-Type", "application/json");
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                post.setHeader(entry.getKey(), entry.getValue());
            }
        }
        // 发送post请求
        HttpClient client = HttpClients.createDefault();
        HttpResponse httpResponse = client.execute(post);
        int status = httpResponse.getStatusLine().getStatusCode();
        if (status < HttpStatus.SC_OK || status >= HttpStatus.SC_MULTIPLE_CHOICES) {
            throw new ClientProtocolException("Unexpected response status: " + status);
        } else {
            return EntityUtils.toByteArray(httpResponse.getEntity());
        }
    }

    /**
     * PUT JSON请求
     * @param urlString 请求地址
     * @param param JSON参数
     * @param headers 头参数
     * @return 返回结果
     * @throws Exception 其他异常
     */
    public static String jsonPut(String urlString, JSONObject param, Map<String, String> headers) throws Exception {
        if (StringUtils.isBlank(urlString) || param == null) {
            return null;
        }
        // 拼接post请求对象
        String content = param.toString();
        StringEntity entity = new StringEntity(content, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");

        HttpPut put = new HttpPut(urlString);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(REQUEST_TIMEOUT)
                .setConnectTimeout(REQUEST_TIMEOUT)
                .setConnectionRequestTimeout(REQUEST_TIMEOUT).build();
        put.setConfig(requestConfig);
        put.setEntity(entity);
        put.setHeader("Content-Type", "application/json");
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                put.setHeader(entry.getKey(), entry.getValue());
            }
        }
        // 发送put请求
        HttpClient client = HttpClients.createDefault();
        HttpResponse httpResponse = client.execute(put);
        int status = httpResponse.getStatusLine().getStatusCode();
        if (status < HttpStatus.SC_OK || status >= HttpStatus.SC_MULTIPLE_CHOICES) {
            throw new ClientProtocolException("Unexpected response status: " + status);
        } else {
            return EntityUtils.toString(httpResponse.getEntity());
        }
    }
}
