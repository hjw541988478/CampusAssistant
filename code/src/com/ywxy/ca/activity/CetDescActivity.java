package com.ywxy.ca.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ywxy.ca.R;
import com.ywxy.ca.entity.CetScore;

public class CetDescActivity extends Activity {

	private TextView tv_cet_no, tv_cet_name, tv_cet_level, tv_cet_college,
			tv_cet_sum, tv_cet_listening, tv_cet_writing, tv_cet_reading;
	private ImageButton btn_back;

	@Override
	public void onBackPressed() {
		doBackToHome();
	}

	private void doBackToHome() {
		finish();
	}

	private CetScore data;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		data = (CetScore) getIntent().getSerializableExtra("cet_score");
		setContentView(R.layout.activity_cet_desc);
		tv_cet_no = (TextView) findViewById(R.id.tv_cet_no);
		tv_cet_name = (TextView) findViewById(R.id.tv_cet_name);
		tv_cet_level = (TextView) findViewById(R.id.tv_cet_level);
		tv_cet_college = (TextView) findViewById(R.id.tv_cet_college);
		tv_cet_sum = (TextView) findViewById(R.id.tv_cet_sum);
		tv_cet_listening = (TextView) findViewById(R.id.tv_cet_listening);
		tv_cet_writing = (TextView) findViewById(R.id.tv_cet_writing);
		tv_cet_reading = (TextView) findViewById(R.id.tv_cet_reading);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
//		SwipeBackLayout layout = getSwipeBackLayout();
//		layout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				doBackToHome();
			}
		});
		tv_cet_no.setText(data.getCet_no());
		tv_cet_name.setText(data.getCet_name());
		tv_cet_level.setText(data.getCet_level());
		tv_cet_college.setText(data.getCet_college());
		tv_cet_sum.setText(data.getCet_sum_score());
		tv_cet_listening.setText(data.getCet_listening_score());
		tv_cet_writing.setText(data.getCet_writing_score());
		tv_cet_reading.setText(data.getCet_reading_score());
	}
}
