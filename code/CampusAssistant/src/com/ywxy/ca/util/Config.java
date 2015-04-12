package com.ywxy.ca.util;

public class Config {
	public static final int REQUEST_ADD_CODE = 0x100;
	public static final int RESPONSE_SUC_CODE = 0x101;
	public static final int RESPONSE_FAIL_CODE = 0x102;

	/**
	 * 四六级在线查询
	 */
	public static final String API_QUERY46 = "http://cet.99sushe.com/find";
	/**
	 * 公共URL
	 */
	public static final String API_BASE = "http://www.dengwz.com/tools/index.php/Api";
	/**
	 * 授权
	 */
	public static final String API_AUTH = "/auth";
	/**
	 * 登录
	 */
	public static final String API_LOGIN = "/Login";
	/**
	 * 反馈
	 */
	public static final String API_FEEDBACK = "/feedback";
	/**
	 * 
	 */
	public static final String API_GET_ALL_TERMS_GRADE = "/getAllSchoolYear";
	/**
	 * 心跳包保持在线
	 */
	public static final String API_KEEP_LOGIN = "/KeepLogin";
	/**
	 * 获取学期详情成绩
	 */
	public static final String API_GET_GRADE = "/GetPersonGrade";
	/**
	 * 
	 */
	public static final String API_GET_ALL_GRADE = "/getAllGrade";

	public static final String CACHE_FILE = "history_cache.json";
	public static final String CET_CACHE_FILE = "cet_cache.json";

	public static final String KEY_GET_NEW_STUDENT = "key_get_new_student";
	public static final String KEY_STUDENT = "key_student";
	public static final String LOG_TAG = "campusassist";
}
