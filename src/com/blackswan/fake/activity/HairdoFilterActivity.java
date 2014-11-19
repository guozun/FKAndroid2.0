package com.blackswan.fake.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import com.blackswan.fake.R;
import com.blackswan.fake.adapter.HairdoHairFilterAdapter;
import com.blackswan.fake.base.BaseActivity;
import com.blackswan.fake.util.Utility;

public class HairdoFilterActivity extends BaseActivity {
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Window window = getWindow();
		android.view.WindowManager.LayoutParams windowLayoutParams = window
				.getAttributes(); // 获取对话框当前的参数值
		// windowLayoutParams.width = (int) (mScreenWidth ); //
		// windowLayoutParams.height = (int) (mScreenHeight)
		// - Utility.Dp2Px(this, 50f)-statusBarHeight; //
		windowLayoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
		window.setAttributes(windowLayoutParams);
		setContentView(R.layout.hairdo_filter_popupwindow);
		this.initViews();
	}

	@Override
	protected void initViews() {
		ViewPager mViewPager = (ViewPager) findViewById(R.id.id_hairdo_filter_viewpager);
		mViewPager.setAdapter(new HairdoHairFilterAdapter(
				getSupportFragmentManager()));
		findViewById(R.id.id_hairdo_filter_commit).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						defaultFinish();
					}
				});
		findViewById(R.id.hairdo_filter_block_view).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						defaultFinish();
					}
				});
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

	}

}
