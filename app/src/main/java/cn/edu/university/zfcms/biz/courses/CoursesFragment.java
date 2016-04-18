package cn.edu.university.zfcms.biz.courses;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.edu.university.zfcms.R;
import cn.edu.university.zfcms.base.BaseFragment;
import cn.edu.university.zfcms.model.Course;

/**
 * Created by hjw on 16/4/15.
 */
public class CoursesFragment extends BaseFragment implements CoursesContract.View {

    private static final String tag = CoursesFragment.class.getSimpleName();

    private CoursesContract.Presenter coursesPresenter;

    @Bind(R.id.course_no_data)
    View courseNoData;
    @Bind(R.id.course_content_scroll_body)
    View courseContentContainer;
    @Bind(R.id.course_content_container)
    ViewGroup courseContent;

    public static CoursesFragment newInstance(){
        return new CoursesFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_courses;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        ButterKnife.bind(this,self);
    }

    @Override
    protected void initListeners() {
        setHasOptionsMenu(true);
    }

    @Override
    protected void initData() {
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

        Log.d(tag,"show courses current week " + coursesPresenter.getCurrentWeekNo() + ":\n" + remoteRawCourses.toString());
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
}
