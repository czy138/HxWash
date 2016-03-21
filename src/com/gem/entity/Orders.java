package com.gem.entity;

import java.io.Serializable;
import java.util.Date;

public class Orders implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int orderId;
	private User user;
	private OrderStatus orderStatus;
	private Business business;
	private Address address;
	private UserCoupon userCoupon;
	private Date orderTime;//预约
	private Date singleTime;//下单
	private Date collectTime;//取衣
	private Date sentTime;//送衣
	private String remark;
	private double total;//使用优惠券之前的总价
	private int payType;//0代表支付宝，1代表余额付款
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Business getBusiness() {
		return business;
	}
	public void setBusiness(Business business) {
		this.business = business;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public UserCoupon getUserCoupon() {
		return userCoupon;
	}
	public void setUserCoupon(UserCoupon userCoupon) {
		this.userCoupon = userCoupon;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public Date getSingleTime() {
		return singleTime;
	}
	public void setSingleTime(Date singleTime) {
		this.singleTime = singleTime;
	}
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	public Date getSentTime() {
		return sentTime;
	}
	public void setSentTime(Date sentTime) {
		this.sentTime = sentTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	
}
