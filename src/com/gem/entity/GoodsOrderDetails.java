package com.gem.entity;

public class GoodsOrderDetails {
	private GoodsOrder goodsOrder;
	private Goods goods;
	private int goodsNum;
	private double goodsPrice;
	public GoodsOrder getGoodsOrder() {
		return goodsOrder;
	}
	public void setGoodsOrder(GoodsOrder goodsOrder) {
		this.goodsOrder = goodsOrder;
	}
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public int getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}
	public double getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
}
