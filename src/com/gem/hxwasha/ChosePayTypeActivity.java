package com.gem.hxwasha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ChosePayTypeActivity extends Activity implements OnCheckedChangeListener {
	
	int item=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chose_pay_type);
		ViewUtils.inject(this);
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		
	}
	public void onclick(View v){
		Intent intent = new Intent();
		switch(item){
		case 0:
			break;
		case 1:
			break;
		}
		startActivity(intent);
	}

}
