package com.ywxy.ca.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ywxy.ca.R;
import com.ywxy.ca.entity.StudentGradeInfo;
import com.ywxy.ca.util.Config;

public class HistoryGridAdapter extends BaseAdapter {

	private Context context;
	private List<StudentGradeInfo> mlist;
	private LayoutInflater inflater;
	private int checkNum = 0;

	public int getCheckNum() {
		return checkNum;
	}

	public void setCheckNum(int checkNum) {
		this.checkNum = checkNum;
	}

	public List<StudentGradeInfo> getDataList() {
		return mlist;
	}

	public void setDataList(List<StudentGradeInfo> list) {
		mlist.clear();
		Log.d(Config.LOG_TAG, "addBoolean:" + mlist.addAll(list));
	}

	public HistoryGridAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		mlist = new ArrayList<StudentGradeInfo>();
	}

	@Override
	public int getCount() {
		return mlist.size();
	}

	@Override
	public Object getItem(int position) {
		return mlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HistoryItemHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.aty_history_item, null);
			holder = new HistoryItemHolder();
			holder.sno = (TextView) convertView
					.findViewById(R.id.grade_list_item_sno);
			holder.sname = (TextView) convertView
					.findViewById(R.id.grade_list_item_sname);
			holder.savg = (TextView) convertView
					.findViewById(R.id.grade_list_item_savg);
			holder.spoint = (TextView) convertView
					.findViewById(R.id.grade_list_item_spoint);
			holder.sfail = (TextView) convertView
					.findViewById(R.id.grade_list_item_sfail);
			holder.scbx = (CheckBox) convertView
					.findViewById(R.id.grades_check);
			holder.ll_root = (LinearLayout) convertView
					.findViewById(R.id.ll_grades);
			holder.rl_sno_bg = (RelativeLayout) convertView
					.findViewById(R.id.rl_sno_bg);
			convertView.setTag(holder);
		} else {
			holder = (HistoryItemHolder) convertView.getTag();
		}
		StudentGradeInfo item = mlist.get(position);
		holder.sname.setText(item.getSumGrade().getTruename());
		holder.sno.setText(item.getSno());
		holder.savg.setText("平均分:" + item.getSumGrade().getAvgGrade());
		holder.spoint.setText("绩点:" + item.getSumGrade().getGradePoint());
		holder.sfail.setText("未通过科目:" + item.getSumGrade().getNopassNum());
		holder.scbx.setChecked(item.isFlag());
		holder.ll_root.setBackgroundColor(item.isFlag() ? context
				.getResources().getColor(R.color.gray_bg) : context
				.getResources().getColor(R.color.white));
		holder.rl_sno_bg.setBackgroundColor(item.isFlag() ? context
				.getResources().getColor(R.color.bg_blue_pressed) : context
				.getResources().getColor(R.color.bg_blue));
		return convertView;
	}

	public class HistoryItemHolder {
		public TextView sno;
		public TextView sname;
		public TextView savg;
		public TextView spoint;
		public TextView sfail;
		public CheckBox scbx;
		public LinearLayout ll_root;
		public RelativeLayout rl_sno_bg;
	}

}
