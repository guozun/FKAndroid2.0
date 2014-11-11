package com.blackswan.fake.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackswan.fake.R;
import com.blackswan.fake.base.BaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class HairDoDetailActivity extends BaseActivity {

	private TextView barber;
	private ImageView barberpic;
	private ImageView hairdopic;
	private DisplayImageOptions options;
	private Button order;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hair_do_detail);
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.homepage_location)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		this.initViews();
	}

	@Override
	protected void initViews() {
		barber = (TextView)findViewById(R.id.tv_hairdo_detail_barber);
		barberpic = (ImageView)findViewById(R.id.iv_rotat_hairdo_detail);
		hairdopic = (ImageView)findViewById(R.id.iv_hairdo_detail_pic);
		order = (Button) findViewById(R.id.bt_hairdo_detail_order);

		barber.setText("11111");
		order.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 进行预约操作
			}
		});
		ImageLoader.getInstance().displayImage(
				"http://avatar.csdn.net/F/C/3/1_heaimnmn.jpg", barberpic,
				options);
		ImageLoader
				.getInstance()
				.displayImage(
						"http://e.hiphotos.baidu.com/image/pic/item/359b033b5bb5c9eae1d75710d739b6003af3b319.jpg",
						hairdopic, options);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub

	}

}
