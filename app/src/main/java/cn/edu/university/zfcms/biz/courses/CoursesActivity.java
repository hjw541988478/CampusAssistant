package cn.edu.university.zfcms.biz.courses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;


import butterknife.BindView;
import cn.edu.university.zfcms.R;
import cn.edu.university.zfcms.app.Constant;
import cn.edu.university.zfcms.base.BaseDrawerLayoutActivity;
import cn.edu.university.zfcms.data.course.CourseDataRepo;

/**
 * Created by hjw on 16/4/16.
 */
public class CoursesActivity extends BaseDrawerLayoutActivity {

    private CoursesPresenter coursesPresenter;


    public static final int TYPE_DAY_COURSES = 1;
    public static final int TYPE_WEEK_COURSES = 2;

    public static final String COURSES_TYPE = "courses_type";

    private int currentType;

    public static void launch(Context context, int type) {
        Intent intent = new Intent(context, CoursesActivity.class);
        intent.putExtra(COURSES_TYPE, type);
        context.startActivity(intent);
    }

    @BindView(R.id.tab_layout)
    TabLayout tabs;

    protected TabLayout getTabs() {
        return tabs;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_course;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        Fragment coursesFragment = getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (coursesFragment == null) {
            currentType = getIntent().getIntExtra(COURSES_TYPE, TYPE_DAY_COURSES);
            if (currentType == TYPE_DAY_COURSES) {
                coursesFragment = DayCoursesFragment.newInstance();
            } else if (currentType == TYPE_WEEK_COURSES) {
                coursesFragment = WeekCoursesFragment.newInstance();
            }
            getSupportFragmentManager().beginTransaction().add(R.id.contentFrame,coursesFragment).commit();
        }
        coursesPresenter = new CoursesPresenter(
                CourseDataRepo.getInstance(getApplicationContext()), (CoursesContract.View) coursesFragment);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {
        if (currentType == TYPE_WEEK_COURSES) {

        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_courses, menu);
        return true;
    }

    @Override
    protected NavigationView.OnNavigationItemSelectedListener getNavigationItemSelectedListener() {
        return item -> CoursesActivity.this.menuItemChecked(item.getItemId());
    }

    @Override
    protected int[] getMenuItemIds() {
        return Constant.menuIds;
    }

    @Override
    protected void onMenuItemOnClick(MenuItem now) {

    }

    @Override
    public void onSwipeRefresh() {

    }
}
