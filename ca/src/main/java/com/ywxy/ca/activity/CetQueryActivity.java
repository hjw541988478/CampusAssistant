package com.ywxy.ca.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.ywxy.ca.R;
import com.ywxy.ca.activity.SignInActivity.CustomLabelAnimator;
import com.ywxy.ca.entity.CetScore;
import com.ywxy.ca.http.HttpUtil;
import com.ywxy.ca.http.HttpUtil.HttpRequestCallback;
import com.ywxy.ca.util.Config;
import com.ywxy.ca.util.NetUtil;
import com.ywxy.ca.util.ViewUtil;
import com.ywxy.ca.view.FloatLabelEditText;

import java.util.HashMap;
import java.util.Map;

public class CetQueryActivity extends Activity {

    private FloatLabelEditText edt_cet_name, edt_cet_no;
    private ActionProcessButton btn_cet_query;
    private ListView lv_cethistory;
    private ImageButton btn_back;
    private HttpUtil mHttpUtil;
    private int mProgress = 0;

    private Handler mHandler = new Handler(new Callback() {

        @Override
        public boolean handleMessage(Message arg0) {
            if (arg0.what == 0x1) {
                setStatus((Boolean.valueOf(arg0.obj.toString())));
            }
            return false;
        }
    });

    @Override
    public void onBackPressed() {
        doBackToHome();
    }

    private void doBackToHome() {
        finish();
    }

    private void setStatus(boolean f) {
        btn_cet_query.setEnabled(f);
        edt_cet_name.setEnabled(f);
        edt_cet_no.setEnabled(f);
    }

    private boolean isSnoValid(String no, String name) {
        if (TextUtils.isEmpty(no)) {
            ViewUtil.toastText(CetQueryActivity.this, "׼��֤�Ų���Ϊ��~", false);
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            ViewUtil.toastText(CetQueryActivity.this, "������Ϊ��~", false);
            return false;
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHttpUtil = new HttpUtil();
        // listCetScore = CacheUtil.getCache(CetQueryActivity.this,
        // CetScore.class, Constant.CET_CACHE_FILE);
        // adapter = new CetHistoryListAdapter(CetQueryActivity.this);
        // adapter.setData(listCetScore);
        setContentView(R.layout.activity_cet);
        lv_cethistory = (ListView) findViewById(R.id.lv_cet_history);
        // lv_cethistory.setAdapter(adapter);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        edt_cet_name = (FloatLabelEditText) findViewById(R.id.edt_cetname);
        edt_cet_no = (FloatLabelEditText) findViewById(R.id.edt_cetno);
        edt_cet_name.setLabelAnimator(new CustomLabelAnimator());
        edt_cet_no.setLabelAnimator(new CustomLabelAnimator());
        btn_cet_query = (ActionProcessButton) findViewById(R.id.btn_cetquery);

        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                doBackToHome();
            }
        });
        btn_cet_query.setMode(ActionProcessButton.Mode.ENDLESS);
        btn_cet_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_cet_query.getProgress() == -1) {
                    btn_cet_query.setProgress(btn_cet_query.getMinProgress());
                } else {
                    // if (CacheUtil.isHistoryItemExist(CetQueryActivity.this,
                    // edt_sno
                    // .getText().toString())) {
                    // Toast.makeText(CetQueryActivity.this, "�Ѵ������,�������~",
                    // Toast.LENGTH_LONG).show();
                    // } else {
                    if (!isSnoValid(edt_cet_no.getText(),
                            edt_cet_name.getText())) {
                        return;
                    }
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    btn_cet_query.getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    final Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            btn_cet_query.setProgress(mProgress);
                            if (mProgress < 95) {
                                mProgress += 5;
                                handler.postDelayed(this, 10);
                            }
                        }
                    });
                    setStatus(false);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", edt_cet_no.getText().toString().trim());
                    params.put("name", edt_cet_name.getText().toString().trim());
                    if (NetUtil.isNetwork(CetQueryActivity.this)) {
                        mHttpUtil.query46(params, new HttpRequestCallback() {

                            @Override
                            public void onSuccess(Object data) {
                                String result = (String) data;
                                Log.d(Config.LOG_TAG, result);
                                String[] results = result.split(",");
                                if (results.length == 7) {
                                    btn_cet_query.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            btn_cet_query
                                                    .setProgress(btn_cet_query
                                                            .getMaxProgress());
                                        }
                                    });
                                    btn_cet_query.postDelayed(new Runnable() {

                                        @Override
                                        public void run() {
                                            btn_cet_query
                                                    .setProgress(btn_cet_query
                                                            .getMinProgress());
                                            setStatus(true);
                                        }
                                    }, 2000);
                                    CetScore cs = new CetScore();
                                    cs.setCet_name(results[6]);
                                    cs.setCet_no(edt_cet_no.getText()
                                            .toString().trim());
                                    cs.setCet_college(results[5]);
                                    cs.setCet_level("cet-"
                                            + (edt_cet_no.getText().toString()
                                            .trim().charAt(9) == '1' ? "4"
                                            : "6"));
                                    cs.setCet_sum_score(results[4]);
                                    cs.setCet_writing_score(results[3]);
                                    cs.setCet_reading_score(results[2]);
                                    cs.setCet_listening_score(results[1]);
                                    Intent intent = new Intent();
                                    intent.putExtra("cet_score", cs);
                                    intent.setClass(CetQueryActivity.this,
                                            CetDescActivity.class);
                                    // CacheUtil.setCache(context, itemList,
                                    // source_file);
                                    startActivity(intent);
                                } else {
                                    btn_cet_query.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            btn_cet_query.setProgress(-1);
                                            ViewUtil.toastText(
                                                    CetQueryActivity.this,
                                                    "�����׼��֤�Ŵ���~", false);
                                        }
                                    });
                                    btn_cet_query.postDelayed(new Runnable() {

                                        @Override
                                        public void run() {
                                            btn_cet_query
                                                    .setProgress(btn_cet_query
                                                            .getMinProgress());
                                            setStatus(true);
                                        }
                                    }, 2000);
                                }

                            }

                            @Override
                            public void onFail(final Object data) {
                                btn_cet_query.post(new Runnable() {

                                    @Override
                                    public void run() {
                                        btn_cet_query.setProgress(-1);
                                        ViewUtil.toastText(
                                                CetQueryActivity.this,
                                                data.toString(), false);
                                    }
                                });
                                btn_cet_query.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        btn_cet_query.setProgress(btn_cet_query
                                                .getMinProgress());
                                        setStatus(true);

                                    }
                                }, 2000);
                            }
                        });
                    } else {
                        Toast.makeText(CetQueryActivity.this, "���粻����~",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
