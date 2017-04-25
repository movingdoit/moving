package org.base.component.utils;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;



import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * httpclient工具类

 */
@Component
public class HttpClientUtil {
    private static Logger LOGGER = LoggerFactory
            .getLogger(HttpClientUtil.class);
 
    /**
     * 发送GET请求，携带参数
     * 
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public  String sendGetRequest(String url, Map<String, String> params,
            int connTimeout, int soTimeout, String decodeCharset) {
        // 构造HttpClient的实例
        HttpClient orgHttpClient = getHttpClient(connTimeout);
        GetMethod getMethod = getGetMethod(url, params, soTimeout);
        String result = doGet(orgHttpClient, getMethod, decodeCharset);
        getMethod.releaseConnection();
        return result;
    }
    
    
   
    
    /**
     * 执行get请求
     */
    private String doGet(HttpClient orgHttpClient, GetMethod getMethod,
            String decodeCharset) {
        try {
            // 执行getMethod
            int statusCode = orgHttpClient.executeMethod(getMethod);
            if (statusCode == HttpStatus.SC_OK) {
                // 读取内容
                byte[] responseBody = getMethod.getResponseBody();
                // 处理内容
                return new String(responseBody, decodeCharset);

            } else {
                LOGGER.info("get请求失败（" + getMethod.getStatusLine()
                        + "）！statusCode：" + statusCode);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取HttpClient
     */
    private HttpClient getHttpClient(int connTimeout) {
        // 构造HttpClient的实例
        HttpClient orgHttpClient = new HttpClient();
        // 设置 Http连接超时
        orgHttpClient.getHttpConnectionManager().getParams()
                .setConnectionTimeout(connTimeout);
        return orgHttpClient;
    }

    /**
     * 获取GetMethod
     */
    private GetMethod getGetMethod(String url, Map<String, String> params,
            int soTimeout) {
        GetMethod getMethod = new GetMethod(getGetMethodUrl(url, params));
        // 设置get请求超时
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT,
                soTimeout);
        // 使用系统提供的默认的恢复策略
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
        return getMethod;
    }

    /**
     * 获取GetMethod的url
     */
    private String getGetMethodUrl(String url, Map<String, String> params) {
        final StringBuffer tmp = new StringBuffer(url);
        if (params != null) {
            Set<Entry<String, String>> paramset = params.entrySet();
            if (paramset.size() > 0) {
                tmp.append("?");
                int totalLen = paramset.size(), index = 0;
                for (Entry<String, String> entry : paramset) {
                    tmp.append(entry.getKey() + "=" + entry.getValue());
                    if (++index < totalLen) {
                        tmp.append("&");
                    }
                }
            }
            
        }
        return tmp.toString();
    }

    /**
     * 利用http client模拟发送http post请求
     * 
     * @param targetUrl
     *            请求地址
     * @param params
     *            请求参数<paramName,paramValue>
     * @return Object 返回的类型可能是：String 或者 byte[] 或者 outputStream
     * @throws Exception
     */
    public String setPostRequest(String targetUrl, Map<String, String> params,
            RequestEntity requestEntity, int connTimeout, int soTimeout,
            String decodeCharset) {
        HttpClient client = getHttpClient(connTimeout);
        PostMethod post = getPostMethod(targetUrl, params, requestEntity,
                soTimeout,decodeCharset);
        String result = doPost(client, post, decodeCharset);
        post.releaseConnection();
        return result;
    }

    /**
     * 执行post请求
     */
    private String doPost(HttpClient client, PostMethod post,
            String decodeCharset) {
        try {
            int sendStatus = client.executeMethod(post);
            if (sendStatus == HttpStatus.SC_OK) {
                // 读取内容
                byte[] responseBody = post.getResponseBody();
                return new String(responseBody, decodeCharset);
                
            } else {
                LOGGER.info("post请求失败（" + post.getStatusLine()
                        + "）！statusCode：" + sendStatus);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 执行PostMethod请求
     */
    private PostMethod getPostMethod(String targetUrl,
            Map<String, String> params, RequestEntity requestEntity,
            int soTimeout,String decodeCharset) {
        PostMethod post = new PostMethod(targetUrl);
        // 设置get请求超时
        post.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, soTimeout);
        // 使用系统提供的默认的恢复策略
        post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler());
        post.getParams().setContentCharset(decodeCharset);
        if (requestEntity != null) {
            post.setRequestEntity(requestEntity);
        }
        // 服务端完成返回后，主动关闭链接
        post.setRequestHeader("Connection", "close");
        if (params != null) {
            Iterator<String> keyIte = params.keySet().iterator();
            while (keyIte.hasNext()) {
                Object paramName = keyIte.next();
                Object paramValue = params.get(paramName);
                NameValuePair pair = new NameValuePair((String) paramName,
                         paramValue+"");
                post.addParameter(pair);

            }
        }
        return post;
    }
    
    /**
     *服务器连接状态.
     * @return
     *      boolean 连接是否正常.
     */
    public boolean linkStatus(String urlStr) {
        InputStream is = null;
        try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();
            is = urlConn.getInputStream();
        } catch (IOException e) {
            LOGGER.error("【异常 】"+urlStr+"连接异常", e);
            return false;
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
    
  
    
}

