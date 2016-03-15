package com.gem.fragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gem.adapter.SectionedBaseAdapter;
import com.gem.entity.Clothes;
import com.gem.entity.ClothesType;
import com.gem.entity.Price;
import com.gem.hxwasha.R;
import com.gem.util.Content;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class BusinessClothesFragment extends Fragment {
	private boolean isScroll = true;
	
	ListView lvLeft;
	ListView lvRight;
	String[] leftStr;
	Activity mactivity;
	
	RightAdapter adapter;
	@Override
	public void onAttach(Activity activity) { 
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mactivity=activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view =inflater.inflate(R.layout.fragment_business_clothes, container, false);
		lvLeft=(ListView) view.findViewById(R.id.lv_left);
		lvRight=(ListView) view.findViewById(R.id.lv_right);
		
		
		lvLeft.setBackgroundColor(Color.rgb(248, 248, 248));
		lvLeft.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				isScroll = false;

				for (int i = 0; i < lvLeft.getChildCount(); i++)
				{
					if (i == position)
					{
						lvLeft.getChildAt(i).setBackgroundColor(Color.rgb(255, 255, 255));
					}else{
						lvLeft.getChildAt(i).setBackgroundColor(Color.rgb(248, 248, 248));
					}
				}
				int rightSection = 0;
				for(int i=0;i<position;i++){
					rightSection += adapter.getCountForSection(i)+1;
				}
				lvRight.setSelection(rightSection);
				
			}
		});
		
		lvRight.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if(isScroll){
					for (int i = 0; i < lvLeft.getChildCount(); i++)
					{
						
						if (i == adapter.getSectionForPosition(firstVisibleItem))
						{
							lvLeft.getChildAt(i).setBackgroundColor(
									Color.rgb(255, 255, 255));
						} else
						{
							lvLeft.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);

						}
					}
						
				}else{
					isScroll = true;
				}
			}
		});
		String url ="http://"+Content.getIp()+":8080/HXXa/ClothesListAndroidServlet";
		getList(url);
		
		return view;
	}
	//"http://10.40.5.4:8080/HXXa/ClothesListAndroidServlet?businessId=1
	public void getList(String url){
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("businessId",1+"");
		http.configHttpCacheSize(0);
		http.send(HttpMethod.GET, url,params,new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				Type type =	new TypeToken<HashMap<Integer,List<Price>>>() {  
                }.getType();
				Gson gson = new Gson();
				HashMap<Integer,List<Price>> temp=null;
				temp = gson.fromJson(arg0.result,type);
				Set<Integer> keyset = temp.keySet();
				Integer[] ctype = new Integer[temp.size()];
				int j =0;
				for(Integer key : keyset){
					ctype[j]=key;
					j++;
				}
				Arrays.sort(ctype);
			    leftStr = new String[ctype.length];
				for(int i =0;i<ctype.length;i++){
					leftStr[i]=ClothesType.valueOf(ctype[i]).getcTypeName();
				}
				List<List<Price>> prices = new ArrayList<List<Price>>();
				for(int i=0;i<ctype.length;i++){
					prices.add(temp.get(ctype[i]));
				}
				lvLeft.setAdapter(new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_expandable_list_item_1, leftStr));
				
				adapter =new RightAdapter(mactivity, leftStr, prices);
				lvRight.setAdapter(adapter);
			}
		});
	}
	
	
	class RightAdapter extends SectionedBaseAdapter{
		private Context mContext;
		private String[] leftStr;
		private List<List<Price>> rightObj;
		
		public RightAdapter(Context context, String[] leftStr, List<List<Price>> rightStr){
			this.mContext = context;
			this.leftStr = leftStr;
			this.rightObj = rightStr;
		}
		@Override
		public Object getItem(int section, int position) {
			// TODO Auto-generated method stub
			return rightObj.get(section).get(position);
		}

		@Override
		public long getItemId(int section, int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public int getSectionCount() {
			// 分类的个数
			return leftStr.length;
		}

		@Override
		public int getCountForSection(int section) {
			// RightList每个分类中的个数
			return rightObj.get(section).size();
		}

		@Override
		public View getItemView(int section, int position, View convertView,
				ViewGroup parent) {
			View v=null;
		if(convertView!=null){
			v=convertView;
		}else{
			 v=LayoutInflater.from(mContext).inflate(R.layout.item_business_details_header_list, parent,false);
			
		}
		TextView tv=(TextView) v.findViewById(R.id.tv_clothes_name);
		tv.setText(rightObj.get(section).get(position).getClothes().getClothesName());
		return v;
		}

		@Override
		public View getSectionHeaderView(int section, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View v=null;
		if(convertView!=null){
			v=convertView;
		}else{
			 v=LayoutInflater.from(mContext).inflate(R.layout.item_business_details_header, parent,false);
		}
			TextView tv = (TextView) v.findViewById(R.id.tv_header);
			tv.setText(leftStr[section]);
			v.setClickable(false);
			v.setBackgroundColor(Color.rgb(248, 248, 248));
			return v;
		}
		
	}
	
}
