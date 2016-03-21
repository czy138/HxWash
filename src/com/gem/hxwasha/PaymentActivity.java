package com.gem.hxwasha;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gem.entity.OrderStatus;
import com.gem.entity.Orders;
import com.gem.util.Content;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class PaymentActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.tv_pay_money)
	TextView tvShowPayMoney;
	@ViewInject(R.id.tv_pay_order_id2)
	TextView tvOrderId;
	@ViewInject(R.id.tv_pay_yue2)
	TextView tvBalance;
	@ViewInject(R.id.tv_need_pay_money2)
	TextView tvNeedPay;
	@ViewInject(R.id.iv_pay_type)
	ImageView payType;
	@ViewInject(R.id.rel_zhi_fu_bao)
	RelativeLayout relZFB;
	@ViewInject(R.id.btn_ensure_pay)
	Button ensurePay;
	
	boolean isSelect=false;
	
	Orders order;
	double balanceSelect;
	double needPaySelect;
	double balanceDefault;
	double needPayDefault;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_payment);
		ViewUtils.inject(this);
		initDate();
		initEvent();
	}

	public void initDate(){
		order = (Orders) getIntent().getSerializableExtra("orders");
		int orderId = order.getOrderId();
		BigDecimal total =new BigDecimal(order.getTotal());
		BigDecimal couponValue = new BigDecimal(order.getUserCoupon().getCoupon().getValue());
		BigDecimal payMoney=null;
		if(order.getUserCoupon()!=null){
			payMoney = total.subtract(couponValue);
		}else{
			payMoney=total;
		}
		
		BigDecimal balance = BigDecimal.valueOf(order.getUser().getBalance());
		balanceDefault = balance.doubleValue();
		needPayDefault=payMoney.doubleValue();
		
		needPaySelect = payMoney.subtract(balance).doubleValue();
		if(needPaySelect<0){
			balanceSelect=Math.abs(needPaySelect);
			needPaySelect=0;
		}else{
			balanceSelect=0;
		}
		tvShowPayMoney.setText(payMoney.toString());
		tvOrderId.setText(String.valueOf(orderId));
		tvBalance.setText(String.valueOf(balanceDefault));
		tvNeedPay.setText(String.valueOf(needPayDefault));
	}
	
	public void initEvent(){
		payType.setOnClickListener(this);
		relZFB.setOnClickListener(this);
		ensurePay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.iv_pay_type:
			if(isSelect){
				isSelect=false;
				payType.setImageDrawable(getResources().getDrawable(R.drawable.pay_type2));
				tvBalance.setText(String.valueOf(balanceDefault));
				tvNeedPay.setText(String.valueOf(needPayDefault));
			}else{
				isSelect=true;
				payType.setImageDrawable(getResources().getDrawable(R.drawable.pay_type1));
				tvBalance.setText(String.valueOf(balanceSelect));
				tvNeedPay.setText(String.valueOf(needPaySelect));
			}
			break;
		case R.id.rel_zhi_fu_bao:
			break;
		case R.id.btn_ensure_pay:
			if(Double.valueOf(tvNeedPay.getText().toString())==0){
				pay();
			}else{
				Toast.makeText(PaymentActivity.this, "余额不足", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	public void pay(){
		String url ="http://"+Content.getIp()+":8080/HXXa/PayServlet";
		order.setOrderStatus(OrderStatus.PAYED);
		Gson gson = new GsonBuilder().setDateFormat("yyy-MM-dd HH:mm:ss").create();
		String orderStr =gson.toJson(order);
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("order",orderStr);
		params.addQueryStringParameter("payMoney",String.valueOf(needPayDefault));
		http.send(HttpMethod.GET, url,params,new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				if(arg0.result!=null&&arg0.result.equals("rollback")){
					Toast.makeText(PaymentActivity.this, "网络连接出错", Toast.LENGTH_SHORT).show();
				}else{
					Intent data = new Intent();
					setResult(RESULT_OK, data);
					finish();
				}
			}
		});
		
	}
}
