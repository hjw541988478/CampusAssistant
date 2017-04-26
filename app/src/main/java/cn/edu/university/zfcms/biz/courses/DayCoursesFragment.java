package cn.edu.university.zfcms.biz.courses;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import cn.edu.university.zfcms.R;
import cn.edu.university.zfcms.app.ZfsoftCampusAsstApp;
import cn.edu.university.zfcms.base.BaseFragment;
import cn.edu.university.zfcms.biz.courses.adapter.DayCoursesAdapter;
import cn.edu.university.zfcms.storage.entity.Course;

/**
 * Created by hjw on 16/4/15.
 */
public class DayCoursesFragment extends BaseFragment implements CoursesContract.View {

    private static final String TAG = "DayCoursesFragment";

    private CoursesContract.Presenter coursesPresenter;
    private DayCoursesAdapter adapter;

    @BindView(R.id.day_courses_pager)
    ViewPager pager;

    public static DayCoursesFragment newInstance() {
        return new DayCoursesFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_day_courses;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        super.initViews(self, savedInstanceState);
    }

    @Override
    protected void initListeners() {
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_course_week_change:
                showWeekFilterMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initNoLazyData() {
        adapter = new DayCoursesAdapter(ZfsoftCampusAsstApp.getInstance(), getFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(adapter.getCount());
        ((CoursesActivity) getActivity()).getTabs().setupWithViewPager(pager);
    }


    @Override
    public void showNoCourses() {
    }

    @Override
    public void showCourses(List<Course> remoteRawCourses) {

//        Log.d(tag,"show courses current week " + coursesPresenter.getCurrentWeekNo() + ":\n" + remoteRawCourses.toString());
    }

    @Override
    public void showCoursesLoadError() {
        showToast("load courses error.");
    }

    @Override
    public void showWeekFilterMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_course_week_change));
        popup.getMenu().add("第1周");
        popup.getMenu().add("第2周");
        popup.getMenu().add("第3周");
        popup.getMenu().add("第4周");
        popup.getMenu().add("第5周");
        popup.getMenu().add("第6周");
        popup.getMenu().add("第7周");
        popup.getMenu().add("第8周");
        popup.getMenu().add("第9周");
        popup.getMenu().add("第10周");
        popup.getMenu().add("第11周");
        popup.getMenu().add("第12周");
        popup.getMenu().add("第13周");
        popup.getMenu().add("第14周");
        popup.getMenu().add("第15周");
        popup.setOnMenuItemClickListener(item -> {
            showToast(item.getTitle().toString());
            coursesPresenter.setCurrentWeekNo(1);
            return true;
        });
        popup.show();
    }

    @Override
    public void setPresenter(CoursesContract.Presenter presenter) {
        this.coursesPresenter = presenter;
    }

    @Override
    public Context getRxLifeActivity() {
        return getActivity();
    }
}
