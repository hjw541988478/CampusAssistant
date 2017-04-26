package cn.edu.university.zfcms.data.course;

import java.util.List;

import cn.edu.university.zfcms.storage.entity.Course;

/**
 * Created by hjw on 16/4/15.
 */
public interface CourseDataSource {
    interface LoadCoursesCallback {
        void onCoursesLoaded(List<Course> remoteRawCourses);
        void onDataError();
    }
    interface LoadCourseCallback {
        void onCourseLoaded(Course remoteRawCourse);
        void onDataError();
    }

    void loadRawCourses(LoadCoursesCallback callback);
    void getLocalWeekTimetableCourses(int currentWeekNo , LoadCoursesCallback callback);
    void getLocalTimetableCourse(String timeCourseId , LoadCourseCallback callback);

    void saveTimetableCourses(List<Course> courses);
    void saveTimetableCourse(Course course);
    void updateTimetableCourse(Course course);

    void deleteTimetableCourse(String courseId);
    void deleteAllCourses();
}
