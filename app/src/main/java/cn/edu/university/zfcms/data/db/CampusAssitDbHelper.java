
package cn.edu.university.zfcms.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.edu.university.zfcms.data.course.local.CoursePersistenceContract;

public class CampusAssitDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "ZfsoftCampusAssit.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_COURSE_TIMETABLE =
            "CREATE TABLE " + CoursePersistenceContract.CourseTimetableEntry.TABLE_NAME + " (" +
                    CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_TIME_ID + TEXT_TYPE + " PRIMARY KEY ," +
                    CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WHERE_TEACH + TEXT_TYPE + COMMA_SEP +
                    CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WHO_TEACH + TEXT_TYPE + COMMA_SEP +
                    CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_DAY_OF_WEEK + TEXT_TYPE + COMMA_SEP +
                    CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_WEEKS + TEXT_TYPE + COMMA_SEP +
                    CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_START + INTEGER_TYPE + COMMA_SEP +
                    CoursePersistenceContract.CourseTimetableEntry.COLUMN_NAME_SECTION_END + INTEGER_TYPE +
            " )";

    public CampusAssitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_COURSE_TIMETABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}
