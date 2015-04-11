package com.ywxy.ca.entity;

import java.io.Serializable;

public class CetScore implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9042075620714284798L;
	private String cet_name;
	private String cet_no;
	private String cet_level;
	private String cet_college;
	private String cet_sum_score;
	private String cet_listening_score;
	private String cet_reading_score;
	private String cet_writing_score;

	public String getCet_name() {
		return cet_name;
	}

	public void setCet_name(String cet_name) {
		this.cet_name = cet_name;
	}

	public String getCet_no() {
		return cet_no;
	}

	public void setCet_no(String cet_no) {
		this.cet_no = cet_no;
	}

	public String getCet_level() {
		return cet_level;
	}

	public void setCet_level(String cet_level) {
		this.cet_level = cet_level;
	}

	public String getCet_college() {
		return cet_college;
	}

	public void setCet_college(String cet_college) {
		this.cet_college = cet_college;
	}

	public String getCet_sum_score() {
		return cet_sum_score;
	}

	public void setCet_sum_score(String cet_sum_score) {
		this.cet_sum_score = cet_sum_score;
	}

	public String getCet_listening_score() {
		return cet_listening_score;
	}

	public void setCet_listening_score(String cet_listening_score) {
		this.cet_listening_score = cet_listening_score;
	}

	public String getCet_reading_score() {
		return cet_reading_score;
	}

	public void setCet_reading_score(String cet_reading_score) {
		this.cet_reading_score = cet_reading_score;
	}

	public String getCet_writing_score() {
		return cet_writing_score;
	}

	public void setCet_writing_score(String cet_writing_score) {
		this.cet_writing_score = cet_writing_score;
	}

	@Override
	public String toString() {
		return "CetScore [cet_name=" + cet_name + ", cet_no=" + cet_no
				+ ", cet_level=" + cet_level + ", cet_college=" + cet_college
				+ ", cet_sum_score=" + cet_sum_score + ", cet_listening_score="
				+ cet_listening_score + ", cet_reading_score="
				+ cet_reading_score + ", cet_writing_score="
				+ cet_writing_score + "]";
	}

}
