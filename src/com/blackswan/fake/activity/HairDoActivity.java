package com.blackswan.fake.activity;

import java.util.ArrayList;
import java.util.List;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.blackswan.fake.R;
import com.blackswan.fake.base.BaseActivity;
import com.blackswan.fake.base.HairdoWaterfallFragment;


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
		
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
		{

			@Override
			public int getCount()
			{
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int arg0)
			{
				return mFragments.get(arg0);
			}
		};
		mViewPager.setAdapter(mAdapter);
		
//		mViewPager.setOnPageChangeListener(new OnPageChangeListener()
//		{
//
//			private int currentIndex;
//
//			@Override
//			public void onPageSelected(int position)
//			{
//				
//		
//
//				currentIndex = position;
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2)
//			{
//
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int arg0)
//			{
//			}
//		});
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initViews() {

		mTabBtnLongHair = (FrameLayout) findViewById(R.id.id_hairdo_tab_longhair);
		mTabBtnShortHair = (FrameLayout) findViewById(R.id.id_hairdo_tab_shorthair);
		mTabBtnMiddleHair = (FrameLayout) findViewById(R.id.id_hairdo_tab_middlehair);
		mTabBtnManHair = (FrameLayout) findViewById(R.id.id_hairdo_tab_manhair);
		
		ArrayList<String> imageList = new ArrayList<String>();
		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043531502.jpg");
		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043532264.jpg");
		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043533581.jpg");
		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043533571.jpg");
		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043534672.jpg");
		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043534854.jpg");
		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043535929.jpg");
		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043535784.jpg");
		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043536626.jpg");
		imageList.add("http://www.yjz9.com/uploadfile/2012/1219/20121219043536244.jpg");
		ArrayList<String> imageList2 = new ArrayList<String>();
		imageList2.add("http://g.hiphotos.baidu.com/image/pic/item/024f78f0f736afc397250d54b019ebc4b74512d1.jpg");
		imageList2.add("http://a.hiphotos.baidu.com/image/pic/item/fd039245d688d43f670949ff7f1ed21b0ef43b1f.jpg");
		imageList2.add("http://imgt6.bdstatic.com/it/u=2,838971799&fm=25&gp=0.jpg");
		imageList2.add("http://e.hiphotos.baidu.com/image/pic/item/0823dd54564e925895e4edec9e82d158cdbf4edb.jpg");
		imageList2.add("http://e.hiphotos.baidu.com/image/w%3D230/sign=ebd694fe72cf3bc7e800caefe102babd/43a7d933c895d14321948cd071f082025baf0766.jpg");
		imageList2.add("http://a.hiphotos.baidu.com/image/w%3D230/sign=1574a37722a446237ecaa261a8237246/faf2b2119313b07eb2d689050ed7912397dd8c50.jpg");
		imageList2.add("http://f.hiphotos.baidu.com/image/w%3D230/sign=5d2d573c259759ee4a5067c882fa434e/b7003af33a87e95060fdc05d13385343fbf2b41c.jpg");
		imageList2.add("http://f.hiphotos.baidu.com/image/w%3D230/sign=4f6d050978f40ad115e4c0e0672c1151/1b4c510fd9f9d72a254f222cd72a2834349bbbd3.jpg");
		imageList2.add("http://f.hiphotos.baidu.com/image/w%3D230/sign=30d5494b23a446237ecaa261a8237246/faf2b2119313b07e977763390fd7912397dd8c3c.jpg");
		imageList2.add("http://c.hiphotos.baidu.com/image/w%3D230/sign=c399a7ed0bfa513d51aa6bdd0d6d554c/37d3d539b6003af36891ee51362ac65c1038b6d3.jpg");
		ArrayList<String> imageList3 = new ArrayList<String>();
		imageList3.add("http://a.hiphotos.baidu.com/image/pic/item/cf1b9d16fdfaaf51ab6a81a98f5494eef01f7a1e.jpg");
		imageList3.add("http://f.hiphotos.baidu.com/image/pic/item/91529822720e0cf3f21c638f0946f21fbf09aade.jpg");
		imageList3.add("http://a.hiphotos.baidu.com/image/w%3D230/sign=6f067309cbfcc3ceb4c0ce30a245d6b7/4afbfbedab64034f50e63d2dacc379310a551df5.jpg");
		imageList3.add("http://h.hiphotos.baidu.com/image/w%3D230/sign=b4c85699d258ccbf1bbcb23929d8bcd4/a5c27d1ed21b0ef41cc36775dec451da81cb3ef5.jpg");
		imageList3.add("http://f.hiphotos.baidu.com/image/pic/item/f9dcd100baa1cd11a7efaa37ba12c8fcc3ce2d48.jpg");
		imageList3.add("http://b.hiphotos.baidu.com/image/w%3D230/sign=b870d426223fb80e0cd166d406d02ffb/b2de9c82d158ccbfd5186d2d1ad8bc3eb1354105.jpg");
		imageList3.add("http://h.hiphotos.baidu.com/image/pic/item/18d8bc3eb13533fa3d124b39abd3fd1f41345b05.jpg");
		imageList3.add("http://g.hiphotos.baidu.com/image/pic/item/77c6a7efce1b9d1663294276f0deb48f8c54643f.jpg");
		imageList3.add("http://e.hiphotos.baidu.com/image/w%3D230/sign=5d51dedd818ba61edfeecf2c713497cc/80cb39dbb6fd52663c72e403a818972bd4073648.jpg");
		
		mFragments.add(new HairdoWaterfallFragment(imageList));
		mFragments.add(new HairdoWaterfallFragment(imageList2));
		mFragments.add(new HairdoWaterfallFragment(imageList2));
		mFragments.add(new HairdoWaterfallFragment(imageList3));
		
//		mFragments.add(tab02);
//		mFragments.add(tab03);
//		mFragments.add(tab04);

	}

}
