package com.ywxy.ca.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.ywxy.ca.entity.StudentGradeInfo;

/**
 * 历史记录缓存类
 * 注释TEST
 */
public class CacheUtil {

	/*
	 * 从缓存文件读取JSON文本串，并进行解析
	 */
	// public static List<HistoryItem> getHistoryCache(Context context) {
	// List<HistoryItem> listHistory = new ArrayList<HistoryItem>();
	// try {
	// FileInputStream inStream = context
	// .openFileInput(Constant.CACHE_FILE);
	// ByteArrayOutputStream stream = new ByteArrayOutputStream();
	// byte[] buffer = new byte[1024];
	// int length = -1;
	// while ((length = inStream.read(buffer)) != -1) {
	// stream.write(buffer, 0, length);
	// }
	// stream.close();
	// inStream.close();
	// String res = stream.toString();
	// listHistory
	// .addAll(JsonUtil.parseObjectList(res, HistoryItem.class));
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return listHistory;
	// }

	public static List<StudentGradeInfo> getHistoryCache(Context context) {
		List<StudentGradeInfo> listHistory = new ArrayList<StudentGradeInfo>();
		try {
			FileInputStream inStream = context
					.openFileInput(Constant.CACHE_FILE);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				stream.write(buffer, 0, length);
			}
			stream.close();
			inStream.close();
			String res = stream.toString();
			listHistory.addAll(JsonUtil.parseObjectList(res,
					StudentGradeInfo.class));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listHistory;
	}

	// 使用泛型获取缓存记录
	public static <T> List<T> getCache(Context context, Class<T> cls,
			String source_file) {
		List<T> listHistory = new ArrayList<T>();
		try {

			FileInputStream inStream = null;
			if (source_file.equals(Constant.CACHE_FILE)) {
				inStream = context.openFileInput(Constant.CACHE_FILE);
			} else if (source_file.equals(Constant.CET_CACHE_FILE)) {
				inStream = context.openFileInput(Constant.CET_CACHE_FILE);
			} else {
				return listHistory;
			}
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				stream.write(buffer, 0, length);
			}
			stream.close();
			inStream.close();
			String res = stream.toString();
			listHistory.addAll(JsonUtil.parseObjectList(res, cls));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listHistory;
	}

	/**
	 * 更新缓存
	 * 
	 * @param <T>
	 * 
	 * @param item
	 * @return
	 */
	public static <T> void setCache(Context context, List<T> itemList,
			String source_file) {
		try {
			FileOutputStream outStream = null;
			if (source_file.equals(Constant.CACHE_FILE)) {
				outStream = context.openFileOutput(Constant.CACHE_FILE,
						Context.MODE_PRIVATE);
			} else if (source_file.equals(Constant.CET_CACHE_FILE)) {
				outStream = context.openFileOutput(Constant.CET_CACHE_FILE,
						Context.MODE_PRIVATE);
			} else {
				return;
			}
			outStream.write(JSON.toJSONString(itemList).getBytes());
			outStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean isHistoryItemExist(Context context, String userid) {
		List<StudentGradeInfo> listHistory = getHistoryCache(context);
		for (StudentGradeInfo item : listHistory) {
			if (item.getSno().equals(userid)) {
				return true;
			}
		}
		return false;
	}
}
