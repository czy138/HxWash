package com.gem.entity;

import java.io.Serializable;

public class Clothes implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//一袋洗是一种衣物，id=0
	private int clothesId;
	private Business business;
	private String clothesName;
	private ClothesType clothesType;
	private String clothesImg;
	public Clothes(){}
	public Clothes(String string) {
		// TODO Auto-generated constructor stub
	}
	public String getClothesImg() {
		return clothesImg;
	}
	public void setClothesImg(String clothesImg) {
		this.clothesImg = clothesImg;
	}
	public int getClothesId() {
		return clothesId;
	}
	public void setClothesId(int clothesId) {
		this.clothesId = clothesId;
	}
	public Business getBusiness() {
		return business;
	}
	public void setBusiness(Business business) {
		this.business = business;
	}
	public String getClothesName() {
		return clothesName;
	}
	public void setClothesName(String clothesName) {
		this.clothesName = clothesName;
	}
	public ClothesType getClothesType() {
		return clothesType;
	}
	public void setClothesType(ClothesType clothesType) {
		this.clothesType = clothesType;
	}
	@Override
	public String toString() {
		return "Clothes [clothesId=" + clothesId + ", business=" + business
				+ ", clothesName=" + clothesName + ", clothesType="
				+ clothesType + ", clothesImg=" + clothesImg + "]";
	}
	
}
