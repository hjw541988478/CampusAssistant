package cn.edu.university.zfcms.biz.courses;

import java.util.List;

import cn.edu.university.zfcms.base.mvp.BasePresenter;
import cn.edu.university.zfcms.base.mvp.BaseView;
import cn.edu.university.zfcms.model.Course;

/**
 * Created by hjw on 16/4/15.
 */
public interface CoursesContract {

    interface View extends BaseView<Presenter> {
        void showNoCourses();
        void showCourses(List<Course> remoteRawCourses);
        void showCoursesLoadError();

        void showWeekFilterMenu();
    }

    interface DayView extends View {
        void showCourse(Course course);
    }

    interface Presenter extends BasePresenter {
        int getCurrentWeekNo();
        void setCurrentWeekNo(int weekNo);
        void loadCurrentWeekCourses(boolean forceUpdate);
    }

    interface Parser {
        boolean isPersonalCoursesPage(String rawHtml);
        List<Course> parseCourses(String rawCoursesHtml);
    }
}
