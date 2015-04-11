package com.ywxy.ca.entity;

import java.io.Serializable;
import java.util.List;

public class SemesterGrade implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5230142775033265409L;
	private List<SemesterAllGradeItem> allItem;
	private SemesterAvgGradeItem avgItem;
	private String currentSemester;

	public List<SemesterAllGradeItem> getAllItem() {
		return allItem;
	}

	public void setAllItem(List<SemesterAllGradeItem> allItem) {
		this.allItem = allItem;
	}

	public SemesterAvgGradeItem getAvgItem() {
		return avgItem;
	}

	public void setAvgItem(SemesterAvgGradeItem avgItem) {
		this.avgItem = avgItem;
	}

	public String getCurrentSemester() {
		return currentSemester;
	}

	public void setCurrentSemester(String currentSemester) {
		this.currentSemester = currentSemester;
	}

	@Override
	public String toString() {
		return "SemesterGrade [allItem=" + allItem + ", avgItem=" + avgItem
				+ ", currentSemester=" + currentSemester + "]";
	}

}
