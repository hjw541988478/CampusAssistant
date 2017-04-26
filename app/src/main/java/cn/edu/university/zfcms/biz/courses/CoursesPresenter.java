package cn.edu.university.zfcms.biz.courses;

import android.os.Handler;
import android.os.Looper;

import java.util.List;

import cn.edu.university.zfcms.data.course.CourseDataRepo;
import cn.edu.university.zfcms.data.course.CourseDataSource;
import cn.edu.university.zfcms.model.Course;

/**
 * Created by hjw on 16/4/15.
 */
public class CoursesPresenter implements CoursesContract.Presenter {

    private final CoursesContract.View mTimetableView;
    private final CourseDataRepo mCourseDataRepo;
    Handler uiHandler = new Handler(Looper.getMainLooper());

    private int currentWeekNo = 1;
//    private SparseArray<List<RemoteRawCourse>> weekCoursesCache = new SparseArray<>();

    public CoursesPresenter(CourseDataRepo mCourseDataRepo,CoursesContract.View courseView) {
        this.mCourseDataRepo = mCourseDataRepo;
        this.mTimetableView = courseView;

        this.mTimetableView.setPresenter(this);
    }

    @Override
    public void start() {
        loadCurrentWeekCourses(true);
    }

    @Override
    public int getCurrentWeekNo() {
        return currentWeekNo;
    }

    @Override
    public void setCurrentWeekNo(int weekNo) {
        this.currentWeekNo = weekNo;
    }

    @Override
    public void loadCurrentWeekCourses(boolean forceUpdate) {
        if (forceUpdate) {
            mCourseDataRepo.loadRawCourses(new CourseDataSource.LoadCoursesCallback() {
                @Override
                public void onCoursesLoaded(final List<Course> remoteRawCourses) {
                    cleanCache();
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTimetableView.showCourses(remoteRawCourses);
                        }
                    });
//                    loadLocalCurrentWeekCourses();
                }

                @Override
                public void onDataError() {
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTimetableView.showCoursesLoadError();
                        }
                    });
                }
            });

        } else {
//            if (weekCoursesCache.get(currentWeekNo).isEmpty()) {
                loadLocalCurrentWeekCourses();
//            } else {
//                mTimetableView.showCourses(weekCoursesCache.get(currentWeekNo));
//            }
        }

    }

    private void loadLocalCurrentWeekCourses() {
        mCourseDataRepo.getLocalWeekTimetableCourses(currentWeekNo, new CourseDataSource.LoadCoursesCallback() {
            @Override
            public void onCoursesLoaded(List<Course> remoteRawCourses) {
                if (remoteRawCourses.isEmpty()) {
                    mTimetableView.showNoCourses();
                } else {
//                    weekCoursesCache.put(currentWeekNo, remoteRawCourses);
                    mTimetableView.showCourses(remoteRawCourses);
                }
            }

            @Override
            public void onDataError() {
                mTimetableView.showCoursesLoadError();
            }
        });
    }
    private void cleanCache(){
        this.currentWeekNo = 1;
//        this.weekCoursesCache.clear();
    }
}
