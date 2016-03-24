package com.gem.fragment;

import java.lang.reflect.Type;

import com.gem.entity.Business;
import com.gem.hxwasha.R;
import com.gem.util.Content;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BusinessDetailsFragment extends Fragment {
	TextView shopTime;
	TextView shopPhone;
	TextView shopAddress;
	Business business;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_business_shopinfo, container,false);		
		return v;
	}
	
	public void initView(){
		shopTime = (TextView) getView().findViewById(R.id.tv_shopinfo_time);
		shopPhone = (TextView) getView().findViewById(R.id.tv_shopinfo_phone);
		shopAddress = (TextView) getView().findViewById(R.id.tv_shopinfo_address);
		
		String url ="http://"+Content.getIp()+":8080/HXXa/ListBusinessDetail";
		getList(url);
		

	}
	public void getList(String url){
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("businessId",1+"");
		http.send(HttpMethod.POST, url, params,new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {				
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Type type =	new TypeToken<Business>() {}.getType();
				Gson gson = new GsonBuilder().setDateFormat("HH:mm:dd").create();
				business = gson.fromJson(arg0.result,type);
				shopTime.setText(business.getRunTime());
				shopPhone.setText(business.getPhoneNum());
				shopAddress.setText(business.getbAddress());
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}
	
}
