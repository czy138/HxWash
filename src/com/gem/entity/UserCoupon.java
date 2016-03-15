package com.gem.entity;

import java.util.Date;

public class UserCoupon {
	private int userCouponId;
	private Coupon coupon;
	private User user;
	private Date getTime;
	private int isUsed;
	public int getUserCouponId() {
		return userCouponId;
	}
	public void setUserCouponId(int userCouponId) {
		this.userCouponId = userCouponId;
	}
	public Coupon getCoupon() {
		return coupon;
	}
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getGetTime() {
		return getTime;
	}
	public void setGetTime(Date getTime) {
		this.getTime = getTime;
	}
	public int getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(int isUsed) {
		this.isUsed = isUsed;
	}
}
