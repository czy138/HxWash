package com.gem.util;

import java.lang.reflect.Type;
import java.util.List;

import android.content.Context;
import android.widget.ListView;

import com.gem.adapter.BusinessListAdapter;
import com.gem.entity.Business;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class NetUtil {
//	public void getBuisnessList(String url,RequestParams params,final ListView lv,final List<Business> bs,final Context context ){
//		
//		//String url ="http://"+Content.IP+":8080/HXXa/BusinessListServlet"; 
//		HttpUtils http = new HttpUtils();
//		http.send(HttpMethod.GET,url,params, new RequestCallBack<String>() {
//
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> arg0) {
//				// TODO Auto-generated method stub
//				Type type =	new TypeToken<List<Business>>() {  
//                }.getType();
//				Gson gson = new Gson();
//				bs = gson.fromJson(arg0.result,type);
//				lv.setAdapter(new BusinessListAdapter(bs,context));
//			}
//		});
//	}
	
}
