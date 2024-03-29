package com.gem.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.util.Log;

public class Content {
	public static String MESSAGE_RECEIVED_ACTION="com.czy.orderStatus";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	//地球
	public static double EARTH_RADIUS = 6378.137;
	//首页分页大小
	public static final int PAGESIZE=10;
	//洗衣订单跳转 requestCode
	public static final int PAY_CLOTHS=0;
	//购物订单跳转 requestCode
	public static final int PAY_GOODS=1;
	public static String getIp(){
		Properties p =new Properties();
		InputStream input = Content.class.getResourceAsStream("net.properties");
		try {
			p.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p.getProperty("ip");
	}
}
