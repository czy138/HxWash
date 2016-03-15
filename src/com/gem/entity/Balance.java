package com.gem.entity;

import java.util.Date;

public class Balance {
	private int balanceId;
	private User user;
	private double baMoney;
	private Date baTime;
	private int baOrder;
	public int getBalanceId() {
		return balanceId;
	}
	public void setBalanceId(int balanceId) {
		this.balanceId = balanceId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public double getBaMoney() {
		return baMoney;
	}
	public void setBaMoney(double baMoney) {
		this.baMoney = baMoney;
	}
	public Date getBaTime() {
		return baTime;
	}
	public void setBaTime(Date baTime) {
		this.baTime = baTime;
	}
	public int getBaOrder() {
		return baOrder;
	}
	public void setBaOrder(int baOrder) {
		this.baOrder = baOrder;
	}
	
}
