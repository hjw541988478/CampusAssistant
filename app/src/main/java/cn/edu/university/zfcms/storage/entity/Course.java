package cn.edu.university.zfcms.storage.entity;

import java.util.List;

/**
 * Created by hjw on 16/4/15.
 */
public class Course {
    public String courseTableId;
    // 第几节课开始
    public int whichSectionStart;
    // 第几节课结束
    public int whichSectionEnd;
    // 哪些周上
    public List<String> weeks;
    // 周几
    public String dayOfWeek;
    // 课程名
    public String courseName;
    // 老师
    public String whoTeach;
    // 在哪上
    public String whereTeach;
    // 如果有调休
    public String adjustCourseNo;
    // 学年
    public String year;
    // 学期
    public String term;

    public Course() {
//        this.setTableName("timetable");
    }

    public Course(String timeTableId, int whichSectionStart, int whichSectionEnd, List<String> weeks, String dayOfWeek, String courseName, String whoTeach, String whereTeach) {
        this.courseTableId = timeTableId;
        this.whichSectionStart = whichSectionStart;
        this.whichSectionEnd = whichSectionEnd;
        this.weeks = weeks;
        this.dayOfWeek = dayOfWeek;
        this.courseName = courseName;
        this.whoTeach = whoTeach;
        this.whereTeach = whereTeach;
    }

    @Override
    public String toString() {
        return "Course{" +
                "whichSectionStart=" + whichSectionStart +
                ", whichSectionEnd=" + whichSectionEnd +
                ", weeks=" + weeks +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", courseName='" + courseName + '\'' +
                ", whoTeach='" + whoTeach + '\'' +
                ", whereTeach='" + whereTeach + '\'' +
                '}';
    }
}
