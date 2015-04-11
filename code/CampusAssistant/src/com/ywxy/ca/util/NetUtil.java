package com.ywxy.ca.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {

	/**
	 * 
	 * 方法说明：判断是否开启网络
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean isNetwork(Context mContext) {
		ConnectivityManager manager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info == null || !info.isAvailable()) {
			return false;
		}
		return true;
	}
	//
	// /**
	// *
	// * 方法说明：访问网络接口,接到json数据
	// *
	// * @param mContext
	// * @param url
	// * @param jsonparams
	// * @return
	// */
	// public static void getJsonDataViaPost(Context mContext, String url,
	// Map<String, String> params, final LoadJson loadjson) {
	// AsyncHttpClient client = new AsyncHttpClient();
	// client.post(mContext, url, new RequestParams(params),
	// new JsonHttpResponseHandler() {
	//
	// @Override
	// public void onFailure(int statusCode, Header[] headers,
	// String responseString, Throwable throwable) {
	// Log.d("TAG", "请求数据失败:" + responseString);
	// }
	//
	// @Override
	// public void onSuccess(int statusCode, Header[] headers,
	// JSONObject response) {
	// loadjson.getJson(response.toString());
	// Log.d("TAG", "请求数据成功:" + response.toString());
	// }
	//
	// @Override
	// public void onStart() {
	// Log.d("TAG", "开始请求数据");
	// }
	//
	// @Override
	// public void onFinish() {
	// Log.d("TAG", "请求数据结束");
	// }
	//
	// });
	// }
	//
	// public static void getJsonDataViaGet(Context mContext, String url,
	// final LoadJson loadjson) {
	// AsyncHttpClient client = new AsyncHttpClient();
	// client.get(mContext, url, new JsonHttpResponseHandler() {
	//
	// @Override
	// public void onSuccess(int statusCode, Header[] headers,
	// JSONObject response) {
	// loadjson.getJson(response.toString());
	// Log.d("TAG", "请求数据成功" + response.toString());
	// }
	//
	// @Override
	// public void onStart() {
	// Log.d("TAG", "开始请求数据");
	// }
	//
	// @Override
	// public void onFinish() {
	// Log.d("TAG", "请求数据结束");
	// }
	//
	// });
	// }
	//
	// /**
	// * HttpClient访问网络接口 （暂时）
	// *
	// * @param url
	// * @param jsonparams
	// * @return
	// * @param
	// * @throws
	// * @return
	// */
	// public static String getHttpClientJsonData(String url, String jsonparams)
	// {
	//
	// HttpPost httpPost = new HttpPost(url);
	// StringEntity entity;
	// try {
	// entity = new StringEntity(jsonparams, HTTP.UTF_8);
	// entity.setContentType("application/json");
	// httpPost.setEntity(entity);
	// HttpClient client = new DefaultHttpClient();
	// HttpResponse response = client.execute(httpPost);
	// if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	// HttpEntity resEntity = response.getEntity();
	// result = EntityUtils.toString(resEntity, "utf-8");
	// } else {
	//
	// }
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// } catch (ClientProtocolException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// return result;
	// }
	//
	// /**
	// * 通过地址获取Bitmap （暂时）
	// *
	// * @param imageUrl
	// * @return
	// * @param
	// * @throws
	// * @return
	// */
	// public static Bitmap loadImageFromUrl(String imageUrl) {
	// Bitmap result = null;
	// HttpGet req = new HttpGet(imageUrl);
	// HttpParams connParams = new BasicHttpParams();
	// HttpConnectionParams.setConnectionTimeout(connParams, 5 * 1000);
	// HttpConnectionParams.setSoTimeout(connParams, 5 * 1000);
	// HttpClient client = new DefaultHttpClient(connParams);
	//
	// try {
	// HttpResponse resp = client.execute(req);
	// if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	// HttpEntity respEntity = resp.getEntity();
	// result = BitmapFactory.decodeStream(respEntity.getContent());
	// }
	// } catch (ClientProtocolException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return result;
	//
	// }
	//
	// public interface LoadJson {
	// void getJson(String data);
	// }
}
