package com.ywxy.ca.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ywxy.ca.entity.CollegeSemester;
import com.ywxy.ca.entity.SemesterGrade;
import com.ywxy.ca.entity.SumGradeInfo;
import com.ywxy.ca.util.Config;
import com.ywxy.ca.util.JsonUtil;

public class HttpUtil {

	public void query46(final Map<String, String> map,
			final HttpRequestCallback callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				StringBuffer buffer = new StringBuffer();
				InputStream is = null;
				OutputStream os = null;
				if (map != null && !map.isEmpty()) {
					for (Map.Entry<String, String> entry : map.entrySet()) {
						try {
							buffer.append(entry.getKey())
									.append("=")
									.append(URLEncoder.encode(entry.getValue(),
											"gbk")).append("&");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					buffer.deleteCharAt(buffer.length() - 1);
				}
				StringBuilder sb = new StringBuilder();
				HttpURLConnection con = null;
				try {
					URL url = new URL(Config.API_QUERY46);
					con = (HttpURLConnection) url.openConnection();
					con.setDoInput(true);
					con.setDoOutput(true);
					con.setRequestMethod("POST");
					con.setConnectTimeout(3000);
					con.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:19.0) Gecko/20100101 Firefox/19.0");
					con.setRequestProperty("Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
					con.setRequestProperty("Accept-Language",
							"zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
					con.setRequestProperty("Accept-Encoding", "gzip, deflate");
					con.setRequestProperty("Referer", "http://cet.99sushe.com");
					con.setRequestProperty("Connection", "keep-alive");
					byte[] tdata = buffer.toString().getBytes();
					os = con.getOutputStream();
					os.write(tdata);
					os.close();
					if (con.getResponseCode() == 200) {
						is = con.getInputStream();
						byte[] data = new byte[1024];
						try {
							while ((is.read(data)) != -1) {
								sb.append(new String(data, "gbk"));
							}
						} catch (IOException e) {
							e.printStackTrace();
							callback.onFail(e.getMessage());
						}
					} else {
						callback.onFail(con.getResponseCode());
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					callback.onFail(e.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
					callback.onFail(e.getMessage());
				} finally {
					con.disconnect();
				}
				callback.onSuccess(sb.toString());
			}
		}).start();
	}

	private Map<String, String> authParams = null;

	public void auth(Map<String, String> params,
			final HttpRequestCallback callback) {
		Log.d(Config.LOG_TAG, "authing," + params.toString());
		authParams = params;
		loopAuth(Config.API_AUTH, params, callback);
	}

	public int index = 0;

	public void getAllSchoolYear(final Map<String, String> params,
			final HttpRequestCallback callback) {
		params.remove("userid");
		params.remove("password");
		Log.d(Config.LOG_TAG, "getAllSchoolYear,"
				+ Config.API_GET_ALL_TERMS_GRADE + "," + params.toString());
		RestClient.post(Config.API_GET_ALL_TERMS_GRADE, new RequestParams(
				params), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				Log.d(Config.LOG_TAG, response.toString());
				final Map<String, SemesterGrade> allList = new HashMap<String, SemesterGrade>();
				List<CollegeSemester> semsterList = JsonUtil
						.parseCollegeSemester(JSON.parseObject(response
								.toString()));
				Log.d(Config.LOG_TAG, "SemesterList:" + semsterList);
				callback.onSuccess(semsterList);
				final int size = semsterList.size();
				for (int i = 0; i < semsterList.size(); i++) {
					final CollegeSemester col = semsterList.get(i);
					Map<String, String> newParams = new HashMap<String, String>();
					newParams.put("Api_login_id", params.get("Api_login_id"));
					newParams.put("SchoolYear", col.getSchoolYear());
					newParams.put("Semester", col.getSemester());
					getGrade(newParams, new HttpRequestCallback() {
						@Override
						public void onSuccess(Object data) {
							index++;
							SemesterGrade temp = (SemesterGrade) data;
							allList.put(temp.getAvgItem().getSchoolYear()
									+ temp.getAvgItem().getSemester(), temp);
							Log.d(Config.LOG_TAG,
									"RealMapData:" + allList.size());
							if (index == size) {
								callback.onSuccess(allList);
							}
						}

						@Override
						public void onFail(Object data) {
							Log.d(Config.LOG_TAG,
									"getGrade" + data.toString());
							callback.onFail(data.toString());
						}
					});
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				callback.onFail(errorResponse.toString());
			}
		});

	}

	public void getSumAllGrade(final Map<String, String> params,
			final HttpRequestCallback callback) {
		Log.d(Config.LOG_TAG, "getSumAll" + params.toString());
		RestClient.post(Config.API_GET_ALL_GRADE, new RequestParams(params),
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						SumGradeInfo result = JsonUtil.parseSumGradeInfo(JSON
								.parseObject(response.toString()));
						callback.onSuccess(result);
						getAllSchoolYear(params, callback);
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						callback.onFail(errorResponse.toString());
					}
				});
	}

	// 反馈
	public void feedback(Map<String, String> params,
			final HttpRequestCallback callback) {
		RestClient.post(Config.API_FEEDBACK, new RequestParams(params),
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						com.alibaba.fastjson.JSONObject obj = JSON
								.parseObject(response.toString());
						boolean flag = obj.getBooleanValue("status");
						String info = obj.getString("info");
						if (flag) {
							callback.onSuccess(info);
						} else {
							callback.onSuccess(info);
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						callback.onFail(errorResponse.toString());
					}
				});
	}

	// 登录
	public void login(final Map<String, String> params,
			final HttpRequestCallback callback) {
		Log.d(Config.LOG_TAG, "login" + params.toString());
		RestClient.post(Config.API_LOGIN, new RequestParams(params),
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						com.alibaba.fastjson.JSONObject obj = JSON
								.parseObject(response.toString());
						String res = JsonUtil.parseLoginInfo(obj);
						Log.d(Config.LOG_TAG, "res," + response.toString());
						if (JsonUtil.parseLoginSuccess(obj)) {
							// Map<String, String> param = new HashMap<String,
							// String>();
							params.put("Api_login_id", res);
							// 获取平均成绩，绩点，不及格科目
							getSumAllGrade(params, callback);
						} else {
							callback.onFail(res);
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						callback.onFail(errorResponse.toString());
					}
				});
	}

	// 获取学期详情成绩
	public void getGrade(final Map<String, String> params,
			final HttpRequestCallback callback) {
		Log.d(Config.LOG_TAG, "API_GET_GRADE" + params);
		RestClient.post(Config.API_GET_GRADE, new RequestParams(params),
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						com.alibaba.fastjson.JSONObject obj = JSON
								.parseObject(response.toString());
						SemesterGrade data = JsonUtil
								.parseSemesterAllGrade(obj);
						if (data == null) {
							callback.onFail("获取数据失败" + params.toString());
							Log.d(Config.LOG_TAG,
									"获取数据失败" + params.toString());
						} else {
							callback.onSuccess(data);
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						callback.onFail(errorResponse.toString());
					}
				});
	}

	// 获取汇总信息
	public void loopAuth(String relative_url, final Map<String, String> params,
			final HttpRequestCallback callback) {
		Log.d(Config.LOG_TAG, "loopAuth," + params.toString());
		RestClient.post(relative_url, new RequestParams(params),
				new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							JSONObject response) {
						com.alibaba.fastjson.JSONObject obj = JSON
								.parseObject(response.toString());
						Map<String, Object> data = JsonUtil.parseNextUrl(obj);
						if (data.size() != 0) {
							if (data.containsKey("getAllgrade")) {
								Log.d(Config.LOG_TAG, authParams.toString());
								// 进行登录
								login(authParams, callback);
							} else {
								Log.d("TAG", (String) data.get("url"));
								Log.d("TAG", ((HashMap<String, String>) data
										.get("params")).toString());
								loopAuth((String) data.get("url"),
										(Map<String, String>) data
												.get("params"), callback);
							}
						} else {
							Log.d("TAG", obj.toString());
							callback.onFail(obj.toString());
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							Throwable throwable, JSONObject errorResponse) {
						callback.onFail(errorResponse.toString());
					}
				});
	}

	public interface HttpRequestCallback {
		void onSuccess(Object data);

		void onFail(Object data);
	}
}
