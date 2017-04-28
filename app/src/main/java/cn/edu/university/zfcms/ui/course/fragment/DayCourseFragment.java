package cn.edu.university.zfcms.ui.course.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import cn.edu.university.zfcms.R;
import cn.edu.university.zfcms.base.BaseFragment;
import cn.edu.university.zfcms.base.contract.CourseContract;
import cn.edu.university.zfcms.model.entity.Course;

/**
 * Created by hjw on 16/4/15.
 */
public class DayCourseFragment extends BaseFragment implements CourseContract.DayView {

    private static final String tag = DayCourseFragment.class.getSimpleName();

    private CourseContract.Presenter coursesPresenter;

    @BindView(R.id.day_course_view)
    CardView dayCourseView;

    public static DayCourseFragment newInstance() {
        return new DayCourseFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_day_course;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        super.initViews(self, savedInstanceState);
        dayCourseView.setVisibility(View.GONE);
    }

    @Override
    protected void initListeners() {
        setHasOptionsMenu(true);
    }

    @Override
    protected void initLazyData() {
        super.initLazyData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dayCourseView.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }

    @Override
    public void showNoCourses() {
//        courseNoData.setVisibility(View.VISIBLE);
//        courseContentContainer.setVisibility(View.GONE);
    }

    @Override
    public void showCourses(List<Course> remoteRawCourses) {
//        courseNoData.setVisibility(View.GONE);
//        courseContentContainer.setVisibility(View.VISIBLE);

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

    @Override
    public void showCourse(Course course) {

    }
}
