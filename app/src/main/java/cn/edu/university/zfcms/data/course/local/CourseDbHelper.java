
package cn.edu.university.zfcms.data.course.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CourseDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "XmutAssit.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_COURSES =
            "CREATE TABLE " + CoursePersistenceContract.CourseEntry.TABLE_NAME + " (" +
                    CoursePersistenceContract.CourseEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    CoursePersistenceContract.CourseEntry.COLUMN_NAME_COURSE_ID + TEXT_TYPE + COMMA_SEP +
                    CoursePersistenceContract.CourseEntry.COLUMN_NAME_COURSE_NAME + TEXT_TYPE + COMMA_SEP +
                    CoursePersistenceContract.CourseEntry.COLUMN_NAME_WHO_TEACH + TEXT_TYPE + COMMA_SEP +
                    CoursePersistenceContract.CourseEntry.COLUMN_NAME_WHERE_TEACH + TEXT_TYPE +
            " )";

    private static final String SQL_CREATE_COURSE_TIMETABLE =
            "CREATE TABLE " + CoursePersistenceContract.CourseTimetableEntry.TABLE_NAME + " (" +
                    CoursePersistenceContract.CourseTimetableEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_TIME_ID + TEXT_TYPE + COMMA_SEP +
                    CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_COURSE_ID + TEXT_TYPE + COMMA_SEP +
                    CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_DAY_OF_WEEK + TEXT_TYPE + COMMA_SEP +
                    CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WEEK_NO + INTEGER_TYPE + COMMA_SEP +
                    CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_START + INTEGER_TYPE + COMMA_SEP +
                    CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_DURATION + INTEGER_TYPE +
            " )";

    public CourseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_COURSES);
        db.execSQL(SQL_CREATE_COURSE_TIMETABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}
