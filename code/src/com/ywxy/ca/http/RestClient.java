package com.ywxy.ca.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ywxy.ca.util.Constant;

public class RestClient {

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void query46(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		AsyncHttpClient queryClient = new AsyncHttpClient();
		queryClient
				.addHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:19.0) Gecko/20100101 Firefox/19.0");
		queryClient
				.addHeader("Accept",
						"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		queryClient.addHeader("Accept-Language",
				"zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		queryClient.addHeader("Accept-Encoding", "gzip, deflate");
		queryClient.addHeader("Referer", "http://cet.99sushe.com");
		queryClient.addHeader("Connection", "keep-alive");

		queryClient.post(url, params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return Constant.API_BASE + relativeUrl;
	}
}
