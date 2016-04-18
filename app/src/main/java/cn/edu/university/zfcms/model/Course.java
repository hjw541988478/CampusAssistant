package cn.edu.university.zfcms.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by hjw on 16/4/15.
 */
public class Course extends BmobObject{
    public String timeCourseId;
    // 第几课开始
    public int courseWhichSectionStart;
    // 持续几节课
    public int courseSectionsDuration;
    // 第几周
    public int courseWeekNo;
    // 周几
    public String courseDayOfWeek;
    // 在哪上啥时候上上的啥课
    public BasicCourse courseBasicInfo;

    public Course() {}

    public Course(String timeCourseId, BasicCourse course, int courseWeekNo, int courseWhichSectionStart, int courseSectionsDuration, String courseDayOfWeek) {
        this.timeCourseId = timeCourseId;
        this.courseBasicInfo = course;
        this.courseWhichSectionStart = courseWhichSectionStart;
        this.courseSectionsDuration = courseSectionsDuration;
        this.courseDayOfWeek = courseDayOfWeek;
        this.courseWeekNo = courseWeekNo;
    }

    @Override
    public String toString() {
        return "Course{" +
                "timeCourseId='" + timeCourseId + '\'' +
                ", courseWhichSectionStart=" + courseWhichSectionStart +
                ", courseSectionsDuration=" + courseSectionsDuration +
                ", courseWeekNo=" + courseWeekNo +
                ", courseDayOfWeek='" + courseDayOfWeek + '\'' +
                ", courseBasicInfo=" + courseBasicInfo +
                '}';
    }
}
