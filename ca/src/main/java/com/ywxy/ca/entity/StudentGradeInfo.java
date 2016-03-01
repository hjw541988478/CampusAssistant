package com.ywxy.ca.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 学生成绩信息，包括汇总信息和每学期成绩信息
 *
 * @author hjw
 */
public class StudentGradeInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 7813144741646152306L;
    // 历史页面的汇总成绩信息
    private SumGradeInfo sumGrade;
    // 详情页面的成绩信息列表，包括平均分和详细成绩
    private Map<String, SemesterGrade> allSemMap;
    // 拥有的学期学年信息
    private List<CollegeSemester> collegeList;
    // 学号
    private String sno;

    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public SumGradeInfo getSumGrade() {
        return sumGrade;
    }

    public void setSumGrade(SumGradeInfo sumGrade) {
        this.sumGrade = sumGrade;
    }

    public List<CollegeSemester> getCollegeList() {
        return collegeList;
    }

    public void setCollegeList(List<CollegeSemester> collegeList) {
        this.collegeList = collegeList;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public Map<String, SemesterGrade> getAllSemMap() {
        return allSemMap;
    }

    public void setAllSemMap(Map<String, SemesterGrade> allSemMap) {
        this.allSemMap = allSemMap;
    }

    @Override
    public String toString() {
        return "StudentGradeInfo [sumGrade=" + sumGrade + ", allSemMap="
                + allSemMap + ", collegeList=" + collegeList + ", sno=" + sno
                + ", flag=" + flag + "]";
    }

}
