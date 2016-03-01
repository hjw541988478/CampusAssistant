package com.ywxy.ca.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.widgets.Dialog;
import com.ywxy.ca.R;
import com.ywxy.ca.adapter.HistoryGridAdapter;
import com.ywxy.ca.adapter.HistoryGridAdapter.HistoryItemHolder;
import com.ywxy.ca.entity.StudentGradeInfo;
import com.ywxy.ca.util.CacheUtil;
import com.ywxy.ca.util.Config;
import com.ywxy.ca.util.ViewUtil;

import java.util.Iterator;
import java.util.List;

public class HistoryActivity extends Activity {

    private int checkNum = 0;
    private ImageButton btn_back, btn_add, btn_del;
    private TextView tv_no_data;
    private GridView gv_grade;
    private boolean isShowingCbx;
    private HistoryGridAdapter adapter = null;
    private List<StudentGradeInfo> mDatalist = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initViews();
        setListener();
        mDatalist = CacheUtil.getCache(this, StudentGradeInfo.class,
                Config.CACHE_FILE);
        adapter.setDataList(mDatalist);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        if (arg0 == Config.REQUEST_ADD_CODE
                && arg1 == Config.RESPONSE_SUC_CODE) {
            StudentGradeInfo info = (StudentGradeInfo) arg2
                    .getSerializableExtra(Config.KEY_GET_NEW_STUDENT);
            List<StudentGradeInfo> list = adapter.getDataList();
            list.add(info);
            adapter.notifyDataSetChanged();
            CacheUtil.setCache(this, list, Config.CACHE_FILE);
        } else if (arg0 == Config.REQUEST_ADD_CODE
                && arg1 == Config.RESPONSE_FAIL_CODE) {
            Log.d(Config.LOG_TAG, "fail to get new student");
        }
    }

    @Override
    public void onBackPressed() {
        if (isShowingCbx) {
            unshowDeleteView();
        } else {
            doBackToHome();
        }
    }

    private void doBackToHome() {
        setResult(Config.RESPONSE_FAIL_CODE);
        finish();
    }

    public void setListener() {
        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (isShowingCbx) {
                    unshowDeleteView();
                } else {
                    doBackToHome();
                }
            }
        });
        btn_del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(HistoryActivity.this, "提示",
                        "确认删除?");
                dialog.addCancelButton("取消", new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        unshowDeleteView();
                        dialog.dismiss();
                    }
                });
                dialog.show();
                dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        if (checkNum == 0) {
                            Toast.makeText(HistoryActivity.this, "未选中任何记录~", 0)
                                    .show();
                        } else {
                            Iterator<StudentGradeInfo> iterator = adapter
                                    .getDataList().iterator();
                            while (iterator.hasNext()) {
                                StudentGradeInfo temp = iterator.next();
                                if (temp.isFlag()) {
                                    iterator.remove();
                                }
                            }
                            CacheUtil.setCache(HistoryActivity.this,
                                    adapter.getDataList(), Config.CACHE_FILE);
                            adapter.notifyDataSetChanged();
                            // checkNum = 0;
                            unshowDeleteView();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(HistoryActivity.this,
                        SignInActivity.class);
                startActivityForResult(intent, Config.REQUEST_ADD_CODE);
            }
        });
        gv_grade.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (!isShowingCbx) {
                    if (ViewUtil.isFastDoubleClick()) {
                        return;
                    }
                    StudentGradeInfo item = (StudentGradeInfo) adapter
                            .getItem(position);
                    Intent intent = new Intent(HistoryActivity.this,
                            GradeDescActivity.class);
                    Log.d(Config.LOG_TAG, "item,onclick" + item.toString());
                    intent.putExtra(Config.KEY_STUDENT, item);
                    startActivity(intent);
                } else {
                    HistoryItemHolder holder = (HistoryItemHolder) arg1
                            .getTag();
                    holder.scbx.toggle();
                    if (holder.scbx.isChecked()) {
                        adapter.getDataList().get(position).setFlag(true);
                        holder.ll_root.setBackgroundColor(getResources()
                                .getColor(R.color.gray_bg));
                        holder.rl_sno_bg.setBackgroundColor(getResources()
                                .getColor(R.color.bg_blue_pressed));
                        checkNum++;
                    } else {
                        adapter.getDataList().get(position).setFlag(false);
                        holder.ll_root.setBackgroundColor(getResources()
                                .getColor(R.color.white));
                        holder.rl_sno_bg.setBackgroundColor(getResources()
                                .getColor(R.color.bg_blue));
                        checkNum--;
                    }
                    Log.d(Config.LOG_TAG, "checkNum:" + checkNum);
                }
            }

        });
        gv_grade.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                showDeleteView();
                return true;
            }
        });
    }

    public void showDeleteView() {
        btn_del.setVisibility(View.VISIBLE);
        isShowingCbx = true;
    }

    public void unshowDeleteView() {
        List<StudentGradeInfo> list = adapter.getDataList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isFlag())
                list.get(i).setFlag(false);
        }
        adapter.notifyDataSetChanged();
        btn_del.setVisibility(View.GONE);
        isShowingCbx = false;
    }

    private void initViews() {
        tv_no_data = (TextView) findViewById(R.id.tv_no_grade);
        gv_grade = (GridView) findViewById(R.id.gv_grade);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_add = (ImageButton) findViewById(R.id.btn_add);
        btn_del = (ImageButton) findViewById(R.id.btn_del);
        gv_grade.setEmptyView(tv_no_data);
        adapter = new HistoryGridAdapter(this);
        gv_grade.setAdapter(adapter);
        // SwipeBackLayout layout = getSwipeBackLayout();
        // layout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
    }
}
