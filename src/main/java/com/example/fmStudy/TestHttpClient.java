package com.example.fmStudy;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.example.fmStudy.dto.User;
import com.example.fmStudy.utils.HttpClientUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *
 * 为什么要设置HTTP timeout
 * 1、于用户操作相关的接口，如果不设置超时时间，将会出现长时间的无响应，严重影响用户体验。
 * 2、负载很高的系统，因为大量调用耗时长的接口，导致性能急剧下降，从而影响其他正常的业务。
 * 3、某些情况下，HTTP请求可能永远都得不到响应，那么这部分系统资源就一直被占用，直到系统崩溃。
 * </pre>
 *
 * @author 王蕾<br>
 * <b>mail</b> wanglei@syncsoft.com.cn<br>
 * <b>date</b> 2023年12月19日<br>
 * @version 1.0.0
 *
 * <pre>
 * 修改记录
 *  版本号		修订日期		修改人		bug编号		修改内容
 *  1.0.0		2023/12/19	王蕾	    		新建
 * </pre>
 */
public class TestHttpClient {
    public static void main(String[] args) throws Exception {
//        reqByGetNoParam("http://localhost:8080/fmStudy/hello/getNotParam");
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("pageNum", "1");
        paramMap.put("pageSize", "10页");
//        reqByGetParam("http://localhost:8080/fmStudy/hello/getByParams", paramMap);
//        reqByPostNoParam("http://localhost:8080/fmStudy/hello/postNotParam");
//        reqByPostParam("http://localhost:8080/fmStudy/hello/postByParams", paramMap);
        User user = new User();
        user.setId("1");
        user.setUserName("张三");
//        reqByPostJson("http://localhost:8080/fmStudy/hello/postByJson", user);
        String requestXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<envelope>" +
                "<header>" +
                "<authen name=\"default\">" +
                "<data timestamp=\"20190928153121100\"/>" +
                "<data sequence=\"a93ae944445003093eab06cb90b31bba\"/>" +
                "<data appid=\"8fc22437adbd42eaaf474d65d3d44aba\"/>" +
                "<data service_code=\"1001\"/>" +
                "</authen>" +
                "</header>" +
                "<body>" +
                "<logic name=\"啊\">" +
                "<data zdbh=\"JJ202208121150517002\"/>" +
                "</logic>" +
                "</body>" +
                "</envelope>";
//        reqByPostXml("http://localhost:8080/fmStudy/hello/postByXml", requestXml);
//        reqByPostFile("http://localhost:8080/fmStudy/hello/postByFile", "D:\\测试.txt");
//        reqByPostFileAndParam("http://localhost:8080/fmStudy/hello/postByFileAndParam", "D:\\测试.txt", paramMap);
//        reqByPostFileAndJson("http://localhost:8080/fmStudy/hello/postByFileAndJson", "D:\\测试.txt", user);
//        reqByPostJsonAndReturnObj("http://localhost:8080/fmStudy/hello/postByJsonAndReturnObj", user);
        HttpClientUtils httpClientUtils = new HttpClientUtils();
//        System.out.println(httpClientUtils.doGet("http://localhost:8080/fmStudy/hello/getNotParam"));
//        System.out.println(httpClientUtils.doGet("http://localhost:8080/fmStudy/hello/getByParams", paramMap));
//        System.out.println(httpClientUtils.doPost("http://localhost:8080/fmStudy/hello/postNotParam"));
//        System.out.println(httpClientUtils.doPost("http://localhost:8080/fmStudy/hello/postByParams", paramMap));
//        System.out.println(httpClientUtils.doPost("http://localhost:8080/fmStudy/hello/postByJson", user));
//        System.out.println(httpClientUtils.doPost("http://localhost:8080/fmStudy/hello/postByParamsAndJson", paramMap, user));
//        System.out.println(httpClientUtils.doPost("http://localhost:8080/fmStudy/hello/postByXml", requestXml));
//        System.out.println(httpClientUtils.doPost("http://localhost:8080/fmStudy/hello/postByFile", "D:\\测试.txt", "files"));
//        System.out.println(httpClientUtils.doPost("http://localhost:8080/fmStudy/hello/postByFileAndParam", "D:\\测试.txt", "files", paramMap));
//        System.out.println(httpClientUtils.doPost("http://localhost:8080/fmStudy/hello/postByFileAndJson", "D:\\测试.txt", "files", user, "jsonData"));
//        System.out.println(httpClientUtils.doPost("http://localhost:8080/fmStudy/hello/postByFileAndParamAndJson", "D:\\测试.txt", "files", paramMap, user, "jsonData"));
        System.out.println(httpClientUtils.doPost("http://localhost:8080/fmStudy/hello/postByJsonAndReturnObj", user));
    }

    // 没有参数的get请求
    // http://localhost:8080/fmStudy/hello/getNotParam
    public static void reqByGetNoParam(String requestUrl) throws Exception {
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        String errorMsg = "";
        try {
            // 1、创建HttpClient实例
            httpClient = HttpClients.createDefault();
            // 2、创建GET请求方法实例
            httpGet = new HttpGet(requestUrl);
            int timeOut = 40000;
            // 请求超时配置
            RequestConfig requestConfig = RequestConfig.custom()
                    // 连接目标url的连接超时时间
                    .setConnectTimeout(timeOut)
                    // 从连接池获取连接的超时时间
                    .setConnectionRequestTimeout(timeOut / 2)
                    // 等待响应超时（读取数据超时）
                    .setSocketTimeout(timeOut * 2).build();
            httpGet.setConfig(requestConfig);
            // 3、调用HttpClient实例执行GET实例，返回response
            response = httpClient.execute(httpGet);
            if (null == response) {
                throw new Exception("response为空");
            }
            // 4、解析response
            // 获取状态码
            int status = response.getStatusLine().getStatusCode();
            // 状态码不为成功则抛异常
            if (HttpStatus.SC_OK != status) {
                throw new Exception("code：" + status);
            }
            // 获取实例
            HttpEntity entity = response.getEntity();
            // 获取responseStr
            String responseStr = EntityUtils.toString(entity);
            if (null == responseStr || "".equals(responseStr)) {
                throw new Exception("返回空报文");
            }
        } catch (ConnectTimeoutException cte) {
            errorMsg = "连接超时(" + requestUrl + ")" + cte.getMessage();
            throw new Exception(errorMsg);
        } catch (SocketTimeoutException ste) {
            errorMsg = "数据读取超时(" + requestUrl + ")" + ste.getMessage();
            throw new Exception(errorMsg);
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
            throw new Exception(errorMsg);
        } finally {
            // 5、释放连接
            if (null != response) {
                response.close();
            }
            if (null != httpGet) {
                httpGet.releaseConnection();
            }
            if (null != httpClient) {
                httpClient.close();
            }
        }
    }

    // 有参数的get请求
    // http://localhost:8080/fmStudy/hello/getByParams?pageNum=1&pageSize=10
    public static void reqByGetParam(String requestUrl, Map<String, String> params) throws Exception {
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        String errorMsg = "";
        try {
            // 1、创建HttpClient实例
            httpClient = HttpClients.createDefault();
            // 2、设置参数
            URIBuilder uriBuilder = new URIBuilder(requestUrl);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
            // 3、创建GET请求方法实例
            httpGet = new HttpGet(uriBuilder.build());
            int timeOut = 40000;
            // 请求超时配置
            RequestConfig requestConfig = RequestConfig.custom()
                    // 连接目标url的连接超时时间
                    .setConnectTimeout(timeOut)
                    // 从连接池获取连接的超时时间
                    .setConnectionRequestTimeout(timeOut / 2)
                    // 等待响应超时（读取数据超时）
                    .setSocketTimeout(timeOut * 2).build();
            httpGet.setConfig(requestConfig);
            // 4、调用HttpClient实例执行GET实例，返回response
            response = httpClient.execute(httpGet);
            if (null == response) {
                throw new Exception("response为空");
            }
            // 5、解析response
            // 获取状态码
            int status = response.getStatusLine().getStatusCode();
            // 状态码不为成功则抛异常
            if (HttpStatus.SC_OK != status) {
                throw new Exception("code:" + status);
            }
            // 获取实例
            HttpEntity httpEntity = response.getEntity();
            // 获取responseStr
            String responseStr = EntityUtils.toString(httpEntity);
            if (null == responseStr || "".equals(responseStr)) {
                throw new Exception("返回空报文");
            }
        } catch (ConnectTimeoutException cte) {
            errorMsg = "连接超时(" + requestUrl + ")" + cte.getMessage();
            throw new Exception(errorMsg);
        } catch (SocketTimeoutException ste) {
            errorMsg = "数据读取超时(" + requestUrl + ")" + ste.getMessage();
            throw new Exception(errorMsg);
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
            throw new Exception(errorMsg);
        } finally {
            // 6、释放资源
            if (null != response) {
                response.close();
            }
            if (null != httpGet) {
                httpGet.releaseConnection();
            }
            if (null != httpClient) {
                httpClient.close();
            }
        }
    }

    // 没有参数的post请求
    // http://localhost:8080/fmStudy/hello/postNotParam
    public static void reqByPostNoParam(String requestUrl) throws Exception {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        String errorMsg = "";
        try {
            // 1、创建HttpClient实例
            httpClient = HttpClients.createDefault();
            // 2、创建POST请求方法实例
            httpPost = new HttpPost(requestUrl);
            int timeOut = 40000;
            // 请求超时配置
            RequestConfig requestConfig = RequestConfig.custom()
                    // 连接目标url的连接超时时间
                    .setConnectTimeout(timeOut)
                    // 从连接池获取连接的超时时间
                    .setConnectionRequestTimeout(timeOut / 2)
                    // 等待响应超时（读取数据超时）
                    .setSocketTimeout(timeOut * 2).build();
            httpPost.setConfig(requestConfig);
            // 3、调用HttpClient实例执行POST实例，返回response
            response = httpClient.execute(httpPost);
            if (null == response) {
                throw new Exception("response为空");
            }
            // 4、解析response
            // 获取状态码
            int status = response.getStatusLine().getStatusCode();
            // 状态码不为成功则抛异常
            if (HttpStatus.SC_OK != status) {
                throw new Exception("code:" + status);
            }
            // 获取实例
            HttpEntity entity = response.getEntity();
            // 获取responseStr
            String responseStr = EntityUtils.toString(entity);
            if (null == responseStr || "".equals(responseStr)) {
                throw new Exception("返回空报文");
            }
        } catch (ConnectTimeoutException cte) {
            errorMsg = "连接超时(" + requestUrl + ")" + cte.getMessage();
            throw new Exception(errorMsg);
        } catch (SocketTimeoutException ste) {
            errorMsg = "数据读取超时(" + requestUrl + ")" + ste.getMessage();
            throw new Exception(errorMsg);
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
            throw new Exception(errorMsg);
        } finally {
            // 释放资源
            if (null != response) {
                response.close();
            }
            if (null != httpPost) {
                httpPost.releaseConnection();
            }
            if (null != httpClient) {
                httpClient.close();
            }
        }
    }

    // 有参数的post请求
    // http://localhost:8080/fmStudy/hello/postByParams?pageNum=1&pageSize=10
    public static void reqByPostParam(String requestUrl, Map<String, String> params) throws Exception {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        String errorMsg = "";
        try {
            // 1、创建HttpClient实例
            httpClient = HttpClients.createDefault();
            // 2、设置参数
            URIBuilder uriBuilder = new URIBuilder(requestUrl);
            for(Map.Entry<String, String> entry : params.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
            // 3、创建POST请求方法实例
            httpPost = new HttpPost(uriBuilder.build());
            int timeOut = 40000;
            // 请求超时配置
            RequestConfig requestConfig = RequestConfig.custom()
                    // 连接目标url的连接超时时间
                    .setConnectTimeout(timeOut)
                    // 从连接池获取连接的超时时间
                    .setConnectionRequestTimeout(timeOut / 2)
                    // 等待响应超时（读取数据超时）
                    .setSocketTimeout(timeOut * 2).build();
            httpPost.setConfig(requestConfig);
            // 4、调用HttpClient实例执行POST实例，返回response
            response = httpClient.execute(httpPost);
            if (null == response) {
                throw new Exception("response为空");
            }
            // 5、解析response
            // 获取状态码
            int status = response.getStatusLine().getStatusCode();
            // 状态码不为成功则抛异常
            if (HttpStatus.SC_OK != status) {
                throw new Exception("code:" + status);
            }
            // 获取实例
            HttpEntity entity = response.getEntity();
            // 获取responseStr
            String responseStr = EntityUtils.toString(entity);
            if (null == responseStr || "".equals(responseStr)) {
                throw new Exception("返回空报文");
            }
        } catch (ConnectTimeoutException cte) {
            errorMsg = "连接超时(" + requestUrl + ")" + cte.getMessage();
            throw new Exception(errorMsg);
        } catch (SocketTimeoutException ste) {
            errorMsg = "数据读取超时(" + requestUrl + ")" + ste.getMessage();
            throw new Exception(errorMsg);
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
            throw new Exception(errorMsg);
        } finally {
            // 6、释放资源
            if (null != response) {
                response.close();
            }
            if (null != httpPost) {
                httpPost.releaseConnection();
            }
            if (null != httpClient) {
                httpClient.close();
            }
        }
    }

    // 请求参数为JSON格式的post请求
    public static void reqByPostJson(String requestUrl, Object requestJson) throws Exception {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        String errorMsg = "";
        try {
            // 1、创建HttpClient实例
            httpClient = HttpClients.createDefault();
            // 2、创建POST请求方法实例
            httpPost = new HttpPost(requestUrl);
            // 3、将对象转换成JSON的字符串
            String requestJsonStr = JSON.toJSONString(requestJson);
            // 4、将JSON放入post请求，并且解决中文乱码问题
            StringEntity stringEntity = new StringEntity(requestJsonStr, HTTP.UTF_8);
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
            int timeOut = 40000;
            // 请求超时配置
            RequestConfig requestConfig = RequestConfig.custom()
                    // 连接目标url的连接超时时间
                    .setConnectTimeout(timeOut)
                    // 从连接池获取连接的超时时间
                    .setConnectionRequestTimeout(timeOut / 2)
                    // 等待响应超时（读取数据超时）
                    .setSocketTimeout(timeOut * 2).build();
            httpPost.setConfig(requestConfig);
            // 5、调用HttpClient实例执行POST实例，返回response
            response = httpClient.execute(httpPost);
            if (null == response) {
                throw new Exception("response为空");
            }
            // 6、解析response
            // 获取状态码
            int status = response.getStatusLine().getStatusCode();
            // 状态码不为成功则抛异常
            if (HttpStatus.SC_OK != status) {
                throw new Exception("code:" + status);
            }
            // 获取实例
            HttpEntity entity = response.getEntity();
            // 获取responseStr
            String responseStr = EntityUtils.toString(entity);
            if (null == responseStr || "".equals(responseStr)) {
                throw new Exception("返回报文为空");
            }
        } catch (ConnectTimeoutException cte) {
            errorMsg = "连接超时(" + requestUrl + ")" + cte.getMessage();
            throw new Exception(errorMsg);
        } catch (SocketTimeoutException ste) {
            errorMsg = "数据读取超时(" + requestUrl + ")" + ste.getMessage();
            throw new Exception(errorMsg);
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
            throw new Exception(errorMsg);
        } finally {
            // 释放支援
            if (null != response) {
                response.close();
            }
            if (null != httpPost) {
                httpPost.releaseConnection();
            }
            if (null != httpClient) {
                httpClient.close();
            }
        }

    }

    // 请求参数为xml格式的post请求
    public static void reqByPostXml(String requestUrl, String xmlStr) throws Exception {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        String errorMsg = "";
        try {
            // 1、创建HttpClient实例
            httpClient = HttpClients.createDefault();
            // 2、创建POST请求方法实例
            httpPost = new HttpPost(requestUrl);
            // 3、将XML放入post请求，并且解决中文乱码问题
            StringEntity stringEntity = new StringEntity(xmlStr, HTTP.UTF_8);
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Content-Type", "application/xml;charset=utf-8");
            int timeOut = 10000;
            // 请求超时配置
            RequestConfig requestConfig = RequestConfig.custom()
                    // 连接目标url的连接超时时间
                    .setConnectTimeout(timeOut)
                    // 从连接池获取连接的超时时间
                    .setConnectionRequestTimeout(timeOut / 2)
                    // 等待响应超时（读取数据超时）
                    .setSocketTimeout(timeOut * 2).build();
            httpPost.setConfig(requestConfig);
            // 4、调用HttpClient实例执行POST实例，返回response
            response = httpClient.execute(httpPost);
            if (null == response) {
                throw new Exception("response为空");
            }
            // 5、解析response
            // 获取状态码
            int status = response.getStatusLine().getStatusCode();
            // 状态码不为成功则抛异常
            if (HttpStatus.SC_OK != status) {
                throw new Exception("code:" + status);
            }
            // 获取实例
            HttpEntity httpEntity = response.getEntity();
            // 获取responseStr
            String responseStr = EntityUtils.toString(httpEntity);
            if (null == responseStr || "".equals(responseStr)) {
                throw new Exception("返回空报文");
            }
        } catch (ConnectTimeoutException cte) {
            errorMsg = "连接超时(" + requestUrl + ")" + cte.getMessage();
            throw new Exception(errorMsg);
        } catch (SocketTimeoutException ste) {
            errorMsg = "数据读取超时(" + requestUrl + ")" + ste.getMessage();
            throw new Exception(errorMsg);
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
            throw new Exception(errorMsg);
        } finally {
            if (null != response) {
                response.close();
            }
            if (null != httpPost) {
                httpPost.releaseConnection();
            }
            if (null != httpClient) {
                httpClient.close();
            }
        }
    }

    // 上传文件的post请求
    public static void reqByPostFile(String requestUrl, String filePath) throws Exception {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        String errorMsg = "";
        try {
            // 1、创建HttpClient实例
            httpClient = HttpClients.createDefault();
            // 2、创建POST请求方法实例
            httpPost = new HttpPost(requestUrl);
            // 3、设置请求超时配置
            int timeOut = 40000;
            RequestConfig requestConfig = RequestConfig.custom()
                    // 连接目标url的连接超时时间
                    .setConnectTimeout(timeOut)
                    // 从连接池获取连接的超时时间
                    .setConnectionRequestTimeout(timeOut / 2)
                    // 等待响应超时（读取数据超时）
                    .setSocketTimeout(timeOut * 2).build();
            httpPost.setConfig(requestConfig);
            // 4、读取文件，并将文件放入请求中
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            fileInputStream.read(buffer);
            fileInputStream.close();
            MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                    // 解决文件名中文乱码问题
                    .setMode(HttpMultipartMode.RFC6532);
            // 读取文件，放入请求，并设置contentType为multipart/form-data
            builder.addBinaryBody("files", buffer, ContentType.MULTIPART_FORM_DATA, file.getName());
            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);
            // 5、调用HttpClient实例执行POST实例，返回response
            response = httpClient.execute(httpPost);
            if (null == response) {
                throw new Exception("response为空");
            }
            // 6、解析response
            // 获取状态码
            int status = response.getStatusLine().getStatusCode();
            // 状态码不为成功则抛异常
            if (HttpStatus.SC_OK != status) {
                throw new Exception("code:" + status);
            }
            // 获取实例
            HttpEntity httpEntity = response.getEntity();
            // 获取responseStr
            String responseStr = EntityUtils.toString(httpEntity);
            if (null == responseStr || "".equals(responseStr)) {
                throw new Exception("返回空报文");
            }
        } catch (ConnectTimeoutException cte) {
            errorMsg = "连接超时(" + requestUrl + ")" + cte.getMessage();
            throw new Exception(errorMsg);
        } catch (SocketTimeoutException ste) {
            errorMsg = "数据读取超时(" + requestUrl + ")" + ste.getMessage();
            throw new Exception(errorMsg);
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
            throw new Exception(errorMsg);
        } finally {
            if (null != response) {
                response.close();
            }
            if (null != httpPost) {
                httpPost.releaseConnection();
            }
            if (null != httpClient) {
                httpClient.close();
            }
        }
    }

    // 上传文件和带param请求的post请求
    public static void reqByPostFileAndParam(String requestUrl, String fileName, Map<String, String> params) throws Exception {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        String errorMsg = "";
        try {
            // 1、创建HttpClient实例
            httpClient = HttpClients.createDefault();
            // 2、创建POST请求方法实例
            httpPost = new HttpPost(requestUrl);
            // 3、设置请求超时配置
            int timeout = 40000;
            RequestConfig requestConfig = RequestConfig.custom()
                    // 连接目标url的连接超时时间
                    .setConnectTimeout(timeout)
                    // 从连接池获取连接的超时时间
                    .setConnectionRequestTimeout(timeout / 2)
                    // 等待响应超时（读取数据超时）
                    .setSocketTimeout(timeout * 2)
                    .build();
            httpPost.setConfig(requestConfig);
            // 4、读取文件，并将文件放入请求中
            File file = new File(fileName);
            byte[] bytes = new byte[(int) file.length()];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            fileInputStream.close();
            MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                    // 解决文件名中文乱码问题
                    .setMode(HttpMultipartMode.RFC6532);
            // 读取文件，放入请求，并设置contentType为multipart/form-data
            builder.addBinaryBody("files", bytes, ContentType.MULTIPART_FORM_DATA, file.getName());
            // 将请求参数放入请求
            ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addTextBody(entry.getKey(), entry.getValue(), contentType);
            }
            HttpEntity multipart = builder.build();
            httpPost.setEntity(multipart);
            // 5、调用HttpClient实例执行POST实例，返回response
            response = httpClient.execute(httpPost);
            if (null == response) {
                throw new Exception("response为空");
            }
            // 6、解析response
            // 获取状态码
            int status = response.getStatusLine().getStatusCode();
            // 状态码不为成功则抛异常
            if (HttpStatus.SC_OK != status) {
                throw new Exception("code:" + status);
            }
            // 获取实例
            HttpEntity httpEntity = response.getEntity();
            // 获取responseStr
            String responseStr = EntityUtils.toString(httpEntity);
            if (null == responseStr || "".equals(responseStr)) {
                throw new Exception("返回空报文");
            }
        } catch (ConnectTimeoutException cte) {
            errorMsg = "连接超时(" + requestUrl + ")" + cte.getMessage();
            throw new Exception(errorMsg);
        } catch (SocketTimeoutException ste) {
            errorMsg = "数据读取超时(" + requestUrl + ")" + ste.getMessage();
            throw new Exception(errorMsg);
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
            throw new Exception(errorMsg);
        } finally {
            // 释放资源
            if (null != response) {
                response.close();
            }
            if (null != httpPost) {
                httpPost.releaseConnection();
            }
            if (null != httpClient) {
                httpClient.close();
            }
        }
    }

    // 上传文件和带JSON请求的post请求
    public static void reqByPostFileAndJson(String requestUrl, String fileName, Object jsonObj) throws Exception {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        String errorMsg = "";
        try {
            // 1、创建HttpClient实例
            httpClient = HttpClients.createDefault();
            // 2、创建POST请求方法实例
            httpPost = new HttpPost(requestUrl);
            // 3、设置请求超时配置
            int timeout = 40000;
            RequestConfig requestConfig = RequestConfig.custom()
                    // 连接目标url的连接超时时间
                    .setConnectTimeout(timeout)
                    // 从连接池获取连接的超时时间
                    .setConnectionRequestTimeout(timeout / 2)
                    // 等待响应超时（读取数据超时）
                    .setSocketTimeout(timeout * 2)
                    .build();
            httpPost.setConfig(requestConfig);
            // 4、读取文件，并将文件放入请求中
            File file = new File(fileName);
            byte[] bytes = new byte[(int) file.length()];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            fileInputStream.close();
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create()
                    // 解决文件名中文乱码问题
                    .setMode(HttpMultipartMode.RFC6532);
            // 读取文件，放入请求，并设置contentType为multipart/form-data
            multipartEntityBuilder.addBinaryBody("files", bytes, ContentType.MULTIPART_FORM_DATA, file.getName());
            // 将JSON请求参数放入请求
            String requestJsonStr = JSON.toJSONString(jsonObj);
            multipartEntityBuilder.addTextBody("jsonData", requestJsonStr, ContentType.create("application/json", Charset.forName("UTF-8")));
            httpPost.setEntity(multipartEntityBuilder.build());
            // 5、调用HttpClient实例执行POST实例，返回response
            response = httpClient.execute(httpPost);
            if (null == response) {
                throw new Exception("response为空");
            }
            // 6、解析response
            // 获取状态码
            int status = response.getStatusLine().getStatusCode();
            // 状态码不为成功则抛异常
            if (HttpStatus.SC_OK != status) {
                throw new Exception("code:" + status);
            }
            // 获取实例
            HttpEntity httpEntity = response.getEntity();
            // 获取responseStr
            String responseStr = EntityUtils.toString(httpEntity);
            if (null == responseStr || "".equals(responseStr)) {
                throw new Exception("返回空报文");
            }
        } catch (ConnectTimeoutException cte) {
            errorMsg = "连接超时(" + requestUrl + ")" + cte.getMessage();
            throw new Exception(errorMsg);
        } catch (SocketTimeoutException ste) {
            errorMsg = "数据读取超时(" + requestUrl + ")" + ste.getMessage();
            throw new Exception(errorMsg);
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
            throw new Exception(errorMsg);
        } finally {
            if (null != response) {
                response.close();
            }
            if (null != httpPost) {
                httpPost.releaseConnection();
            }
            if (null != httpClient) {
                httpClient.close();
            }
        }
    }

    // 请求参数为JSON格式的post请求，返回为对象
    public static void reqByPostJsonAndReturnObj(String requestUrl, Object obj) throws Exception {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        String errorMsg = "";
        try {
            // 1、创建HttpClient实例
            httpClient = HttpClients.createDefault();
            // 2、创建POST请求方法实例
            httpPost = new HttpPost(requestUrl);
            // 请求超时配置
            int timeout = 40000;
            RequestConfig requestConfig = RequestConfig.custom()
                    // 连接目标url的连接超时时间
                    .setConnectTimeout(timeout)
                    // 从连接池获取连接的超时时间
                    .setConnectionRequestTimeout(timeout / 2)
                    // 等待响应超时（读取数据超时）
                    .setSocketTimeout(timeout * 2)
                    .build();
            httpPost.setConfig(requestConfig);
            // 4、将JSON放入post请求，并且解决中文乱码问题
            String requestJsonStr = JSON.toJSONString(obj);
            StringEntity stringEntity = new StringEntity(requestJsonStr, HTTP.UTF_8);
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Content-Type", "application/json");
            // 5、调用HttpClient实例执行POST实例，返回response
            response = httpClient.execute(httpPost);
            if (null == response) {
                throw new Exception("response为空");
            }
            // 6、解析response
            // 获取状态码
            int status = response.getStatusLine().getStatusCode();
            // 状态码不为成功则抛异常
            if (HttpStatus.SC_OK != status) {
                throw new Exception("code:" + status);
            }
            // 获取实例
            HttpEntity httpEntity = response.getEntity();
            // 获取responseStr
            String responseStr = EntityUtils.toString(httpEntity);
            if (null == responseStr || "".equals(responseStr)) {
                throw new Exception("返回空报文");
            }
            // 处理responseStr转化为对象
            User responseObject = JSON.parseObject(responseStr, User.class);
        } catch (ConnectTimeoutException cte) {
            errorMsg = "连接超时(" + requestUrl + ")" + cte.getMessage();
            throw new Exception(errorMsg);
        } catch (SocketTimeoutException ste) {
            errorMsg = "数据读取超时(" + requestUrl + ")" + ste.getMessage();
            throw new Exception(errorMsg);
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
            throw new Exception(errorMsg);
        } finally {
            // 释放资源
            if (null != response) {
                response.close();
            }
            if (null != httpPost) {
                httpPost.releaseConnection();
            }
            if (null != httpClient) {
                httpClient.close();
            }
        }
    }

}

