package com.ywxy.ca.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.dd.processbutton.iml.ActionProcessButton;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.ywxy.ca.R;
import com.ywxy.ca.entity.CollegeSemester;
import com.ywxy.ca.entity.SemesterGrade;
import com.ywxy.ca.entity.StudentGradeInfo;
import com.ywxy.ca.entity.SumGradeInfo;
import com.ywxy.ca.http.HttpUtil;
import com.ywxy.ca.http.HttpUtil.HttpRequestCallback;
import com.ywxy.ca.util.CacheUtil;
import com.ywxy.ca.util.Config;
import com.ywxy.ca.util.NetUtil;
import com.ywxy.ca.util.ViewUtil;
import com.ywxy.ca.view.FloatLabelEditText;

public class SignInActivity extends Activity {

	private FloatLabelEditText edt_sno, edt_spwd;
	private CheckBox cb_agree;
	private ImageButton btn_sign_back;
	private int mProgress = 0;
	private ActionProcessButton apb_login;
	private HttpUtil mHttpUtil;
	private Context context;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHttpUtil = null;
	}

	@Override
	public void onBackPressed() {
		doBackToHome();
	}

	private void doBackToHome() {
		setResult(Config.RESPONSE_FAIL_CODE);
		finish();
	}

	public static class CustomLabelAnimator implements
			FloatLabelEditText.LabelAnimator {
		/* package */static final float SCALE_X_SHOWN = 1f;
		/* package */static final float SCALE_X_HIDDEN = 2f;
		/* package */static final float SCALE_Y_SHOWN = 1f;
		/* package */static final float SCALE_Y_HIDDEN = 0f;

		@Override
		public void onDisplayLabel(View label) {
			final float shift = label.getWidth() / 2;
			ViewHelper.setScaleX(label, SCALE_X_HIDDEN);
			ViewHelper.setScaleY(label, SCALE_Y_HIDDEN);
			ViewHelper.setX(label, shift);
			ViewPropertyAnimator.animate(label).alpha(1).scaleX(SCALE_X_SHOWN)
					.scaleY(SCALE_Y_SHOWN).x(0f);
		}

		@Override
		public void onHideLabel(View label) {
			final float shift = label.getWidth() / 2;
			ViewHelper.setScaleX(label, SCALE_X_SHOWN);
			ViewHelper.setScaleY(label, SCALE_Y_SHOWN);
			ViewHelper.setX(label, 0f);
			ViewPropertyAnimator.animate(label).alpha(0).scaleX(SCALE_X_HIDDEN)
					.scaleY(SCALE_Y_HIDDEN).x(shift);
		}
	}

	private boolean isSnoValid(String sno, String spwd) {
		if (TextUtils.isEmpty(sno)) {
			ViewUtil.toastText(context, "学号不能为空~", false);
			return false;
		}
		if (sno.trim().length() != 11) {
			ViewUtil.toastText(context, "学号只能为11位~", false);
			return false;
		}
		if (TextUtils.isEmpty(spwd)) {
			ViewUtil.toastText(context, "密码不能为空~", false);
			return false;
		}
		return true;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = SignInActivity.this;
		mHttpUtil = new HttpUtil();
		setContentView(R.layout.activity_signin);
		cb_agree = (CheckBox) findViewById(R.id.cb_agree);
		apb_login = (ActionProcessButton) findViewById(R.id.btnSignIn);
		edt_sno = (FloatLabelEditText) findViewById(R.id.edt_sno);
		edt_spwd = (FloatLabelEditText) findViewById(R.id.edt_spwd);
		edt_sno.setLabelAnimator(new CustomLabelAnimator());
		edt_spwd.setLabelAnimator(new CustomLabelAnimator());
		btn_sign_back = (ImageButton) findViewById(R.id.btn_signin_back);
		btn_sign_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				doBackToHome();
			}
		});
		apb_login.setMode(ActionProcessButton.Mode.ENDLESS);
		apb_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!NetUtil.isNetwork(SignInActivity.this)) {
					ViewUtil.toastText(context, "网络不可用~", false);
					return;
				}
				if (apb_login.getProgress() == -1) {
					apb_login.setProgress(apb_login.getMinProgress());
					return;
				}
				if (!isSnoValid(edt_sno.getText(), edt_spwd.getText())) {
					return;
				}
				if (CacheUtil.isHistoryItemExist(SignInActivity.this, edt_sno
						.getText().toString())) {
					ViewUtil.toastText(SignInActivity.this, "已存在数据,无需添加~",
							false);
					return;
				}
				((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(apb_login.getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				final Handler handler = new Handler();
				handler.post(new Runnable() {
					@Override
					public void run() {
						apb_login.setProgress(mProgress);
						if (mProgress < 95) {
							mProgress += 5;
							handler.postDelayed(this, 10);
						}
					}
				});
				apb_login.setEnabled(false);
				edt_sno.setEnabled(false);
				edt_spwd.setEnabled(false);
				Map<String, String> params = new HashMap<String, String>();
				params.put("password", edt_spwd.getText().toString());
				params.put("userid", edt_sno.getText().toString());
				final StudentGradeInfo info = new StudentGradeInfo();
				mHttpUtil.auth(params, new HttpRequestCallback() {

					@Override
					public void onSuccess(Object data) {
						if (data instanceof SumGradeInfo) {
							info.setSumGrade((SumGradeInfo) data);
						} else if (data instanceof List) {
							Log.d(Config.LOG_TAG,
									"CollegeSemester onSuccess:"
											+ ((List<CollegeSemester>) data)
													.size());
							info.setCollegeList((List<CollegeSemester>) data);
						} else {
							Log.d(Config.LOG_TAG,
									"Map<String, SemesterGrade> onSuccess:"
											+ ((Map<String, SemesterGrade>) data)
													.size());
							info.setFlag(false);
							info.setSno(edt_sno.getText().toString());
							info.setAllSemMap((Map<String, SemesterGrade>) data);
							Log.d(Config.LOG_TAG, "onSuccess"
									+ info.getAllSemMap().size());
							Intent intent = new Intent();
							intent.putExtra(Config.KEY_GET_NEW_STUDENT, info);
							setResult(Config.RESPONSE_SUC_CODE, intent);
							finish();
						}
					}

					@Override
					public void onFail(Object data) {
						apb_login.post(new Runnable() {

							@Override
							public void run() {
								apb_login.setProgress(-1);
							}
						});
						apb_login.postDelayed(new Runnable() {

							@Override
							public void run() {
								apb_login.setProgress(apb_login
										.getMinProgress());
								apb_login.setEnabled(true);
								edt_sno.setEnabled(true);
								edt_spwd.setEnabled(true);
							}
						}, 2000);
						ViewUtil.toastText(SignInActivity.this,
								data.toString(), true);
					}
				});
			}
		});
	}
}
