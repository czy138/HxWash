package com.gem.hxwasha;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ChosePayTypeActivity extends Activity implements OnCheckedChangeListener {
	@ViewInject(R.id.group_pay)
	RadioGroup rg;
	
	int item=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chose_pay_type);
		ViewUtils.inject(this);
		rg.setOnCheckedChangeListener(this);
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		
		switch(checkedId){
		case R.id.rb_pay_acount:
			item=0;
			break;
		case R.id.rb_pay_zfb:
			item=1;
			break;
		}
	}


}
