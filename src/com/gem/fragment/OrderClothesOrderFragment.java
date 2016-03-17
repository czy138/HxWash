package com.gem.fragment;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.gem.entity.Orders;
import com.gem.hxwasha.R;
import com.gem.util.CommonAdapter;
import com.gem.util.Content;
import com.gem.util.ImageUtil;
import com.gem.util.ViewHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class OrderClothesOrderFragment extends Fragment {
	SharedPreferences  shared;
	ListView lvOrder;
	List<Orders> lists;
	int curPage=1;
	int userId=-1;
	Context context;
	ImageUtil imageUtil;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
				View v =inflater.inflate(R.layout.fragment_order_clothes_order, container,false);
				imageUtil = new ImageUtil(getActivity());
				shared= getActivity().getApplicationContext().getSharedPreferences("user",Context.MODE_PRIVATE);
				userId = shared.getInt("LoginUser", -1);
				userId=1;
				getOrdersList();
						return v;
	}

	public void initView() {
		lvOrder=(ListView) getView().findViewById(R.id.lv_order);
	}
	
	public void initData() {

	}
	
	public void initEvent(List<Orders> list) {
		lvOrder.setAdapter(new CommonAdapter<Orders>(getActivity(),list,R.layout.order_clothes_listview_item) {

			@Override
			public void convert(ViewHolder helper, Orders item) {
				// TODO Auto-generated method stub
				SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
				ImageView iv = helper.getView(R.id.iv_wash_store_header);
				helper
				.setImageByUrl(R.id.iv_wash_store_header, item.getBusiness().getImgesUrl(),imageUtil)
				.setText(R.id.tv_wash_price, String.valueOf(item.getTotal()))
				.setText(R.id.tv_wash_order_time, sdf.format(item.getOrderTime()))
				.setText(R.id.tv_wash_order_address,item.getAddress().getUserAddress());
			}
		});
	}
	//http://10.201.1.3:8080/HXXa/OrdersListServlet
	public void getOrdersList(){
		String url = "http://"+Content.getIp()+":8080/HXXa/OrdersListServlet";
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userId",String.valueOf(userId));
		params.addQueryStringParameter("curPage",String.valueOf(curPage));
		http.send(HttpMethod.GET, url,params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
			
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				Type type =	new TypeToken<List<Orders>>() {  
                }.getType();
				Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
				lists = gson.fromJson(arg0.result,type);	
				initEvent(lists);
			}
		});
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();
		
	}
	
}
