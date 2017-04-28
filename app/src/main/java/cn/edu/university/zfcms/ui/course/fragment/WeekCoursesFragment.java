package cn.edu.university.zfcms.ui.course.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.university.zfcms.R;
import cn.edu.university.zfcms.base.BaseFragment;
import cn.edu.university.zfcms.base.contract.CourseContract;
import cn.edu.university.zfcms.model.entity.Course;

/**
 * Created by hjw on 16/4/15.
 */
public class WeekCoursesFragment extends BaseFragment implements CourseContract.View {

    private static final String tag = WeekCoursesFragment.class.getSimpleName();

    private CourseContract.Presenter coursesPresenter;

    @BindView(R.id.course_no_data)
    View courseNoData;
    @BindView(R.id.course_content_scroll_body)
    View courseContentContainer;
    @BindView(R.id.course_content_container)
    ViewGroup courseContent;

    public static WeekCoursesFragment newInstance() {
        return new WeekCoursesFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_week_courses;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        ButterKnife.bind(this, self);
    }

    @Override
    protected void initListeners() {
        setHasOptionsMenu(true);
    }

    @Override
    protected void initNoLazyData() {
        coursesPresenter.start();
    }


    @Override
    public void showNoCourses() {
        courseNoData.setVisibility(View.VISIBLE);
        courseContentContainer.setVisibility(View.GONE);
    }

    @Override
    public void showCourses(List<Course> remoteRawCourses) {
        courseNoData.setVisibility(View.GONE);
        courseContentContainer.setVisibility(View.VISIBLE);

        Log.d(tag, "show courses current week " + coursesPresenter.getCurrentWeekNo() + ":\n" + remoteRawCourses.toString());
    }

    @Override
    public void showCoursesLoadError() {
        showToast("load courses error.");
    }

    @Override
    public void showWeekFilterMenu() {

    }

    @Override
    public void setPresenter(CourseContract.Presenter presenter) {
        this.coursesPresenter = presenter;
    }

    @Override
    public Context getRxLifeActivity() {
        return getActivity();
    }
}