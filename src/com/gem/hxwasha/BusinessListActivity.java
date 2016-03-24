package com.gem.hxwasha;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.gem.adapter.BusinessListAdapter;
import com.gem.entity.Business;
import com.gem.util.Content;
import com.gem.util.SingleRequestQueue;
import com.gem.view.BusinessRefreshListView;
import com.gem.view.BusinessRefreshListView.OnPullListener;
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class BusinessListActivity extends Activity implements OnClickListener,OnPullListener {
	@ViewInject(R.id.lv_business_list_activity)
	BusinessRefreshListView lv;
	@ViewInject(R.id.rl_business_list_clothes)
	RelativeLayout rlClothes;
	@ViewInject(R.id.rl_business_list_luxury)
	RelativeLayout rlLuxury;
	@ViewInject(R.id.rl_business_list_bed)
	RelativeLayout rlBed;
	public static final int ADDLIST=0;
	public static final int REPLAYCE=1;
	List<NameValuePair> parmasList;
	Context context = this;
	List<Business> bs;
	int curPage=1;
	int preCount=0;
	
	double lat;
	double lng;
	
	BusinessListAdapter adapter;
	String clothes;
	String wash;
	
	List<Business> preList=new ArrayList<Business>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_business_list);
		ViewUtils.inject(this);
		clothes="clothes";
		wash ="mashine";
		lat=0;
		lng=0;
		parmasList = new ArrayList<NameValuePair>();
		initEvent();
		parmasList.add(new BasicNameValuePair("curPage",curPage+""));
		parmasList.add(new BasicNameValuePair("clothesType",clothes));
		parmasList.add(new BasicNameValuePair("washType",wash));
		parmasList.add(new BasicNameValuePair("lat",lat+""));
		parmasList.add(new BasicNameValuePair("lng",lng+""));
		getList(parmasList,true,-1);
	}
	public void initEvent(){
		rlClothes.setOnClickListener(this);
		rlLuxury.setOnClickListener(this);
		rlBed.setOnClickListener(this);
		lv.setOnPullListener(this);
		lv.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent,
					View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,BusinessDetailsActivity.class);
				intent.putExtra("businessId", bs.get(position-1).getBusinessId());
				startActivity(intent);
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.rl_business_list_clothes:
			clothes="clothes";
			parmasList.clear();
			parmasList.add(new BasicNameValuePair("curPage",curPage+""));
			parmasList.add(new BasicNameValuePair("clothesType",clothes));
			parmasList.add(new BasicNameValuePair("washType",wash));
			parmasList.add(new BasicNameValuePair("lat",lat+""));
			parmasList.add(new BasicNameValuePair("lng",lng+""));
			getList(parmasList,false,REPLAYCE);
			break;
		case R.id.rl_business_list_luxury:
			clothes="luxury";
			parmasList.clear();
			parmasList.add(new BasicNameValuePair("curPage",curPage+""));
			parmasList.add(new BasicNameValuePair("clothesType",clothes));
			parmasList.add(new BasicNameValuePair("washType",wash));
			parmasList.add(new BasicNameValuePair("lat",lat+""));
			parmasList.add(new BasicNameValuePair("lng",lng+""));
			getList(parmasList,false,REPLAYCE);
			break;
		case R.id.rl_business_list_bed:
			clothes="bed";
			parmasList.clear();
			parmasList.add(new BasicNameValuePair("curPage",curPage+""));
			parmasList.add(new BasicNameValuePair("clothesType",clothes));
			parmasList.add(new BasicNameValuePair("washType",wash));
			parmasList.add(new BasicNameValuePair("lat",lat+""));
			parmasList.add(new BasicNameValuePair("lng",lng+""));
			getList(parmasList,false,REPLAYCE);
			break;
		}
	}
	
	public void getList(List<NameValuePair> parmasList,final boolean isFrist,final int updateType){
		String url = "http://"+Content.getIp()+":8080/HXXa/BusinessListServlet";
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter(parmasList);
		http.send(HttpMethod.POST,url,params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				Type type =	new TypeToken<List<Business>>() {  
                }.getType();
				Gson gson = new Gson();
				List<Business> temp=null;
				temp = gson.fromJson(arg0.result,type);
				if(isFrist){
					bs=temp;
					adapter=new BusinessListAdapter(bs, context);
					lv.setAdapter(adapter);
				}else{

					if(temp!=null||temp.size()>0){
						switch(updateType){
						case ADDLIST:
							if(preList.size()>0){
								bs.removeAll(preList);
								preCount=temp.size();
								preList.clear();
								
							}
							bs.addAll(temp);
							if(temp.size()<Content.PAGESIZE){
								curPage--;
								preList.addAll(temp);
							}
					
							break;
						case REPLAYCE:
							bs.clear();
							bs.addAll(temp);
							break;	
						}
						adapter.notifyDataSetChanged();
					}
					if(preCount!=0&&preCount==temp.size()||temp==null||temp.size()==0){
						Toast.makeText(BusinessListActivity.this, "没有更多了", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
	@Override
	public void onLocation() {
		// TODO Auto-generated method stub
		//定位
		curPage=1;
		parmasList.clear();
		parmasList.add(new BasicNameValuePair("curPage",curPage+""));
		parmasList.add(new BasicNameValuePair("clothesType",clothes));
		parmasList.add(new BasicNameValuePair("washType",wash));
		parmasList.add(new BasicNameValuePair("lat",lat+""));
		parmasList.add(new BasicNameValuePair("lng",lng+""));
		getList(parmasList,false,REPLAYCE);
	}
	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
				parmasList.clear();
				parmasList.add(new BasicNameValuePair("curPage",++curPage+""));
				parmasList.add(new BasicNameValuePair("clothesType",clothes));
				parmasList.add(new BasicNameValuePair("washType",wash));
				parmasList.add(new BasicNameValuePair("lat",lat+""));
				parmasList.add(new BasicNameValuePair("lng",lng+""));
				getList(parmasList,false,ADDLIST);
				lv.completePull();
	}
	


}
