package com.gem.fragment;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gem.entity.Assess;
import com.gem.entity.OrderStatus;
import com.gem.entity.Orders;
import com.gem.entity.User;
import com.gem.hxwasha.OrderDtailsActivity;
import com.gem.hxwasha.PaymentActivity;
import com.gem.hxwasha.R;
import com.gem.util.CommonAdapter;
import com.gem.util.Content;
import com.gem.util.ImageUtil;
import com.gem.util.ViewHolder;
import com.gem.view.AssessDialog;
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
	User user;
	Context context;
	ImageUtil imageUtil;
	BaseAdapter adapter;
	private MessageReceiver mMessageReceiver;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		context=activity;
		registerMessageReceiver();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
				View v =inflater.inflate(R.layout.fragment_order_clothes_order, container,false);
				imageUtil = new ImageUtil(getActivity());
				shared= getActivity().getApplicationContext().getSharedPreferences("user",Context.MODE_PRIVATE);
				String userStr = shared.getString("loginUser", null);
//				if(userStr!=null){
//					Type type = new TypeToken<User>(){}.getType();
//					Gson gson = new Gson();
//					user = gson.fromJson(userStr, type);
//					getOrdersList();
//				}else{
					//测试用
					user = new User();
					user.setUserId(1);
					getOrdersList();
//				}
			
				return v;
	}

	public void initView() {
		lvOrder=(ListView) getView().findViewById(R.id.lv_order);
		lvOrder.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				//点击跳转订单详情
//				Intent intent = new Intent();
				Intent intent = new Intent(getActivity(),OrderDtailsActivity.class);
				startActivity(intent);
			}
			
		});
		
	}
	
	public void initData() {

	}
	
	public void initEvent(List<Orders> list) {
		
		adapter=new CommonAdapter<Orders>(getActivity(),list,R.layout.order_clothes_listview_item) {

			@Override
			public void convert(ViewHolder helper, Orders item) {
				// TODO Auto-generated method stub
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
				ImageView iv = helper.getView(R.id.iv_wash_store_header);
				helper
				.setImageByUrl(R.id.iv_wash_store_header, item.getBusiness().getImgesUrl(),imageUtil)
				.setText(R.id.tv_wash_price, String.valueOf(item.getTotal()))
				.setText(R.id.tv_wash_order_time, sdf.format(item.getOrderTime()))
				.setText(R.id.tv_wash_order_address,item.getAddress().getUserAddress());
				initButton(item, helper);
			}
		};
		lvOrder.setAdapter(adapter);
		
		
	}
	
	@SuppressLint("NewApi") public void initButton(Orders item,ViewHolder helper){
		Button left =helper.getView(R.id.btn_wash_order_evaluate);
		Button right =helper.getView(R.id.btn_reorder);
		TextView status = helper.getView(R.id.tv_evaluate_state);
		TextView shop = helper.getView(R.id.tv_wash_store_name);
		OrderButtonListener listener = new OrderButtonListener(item,left,right,status);
		left.setOnClickListener(listener);
		right.setOnClickListener(listener);
		shop.setOnClickListener(listener);
		left.setTag("left");
		right.setTag("right");
		shop.setTag("shop");
		//两个按钮的默认状态
		showOrders(item,left,right,status);
	}
	@SuppressLint("NewApi") public void showOrders(Orders item,Button left,Button right,TextView status){
		left.setVisibility(View.INVISIBLE);
		right.setVisibility(View.VISIBLE);
		right.setBackground(getResources().getDrawable(R.drawable.white_shape_btn));
		switch(item.getOrderStatus()){
		case PAYNO:
			status.setText("待付款");
			left.setVisibility(View.VISIBLE);
			left.setBackground(getResources().getDrawable(R.drawable.white_shape_btn));
			left.setText("取消订单");
			right.setText("付款");
			break;
		case GETNO:
			status.setText("待取衣");
			right.setBackground(getResources().getDrawable(R.drawable.shape_btn_gray));
			right.setText("等待上门");
			break;
		case SERVICE:
			status.setText("服务中");
			right.setText("确认收衣");
			break;
		case DONE:
			status.setText("已完成");
			left.setVisibility(View.VISIBLE);
			left.setBackground(getResources().getDrawable(R.drawable.shape_btn));
			left.setText("评价订单");
			right.setText("再来一单");
			break;
		case PAYED:
			status.setText("已付款");
			right.setBackground(getResources().getDrawable(R.drawable.shape_btn_gray));
			right.setText("等待接单");
			break;
		case CANCEL:
			status.setText("已取消");
			right.setVisibility(View.INVISIBLE);
			break;
		case DISCUSSS:
			status.setText("已评价");
			right.setText("再来一单");
			break;
		}
	}
	//http://10.201.1.3:8080/HXXa/OrdersListServlet
	public void getOrdersList(){
		String url = "http://"+Content.getIp()+":8080/HXXa/OrdersListServlet";
		HttpUtils http = new HttpUtils();
		Log.i("getOrderList", "userId"+user.getUserId());
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userId",String.valueOf(user.getUserId()));
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
	
	class OrderButtonListener implements OnClickListener{
		private Orders item;
		private Button left;
		private Button right;
		private TextView status;
		
		public OrderButtonListener(Orders item,Button left,Button right,TextView status) {
			super();
			this.item = item;
			this.left = left;
			this.right = right;
			this.status = status;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch((String)v.getTag()){
			case "left":
				switch(item.getOrderStatus()){
				case PAYNO:
					//取消订单
					item.setOrderStatus(OrderStatus.CANCEL);
					chageOrderStatus(item,left,right,status);
					break;
				case DONE:
					//评价订单
					createDialog(item);
					break;
				}
				break;
			case "right":
				switch(item.getOrderStatus()){
				case PAYNO:
					//付款
					Intent intent = new Intent(getActivity(),PaymentActivity.class);
					intent.putExtra("orders", item);
					//跳回来后刷新列表，或者修改该item的按钮
					startActivityForResult(intent, Activity.RESULT_FIRST_USER);
					break;
				case SERVICE:
					//确认收衣
					item.setOrderStatus(OrderStatus.DONE);
					item.setSentTime(new Date());
					chageOrderStatus(item,left,right,status);
					break;
				case DONE:
					//再来一单
					break;
				case DISCUSSS:
					//再来一单
					break;
				}
				break;
			case "shop":
				//跳转洗衣店
				Toast.makeText(getActivity(), "shop:"+item.getOrderId(), 1).show();
				break;
			}
		}

	}
	
	//评价对话框
	private void createDialog(final Orders order) {
		// TODO Auto-generated method stub
		final AssessDialog.Builder builder = new AssessDialog.Builder(context);  
        builder.setTitle("评论");  
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
                //设置你的操作事项  
               int mark =(int) builder.getRatingBar().getRating();
               String content = builder.getEtAssess().getText().toString();
               Assess assess = new Assess();
               assess.setBusiness(order.getBusiness());
               assess.setUser(order.getUser());
               assess.setMark(mark);
               assess.setConntent(content);
               assess(assess,order);
            }  
        });  
  
        builder.setNegativeButton("取消",  
                new android.content.DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int which) {  
                        dialog.dismiss();  
                    }  
                });  
  
        builder.create().show();  
	}
	
	public void assess(Assess assess,Orders order){
		String url ="http://"+Content.getIp()+":8080/HXXa/AssessServlet";
		String assessStr = null;
		String orderStr = null;
		if(assess!=null&&order!=null){
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			assessStr = gson.toJson(assess);
			orderStr = gson.toJson(order);
		}
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("assess",assessStr);
		params.addQueryStringParameter("order",orderStr);
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Log.i("AssessHttp", "访问失败");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				Log.i("AssessHttp", "访问成功");
				notifyList(arg0.result);
			}
		});
		
	}
	
	
	
	public void chageOrderStatus(final Orders item,final Button left,final Button right,final TextView status){
		Gson gson= new GsonBuilder().setDateFormat("yyy-MM-dd HH:mm:ss").create();
		String order=gson.toJson(item);
		
		String url ="http://"+Content.getIp()+":8080/HXXa/ChangeOrderStatusServlet";
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("order",order);
		http.send(HttpMethod.GET, url,params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				showOrders(item,left,right,status);
			}
		});
		
	}
	
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(Content.MESSAGE_RECEIVED_ACTION);
		context.registerReceiver(mMessageReceiver, filter);
	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		context.unregisterReceiver(mMessageReceiver);
	}
	
	  public class MessageReceiver extends BroadcastReceiver {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (Content.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
//	              String extras = intent.getStringExtra(Content.KEY_EXTRAS);
				  String messge = intent.getStringExtra(Content.KEY_MESSAGE);
				  notifyList(messge);
						
				}
			}
		}
	  
	  //order状态改变，服务器返回对象后，更新ListView
	  public void notifyList(String orderStr){
		  Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
          Type type = new TypeToken<Orders>(){}.getType();
          Orders order =gson.fromJson(orderStr, type);
          if(lists!=null&&order!=null){
              for(int i =0 ;i<lists.size();i++){
            	  if(lists.get(i).getOrderId()==order.getOrderId()){
            		  lists.remove(i);
            		  lists.add(i,order);
            	  }
            	  
              }
          }
		  adapter.notifyDataSetChanged();
	  }
	  public void setCostomMsg(String str){
		  
	  }
	  
	  
}
