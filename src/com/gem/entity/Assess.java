package com.gem.entity;

import java.util.Date;

public class Assess {
	private int assessId;
	private User user;
	private Business business;
	private Date assessTime;
	private int mark;
	private String conntent;
	public int getAssessId() {
		return assessId;
	}
	public void setAssessId(int assessId) {
		this.assessId = assessId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Business getBusiness() {
		return business;
	}
	public void setBusiness(Business business) {
		this.business = business;
	}
	public Date getAssessTime() {
		return assessTime;
	}
	public void setAssessTime(Date assessTime) {
		this.assessTime = assessTime;
	}
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	public String getConntent() {
		return conntent;
	}
	public void setConntent(String conntent) {
		this.conntent = conntent;
	}
}
