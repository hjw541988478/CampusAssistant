package cn.edu.university.zfcms.model.data.course;

import android.content.Context;

import java.util.List;

import cn.edu.university.zfcms.base.data.CourseDataSource;
import cn.edu.university.zfcms.model.data.course.remote.BmobCourseDataSource;
import cn.edu.university.zfcms.model.entity.Course;

/**
 * Created by hjw on 16/4/15.
 */
public class CourseDataManager implements CourseDataSource {

    private static CourseDataManager INSTANCE = null;

    private final CourseDataSource localCourseData;
    private final CourseDataSource remoteCourseData;
    private final CourseDataSource bmobRemoteData;

    private CourseDataManager(Context context){
        this.localCourseData = LocalCourseDataSource.getInstance(context);
        this.remoteCourseData = RemoteCourseDataSource.getInstance();
        this.bmobRemoteData = BmobCourseDataSource.getInstance(context);
    }

    public static CourseDataManager getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = new CourseDataManager(context);
        }
        return INSTANCE;
    }

    @Override
    public void loadRawCourses(final LoadCoursesCallback callback) {
        remoteCourseData.loadRawCourses(new LoadCoursesCallback() {
            @Override
            public void onCoursesLoaded(List<Course> remoteRawCourses) {
                // 缓存到服务器和 DB
                if (!remoteRawCourses.isEmpty()) {
                    localCourseData.saveTimetableCourses(remoteRawCourses);

                    localCourseData.loadRawCourses(new LoadCoursesCallback() {
                        @Override
                        public void onCoursesLoaded(List<Course> remoteRawCourses) {
                            bmobRemoteData.saveTimetableCourses(remoteRawCourses);
                            callback.onCoursesLoaded(remoteRawCourses);
                        }

                        @Override
                        public void onDataError() {
                            callback.onDataError();
                        }
                    });
                }
            }

            @Override
            public void onDataError() {
                callback.onDataError();
            }
        });
    }

    @Override
    public void getLocalWeekTimetableCourses(final int currentWeekNo, final LoadCoursesCallback callback) {
        localCourseData.getLocalWeekTimetableCourses(currentWeekNo, new LoadCoursesCallback() {
            @Override
            public void onCoursesLoaded(List<Course> remoteRawCourses) {
                if (remoteRawCourses.isEmpty()) {

                    bmobRemoteData.getLocalWeekTimetableCourses(currentWeekNo, new LoadCoursesCallback() {
                        @Override
                        public void onCoursesLoaded(List<Course> remoteRawCourses) {
                            // 缓存到 DB
                            localCourseData.saveTimetableCourses(remoteRawCourses);
                            callback.onCoursesLoaded(remoteRawCourses);
                        }

                        @Override
                        public void onDataError() {
                            callback.onDataError();
                        }
                    });

                } else {
                    callback.onCoursesLoaded(remoteRawCourses);
                }
            }

            @Override
            public void onDataError() {
                callback.onDataError();
            }
        });
    }

    @Override
    public void getLocalTimetableCourse(final String courseId , final LoadCourseCallback callback) {
        localCourseData.getLocalTimetableCourse(courseId, new LoadCourseCallback() {
            @Override
            public void onCourseLoaded(Course remoteRawCourse) {
                callback.onCourseLoaded(remoteRawCourse);
            }

            @Override
            public void onDataError() {
                bmobRemoteData.getLocalTimetableCourse(courseId, new LoadCourseCallback() {
                    @Override
                    public void onCourseLoaded(Course remoteRawCourse) {
                        callback.onCourseLoaded(remoteRawCourse);
                        localCourseData.saveTimetableCourse(remoteRawCourse);
                    }

                    @Override
                    public void onDataError() {
                        callback.onDataError();
                    }
                });
            }
        });
    }

    @Override
    public void saveTimetableCourses(List<Course> courses) {
        localCourseData.saveTimetableCourses(courses);
        bmobRemoteData.saveTimetableCourses(courses);
    }

    @Override
    public void saveTimetableCourse(Course remoteRawCourse) {
        localCourseData.saveTimetableCourse(remoteRawCourse);
        bmobRemoteData.saveTimetableCourse(remoteRawCourse);
    }

    @Override
    public void updateTimetableCourse(Course remoteRawCourse) {
        localCourseData.updateTimetableCourse(remoteRawCourse);
        bmobRemoteData.updateTimetableCourse(remoteRawCourse);
    }

    @Override
    public void deleteTimetableCourse(String courseId) {
        localCourseData.deleteTimetableCourse(courseId);
    }

    @Override
    public void deleteAllCourses() {
        localCourseData.deleteAllCourses();
    }
}
