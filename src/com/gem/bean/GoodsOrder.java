package com.gem.bean;


public class GoodsOrder {

	private String size;
	private double price;
	private String name;
	private String color;
	
	public GoodsOrder(String size, double price, String name, String color) {
		super();
		this.size = size;
		this.price = price;
		this.name = name;
		this.color = color;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	
}
