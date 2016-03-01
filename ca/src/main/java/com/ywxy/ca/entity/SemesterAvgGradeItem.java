package com.ywxy.ca.entity;

import java.io.Serializable;

public class SemesterAvgGradeItem implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6718800832293023716L;
    private String studentid;
    private String semester;
    private String AvgGrade;
    private String GradePoint;
    private String ClassRank;
    private String UpdateDate;
    private String schoolYear;

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getAvgGrade() {
        return AvgGrade;
    }

    public void setAvgGrade(String avgGrade) {
        AvgGrade = avgGrade;
    }

    public String getGradePoint() {
        return GradePoint;
    }

    public void setGradePoint(String gradePoint) {
        GradePoint = gradePoint;
    }

    public String getClassRank() {
        return ClassRank;
    }

    public void setClassRank(String classRank) {
        ClassRank = classRank;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    @Override
    public String toString() {
        return "SemesterAvgGradeItem [studentid=" + studentid + ", semester="
                + semester + ", AvgGrade=" + AvgGrade + ", GradePoint="
                + GradePoint + ", ClassRank=" + ClassRank + ", UpdateDate="
                + UpdateDate + ", schoolYear=" + schoolYear + "]";
    }

}
