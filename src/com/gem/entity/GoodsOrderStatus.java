package com.gem.entity;

public enum GoodsOrderStatus {
	//待付款，待收货，待评价，完成，已取消
	 NPay(1,"待付款"), Receipt(2,"待收货"),Evaluated(3,"待评价"),Finish(4,"完成"),Cancel(5,"已取消");
	private int goodsOrderStatusId;
	private String goodsOrderStatus;
	
	public static GoodsOrderStatus getEnumById(int id){
		switch(id){
		case 1:
			return  NPay;
		case 2:
			return Receipt;
		case 3:
			return Evaluated;
		case 4:
			return Finish;
		case 5:
			return Cancel;
		default:
			return null;			
		}
	}
	
	public int getGoodsOrderStatusId() {
		return goodsOrderStatusId;
	}

	public void setGoodsOrderStatusId(int goodsOrderStatusId) {
		this.goodsOrderStatusId = goodsOrderStatusId;
	}

	public String getGoodsOrderStatus() {
		return goodsOrderStatus;
	}

	public void setGoodsOrderStatus(String goodsOrderStatus) {
		this.goodsOrderStatus = goodsOrderStatus;
	}

	private GoodsOrderStatus(int goodsOrderStatusId, String goodsOrderStatus) {
		this.goodsOrderStatusId = goodsOrderStatusId;
		this.goodsOrderStatus = goodsOrderStatus;
	}
	
}
