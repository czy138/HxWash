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
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
	Handler handler=new Handler();
	Context context = this;
	List<Business> bs;
	int curPage=1;
	int preCount=0;
	
	BusinessListAdapter adapter;
	String busiType;
	String clothes;
	String wash;
	
	RequestQueue queue;
	List<Business> preList=new ArrayList<Business>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business_list);
		ViewUtils.inject(this);
		clothes="clothes";
		wash ="mashine";
		parmasList = new ArrayList<NameValuePair>();
		initEvent();
		busiType =(String) getIntent().getExtras().get("busiType");
		queue=SingleRequestQueue.getRequestQueue(this);
		switch(busiType){
		case "wash":
			parmasList.add(new BasicNameValuePair("from","wash"));
			parmasList.add(new BasicNameValuePair("curPage",curPage+""));
			parmasList.add(new BasicNameValuePair("clothesType",clothes));
			parmasList.add(new BasicNameValuePair("washType",wash));
			
			break;
		case "bagwash":
			//只支持衣物，机洗
			parmasList.add(new BasicNameValuePair("from","bagwash"));
			break;
		case "urgent":
			//只支持衣物
			parmasList.add(new BasicNameValuePair("from","urgent"));
			
			break;
		}
		getList(parmasList,true,-1);
		
	}
	public void initEvent(){
		rlClothes.setOnClickListener(this);
		rlLuxury.setOnClickListener(this);
		rlBed.setOnClickListener(this);
		lv.setOnPullListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.rl_business_list_clothes:
			clothes="clothes";
			parmasList.clear();
			parmasList.add(new BasicNameValuePair("from","wash"));
			parmasList.add(new BasicNameValuePair("curPage",curPage+""));
			parmasList.add(new BasicNameValuePair("clothesType",clothes));
			parmasList.add(new BasicNameValuePair("washType",wash));
			getList(parmasList,false,REPLAYCE);
			break;
		case R.id.rl_business_list_luxury:
			clothes="luxury";
			parmasList.clear();
			parmasList.add(new BasicNameValuePair("from","wash"));
			parmasList.add(new BasicNameValuePair("curPage",curPage+""));
			parmasList.add(new BasicNameValuePair("clothesType",clothes));
			parmasList.add(new BasicNameValuePair("washType",wash));
			getList(parmasList,false,REPLAYCE);
			break;
		case R.id.rl_business_list_bed:
			clothes="bed";
			parmasList.clear();
			parmasList.add(new BasicNameValuePair("from","wash"));
			parmasList.add(new BasicNameValuePair("curPage",curPage+""));
			parmasList.add(new BasicNameValuePair("clothesType",clothes));
			parmasList.add(new BasicNameValuePair("washType",wash));
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
		parmasList.add(new BasicNameValuePair("from",busiType));
		parmasList.add(new BasicNameValuePair("curPage",curPage+""));
		parmasList.add(new BasicNameValuePair("clothesType",clothes));
		parmasList.add(new BasicNameValuePair("washType",wash));
		getList(parmasList,false,REPLAYCE);
	}
	@Override
	public void onLoad() {
		// TODO Auto-generated method stub

		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				parmasList.clear();
				parmasList.add(new BasicNameValuePair("from",busiType));
				parmasList.add(new BasicNameValuePair("curPage",++curPage+""));
				parmasList.add(new BasicNameValuePair("clothesType",clothes));
				parmasList.add(new BasicNameValuePair("washType",wash));
				getList(parmasList,false,ADDLIST);
				lv.completePull();
				
			}
		}, 2000);
		
	}
	


}
