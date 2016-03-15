package com.gem.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class SingleRequestQueue {
	private static RequestQueue mQueue;
	 
	private SingleRequestQueue(Context context) {
		mQueue = Volley.newRequestQueue(context);
	}
 
	public static synchronized RequestQueue getRequestQueue(Context context){
		if (mQueue == null){
		new SingleRequestQueue(context.getApplicationContext());
		}
		return mQueue;
	}
}
