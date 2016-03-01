package com.ywxy.ca.entity;

import java.io.Serializable;

public class CollegeSemester implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3509988972872677734L;
    private String semester;
    private String schoolYear;

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    @Override
    public String toString() {
        return "CollegeSemester [semester=" + semester + ", schoolYear="
                + schoolYear + "]";
    }


}
