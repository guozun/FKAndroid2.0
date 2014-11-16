package com.blackswan.fake.activity.useractivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.style.BulletSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.blackswan.fake.R;
import com.blackswan.fake.base.BaseActivity;
import com.blackswan.fake.bean.UserInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class PersonalInfoActivity extends BaseActivity {
	private UserInfo user;
	private DisplayImageOptions options;
	private ImageView ivHead;
	private TextView name;
	private TextView sex;
	private TextView phone;
	private PopupWindow mPopupWindow;
	protected final int INTENT_NAME  = 1;
	protected final int INTENT_PHONE  = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 获取启动该Result的Intent
		Intent intent = getIntent();
		// 获取该intent所携带的数据
		Bundle data = intent.getExtras();
		// 从Bundle包中取出数据
		user = (UserInfo) data.getSerializable("personinfo");
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.bg_cover_userheader_outerpress)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		setContentView(R.layout.activity_personalinfo);
		this.initViews();

	}

	@Override
	protected void initViews() {
		findViewById(R.id.id_personalinfo_layout_alterSex).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						getPopupWindowInstance();
						LinearLayout layout = (LinearLayout) LayoutInflater
								.from(PersonalInfoActivity.this).inflate(
										R.layout.activity_personalinfo, null);
						getPopupWindowInstance();
						mPopupWindow
								.setAnimationStyle(R.style.PopupAnimationPersonalinfo);
						mPopupWindow
								.showAtLocation(
										findViewById(R.id.id_personalinfo_layout_alterSex),
										Gravity.BOTTOM, 0, 0);
						setWindowBack(0.3f);

					}
				});
		findViewById(R.id.id_personalinfo_layout_alterName).setOnClickListener(
				new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(); 
						intent.setClass(PersonalInfoActivity.this,AlterNickNameActivity.class);
						intent.putExtra("name", user.getName());
						startActivityForResult(intent, INTENT_NAME);
					}
				});
		findViewById(R.id.id_personalinfo_layout_alterPhone).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(); 
						intent.setClass(PersonalInfoActivity.this,AlterPhoneActivity.class);
						intent.putExtra("phone", user.getPhoneNumber());
						startActivityForResult(intent, INTENT_PHONE);
					}
				});
		findViewById(R.id.id_personalinfo_layout_alterHead).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
				
						//startActivity(AlterHeadActivity.class);
					}
				});
		ivHead = (ImageView) findViewById(R.id.iv_personalinfo_head);
		name = (TextView) findViewById(R.id.tv_personalinfo_name);
		sex = (TextView) findViewById(R.id.tv_personalinfo_sex);
		phone = (TextView) findViewById(R.id.tv_personalinfo_phone);

		ImageLoader.getInstance().displayImage(user.getHeadUrl(), ivHead,
				options);

		name.setText(user.getName());
		sex.setText(user.getSexLocal());
		phone.setText(user.getPhoneNumber());
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case INTENT_NAME:
			if(resultCode == Activity.RESULT_OK){
				Bundle b = data.getExtras();
				//String nn = b.getString("newName");
				String nn = b.getString("newName");
				//TODO 向服务器上传,是否变化 判断
				showLongToast("修改姓名成功");
				name.setText(nn);
				
			}
			break;

		default:
			break;
		}
	}

	/*
	 * 获取PopupWindow实例
	 */
	private void getPopupWindowInstance() {
		if (null != mPopupWindow) {
			mPopupWindow.dismiss();
			return;
		} else {
			initPopuptWindow();
		}
	}

	private void setWindowBack(float x) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = x;
		getWindow().setAttributes(lp);
	}

	/*
	 * 创建PopupWindow
	 */
	private void initPopuptWindow() {

		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View popupWindow = layoutInflater.inflate(
				R.layout.popup_window_personalinfo, null);

		mPopupWindow = new PopupWindow(popupWindow, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, true);

		mPopupWindow.setFocusable(true); // 设置PopupWindow可获得焦点
		// mPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
		mPopupWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

		mPopupWindow.setOutsideTouchable(true);
		final ImageView ivMale = (ImageView) popupWindow
				.findViewById(R.id.iv_popup_window_personallinfo_male);
		final ImageView ivFamale = (ImageView) popupWindow
				.findViewById(R.id.iv_popup_window_personallinfo_famale);
		RelativeLayout male = (RelativeLayout) popupWindow
				.findViewById(R.id.id_popup_window_personallinfo_male);
		RelativeLayout famale = (RelativeLayout) popupWindow
				.findViewById(R.id.id_popup_window_personallinfo_famale);
		RelativeLayout cancle = (RelativeLayout) popupWindow
				.findViewById(R.id.id_popup_window_personallinfo_cancle);
		popupWindow.findViewById(R.id.id_popup_window_personallinfo_view)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						mPopupWindow.dismiss();
						setWindowBack(1f);
					}
				});
		male.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setPopupWindowSex(ivMale, ivFamale, true);
				sexCommit(true);
				mPopupWindow.dismiss();
				setWindowBack(1f);
			}
		});
		famale.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setPopupWindowSex(ivMale, ivFamale, false);
				sexCommit(false);
				mPopupWindow.dismiss();
				setWindowBack(1f);
			}
		});
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
				setWindowBack(1f);
			}
		});
		setPopupWindowSex(ivMale, ivFamale, user.getSex());
		mPopupWindow.update();
	}

	// 向服务器上传修改后的性别值
	private Boolean sexCommit(Boolean s) {
		// TODO 向服务器上传修改后的性别值
		showLongToast("修改性别成功");
		user.setSex(s);

		sex.setText(user.getSexLocal());
		return true;
	}

	private void setPopupWindowSex(ImageView i1, ImageView i2, Boolean s) {
		if (s != null) {
			if (s) {
				i1.setVisibility(View.VISIBLE);
				i2.setVisibility(View.GONE);

			} else {
				i2.setVisibility(View.VISIBLE);
				i1.setVisibility(View.GONE);
			}
		}
	}

	@Override
	protected void initEvents() {

	}

	public void alterSex(View view) {

	}

	public void alterPassword(View view) {

	}

	public void upgrade(View view) {

	}

	public void about(View view) {

	}

	public void alterHead(View view) {

	}

	public void alterName(View view) {

	}

	public void alterPhone(View view) {

	}

}
