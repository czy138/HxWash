package com.gem.entity;

import java.io.Serializable;

public class Business implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int businessId;
	private String shopName;
	private String imgesUrl;
	private String bAddress;
	private String bArea;
	private String zfb;
	private String runTime;
	private double bagWash;
	private int isurgent;
	private String phoneNum;
	private String password;
	private double money;
	private double lat;
	private double lng;
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public int getBusinessId() {
		return businessId;
	}
	public void setBusinessId(int businessId) {
		this.businessId = businessId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getImgesUrl() {
		return imgesUrl;
	}
	public void setImgesUrl(String imgesUrl) {
		this.imgesUrl = imgesUrl;
	}
	public String getbAddress() {
		return bAddress;
	}
	public void setbAddress(String bAddress) {
		this.bAddress = bAddress;
	}
	public String getbArea() {
		return bArea;
	}
	public void setbArea(String bArea) {
		this.bArea = bArea;
	}
	public String getZfb() {
		return zfb;
	}
	public void setZfb(String zfb) {
		this.zfb = zfb;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public double getBagWash() {
		return bagWash;
	}
	public void setBagWash(double bagWash) {
		this.bagWash = bagWash;
	}
	public int getIsurgent() {
		return isurgent;
	}
	public void setIsurgent(int isurgent) {
		this.isurgent = isurgent;
	}
}
