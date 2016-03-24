package com.gem.entity;

public class Price {
	private int priceId;
	private Clothes clothes;
	private WashType washType;
	private double price;
	private int urgent;
	private int eTime;//Ô¤¼ÆÊ±¼ä
	public int getPriceId() {
		return priceId;
	}
	public void setPriceId(int priceId) {
		this.priceId = priceId;
	}
	public Clothes getClothes() {
		return clothes;
	}
	public void setClothes(Clothes clothes) {
		this.clothes = clothes;
	}
	public WashType getWashType() {
		return washType;
	}
	public void setWashType(WashType washType) {
		this.washType = washType;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getUrgent() {
		return urgent;
	}
	public void setUrgent(int urgent) {
		this.urgent = urgent;
	}
	public int geteTime() {
		return eTime;
	}
	public void seteTime(int eTime) {
		this.eTime = eTime;
	}
	@Override
	public String toString() {
		return "Price [priceId=" + priceId + ", clothes=" + clothes
				+ ", washType=" + washType + ", price=" + price + ", urgent="
				+ urgent + ", eTime=" + eTime + "]";
	}
	
}
