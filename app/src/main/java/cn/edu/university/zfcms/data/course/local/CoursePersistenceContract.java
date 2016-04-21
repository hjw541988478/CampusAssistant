package cn.edu.university.zfcms.data.course.local;

public final class CoursePersistenceContract {

    public CoursePersistenceContract() {}

    public static abstract class CourseTimetableEntry {
        public static final String TABLE_NAME = "timetable";
        public static final String COLUMN_NAME_TIME_ID = "timeid";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_WHO_TEACH = "whoteach";
        public static final String COLUMN_NAME_WHERE_TEACH = "whereteach";
        public static final String COLUMN_NAME_DAY_OF_WEEK = "whichdayofweek";
        public static final String COLUMN_NAME_WEEKS = "whichweeks";
        public static final String COLUMN_NAME_SECTION_START = "whichsectionstart";
        public static final String COLUMN_NAME_SECTION_END = "whichsectionend";
    }
}
