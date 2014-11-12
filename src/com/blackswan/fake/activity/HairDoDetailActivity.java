package com.blackswan.fake.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackswan.fake.R;
import com.blackswan.fake.base.BaseActivity;
import com.blackswan.fake.view.ProgressWheel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

public class HairDoDetailActivity extends BaseActivity implements
		OnTouchListener {
	protected final int PIC_NUM = 5;
	private TextView barber;
	private ImageView barberpic;
	// 完成图片消失的动画的图片
	private ImageView imageGone;
	private ViewPager mViewPager;
	private DisplayImageOptions options;
	private Button order;
	// 进度条
	private ProgressWheel mProgressWheel;
	// ImageSwitch
	private List<String> imgUrl = new ArrayList<String>();
	private int currentPosition = 0;
	private ImageView[] imgPage = new ImageView[PIC_NUM];
	// 小点点
	private ImageView[] drops = new ImageView[PIC_NUM];
	// 存放 上下两个相册的图片
	private ImageView[] cacheImage = new ImageView[2];
	/**
	 * 按下点的X坐标
	 */
	private float downY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hair_do_detail);
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		// 填入测试数据
		barber = (TextView) findViewById(R.id.tv_hairdo_detail_barber);
		barberpic = (ImageView) findViewById(R.id.iv_rotat_hairdo_detail);
		mViewPager = (ViewPager) findViewById(R.id.id_hairdo_detail_pic);
		order = (Button) findViewById(R.id.bt_hairdo_detail_order);
		mProgressWheel = (ProgressWheel) findViewById(R.id.id_hairdo_detail_progress);
		imageGone = (ImageView) findViewById(R.id.id_hairdo_detail_pic_cache);
		mProgressWheel.setVisibility(View.GONE);
		// imageGone.setVisibility(View.GONE);
		imageGone.setImageLevel(1);
		mViewPager.setOnTouchListener(this);
		cacheImage[0] = new ImageView(this);
		cacheImage[1] = new ImageView(this);
		this.initViews();
	}

	@Override
	protected void initViews() {
		imgUrl.add("http://h.hiphotos.baidu.com/image/pic/item/3b87e950352ac65c478e556ff8f2b21193138a26.jpg");
		imgUrl.add("http://g.hiphotos.baidu.com/image/pic/item/f31fbe096b63f624b55c6a738444ebf81a4ca3b9.jpg");
		imgUrl.add("http://e.hiphotos.baidu.com/image/pic/item/359b033b5bb5c9eae1d75710d739b6003af3b319.jpg");
		imgUrl.add("http://c.hiphotos.baidu.com/image/w%3D230/sign=beeba198a0cc7cd9fa2d33da09012104/0823dd54564e9258c34d3c9b9f82d158ccbf4e4d.jpg");
		
		for (int i = 0; i < imgUrl.size(); i++) {
			ImageView iv = new ImageView(this);
			iv.setImageLevel(0);
			imgPage[i] = iv;
		}
		barber.setText("11111");
		order.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 进行预约操作
			}
		});
		// 进行上下图片缓存
		ImageLoader
				.getInstance()
				.displayImage(
						"http://f.hiphotos.baidu.com/image/pic/item/ac4bd11373f0820287c2ab3c48fbfbedab641b64.jpg",
						cacheImage[0], options);

		ImageLoader
				.getInstance()
				.displayImage(
						"http://img0.bdstatic.com/img/image/shouye/mnnll-14204092064.jpg",
						barberpic, options);
		mViewPager.setAdapter(new PagerAdapter() {

			@Override
			public int getItemPosition(Object object) {
				return POSITION_NONE;
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return imgUrl.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(imgPage[position]);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {

				ImageLoader.getInstance().displayImage(imgUrl.get(position),
						imgPage[position], options, new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {

							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								mProgressWheel.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {

							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {

							}

						}, new ImageLoadingProgressListener() {

							@Override
							public void onProgressUpdate(String imageUri,
									View view, int current, int total) {
								if (current > 45 && current < 180) {
									mProgressWheel.setVisibility(View.VISIBLE);
								}
								mProgressWheel.setProgress(current * 360
										/ total);

							}
						});

				container.addView(imgPage[position]);
				return imgPage[position];

			}
		});
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

	}

	private void showNext() {
		imgUrl.clear();
		imgUrl.add("http://f.hiphotos.baidu.com/image/pic/item/ac4bd11373f0820287c2ab3c48fbfbedab641b64.jpg");
		imgUrl.add("http://d.hiphotos.baidu.com/image/pic/item/dc54564e9258d109207093b1d258ccbf6c814d25.jpg");
		imgUrl.add("http://d.hiphotos.baidu.com/image/pic/item/4afbfbedab64034f9b88f805acc379310a551d6f.jpg");
		imgUrl.add("http://a.hiphotos.baidu.com/image/pic/item/aa18972bd40735fa369774ba9d510fb30f240849.jpg");
		
		for (int i = 0; i < imgUrl.size(); i++) {
			ImageView iv = new ImageView(this);
			iv.setImageLevel(0);
			imgPage[i] = iv;
		}
		mViewPager.getAdapter().notifyDataSetChanged();
		mViewPager.setCurrentItem(0);
		mViewPager.setBackgroundColor(Color.BLACK);
		imageGone.setVisibility(View.GONE);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			// 手指按下的X坐标
			downY = event.getY();
			break;
		}
		case MotionEvent.ACTION_UP: {
			float lastY = event.getY();
			// 抬起的时候的X坐标大于按下的时候就显示上一张图片
			if (lastY > downY) {

			}

			else if ((downY - lastY) > mScreenHeight / 10) {
				// Log.d("", "上华中");
				AnimationSet mAnimationSet = new AnimationSet(false);
				TranslateAnimation translate = new TranslateAnimation(0, 0, 0,
						-mScreenHeight);
				AlphaAnimation alpha = new AlphaAnimation((float) 1.0,
						(float) 0.3);
				ScaleAnimation scale = new ScaleAnimation((float) 1.0,
						(float) 1.0, (float) 1.0, (float) 0.5);
				mAnimationSet.addAnimation(translate);
				mAnimationSet.addAnimation(alpha);
				mAnimationSet.addAnimation(scale);
				mAnimationSet.setDuration(500);

				imageGone.setImageDrawable(imgPage[mViewPager.getCurrentItem()]
						.getDrawable());
				imageGone.setVisibility(View.VISIBLE);
				imageGone.startAnimation(mAnimationSet);

				mViewPager.setBackgroundDrawable(cacheImage[0].getDrawable());
				mViewPager.removeAllViews();
				showNext();
			}
		}
		}
		return false;
	}
	private void initDrops(){
		//把5个小点点隐藏起来
//		int i;
//		for(i=0;i<PIC_NUM;i++){
//			drops[i]
//		}
	}
}
