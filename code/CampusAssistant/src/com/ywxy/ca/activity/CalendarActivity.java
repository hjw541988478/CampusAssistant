package com.ywxy.ca.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.ywxy.ca.R;
import com.ywxy.ca.adapter.CalendarListAdapter;

public class CalendarActivity extends Activity {
	private ImageButton btn_back;
	private MaterialCalendarView md_calendar;
	private ProgressDialog dialog;
	private Context context;
	private LinearLayout ll_calen;
	private ListView lv_notes;
	private CalendarListAdapter adapter = null;
	private Map<Integer, List<String>> month_data = new HashMap<Integer, List<String>>();

	class MyAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(context);
			dialog.setMessage("校历加载中...");
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			List<String> strList = new ArrayList<String>();
			strList.add("3月2日 报到注册");
			strList.add("3月3日 报到注册");
			strList.add("3月4日 正式上课");
			month_data.put(3, strList);
			strList = new ArrayList<String>();
			strList.add("4月5日 清明节");
			month_data.put(4, strList);
			strList = new ArrayList<String>();
			strList.add("5月1日 劳动节");
			month_data.put(5, strList);
			strList = new ArrayList<String>();
			strList.add("6月20日 端午节");
			month_data.put(6, strList);
			strList = new ArrayList<String>();
			strList.add("7月9日 暑假开始");
			month_data.put(7, strList);
			strList = new ArrayList<String>();
			strList.add("8月27日 暑假结束");
			month_data.put(8, strList);
			md_calendar = new MaterialCalendarView(context);
			md_calendar.setShowOtherDates(true);
			Calendar maxCalen = Calendar.getInstance();
			maxCalen.set(Calendar.YEAR, 2015);
			maxCalen.set(Calendar.MONTH, Calendar.AUGUST);
			maxCalen.set(Calendar.DAY_OF_MONTH, 27);
			md_calendar.setMaximumDate(maxCalen);
			maxCalen.set(Calendar.YEAR, 2015);
			maxCalen.set(Calendar.MONTH, Calendar.MARCH);
			maxCalen.set(Calendar.DAY_OF_MONTH, 2);
			md_calendar.setMinimumDate(maxCalen);
			adapter = new CalendarListAdapter(context, month_data);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			ll_calen.addView(md_calendar, 1, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			adapter.setCurrentPageData(3);
			lv_notes.setAdapter(adapter);
			md_calendar.setSelectionColor(Color.parseColor("#949494"));
			md_calendar.setOnMonthChagedListener(new OnMonthChangedListener() {

				@Override
				public void onMonthChanged(int month) {
					adapter.setCurrentPageData(month + 1);
					adapter.notifyDataSetChanged();
				}
			});
			dialog.dismiss();

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		context = this;
		lv_notes = (ListView) findViewById(R.id.lv_notes);
		ll_calen = (LinearLayout) findViewById(R.id.ll_calen);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		new MyAsyncTask().execute();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		md_calendar = null;
	}
}
