package cn.edu.university.zfcms.data.course.remote;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.edu.university.zfcms.data.course.CourseDataSource;
import cn.edu.university.zfcms.data.course.local.CoursePersistenceContract;
import cn.edu.university.zfcms.data.model.Course;

/**
 * Created by hjw on 16/4/18.
 */
public class BmobCourseDataSource implements CourseDataSource{
    private static BmobCourseDataSource ourInstance = null;
    private static final String tag = BmobCourseDataSource.class.getSimpleName();

    private Context mContext = null;

    public static BmobCourseDataSource getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new BmobCourseDataSource(context);
        }
        return ourInstance;
    }

    private BmobCourseDataSource(Context context) {
        this.mContext = context;
    }


    @Override
    public void loadRawCourses(LoadCoursesCallback callback) {
        // Remote 去做
    }

    @Override
    public void getLocalWeekTimetableCourses(int currentWeekNo, final LoadCoursesCallback callback) {
        BmobQuery<Course> weekCourseQuery = new BmobQuery<>();
        weekCourseQuery.addWhereContains(
                CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WEEK_NO,String.valueOf(currentWeekNo));
        weekCourseQuery.findObjects(mContext, new FindListener<Course>() {
            @Override
            public void onSuccess(List<Course> list) {
                callback.onCoursesLoaded(list);
            }

            @Override
            public void onError(int i, String s) {
                callback.onDataError();
            }
        });
    }

    @Override
    public void getLocalTimetableCourse(String timeCourseId, final LoadCourseCallback callback) {
        BmobQuery<Course> singleCourseQuery = new BmobQuery<>();
        singleCourseQuery.addWhereContains(
                CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_TIME_ID,timeCourseId);
        singleCourseQuery.getObject(mContext, timeCourseId, new GetListener<Course>() {
            @Override
            public void onSuccess(Course course) {
                callback.onCourseLoaded(course);
            }

            @Override
            public void onFailure(int i, String s) {
                callback.onDataError();
            }
        });
    }

    @Override
    public void saveTimetableCourses(List<Course> courses) {
        if (!courses.isEmpty() && mContext != null) {
            new BmobObject().insertBatch(mContext, new ArrayList<BmobObject>(courses), new SaveListener() {
                @Override
                public void onSuccess() {
                    Log.d(tag,"bmob batch timetable courses saved successful.");
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.e(tag,"bmob batch timetable courses failure.");
                }
            });
        } else {
            Log.e(tag,"bmob batch insert cannot null");
        }
    }

    @Override
    public void saveTimetableCourse(Course course) {
        if (course != null && mContext != null) {
            course.save(mContext, new SaveListener() {
                @Override
                public void onSuccess() {
                    Log.d(tag,"bmob timetable course saved successful.");
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.e(tag,"bmob timetable course saved failure.");
                }
            });
        } else {
            Log.e(tag,"bmob insert cannot null");
        }
    }

    @Override
    public void updateTimetableCourse(Course course) {
        if (course != null && mContext != null) {
            course.update(mContext, course.getObjectId() , new UpdateListener() {
                @Override
                public void onSuccess() {
                    Log.d(tag,"bmob timetable course updated successful.");
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.e(tag,"bmob timetable course updated failure.");
                }
            });
        } else {
            Log.e(tag,"bmob update cannot null");
        }
    }

    @Override
    public void deleteTimetableCourse(String courseId) {
        //never delete
    }

    @Override
    public void deleteAllCourses() {
        //never delete
    }
}
