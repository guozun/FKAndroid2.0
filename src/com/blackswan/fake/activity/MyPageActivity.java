package com.blackswan.fake.activity;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.IvParameterSpec;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.style.BulletSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blackswan.fake.R;
import com.blackswan.fake.activity.useractivity.AlterSexActivity;
import com.blackswan.fake.activity.useractivity.LoginActivity;
import com.blackswan.fake.activity.useractivity.MyCollectActivity;
import com.blackswan.fake.activity.useractivity.PersonalInfoActivity;
import com.blackswan.fake.adapter.NowOrderListFragment;
import com.blackswan.fake.adapter.ToEvaluateFragment;
import com.blackswan.fake.base.BaseActivity;
import com.blackswan.fake.bean.UserInfo;
import com.blackswan.fake.util.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class MyPageActivity extends BaseActivity {
	private DisplayImageOptions options;
	// 用户信息
	private UserInfo userInfo = new UserInfo("name", "183139087908", "123",
			"简介",
			"http://img0.bdstatic.com/img/image/shouye/mxlyfs-9632102318.jpg",
			true);
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
	private ViewPager mViewPager;
	// 游标
	private ImageView cursor;
	private int currIndex = 0;
	private FragmentPagerAdapter mFragmentPagerAdapter;
	private List<Fragment> mFragments = new ArrayList<Fragment>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
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
			mViewPager = (ViewPager) findViewById(R.id.id_mypager_viewpager);
			cursor = new ImageView(this);
			setLisentener();
			initViews();
		} else {
			Button login = (Button) findViewById(R.id.mypage_nologin_login);
			login.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// 去登陆
					startActivity(LoginActivity.class);
				}
			});
		}
	}

	/**
	 * 为各个控件设置监听
	 */
	private void setLisentener() {
		settingImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putSerializable("personinfo", userInfo);
				startActivity(PersonalInfoActivity.class, b);
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
					startActivity(AlterSexActivity.class);
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

		ImageLoader.getInstance().displayImage(userInfo.getHeadUrl(),
				myHeadImageView, options);
		myName.setText(userInfo.getName());
		myDisp.setText(userInfo.getDisp());

		mySex.setImageResource(R.drawable.bg_famale);
		myCollect.setText("我的收藏 100");
		myTopic.setText("我的主题 100");
		myHairShow.setText("我的发型秀 100");

		mFragments.add(new NowOrderListFragment());
		mFragments.add(new ToEvaluateFragment());
		mFragmentPagerAdapter = new FragmentPagerAdapter(
				getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return mFragments.get(arg0);
			}
		};
		mViewPager.setAdapter(mFragmentPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						// 初始化移动的动画（从当前位置，x平移到即将要到的位置）
						Animation animation = new TranslateAnimation(currIndex
								* mScreenWidth / 3, arg0 * mScreenWidth / 3, 0,
								0);
						currIndex = arg0;
						animation.setFillAfter(true); // 动画终止时停留在最后一帧，不然会回到没有执行前的状态
						animation.setDuration(200); // 动画持续时间，0.2秒
						cursor.startAnimation(animation); // 是用imageview来显示动画
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {

					}

					@Override
					public void onPageScrollStateChanged(int arg0) {

					}
				});

		RelativeLayout rlayout = (RelativeLayout) findViewById(R.id.id_mypage_tab);
		cursor.setImageResource(R.drawable.bg_cursor);

		// RelativeLayout.LayoutParams parms = new
		// RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(
				mScreenWidth / 3, Utility.Dp2Px(this, 5));
		parms.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		parms.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		rlayout.addView(cursor, parms);
	}

	@Override
	protected void initEvents() {

	}

}
