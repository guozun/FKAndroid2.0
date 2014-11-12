package com.blackswan.fake.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
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
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class HairDoDetailActivity extends BaseActivity {

	private TextView barber;
	private ImageView barberpic;
	private ViewPager mViewPager;
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
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
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
		mViewPager = (ViewPager) findViewById(R.id.id_hairdo_detail_pic);
		order = (Button) findViewById(R.id.bt_hairdo_detail_order);

		barber.setText("11111");
		order.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 进行预约操作
			}
		});
		ImageLoader
				.getInstance()
				.displayImage(
						"http://img0.bdstatic.com/img/image/shouye/mnnll-14204092064.jpg",
						barberpic, options);
		mViewPager.setAdapter(new PagerAdapter() {

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
				container.removeViewAt(position);
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				ImageView i = new ImageView(HairDoDetailActivity.this);
				ImageLoader.getInstance().displayImage(imgUrl.get(position), i,
						options);
				container.addView(i, position);
				return i;
			}
		});
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

	}

}
