package com.blackswan.fake.dialog;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;

import com.blackswan.fake.R;
import com.blackswan.fake.activity.useractivity.PersonalInfoActivity;
import com.blackswan.fake.util.FileUtils;

public class AlterHeadPopup {

	private PopupWindow mPopupWindow;
	private Window win;
	private Context context;
	private View popupWindow;
	private Activity activity;

	public AlterHeadPopup(Context c, Window w) {
		win = w;
		context = c;
		activity = (Activity) c;
	}

	// public AlterHeadPopup(Context c, Window w,Activity a) {
	// win = w;
	// context = c;
	// activity = c;
	// }

	/*
	 * 获取PopupWindow实例
	 */

	private void getPopupWindowInstance() {
		if (null != mPopupWindow) {
			mPopupWindow.dismiss();
		} else {
			initPopuptWindow(context);
		}

	}

	public void showPopupWindow(View parent) {
		getPopupWindowInstance();
		mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		setWindowAlpha(0.3f);
	}

	private void setWindowAlpha(float value) {
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.alpha = value;
		win.setAttributes(lp);
	}

	/**
	 * 关闭 这个Popupwindows
	 * 
	 * @param win
	 *            调用该对话框的Activity的 Windows getWindow
	 */
	public void closeMe() {
		mPopupWindow.dismiss();
		setWindowAlpha(1f);
	}

	/**
	 * 创建PopupWindow
	 * 
	 * @param context
	 */
	private void initPopuptWindow(final Context context) {

		LayoutInflater layoutInflater = LayoutInflater.from(context);
		popupWindow = layoutInflater.inflate(R.layout.popup_window_alter_head,
				null);

		mPopupWindow = new PopupWindow(popupWindow, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT, true);

		mPopupWindow.setFocusable(true); // 设置PopupWindow可获得焦点
		mPopupWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setAnimationStyle(R.style.PopupAnimationPersonalinfo);

		popupWindow.findViewById(R.id.id_popup_window_alter_head_cancle)
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						closeMe();
					}
				});
		popupWindow.findViewById(R.id.id_popup_window_alter_head_camera)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri
								.fromFile(new File(FileUtils.getDiskCacheDir(context,PersonalInfoActivity.HEAD_PATH),
										PersonalInfoActivity.HEAD_OLD_NAME)));

						activity.startActivityForResult(intent,
								PersonalInfoActivity.CODE_CAMERA);
						closeMe();
					}
				});
		popupWindow.findViewById(R.id.id_popup_window_alter_head_photo)
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(Intent.ACTION_PICK, null);
						intent.setDataAndType(
								MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
								"image/*");

						activity.startActivityForResult(intent,
								PersonalInfoActivity.CODE_PHOTO);
						closeMe();
					}
				});
		mPopupWindow.update();
	}

	
}
