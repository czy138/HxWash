package com.gem.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gem.entity.Address;
import com.gem.hxwasha.R;

public class AddressAdapter extends BaseAdapter {
	
	Context context;
	List<Address> ads;
	
	
	public AddressAdapter(Context context, List<Address> ads) {
		super();
		this.context = context;
		this.ads = ads;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ads.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return ads.get(position);
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
			convertView =LayoutInflater.from(context).inflate(R.layout.item_address_list,null);
			holder.tvCallName=(TextView) convertView.findViewById(R.id.tv_call_name);
			holder.tvPhone=(TextView) convertView.findViewById(R.id.tv_phone);
			holder.tvAddress=(TextView) convertView.findViewById(R.id.tv_address);
			convertView.setTag(holder);
		}
		holder.tvCallName.setText(ads.get(position).getCallName());
		holder.tvPhone.setText(ads.get(position).getUser().getTel());
		holder.tvAddress.setText(ads.get(position).getUserAddress());
		return convertView;
	}
class ViewHolder{
	
	TextView tvCallName;
	TextView tvPhone;
	TextView tvAddress;
}
	
}
