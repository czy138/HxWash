package com.gem.entity;

import java.io.Serializable;

public enum OrderStatus implements Serializable{
	//待付款，待取衣，服务中，已完成，已取消
	PAYNO(1,"待付款"),GETNO(2,"待取衣"),SERVICE(3,"服务中"),DONE(4,"已完成"),CANCEL(5,"已取消"),PAYED(6,"已付款"),DISCUSSS(7,"已评论");
	private int statusId;
	private String orderStatus;	
	public static OrderStatus valueOf(int statusId){
		switch(statusId){
		case 1:
			return OrderStatus.valueOf("PAYNO");
		case 2:
			return OrderStatus.valueOf("GETNO");
		case 3:
			return OrderStatus.valueOf("SERVICE");
		case 4:
			return OrderStatus.valueOf("DONE");
		case 5:
			return OrderStatus.valueOf("CANCEL");
		case 6:
			return OrderStatus.valueOf("PAYED");
		case 7:
			return OrderStatus.valueOf("DISCUSSS");
		default:
			return null;
		}
	}
	private OrderStatus(int statusId, String orderStatus) {
		this.statusId = statusId;
		this.orderStatus = orderStatus;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
}
