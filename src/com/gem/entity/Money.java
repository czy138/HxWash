package com.gem.entity;

import java.util.Date;

public class Money {
	private int moneyId;
	private Business business;
	private double money;
	private Date time;
	public int getMoneyId() {
		return moneyId;
	}
	public void setMoneyId(int moneyId) {
		this.moneyId = moneyId;
	}
	public Business getBusiness() {
		return business;
	}
	public void setBusiness(Business business) {
		this.business = business;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
}
