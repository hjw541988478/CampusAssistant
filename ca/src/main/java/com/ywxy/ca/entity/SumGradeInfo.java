package com.ywxy.ca.entity;

import java.io.Serializable;
import java.util.List;

public class SumGradeInfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 5361474157691189370L;
    //总平均绩点
    private float GradePoint;
    //总平均分
    private float AvgGrade;
    //挂科数
    private int NopassNum;
    //真名
    private String Truename;
    //挂科列表信息
    private List<SemesterAllGradeItem> noPassList;

    public List<SemesterAllGradeItem> getNoPassList() {
        return noPassList;
    }

    public void setNoPassList(List<SemesterAllGradeItem> noPassList) {
        this.noPassList = noPassList;
    }

    public float getGradePoint() {
        return GradePoint;
    }

    public void setGradePoint(float gradePoint) {
        GradePoint = gradePoint;
    }

    public float getAvgGrade() {
        return AvgGrade;
    }

    public void setAvgGrade(float avgGrade) {
        AvgGrade = avgGrade;
    }

    public int getNopassNum() {
        return NopassNum;
    }

    public void setNopassNum(int nopassNum) {
        NopassNum = nopassNum;
    }

    public String getTruename() {
        return Truename;
    }

    public void setTruename(String truename) {
        Truename = truename;
    }

    @Override
    public String toString() {
        return "SumGradeInfo [GradePoint=" + GradePoint + ", AvgGrade="
                + AvgGrade + ", NopassNum=" + NopassNum + ", Truename="
                + Truename + ", noPassList=" + noPassList + "]";
    }

}
