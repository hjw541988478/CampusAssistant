package cn.edu.university.zfcms.model;

import java.util.UUID;

import cn.bmob.v3.BmobObject;

/**
 * Created by hjw on 16/4/18.
 */
public class BasicCourse extends BmobObject{

    public String courseId;
    public String courseName;
    public String whoTeach;
    public String whereTeach;

    public BasicCourse() {}

    public BasicCourse(String courseId, String courseName, String whoTeach, String whereTeach) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.whoTeach = whoTeach;
        this.whereTeach = whereTeach;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        setCourseId(UUID.randomUUID().toString());
        this.courseName = courseName;
    }

    public void setWhoTeach(String whoTeach) {
        this.whoTeach = whoTeach;
    }

    public void setWhereTeach(String whereTeach) {
        this.whereTeach = whereTeach;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getWhoTeach() {
        return whoTeach;
    }

    public String getWhereTeach() {
        return whereTeach;
    }

    public boolean isEmpty(){
        return courseName == null || whereTeach == null || whoTeach == null;
    }

    @Override
    public String toString() {
        return "BasicCourse{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", whoTeach='" + whoTeach + '\'' +
                ", whereTeach='" + whereTeach + '\'' +
                '}';
    }
}
