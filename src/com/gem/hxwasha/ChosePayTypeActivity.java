package com.gem.hxwasha;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gem.entity.OrderClothes;
import com.gem.entity.Orders;
import com.gem.entity.User;
import com.gem.util.CommonAdapter;
import com.gem.util.Content;
import com.gem.util.ViewHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ChosePayTypeActivity extends Activity {
	@ViewInject(R.id.tv_pay_user_name)
	TextView tvUserName;
	@ViewInject(R.id.tv_pay_user_tel)
	TextView tvUserTel;
	@ViewInject(R.id.tv_pay_user_address)
	TextView tvUserAddress;
	@ViewInject(R.id.tv_pay_order_time)
	TextView tvOrderTime;
	@ViewInject(R.id.tv_pay_type_count_money)
	TextView tvTotalMoney;
	@ViewInject(R.id.tv_pay_type_discount1)
	TextView tvCoupontValue;
	@ViewInject(R.id.tv_pay_type_real_money)
	TextView tvRealPay;
	@ViewInject(R.id.tv_pay_type_discount2)
	TextView tvCouponValueBottom;
	@ViewInject(R.id.tv_submit_pay_money)
	TextView tvPayBottom;
	@ViewInject(R.id.tv_pay_type_store_name)
	TextView tvShopName;
	@ViewInject(R.id.btn_submit_order)
	ImageView ivSubmit;
	@ViewInject(R.id.lv_pay_order_detail)
	ListView lv;
	@ViewInject(R.id.iv_return)
	ImageView ivReturn;
	
	
	List<OrderClothes> ocs;
	Orders orders;
	OrderClothes orderClothes ;
	double needPay ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chose_pay_type);
		ViewUtils.inject(this);
		
		getData();
		//initData();
		setListViewHeightBasedOnChildren(lv);
		//返回上个界面
		ivReturn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}	  
	
	
	//从上个界面获取订单数据
	public void getData(){
		orders = new Orders();
		ocs = new ArrayList<OrderClothes>();	
		Intent intent = getIntent();
		//获取上个界面的Orders信息
		orders = (Orders) intent.getExtras().getSerializable("orders");
		//获取上个界面的OrderClothes
		String sOcs = intent.getExtras().getString("OrderClothes");
		Type type = new TypeToken<List<OrderClothes>>(){}.getType();
		Gson gson = new GsonBuilder()
				.enableComplexMapKeySerialization()
				.setPrettyPrinting()
				.disableHtmlEscaping().create();
		ocs = gson.fromJson(sOcs, type);	
//		User user = SharedUtil.getLoginUser(this);
		//测试User
		User user = new User();
		user.setBalance(20);
		user.setNickName("隔壁老王");
		user.setTel("18297305863");
		user.setUserId(1);
		
		orders.setUser(user);		
		if(user.getNickName()==null){
			user.setNickName("noNickName");
		}
		
		tvUserName.setText(orders.getAddress().getCallName());
		tvUserTel.setText(orders.getUser().getTel());
		tvUserAddress.setText(orders.getAddress()+"");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		String time = sdf.format(orders.getOrderTime());
		tvOrderTime.setText(time);
		tvTotalMoney.setText(orders.getTotal()+"");
		
		//优惠券的控件赋值
		needPay = orders.getTotal()-orders.getUserCoupon().getCoupon().getValue();
		tvCoupontValue.setText(orders.getUserCoupon().getCoupon().getValue()+"");
		tvRealPay.setText(needPay+"");
		tvCouponValueBottom.setText(orders.getUserCoupon().getCoupon().getValue()+"");
		
		tvPayBottom.setText(needPay+"");
		
		Log.i("TTT", needPay+"");
		
		//显示lv列表(万能适配器)
		 lv.setAdapter(new CommonAdapter<OrderClothes>(this, ocs, R.layout.item_order_details) {
			@Override
			public void convert(ViewHolder helper, OrderClothes item) {
				helper.setText(R.id.i_cloth, item.getPrice().getClothes().getClothesName())
				.setText(R.id.i_Laundryway, item.getPrice().getWashType().getWashType())
				.setText(R.id.i_price, item.getPrice().getPrice()+"")
				.setText(R.id.i_allprice, item.getPrice().getPrice()*item.getNum()+"")
				.setText(R.id.i_number, item.getNum()+"");
				}
		});
	}
	
	public static void setListViewHeightBasedOnChildren(ListView lv) {
		  ListAdapter listAdapter = lv.getAdapter();
		  if (listAdapter == null) {
		   return;
		  }
		  int totalHeight = 0;
		  for (int i = 0; i < listAdapter.getCount(); i++) {
		   View listItem = listAdapter.getView(i, null, lv);
		   listItem.measure(0, 0);
		   totalHeight += listItem.getMeasuredHeight();
		  }
		  ViewGroup.LayoutParams params = lv.getLayoutParams();
		  params.height = totalHeight
		    + (lv.getDividerHeight() * (listAdapter.getCount() - 1));
		  lv.setLayoutParams(params);
		 }
	
	public void initEvent(){
		ivSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				orders.setSingleTime(new Date());
				submitOrders();
			}
		});
	}
	
	public void submitOrders(){
		Gson gson = new GsonBuilder()
				.enableComplexMapKeySerialization()
				.setPrettyPrinting()
				.disableHtmlEscaping()
				.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		
		String ordersStr=gson.toJson(orders);
		String orderClothes = gson.toJson(ocs);
		
		String url ="http://"+Content.getIp()+":8080/HXXa/SubmitOrdersServlet";
		RequestParams params = new RequestParams();
		params.addBodyParameter("orderClothes", orderClothes);
		params.addBodyParameter("orders", ordersStr);
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				//在这里传值跳转（orders）
				Bundle bundle = new Bundle();
				bundle.putSerializable("orders", orders);
				Intent intent = new Intent(ChosePayTypeActivity.this,PaymentActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
		});
	}
}
