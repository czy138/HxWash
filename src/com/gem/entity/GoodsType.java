package com.gem.entity;

public enum GoodsType {
	 Clothse(1,"衣物"), BED(2,"奢侈品"),WAZI(3,"床上用品"),UNDERWEAR(4,"内裤");
	    private int goodsType;
	    private String type;
		
		public static GoodsType getEnumById(int id){
			switch(id){
			case 1:
				return Clothse;
			case 2:
				return BED;
			case 3:
				return WAZI;
			case 4:
				return UNDERWEAR;
			default:
				return null;			
			}
		}

	public int getGoodsType() {
			return goodsType;
		}

		public void setGoodsType(int goodsType) {
			this.goodsType = goodsType;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	private GoodsType(int goodsType, String type) {
		this.goodsType = goodsType;
		this.type = type;
	}
}