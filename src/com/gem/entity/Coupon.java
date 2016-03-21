package com.gem.entity;

import java.io.Serializable;
import java.util.Date;

public class Coupon implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int couponId;
	private double value;
	private double usePrice;
	private Date lineTime;
	private String acName;
	private String acNr;//¹ã¸æÄÚÈÝ
	private String acImageUrl;
	public int getCouponId() {
		return couponId;
	}
	public void setCouponId(int couponId) {
		this.couponId = couponId;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public double getUsePrice() {
		return usePrice;
	}
	public void setUsePrice(double usePrice) {
		this.usePrice = usePrice;
	}
	public Date getLineTime() {
		return lineTime;
	}
	public void setLineTime(Date lineTime) {
		this.lineTime = lineTime;
	}
	public String getAcName() {
		return acName;
	}
	public void setAcName(String acName) {
		this.acName = acName;
	}
	public String getAcNr() {
		return acNr;
	}
	public void setAcNr(String acNr) {
		this.acNr = acNr;
	}
	public String getAcImageUrl() {
		return acImageUrl;
	}
	public void setAcImageUrl(String acImageUrl) {
		this.acImageUrl = acImageUrl;
	}
}
