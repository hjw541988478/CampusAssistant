package com.ywxy.ca.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ywxy.ca.R;
import com.ywxy.ca.adapter.GradeDescListAdapter;
import com.ywxy.ca.entity.CollegeSemester;
import com.ywxy.ca.entity.SemesterGrade;
import com.ywxy.ca.entity.StudentGradeInfo;
import com.ywxy.ca.entity.SumGradeInfo;
import com.ywxy.ca.http.HttpUtil;
import com.ywxy.ca.util.Config;

import java.util.ArrayList;
import java.util.List;

public class GradeDescActivity extends Activity implements
        OnClickListener {
    private HttpUtil mHttpUtil;
    private ListView lv_grades_list;
    private TextView lb_grade_avg_hint, tv_grade_avg, lb_point_avg_hint,
            tv_point_avg, tv_select, tv_title, tv_term_avg_grade,
            tv_term_avg_point, tv_no_records;
    private ImageButton btn_Back;
    private LinearLayout ll_sum, ll_sum_desc;
    private StudentGradeInfo currentStudentInfo;
    private SumGradeInfo sumInfo;
    private GradeDescListAdapter adapter;
    private List<CollegeSemester> listSemester;
    private Intent data;
    private String[] keys;
    private String[] terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getIntent();
        getData();
        mHttpUtil = new HttpUtil();
        setContentView(R.layout.activity_gradedesc);
        initViews();
        setListener();
//		SwipeBackLayout mBackLayout = getSwipeBackLayout();
//		mBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        adapter = new GradeDescListAdapter(this);
        updateSumView();
        lv_grades_list.setAdapter(adapter);
    }

    private void updateSemesterView(SemesterGrade grade) {
//		lb_list_title.setText("各科成绩");
        tv_term_avg_grade.setText(grade.getAvgItem().getAvgGrade());
        tv_term_avg_point.setText(grade.getAvgItem().getGradePoint());
        adapter.setGradeList(grade.getAllItem());
        adapter.notifyDataSetChanged();
        adapter.setRed(false);
        ll_sum.setVisibility(View.GONE);
        ll_sum_desc.setVisibility(View.VISIBLE);
    }

    public void updateSumView() {
//		lb_list_title.setText("未通过科目");
        float avg_grade = sumInfo.getAvgGrade();
        float avg_point = sumInfo.getGradePoint();
        tv_grade_avg.setText(avg_grade + "");
        lb_grade_avg_hint.setText(avg_grade < 68 ? R.string.grade_lessthan_68
                : avg_grade < 80 ? R.string.grade_lessthan_80
                : R.string.grade_lessthan_100);
        tv_point_avg.setText(avg_point + "");
        lb_point_avg_hint.setText(avg_point < 1.8 ? R.string.point_lessthan_1_8
                : avg_point < 3.0 ? R.string.point_lessthan_3_0
                : R.string.point_lessthan_5_0);
        Log.d(Config.LOG_TAG, "sum_fail_list:" + sumInfo.getNoPassList());
        adapter.setGradeList(sumInfo.getNoPassList());
        adapter.setRed(true);
        adapter.notifyDataSetChanged();
        ll_sum.setVisibility(View.VISIBLE);
        ll_sum_desc.setVisibility(View.GONE);
    }

    private void initViews() {
        ll_sum = (LinearLayout) findViewById(R.id.ll_sum);
        ll_sum_desc = (LinearLayout) findViewById(R.id.ll_sum_desc);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_select = (TextView) findViewById(R.id.tv_select);
        btn_Back = (ImageButton) findViewById(R.id.btn_back);
        lb_grade_avg_hint = (TextView) findViewById(R.id.lb_grade_avg_hint);
        lb_point_avg_hint = (TextView) findViewById(R.id.lb_point_avg_hint);
        tv_grade_avg = (TextView) findViewById(R.id.tv_grade_avg);
        tv_point_avg = (TextView) findViewById(R.id.tv_point_avg);

        tv_term_avg_grade = (TextView) findViewById(R.id.tv_term_avg_grade);
        tv_term_avg_point = (TextView) findViewById(R.id.tv_term_avg_point);
//		lb_list_title = (TextView) findViewById(R.id.tv_list_title);
        tv_no_records = (TextView) findViewById(R.id.tv_no_grade1);
        lv_grades_list = (ListView) findViewById(R.id.lv_grade_list);
        lv_grades_list.setEmptyView(tv_no_records);
    }

    public void setListener() {
        tv_select.setOnClickListener(this);
        btn_Back.setOnClickListener(this);
    }

    public void setAllTerms() {
        List<String> semsList = new ArrayList<String>();
        List<String> keysList = new ArrayList<String>();
        semsList.add("汇总");
        Log.d(Config.LOG_TAG, "terms.size" + listSemester.size());
        for (CollegeSemester item : listSemester) {
            semsList.add(item.getSchoolYear()
                    + (item.getSemester().equals("1") ? " 上学期" : " 下学期"));
            keysList.add(item.getSchoolYear() + item.getSemester());
        }
        keys = keysList.toArray(new String[keysList.size()]);
        terms = semsList.toArray(new String[semsList.size()]);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.length; i++) {
            sb.append(keys[i]);
        }
        Log.d(Config.LOG_TAG, "keys," + sb.toString());
    }

    private String[] getAllTerms() {
        return terms;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_select:
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        GradeDescActivity.this);
                builder.setItems(getAllTerms(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tv_title.setText(getAllTerms()[which]);
                                if (which == 0) {
                                    // 显示汇总成绩视图
                                    updateSumView();
                                } else {
                                    SemesterGrade currentGrade = currentStudentInfo
                                            .getAllSemMap().get(keys[which - 1]);
                                    updateSemesterView(currentGrade);
                                }
                            }
                        });
                builder.show();
                break;
            default:
                break;
        }
    }

    private void getData() {
        currentStudentInfo = (StudentGradeInfo) data
                .getSerializableExtra(Config.KEY_STUDENT);
        sumInfo = currentStudentInfo.getSumGrade();
        listSemester = currentStudentInfo.getCollegeList();
        setAllTerms();
        Log.d(Config.LOG_TAG, "real_size:"
                + currentStudentInfo.getAllSemMap().size());
        Log.d(Config.LOG_TAG, "CollegeListSize:" + listSemester.size());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHttpUtil = null;
    }
}
