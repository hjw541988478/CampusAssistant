package com.ywxy.ca.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ywxy.ca.R;
import com.ywxy.ca.entity.SemesterAllGradeItem;

public class CalendarListAdapter extends BaseAdapter {

	private Map<Integer, List<String>> map_data;
	private List<String> monthPage;
	private LayoutInflater inflater;
	private Context context;

	public CalendarListAdapter(Context context,
			Map<Integer, List<String>> map_data) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.map_data = map_data;
		monthPage = new ArrayList<String>();
	}

	public void setCurrentPageData(int month) {
		monthPage = map_data.get(Integer.valueOf(month));
	}

	@Override
	public int getCount() {
		return monthPage.size();
	}

	@Override
	public Object getItem(int position) {
		return monthPage.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.aty_calendar_item, null);
			holder = new ViewHolder();
			holder.calendar_thing = (TextView) convertView
					.findViewById(R.id.calendar_thing);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.calendar_thing.setText(monthPage.get(position));
		return convertView;
	}

	public class ViewHolder {
		public TextView calendar_thing;
	}

}
