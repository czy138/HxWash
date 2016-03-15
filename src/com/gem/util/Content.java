package com.gem.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.util.Log;

public class Content {
	public static final int PAGESIZE=5;
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
