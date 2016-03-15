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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.gem.fragment.AssessFragment;
import com.gem.fragment.BusinessDetailsFragment;
import com.gem.fragment.BusinessClothesFragment;

public class BusinessDetailsActivity extends FragmentActivity implements OnPageChangeListener, OnCheckedChangeListener {
	ViewPager vp;
	RadioGroup rg;
	List<Fragment> fragments;
	
	AssessFragment assessFragment;
	BusinessDetailsFragment detailsFragment;
	BusinessClothesFragment orderClothesFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business_details);
		vp=(ViewPager) findViewById(R.id.content);
		rg=(RadioGroup) findViewById(R.id.group);
		fragments= new ArrayList<Fragment>();
		assessFragment = new AssessFragment();
		detailsFragment = new BusinessDetailsFragment();
		orderClothesFragment= new BusinessClothesFragment();
		fragments.add(orderClothesFragment);
		fragments.add(assessFragment);
		fragments.add(detailsFragment);
		
		vp.setAdapter(new BusinessDetailsFrVpAdapter(getSupportFragmentManager()));
		vp.setOnPageChangeListener(this);
		rg.setOnCheckedChangeListener(this);
	}
	class BusinessDetailsFrVpAdapter extends FragmentPagerAdapter{

		public BusinessDetailsFrVpAdapter(FragmentManager fm) {
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.business_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		int item=-1;
		switch(checkedId){
		case R.id.order_clothes:
			item=0;
			break;
		case R.id.assess:
			item=1;
			break;
		case R.id.details:
			item=2;
			break;
		}
		vp.setCurrentItem(item);
	}
}
