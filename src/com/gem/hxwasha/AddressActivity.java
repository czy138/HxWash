package com.gem.hxwasha;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.gem.adapter.AddressAdapter;
import com.gem.entity.Address;
import com.gem.util.Content;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AddressActivity extends Activity {
	@ViewInject(R.id.lv_address_list)
	ListView lv;
	List<Address> ads;
	AddressAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		ViewUtils.inject(this);
		ads= new ArrayList<Address>();
		getAddressLsit();
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long arg3) {
				Intent intent = new Intent(AddressActivity.this,ConfirmOrderActivity.class);
				intent.putExtra("addressIndex", position);
				setResult(RESULT_OK,intent);
				finish();
			}
		});

	}
	
	public void getAddressLsit(){
		HttpUtils http = new HttpUtils();
		String url = "http://"+Content.getIp()+":8080/HXXa/AddressListServlet";
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userId","1");
		params.addQueryStringParameter("from","address");
		http.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				Type type =	new TypeToken<List<Address>>() {  
                }.getType();
				Gson gson = new Gson();
				
				String result = arg0.result;
				ads = gson.fromJson(result, type);
				adapter= new AddressAdapter(AddressActivity.this,ads);
				lv.setAdapter(adapter);
			}
		});
	}

	
}
