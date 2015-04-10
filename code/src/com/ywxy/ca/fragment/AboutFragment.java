package com.ywxy.ca.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.ywxy.ca.R;
import com.ywxy.ca.view.TeamMemberGridView;

public class AboutFragment extends BaseFragment {

	private TeamMemberGridView gv_team;
	private SimpleAdapter adapter;
	private List<Map<String, String>> data;

	public AboutFragment(SimpleAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_about, container, false);
		gv_team = (TeamMemberGridView) view.findViewById(R.id.gv_team);
		gv_team.setAdapter(adapter);
		return view;
	}

}
