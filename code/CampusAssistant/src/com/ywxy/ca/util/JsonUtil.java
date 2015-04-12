package com.ywxy.ca.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ywxy.ca.entity.CollegeSemester;
import com.ywxy.ca.entity.SemesterAllGradeItem;
import com.ywxy.ca.entity.SemesterAvgGradeItem;
import com.ywxy.ca.entity.SemesterGrade;
import com.ywxy.ca.entity.SumGradeInfo;

public class JsonUtil {
	/**
	 * {sno:xx,spswd:xx,sname:xx,savg:xx,spoint:xx,sfail:xx}
	 * 
	 * @param hisJson
	 * @return
	 */
	public static <T> T parseObjectItem(String jsonstring, Class<T> cls) {
		T t = null;
		try {
			t = JSON.parseObject(jsonstring, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * 解析成List
	 * 
	 * @param jsonstring
	 * @param cls
	 * @return
	 */
	public static <T> List<T> parseObjectList(String jsonstring, Class<T> cls) {
		List<T> list = new ArrayList<T>();
		try {
			list = JSON.parseArray(jsonstring, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 返回List<Map>类型
	 * 
	 * @param jsonstring
	 * @return
	 */
	public static <T> List<HashMap<String, Object>> parseMapList(
			String jsonstring) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		try {
			list = JSON.parseObject(jsonstring,
					new TypeReference<List<HashMap<String, Object>>>() {
					}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static Map<String, Object> parseNextUrl(JSONObject obj) {
		Log.d(Config.LOG_TAG, obj.toJSONString());
		Map<String, Object> data = new HashMap<String, Object>();
		boolean status = obj.getBoolean("status");
		if (status) {
			String url = obj.getJSONObject("back").getString("url");
			if (url == null || "".equals(url.trim())) {
				data.put("getAllgrade", "getAllgrade");
			} else {
				JSONObject dataArr = obj.getJSONObject("back").getJSONObject(
						"data");
				data.put("url", url);
				Map<String, String> params = new HashMap<String, String>();
				Log.d("loop", dataArr.toJSONString());
				Set<String> keys = dataArr.keySet();
				for (String key : keys) {
					params.put(key, dataArr.getString(key));
				}
				data.put("params", params.size() == 0 ? null : params);
			}
		}
		return data;
	}

	/**
	 * 解析一学期的成绩，包括普通学科成绩和平均成绩
	 * 
	 * @param obj
	 * @return
	 */
	public static SemesterGrade parseSemesterAllGrade(JSONObject obj) {
		SemesterGrade semesterGrade = null;
		List<SemesterAllGradeItem> gradeList = null;
		SemesterAvgGradeItem avg_grade = null;
		boolean status = obj.getBoolean("status");
		if (status) {
			semesterGrade = new SemesterGrade();
			JSONArray gradeArray = obj.getJSONObject("data").getJSONArray(
					"GradeData");
			JSONArray avgArray = obj.getJSONObject("data").getJSONArray("Avg");
			gradeList = JsonUtil.parseObjectList(gradeArray.toJSONString(),
					SemesterAllGradeItem.class);
			JSONObject avgObject =  avgArray.getJSONObject(0);
			avg_grade = new SemesterAvgGradeItem();
			avg_grade.setAvgGrade(avgObject.getString("AvgGrade"));
			avg_grade.setClassRank(avgObject.getString("ClassRank"));
			avg_grade.setGradePoint(avgObject.getString("GradePoint"));
			avg_grade.setSchoolYear(avgObject.getString("schoolYear"));
			avg_grade.setSemester(avgObject.getString("semester"));
			avg_grade.setStudentid(avgObject.getString("studentid"));
			avg_grade.setUpdateDate(avgObject.getString("UpdateDate"));
			semesterGrade.setAllItem(gradeList);
			semesterGrade.setAvgItem(avg_grade);
		}
		return semesterGrade;
	}

	public static boolean parseLoginSuccess(JSONObject obj) {
		return obj.getBoolean("status");
	}

	public static String parseLoginInfo(JSONObject obj) {
		boolean isLogin = obj.getBoolean("status");
		if (isLogin) {
			return obj.getJSONObject("data").getString("Api_login_id");
		} else {
			return obj.getString("info");
		}
	}

	public static List<CollegeSemester> parseCollegeSemester(JSONObject obj) {
		List<CollegeSemester> collegeList = new ArrayList<CollegeSemester>();
		boolean status = obj.getBoolean("status");
		if (status) {
			JSONArray arr = obj.getJSONObject("data").getJSONArray("data");
			collegeList = JsonUtil.parseObjectList(arr.toJSONString(),
					CollegeSemester.class);
		}
		return collegeList;
	}

	public static SumGradeInfo parseSumGradeInfo(JSONObject obj) {
		SumGradeInfo sumInfo = null;
		if (obj.getBoolean("status")) {
			sumInfo = new SumGradeInfo();
			sumInfo.setGradePoint(obj.getJSONObject("data").getFloat(
					"GradePoint"));
			sumInfo.setAvgGrade(obj.getJSONObject("data").getFloat("AvgGrade"));
			sumInfo.setNopassNum(obj.getJSONObject("data").getInteger(
					"NopassNum"));
			sumInfo.setTruename(obj.getJSONObject("data").getString("Truename"));
			String noPassList = obj.getJSONObject("data").getString("Nopass");
			if (noPassList == null) {
				sumInfo.setNoPassList(new ArrayList<SemesterAllGradeItem>());
			} else {
				sumInfo.setNoPassList(JsonUtil.parseObjectList(noPassList,
						SemesterAllGradeItem.class));
			}
		}
		return sumInfo;
	}
}
