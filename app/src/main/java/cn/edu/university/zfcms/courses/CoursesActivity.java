package cn.edu.university.zfcms.courses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import cn.edu.university.zfcms.R;
import cn.edu.university.zfcms.base.BaseToolbarActivity;
import cn.edu.university.zfcms.data.course.CourseDataRepo;

/**
 * Created by hjw on 16/4/16.
 */
public class CoursesActivity extends BaseToolbarActivity{

    private CoursesPresenter coursesPresenter;

    public static void launch(Context context) {
        context.startActivity(new Intent(context,CoursesActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_course;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        CoursesFragment coursesFragment =
                (CoursesFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (coursesFragment == null) {
            coursesFragment = CoursesFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.contentFrame,coursesFragment).commit();
        }

        coursesPresenter = new CoursesPresenter(CourseDataRepo.getInstance(getApplicationContext()),coursesFragment);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }
}
