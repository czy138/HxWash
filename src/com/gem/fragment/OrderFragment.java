package com.gem.fragment;

import com.gem.hxwasha.R;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class OrderFragment extends Fragment implements OnClickListener {
	TextView tvClothes;
	TextView tvGoods;
	Fragment curFragment;
	OrderGoodsOrderFragment orderGoodsOrderFragment;
	OrderClothesOrderFragment orderClothesOrderFragment;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	
		View v = inflater.inflate(R.layout.fragment_index_order, null);
		tvClothes = (TextView) v.findViewById(R.id.tv_clothes_order);
		tvGoods = (TextView) v.findViewById(R.id.tv_goods_order);
		tvClothes.setOnClickListener(this);
		tvGoods.setOnClickListener(this);
		tvClothes.setBackgroundColor(Color.rgb(228, 228, 228));
		
		 orderGoodsOrderFragment =new OrderGoodsOrderFragment();
		 orderClothesOrderFragment=new OrderClothesOrderFragment();
		 FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		 ft.add(R.id.frame_order, orderClothesOrderFragment).commit();
		 curFragment=orderClothesOrderFragment;
		return v;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.tv_clothes_order:
			tvGoods.setBackgroundColor(Color.WHITE);
			tvClothes.setBackgroundColor(Color.rgb(228, 228, 228));

			fragmentToggle(orderClothesOrderFragment);
			break;
		case R.id.tv_goods_order:
			tvGoods.setBackgroundColor(Color.rgb(228, 228, 228));
			tvClothes.setBackgroundColor(Color.WHITE);

			fragmentToggle(orderGoodsOrderFragment);
			break;
		}
		
	}
	public void fragmentToggle(Fragment nextFragment){
		 if(curFragment!=nextFragment){
			 FragmentTransaction ft = getChildFragmentManager().beginTransaction();
			 if(!nextFragment.isAdded()){
				 ft.hide(curFragment);
				 ft.add(R.id.frame_order, nextFragment);
			 }else{
				 ft.hide(curFragment);
				 ft.show(nextFragment);
			 }
			 ft.commit();
		 	}
		 curFragment=nextFragment;
		}
	
	
	
	
}
