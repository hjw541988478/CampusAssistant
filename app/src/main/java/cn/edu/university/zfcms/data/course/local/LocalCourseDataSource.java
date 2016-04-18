package cn.edu.university.zfcms.data.course.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteTransactionListener;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.edu.university.zfcms.model.Course;
import cn.edu.university.zfcms.model.BasicCourse;
import cn.edu.university.zfcms.data.course.CourseDataSource;

/**
 * DB 层保存课表信息
 */
public class LocalCourseDataSource implements CourseDataSource {

    private static final String tag = LocalCourseDataSource.class.getSimpleName();

    private static final String[] CourseProjection = {
            CoursePersistenceContract.CourseEntry.COLUMN_NAME_COURSE_ID,
            CoursePersistenceContract.CourseEntry.COLUMN_NAME_COURSE_NAME,
            CoursePersistenceContract.CourseEntry.COLUMN_NAME_WHO_TEACH,
            CoursePersistenceContract.CourseEntry.COLUMN_NAME_WHERE_TEACH
    };

    private static final String[] CourseTimetableProjection = {
            CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_TIME_ID,
            CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_COURSE_ID,
            CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WEEK_NO,
            CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_DAY_OF_WEEK,
            CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_START,
            CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_DURATION
    };

    private static final String CourseLocalWeekTimetableSelection = CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WEEK_NO + " LIKE ?";
    private static final String CourseLocalCourseSelection = CoursePersistenceContract.CourseEntry.COLUMN_NAME_COURSE_ID + " LIKE ?";
    private static final String CourseLocalSingleTimetableSelection = CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_TIME_ID + " LIKE ?";

    private static LocalCourseDataSource INSTANCE;

    private CourseDbHelper mDbHelper;

    private LocalCourseDataSource(@NonNull Context context) {
        mDbHelper = new CourseDbHelper(context);
    }

    public static LocalCourseDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocalCourseDataSource(context);
        }
        return INSTANCE;
    }

    // 交由网络去查
    @Override
    public void loadRawCourses(LoadCoursesCallback callback) {

    }

    // 获取某周的课表信息
    @Override
    public void getLocalWeekTimetableCourses(int currentWeekNo, LoadCoursesCallback callback) {
        List<Course> courses = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // 查询某周课程表信息
        Cursor timeTableCursor = db.query(
                CoursePersistenceContract.CourseTimetableEntry.TABLE_NAME, CourseTimetableProjection
                , CourseLocalWeekTimetableSelection,new String[]{String.valueOf(currentWeekNo)},null,null,null);

        if (timeTableCursor != null && timeTableCursor.getCount() > 0) {
            while (timeTableCursor.moveToNext()) {
                String timeTableId = timeTableCursor
                        .getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_TIME_ID));
                String courseId = timeTableCursor
                        .getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_COURSE_ID));
                int weekNo = timeTableCursor
                        .getInt(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WEEK_NO));
                String dayOfWeek = timeTableCursor
                        .getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_DAY_OF_WEEK));
                int sectionStart = timeTableCursor
                        .getInt(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_START));
                int sectionDuration = timeTableCursor
                        .getInt(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_DURATION));
                BasicCourse basicCourse = getBasicCourseByTimeIdInProc(db,courseId);
                if ( basicCourse.isEmpty() ) {
                    basicCourse.courseName = "";
                    basicCourse.whoTeach = "";
                    basicCourse.whereTeach = "";
                }
                Course course = new Course(timeTableId,basicCourse,weekNo,sectionStart,sectionDuration,dayOfWeek);
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
                , CourseLocalSingleTimetableSelection,new String[]{courseTimeId},null,null,null);

        if (timeTableCursor != null && timeTableCursor.getCount() > 0) {
            timeTableCursor.moveToFirst();
            String timeTableId = timeTableCursor
                    .getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_TIME_ID));
            String courseId = timeTableCursor
                    .getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_COURSE_ID));
            int weekNo = timeTableCursor
                    .getInt(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WEEK_NO));
            String dayOfWeek = timeTableCursor
                    .getString(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_DAY_OF_WEEK));
            int sectionStart = timeTableCursor
                    .getInt(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_START));
            int sectionDuration = timeTableCursor
                    .getInt(timeTableCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_DURATION));

            BasicCourse basicCourse = getBasicCourseByTimeIdInProc(db,courseId);
            if ( basicCourse.isEmpty() ) {
                basicCourse.courseName = "";
                basicCourse.whoTeach = "";
                basicCourse.whereTeach = "";
            }
            course = new Course(timeTableId,basicCourse,weekNo,sectionStart,sectionDuration,dayOfWeek);
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
                Log.d(tag,"save courses " + courses.toString() + "successful");
            }

            @Override
            public void onRollback() {
                Log.d(tag,"save courses " + courses.toString() + "rollback");
            }
        });
        for (Course course : courses) {
            if (!isBasicCourseExistInProc(db,course.courseBasicInfo.courseId)) {
                saveBasicCourseInProc(db,course.courseBasicInfo);
            }
            db.insert(CoursePersistenceContract.CourseTimetableEntry.TABLE_NAME, null , convertTimetableCourseToCv(course));
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    // 转换
    private ContentValues convertTimetableCourseToCv(Course course) {
        ContentValues cv = new ContentValues();
        cv.put(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_TIME_ID, course.timeCourseId);
        cv.put(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_COURSE_ID, course.courseBasicInfo.courseId);
        cv.put(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_DAY_OF_WEEK , course.courseDayOfWeek);
        cv.put(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_DURATION , course.courseSectionsDuration);
        cv.put(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_START, course.courseWhichSectionStart);
        cv.put(CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WEEK_NO , course.courseWeekNo);
        return cv;
    }

    // 转换
    private ContentValues convertBasicCourseToCv(BasicCourse course) {
        ContentValues cv = new ContentValues();
        cv.put(CoursePersistenceContract.CourseEntry.COLUMN_NAME_COURSE_ID,course.courseId);
        cv.put(CoursePersistenceContract.CourseEntry.COLUMN_NAME_COURSE_NAME,course.courseName);
        cv.put(CoursePersistenceContract.CourseEntry.COLUMN_NAME_WHO_TEACH , course.whoTeach);
        cv.put(CoursePersistenceContract.CourseEntry.COLUMN_NAME_WHERE_TEACH , course.whereTeach);
        return cv;
    }

    // 获取单条课表基本信息
    private BasicCourse getBasicCourseByTimeIdInProc(SQLiteDatabase db , String courseId) {
        BasicCourse basicCourse = new BasicCourse();
        Cursor courseCursor = db.query(
                CoursePersistenceContract.CourseEntry.TABLE_NAME, CourseProjection
                , CourseLocalCourseSelection, new String[]{ courseId }, null, null, null);
        if (courseCursor != null && courseCursor.getCount() > 0 ) {
            courseCursor.moveToFirst();
            basicCourse.courseName = courseCursor.getString(courseCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseEntry.COLUMN_NAME_COURSE_NAME));
            basicCourse.whoTeach = courseCursor.getString(courseCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseEntry.COLUMN_NAME_WHO_TEACH));
            basicCourse.whereTeach = courseCursor.getString(courseCursor.getColumnIndexOrThrow(CoursePersistenceContract.CourseEntry.COLUMN_NAME_WHERE_TEACH));
        }
        if (courseCursor != null) {
            courseCursor.close();
        }
        return basicCourse;
    }

    // 课表基本信息是否存在
    private boolean isBasicCourseExistInProc(SQLiteDatabase db, String courseId){
        return !getBasicCourseByTimeIdInProc(db,courseId).isEmpty();
    }

    // 插入新的课表基本信息
    private void saveBasicCourseInProc(SQLiteDatabase db, BasicCourse course) {
        ContentValues values = convertBasicCourseToCv(course);
        db.insert(CoursePersistenceContract.CourseEntry.TABLE_NAME,null,values);
    }

    // 更新课表基本信息
    private void updateBasicCourseInProc(SQLiteDatabase db ,BasicCourse course) {
        String courseSelection = CoursePersistenceContract.CourseEntry.COLUMN_NAME_COURSE_ID + " LIKE ?";
        String[] courseSelectionArgs = { course.courseId };
        db.update(CoursePersistenceContract.CourseEntry.TABLE_NAME,null,courseSelection,courseSelectionArgs);
    }

    // 保存新的课表课程信息
    @Override
    public void saveTimetableCourse(Course course) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        updateOrInsertBasicCourseInProc(db,course.courseBasicInfo);

        db.insert(CoursePersistenceContract.CourseTimetableEntry.TABLE_NAME, null , convertTimetableCourseToCv(course));
        db.close();
    }

    // 存在更新不存在插入
    private void updateOrInsertBasicCourseInProc(SQLiteDatabase db , BasicCourse course){
        if (!isBasicCourseExistInProc(db,course.courseId)) {
            saveBasicCourseInProc(db,course);
        } else {
            updateBasicCourseInProc(db,course);
        }
    }

    // 更新某条课表信息
    @Override
    public void updateTimetableCourse(Course course) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        updateOrInsertBasicCourseInProc(db,course.courseBasicInfo);

        String selection = CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_TIME_ID + " LIKE ?";
        String[] selectionArgs = { course.timeCourseId };
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
        db.delete(CoursePersistenceContract.CourseEntry.TABLE_NAME , null , null);
        db.close();
    }

}
