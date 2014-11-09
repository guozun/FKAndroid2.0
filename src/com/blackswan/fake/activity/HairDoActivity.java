package com.blackswan.fake.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blackswan.fake.R;
import com.blackswan.fake.base.BaseActivity;


public class HairDoActivity extends BaseActivity {
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragments = new ArrayList<Fragment>();

	/**
	 * bar四个按钮
	 */
	private FrameLayout mTabBtnLongHair;
	private FrameLayout mTabBtnShortHair;
	private FrameLayout mTabBtnMiddleHair;
	private FrameLayout mTabBtnManHair;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hairdo);

		mViewPager = (ViewPager) findViewById(R.id.id_hairdo_waterfall);
		this.initViews();
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initViews() {

//		mTabBtnLongHair = (FrameLayout) findViewById(R.id.id_hairdo_tab_longhair);
//		mTabBtnShortHair = (FrameLayout) findViewById(R.id.id_hairdo_tab_shorthair);
//		mTabBtnMiddleHair = (FrameLayout) findViewById(R.id.id_hairdo_tab_middlehair);
//		mTabBtnManHair = (FrameLayout) findViewById(R.id.id_hairdo_tab_manhair);
//
//		MainTab01 tab01 = new MainTab01();
//		MainTab02 tab02 = new MainTab02();
//		MainTab03 tab03 = new MainTab03();
//		MainTab04 tab04 = new MainTab04();
//		mFragments.add(tab01);
//		mFragments.add(tab02);
//		mFragments.add(tab03);
//		mFragments.add(tab04);

	}

}
