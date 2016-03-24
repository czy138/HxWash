package com.gem.fragment;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gem.adapter.SectionedBaseAdapter;
import com.gem.entity.Business;
import com.gem.entity.ClothesType;
import com.gem.entity.OrderClothes;
import com.gem.entity.Orders;
import com.gem.entity.Price;
import com.gem.hxwasha.ConfirmOrderActivity;
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

public class BusinessClothesFragment extends Fragment {
	
	private boolean isScroll = true;
	ListView lvLeft;
	ListView lvRight;
	String[] leftStr;
	Activity mactivity;

	RightAdapter adapter;

	Button btnBalance;// 跳转按钮
	TextView tvAllPrice;// 总价
	private double allPrice;
	//衣物订单
	OrderClothes orderClothes;
	List<OrderClothes> ocList;

	Orders orders ;
	
	Map<Map<Integer,Integer>, OrderClothes> mapOrderClothes ;
	Map<Price,Integer> mapPrice = new HashMap<Price,Integer>();
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mactivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_business_clothes, container, false);
		lvLeft = (ListView) view.findViewById(R.id.lv_left);
		lvRight = (ListView) view.findViewById(R.id.lv_right);

		lvLeft.setBackgroundColor(Color.rgb(248, 248, 248));
		lvLeft.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				isScroll = false;

				for (int i = 0; i < lvLeft.getChildCount(); i++) {
					if (i == position) {
						lvLeft.getChildAt(i).setBackgroundColor(Color.rgb(255, 255, 255));
					} else {
						lvLeft.getChildAt(i).setBackgroundColor(Color.rgb(248, 248, 248));
					}
				}
				int rightSection = 0;
				for (int i = 0; i < position; i++) {
					rightSection += adapter.getCountForSection(i) + 1;
				}
				lvRight.setSelection(rightSection);
			}
		});
		lvRight.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (isScroll) {
					for (int i = 0; i < lvLeft.getChildCount(); i++) {
						if (i == adapter.getSectionForPosition(firstVisibleItem)) {
							lvLeft.getChildAt(i).setBackgroundColor(Color.rgb(255, 255, 255));
						} else {
							lvLeft.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
						}
					}
				} else {
					isScroll = true;
				}
			}
		});
		String url = "http://" + Content.getIp() + ":8080/HXXa/ClothesListAndroidServlet";
		getList(url);
		return view;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		tvAllPrice = (TextView) getView().findViewById(R.id.tv_all_price);
		btnBalance = (Button) getView().findViewById(R.id.btn_balance);

		btnBalance.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				方案二：年月日（6-8位，年份的前两位可以忽略）+类型标记（2位）+ 内部流水（自增+1）；
//				优点：意义明确，且有4-6位的流水，可以满足一般应用；缺点：不适合大型应用
				if(allPrice>0){
				Date date = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
				String time =dateFormat.format(date);
				
				
				orders = new Orders();
				Business business = new Business();
				
				//从上个界面获取的商铺ID,需要修改
				business.setBusinessId(1);
				
				orders.setOrderId(Integer.valueOf(time));
				orders.setBusiness(business);
				
				Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
						.setPrettyPrinting()
						.disableHtmlEscaping().create();
				
				String sPrices = gson.toJson(mapPrice);
				
				
				
				
				//Log.i("TTT", sPrices);
				Bundle bundle = new Bundle();
				bundle.putSerializable("orders", orders);
				bundle.putString("Map",sPrices);
				bundle.putDouble("allPrice", allPrice);
				
				Intent intent = new Intent(getActivity(), ConfirmOrderActivity.class);
				intent.putExtras(bundle);
				getActivity().startActivity(intent);
			}else{
				Toast.makeText(getActivity(), "请选择商品", 1).show();
			}
				
				
			}
		});
	}

	// "http://10.40.5.4:8080/HXXa/ClothesListAndroidServlet?businessId=1
	public void getList(String url) {
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("businessId", 1 + "");
		http.configHttpCacheSize(0);
		http.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				Type type = new TypeToken<HashMap<Integer, List<Price>>>() {
				}.getType();
				Gson gson = new Gson();
				HashMap<Integer, List<Price>> temp = null;
				temp = gson.fromJson(arg0.result, type);
				Set<Integer> keyset = temp.keySet();
				Integer[] ctype = new Integer[temp.size()];
				int j = 0;
				for (Integer key : keyset) {
					ctype[j] = key;
					j++;
				}
				Arrays.sort(ctype);
				leftStr = new String[ctype.length];
				for (int i = 0; i < ctype.length; i++) {
					leftStr[i] = ClothesType.valueOf(ctype[i]).getcTypeName();
				}
				List<List<Price>> prices = new ArrayList<List<Price>>();
				for (int i = 0; i < ctype.length; i++) {
					prices.add(temp.get(ctype[i]));
				}
				lvLeft.setAdapter(new ArrayAdapter<String>(getActivity(),
						android.R.layout.simple_expandable_list_item_1, leftStr));

				adapter = new RightAdapter(mactivity, leftStr, prices);
				lvRight.setAdapter(adapter);
			}
		});

	}

	class RightAdapter extends SectionedBaseAdapter {

		private Context mContext;
		private String[] leftStr;
		private List<List<Price>> rightObj;
		
		Map<Map<Integer, Integer>, Integer> countsMap = new HashMap<Map<Integer, Integer>, Integer>();
		
		
		
		Map<Integer, Integer> mapKey;
		
		TextView tvName;
		TextView tvPrice;
		TextView tvWashType;
		TextView tvMaxTime;
		TextView tvUrgent;
		ImageView ivAdd;
		ImageView ivSub;
		TextView tvNum;

		public RightAdapter(Context context, String[] leftStr, List<List<Price>> rightStr) {
			this.mContext = context;
			this.leftStr = leftStr;
			this.rightObj = rightStr;
		}

		@Override
		public Object getItem(int section, int position) {
			return rightObj.get(section).get(position);
		}

		@Override
		public long getItemId(int section, int position) {
			return position;
		}

		@Override
		public int getSectionCount() {
			return leftStr.length;
		}

		@Override
		public int getCountForSection(int section) {
			return rightObj.get(section).size();
		}

		@Override
		public View getItemView(int section, int position, View convertView, ViewGroup parent) {
		
			View cv = null;
			mapKey = new HashMap<Integer, Integer>();
			mapKey.put(section, position);
			
			if (convertView != null) {
				cv = convertView;
			} else {
				cv = LayoutInflater.from(mContext).inflate(R.layout.item_business_details_header_list, parent, false);
			}
			tvName = (TextView) cv.findViewById(R.id.tv_clothes_name);
			tvPrice = (TextView) cv.findViewById(R.id.tv_clothes_price);
			tvWashType = (TextView) cv.findViewById(R.id.tv_wash_type);
			tvMaxTime = (TextView) cv.findViewById(R.id.tv_max_time);
			tvUrgent = (TextView) cv.findViewById(R.id.tv_urgent);
			ivAdd = (ImageView) cv.findViewById(R.id.iv_add);
			ivSub = (ImageView) cv.findViewById(R.id.iv_sub);
			tvNum = (TextView) cv.findViewById(R.id.tv_num);
			// 标记
			ivAdd.setTag(mapKey);
			ivSub.setTag(mapKey);
			
			ivAdd.setOnClickListener(new OnClickChange(section,position));
			ivSub.setOnClickListener(new OnClickChange(section,position));
			
			tvName.setText(rightObj.get(section).get(position).getClothes().getClothesName());
			tvPrice.setText( "￥"+rightObj.get(section).get(position).getPrice());
			tvWashType.setText("(" + rightObj.get(section).get(position).getWashType().getWashType() + ")");
			tvMaxTime.setText(rightObj.get(section).get(position).geteTime() + "天");
			if(rightObj.get(section).get(position).getUrgent()==1){
			tvUrgent.setText("  急");
			}else{
				tvUrgent.setText("");
			}
			if (countsMap.containsKey(mapKey)) {
				tvNum.setText(countsMap.get(mapKey) + "");
			} else {
				countsMap.put(mapKey, 0);
				tvNum.setText("0");
			}
			return cv;
		}
		class OnClickChange implements OnClickListener {
			int section;
			int position;
			int number = 0;
			
			public OnClickChange(int section,int position) {
				this.section = section;
				this.position = position;
			}
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.iv_add:
					Map<Integer, Integer> mapKey = (Map<Integer, Integer>) v.getTag();
					Integer count = countsMap.get(mapKey) + 1;
					countsMap.put(mapKey, count);
					TextView tv = (TextView) (((RelativeLayout) v.getParent()).findViewById(R.id.tv_num));
					tv.setText(count + "");
					allPrice += rightObj.get(section).get(position).getPrice();	
					number = count;
					break;
				case R.id.iv_sub:
					Map<Integer, Integer> mapKey1 = (Map<Integer, Integer>) v.getTag();
					Integer count1 = countsMap.get(mapKey1);
					if (count1 > 0) {
						count1--;
						countsMap.put(mapKey1, count1);
						TextView tv1 = (TextView) (((RelativeLayout) v.getParent()).findViewById(R.id.tv_num));
						tv1.setText(count1 + "");
						allPrice -= rightObj.get(section).get(position).getPrice();	
						number = count1;
						break;
					}
				}
				if(number==0){
					mapPrice.remove(rightObj.get(section).get(position).getPriceId());
				}else{
					mapPrice.put(rightObj.get(section).get(position), number);
				}	
				tvAllPrice.setText(allPrice + "￥");
			}
		}
		@Override
		public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
			View v = null;
			if (convertView != null) {
				v = convertView;
			} else {
				v = LayoutInflater.from(mContext).inflate(R.layout.item_business_details_header, parent, false);
			}
			TextView tv = (TextView) v.findViewById(R.id.tv_header);
			tv.setText(leftStr[section]);
			v.setClickable(false);
			v.setBackgroundColor(Color.rgb(248, 248, 248));
			return v;
		}
	}
}
