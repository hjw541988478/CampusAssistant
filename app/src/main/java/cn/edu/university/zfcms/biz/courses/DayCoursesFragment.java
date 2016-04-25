package cn.edu.university.zfcms.biz.courses;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

import butterknife.Bind;
import cn.edu.university.zfcms.R;
import cn.edu.university.zfcms.base.ui.BaseFragment;
import cn.edu.university.zfcms.biz.courses.adapter.DayCoursesAdapter;
import cn.edu.university.zfcms.model.Course;

/**
 * Created by hjw on 16/4/15.
 */
public class DayCoursesFragment extends BaseFragment implements CoursesContract.View {

    private static final String tag = DayCoursesFragment.class.getSimpleName();

    private CoursesContract.Presenter coursesPresenter;
    private DayCoursesAdapter adapter;

    @Bind(R.id.day_courses_pager)
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
    protected void initNoLazyData() {
        adapter = new DayCoursesAdapter(getLongLifeCycleContext(), getFragmentManager());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(adapter.getCount());
        if (((CoursesActivity) getActivity()).getTabs() != null) {
            ((CoursesActivity) getActivity()).getTabs().setupWithViewPager(pager);
        }
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

    }

    @Override
    public void setPresenter(CoursesContract.Presenter presenter) {
        this.coursesPresenter = presenter;
    }

    @Override
    public Context getLongLifeCycleContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }
}
