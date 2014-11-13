package com.blackswan.fake.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blackswan.fake.R;
import com.blackswan.fake.activity.useractivity.LoginActivity;
import com.blackswan.fake.activity.useractivity.MyCollectActivity;
import com.blackswan.fake.activity.useractivity.PersonalInfoActivity;
import com.blackswan.fake.base.BaseActivity;
import com.blackswan.fake.bean.UserInfo;

public class MyPageActivity extends BaseActivity {
	// 用户信息
	private UserInfo userInfo = new UserInfo();
	// 我的头像
	private ImageView myHeadImageView;
	// 我的昵称栏
	private TextView myName;
	private TextView myDisp;
	private ImageView mySex;
	// 我的收藏
	private Button myCollect;
	// 我的话题
	private Button myTopic;
	//
	private TextView myHairShow;
	// 设置
	private ImageButton settingImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (userInfo != null) {
			setContentView(R.layout.activity_mypage);
		} else {
			setContentView(R.layout.activity_mypage_nologin);
		}
		initControl();
		
		
	}

	/**
	 * 初始化控件
	 */
	public void initControl() {
		if (userInfo != null) {
			myHeadImageView = (ImageView) findViewById(R.id.iv_mypage_head);
			myName = (TextView) findViewById(R.id.tv_mypage_name);
			myDisp = (TextView) findViewById(R.id.tv_mypage_disp);
			mySex = (ImageView) findViewById(R.id.iv_mypage_sex);
			myCollect = (Button) findViewById(R.id.bt_mypage_collect);
			myTopic = (Button) findViewById(R.id.bt_mypage_topic);
			myHairShow = (TextView) findViewById(R.id.tv_mypage_hairshow);
			settingImageView = (ImageButton) findViewById(R.id.ib_mypage_detail);
			//setLisentener();
			initViews();
		} else {
			Button login = (Button) findViewById(R.id.mypage_nologin_login);
			login.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//去登陆
					startActivity(LoginActivity.class);
				}
			});
		}
	}

	/**
	 * 为各个控件设置监听
	 */
	private void setLisentener() {
		myHeadImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 弹出修改头像的界面
				
			}
		});
		myName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断是否登入，未登入跳转登入界面
				if (userInfo == null) {
					startActivity(LoginActivity.class);
				} else {
					startActivity(MyPageActivity.class);
				}
			}
		});
		settingImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(MyPageActivity.class);
			}
		});
		myTopic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断是否登入，未登入跳转登入界面
				if (userInfo == null) {
					startActivity(LoginActivity.class);
				} else {
					// 若已登入，我的预约详细信息刷新显示
				}

			}
		});
		// 收藏夹
		myCollect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断是否登入，为登入跳转到登入页面
				if (userInfo == null) {
					startActivity(LoginActivity.class);
				} else {
					startActivity(MyCollectActivity.class);
				}
			}
		});
	}

	@Override
	protected void initViews() {
		myHeadImageView.setImageResource(R.drawable.head);
		myName.setText("名字");
		myDisp.setText("简介");
		
		mySex.setImageResource(R.drawable.bg_famale);
		myCollect.setText("我的收藏 100");
		myTopic.setText("我的主题 100");
		myHairShow.setText("我的发型秀 100");
	}

	@Override
	protected void initEvents() {

	}

}
