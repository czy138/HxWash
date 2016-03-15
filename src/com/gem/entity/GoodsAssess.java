package com.gem.entity;

import java.util.Date;

public class GoodsAssess {
	private Goods goods;
	private User user;
	private String goodsAssess;
	private Date goodsAssessTime;
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getGoodsAssess() {
		return goodsAssess;
	}
	public void setGoodsAssess(String goodsAssess) {
		this.goodsAssess = goodsAssess;
	}
	public Date getGoodsAssessTime() {
		return goodsAssessTime;
	}
	public void setGoodsAssessTime(Date goodsAssessTime) {
		this.goodsAssessTime = goodsAssessTime;
	}
}
