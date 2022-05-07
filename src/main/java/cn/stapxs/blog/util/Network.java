package cn.stapxs.blog.util;

import cn.stapxs.blog.AppInfo;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Version: 1.0
 * @Date: 2021/9/2 下午 7:31
 * @ClassName: NetWork
 * @Author: Stapxs
 * @Description TO DO
 **/
public class Network {
    public static String get(String url, String charset) {
        // 生成 HttpClient 对象并设置参数
        HttpClient httpClient = new HttpClient();
        // 设置 Http 连接超时为 5 秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

        // 生成 GetMethod 对象并设置参数
        GetMethod getMethod = new GetMethod(url);
        //设置get请求超时为5秒
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        //设置请求重试处理，用的是默认的重试处理：请求三次
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

        // 设置请求头
        getMethod.setRequestHeader("User-Agent", "FructoseBlog/" + AppInfo.APP_VERSION);

        // 执行HTTP GET 请求
        String response = "";
        try {
            int statusCode = httpClient.executeMethod(getMethod);

            // 判断访问的状态码
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("请求出错：" + getMethod.getStatusLine());
            }

            // 处理HTTP响应内容
            //HTTP响应头部信息
            Header[] headers = getMethod.getResponseHeaders();
            //读取HTTP响应内容
            //读取为字节数组
            byte[] responseBody = getMethod.getResponseBody();
            // 尝试解压 gzip
            try {
                responseBody = Gzip.uncompress(responseBody);
            } finally {
                response = new String(responseBody, charset);
            }
        } catch (HttpException e) {
            //发生致命的异常，可能是协议不对或者返回的内容有问题
            System.out.println("请检查输入的URL!");
            e.printStackTrace();
        } catch (IOException e) {
            //发生网络异常
            System.out.println("发生网络异常!");
        } finally {
            // 释放连接
            getMethod.releaseConnection();
        }
        return response;
    }

    public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
