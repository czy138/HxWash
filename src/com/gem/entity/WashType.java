package com.gem.entity;

public enum WashType {
	MACHINE(1,"ª˙œ¥"),DRY(2,"∏…œ¥"),HAND(3," ÷œ¥");
	private int washId;
	private String washType;
	private WashType(int washId, String washType) {
		this.washId = washId;
		this.washType = washType;
	}
	public static WashType valueOf(int washType){
		switch(washType){
		case 1:
			return WashType.valueOf("MACHINE");
		case 2:
			return WashType.valueOf("DRY");
		case 3:
			return WashType.valueOf("HAND");
		default:
			return null;
		}
	}
	public int getWashId() {
		return washId;
	}
	public void setWashId(int washId) {
		this.washId = washId;
	}
	public String getWashType() {
		return washType;
	}
	public void setWashType(String washType) {
		this.washType = washType;
	}
}
