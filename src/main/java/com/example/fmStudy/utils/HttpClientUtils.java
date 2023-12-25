package com.example.fmStudy.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *
 * </pre>
 *
 * @author 王蕾<br>
 * <b>mail</b> wanglei@syncsoft.com.cn<br>
 * <b>date</b> 2023年12月22日<br>
 * @version 1.0.0
 *
 * <pre>
 * 修改记录
 *  版本号		修订日期		修改人		bug编号		修改内容
 *  1.0.0		2023/12/22	王蕾	    		新建
 * </pre>
 */
public class HttpClientUtils {

    // 默认连接目标url的连接超时时间
    public static Integer DEFAULT_CONNECT_TIMEOUT = 20000;
    // 默认从连接池获取连接的超时时间
    public static Integer DEFAULT_CONNECTION_REQUEST_TIMEOUT = 10000;
    // 默认等待响应超时（读取数据超时）
    public static Integer DEFAULT_SOCKET_TIMEOUT = 40000;
    // 字符串为utf-8
    public static String CHARSET_UTF8 = "UTF-8";

    // 连接目标url的连接超时时间
    private Integer connectTimeout;
    // 从连接池获取连接的超时时间
    private Integer connectionRequestTimeout;
    // 等待响应超时（读取数据超时）
    private Integer socketTimeout;
    // 字符集编码
    private String charset;

    public HttpClientUtils () {
        this.connectTimeout = DEFAULT_CONNECT_TIMEOUT;
        this.connectionRequestTimeout = DEFAULT_CONNECTION_REQUEST_TIMEOUT;
        this.socketTimeout = DEFAULT_SOCKET_TIMEOUT;
        this.charset = CHARSET_UTF8;
    }

    /**
     *
     * @param charset 字符集编码
     */
    public HttpClientUtils (String charset) {
        this.connectTimeout = DEFAULT_CONNECT_TIMEOUT;
        this.connectionRequestTimeout = DEFAULT_CONNECTION_REQUEST_TIMEOUT;
        this.socketTimeout = DEFAULT_SOCKET_TIMEOUT;
        this.charset = charset;
    }

    /**
     *
     * @param connectTimeout 连接目标url的连接超时时间
     * @param connectionRequestTimeout 从连接池获取连接的超时时间
     * @param socketTimeout 等待响应超时（读取数据超时）
     */
    public HttpClientUtils(Integer connectTimeout, Integer connectionRequestTimeout, Integer socketTimeout) {
        this.connectTimeout = connectTimeout;
        this.connectionRequestTimeout = connectionRequestTimeout;
        this.socketTimeout = socketTimeout;
        this.charset = CHARSET_UTF8;
    }

    /**
     *
     * @param connectTimeout 连接目标url的连接超时时间
     * @param connectionRequestTimeout 从连接池获取连接的超时时间
     * @param socketTimeout 等待响应超时（读取数据超时）
     * @param charset 字符集编码
     */
    public HttpClientUtils(Integer connectTimeout, Integer connectionRequestTimeout, Integer socketTimeout, String charset) {
        this.connectTimeout = connectTimeout;
        this.connectionRequestTimeout = connectionRequestTimeout;
        this.socketTimeout = socketTimeout;
        this.charset = charset;
    }

    /**
     * get请求
     * @param requestUrl 请求路径
     * @return 请求响应的字符串
     */
    public String doGet(String requestUrl) {
        return doGet(requestUrl, null);
    }

    /**
     * get请求
     * @param requestUrl 请求路径
     * @param paramMap 请求参数，请求路径后面的参数
     * @return 请求响应的字符串
     */
    public String doGet(String requestUrl, Map<String, String> paramMap) {
        if (isStrEmpty(requestUrl)) {
            throw new HttpClientException("请求路径为空");
        }
        try {
            HttpGet httpGet = null;
            // 请求参数放入请求中
            if (null != paramMap && paramMap.size() != 0) {
                URIBuilder uriBuilder = new URIBuilder(requestUrl);
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
                httpGet = new HttpGet(uriBuilder.build());
            } else {
                httpGet = new HttpGet(requestUrl);
            }
            if (null == httpGet) {
                throw new HttpClientException("httpGet为空");
            }
            // 请求超时配置
            RequestConfig requestConfig = RequestConfig.custom()
                    // 连接目标url的连接超时时间
                    .setConnectTimeout(connectTimeout)
                    // 从连接池获取连接的超时时间
                    .setConnectionRequestTimeout(connectionRequestTimeout)
                    // 等待响应超时（读取数据超时）
                    .setSocketTimeout(socketTimeout)
                    .build();
            httpGet.setConfig(requestConfig);
            // 发送请求
            return send(requestUrl, httpGet);
        } catch (Exception e) {
            throw new HttpClientException(e);
        }
    }

    /**
     * post请求
     * @param requestUrl 请求路径
     * @return 请求响应字符串
     */
    public String doPost(String requestUrl) {
        return doPost(requestUrl, null, null, null);
    }

    /**
     * post请求
     * @param requestUrl 请求路径
     * @param paramMap 请求参数，即请求路径后面的参数
     * @return 请求响应字符串
     */
    public String doPost(String requestUrl, Map<String, String> paramMap) {
        return doPost(requestUrl, paramMap, null);
    }

    /**
     * post请求
     * @param requestUrl 请求路径
     * @param o 请求体参数
     * @return 请求响应字符串
     */
    public String doPost(String requestUrl, Object o) {
        return doPost(requestUrl, null, o);
    }

    /**
     * post请求
     * @param requestUrl 请求路径
     * @param paramMap 请求参数，即请求路径后面的参数
     * @param o 请求体参数
     * @return 请求响应字符串
     */
    public String doPost(String requestUrl, Map<String, String> paramMap, Object o) {
        return doPost(requestUrl, paramMap, o, null, null, null, null);
    }

    /**
     * post请求
     * @param requestUrl 请求路径
     * @param xmlStr xml报文字符串
     * @return 请求响应字符串
     */
    public String doPost(String requestUrl, String xmlStr) {
        return doPost(requestUrl, null, null, xmlStr, null, null, null);
    }

    /**
     * post请求
     * @param requestUrl 请求路径
     * @param filePath 上传文件路径
     * @param fileParam 上传文件请求名称
     * @return 请求响应字符串
     */
    public String doPost(String requestUrl, String filePath, String fileParam) {
        return doPost(requestUrl, filePath, fileParam, null);
    }

    /**
     * post请求
     * @param requestUrl 请求路径
     * @param filePath 上传文件路径
     * @param fileParam 上传文件请求名称
     * @param paramMap 请求参数，即请求路径后面的参数
     * @return 请求响应字符串
     */
    public String doPost(String requestUrl, String filePath, String fileParam, Map<String, String> paramMap) {
        return doPost(requestUrl, filePath, fileParam, paramMap, null, null);
    }

    /**
     * post请求
     * @param requestUrl 请求路径
     * @param filePath 上传文件路径
     * @param fileParam 上传文件请求名称
     * @param jsonValue 请求体参数
     * @param jsonName 请求体对应的请求名称
     * @return 请求响应字符串
     */
    public String doPost(String requestUrl, String filePath, String fileParam, Object jsonValue, String jsonName) {
        return doPost(requestUrl, filePath, fileParam, null, jsonValue, jsonName);
    }

    /**
     * post请求
     * @param requestUrl 请求路径
     * @param filePath 上传文件路径
     * @param fileParam 上传文件请求名称
     * @param paramMap 请求参数，即请求路径后面的参数
     * @param jsonValue 请求体参数
     * @param jsonName 请求体对应的请求名称
     * @return 请求响应字符串
     */
    public String doPost(String requestUrl, String filePath, String fileParam, Map<String, String> paramMap, Object jsonValue, String jsonName) {
        return doPost(requestUrl, paramMap, jsonValue, null, filePath, fileParam, jsonName);
    }

    /**
     * post请求
     * @param requestUrl 请求路径
     * @param paramMap 请求参数，即请求路径后面的参数
     * @param jsonValue 请求体参数
     * @param xmlStr xml报文字符串
     * @param filePath 上传文件路径
     * @param fileParam 上传文件请求名称
     * @param fileJsonName 请求体对应的请求名称
     * @return 请求响应字符串
     */
    private String doPost(String requestUrl, Map<String, String> paramMap, Object jsonValue, String xmlStr, String filePath, String fileParam, String fileJsonName) {
        if (isStrEmpty(requestUrl)) {
            throw new HttpClientException("请求路径为空");
        }
        try {
            HttpPost httpPost = null;
            // 传文件
            if (!isStrEmpty(filePath) && !isStrEmpty(fileParam)) {
                httpPost = new HttpPost(requestUrl);
                handlePostByFile(httpPost, filePath, fileParam, paramMap, fileJsonName, jsonValue);
            } else {
                // 用于处理xml和json
                StringEntity stringEntity;
                // 处理xml报文
                if (!isStrEmpty(xmlStr)) {
                    httpPost = new HttpPost(requestUrl);
                    stringEntity = new StringEntity(xmlStr, charset);
                    httpPost.setEntity(stringEntity);
                    httpPost.setHeader("Content-type", "application/xml");
                } else {
                    // 处理param请求
                    if (null != paramMap && paramMap.size() != 0) {
                        URIBuilder uriBuilder = new URIBuilder(requestUrl);
                        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                            uriBuilder.setParameter(entry.getKey(), entry.getValue());
                        }
                        httpPost = new HttpPost(uriBuilder.build());
                    }
                    if (null == httpPost) {
                        httpPost = new HttpPost(requestUrl);
                    }
                    // 处理json请求
                    if (null != jsonValue) {
                        stringEntity = new StringEntity(JSON.toJSONString(jsonValue), charset);
                        httpPost.setEntity(stringEntity);
                        httpPost.setHeader("Content-Type", "application/json");
                    }
                }
            }
            if (null == httpPost) {
                throw new HttpClientException("httpPost为空");
            }
            // 配置超时
            RequestConfig requestConfig = RequestConfig.custom()
                    // 默认连接目标url的连接超时时间
                    .setConnectTimeout(connectTimeout)
                    // 默认从连接池获取连接的超时时间
                    .setConnectionRequestTimeout(connectionRequestTimeout)
                    // // 默认等待响应超时（读取数据超时）
                    .setSocketTimeout(socketTimeout)
                    .build();
            httpPost.setConfig(requestConfig);
            // 发送请求
            return send(requestUrl, httpPost);
        } catch (Exception e) {
            throw new HttpClientException(e);
        }
    }

    /**
     * 处理上传文件请求
     * @param httpPost post
     * @param filePath 上传文件路径
     * @param fileParam 上传文件请求名称
     * @param paramMap 请求参数，即请求路径后面的参数
     * @param jsonName 请求体对应的请求名称
     * @param jsonValue 请求体参数
     */
    private void handlePostByFile(HttpPost httpPost, String filePath, String fileParam, Map<String, String> paramMap, String jsonName, Object jsonValue) {
        if (null == httpPost) {
            throw new HttpClientException("httpPost为空");
        }
        if (isStrEmpty(filePath)) {
            throw new HttpClientException(filePath + "文件不存在");
        }
        if (isStrEmpty(fileParam)) {
            throw new HttpClientException(fileParam + "文件请求名称为空");
        }
        FileInputStream fileInputStream = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new HttpClientException(filePath + "文件不存在");
            }
            // 读取文件
            byte[] bytes = new byte[(int) file.length()];
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create()
                    // 解决文件名中文乱码问题
                    .setMode(HttpMultipartMode.RFC6532);
            // 读取文件，放入请求，并设置contentType为multipart/form-data
            multipartEntityBuilder.addBinaryBody(fileParam, bytes, ContentType.MULTIPART_FORM_DATA, file.getName());
            // 将请求参数放入请求
            if (null != paramMap && paramMap.size() != 0) {
                for(Map.Entry<String, String> entry : paramMap.entrySet()) {
                    multipartEntityBuilder.addTextBody(entry.getKey(), entry.getValue(), ContentType.create("text/plain", charset));
                }
            }
            // 将json请求参数放入请求
            if (!isStrEmpty(jsonName) && null != jsonValue) {
                String requestJsonStr = JSON.toJSONString(jsonValue);
                multipartEntityBuilder.addTextBody(jsonName, requestJsonStr, ContentType.create("application/json", charset));
            }
            httpPost.setEntity(multipartEntityBuilder.build());
        } catch (Exception e) {
            throw new HttpClientException(e);
        } finally {
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                }catch (Exception e) {
                    throw new HttpClientException(e);
                }
            }
        }
    }

    /**
     * 发送请求
     * @param requestUrl 请求路径
     * @param request post/get请求
     * @return 请求响应字符串
     */
    private String send(String requestUrl, HttpRequestBase request) {
        if (null == request) {
            throw new HttpClientException("请求为空");
        }
        if (isStrEmpty(requestUrl)) {
            throw new HttpClientException("请求路径为空");
        }
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String errorMsg = "";
        try {
            // 创建HttpClient实例
            httpClient = HttpClients.createDefault();
            // 调用HttpClient实例执行POST实例，返回response
            response = httpClient.execute(request);
            if (null == response) {
                throw new Exception("response为空");
            }
            // 解析response
            // 获取状态码
            int status = response.getStatusLine().getStatusCode();
            // 状态码不为成功则抛异常
            if (HttpStatus.SC_OK != status) {
                throw new HttpClientException("code:" + status);
            }
            // 获取实例
            HttpEntity httpEntity = response.getEntity();
            if (null == httpEntity) {
                throw new HttpClientException("返回的httpEntity为空");
            }
            // 获取responseStr
            String responseStr = EntityUtils.toString(httpEntity);
            if (isStrEmpty(responseStr)) {
                throw new HttpClientException("返回空报文");
            }
            return responseStr;
        } catch (ConnectTimeoutException cte) {
            errorMsg = "连接超时(" + requestUrl + ")" + cte.getMessage();
            throw new HttpClientException(errorMsg);
        } catch (SocketTimeoutException ste) {
            errorMsg = "数据读取超时(" + requestUrl + ")" + ste.getMessage();
            throw new HttpClientException(errorMsg);
        } catch (Exception ect) {
            errorMsg = ect.getMessage();
            errorMsg = null == errorMsg || "".equals(errorMsg) ? ect.toString() : errorMsg;
            if (errorMsg.startsWith("3")) {
                errorMsg = "重定向(" + errorMsg + ")";
            } else if (errorMsg.startsWith("4")) {
                errorMsg = "请求错误(" + errorMsg + ")";
            } else if (errorMsg.startsWith("5")) {
                errorMsg = "服务器错误(" + errorMsg + ")";
            }
            throw new HttpClientException(errorMsg);
        } finally {
            // 6、释放资源
            if (null != response) {
                try {
                    response.close();
                } catch (Exception e) {
                    throw new HttpClientException(e);
                }
            }
            if (null != request) {
                request.releaseConnection();
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (Exception e) {
                    throw new HttpClientException(e);
                }

            }
        }
    }

    /**
     * 判断字符串是否为空
     * @param str 需要判断为空的字符串
     * @return true：空，false：非空
     */
    public static boolean isStrEmpty(String str) {
        return null == str || "".equals(str);
    }
}
