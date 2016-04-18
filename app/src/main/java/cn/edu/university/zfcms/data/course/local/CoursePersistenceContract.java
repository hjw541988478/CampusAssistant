package cn.edu.university.zfcms.data.course.local;

import android.provider.BaseColumns;

public final class CoursePersistenceContract {

    public CoursePersistenceContract() {}

    public static abstract class CourseEntry implements BaseColumns {
        public static final String TABLE_NAME = "coursebasic";
        public static final String COLUMN_NAME_COURSE_ID = "courseid";
        public static final String COLUMN_NAME_COURSE_NAME = "coursename";
        public static final String COLUMN_NAME_WHO_TEACH = "whototeach";
        public static final String COLUMN_NAME_WHERE_TEACH = "wheretoteach";
    }

    public static abstract class CourseTimetableEntry implements BaseColumns {
        public static final String TABLE_NAME = "coursetimetable";
        public static final String COLUMN_NAME_TIME_ID = "coursetimeid";
        public static final String COLUMN_NAME_COURSE_ID = CourseEntry.COLUMN_NAME_COURSE_ID;
        public static final String COLUMN_NAME_DAY_OF_WEEK = "whichdayofweek";
        public static final String COLUMN_NAME_WEEK_NO = "whichweek";
        public static final String COLUMN_NAME_SECTION_START = "whichsectionstart";
        public static final String COLUMN_NAME_SECTION_DURATION = "sectionduration";
    }
}
