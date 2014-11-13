package com.blackswan.fake.activity.useractivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.blackswan.fake.R;
import com.blackswan.fake.base.BaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class PersonalInfoActivity extends BaseActivity {
	// 图片加载配置
	private DisplayImageOptions options;
	private ImageView ivHead;
	private EditText etName;
	private EditText etPhone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.bg_cover_userheader_outerpress)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		setContentView(R.layout.activity_personalinfo);
	}

	@Override
	protected void initViews() {
		ivHead = (ImageView) findViewById(R.id.iv_personalinfo_alter_head);
		etName = (EditText)findViewById(R.id.et_personalinfo_alter_name);
		etPhone = (EditText)findViewById(R.id.et_personalinfo_alter_phone);
		ImageLoader
				.getInstance()
				.displayImage(
						"http://img0.bdstatic.com/img/image/shouye/mxlss-13058626297.jpg",
						ivHead, options);
	}

	@Override
	protected void initEvents() {

	}

	public void alterPassword(View view) {

	}

	public void upgrade(View view) {

	}

	public void about(View view) {

	}

	public void alterHead(View view) {

	}

}
