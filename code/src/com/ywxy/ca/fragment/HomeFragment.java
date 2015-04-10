package com.ywxy.ca.fragment;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ywxy.ca.R;
import com.ywxy.ca.activity.CalendarActivity;
import com.ywxy.ca.activity.CetQueryActivity;
import com.ywxy.ca.activity.HistoryActivity;
import com.ywxy.ca.entity.HomePageItem;

public class HomeFragment extends BaseFragment {
	private View currentView;
	private ListView mListView;
	private List<HomePageItem> mlist;
	private Context context;
	private LinearLayout btn_grade, btn_46, btn_calendar;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.context = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView = inflater
				.inflate(R.layout.fragment_home, container, false);
		btn_grade = (LinearLayout) currentView.findViewById(R.id.btn_func_home);
		btn_46 = (LinearLayout) currentView.findViewById(R.id.btn_func_46);
		btn_calendar = (LinearLayout) currentView
				.findViewById(R.id.btn_func_calendar);
		btn_grade.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getActivity(), HistoryActivity.class));
			}
		});
		btn_46.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getActivity(), CetQueryActivity.class));
			}
		});
		btn_calendar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), CalendarActivity.class));
			}
		});
		return currentView;
	}

}
