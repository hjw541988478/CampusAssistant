package cn.edu.university.zfcms.biz.courses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.Menu;


import butterknife.Bind;
import cn.edu.university.zfcms.R;
import cn.edu.university.zfcms.base.ui.BaseToolbarActivity;
import cn.edu.university.zfcms.biz.courses.adapter.DayCoursesAdapter;
import cn.edu.university.zfcms.data.course.CourseDataRepo;

/**
 * Created by hjw on 16/4/16.
 */
public class CoursesActivity extends BaseToolbarActivity{

    private CoursesPresenter coursesPresenter;

    public static final int TYPE_DAY_COURESE = 1;
    public static final int TYPE_WEEK_COURSES = 2;

    public static final String COURSES_TYPE = "courses_type";

    @Bind(R.id.tab_layout)
    TabLayout tabs;

    public static void launch(Context context, int type) {
        Intent intent = new Intent(context, CoursesActivity.class);
        intent.putExtra(COURSES_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_course;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Fragment coursesFragment = getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (coursesFragment == null) {
            int currentType = getIntent().getIntExtra(COURSES_TYPE, TYPE_DAY_COURESE);
            if (currentType == TYPE_DAY_COURESE) {
                coursesFragment = DayCoursesFragment.newInstance();
            } else if (currentType == TYPE_WEEK_COURSES) {
                coursesFragment = WeekCoursesFragment.newInstance();
            }
            getSupportFragmentManager().beginTransaction().add(R.id.contentFrame,coursesFragment).commit();
        }
        coursesPresenter = new CoursesPresenter(
                CourseDataRepo.getInstance(getApplicationContext()), (CoursesContract.View) coursesFragment);
    }

    protected TabLayout getTabs() {
        return tabs;
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_courses, menu);
        return true;
    }
}
