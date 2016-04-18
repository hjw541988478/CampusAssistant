package cn.edu.university.zfcms.http;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.edu.university.zfcms.util.SpUtil;

/**
 * Created by hjw on 16/4/14.
 */
public class HttpManager {

    public static HttpClient mHttpClient = new DefaultHttpClient();

    private static final String USER_AGENT_DEFAULT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36";

    static {
        HttpClientParams.setCookiePolicy(mHttpClient.getParams(),CookiePolicy.BROWSER_COMPATIBILITY);
        HttpConnectionParams.setConnectionTimeout(mHttpClient.getParams(),10 * 1000);
        HttpConnectionParams.setSoTimeout(mHttpClient.getParams(),10 * 1000);
        HttpProtocolParams.setUserAgent(mHttpClient.getParams(),USER_AGENT_DEFAULT);
    }

    private static String CHARSET_DEFAULT = "gb2312";

    // 众多 post 请求需要带上状态请求头
    private static void buildViewStateParam(List<NameValuePair> paramPairs){
        paramPairs.add(new BasicNameValuePair("__VIEWSTATE", SpUtil.getNewerStateHeaderVal()));
    }

    public static HttpPost post(String url, Map<String,String> params, Map<String,String> headers){
        HttpPost post = new HttpPost(url);
        configBasicHeaders(headers);
        try {
            for (Map.Entry<String,String> headerEntry : headers.entrySet()) {
                post.setHeader(headerEntry.getKey(),headerEntry.getValue());
            }
            List<NameValuePair> paramPairs = new ArrayList<>();
            for (Map.Entry<String,String> paramEntry: params.entrySet()) {
                paramPairs.add(new BasicNameValuePair(paramEntry.getKey(),paramEntry.getValue()));
            }
            buildViewStateParam(paramPairs);
            post.setEntity(new UrlEncodedFormEntity(paramPairs, CHARSET_DEFAULT));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return post;
    }

    public static HttpGet get(String url, Map<String,String> headers){
        HttpGet get = new HttpGet(url);
        configBasicHeaders(headers);
        for (Map.Entry<String,String> headerEntry : headers.entrySet()) {
            get.setHeader(headerEntry.getKey(),headerEntry.getValue());
        }
        return get;
    }


    public static boolean isRequestSuccessful(int code) {
        return code >= 200 && code < 300;
    }

    public static String parseStringResponse(HttpResponse response){
        String result = "";
        try {
            if (response != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (isRequestSuccessful(statusCode)) {
                    result = EntityUtils.toString(response.getEntity(), CHARSET_DEFAULT);
                } else {
                    result = "status code:" + statusCode + ",request failure.";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static HttpResponse performRequest(HttpUriRequest request){

        HttpResponse response = null;
        try {
            response =  mHttpClient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private static void configBasicHeaders(Map<String,String> headers){
        if (headers == null)
            return;
        headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headers.put("Accept-Encoding","gzip, deflate");
        headers.put("Accept-Language","zh-CN,zh;q=0.8,en;q=0.6");
        headers.put("Cache-Control","max-age=0");
        headers.put("DNT","1");
        headers.put("Connection","keep-alive");
        headers.put("Upgrade-Insecure-Requests","1");

        headers.put("Host","210.34.213.88");
        headers.put("Origin","http://210.34.213.88");
        headers.put("Referer", "http://210.34.213.88/default2.aspx");
    }

}
