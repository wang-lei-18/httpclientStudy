package com.example.fmStudy.utils;

/**
 * <pre>
 * HttpClient异常类
 * </pre>
 *
 * @author 王蕾<br>
 * <b>mail</b> wanglei@syncsoft.com.cn<br>
 * <b>date</b> 2023年12月25日<br>
 * @version 1.0.0
 *
 * <pre>
 * 修改记录
 *  版本号		修订日期		修改人		bug编号		修改内容
 *  1.0.0		2023/12/25	王蕾	    		新建
 * </pre>
 */
public class HttpClientException extends RuntimeException {

    public HttpClientException() {
    }

    public HttpClientException(String msg) {
        super(msg);
    }

    public HttpClientException(Throwable cause) {
        super(cause);
    }

    public HttpClientException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
