package com.gem.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gem.bean.GoodsOrder;
import com.gem.hxwasha.R;
import com.gem.util.CommonAdapter;
import com.gem.util.ViewHolder;

public class OrderGoodsOrderFragment extends Fragment{
	ListView lvOrder;
	List<GoodsOrder> lists=new ArrayList<GoodsOrder>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
				View v =inflater.inflate(R.layout.fragment_order_goods_order, container,false);
						return v;
	}
	
	public void initView() {
		lvOrder=(ListView) getView().findViewById(R.id.lv_order_goods);
	}
	
	public void initData() {
		GoodsOrder goods=new GoodsOrder("均码", 49, "韩版冬款加绒女士五指触屏骑车羊手套", "浅灰色");
		GoodsOrder goods2=new GoodsOrder("均码", 49, "韩版冬款加绒女士五指触屏骑车羊手套", "浅灰色");
		GoodsOrder goods3=new GoodsOrder("均码", 49, "韩版冬款加绒女士五指触屏骑车羊手套", "浅灰色");
		GoodsOrder goods4=new GoodsOrder("均码", 49, "韩版冬款加绒女士五指触屏骑车羊手套", "浅灰色");
		
		lists.add(goods);
		lists.add(goods2);
		lists.add(goods3);
		lists.add(goods4);
	}
	
	public void initEvent() {
		lvOrder.setAdapter(new CommonAdapter<GoodsOrder>(getActivity(),lists,R.layout.order_goods_listview_item) {

			@Override
			public void convert(ViewHolder helper, GoodsOrder item) {
				// TODO Auto-generated method stub
				helper.setText(R.id.tv_order_goods_size, item.getSize())
				.setText(R.id.tv_order_goods_price, String.valueOf(item.getPrice()))
				.setText(R.id.tv_order_goods_name, item.getName())
				.setText(R.id.tv_order_goods_color, item.getColor());
			}
		});
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();
		initEvent();
	}
}
