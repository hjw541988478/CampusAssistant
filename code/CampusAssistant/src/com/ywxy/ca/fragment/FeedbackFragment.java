package com.ywxy.ca.fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dd.processbutton.iml.GenerateProcessButton;
import com.dd.processbutton.iml.SubmitProcessButton;
import com.ywxy.ca.R;
import com.ywxy.ca.http.HttpUtil;
import com.ywxy.ca.http.HttpUtil.HttpRequestCallback;
import com.ywxy.ca.util.Config;
import com.ywxy.ca.util.NetUtil;
import com.ywxy.ca.util.ViewUtil;

public class FeedbackFragment extends BaseFragment {

	private EditText edt_name;
	private EditText edt_contact;
	private EditText edt_content;
	private SubmitProcessButton btn_feedback;
	private int mProgress = 0;
	private Context context;
	private HttpUtil mHttpUtil;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = getActivity();
		mHttpUtil = new HttpUtil();
	}

	private void setEditTextEnabled(boolean flag) {
		edt_name.setEnabled(flag);
		edt_contact.setEnabled(flag);
		edt_content.setEnabled(flag);
		btn_feedback.setEnabled(flag);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_feedback, container,
				false);
		edt_name = (EditText) view.findViewById(R.id.edt_name);
		edt_contact = (EditText) view.findViewById(R.id.edt_contact);
		edt_content = (EditText) view.findViewById(R.id.edt_content);
		btn_feedback = (SubmitProcessButton) view
				.findViewById(R.id.btn_feedback);
		btn_feedback.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!NetUtil.isNetwork(context)) {
					ViewUtil.toastText(getActivity(), "无可用网络~", false);
					return;
				}
				if (btn_feedback.getProgress() == -1
						|| btn_feedback.getProgress() == 100) {
					btn_feedback.setProgress(0);
				}
				if (!TextUtils.isEmpty(edt_name.getText().toString().trim())) {
					if (edt_name.getText().toString().trim().length() >= 160) {
						ViewUtil.toastText(getActivity(), "大名字数不能超过160字~",
								false);
						return;
					}
				}
				if (!TextUtils.isEmpty(edt_contact.getText().toString().trim())) {
					if (edt_contact.getText().toString().trim().length() >= 160) {
						ViewUtil.toastText(getActivity(), "联系方式字数不能超过160字~",
								false);
						return;
					}
				}
				if (TextUtils.isEmpty(edt_content.getText().toString())) {
					ViewUtil.toastText(getActivity(), "反馈内容不能为空~", false);
					return;
				}
				if (edt_content.getText().toString().trim().length() >= 160) {
					ViewUtil.toastText(getActivity(), "字数不能超过160字~", false);
					return;
				}
				((InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(btn_feedback.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				setEditTextEnabled(false);
				final Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mProgress += 10;
						btn_feedback.setProgress(mProgress);
						if (mProgress < 100) {
							handler.postDelayed(this, new Random().nextInt(300));
						} else {
							mProgress = 0;
							ViewUtil.toastText(context, "反馈成功", false);
							btn_feedback.postDelayed(new Runnable() {

								@Override
								public void run() {
									btn_feedback.setProgress(0);
									setEditTextEnabled(true);
								}
							}, 1000);
						}
					}
				}, new Random().nextInt(1000));
				Map<String, String> params = new HashMap<String, String>();
				params.put("Truename", edt_name.getText().toString());
				params.put("Contect", edt_contact.getText().toString());
				params.put("Content", edt_content.getText().toString());
				mHttpUtil.feedback(params, new HttpRequestCallback() {

					@Override
					public void onSuccess(Object data) {

						// btn_feedback.post(new Runnable() {
						//
						// @Override
						// public void run() {
						// btn_feedback.setProgress(100);
						// }
						// });

					}

					@Override
					public void onFail(Object data) {
						ViewUtil.toastText(context, data.toString(), false);
						btn_feedback.post(new Runnable() {

							@Override
							public void run() {
								btn_feedback.setProgress(-1);
							}
						});
						btn_feedback.postDelayed(new Runnable() {

							@Override
							public void run() {
								btn_feedback.setProgress(0);
								setEditTextEnabled(true);
							}
						}, 1000);
					}
				});
			}
		});
		return view;
	}
}
