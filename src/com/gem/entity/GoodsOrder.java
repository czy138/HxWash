package com.gem.entity;

import java.util.Date;

public class GoodsOrder {
	private int goodsOrderId;
	private User user;
	private Address address;
	private GoodsOrderStatus goodsOrderStatus;
	private Date goodsSingleTime;
	private Date goodsFinishTime;
	private String goodsRemarks;
	private double goodsTotal;
	private int payType;
	public int getGoodsOrderId() {
		return goodsOrderId;
	}
	public void setGoodsOrderId(int goodsOrderId) {
		this.goodsOrderId = goodsOrderId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public GoodsOrderStatus getGoodsOrderStatus() {
		return goodsOrderStatus;
	}
	public void setGoodsOrderStatus(GoodsOrderStatus goodsOrderStatus) {
		this.goodsOrderStatus = goodsOrderStatus;
	}
	public Date getGoodsSingleTime() {
		return goodsSingleTime;
	}
	public void setGoodsSingleTime(Date goodsSingleTime) {
		this.goodsSingleTime = goodsSingleTime;
	}
	public Date getGoodsFinishTime() {
		return goodsFinishTime;
	}
	public void setGoodsFinishTime(Date goodsFinishTime) {
		this.goodsFinishTime = goodsFinishTime;
	}
	public String getGoodsRemarks() {
		return goodsRemarks;
	}
	public void setGoodsRemarks(String goodsRemarks) {
		this.goodsRemarks = goodsRemarks;
	}
	public double getGoodsTotal() {
		return goodsTotal;
	}
	public void setGoodsTotal(double goodsTotal) {
		this.goodsTotal = goodsTotal;
	}
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	
}
