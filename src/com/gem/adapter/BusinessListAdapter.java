package com.gem.adapter;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gem.entity.Business;
import com.gem.entity.User;
import com.gem.hxwasha.R;
import com.gem.util.Content;
import com.gem.util.ImageUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class BusinessListAdapter extends BaseAdapter {
	List<Business> bs;
	Context context;
	ImageUtil imageUtil;
	public BusinessListAdapter(List<Business> bs,Context context){
		this.bs=bs;
		this.context=context;
		imageUtil=new ImageUtil(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bs.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return bs.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}   

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();
		if(convertView!=null){
			holder=(ViewHolder) convertView.getTag();
		}else{
			convertView =LayoutInflater.from(context).inflate(R.layout.item_business_list,null);
			holder.ratingBar=(RatingBar) convertView.findViewById(R.id.rb_star);
			holder.img=(ImageView) convertView.findViewById(R.id.iv_image);
			holder.tvShopName=(TextView) convertView.findViewById(R.id.tv_shopname);
			holder.tvMoney=(TextView) convertView.findViewById(R.id.tv_money_send);
			holder.tvBagWash=(TextView) convertView.findViewById(R.id.tv_bagwash_text);
			holder.tvUrgent =(TextView) convertView.findViewById(R.id.tv_urgent_text);
			holder.ivBag = (ImageView) convertView.findViewById(R.id.tv_bag);
			holder.ivUrgent = (ImageView) convertView.findViewById(R.id.tv_urgent);
			convertView.setTag(holder); 
		}
		holder.tvShopName.setText(bs.get(position).getShopName());
		hideShow(holder,position);
		holder.tvMoney.setText(sendMoney(position));
		String url ="http://"+Content.getIp()+":8080/"+bs.get(position).getImgesUrl();
		imageUtil.getImage(url,holder.img);	
		return convertView;
	}

	public void hideShow(ViewHolder holder,int position){
		double bag = bs.get(position).getBagWash();
		int urgent = bs.get(position).getIsurgent();
		if(urgent==0){
			holder.tvUrgent.setVisibility(View.INVISIBLE);
			holder.ivUrgent.setVisibility(View.INVISIBLE);
		}else{
			holder.tvUrgent.setVisibility(View.VISIBLE);
			holder.ivUrgent.setVisibility(View.VISIBLE);
		}
		if(bag>0){
			holder.tvBagWash.setVisibility(View.VISIBLE);
			holder.ivBag.setVisibility(View.VISIBLE);
		}else{
			holder.tvBagWash.setVisibility(View.INVISIBLE);
			holder.ivBag.setVisibility(View.INVISIBLE);
		}
	}
	public String sendMoney(int position){
		double lat1 = bs.get(position).getLat();
		double lng1 = bs.get(position).getLng();
		SharedPreferences locationSave=context.getApplicationContext().getSharedPreferences("Location",Context.MODE_PRIVATE);
		double lat2 =Double.parseDouble(locationSave.getString("lat", "0"));
		double lng2 =Double.parseDouble(locationSave.getString("lng", "0"));
		double distance =GetDistance(lat1,lng1,lat2,lng2);
		Log.i("distance", distance+""+"lat"+lat2+"lng"+lng2);
		double money=0;
		if(distance>500&&distance<=870){
			money=5;
		}else if(distance>870&&distance<=1870){
			money=10;
		}else if(distance>1870&&distance<=5000){
			money=30;
		}
		return String.valueOf((int)money);
	}
	
	 
	private static double rad(double d)
	{
	   return d * Math.PI / 180.0;
	}

	public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
	{
	   double radLat1 = rad(lat1);
	   double radLat2 = rad(lat2);
	   double a = radLat1 - radLat2;
	   double b = rad(lng1) - rad(lng2);
	   double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
	    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	   s = s * Content.EARTH_RADIUS;
	   s = Math.round(s * 10000) / 10000;
	   return s;
	}
class ViewHolder{
	ImageView img;
	ImageView ivBag;
	ImageView ivUrgent;
	RatingBar ratingBar;
	
	TextView tvShopName;
	TextView tvBagWash;
	TextView tvUrgent;
	TextView tvMoney;
}





}
