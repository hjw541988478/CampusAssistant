package com.ywxy.ca.entity;

import java.io.Serializable;

public class SemesterAllGradeItem implements Serializable {
	private static final long serialVersionUID = 4605493205654007864L;
	private String studentid;
	private String courseid;
	private String coursename;
	private String grade;
	private String makeup;
	private String isAgain;
	private String schoolYear;
	private String mark;
	private String semester;
	private String name;
	private String id;
	private String credit;
	private String type;
	private String addtime;
	private String schoolname;
	private String gradeText;
	private String isAgainText;

	public String getStudentid() {
		return studentid;
	}

	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}

	public String getCourseid() {
		return courseid;
	}

	public void setCourseid(String courseid) {
		this.courseid = courseid;
	}

	public String getCoursename() {
		return coursename;
	}

	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getMakeup() {
		return makeup;
	}

	public void setMakeup(String makeup) {
		this.makeup = makeup;
	}

	public String getIsAgain() {
		return isAgain;
	}

	public void setIsAgain(String isAgain) {
		this.isAgain = isAgain;
	}

	public String getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getSchoolname() {
		return schoolname;
	}

	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}

	public String getGradeText() {
		return gradeText;
	}

	public void setGradeText(String gradeText) {
		this.gradeText = gradeText;
	}

	public String getIsAgainText() {
		return isAgainText;
	}

	public void setIsAgainText(String isAgainText) {
		this.isAgainText = isAgainText;
	}

	@Override
	public String toString() {
		return "SemesterAllGradeItem [studentid=" + studentid + ", courseid="
				+ courseid + ", coursename=" + coursename + ", grade=" + grade
				+ ", makeup=" + makeup + ", isAgain=" + isAgain
				+ ", schoolYear=" + schoolYear + ", mark=" + mark
				+ ", semester=" + semester + ", name=" + name + ", id=" + id
				+ ", credit=" + credit + ", type=" + type + ", addtime="
				+ addtime + ", schoolname=" + schoolname + ", gradeText="
				+ gradeText + ", isAgainText=" + isAgainText + "]";
	}

}
