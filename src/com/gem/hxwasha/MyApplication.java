package com.gem.hxwasha;

import cn.smssdk.SMSSDK;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);
		SMSSDK.initSDK(this, "10621829d8b94", "077f0629ff4afe8bbdf87219ddaa16b2");
	}
}
