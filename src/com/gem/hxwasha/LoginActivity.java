package com.gem.hxwasha;

import java.lang.reflect.Type;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.gem.entity.User;
import com.gem.util.Content;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

public class LoginActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.login_input_phone_et)
	EditText inputPhoneEt;
	@ViewInject(R.id.login_input_code_et)
	EditText inputCodeEt;
	@ViewInject(R.id.login_request_code_btn)
	Button requestCodeBtn;
	@ViewInject(R.id.login_commit_btn)
	Button commitBtn;
	
	String phone;
	int i = 120;//����ʱ
	
	SharedPreferences sharedPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);
		sharedPreferences=getApplicationContext().getSharedPreferences("user",Context.MODE_PRIVATE);
		initSMS();
	}
	public void initSMS(){
		requestCodeBtn.setOnClickListener(this);
		commitBtn.setOnClickListener(this);

		EventHandler eventHandler = new EventHandler(){

			@Override
			public void afterEvent(int event, int result, Object data) {
				// TODO Auto-generated method stub
			     Message msg = new Message();  
	                msg.arg1 = event;  
	                msg.arg2 = result;  
	                msg.obj = data;  
	                handler.sendMessage(msg);
			}
			
		};
		
		SMSSDK.registerEventHandler(eventHandler);  
	}
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == -9) {
				requestCodeBtn.setText("���·���(" + i + ")");
			} else if (msg.what == -8) {
				requestCodeBtn.setText("��ȡ��֤��");
				requestCodeBtn.setClickable(true);
				i = 120;
			} else {
				int event = msg.arg1;
				int result = msg.arg2;
				Object data = msg.obj;
				Log.e("event", "event=" + event);
				if (result == SMSSDK.RESULT_COMPLETE) {
					// ����ע��ɹ��󣬷���MainActivity,Ȼ����ʾ
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// �ύ��֤��ɹ�
						Toast.makeText(getApplicationContext(), "�ύ��֤��ɹ�",
								Toast.LENGTH_SHORT).show();
//						Intent intent = new Intent(LoginActivity.this,
//								MainActivity.class);
//						startActivity(intent);
						
					} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
						Toast.makeText(getApplicationContext(), "��֤���Ѿ�����",
								Toast.LENGTH_SHORT).show();
					} else {
						((Throwable) data).printStackTrace();
					}
				}
			}
		}
	};
	
	public void login(){
		String url ="http://"+Content.getIp()+":8080/HXXa/UserLoginServlet";
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("phone",phone);
		http.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
//				Type type = new TypeToken<User>(){}.getType();
//				Gson gson = new Gson();
//				gson.fromJson(arg0.result, type);
			    Editor edit =sharedPreferences.edit();
		        edit.putString("loginUser", arg0.result);
		        edit.commit();
			}
		});
    
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String phoneNums = inputPhoneEt.getText().toString();
		phone =phoneNums;
		switch (v.getId()) {
		case R.id.login_request_code_btn:
			// 1. ͨ�������ж��ֻ���
			if (!judgePhoneNums(phoneNums)) {
				return;
			} // 2. ͨ��sdk���Ͷ�����֤
			SMSSDK.getVerificationCode("86", phoneNums);

			// 3. �Ѱ�ť��ɲ��ɵ����������ʾ����ʱ�����ڻ�ȡ��
			requestCodeBtn.setClickable(false);
			requestCodeBtn.setText("���·���(" + i + ")");
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (; i > 0; i--) {
						handler.sendEmptyMessage(-9);
						if (i <= 0) {
							break;
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					handler.sendEmptyMessage(-8);
				}
			}).start();
			break;

		case R.id.login_commit_btn:
			login();
			SMSSDK.submitVerificationCode("86", phoneNums, inputCodeEt
					.getText().toString());
			createProgressBar();
			break;
		}
	}
	
	
	
	private boolean judgePhoneNums(String phoneNums) {
		if (isMatchLength(phoneNums, 11)
				&& isMobileNO(phoneNums)) {
			return true;
		}
		Toast.makeText(this, "�ֻ�������������",Toast.LENGTH_SHORT).show();
		return false;
	}
	
	public static boolean isMatchLength(String str, int length) {
		if (str.isEmpty()) {
			return false;
		} else {
			return str.length() == length ? true : false;
		}
	}
	
	public static boolean isMobileNO(String mobileNums) {
		/*
		 * �ƶ���134��135��136��137��138��139��150��151��157(TD)��158��159��187��188
		 * ��ͨ��130��131��132��152��155��156��185��186 ���ţ�133��153��180��189����1349��ͨ��
		 * �ܽ��������ǵ�һλ�ض�Ϊ1���ڶ�λ�ض�Ϊ3��5��8������λ�õĿ���Ϊ0-9
		 */
		String telRegex = "[1][358]\\d{9}";// "[1]"�����1λΪ����1��"[358]"����ڶ�λ����Ϊ3��5��8�е�һ����"\\d{9}"��������ǿ�����0��9�����֣���9λ��
		if (TextUtils.isEmpty(mobileNums))
			return false;
		else
			return mobileNums.matches(telRegex);
	}

	private void createProgressBar() {
		FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;
		ProgressBar mProBar = new ProgressBar(this);
		mProBar.setLayoutParams(layoutParams);
		mProBar.setVisibility(View.VISIBLE);
		layout.addView(mProBar);
	}
	
	@Override
	protected void onDestroy() {
		SMSSDK.unregisterAllEventHandler();
		super.onDestroy();
	}
	
}
