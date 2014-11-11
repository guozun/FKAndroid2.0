package com.blackswan.fake.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.blackswan.fake.R;
import com.blackswan.fake.base.BaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class HairDoDetailActivity extends BaseActivity implements ViewFactory,
		OnTouchListener {

	private TextView barber;
	private ImageView barberpic;
	private ImageSwitcher mImageSwitcher;
	private DisplayImageOptions options;
	private Button order;
	// ImageSwitch
	private List<String> imgUrl = new ArrayList<String>();
	private int currentPosition = 0;
	/**
	 * 按下点的X坐标
	 */
	private float downX;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hair_do_detail);
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.homepage_location)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		// 填入测试数据
		imgUrl.add("http://g.hiphotos.baidu.com/image/pic/item/f31fbe096b63f624b55c6a738444ebf81a4ca3b9.jpg");
		imgUrl.add("http://e.hiphotos.baidu.com/image/pic/item/359b033b5bb5c9eae1d75710d739b6003af3b319.jpg");
		imgUrl.add("http://c.hiphotos.baidu.com/image/w%3D230/sign=beeba198a0cc7cd9fa2d33da09012104/0823dd54564e9258c34d3c9b9f82d158ccbf4e4d.jpg");
		this.initViews();
	}

	@Override
	protected void initViews() {
		barber = (TextView) findViewById(R.id.tv_hairdo_detail_barber);
		barberpic = (ImageView) findViewById(R.id.iv_rotat_hairdo_detail);
		mImageSwitcher = (ImageSwitcher) findViewById(R.id.is_hairdo_detail_pic);
		order = (Button) findViewById(R.id.bt_hairdo_detail_order);

		barber.setText("11111");
		order.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 进行预约操作
			}
		});
		ImageLoader.getInstance().displayImage("http://img0.bdstatic.com/img/image/shouye/mnnll-14204092064.jpg",
				barberpic, options);
		mImageSwitcher.setFactory(this);
		mImageSwitcher.setOnTouchListener(this);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	public View makeView() {
		final ImageView i = new ImageView(this);
		i.setScaleType(ScaleType.CENTER_CROP);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT)); 
		return i;
	}

	private void  showNext(){
		ImageView i =mImageSwitcher.getBackground().;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			// 手指按下的X坐标
			downX = event.getX();
			break;
		}
		case MotionEvent.ACTION_UP: {
			float lastX = event.getX();
			// 抬起的时候的X坐标大于按下的时候就显示上一张图片
			if (lastX > downX) {
				if (currentPosition > 0) {
					// 设置动画，这里的动画比较简单，不明白的去网上看看相关内容
					TranslateAnimation leftInAnimation = new TranslateAnimation(
							-100, 0, 0, 0);
					leftInAnimation.setDuration(500);
					mImageSwitcher.setInAnimation(leftInAnimation);
					TranslateAnimation RightOutAnimation = new TranslateAnimation(
							0, 100, 0, 0);
					RightOutAnimation.setDuration(500);
					mImageSwitcher.setOutAnimation(RightOutAnimation);
					currentPosition--;
					mImageSwitcher.showPrevious();
				} else {
					Toast.makeText(getApplication(), "已经是第一张",
							Toast.LENGTH_SHORT).show();
				}
			}

			if (lastX < downX) {
				if (currentPosition < imgUrl.size() - 1) {
					TranslateAnimation rightInAnimation = new TranslateAnimation(
							100, 0, 0, 0);
					rightInAnimation.setDuration(500);
					mImageSwitcher.setInAnimation(rightInAnimation);
					TranslateAnimation leftOutAnimation = new TranslateAnimation(
							0, -100, 0, 0);
					leftOutAnimation.setDuration(500);
					mImageSwitcher.setOutAnimation(leftOutAnimation);
					currentPosition++;
					showNext();
				} else {
					Toast.makeText(getApplication(), "到了最后一张",
							Toast.LENGTH_SHORT).show();
				}
			}
		}

			break;
		}

		return true;
	}
}
