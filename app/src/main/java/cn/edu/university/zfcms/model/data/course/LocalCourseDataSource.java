package cn.edu.university.zfcms.model.data.course;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteTransactionListener;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.edu.university.zfcms.data.db.CampusAssitDbHelper;
import cn.edu.university.zfcms.model.entity.Course;
import cn.edu.university.zfcms.base.data.CourseDataSource;
import cn.edu.university.zfcms.util.StringUtil;

/**
 * DB 层保存课表信息
 */
public class LocalCourseDataSource implements CourseDataSource {

    private static final String tag = LocalCourseDataSource.class.getSimpleName();

    private static final String[] CourseTimetableProjection = {
            CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_TIME_ID,
            CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_NAME,
            CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WHO_TEACH,
            CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WHERE_TEACH,
            CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WEEKS,
            CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_DAY_OF_WEEK,
            CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_START,
            CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_END
    };

    private static final String WeekCoursesSelection = CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WEEKS + " LIKE ?";
    private static final String SingleCourseSelection = CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_TIME_ID + " LIKE ?";

    private static LocalCourseDataSource INSTANCE;

    private CampusAssitDbHelper mDbHelper;

    private LocalCourseDataSource(@NonNull Context context) {
        mDbHelper = new CampusAssitDbHelper(context);
    }

    public static LocalCourseDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalCourseDataSource(context);
        }
        return INSTANCE;
    }

    // 查询所有的课程
    @Override
    public void loadRawCourses(LoadCoursesCallback callback) {
        List<Course> courses = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // 查询某周课程表信息
        Cursor timeTableCursor = db.query(
                CoursePersistenceContract.CourseTimetableEntry.TABLE_NAME, CourseTimetableProjection
                , null, null, null, null, null);

        if (timeTableCursor != null && timeTableCursor.getCount() > 0) {
            while (timeTableCursor.moveToNext()) {
                String timeTableId = timeTableCursor
                        .getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_TIME_ID));

                String weeksArrayStr = timeTableCursor
                        .getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WEEKS));
                List<String> weeksList = StringUtil.stringToList(weeksArrayStr);

                String dayOfWeek = timeTableCursor
                        .getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_DAY_OF_WEEK));
                int sectionStart = timeTableCursor
                        .getInt(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_START));
                int sectionEnd = timeTableCursor
                        .getInt(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_END));

                String courseName = timeTableCursor.getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_NAME));
                String whoTeach = timeTableCursor.getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WHO_TEACH));
                String whereTeach = timeTableCursor.getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WHERE_TEACH));
                Course course = new Course(timeTableId, sectionStart, sectionEnd, weeksList, dayOfWeek, courseName, whoTeach, whereTeach);
                courses.add(course);
            }
        }
        if (timeTableCursor != null) {
            timeTableCursor.close();
        }
        db.close();
        Log.d(tag, "load all weeks courses :" + courses.size());
        if (callback != null) {
            callback.onCoursesLoaded(courses);
        }
    }

    // 获取某周的课表信息
    @Override
    public void getLocalWeekTimetableCourses(int currentWeekNo, LoadCoursesCallback callback) {
        List<Course> courses = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // 查询某周课程表信息
        Cursor timeTableCursor = db.query(
                CoursePersistenceContract.CourseTimetableEntry.TABLE_NAME, CourseTimetableProjection
                , WeekCoursesSelection, new String[]{String.valueOf(currentWeekNo)}, null, null, null);

        if (timeTableCursor != null && timeTableCursor.getCount() > 0) {
            while (timeTableCursor.moveToNext()) {
                String timeTableId = timeTableCursor
                        .getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_TIME_ID));

                String weeksArrayStr = timeTableCursor
                        .getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WEEKS));
                List<String> weeksList = StringUtil.stringToList(weeksArrayStr);

                String dayOfWeek = timeTableCursor
                        .getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_DAY_OF_WEEK));
                int sectionStart = timeTableCursor
                        .getInt(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_START));
                int sectionEnd = timeTableCursor
                        .getInt(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_END));

                String courseName = timeTableCursor.getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_NAME));
                String whoTeach = timeTableCursor.getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WHO_TEACH));
                String whereTeach = timeTableCursor.getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WHERE_TEACH));
                Course course = new Course(timeTableId, sectionStart, sectionEnd, weeksList, dayOfWeek, courseName, whoTeach, whereTeach);
                courses.add(course);
            }
        }
        if (timeTableCursor != null) {
            timeTableCursor.close();
        }
        db.close();
        Log.d(tag,"load no."+currentWeekNo + "week timetable :" + courses.toString());
        if (callback != null) {
            callback.onCoursesLoaded(courses);
        }
    }

    // 获取单条课表课程信息
    @Override
    public void getLocalTimetableCourse(String courseTimeId,LoadCourseCallback callback) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Course course = null;

        // 查询某周课程表信息
        Cursor timeTableCursor = db.query(
                CoursePersistenceContract.CourseTimetableEntry.TABLE_NAME, CourseTimetableProjection
                , SingleCourseSelection, new String[]{courseTimeId}, null, null, null);

        if (timeTableCursor != null && timeTableCursor.getCount() > 0) {
            timeTableCursor.moveToFirst();
            String timeTableId = timeTableCursor
                    .getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_TIME_ID));

            String weeksArrayStr = timeTableCursor
                    .getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WEEKS));
            List<String> weeksList = StringUtil.stringToList(weeksArrayStr);

            String dayOfWeek = timeTableCursor
                    .getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_DAY_OF_WEEK));
            int sectionStart = timeTableCursor
                    .getInt(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_START));
            int sectionEnd = timeTableCursor
                    .getInt(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_END));

            String courseName = timeTableCursor.getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_NAME));
            String whoTeach = timeTableCursor.getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WHO_TEACH));
            String whereTeach = timeTableCursor.getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WHERE_TEACH));
            course = new Course(timeTableId, sectionStart, sectionEnd, weeksList, dayOfWeek, courseName, whoTeach, whereTeach);
        }

        if (timeTableCursor != null) {
            timeTableCursor.close();
        }

        db.close();

        Log.d(tag,"load coursetimeid "+courseTimeId + "\'s course" + (course != null ? course.toString() : ""));

        if (course != null) {
            callback.onCourseLoaded(course);
        } else {
            callback.onDataError();
        }
    }

    // 批量插入新的课表信息
    @Override
    public void saveTimetableCourses(final List<Course> courses) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.beginTransactionWithListener(new SQLiteTransactionListener() {
            @Override
            public void onBegin() {

            }

            @Override
            public void onCommit() {
                Log.d(tag, "save courses " + courses.size() + "commit successful");
            }

            @Override
            public void onRollback() {
                Log.d(tag, "save courses " + courses.size() + "rollback");
            }
        });
        for (Course course : courses) {
            db.insert(CoursePersistenceContract.CourseTimetableEntry.TABLE_NAME, null , convertTimetableCourseToCv(course));
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    // 转换
    private ContentValues convertTimetableCourseToCv(Course course) {
        ContentValues cv = new ContentValues();
        cv.put(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_TIME_ID, course.getObjectId());
        cv.put(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_DAY_OF_WEEK, course.dayOfWeek);
        cv.put(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_END, course.whichSectionEnd);
        cv.put(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_START, course.whichSectionStart);
        cv.put(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WEEKS, StringUtil.listToString(course.weeks));
        cv.put(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_NAME, course.courseName);
        cv.put(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WHO_TEACH, course.whereTeach);
        cv.put(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WHERE_TEACH, course.whereTeach);
        return cv;
    }

    // 保存新的课表课程信息
    @Override
    public void saveTimetableCourse(Course course) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.insert(CoursePersistenceContract.CourseTimetableEntry.TABLE_NAME, null , convertTimetableCourseToCv(course));
        db.close();
    }

    // 更新某条课表信息
    @Override
    public void updateTimetableCourse(Course course) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selection = CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_TIME_ID + " LIKE ?";
        String[] selectionArgs = {course.getObjectId()};
        db.update(CoursePersistenceContract.CourseTimetableEntry.TABLE_NAME, convertTimetableCourseToCv(course), selection, selectionArgs);
        db.close();
    }

    // 删除某条课表课程信息
    @Override
    public void deleteTimetableCourse(String timeId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selection = CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_TIME_ID + " LIKE ?";
        String[] selectionArgs = { timeId };
        db.delete(CoursePersistenceContract.CourseTimetableEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    // 清空所有数据包括基本信息
    @Override
    public void deleteAllCourses() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(CoursePersistenceContract.CourseTimetableEntry.TABLE_NAME , null , null);
        db.close();
    }

}
