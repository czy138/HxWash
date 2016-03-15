package com.gem.hxwasha;

import java.util.ArrayList;
import java.util.List;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.gem.fragment.IndexFragment;
import com.gem.fragment.MineFragment;
import com.gem.fragment.OrderFragment;

public class IndexActivity extends FragmentActivity implements OnPageChangeListener, OnCheckedChangeListener {
	ViewPager vp;
	RadioGroup rg;
	List<Fragment> fragments;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_index);
		fragments = new ArrayList<Fragment>();
		IndexFragment index = new IndexFragment();
		OrderFragment order = new OrderFragment();
		MineFragment mine = new MineFragment();
		fragments.add(index);
		fragments.add(order);
		fragments.add(mine);
		vp=(ViewPager) findViewById(R.id.index_content);
		rg=(RadioGroup) findViewById(R.id.group_index);
		vp.setAdapter(new IndexVpFrAdapter(getSupportFragmentManager()));
		vp.setOnPageChangeListener(this);
		rg.setOnCheckedChangeListener(this);
	}
	

	class IndexVpFrAdapter extends FragmentPagerAdapter{

		public IndexVpFrAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return fragments.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fragments.size();
		}
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		int item=-1;
		switch(checkedId){
		case R.id.rb_index:
			item=0;
			break;
		case R.id.rb_order:
			item=1;
			break;
		case R.id.rb_mine:
			item=2;
			break;
		}
		vp.setCurrentItem(item);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		((RadioButton)rg.getChildAt(arg0)).setChecked(true);
	}

}
